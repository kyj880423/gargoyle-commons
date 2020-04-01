/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.popup
 *	작성일   : 2015. 12. 14.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.models;

/**
 * @author KYJ
 *
 */
public class ReplaceResultVO {

	public static enum REPLACE_TYPE {
		SIMPLE /* 단건바꾸기 */, ALL /* 전체바꾸기 */
	};

	/**
	 * 원본텍스트
	 *
	 * @최초생성일 2015. 12. 28.
	 */
	private String reaplceResult;

	/**
	 * 검색 내용
	 *
	 * @최초생성일 2015. 12. 14.
	 */
	private String searchText;

	/**
	 * 검색유형
	 *
	 * @최초생성일 2015. 12. 28.
	 */
	private REPLACE_TYPE reaplceType = REPLACE_TYPE.SIMPLE;

	/**
	 * 바꾸기를 처리할 텍스트
	 *
	 * @최초생성일 2015. 12. 28.
	 */
	private String replaceText;

	/**
	 * @return the reaplceResult
	 */
	public String getReaplceResult() {
		return reaplceResult;
	}

	/**
	 * @param reaplceResult
	 *            the reaplceResult to set
	 */
	public void setReaplceResult(String reaplceResult) {
		this.reaplceResult = reaplceResult;
	}

	/**
	 * @return the searchText
	 */
	public String getSearchText() {
		return searchText;
	}

	/**
	 * @param searchText
	 *            the searchText to set
	 */
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	/**
	 * @return the reaplceType
	 */
	public REPLACE_TYPE getReaplceType() {
		return reaplceType;
	}

	/**
	 * @param reaplceType
	 *            the reaplceType to set
	 */
	public void setReaplceType(REPLACE_TYPE reaplceType) {
		this.reaplceType = reaplceType;
	}

	/**
	 * @return the replaceText
	 */
	public String getReplaceText() {
		return replaceText;
	}

	/**
	 * @param replaceText
	 *            the replaceText to set
	 */
	public void setReplaceText(String replaceText) {
		this.replaceText = replaceText;
	}

}
