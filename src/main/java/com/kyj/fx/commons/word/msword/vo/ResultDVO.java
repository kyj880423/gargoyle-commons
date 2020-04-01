/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.vo
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.vo;

/**
 * 검사결과 DVO
 * 
 * @author KYJ
 */
public class ResultDVO {

	/**
	 * 검사패스 유무
	 */
	private String isPass;

	/**
	 * 검사한 메소드명
	 */
	private String methodName;

	/**
	 * 실패사유
	 */
	private String notPassStatement;

	/** 검사내용 */
	private String inspectionDesc;

	/**
	 * 소스
	 */
	private String statment;

	private String line;

	public String isPass() {
		return isPass;
	}

	public void setPass(String isPass) {
		this.isPass = isPass;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getNotPassStatement() {
		return notPassStatement;
	}

	public void setNotPassStatement(String notPassStatement) {
		this.notPassStatement = notPassStatement;
	}

	public String getStatment() {
		return statment;
	}

	public void setStatment(String statment) {
		this.statment = statment;
	}

	public String getInspectionDesc() {
		return inspectionDesc;
	}

	public void setInspectionDesc(String inspectionDesc) {
		this.inspectionDesc = inspectionDesc;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

}
