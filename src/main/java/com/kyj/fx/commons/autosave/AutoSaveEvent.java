/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.autosave
 *	작성일   : 2019. 9. 23.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.autosave;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class AutoSaveEvent {

	/**
	 * 해당 이벤트를 호출한 대상. <br/>
	 * 
	 * @최초생성일 2019. 10. 30.
	 */
	private IGargoyleAutoSave caller;
	/**
	 * 반복 주기 <br/>
	 * 
	 * @최초생성일 2019. 10. 30.
	 */
	private int inteval;
	/**
	 * 스케줄 작업이 성공했는지 여부 <br/>
	 * 
	 * @최초생성일 2019. 10. 30.
	 */
	private boolean pass;
	/**
	 * 작업 실패시 Exception <br/>
	 * 
	 * @최초생성일 2019. 10. 30.
	 */
	private String failedReson;

	public IGargoyleAutoSave getCaller() {
		return caller;
	}

	public void setCaller(IGargoyleAutoSave caller) {
		this.caller = caller;
	}

	public int getInteval() {
		return inteval;
	}

	public void setInteval(int inteval) {
		this.inteval = inteval;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public String getFailedReson() {
		return failedReson;
	}

	public void setFailedReson(String failedReson) {
		this.failedReson = failedReson;
	}

}
