/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.vo
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.vo;

public class VocHistoryDVO {

	// 변경일자
	private String modifyDate;
	// VOC번호
	private String vocNo;
	// 변경 레코드
	private String chgRecord;
	// 설계자ID
	private String plannerId;
	// 설계자명
	private String plannerName;
	// 변경내용
	private String chgContent;

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getVocNo() {
		return vocNo;
	}

	public void setVocNo(String vocNo) {
		this.vocNo = vocNo;
	}

	public String getChgRecord() {
		return chgRecord;
	}

	public void setChgRecord(String chgRecord) {
		this.chgRecord = chgRecord;
	}

	public String getPlannerId() {
		return plannerId;
	}

	public void setPlannerId(String plannerId) {
		this.plannerId = plannerId;
	}

	public String getPlannerName() {
		return plannerName;
	}

	public void setPlannerName(String plannerName) {
		this.plannerName = plannerName;
	}

	public String getChgContent() {
		return chgContent;
	}

	public void setChgContent(String chgContent) {
		this.chgContent = chgContent;
	}

	@Override
	public String toString() {
		return "VocHistoryDVO [modifyDate=" + modifyDate + ", vocNo=" + vocNo + ", chgRecord=" + chgRecord + ", plannerId=" + plannerId
				+ ", plannerName=" + plannerName + "]";
	}

}
