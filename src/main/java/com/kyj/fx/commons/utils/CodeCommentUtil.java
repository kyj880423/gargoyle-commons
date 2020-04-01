/**
 *
 * 2014. 7. 24.
 * KYJ
 * @처리내용 :
 */
package com.kyj.fx.commons.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.code.java.comment.CodeCommentAdder;
import com.kyj.fx.commons.code.java.comment.CodeCommentFactory;
import com.kyj.fx.commons.code.java.comment.CodeCommentTargetFinder;
import com.kyj.fx.commons.code.java.comment.CodeCommentTargetFinder.FindType;
import com.kyj.fx.commons.models.CodeCommentResultDVO;

/**
 * 주석 자동화.
 * 2016-10-12 Gargoyle 버젼에 맞게 수정처리.
 * 					else if문의 라인위치를 맞게 보정하기위한 리펙토링 작업.
 * @author KYJ
 */
class CodeCommentUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CodeCommentUtil.class);

	/**
	 * 2014. 7. 24. KYJ
	 *
	 * @param filePath
	 * @return 코드자동화 대상 index번호
	 * @throws Exception
	 * @처리내용 : 소스코드를 읽어서 주석이 존재해야될 문장인경우 주석을 입력한다. 주석의 기준은 CodeCommentFactory를 참조한다.
	 */
	static CodeCommentResultDVO check(List<String> codeList, int startIndex, int endIndex) throws Exception {

		boolean commentFlag = false;
		/*ㅇㅇ*/
		for (int i = startIndex; i < endIndex; i++) {
			String code = codeList.get(i);
			switch (CodeCommentFactory.getCommentContain(code)) {
			/* 주석끝부분만 존재 */
			case 0x001:
				commentFlag = false;
				break;
			/* 주석시작부만존재 */
			case 0x010:
				/* 코멘트 시작부 시작 */
				commentFlag = true;
				break;
			/* 주석이 시작끝이 다 포함됨 */
			case 0x011:
				break;
			/* 빈값 */
			case 0x100:
				break;
			/* 일반텍스트 */
			default:
				/* 주석이 아니면서 */
				if (!commentFlag) {

					/* 주석을 넣어야할 문장이라면 */
					CodeCommentResultDVO target = CodeCommentTargetFinder.checkTarget(code);
					if (target != null) {
						if (!isUpperLineNumberHasComment(codeList, i)) {
							target.setCommentTargetIndex(i + 1);
							return target;
						}

					}

				}
				break;
			}
		}

		/* 대상없음 */
		return null;
	}

	/**
	 * if문 대상일경우 바로 바로 위의 코드에 주석에 해당하는 코멘트부분이 존재하는지 체크
	 *
	 * @param code
	 */
	static boolean isUpperLineNumberHasComment(List<String> codeList, int currentLineNumber) {
		currentLineNumber = currentLineNumber - 1;
		if (currentLineNumber < 0) {
			currentLineNumber = 0;
		}
		String code = codeList.get(currentLineNumber);
		boolean resultFlag = false;
		switch (CodeCommentFactory.getCommentContain(code)) {
		/* 주석끝부분만 존재 */
		case 0x001:
			resultFlag = true;
			break;
		/* 주석시작부만존재 */
		case 0x010:
			/* 코멘트 시작부 시작 */
			break;
		/* 주석이 시작끝이 다 포함됨 */
		case 0x011:
			resultFlag = true;
			break;
		/* 빈값 */
		case 0x100:
			break;
		/* 일반텍스트 */
		default:
			break;
		}
		return resultFlag;
	}

	/**
	 * 자동화 코멘트 기능
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static List<String> doAutoComment(File path, String encode, String appendLineKeyword) throws IOException {
		//		List<String> codeList = new ArrayList<String>();
		//
		//		try {
		//			codeList = Files.readAllLines(path.toPath(), Charset.forName("UTF-8")); //  BaseUtil.readFileReturnList( path , encode);
		//
		//			int index = 0;
		//			StringBuilder chainedBlack = null;
		//			StringBuilder commentByType = null;
		//
		//			while (index != -1) {
		//				CodeCommentResultDVO check = check(codeList, index, codeList.size());
		//				if (check == null) {
		//					break;
		//				}
		//				LOGGER.debug(check.toString());
		//
		//				commentByType = new StringBuilder();
		//				if (!CodeCommentTargetFinder.ELSE_IF_PATTERN.equals(check.getFindType())) {
		//					chainedBlack = CodeCommentAdder.createBlack(check, commentByType);
		//				}
		//
		//				index = CodeCommentAdder.addCommentForSwingTextArea(codeList, check, commentByType, chainedBlack);
		//			}
		//
		//		} catch (Exception e) {
		//			LOGGER.error(ValueUtil.toString(e));
		//		}

		List<String> codeList = Files.readAllLines(path.toPath(), Charset.forName("UTF-8")); //  BaseUtil.readFileReturnList( path , encode);
		return doAutoComment(codeList, appendLineKeyword);
	}

	/**
	 * 자동화 코멘트 기능
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 12.
	 * @param code
	 * @return
	 */
	public static List<String> doAutoComment(String code, String appendLineKeyword) {
		List<String> codeList = ValueUtil.toList(code);
		return doAutoComment(codeList, appendLineKeyword);
	}

	/**
	 * 자동화 코멘트 기능
	 * @param path
	 * @return
	 */
	public static List<String> doAutoComment(List<String> codeList, String appendLineKeyword) {
		//		List<String> codeList = new ArrayList<String>();

		try {

			//			codeList = ValueUtil.toList(code);//Files.readAllLines(path.toPath(), Charset.forName("UTF-8")); //  BaseUtil.readFileReturnList( path , encode);

			int index = 0;
			StringBuilder chainedBlack = null;
			StringBuilder commentByType = null;

			while (index != -1) {
				CodeCommentResultDVO check = check(codeList, index, codeList.size());
				if (check == null) {
					break;
				}
				LOGGER.debug(check.toString());

				commentByType = new StringBuilder(appendLineKeyword);
				if (!(FindType.ELSE_IF == check.getFindType())) {
					chainedBlack = CodeCommentAdder.createBlank(check, commentByType);
				}

				index = CodeCommentAdder.addCommentForSwingTextArea(codeList, check, commentByType, chainedBlack);
			}

		} catch (

		Exception e)

		{
			LOGGER.error(ValueUtil.toString(e));
		}

		return codeList;
	}
}
