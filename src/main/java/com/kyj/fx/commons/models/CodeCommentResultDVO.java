package com.kyj.fx.commons.models;

import com.kyj.fx.commons.code.java.comment.CodeCommentTargetFinder.FindType;

/**
 *
 * 자동주석기에 사용할 DVO 객체
 * @author KYJ
 *
 */
public class CodeCommentResultDVO {

	private FindType findType;
	private String findCode;
	private String findRegex;
	private int commentTargetIndex = -1;

	private int addCommentStartLineNumber = -1;
	private int addCommentEndLineNumber = -1;

	public FindType getFindType() {
		return findType;
	}

	public void setFindType(FindType findType) {
		this.findType = findType;
	}

	public String getFindCode() {
		return findCode;
	}

	public void setFindCode(String findCode) {
		this.findCode = findCode;
	}

	public int getCommentTargetIndex() {
		return commentTargetIndex;
	}

	public void setCommentTargetIndex(int commentTargetIndex) {
		this.commentTargetIndex = commentTargetIndex;
	}

	public String getFindRegex() {
		return findRegex;
	}

	public void setFindRegex(String findRegex) {
		this.findRegex = findRegex;
	}

	public int getAddCommentStartIndex() {
		return addCommentStartLineNumber;
	}

	public void setAddCommentStartIndex(int addCommentStartIndex) {
		this.addCommentStartLineNumber = addCommentStartIndex;
	}

	public int getAddCommentEndIndex() {
		return addCommentEndLineNumber;
	}

	public void setAddCommentEndIndex(int addCommentEndIndex) {
		this.addCommentEndLineNumber = addCommentEndIndex;
	}

	@Override
	public String toString() {
		return "CodeCommentResultDVO [findType=" + findType.getText() + ", findCode=" + findCode + ", findRegex=" + findRegex
				+ ", commentTargetIndex=" + commentTargetIndex + "]";
	}

}
