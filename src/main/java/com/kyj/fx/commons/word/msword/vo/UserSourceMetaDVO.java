/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.vo
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.vo;

/**
 * 소스파일에 대한 메타정보관리
 *
 * @author KYJ
 *
 */
public class UserSourceMetaDVO {

	/**
	 * 시스템명
	 *
	 * @최초생성일 2016. 6. 24.
	 */
	private String systemName;
	// 프로젝트명정의
	private String projectName;
	// 경로를 제거한 파일명
	private String simpleFileName;
	// 사용자 PC 이름 혹은 사양서를 작성하는 사람이름
	private String userPcName;
	// 파일 실제 경로
	private String realFilePath;
	// 패키지
	private String packages;
	// 사양서에 작성될 프로그램 명 적의
	private String programName;

	/**
	 * @return the systemName
	 */
	public final String getSystemName() {
		return systemName;
	}

	/**
	 * @param systemName
	 *            the systemName to set
	 */
	public final void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSimpleFileName() {
		return simpleFileName;
	}

	public void setSimpleFileName(String simpleFileName) {
		this.simpleFileName = simpleFileName;
	}

	public String getUserPcName() {
		return userPcName;
	}

	public void setUserPcName(String userPcName) {
		this.userPcName = userPcName;
	}

	public String getRealFilePath() {
		return realFilePath;
	}

	public void setRealFilePath(String realFilePath) {
		this.realFilePath = realFilePath;
	}

	public String getPackages() {
		return packages;
	}

	public void setPackages(String packages) {
		this.packages = packages;
	}

	@Override
	public String toString() {
		return "UserSourceMetaDVO [projectName=" + projectName + ", simpleFileName=" + simpleFileName + ", userPcName=" + userPcName
				+ ", realFilePath=" + realFilePath + "]";
	}

}
