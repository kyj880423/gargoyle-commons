/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.exceptions
 *	작성일   : 2016. 2. 14.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.exceptions;

/**
 * 파라미터 값이 없는경우 발생
 *
 * @author KYJ
 *
 */
public class GagoyleParamEmptyException extends GagoyleRuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public GagoyleParamEmptyException() {
		setErrorCode(ERROR_CODE.PARAMETER_EMPTY);
	}

	/**
	 * @param message
	 */
	public GagoyleParamEmptyException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public GagoyleParamEmptyException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public GagoyleParamEmptyException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public GagoyleParamEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
