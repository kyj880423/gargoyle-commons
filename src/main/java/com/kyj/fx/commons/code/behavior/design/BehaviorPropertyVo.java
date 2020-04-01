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
@XmlRootElement(name = "Property")
public class BehaviorPropertyVo {

	@XmlAttribute(name = "name")
	private String name;

	@XmlValue
	private String text;

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BehaviorPropertyVo [name=");
		builder.append(name);
		builder.append(", text=");
		builder.append(text);
		builder.append("]");
		return builder.toString();
	}

}
