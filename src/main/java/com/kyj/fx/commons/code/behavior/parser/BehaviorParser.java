/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.parser
 *	작성일   : 2018. 5. 8.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.parser;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KYJ
 *
 */
public class BehaviorParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorParser.class);

	public char[] basicTokens = { '\t', '\n', ' ', '\0' };
	private String code;
	private String prevToken;
	private String currentToken;
	private final int len;

	private boolean isCommentLine;

	private boolean isText;

	private char prevC;

	public BehaviorParser(String code) {
		/*
		 * 중요 텍스트 마지막에 공백을 추가함. 파서의 마지막 토큰을 인지할 수 있는 역할을 수행함.
		 */
		this.code = code.concat(" ");
		len = this.code.length();
	}

	private AbstractBehaviorVisitor classVisitor = new BehaviorClassVisitor();
	private BehaviorFunctionVisitor methodVisitor = new BehaviorFunctionVisitor();
	private BehaviorSubProcedureVisitor subVisitor = new BehaviorSubProcedureVisitor();

	private Stack<BehaviorClass> vbClassStack = new Stack<>();

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 10.
	 * @return
	 */
	final String getCode() {
		return this.code;
	}

	public void parse() {

		int startToken = 0;
		int endToken = -1;
		for (int idx = 1; idx < len; idx++) {

			prevC = code.charAt(idx - 1);
			char c = code.charAt(idx); // charArray[idx];

			if (c == '"' && !isText)
				isText = true;
			else if (c == '"' && isText)
				isText = false;
			else if (c != '"' && isText)
				continue;

			// if( idx >= 45931 )
			// System.err.println(c);
			if (isTokenSeparator(c)) {

				if (startToken == idx) {
					continue;
				}
				endToken = idx;

				prevToken = currentToken;
				currentToken = code.substring(startToken, endToken);
				// trim
				startToken = left(currentToken, startToken);
				endToken = right(currentToken, endToken);

				currentToken = code.substring(startToken, endToken);

				// 코멘트에 Function등 키워드가 포함되면 파싱 대상에서 제외하기 위한 처리 진행.

				if (isContainsCommentToken(currentToken)) {
					isCommentLine = true;
					int commentEndIdx = findCommentEndIdx(idx);
					endToken = commentEndIdx;
					idx = commentEndIdx;
					currentToken = code.substring(startToken, endToken);
				}

//				LOGGER.debug("isCommentLine :{} StartIdx :{} , EndIdx :{} , CurrentToken :{} ", isCommentLine, startToken, endToken,
//						currentToken);
				if (!isCommentLine) {

					String prev = prevToken == null ? "" :prevToken.toUpperCase();
					switch (currentToken.toUpperCase()) {
					case "FUNCTION":
						// visitor = methodVisitor;
						// if("EXIT".equals(anObject))

						if ("EXIT".equals(prev)) {
							// Nothing...
						} else if ("END".equals(prev)) {
							// end function

							methodVisitor.setEndIdx(endToken);
							methodVisitor.visite(code.substring(methodVisitor.getStartIdx(), methodVisitor.getEndIdx()));
							methodVisitor.addCount();

							

						} else {
							// start function
							methodVisitor.setStartIdx(startToken);
							if (!vbClassStack.isEmpty()) {
								BehaviorClass peek = vbClassStack.peek();
								methodVisitor.setVbClass(peek);
							}
						}

						break;

					case "SUB":

						if ("EXIT".equals(prev)) {
							// Nothing...
						} else if ("END".equals(prev)) {
							// end function

							subVisitor.setEndIdx(endToken);
							subVisitor.visite(code.substring(subVisitor.getStartIdx(), subVisitor.getEndIdx()));
							subVisitor.addCount();

							

						} else {
							// start function
							subVisitor.setStartIdx(startToken);
							if (!vbClassStack.isEmpty()) {
								BehaviorClass peek = vbClassStack.peek();
								subVisitor.setVbClass(peek);
							}
						}

						break;
					case "CLASS":

						if ("EXIT".equals(prev)) {
							// Nothing...
							vbClassStack.pop();
						} else if ("END".equals(prev)) {
							// end function

							classVisitor.setEndIdx(endToken);
							classVisitor.visite(code.substring(classVisitor.getStartIdx(), classVisitor.getEndIdx()));
							classVisitor.addCount();
							vbClassStack.pop();
						} else {

							String nextToken = nextToken(idx + 1);
							LOGGER.debug("class Name : {} ", nextToken);
							// start function
							classVisitor.setStartIdx(startToken);

							BehaviorClass behaviorClass = new BehaviorClass();
							behaviorClass.setName(nextToken);
							vbClassStack.push(behaviorClass);
						}

						break;

					default:
						break;
					}
				}

				startToken = endToken + 1;
				endToken = endToken + 2;

			}

			isCommentLine = false;
		}

		LOGGER.info("Class parsing error : {} ", !vbClassStack.isEmpty());
		LOGGER.info("Func Count : {} ", methodVisitor.getCount());
		LOGGER.info("Sub Count : {} ", subVisitor.getCount());
	}

	private int right(String currentToken, int endToken) {
		int delta = 0;
		for (int i = currentToken.length() - 1; i > 0; i--) {

			if (currentToken.charAt(i) == ' ' || currentToken.charAt(i) == '\n' || currentToken.charAt(i) == '\t') {
				delta++;
				continue;
			}

			return endToken - delta;
		}
		return endToken;
	}

	private int left(String currentToken, int startToken) {
		for (int i = 0, len = currentToken.length(); i < len; i++) {
			if (currentToken.charAt(i) == ' ' || currentToken.charAt(i) == '\n' || currentToken.charAt(i) == '\t')
				continue;
			return startToken + i;
		}
		return startToken;
	}

	private int findCommentEndIdx(int curIdx) {

		for (int i = curIdx; i < len; i++) {
			if (code.charAt(i) == '\n')
				return i;
		}
		return -1;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 9.
	 * @param currentIdx
	 * @return
	 */
	private String nextToken(int currentIdx) {
		for (int idx = currentIdx, max = len; idx < max; idx++) {
			char c = code.charAt(idx); // charArray[idx];
			if (isTokenSeparator(c)) {
				return code.substring(currentIdx, idx);
			}
		}
		return null;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 9.
	 * @param c
	 * @return
	 */
	private boolean isNewLine(char c) {

		if (c == '\n')
			return true;
		return false;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 9.
	 * @param token
	 * @return
	 */
	private boolean isContainsCommentToken(String token) {
		return token.startsWith("'");
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 9.
	 * @param c
	 * @return
	 */
	private boolean isTokenSeparator(char c) {
		int len = basicTokens.length;
		for (int i = 0, l = len; i < l; i++) {
			if (c == basicTokens[i])
				return true;
		}

		return false;
	}

	/**
	 * @return the methodVisitor
	 */
	public final AbstractBehaviorVisitor getMethodVisitor() {
		return methodVisitor;
	}

	/**
	 * @param methodVisitor
	 *            the methodVisitor to set
	 */
	public final void setMethodVisitor(BehaviorFunctionVisitor methodVisitor) {
		this.methodVisitor = methodVisitor;
		this.subVisitor.setParser(this);
	}

	/**
	 * @return the subVisitor
	 */
	public final AbstractBehaviorVisitor getSubVisitor() {
		return subVisitor;
	}

	/**
	 * @param subVisitor
	 *            the subVisitor to set
	 */
	public final void setSubVisitor(BehaviorSubProcedureVisitor subVisitor) {
		this.subVisitor = subVisitor;
		this.subVisitor.setParser(this);
	}

	/**
	 * @return the classVisitor
	 */
	public AbstractBehaviorVisitor getClassVisitor() {
		return classVisitor;
	}

	/**
	 * @param classVisitor
	 *            the classVisitor to set
	 */
	public void setClassVisitor(AbstractBehaviorVisitor classVisitor) {
		this.classVisitor = classVisitor;
		this.classVisitor.setParser(this);
	}

}
