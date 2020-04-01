/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.exceptions
 *	작성일   : 2016. 3. 31.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.exceptions;

/**
 * TODO 클래스 역할
 *
 * @author KYJ
 *
 */
public class GagoyleRuntimeException extends RuntimeException implements IGargoyleExceptionCode {

	/**
	 * @최초생성일 2016. 3. 31.
	 */
	private static final long serialVersionUID = 1L;
	private ERROR_CODE errorCode = ERROR_CODE.EMPTY;

	public GagoyleRuntimeException() {

	}

	/**
	 */
	public GagoyleRuntimeException(ERROR_CODE errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @param message
	 */
	public GagoyleRuntimeException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public GagoyleRuntimeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public GagoyleRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public GagoyleRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/***********************************************************************************/
	/* 일반API 구현 */

	public ERROR_CODE getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ERROR_CODE errorCode) {
		this.errorCode = errorCode;
	}

	/***********************************************************************************/
}
