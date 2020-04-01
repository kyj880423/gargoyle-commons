/**
 * package : com.kyj.fx.voeditor.visual.exceptions
 *	fileName : NotYetSupportException.java
 *	date      : 2015. 11. 19.
 *	user      : KYJ
 */
package com.kyj.fx.commons.exceptions;

/**
 * 아직 지원되지않은 행위를 처리한경우 발생
 *
 * @author KYJ
 *
 */
public class NotSupportException extends GargoyleException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public NotSupportException() {
		super();
	}

	public NotSupportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotSupportException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotSupportException(String message) {
		super(message);
	}

	public NotSupportException(Throwable cause) {
		super(cause);
	}
}
