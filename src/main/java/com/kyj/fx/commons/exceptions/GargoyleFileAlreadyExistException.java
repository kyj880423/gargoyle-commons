/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual
 *	작성일   : 2016. 2. 11.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.exceptions;

/**
 * @author KYJ
 *
 */
public class GargoyleFileAlreadyExistException extends GargoyleResourceException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public GargoyleFileAlreadyExistException() {
		super(ERROR_CODE.FILE_ALREADY_EXISTS);
	}

	
	public GargoyleFileAlreadyExistException(ERROR_CODE errorCode) {
		super(errorCode);
	}


	/**
	 * @param message
	 */
	public GargoyleFileAlreadyExistException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public GargoyleFileAlreadyExistException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public GargoyleFileAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public GargoyleFileAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
