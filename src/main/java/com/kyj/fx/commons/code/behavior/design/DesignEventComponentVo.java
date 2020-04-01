/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.design
 *	작성일   : 2018. 8. 12.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.design;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author KYJ
 *
 */
@XmlRootElement(name = "Component")
public class DesignEventComponentVo {
	private String name;

	private List<DesignEventComponentEventVo> events;

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
	 * @return the events
	 */
	@XmlElements(value = { @XmlElement(name = "Event", type = DesignEventComponentEventVo.class) })
	public List<DesignEventComponentEventVo> getEvents() {
		return events;
	}

	/**
	 * @param events
	 *            the events to set
	 */
	public void setEvents(List<DesignEventComponentEventVo> events) {
		this.events = events;
	}

}
