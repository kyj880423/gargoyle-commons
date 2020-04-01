/********************************
 *	프로젝트 : gargoyle-rax
 *	패키지   : com.kyj.fx.behavior.text
 *	작성일   : 2018. 6. 18.
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
@XmlRootElement(name = "Object")
public class BehaviorObjectVO {
	@XmlAttribute(name = "type")
	private String type;
	@XmlAttribute(name = "name")
	private String name;
	@XmlAttribute(name = "children")
	private String children;
	@XmlElements(value = { @XmlElement(name = "Object", type = BehaviorObjectVO.class) })
	private List<BehaviorObjectVO> objects;
	
	@XmlElements( value = { 
			
			@XmlElement(name = "Property", type = BehaviorPropertyVo.class),
			@XmlElement(name = "Property", type = BehaviorPropertyItemsVo.class)
			
			})
	private List<BehaviorPropertyVo> properties;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the children
	 */
	public String getChildren() {
		return children;
	}

	/**
	 * @return the objects
	 */
	public List<BehaviorObjectVO> getObjects() {
		return objects;
	}

	/**
	 * @return the properties
	 */
	public List<BehaviorPropertyVo> getProperties() {
		return properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BehaviorObjectVO [type=");
		builder.append(type);
		builder.append("\n, name=");
		builder.append(name);
		builder.append("\n, children=");
		builder.append(children);
		builder.append("\n, objects=");
		builder.append(objects);
		builder.append("\n, properties=");
		builder.append(properties);
		builder.append("\n]");
		return builder.toString();
	}

}
