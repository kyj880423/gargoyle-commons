/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.design
 *	작성일   : 2018. 6. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.design;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author KYJ
 *
 */
@XmlRootElement(name = "Binary")
public class BehaviorBinaryVO {

	@XmlValue
	private String text;

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BehaviorBinaryVO [text=");
		builder.append(text);
		builder.append("]");
		return builder.toString();
	}

}
