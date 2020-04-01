/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kyj.fx.commons.exceptions.NotYetSupportException;
import com.kyj.fx.commons.memory.ConfigResourceLoader;


/**
 * @author KYJ
 *
 */
public class ValueUtil {

	/**
	 * Email Check REG Expression.
	 * 
	 * @최초생성일 2017. 10. 16.
	 */
	public static final String EMAIL_VALIDATION_EXP = "[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+";

	public static String rightTrim(String text) {
		return text.replaceAll("\\s+$", "");
	}

	public static String leftTrim(String text) {
		return leftReplace(text, "");
	}

	public static String leftReplace(String text, String replaceText) {
		return text.replaceAll("^\\s+", replaceText);
	}

	public static String toString(Throwable e) {
		return toString(null, e);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 3. 28.
	 * @param stackTrace
	 * @return
	 */
	public static String toString(StackTraceElement[] stackTrace) {
		return toString(null, stackTrace);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 9. 28.
	 * @param message
	 * @param stackTrace
	 * @return
	 */
	public static String toString(String message, StackTraceElement[] stackTrace) {
		StringBuffer sb = new StringBuffer();
		if (ValueUtil.isNotEmpty(message))
			sb.append(message).append(System.lineSeparator());
		if (stackTrace != null) {
			for (StackTraceElement st : stackTrace) {
				sb.append(st.toString()).append(System.lineSeparator());
			}
		}

		return sb.toString();

	}

	/**
	 * 에러 메세지 상세화
	 *
	 * @param title
	 *            메세지 타이틀
	 * @param e
	 * @return
	 */
	public static String toString(String title, Throwable e) {
		if (e == null)
			return "[warnning] Exception is null";

		String errMsg = "";
		try (StringBuilderWriter sbw = new StringBuilderWriter()) {
			try (PrintWriter printWriter = new PrintWriter(sbw, true)) {
				if (title != null)
					printWriter.write("#############  ".concat(title).concat("  ##############\n"));
				e.printStackTrace(printWriter);
			}
			errMsg = sbw.toString();
		}
		return errMsg;
	}

	public static boolean isNotEmpty(Object obj) {
		boolean flag = true;
		if (obj != null) {
			if (obj instanceof String) {
				String valueOf = obj.toString().trim();
				flag = valueOf.length() > 0 && !valueOf.equals("")  && !valueOf.equals("null");
			} else if (obj instanceof Collection) {
				Collection<?> list = (Collection<?>) obj;
				flag = !list.isEmpty();

				// flag = list.size() > 0;
			} else if (obj instanceof Map) {

				Map<?, ?> map = (Map<?, ?>) obj;
				flag = map.size() > 0;
			}
		} else {
			flag = false;
		}
		return flag;

	}

	public static boolean isEmpty(Object obj) {
		return !isNotEmpty(obj);
	}

	/**
	 * 배열요소중 하나라도 비어있는경우 true
	 *
	 * @param objs
	 * @return
	 */
	public static boolean isEmpty(Object... objs) {
		for (Object obj : objs) {
			if (isEmpty(obj)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * str 문자열에서 index에 해당하는 부분만 소문자로 고친다.
	 *
	 * @param str
	 * @param index
	 * @return
	 */
	public static String getIndexLowercase(String str, int index) {
		return getIndexcase(str, index, IndexCaseTypes.LOWERCASE);
	}

	public enum IndexCaseTypes {
		UPPERCASE, LOWERCASE
	}

	/**
	 * str 문자열에서 index에 해당하는 부분만 대문자, 혹은 소문자로 고친다. (IndexCaseTypes에 따른 설정)
	 *
	 * @param str
	 *            target
	 * @param index
	 *            character index
	 * @param type
	 *            lower or upper
	 * @return
	 */
	public static String getIndexcase(String str, int index, IndexCaseTypes type) {
		StringBuffer sb = new StringBuffer();

		// DEFAULT UPPERCASE
		char indexChar = Character.toUpperCase(str.charAt(index));
		if (type == IndexCaseTypes.LOWERCASE) {
			indexChar = Character.toLowerCase(str.charAt(index));
		}

		switch (index) {
		case 0:
			sb.append(indexChar).append(str.substring(index + 1));
			break;
		default:
			sb.append(str.substring(0, index)).append(indexChar).append(str.substring(index + 1));
			break;
		}
		return sb.toString();
	}

	/**
	 * Double형으로 텍스트 파싱후 값 비교. 만약 숫자값이 아닌경우 문자열 비교
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 19.
	 * @param text
	 * @param text2
	 * @return
	 */
	public static int compareToNumberString(String text, String text2) {
		if (text == null && text2 == null)
			return 0;
		else if (text == null)
			return 1;
		else if (text2 == null)
			return -1;

		boolean failed = false;
		double a = -1d;
		try {
			a = Double.parseDouble(text);
		} catch (NumberFormatException e) {
			failed = true;
		}

		double b = -1d;
		try {
			b = Double.parseDouble(text2);
		} catch (NumberFormatException e) {
			failed = true;
		}

		if (failed)
			return text.compareTo(text2);

		return Double.compare(a, b);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 1.
	 * @param inputStream
	 *            stream
	 * @param charset
	 *            encoding
	 * @return
	 * @throws IOException
	 */
	public static String toString(InputStream inputStream, Charset charset) {
		try {
			return IOUtils.toString(inputStream, charset);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @param inputStream
	 * @return
	 */
	public static byte[] toByte(InputStream inputStream) {
		try {
			return IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String toString(InputStream inputStream) {
		return toString(inputStream, Charset.forName("UTF-8"));
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 1. 18.
	 * @param text
	 * @param text2
	 * @return
	 */
	public static int compare(String text, String text2) {
		if (text == null && text2 == null)
			return 0;
		else if (text == null)
			return 1;
		else if (text2 == null)
			return -1;
		return text.compareTo(text2);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 20.
	 * @param text
	 * @param text2
	 * @return
	 */
	public static int compareIntString(String text, String text2) {
		if (text == null && text2 == null)
			return 0;
		else if (ValueUtil.isEmpty(text))
			return 1;
		else if (ValueUtil.isEmpty(text2))
			return -1;

		int a1 = Integer.parseInt(text);
		int a2 = Integer.parseInt(text2);
		return Integer.compare(a1, a2);
	}

	static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"|\'([^\"\\\\]|\\\\.)*\'";
	static final String COMMENT_PATTERN = "(?:/\\*[^;]*?\\*/)|(?:--[^\\n]*)";

	/**
	 * Velocity 문법이 포함된 텍스트인지 리턴.
	 *
	 * @param sql
	 * @return
	 */
	public static boolean isVelocityContext(String sql) {
		if (sql == null || sql.isEmpty())
			return false;

		String _sql = sql;
		// 주석에 대해당하는 문자들을 제거
		_sql = _sql.replaceAll(COMMENT_PATTERN, "");
		String pattern = "\\$\\w+";

		Pattern compile = Pattern.compile(pattern);
		Matcher matcher = compile.matcher(_sql);
		return matcher.find();
	}

	/**
	 * Velocity Key값 목록을 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 21.
	 * @param dynamicSql
	 * @return
	 */
	public static List<String> getVelocityKeys(String _dynamicSql) {
		String dynamicSql = _dynamicSql;
		// 주석에 대해당하는 문자들을 제거
		dynamicSql = dynamicSql.replaceAll(COMMENT_PATTERN, "");
		String pattern = "\\$\\w+|:\\w+";
		// 맨앞의 특수문자는 제거.
		return regexMatchs(pattern, dynamicSql, param -> {
			return param.substring(1);
		});
	}

	/**
	 * 정규식으로 일치하는 패턴하나 반환
	 *
	 * @param string
	 * @param tempName
	 * @return
	 */
	public static String regexMatch(String regex, String value) {
		return regexMatch(regex, value, str -> str);
	}

	/**
	 * 정규식으로 일치하는 패턴하나 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 1.
	 * @param regex
	 * @param value
	 * @param convert
	 *            정규식으로 찾아낸 문자열을 변환처리
	 * @return
	 */
	public static String regexMatch(String regex, String value, Function<String, String> convert) {
		return regexMatch(regex, value, -1, convert);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 11. 27.
	 * @param regex
	 * @param value
	 * @param groupIdx
	 * @param convert
	 * @return
	 */
	public static String regexMatch(String regex, String value, int groupIdx, Function<String, String> convert) {
		Pattern compile = Pattern.compile(regex);
		Matcher matcher = compile.matcher(value);
		// 패턴에 일치하는 문자가 없을때까지 반복한다.
		if (matcher.find()) {
			String group = null;
			if (groupIdx == -1) {
				group = matcher.group();
			} else {
				group = matcher.group(groupIdx);
			}

			if (convert != null)
				return convert.apply(group);
			return group;
		}
		if (convert != null)
			return convert.apply(null);
		return null;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 11. 27.
	 * @param regex
	 * @param value
	 * @param groupIdx
	 * @param convert
	 * @return
	 */
	public static String regexMatch(String regex, String value, int groupIdx, Match convert) {
		Pattern compile = Pattern.compile(regex);
		Matcher matcher = compile.matcher(value);
		// 패턴에 일치하는 문자가 없을때까지 반복한다.
		if (matcher.find()) {
			String group = matcher.group(groupIdx);
			int start = matcher.start(groupIdx);
			int end = matcher.end(groupIdx);

			convert.setStart(start);
			convert.setEnd(end);
			
			return convert.apply(group);
		}
		if (convert != null)
			return convert.apply(null);
		return null;
	}

	/**
	 * @author KYJ (callakrsos@naver.com)
	 *
	 * @param <T>
	 */
	public static abstract class Match implements Function<String, String> {

		private int start;
		private int end;

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2018. 11. 27.
		 * @param start
		 * @param end
		 * @param text
		 * @return
		 */
		public abstract String apply(String text);

		/**
		 * @return the start
		 */
		public int getStart() {
			return start;
		}

		/**
		 * @param start
		 *            the start to set
		 */
		public void setStart(int start) {
			this.start = start;
		}

		/**
		 * @return the end
		 */
		public int getEnd() {
			return end;
		}

		/**
		 * @param end
		 *            the end to set
		 */
		public void setEnd(int end) {
			this.end = end;
		}

	}

	/**
	 * 정규식으로 일치하는 패턴 데이터 목록 반환
	 *
	 * @param regex
	 * @param value
	 * @return
	 */
	public static List<String> regexMatchs(String regex, String value) {
		return regexMatchs(regex, value, null);
	}

	/**
	 * 정규식으로 일치하는 패턴 데이터 목록 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 21.
	 * @param regex
	 * @param value
	 * @param convert
	 *            찾은 문자열이 다른형태의 문자로 다시 변환의 필요가 있는경우 사용
	 * @return
	 */
	public static List<String> regexMatchs(String regex, String value, Function<String, String> convert) {

		Pattern compile = Pattern.compile(regex);

		Matcher matcher = compile.matcher(value);
		List<String> resultList = new ArrayList<String>();

		// 패턴에 일치하는 문자가 없을때까지 반복한다.
		while (matcher.find()) {
			String text = matcher.group();
			if (convert != null)
				text = convert.apply(text);

			resultList.add(text);
		}
		return resultList;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 2.
	 * @param regex
	 * @param value
	 * @param replacedText
	 * @return
	 */
	public static String regexReplaceMatchs(String regex, String value, Function<String, String> replacedText) {

		Pattern compile = Pattern.compile(regex);

		StringBuffer sb = new StringBuffer(value);
		Matcher matcher = compile.matcher(value);

		/*
		 * 18.02.05 문자열 치환 버그 수정 <br/> from [[ start = start + end ]] <br/> to
		 * [[ start = start + replaceText.length(); ]] 로 수정 <br/>
		 * 
		 * <br/>
		 * 
		 * 18.02.02 <br/> 중요한 버그 수정. replace되는 값에도 :value 패턴이 존재하면 <br/> 에러가
		 * 발생함. start와 end 인덱스를 유지하여 치환된 텍스트는 검색대상에서 제외하는 로직 추가. <br/>
		 */
		int start = 0;
		int end = 0;
		// 패턴에 일치하는 문자가 없을때까지 반복한다.
		while (matcher.find(start)) {
			start = matcher.start();
			end = matcher.end();
			String replaceText = replacedText.apply(sb.substring(start, end));
			sb.replace(start, end, replaceText);
			matcher = compile.matcher(sb.toString());

			start = start + replaceText.length();
			end = -1;
		}
		return sb.toString();
	}

	/**
	 * 패턴에 일치하는 텍스트를 replace한후 반환 <br/>
	 * 매치되는 텍스트가 없을때까지 반복. <br/>
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 1. 22.
	 * @param regex
	 * @param value
	 * @param replacedText
	 * @return
	 */
	public static String regexReplaceMatchs(String regex, String value, String replacedText) {

		Pattern compile = Pattern.compile(regex);

		StringBuffer sb = new StringBuffer(value);
		Matcher matcher = compile.matcher(value);
		// 패턴에 일치하는 문자가 없을때까지 반복한다.
		while (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();
			sb.replace(start, end, replacedText);
			matcher = compile.matcher(sb.toString());
		}
		return sb.toString();
	}

	/**
	 * 패턴에 일치하는 텍스트를 replace한후 반환 <br/>
	 * 매치되는 텍스트 한번만 처리. <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 27.
	 * @param regex
	 * @param value
	 * @param replacedText
	 * @return
	 */
	public static String regexReplaceMatch(String regex, String value, String replacedText) {

		Pattern compile = Pattern.compile(regex);

		StringBuffer sb = new StringBuffer(value);
		Matcher matcher = compile.matcher(value);
		// 패턴에 일치하는 문자가 없을때까지 반복한다.
		if (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();
			sb.replace(start, end, replacedText);
			matcher = compile.matcher(sb.toString());
		}
		return sb.toString();
	}

	public static String getVelocityToText(String dynamicSql, String key, Object value) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put(key, value);
		return getVelocityToText(dynamicSql, hashMap, false, null);
	}

	/**
	 * Velocity문법의 텍스트를 맵핑된 텍스트결과값으로 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 21.
	 * @param dynamicSql
	 * @param paramMap
	 * @return
	 */
	public static String getVelocityToText(String dynamicSql, Map<String, Object> paramMap) {
		return getVelocityToText(dynamicSql, paramMap, false, null);
	}

	public static String getVelocityToText(String dynamicSql, Map<String, Object> paramMap, boolean replaceNamedValue) {
		return getVelocityToText(dynamicSql, paramMap, replaceNamedValue, null);
	}

	/**
	 * Velocity문법의 텍스트를 맵핑된 텍스트결과값으로 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 22.
	 * @param dynamicSql
	 * @param paramMap
	 * @param replaceNamedValue
	 *            namedParameter값을 바인드 변수로 사용하여 보여줄지 유무
	 * @param customReplaceFormat
	 *            변환할 문자열을 커스텀한 포멧으로 리턴받을 수 있는 기능을 제공하기 위한 파라미터
	 * @return
	 */
	public static String getVelocityToText(String dynamicSql, Map<String, Object> paramMap, boolean replaceNamedValue,
			Context velocityContext, Function<String, String> customReplaceFormat) {
		StringWriter writer = new StringWriter();
		VelocityContext context = new VelocityContext(paramMap, new DateFormatVelocityContextExtension(velocityContext));

		String _dynamicSql = dynamicSql;

		Velocity.evaluate(context, writer, "DaoWizard", _dynamicSql);
		String convetedString = writer.toString();
		if (replaceNamedValue) {
			convetedString = replace(convetedString, paramMap, customReplaceFormat);
		}
		return convetedString.trim();
	}

	/**
	 * Velocity문법의 텍스트를 맵핑된 텍스트결과값으로 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 22.
	 * @param dynamicSql
	 * @param paramMap
	 * @param replaceNamedValue
	 *            namedParameter값을 바인드 변수로 사용하여 보여줄지 유무
	 * @return
	 */
	public static String getVelocityToText(String dynamicSql, Map<String, Object> paramMap, boolean replaceNamedValue,
			Context velocityContext) {
		return getVelocityToText(dynamicSql, paramMap, replaceNamedValue, velocityContext, str -> String.format("'%s'", str));
	}

	private static String replace(String sql, Map<String, Object> paramMap) {
		return replace(sql, paramMap, str -> String.format("'%s'", str));
	}

	private static String replace(String sql, Map<String, Object> paramMap, Function<String, String> customFormat) {
		if (sql == null || sql.trim().isEmpty())
			return sql;

		// String specialCharacter = getDynamicValueSpecialCharacter();
		String _sql = sql.replaceAll(COMMENT_PATTERN, "");
		String pattern = ":\\w+";

		String result = regexReplaceMatchs(pattern, _sql, v -> {
			String replace = v.replaceAll(":", "");
			Object object = paramMap.get(replace);
			String string = object.toString();
			if (object instanceof List) {
				StringBuffer sb = new StringBuffer();
				List<Object> items = (List<Object>) object;
				for (Object o : items) {
					if (ValueUtil.isNotEmpty(o)) {
						sb.append(String.format("'%s',", o.toString()));
					}
				}
				int length = sb.length();
				if (length != 0) {
					sb.setLength(length - 1);
				}
				return sb.toString();
			}
			// 2016-11-01 custom 포멧 제공
			return customFormat.apply(string); // String.format("'%s'", string);
		});
		return result;
	}

	/**
	 * 프로그램의 실행 위치를 리턴함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 9.
	 * @return
	 */
	public static String getBaseDir() {
		return System.getProperty("user.dir");
	}

	/**
	 *
	 * // 16.09.01 >> 쿼리로 부터 테이블을 찾아옴 퍼옴 by Hong
	 *
	 * @param sql
	 * @return
	 */
	public static String getTableNames(String sql) {

		String concatedTables = sql.toUpperCase().replaceAll("Ｆ|Ｗ|Ｃ", " ").replaceAll("(\\r\\n|\\r|\\n)", " ").replaceAll("/[^/]*/", " ")
				.replaceAll("'[^']*'", " ").replaceAll("\\(", " (").replaceAll("\\)", ") ").replaceAll(" FROM ", " Ｆ ")
				.replaceAll("INSERT.*INTO", " Ｆ ").replaceAll("SELECT", " Ｗ ") //
				.replaceAll("UPDATE ", " Ｆ ").replaceAll(" TABLE", " Ｃ ")
				.replaceAll(
						" SET | UNION | WHERE |GROUP BY | HAVING | CONNECT BY | START WITH | MODEL | SAMPLE( )*\\(|USING( )*\\(| ON|\\)|$",
						" Ｗ ")
				.replaceAll("Ｆ([^Ｆ|Ｗ]+)Ｗ|.", "Ｃ$1").replaceAll("( FULL| LEFT| RIGHT| CROSS| NATURAL| INNER)?( OUTER)? JOIN ", "Ｃ")
				.replaceAll("Ｃ{1,}", "Ｃ").replaceAll("\\([^Ｃ]+Ｃ", "").replaceAll("Ｃ", ",");

		return concatedTables;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 19.
	 * @param obj
	 * @return
	 */
	public static String toJSONString(Object obj) {
		Gson gson = new Gson();

		String fromJson = gson.toJson(obj);
		return fromJson;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 19.
	 * @param obj
	 * @return
	 */
	public static JsonObject toJSONObject(Object obj) {
		String jsonString = toJSONString(obj);
		return new JsonParser().parse(jsonString).getAsJsonObject();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 19.
	 * @param str
	 * @return
	 */
	public static JSONObject toJSONObject(String str) {
		Gson gson = new Gson();
		JSONObject fromJson = gson.fromJson(str, JSONObject.class);
		return fromJson == null ? new JSONObject() : fromJson;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 4. 4.
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static JSONArray toJSONArray(String str) throws ParseException {
		JSONParser parser = new JSONParser();
		JSONArray parse = (JSONArray) parser.parse(str);
		return parse;
	}

	/**
	 * 
	 * 수정일 : 2018. 8. 3. <br/>
	 * Json Formatter 별도 클래스로 할당 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 4. 5.
	 * @param json
	 * @return
	 */
	public static String toStringPrettyFormat(String json) {
		return new JsonFormatter().format(json);
	}

	/**
	 * TODO 메세지 처리 방안 기술.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 14.
	 * @param messageId
	 * @return
	 */
	public static String getMessage(String messageId) {
		return "";
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 14.
	 * @param string
	 * @param msgFieldName
	 * @return
	 */
	public static String getMessage(String string, String msgFieldName) {
		return "";
	}

	public static String reverse(String name) {
		int length = name.length();
		char[] ca = new char[length];
		for (int i = 0; i < length; i++) {
			ca[length - 1 - i] = name.charAt(i);
		}
		return new String(ca);
	}

	/**
	 * value값이 빈값이아니면 notEmpty리턴 value값이 빈값이면 empty 리턴
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 26.
	 * @param value
	 * @param emptyThan
	 * @return
	 */
	public static Object decode(Object value, Object notEmpty, Object empty) {
		if (isEmpty(value)) {
			return empty;
		}
		return notEmpty;
	}

	public static Object decode(Object value, Object empty) {
		if (isEmpty(value)) {
			return empty;
		}
		return value;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 5. 26.
	 * @param value
	 * @param notEmptyThan
	 * @param emptyThan
	 * @return
	 */
	public static <T, R> R decode(T value, Function<T, R> notEmptyThan, Supplier<R> emptyThan) {
		if (isNotEmpty(value))
			return notEmptyThan.apply(value);
		return emptyThan.get();
	}

	/**
	 * get메소드 이름 패턴의 명에서 get을 제거하고 앞글자는 소문자로 바꾼 글자를 반환
	 *
	 * @param methodName
	 * @return
	 */
	public static String getSimpleMethodName(final String methodName) {
		String getMethodName = methodName;
		// validation
		char[] charArray = getMethodName.replaceFirst("get", "").toCharArray();
		String lowerCase = String.valueOf(charArray[0]).toLowerCase();
		charArray[0] = lowerCase.charAt(0);
		getMethodName = String.valueOf(charArray);
		return getMethodName;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 1.
	 * @param sql
	 * @return
	 */
	public static boolean isEditScript(String text) {
		String result = regexMatch("^(?i)edit", text.trim());
		if (result == null)
			return false;
		return true;
	}

	public static String tapping(String text) {
		String[] split = text.split("\n");
		if (split != null) {
			Optional<String> reduce = Stream.of(split).map(str -> "\t".concat(str)).reduce((str1, str2) -> str1.concat("\n").concat(str2));
			if (reduce.isPresent()) {
				return reduce.get();
			}
		}
		return text;
	}

	public static String reverseTapping(String text) {

		String[] split = text.split("\n");
		if (split != null) {
			Optional<String> reduce = Stream.of(split).map(str -> {

				return str.replaceAll("^(\t|[ ]{1,3})", "");

				// return str;
			}).reduce((str1, str2) -> str1.concat("\n").concat(str2));
			if (reduce.isPresent()) {
				return reduce.get();
			}
		}
		return text;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 4.
	 * @param value
	 */
	public static void ifNumberPresent(String value, Consumer<Double> action) {
		int length = value.length();
		for (int i = 0; i < length; i++)
			if (!Character.isDigit(value.charAt(i)))
				return;
		if (length == 0 ? false : true) {
			action.accept(Double.parseDouble(value));
		}

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 4.
	 * @param value
	 */
	public static boolean isNumber(String value) {
		int length = value.length();
		for (int i = 0; i < length; i++)
			if (!Character.isDigit(value.charAt(i)))
				return false;
		return length == 0 ? false : true;

	}

	/********************************
	 * 작성일 : 2016. 10. 3. 작성자 :
	 *
	 * @param sql
	 * @return
	 ********************************/

	public static String toUpperCase(String sql) {
		return toUpperLowerCase(sql, true);
	}

	/********************************
	 * 작성일 : 2016. 10. 3. 작성자 :
	 *
	 * @param sql
	 * @return
	 ********************************/
	public static String toLowerCase(String sql) {
		return toUpperLowerCase(sql, false);
	}

	/********************************
	 * 작성일 : 2016. 10. 3. 작성자 :
	 *
	 * 대소문자로 뷴류
	 *
	 * @param sql
	 * @param isUpper
	 * @return
	 ********************************/
	public static String toUpperLowerCase(String sql, boolean isUpper) {
		StringBuffer result = new StringBuffer();
		StringTokenizer tokens = new StringTokenizer(sql, "'-/*\n", true);
		String token;
		while (tokens.hasMoreTokens()) {
			token = tokens.nextToken();
			// 변수
			if ("'".equals(token)) {
				String t;
				do {
					t = tokens.nextToken();
					token += t;
				} while (!"'".equals(t) && tokens.hasMoreTokens());

				result.append(token);
			}
			// 주석 /**/
			else if ("/".equals(token)) {
				String t;
				t = tokens.nextToken();
				token += t;
				if ("*".equals(t)) {

					/*
					 * 2016-10-13 NoSuchElementException 예외처리 by kyj. 주석에 해당하는
					 * 내용은 대소문자 처리안함에 관련된 로직인데 예외에 걸림.
					 */
					try {
						do {
							t = tokens.nextToken();

							token += t;
						} while (!token.endsWith("*/"));
					} catch (NoSuchElementException e) {
					}

				}

				result.append(token);
			}
			// 주석 --
			else if ("-".equals(token)) {
				String t;
				t = tokens.nextToken();
				token += t;
				if ("-".equals(t)) {
					do {
						t = tokens.nextToken();
						token += t;
					} while (!token.endsWith("\n"));
				}
				result.append(token);
			}
			// 문자 저장
			else {
				if (isUpper) {
					result.append(token.toUpperCase());
				} else {
					result.append(token.toLowerCase());
				}
			}
		}
		return result.toString();
	}

	/**
	 * 주석 자동화 처리.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 12.
	 * @param code
	 * @param appendLineKeyword
	 *            코멘트가 추가되는 행의 첫번째줄에 키워드라고 표시할 문자 기입.
	 * @return
	 */
	public static List<String> toAutoCommentedList(String code, String appendLineKeyword) {
		return CodeCommentUtil.doAutoComment(code, appendLineKeyword);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 12.
	 * @param code
	 * @return
	 */
	public static List<String> toList(String code, Supplier<String> regexSeparators) {
		String[] split = code.split(regexSeparators.get());
		return Stream.of(split).collect(Collectors.toList());
	}

	/**
	 * 문자열로된 텍스트로부터 파일명만 추출하는 정규식 패턴을 적용한후 리턴받음.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 18.
	 * @param fileName
	 * @return
	 */
	public static String getSimpleFileName(String fileName) {

		// 경로를 나타내는 특수문자만 제거한 모든 텍스트중 가장 마지막에 있는 텍스트 리턴.
		return ValueUtil.regexMatch("[^\\\\]{1,}$", fileName);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 12.
	 * @param code
	 * @return
	 */
	public static List<String> toList(String code) {
		return toList(code, () -> "\n");
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 19.
	 * @param string
	 * @param string2
	 */
	public static boolean equals(String str1, String str2) {
		return StringUtils.equals(str1, str2);
	}

	/**
	 * charsequence가 포함되는 마지막 인덱스를 리턴 <br/>
	 * 존재하지않는 경우 -1 리턴 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 24.
	 * @param value
	 * @param c
	 * @return
	 */
	public static int lastIndexOf(String value, char c) {

		for (int i = value.length() - 1; i >= 0; i--) {
			if (value.charAt(i) == c)
				return i;
		}
		return -1;
	}

	/**
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 14.
	 * @param value
	 * @param c
	 * @return
	 */
	public static String lastToken(String value, char c) {
		return lastToken(value, c, (str, idx) -> str.substring(idx + 1));
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 14.
	 * @param value
	 * @param c
	 * @return
	 */
	public static String lastToken(String value, char c, BiFunction<String, Integer, String> handler) {
		int lastIndexOf = lastIndexOf(value, c);
		if (lastIndexOf == -1)
			return "";
		return handler.apply(value, lastIndexOf);
	}

	/**
	 * 분자열 배열을 i~ j까지 하나로 결합
	 *
	 * @param split
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public static String valueOf(String[] split, int startIndex, int endIndex) {
		StringBuffer sb = new StringBuffer();
		for (int index = startIndex; index < endIndex; index++) {
			sb.append(split[index].trim());
		}
		return sb.toString();
	}

	/**
	 * 테이블명을 DVO명으로 바꾼다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 16.
	 * @param tableName
	 * @return
	 */
	public static String toDVOName(String tableName) {
		return getPrefixUpperTextMyEdit(tableName) + "DVO";
	}

	/**
	 * 문자열 패턴형식이 '_'가 들어가고 해당하는 문단 첫글자만 대문자로 바꾸고 싶을경우사용
	 *
	 * @param str
	 * @return
	 */
	public static String getPrefixUpperTextMyEdit(String str) {
		StringTokenizer stringTokenizer = new StringTokenizer(str, "_");
		String nextElement = null;
		char[] charArray = null;
		String temp = "";
		while (stringTokenizer.hasMoreElements()) {
			nextElement = (String) stringTokenizer.nextElement();
			charArray = nextElement.toCharArray();
			charArray[0] = Character.toUpperCase(charArray[0]);
			for (int i = 1; i < charArray.length; i++) {
				charArray[i] = Character.toLowerCase(charArray[i]);
			}
			temp += String.valueOf(charArray);
		}
		return temp;
	}

	/**
	 * 문자열 패턴형식이 '_'가 들어가고 해당하는 문단 첫글자만 대문자로 바꾸지만.. 맨 처음 글자는 소문자
	 *
	 * @param str
	 * @return
	 */
	public static String getPrefixLowerTextMyEdit(String str) {

		char[] charArray = getPrefixUpperTextMyEdit(str).toCharArray();

		String lowerCase = String.valueOf(charArray[0]).toLowerCase();
		charArray[0] = lowerCase.charAt(0);

		return String.valueOf(charArray);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 1.
	 * @param name
	 * @return
	 */
	public static String toCamelCase(final String name) {

		String[] names = tokenizeToStringArray(name.toLowerCase(), "_");
		StringBuffer buf = new StringBuffer();
		int i = 0;
		for (String n : names) {
			buf.append(i++ == 0 ? n : capitalize(n));
		}
		return buf.toString();
	}

	public static String capitalize(String name) {
		return StringUtils.capitalize(name);
	}

	/**
	 * from springframework
	 *
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * Trims tokens and omits empty tokens.
	 * <p>
	 * The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using {@code delimitedListToStringArray}
	 *
	 * @param str
	 *            the String to tokenize
	 * @param delimiters
	 *            the delimiter characters, assembled as String (each of those
	 *            characters is individually considered as delimiter).
	 * @return an array of the tokens
	 * @see java.util.StringTokenizer
	 * @see String#trim()
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	/**
	 *
	 * from springframework.
	 *
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * <p>
	 * The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using {@code delimitedListToStringArray}
	 *
	 * @param str
	 *            the String to tokenize
	 * @param delimiters
	 *            the delimiter characters, assembled as String (each of those
	 *            characters is individually considered as delimiter)
	 * @param trimTokens
	 *            trim the tokens via String's {@code trim}
	 * @param ignoreEmptyTokens
	 *            omit empty tokens from the result array (only applies to
	 *            tokens that are empty after trimming; StringTokenizer will not
	 *            consider subsequent delimiters as token in the first place).
	 * @return an array of the tokens ({@code null} if the input String was
	 *         {@code null})
	 * @see java.util.StringTokenizer
	 * @see String#trim()
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

		if (str == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}

	/**
	 * from springframework.
	 *
	 * Copy the given Collection into a String array. The Collection must
	 * contain String elements only.
	 *
	 * @param collection
	 *            the Collection to copy
	 * @return the String array ({@code null} if the passed-in Collection was
	 *         {@code null})
	 */
	public static String[] toStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return collection.toArray(new String[collection.size()]);
	}

	/**
	 * dbms 명으로 드라이버클래스이름을 리턴
	 * 
	 * 대신 Gargoyle에서 허용하는 dbms명만 리턴해줌.
	 * 
	 * config.properties 파일에 기술되어있음.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 7. 12.
	 * @param dbms
	 * @return
	 * @throws NotYetSupportException
	 */
	public static String getDbmsNameToDriver(String dbms) {
		String panePropertyValue = "dbms.".concat(dbms);
		String drvierClassName = ConfigResourceLoader.getInstance().get(panePropertyValue);
		return drvierClassName;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 14.
	 * @param extension
	 * @param data
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static String readConversion(String extension, byte[] data, Charset charset) throws Exception {
		GargoyleFileContConversionPolicy m = new GargoyleFileContConversionPolicy();
		return m.conversion(extension, new ByteArrayInputStream(data), charset);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 8.
	 * @param name
	 * @return
	 */
	public static String getExtension(String name) {

		for (int i = name.length() - 1; i >= 0; i--) {
			if (name.charAt(i) == '.') {
				return name.substring(i);
			}
		}
		return null;
	}

	/**
	 * 확장자 부분을 제거한 값을 리턴한다. <br/>
	 * 확장자는 이름끝 .을 기준으로한다.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 26.
	 * @param name
	 * @return
	 */
	public static String removeExtension(String name) {
		String extension = getExtension(name);
		return name.replace(extension, "");
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 27.
	 * @param c
	 * @return
	 */
	public int toInt(char c) {
		return (int) c;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 27.
	 * @param b
	 * @return
	 */
	public int toInt(byte b) {
		return (int) b;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 27.
	 * @param b
	 * @return
	 */
	public int toInt(short s) {
		return (int) s;
	}

	/**
	 * 배열에서 일치하는 배열 인덱스 리턴. <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 5.
	 * @param signatureNames
	 * @param signature
	 * @return
	 */
	public static int indexOf(String[] signatureNames, String signature) {
		for (int i = 0, max = signatureNames.length; i < max; i++) {
			if (ValueUtil.equals(signatureNames[i], signature))
				return i;
		}
		return -1;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 20.
	 * @param str
	 * @return
	 */
	public static String getCodeBlock(String str) {
		return getCodeBlock(str, '{', '}');
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 20.
	 * @param str
	 * @param blockStart
	 * @param blockEnd
	 * @return
	 */
	public static String getCodeBlock(String str, char blockStart, char blockEnd) {
		boolean start = false;
		int startIdx = -1;
		Stack<Object> stack = new Stack<>();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == ' ')
				continue;

			if (start && stack.isEmpty()) {
				// System.out.println("block end is : " + i);
				// System.out.println(str.subSequence(startIdx, i));
				return str.substring(startIdx, i);
			}

			if (c == blockStart) {
				stack.push(i);
				if (!start) {
					startIdx = i;
				}
				start = true;

			}

			else if (c == blockEnd) {
				stack.pop();
			}

		}
		return "";
	}

	/**
	 * 대소문자를 무시하고 끝 패턴이 일치하는지 여부 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 25.
	 * @param name
	 * @param endWith
	 * @return
	 */
	public static boolean ignoreEndWith(String name, String endWith) {
		String pattern = "(?i)" + endWith + "$";
		Pattern compile = Pattern.compile(pattern);
		Matcher matcher = compile.matcher(name);
		return matcher.find();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 27. 
	 * @param arr
	 * @param convert
	 * @return
	 */
	public static String toString(List<?> arr, Function<Object,String> convert) {
		if (arr == null)
			return null;

		StringBuilder sb = new StringBuilder();
		for (Object str : arr) {
			sb.append(convert.apply(str)).append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 24.
	 * @param arr
	 * @return
	 */
	public static String toString(List<?> arr) {
		return toString(arr, str-> str.toString());
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 24.
	 * @param str
	 * @return
	 */
	public static String toBase64String(String str) {
		if (str == null)
			return null;

		return Base64.getEncoder().withoutPadding().encodeToString(str.getBytes());
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 21.
	 * @param p
	 * @return
	 */
	public static String toString(Properties p) {
		StringBuilder sb = new StringBuilder();
		Set<Entry<Object, Object>> entrySet = p.entrySet();
		Iterator<Entry<Object, Object>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<Object, Object> next = iterator.next();
			sb.append(next.getKey()).append(" - ").append(next.getValue()).append("\n");
		}
		return sb.toString();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 11. 5.
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equalsIgnoreCase(String str1, String str2) {
		String _str1 = null;
		String _str2 = null;
		if (str1 != null) {
			_str1 = str1.toUpperCase();
		}

		if (str2 != null) {
			_str2 = str2.toUpperCase();
		}

		return equals(_str1, _str2);
	}
	/**
	 * @author KYJ (callakrsos@naver.com)
	 *
	 */
	public enum CodeType {
		JAVA_BUFFER, 
		JAVA_LIST,
		DOT_NET, 
		Nashorn,
		VBS,
		JAVASCRIPT
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 11. 14.
	 * @param codeType
	 * @param sql
	 * @param convert
	 * @return
	 */
	public static String getAppliationCode(CodeType codeType, String sql, Function<String, String> convert) {
		String[] split = sql.split("\n");
		StringBuilder sb = new StringBuilder();
		switch (codeType) {
		case JAVA_BUFFER:
			sb.append("StringBuffer sb = new StringBuffer();\n");
			for (String str : split) {
				sb.append("sb.append(\"").append(convert.apply(str)).append("\\n").append("\");\n");
			}
			sb.append("sb.toString();");
			break;
		case JAVA_LIST:
			sb.append("List<String> list = new java.util.ArrayList<String>();\n");
			for (String str : split) {
				sb.append("list.add(\"").append(convert.apply(str)).append("\\n").append("\");\n");
			}
			break;
		case DOT_NET:
			sb.append("StringBuilder sb = new StringBuilder();\n");
			for (String str : split) {
				sb.append("sb.append(\"").append(convert.apply(str)).append("\\n").append("\");\n");
			}
			sb.append("sb.ToString();");
			break;
		case Nashorn:
			sb.append("var sb = new Packages.java.lang.StringBuilder();\n");
			for (String str : split) {
				sb.append("sb.append(\"").append(convert.apply(str)).append("\\n").append("\");\n");
			}
			sb.append("sb.ToString();");
			break;

		}

		String string = sb.toString();
		return string;
	}
	
	/**
	 * xml 문장에서 개행문자를 제거. <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 18.
	 * @param str
	 * @return
	 */
	public static String removeSpace(String str) {

		// remove lineSeparator.
		String[] split = str.split("\n");

		StringBuffer sb = new StringBuffer();
		for (String s : split) {
			s = s.trim();
			char[] charArray = s.toCharArray();
			for (int i = 0; i < charArray.length; i++) {

				switch (charArray[i]) {
				case '\t':
					break;
				case '\n':
					sb.append(" ");
					break;
				case Character.UNASSIGNED:
					break;
				case ' ':
					sb.append(" ");
					break;
				default:
					sb.append(charArray[i]);
				}
			}
			sb.append(" ");
		}

		return sb.toString().trim();
	}


	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 7. 2. 
	 * @param selectedText
	 * @param appendText
	 * @return
	 */
	public static String appendText(String selectedText, String appendText) {
		StringBuilder sb = new StringBuilder();
		String[] split = selectedText.split("\n");
		for (String t : split) {
			sb.append(appendText).append(t).append("\n");
		}
		return sb.toString();
	}
}
