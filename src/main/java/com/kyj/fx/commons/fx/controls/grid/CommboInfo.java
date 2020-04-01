/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2015. 11. 12.
 *	프로젝트 : SOS 미어캣 프로젝트
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import java.util.List;

/**
 * @author KYJ
 *
 */
public class CommboInfo<T> {

	private List<T> codeList;
	private String code;
	private String codeNm;
	
	public CommboInfo(List<T> codeList, String code, String codeNm) {
		super();
		this.codeList = codeList;
		this.code = code;
		this.codeNm = codeNm;
		
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the codeNm
	 */
	public String getCodeNm() {
		return codeNm;
	}

	/**
	 * @param codeNm
	 *            the codeNm to set
	 */
	public void setCodeNm(String codeNm) {
		this.codeNm = codeNm;
	}

	/**
	 * @return the codeList
	 */
	public List<T> getCodeList() {
		return codeList;
	}

	/**
	 * @param codeList
	 *            the codeList to set
	 */
	public void setCodeList(List<T> codeList) {
		this.codeList = codeList;
	}

}
