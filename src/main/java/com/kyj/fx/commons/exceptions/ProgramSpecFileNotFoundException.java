/**
 * package : com.kyj.fx.voeditor.visual.exceptions
 *	fileName : ProgramSpecFileNotFoundException.java
 *	date      : 2016. 02. 15.
 *	user      : KYJ
 */
package com.kyj.fx.commons.exceptions;

public class ProgramSpecFileNotFoundException extends GargoyleException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProgramSpecFileNotFoundException() {

	}

	public ProgramSpecFileNotFoundException(String message) {
		super(message);
	}

	public ProgramSpecFileNotFoundException(Throwable cause) {
		super(cause);
	}

	public ProgramSpecFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
