/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.design
 *	작성일   : 2018. 8. 12.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.design;

import java.io.File;

import com.kyj.fx.commons.code.behavior.reader.BehaviorReader;

/**
 * @author KYJ
 *
 */
public class BehaviorMeta {

	private BehaviorReader reader;

	/**
	 * @return the reader
	 */
	public BehaviorReader getReader() {
		return reader;
	}

	/**
	 * @param reader
	 *            the reader to set
	 */
	public void setReader(BehaviorReader reader) {
		this.reader = reader;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 12.
	 * @return
	 */
	public File getWib() {
		return this.reader.getWib();
	}
}
