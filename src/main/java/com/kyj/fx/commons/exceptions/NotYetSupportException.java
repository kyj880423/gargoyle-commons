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
public class NotYetSupportException extends GargoyleException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public NotYetSupportException() {
		super();
	}

	public NotYetSupportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotYetSupportException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotYetSupportException(String message) {
		super(message);
	}

	public NotYetSupportException(Throwable cause) {
		super(cause);
	}
}
