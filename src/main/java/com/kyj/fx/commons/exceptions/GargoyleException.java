/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.exceptions
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.exceptions;

/**
 * @author KYJ
 *
 */
public class GargoyleException extends Exception implements IGargoyleExceptionCode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 에러 코드
	 * 
	 * @최초생성일 2016. 7. 14.
	 */
	private ERROR_CODE errorCode = ERROR_CODE.EMPTY;

	/**
	 * 
	 */
	public GargoyleException() {

	}
	
	/**
	 * 
	 */
	public GargoyleException(ERROR_CODE errorCode) {
		this.errorCode = errorCode;
	}
	
	/**
	 * 
	 */
	public GargoyleException(ERROR_CODE errorCode,String message) {
		super(message);
		this.errorCode = errorCode;
	}
	

	/**
	 * @param message
	 */
	public GargoyleException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public GargoyleException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public GargoyleException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public GargoyleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	@Override
	public ERROR_CODE getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ERROR_CODE errorCode) {
		this.errorCode = errorCode;
	}

}
