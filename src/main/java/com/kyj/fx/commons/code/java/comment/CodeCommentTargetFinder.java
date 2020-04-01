/**
 * insp.util.code.comment
 * 2014. 7. 24.
 * KYJ
 * @처리내용 :
 */
package com.kyj.fx.commons.code.java.comment;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kyj.fx.commons.models.CodeCommentResultDVO;
import com.kyj.fx.commons.utils.DateUtil;

/**
 * @author KYJ
 */
public class CodeCommentTargetFinder {


	public enum FindType{


		FOR("for"),
		SWITCH("switch"),
		WHILE("while"),
		IF("if"),
		ELSE_IF("elseif"),
		JAVA_METHOD("javaMathod"),
		XFRAME_METHOD("xframeMathod");

		private String text;
		private FindType(String text)
		{
			this.text = text;
		}

		public String getText(){
			return this.text;
		}

	}
	/**/
//	private static final String FOR = "for";
//
//	private static final String SWITCH = "switch";
//
//	private static final String WHILE = "while";
//
//	private static final String IF = "if";
//	private static final String ELSE_IF = "elseif";

//	private static final String JAVA_METHOD = "javaMathod";

//	private static final String XFRAME_METHOD = "xframeMathod";




	/* 정규식 상수 */
	public static final String IF_PATTERN = "[\\s]{0,}if\\s{0,}\\((?:[a-zA-Z0-9\"_&|:\\s.();+-=<>!]{1,})\\)";

	public static final String ELSE_IF_PATTERN = "[\\s]{0,}else\\s+if\\s{0,}\\((?:[a-zA-Z0-9\"_&|:\\s.();+-=<>!]{1,})\\)";

	public static final String WHILE_PATTERN = "[\\s]{0,}while\\s{0,}\\((?:[a-zA-Z0-9\"_&|:\\s.();+-=<>!]{1,})\\)";

	public static final String SWITCH_PATTERN = "[\\s]{0,}switch\\s{0,}\\((?:[a-zA-Z0-9\"_&|:\\s.();+-=<>!]{1,})\\)";

	public static final String FOR_PATTERN = "[\\s]{0,}for\\s{0,}\\((?:[a-zA-Z0-9\"_&|:\\s.();+-=<>!]{1,})\\)";

	/**
	 * 		수정일 2016-10-13 kyj
	 *      앞에 return이 있는 케이스에서 메소드라 인식하므로 [^return]\\s+ 추가.
	 * @최초생성일 2016. 10. 13.
	 */
	public static final String JAVA_MATHOD_PATTERN = "[^return]\\s+((?:(public|private|default|protected|(!Override|\\s))(?:[a-zA-Z0-9,\\[\\]_\\s<>]{1,}|))(?:(\\sstatic\\s\\sfinal\\s|\\sstatic\\s|\\sfinal\\s|\\s))(?:(void\\s|[a-zA-Z0-9_\\[\\]<>]{1,}\\s))(?:(\\w{1,})))(?:(\\()|\\s\\()[a-zA-Z0-9_.,\\[\\]<>\\s]{0,}(?:(\\))|\\s\\))(?:(\\sthrows\\s\\w{1,}|throws\\s\\w{1,}|))[\\s]{0,}[\\{]{0,}";

	public static final String XFRAME_METHOD_PATTERN = "(:?(function))(:?([\\s]{1,}))(?:([a-zA-Z0-9_]{0,}))(?:([a-zA-Z0-9_\\(\\)\\s,]){1,})[\\s]{0,}[\\{]{0,}";

	private static Map<FindType, Pattern> checkMap = new java.util.LinkedHashMap<>();

	static {
		checkMap.put(FindType.ELSE_IF, Pattern.compile(ELSE_IF_PATTERN));
		checkMap.put(FindType.IF, Pattern.compile(IF_PATTERN));
		checkMap.put(FindType.WHILE, Pattern.compile(WHILE_PATTERN));
		checkMap.put(FindType.SWITCH, Pattern.compile(SWITCH_PATTERN));
		checkMap.put(FindType.FOR, Pattern.compile(FOR_PATTERN));
		checkMap.put(FindType.JAVA_METHOD, Pattern.compile(JAVA_MATHOD_PATTERN));
		checkMap.put(FindType.XFRAME_METHOD, Pattern.compile(XFRAME_METHOD_PATTERN));
	}

