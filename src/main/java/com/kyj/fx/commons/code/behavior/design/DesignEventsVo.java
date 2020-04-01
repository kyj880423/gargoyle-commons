/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.design
 *	작성일   : 2018. 8. 12.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.design;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author KYJ
 *
 */
@XmlRootElement(name = "DesignEvents")
public class DesignEventsVo {

	private List<DesignEventComponentVo> events;

	/**
	 * @return the events
	 */
	@XmlElements(value = { @XmlElement(name = "Component", type = DesignEventComponentVo.class) })
	public List<DesignEventComponentVo> getEvents() {
		return events;
	}

	/**
	 * @param events
	 *            the events to set
	 */
	public void setEvents(List<DesignEventComponentVo> events) {
		this.events = events;
	}

}
