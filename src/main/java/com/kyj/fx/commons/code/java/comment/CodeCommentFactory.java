/**
 * insp.util.code.comment
 * 2014. 7. 24.
 * KYJ
 * @처리내용 :
 */
package com.kyj.fx.commons.code.java.comment;

import java.util.ArrayList;
import java.util.List;

import com.kyj.fx.commons.utils.ValueUtil;

/**
 * @author KYJ
 *
 */
public class CodeCommentFactory {
	public static final String COMMENT_START_1 = "/*";
	public static final String COMMENT_START_2 = "/**";

	public static final String COMMENT_END_1 = "*/";

	public static final String COMMENT_1 = "//";
	public static final String COMMENT_2 = "@";

	private static List<String> checkList = new ArrayList<String>();

	static {
		checkList.add(COMMENT_START_1);
		checkList.add(COMMENT_START_2);
		checkList.add(COMMENT_END_1);
		checkList.add(COMMENT_1);
		checkList.add(COMMENT_2);

	}

	/**
	 * 2014. 7. 24. KYJ
	 *
	 * @param readLine
	 * @return
	 * @처리내용 : 해당라인에 주석에 해당하는 구문이 포함되는지 확인한다.
	 *
	 *
	 *       0x011 -> 17 : 주석시작부분과 끝부분이 존재 \n 0x001 -> 1 :주석 끝부분만 존재 \n 0x010 ->
	 *       16 : 주석 시작부분만 존재
	 *
	 *       0x100 -> 빈값 \n 0x000 - > 일반텍스트값
	 */
	public static int getCommentContain(String readLine) {
		int resultValue = 0x000;
		String tReadLine = readLine.trim();

		if (tReadLine.contains(COMMENT_START_1)) {
			resultValue = resultValue | 0x010;
		} else if (tReadLine.contains(COMMENT_START_2)) {
			resultValue = resultValue | 0x010;
		}

		if (tReadLine.contains(COMMENT_END_1)) {
			resultValue = resultValue | 0x001;
		}

		if (tReadLine.contains(COMMENT_1) || tReadLine.contains(COMMENT_2)) {
			resultValue = resultValue | 0x011;
		}

		if (ValueUtil.isEmpty(tReadLine)) {
			resultValue = resultValue | 0x100;
		}

		return resultValue;
	}
}
