
package com.kyj.fx.commons.utils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Performs formatting of basic SQL statements (DML + query).
 *
 *
 * 2016-10-03 일부 함수 ValueUtil로 코드이동. by kyj.
 * 
 * @author Gavin King
 * @author Steve Ebersole
 */
public class SqlFormatter implements Formatter {

	public static final String WHITESPACE = " \n\r\f\t";

	private static final Set<String> BEGIN_CLAUSES = new HashSet<String>();
	private static final Set<String> END_CLAUSES = new HashSet<String>();
	private static final Set<String> LOGICAL = new HashSet<String>();
	private static final Set<String> QUANTIFIERS = new HashSet<String>();
	private static final Set<String> DML = new HashSet<String>();
	private static final Set<String> MISC = new HashSet<String>();
	private static final Set<String> BEGIN_VELOCITY = new HashSet<String>();
	private static final Set<String> END_VELOCITY = new HashSet<String>();

	private static final Set<String> FUNC_KEYWORD = new HashSet<String>();

	static {

		FUNC_KEYWORD.add("substring");

		BEGIN_CLAUSES.add("left");
		BEGIN_CLAUSES.add("right");
		BEGIN_CLAUSES.add("inner");
		BEGIN_CLAUSES.add("outer");
		BEGIN_CLAUSES.add("group");
		BEGIN_CLAUSES.add("order");

		END_CLAUSES.add("where");
		END_CLAUSES.add("set");
		END_CLAUSES.add("having");
		END_CLAUSES.add("join");
		END_CLAUSES.add("from");
		END_CLAUSES.add("by");
		END_CLAUSES.add("join");
		END_CLAUSES.add("into");
		END_CLAUSES.add("union");

		LOGICAL.add("and");
		LOGICAL.add("or");
		LOGICAL.add("when");
		LOGICAL.add("else");
		LOGICAL.add("end");

		BEGIN_VELOCITY.add("#if");

		END_VELOCITY.add("#end");
		// LOGICAL.add("#if");
		// LOGICAL.add("#end");

		QUANTIFIERS.add("in");
		QUANTIFIERS.add("all");
		QUANTIFIERS.add("exists");
		QUANTIFIERS.add("some");
		QUANTIFIERS.add("any");

		DML.add("insert");
		DML.add("update");
		DML.add("delete");

		MISC.add("select");
		MISC.add("on");
	}

	static final String indentString = "    ";
	static final String initial = "\n";

	public String format(String source) {
		return new FormatProcess(source).perform();
	}

	private static class FormatProcess {
		boolean beginLine = true;
		boolean afterBeginBeforeEnd = false;
		boolean afterByOrSetOrFromOrSelect = false;
		boolean afterOn = false;
		boolean afterBetween = false;
		boolean afterInsert = false;
		boolean afterWhite = false;
		int inFunction = 0;
		int parensSinceSelect = 0;
		// 17.11.22 DBMS Function
		boolean isDbmsFunctionScope = false;
		private LinkedList<Integer> parenCounts = new LinkedList<Integer>();
		private LinkedList<Boolean> afterByOrFromOrSelects = new LinkedList<Boolean>();
		int indent = 0;

		StringBuffer result = new StringBuffer();
		StringTokenizer tokens;
		String lastToken;
		String token;
		String lcToken;

		public FormatProcess(String sql) {
			tokens = new StringTokenizer(sql, "()+=<>'`\"[],/*-;" + WHITESPACE, true);
		}

