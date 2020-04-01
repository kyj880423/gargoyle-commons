/**
 * insp.util.code.comment
 * 2014. 7. 24.
 * KYJ
 * @처리내용 :
 */
package com.kyj.fx.commons.code.java.comment;

import java.util.List;

import com.kyj.fx.commons.models.CodeCommentResultDVO;
import com.kyj.fx.commons.utils.ValueUtil;

/**
 * @author KYJ
 *
 */
public class CodeCommentAdder {

	/**
	 * 2014. 7. 25. KYJ
	 *
	 * @param addComList
	 * @param check
	 * @처리내용 : addComList에다가 자동으로 추가할 주석 내용을 삽입한다.
	 */
	public static int addComment(List<String> addComList, CodeCommentResultDVO check) {

		String[] split = check.getFindCode().split("\t");
		StringBuilder commentByType = new StringBuilder();
		int i = 0;
		if (split != null) {
			/*공백으로만 이루어진경우*/
			int indexOf = check.getFindCode().indexOf(ValueUtil.leftTrim(check.getFindCode()));
			for (i = 0; i < indexOf; i++) {
				commentByType.append(" ");
			}

			/*탭으로만된경우*/
			for (i = 0; i < split.length - 1; i++) {
				commentByType.append("\t");
			}

		}
		commentByType.append(CodeCommentTargetFinder.getCommentByType(check.getFindType()));

		int commentTargetIndex = check.getCommentTargetIndex();
		int addLocationIndex = commentTargetIndex - 1;

		addComList.add(addLocationIndex, commentByType.toString());
		return addLocationIndex + 3;
	}

	public static int addCommentForSwingTextArea(List<String> addComList, CodeCommentResultDVO check) {
		return addCommentForSwingTextArea(addComList, check, null, null);
	}

	/**
	 * 2014. 7. 25. KYJ
	 *
	 * @param addComList
	 * @param check
	 * @처리내용 : addComList에다가 자동으로 추가할 주석 내용을 삽입한다.
	 */
	public static int addCommentForSwingTextArea(List<String> addComList, CodeCommentResultDVO check, StringBuilder chainedCommentByType,
			StringBuilder chainedBlank) {

		StringBuilder commentByType = chainedCommentByType;
		StringBuilder blank = chainedBlank;

		//		if (chainedBlank == null) {
		//			blank = createBlack(check, commentByType);
		//		} else

		commentByType.append(CodeCommentTargetFinder.getCommentByType(check.getFindType(), check.getFindCode(), blank.toString()));

		int commentTargetIndex = check.getCommentTargetIndex();
		int addLocationIndex = commentTargetIndex - 1;

		addComList.add(addLocationIndex, commentByType.toString());
		return addLocationIndex + 3;
	}

	public static StringBuilder createBlank(CodeCommentResultDVO check, StringBuilder commentByType) {
		StringBuilder blank = new StringBuilder();
		String[] split = check.getFindCode().split("\t");
		int i = 0;
		if (split != null) {

			//			commentByType.append(appendLineKeyword);
			/*공백으로만 이루어진경우*/
			int indexOf = check.getFindCode().indexOf(ValueUtil.leftTrim(check.getFindCode()));

			for (i = 0; i < indexOf; i++) {
				blank.append(" ");
			}

			/*탭으로만된경우*/
			for (i = 0; i < split.length - 1; i++) {
				blank.append("\t");
			}
		}
		return blank;
	}

}