	/**
	 * 2014. 7. 24. KYJ
	 *
	 * @param source
	 * @return
	 * @처리내용 : 자동화 코멘트를 넣을 대상인지 한판한다.
	 */
	public static CodeCommentResultDVO checkTarget(String source) {
		Iterator<FindType> iterator = checkMap.keySet().iterator();
		while (iterator.hasNext()) {
			FindType findType = iterator.next();
			Pattern regex = checkMap.get(findType);
			Matcher matcher = regex.matcher(source);
			if (matcher.find()) {
				String group = matcher.group();
				CodeCommentResultDVO rDVO = new CodeCommentResultDVO();
				rDVO.setFindType(findType);
				rDVO.setFindCode(group);
				rDVO.setFindRegex(regex.pattern());
				return rDVO;
			}
		}

		return null;
	}

	/**
	 * 2014. 7. 28. KYJ
	 *
	 * 2016-10-12 KYJ Comment 코드 코멧 수정.
	 *
	 * @param type
	 * @param blank 공백구간
	 * @return
	 * @처리내용 : 자동화 주석때 사용할 코드코멘트를 반환한다.
	 */
	public static String getCommentByType(final FindType type, final String findCodeStatement ,final String blank)
	{
		String currentDate = DateUtil.getCurrentDateString();
		String userName = System.getProperty("user.name");
		if (FindType.FOR == type)
		{
			return new StringBuilder(blank).append("/*").append("\n")
									.append(blank).append("*  @Date : ").append(currentDate).append("\n")
									.append(blank).append("*  @User: ").append(userName).append("\n")
									.append(blank).append("*  @처리내용: for 반복문을 실행. */").toString();
		} else if (FindType.SWITCH == type)
		{
			return new StringBuilder(blank).append("/*").append("\n")
									.append(blank).append("*  @Date : ").append(currentDate).append("\n")
									.append(blank).append("*  @User: ").append(userName).append("\n")
									.append(blank).append("*  @처리내용: switch문 실행. */").toString();
		} else if (FindType.WHILE == type)
		{
			return new StringBuilder(blank).append("/*").append("\n")
									.append(blank).append("*  @Date : ").append(currentDate).append("\n")
									.append(blank).append("*  @User: ").append(userName).append("\n")
									.append(blank).append("*  @처리내용: while의 값이 거짓이 될때까지 반복문을 돈다. */").toString();
		} else if (FindType.IF == type || FindType.ELSE_IF == type)
		{
			 StringBuilder append =
					 new StringBuilder(blank).append("/*").append("\n")
									.append(blank).append("*  @Date : ").append(currentDate).append("\n")
									.append(blank).append("*  @User: ").append(userName).append("\n");
					if(FindType.IF == type)
					{
						append.append(blank).append("*  @처리내용: if문값이 참이라면.*/").toString();
					}

					else if(FindType.ELSE_IF == type)
					{
						append.append(blank).append("*  @처리내용: else if문값이 참이라면.*/").toString();
					}
					return append.toString();

		} else if (FindType.JAVA_METHOD == type)
		{
			return new StringBuilder(blank).append("/*").append("\n")
									.append(blank).append("*  @Date : ").append(currentDate).append("\n")
									.append(blank).append("*  @User: ").append(userName).append("\n")
									.append(blank).append("*  @처리내용:  XXX를 처리하는 자바 메소드 */").toString();
		} else if (FindType.XFRAME_METHOD == type)
        {

		    String tmpMethodName = findCodeStatement;
		    tmpMethodName = tmpMethodName.replaceFirst( "function", "" ).replaceAll( "\\{", "" ).replaceAll( "}", "" );
		    int indexOf = tmpMethodName.indexOf( "(" );

            String methodName = tmpMethodName.substring( 0, indexOf );
            int lastIndexOf = tmpMethodName.lastIndexOf( ")" )-1;
            String param = "";
            if(indexOf != lastIndexOf)
            {
                param = tmpMethodName.substring( indexOf );
            }
		    /** sample
		    @author  xx
		    @date    xx
		    @name    xx
		    @param   xx
		    @return  xx
		    @remarks xx
		    **/
            return new StringBuilder(blank)

                    .append("/*  @author : ").append(userName).append("\n")
                    .append("*    @Date : ").append(currentDate).append("\n")
                    .append("*    @name: ").append( methodName ).append("\n")
                    .append("*    @param: ").append(param).append("\n")
                    .append("*    @return: ").append(blank).append("\n")
                    .append("*    @remarks:  XXX를 처리하는 메소드 */").toString();
        }
		else
		{
			return new StringBuilder(blank).append("/*").append("\n").append("@Date : ").append(currentDate).append("\n")
									.append(blank).append("*  @User: ").append(userName).append("\n")
									.append(blank).append("*  @처리내용: */").toString();

		}
	}

	public static Object getCommentByType(CodeCommentResultDVO check) {
		return getCommentByType(check.getFindType(), check.getFindCode(), "");
	}

	public static Object getCommentByType(FindType findType) {
		return getCommentByType(findType, "", "");
	}

}