		public String perform() {

			while (tokens.hasMoreTokens()) {
				token = tokens.nextToken();
				lcToken = token.toLowerCase();

				if ("'".equals(token)) {
					String t;
					do {
						t = tokens.nextToken();
						token += t;
					} while (!"'".equals(t) && tokens.hasMoreTokens());
				} else if ("\"".equals(token)) {
					String t;
					do {

						/*
						 * by kyj. if occured error than system can not be
						 * controed. so the try ~ catch block will be prevent
						 * the crush.
						 */
						try {
							t = tokens.nextToken();
							token += t;
						} catch (NoSuchElementException e) {
							// token += "";
							break;
						}
					} while (!"\"".equals(t));

				}

				// 주석 제거
				if ("/".equals(token)) {
					String t;
					t = tokens.nextToken();
					token += t;
					if ("*".equals(t)) {
						do {
							t = tokens.nextToken();
							token += t;
						} while (!token.endsWith("*/"));
						token += "\n";
					}
					out();
				}
				// 주석 제거
				else if ("-".equals(token)) {
					String t;
					t = tokens.nextToken();
					token += t;
					do {
						t = tokens.nextToken();
						token += t;
					} while (!token.endsWith("\n"));
					out();
				} else if (afterByOrSetOrFromOrSelect && ",".equals(token)) {
					commaAfterByOrFromOrSelect();
				} else if (afterOn && ",".equals(token)) {
					commaAfterOn();
				}

				else if (BEGIN_VELOCITY.contains(
						token) /* && BEGIN_VELOCITY.contains(lastToken) */) {
					beginNewVelocity();
				} else if (END_VELOCITY.contains(lcToken)) {
					endNewVelocity();
				}

				else if ("(".equals(token)) {
					// if(!VELOCITY.contains(lastToken))
					openParen();
				} else if (")".equals(token)) {
					// if(!VELOCITY.contains(lastToken))
					closeParen();
				}

				else if (BEGIN_CLAUSES.contains(lcToken)) {
					beginNewClause();
				} else if (END_CLAUSES.contains(lcToken)) {
					endNewClause();
				} else if ("select".equals(lcToken)) {
					select();
				}

				else if (DML.contains(lcToken)) {
					updateOrInsertOrDelete();
				}

				else if ("values".equals(lcToken)) {
					values();
				}

				else if ("on".equals(lcToken)) {
					on();
				}

				else if (afterBetween && lcToken.equals("and")) {
					misc();
					afterBetween = false;
				}

				else if (LOGICAL.contains(lcToken)) {
					logical();
				}

				else if (isWhitespace(token)) {
					white();
				} else if (";".equals(lcToken)) {
					reset();
				}

				else {
					misc();
				}

				if (!isWhitespace(token)) {
					lastToken = lcToken;
				}

			}
			return result.toString();
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2016. 9. 23.
		 */
		private void beginNewVelocity() {
			inFunction++;
			// beginLine = true;
			newline();
			out();
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2016. 9. 23.
		 */
		private void endNewVelocity() {
			// beginLine = false;
			newline();
			out();
			// newline();
			// if (!afterBeginBeforeEnd) {
			// indent--;
			// if (afterOn) {
			// indent--;
			// afterOn = false;
			// }
			// newline();
			// }
			// out();
			// if (!"union".equals(lcToken)) {
			// indent++;
			// }
			// newline();
			// afterBeginBeforeEnd = false;
			// afterByOrSetOrFromOrSelect = "by".equals(lcToken) ||
			// "set".equals(lcToken) || "from".equals(lcToken);
		}

		private void commaAfterOn() {

			out();
			indent--;
			newline();
			afterOn = false;
			afterByOrSetOrFromOrSelect = true;
		}

		private void commaAfterByOrFromOrSelect() {

			out();

			if (isDbmsFunctionScope) {
				return;
			}

			newline();
		}

		private void logical() {

			if ("end".equals(lcToken)) {
				indent--;
			}
			newline();
			out();

			beginLine = false;
		}

		private void on() {

			indent++;
			afterOn = true;
			newline();
			out();
			beginLine = false;
		}

		private void misc() {

			out();
			if ("between".equals(lcToken)) {
				afterBetween = true;
			}
			if (afterInsert) {
				newline();
				afterInsert = false;
			} else {
				beginLine = false;
				if ("case".equals(lcToken)) {
					indent++;
				}
			}
		}

		private void white() {

			if (!beginLine && !afterWhite) {
				result.append(" ");
				afterWhite = true;
			}
		}

		private void updateOrInsertOrDelete() {

			out();
			indent++;
			beginLine = false;
			if ("update".equals(lcToken)) {
				newline();
			}
			if ("insert".equals(lcToken)) {
				afterInsert = true;
			}
		}

		private void select() {

			out();
			// newline();
			indent++;
			newline();
			parenCounts.addLast(Integer.valueOf(parensSinceSelect));
			afterByOrFromOrSelects.addLast(Boolean.valueOf(afterByOrSetOrFromOrSelect));
			parensSinceSelect = 0;
			afterByOrSetOrFromOrSelect = true;
		}

		private void out() {

			result.append(token);
			afterWhite = false;
		}

		private void endNewClause() {
			if (!afterBeginBeforeEnd) {
				indent--;
				if (afterOn) {
					indent--;
					afterOn = false;
				}
				newline();
			}
			out();
			if (!"union".equals(lcToken)) {
				indent++;
			}
			newline();
			afterBeginBeforeEnd = false;
			afterByOrSetOrFromOrSelect = "by".equals(lcToken) || "set".equals(lcToken) || "from".equals(lcToken);
		}

		private void beginNewClause() {

			if (!afterBeginBeforeEnd) {
				if (afterOn) {
					indent--;
					afterOn = false;
				}
				indent--;
				newline();
			}
			out();
			beginLine = false;
			afterBeginBeforeEnd = true;
		}

		private void values() {

			indent--;
			newline();
			out();
			indent++;
			newline();
		}

		private void closeParen() {

			parensSinceSelect--;
			if (parensSinceSelect < 0) {
				indent--;
				parensSinceSelect = parenCounts.removeLast().intValue();
				afterByOrSetOrFromOrSelect = afterByOrFromOrSelects.removeLast().booleanValue();
			}

			if (isDbmsFunctionScope) {
				isDbmsFunctionScope = false;
			}

			if (inFunction > 0) {
				inFunction--;
				out();
			} else {
				if (!afterByOrSetOrFromOrSelect) {
					indent--;
					newline();
				}
				out();
			}
			beginLine = false;
		}

		private void openParen() {

			if (isFunctionName(lastToken) || inFunction > 0) {
				inFunction++;
			}

			if (dbmsFunction(lastToken)) {
				isDbmsFunctionScope = true;
				inFunction++;
			}

			beginLine = false;
			if (inFunction > 0) {
				out();
			} else {
				out();
				if (!afterByOrSetOrFromOrSelect) {
					indent++;
					newline();
					beginLine = true;
				}
			}
			parensSinceSelect++;
		}

		/**
		 * is DBMS Function
		 * 
		 * @작성자 : KYJ
		 * @작성일 : 2017. 11. 22.
		 * @param token
		 * @return
		 */
		private boolean dbmsFunction(String token) {
			return FUNC_KEYWORD.contains(token);
		}

		private void reset() {
			out();
			beginLine = true;
			afterBeginBeforeEnd = false;
			afterByOrSetOrFromOrSelect = false;
			afterOn = false;
			afterBetween = false;
			afterInsert = false;
			inFunction = 0;
			parensSinceSelect = 0;
			parenCounts.clear();
			afterByOrFromOrSelects.clear();
			indent = 0;
			result.append("\n\n\n");
		}

		private static boolean isFunctionName(String tok) {
			final char begin = tok.charAt(0);
			final boolean isIdentifier = Character.isJavaIdentifierStart(begin) || '"' == begin;
			return isIdentifier && !LOGICAL.contains(tok) && !END_CLAUSES.contains(tok) && !QUANTIFIERS.contains(tok) && !DML.contains(tok)
					&& !MISC.contains(tok);
		}

		private static boolean isWhitespace(String token) {
			return WHITESPACE.indexOf(token) >= 0;
		}

		private void newline() {
			result.append("\n");
			for (int i = 0; i < indent; i++) {
				result.append(indentString);
			}
			beginLine = true;
		}
	}

	@Override
	public String toUpperCase(String sql) {
		return toUpperLowerCase(sql, true);
	}

	@Override
	public String toLowerCase(String sql) {
		return toUpperLowerCase(sql, false);
	}

	private String toUpperLowerCase(String sql, boolean isUpper) {
		return ValueUtil.toUpperLowerCase(sql, isUpper);
	}

	@Override
	public String split(String sql, int caretPosition) {
		StringBuffer result = new StringBuffer();
		String[] split = sql.split("\n\n");
		int textLength = 0;
		for (String str : split) {
			if (ValueUtil.isEmpty(str.trim())) {
				result.append(str);

			} else {
				result.setLength(0);
				result.append(str);
			}
			textLength += str.length();
			textLength += 2;

			if (textLength >= caretPosition) {
				break;
			}
		}

		return result.toString();
	}
}