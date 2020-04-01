/********************************
 *	프로젝트 : gargoyle-rax
 *	패키지   : com.kyj.fx.behavior.text
 *	작성일   : 2018. 6. 18.
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
@XmlRootElement(name = "Property")
public class BehaviorPropertyItemsVo extends BehaviorPropertyVo {

	@XmlElements(value = { @XmlElement(name = "Property", type = BehaviorPropertyVo.class),
			@XmlElement(name = "Property", type = BehaviorPropertyItemsVo.class) })
	private List<BehaviorPropertyVo> properties;

	@XmlElements(value = { @XmlElement(name = "Item", type = BehaviorItemVo.class) })
	private List<BehaviorItemVo> items;

	@XmlElement(name = "Binary")
	private BehaviorBinaryVO binary;

	/**
	 * @return the properties
	 */
	public List<BehaviorPropertyVo> getProperties() {
		return properties;
	}

	/**
	 * @return the items
	 */
	public List<BehaviorItemVo> getItems() {
		return items;
	}

	/**
	 * @return the binary
	 */
	public BehaviorBinaryVO getBinary() {
		return binary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BehaviorPropertyItemsVo [properties=");
		builder.append(properties);
		builder.append(", items=");
		builder.append(items);
		builder.append(", name=");
		builder.append(super.getName());
		builder.append(", text=");
		builder.append(super.getText());
		builder.append(", binary=");
		builder.append(this.getBinary());
		builder.append("]");

		return builder.toString();
	}

}
