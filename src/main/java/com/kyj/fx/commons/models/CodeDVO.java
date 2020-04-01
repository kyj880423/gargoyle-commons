/********************************
 *	프로젝트 : CrudGrigSample
 *	패키지   : application
 *	작성일   : 2016. 5. 17.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.models;

/**
 * @author KYJ
 *
 */
public class CodeDVO {

	private String code;
	private String nm;

	public CodeDVO() {

	}

	public CodeDVO(String code, String nm) {
		super();
		this.code = code;
		this.nm = nm;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String toString() {
		return nm;
	}

	/**
	 * @return the code
	 */
	public final String getCode() {
		return code;
	}

	/**
	 * @return the nm
	 */
	public final String getNm() {
		return nm;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public final void setCode(String code) {
		this.code = code;
	}

	/**
	 * @param nm
	 *            the nm to set
	 */
	public final void setNm(String nm) {
		this.nm = nm;
	}

}
