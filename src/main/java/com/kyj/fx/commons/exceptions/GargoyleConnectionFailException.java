/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.exceptions
 *	작성일   : 2016. 1. 4.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.exceptions;

/**
 * 접속 불가 Exception
 * 
 * @author KYJ
 *
 */
public class GargoyleConnectionFailException extends GargoyleException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GargoyleConnectionFailException() {
		super(ERROR_CODE.CONNECTION_FAIL);
	}

	public GargoyleConnectionFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	public GargoyleConnectionFailException(String message, Throwable cause) {
		super(message, cause);

	}

	public GargoyleConnectionFailException(String message) {
		super(ERROR_CODE.CONNECTION_FAIL, message);
	}

	public GargoyleConnectionFailException(Throwable cause) {
		super(cause);
	}

}
