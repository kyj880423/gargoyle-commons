/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.exceptions
 *	작성일   : 2016. 11. 11.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.exceptions;

/***************************
 * 
 * @author KYJ
 * 
 *         Gargoyle이 중복실행되면 발생되는 에러.
 *
 ***************************/
public class GargoyleDuplicatedRunException extends GagoyleRuntimeException {

	/**
	 * @최초생성일 2016. 11. 11.
	 */
	private static final long serialVersionUID = 4244171596417321150L;

	/**
	 */
	public GargoyleDuplicatedRunException() {

	}

	/**
	 * @param errorCode
	 */
	public GargoyleDuplicatedRunException(ERROR_CODE errorCode) {
		super(errorCode);

	}

	/**
	 * @param message
	 */
	public GargoyleDuplicatedRunException(String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public GargoyleDuplicatedRunException(Throwable cause) {
		super(cause);

	}

	/**
	 * @param message
	 * @param cause
	 */
	public GargoyleDuplicatedRunException(String message, Throwable cause) {
		super(message, cause);

	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public GargoyleDuplicatedRunException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

}
