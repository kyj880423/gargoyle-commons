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
public class SearchResultVO {

	public static enum SEARCH_TYPE {
		SEARCH_SIMPLE, SEARCH_ALL
	};

	/**
	 * 원본텍스트
	 *
	 * @최초생성일 2015. 12. 28.
	 */
	private String fullText;

	/**
	 * 검색 내용
	 *
	 * @최초생성일 2015. 12. 14.
	 */
	private String searchText;

	/**
	 * 찾은 시작 인덱스
	 *
	 * @최초생성일 2015. 12. 14.
	 */
	private int startIndex;

	/**
	 * 찾은 종료 인덱스
	 *
	 * @최초생성일 2015. 12. 14.
	 */
	private int endIndex;

	/**
	 * 검색유형
	 *
	 * @최초생성일 2015. 12. 28.
	 */
	private SEARCH_TYPE searchType = SEARCH_TYPE.SEARCH_SIMPLE;

	/**
	 * 바꾸기를 처리할 텍스트
	 *
	 * @최초생성일 2015. 12. 28.
	 */
	private String replaceText;

	/**
	 * @return the searchContext
	 */
	public String getSearchText() {
		return searchText;
	}

	/**
	 * @param searchText
	 *            the searchContext to set
	 */
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @param startIndex
	 *            the startIndex to set
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @return the endIndex
	 */
	public int getEndIndex() {
		return endIndex;
	}

	/**
	 * @param endIndex
	 *            the endIndex to set
	 */
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	/**
	 * @return the searchType
	 */
	public SEARCH_TYPE getSearchType() {
		return searchType;
	}

	/**
	 * @param searchType
	 *            the searchType to set
	 */
	public void setSearchType(SEARCH_TYPE searchType) {
		this.searchType = searchType;
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

	/**
	 * @return the fullText
	 */
	public String getFullText() {
		return fullText;
	}

	/**
	 * @param fullText
	 *            the fullText to set
	 */
	public void setFullText(String fullText) {
		this.fullText = fullText;
	}

}
