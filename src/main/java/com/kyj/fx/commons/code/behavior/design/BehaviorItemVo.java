/********************************
 *	프로젝트 : gargoyle-rax
 *	패키지   : com.kyj.fx.behavior.text
 *	작성일   : 2018. 6. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.design;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author KYJ
 *
 */
@XmlRootElement(name = "Item")
public class BehaviorItemVo {
	@XmlAttribute(name = "type")
	private String type;
	@XmlValue
	private String text;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

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
		builder.append("BehaviorItemVo [type=");
		builder.append(type);
		builder.append(", text=");
		builder.append(text);
		builder.append("]");
		return builder.toString();
	}

}
