/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.design
 *	작성일   : 2018. 8. 11.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.design;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author KYJ
 *
 */
@XmlRootElement(name = "Behavior")
public class BehaviorVo {

	private BehaviorMeta meta;
	private String schemaVersion;
	private DesignerLayoutVo designerLayoutVo;
	private DesignEventsVo designEventsVo;
	private BehaviorProperties behaviorProperties;
	
	/**
	 * @return the behaviorProperties
	 */
	@XmlElements(value = { @XmlElement(name = "Properties", type = BehaviorProperties.class) })
	public BehaviorProperties getBehaviorProperties() {
		return behaviorProperties;
	}

	/**
	 * @param behaviorProperties the behaviorProperties to set
	 */
	public void setBehaviorProperties(BehaviorProperties behaviorProperties) {
		this.behaviorProperties = behaviorProperties;
	}

	/**
	 * @return the schemaVersion
	 */
	@XmlAttribute(name = "SchemaVersion")
	public String getSchemaVersion() {
		return schemaVersion;
	}

	/**
	 * @param schemaVersion
	 *            the schemaVersion to set
	 */
	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}

	/**
	 * @return the designerLayoutVo
	 */
	@XmlElements(value = { @XmlElement(name = "DesignerLayout", type = DesignerLayoutVo.class) })
	public DesignerLayoutVo getDesignerLayoutVo() {
		return designerLayoutVo;
	}

	/**
	 * @param designerLayoutVo
	 *            the designerLayoutVo to set
	 */
	public void setDesignerLayoutVo(DesignerLayoutVo designerLayoutVo) {
		this.designerLayoutVo = designerLayoutVo;
	}

	/**
	 * @return the designEventsVo
	 */
	@XmlElements(value = { @XmlElement(name = "DesignEvents", type = DesignEventsVo.class) })
	public DesignEventsVo getDesignEventsVo() {
		return designEventsVo;
	}

	/**
	 * @param designEventsVo
	 *            the designEventsVo to set
	 */
	public void setDesignEventsVo(DesignEventsVo designEventsVo) {
		this.designEventsVo = designEventsVo;
	}

	/**
	 * @return the meta
	 */
	public BehaviorMeta getMeta() {
		return meta;
	}

	/**
	 * @param meta
	 *            the meta to set
	 */
	public void setMeta(BehaviorMeta meta) {
		this.meta = meta;
	}

}
