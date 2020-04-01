/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.design
 *	작성일   : 2018. 8. 12.
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
@XmlRootElement(name = "Event")
public class DesignEventComponentEventVo {
	private String name;
	private String text;

	/**
	 * @return the name
	 */
	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the script
	 */
	@XmlValue
	public String getText() {
		return text;
	}

	/**
	 * @param script
	 *            the script to set
	 */
	public void setText(String text) {
		this.text = text;
	}

}
