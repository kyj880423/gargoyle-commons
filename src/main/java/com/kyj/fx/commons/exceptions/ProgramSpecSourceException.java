/**
 * package : com.kyj.fx.voeditor.visual.exceptions
 *	fileName : ProgramSpecSourceException.java
 *	date      : 2016. 02. 15.
 *	user      : KYJ
 */
package com.kyj.fx.commons.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ProgramSpecSourceException extends GargoyleException {

	public List<String> getDetailMsgList() {
		return detailMsgList;
	}

	public void addDetailMsgList(String detailMsg) {
		this.detailMsgList.add(detailMsg);
	}

	private List<String> detailMsgList = new ArrayList<String>();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProgramSpecSourceException(String message) {
		super(message);
	}

}
