/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.design
 *	작성일   : 2018. 8. 11.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.design;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author KYJ
 *
 */
@XmlRootElement(name = "DesignerLayout")
public class DesignerLayoutVo {

	private BehaviorObjectVO behaviorObjectVO;

	/**
	 * @return the behaviorObjectVO
	 */
	@XmlElements(value = { @XmlElement(name = "Object", type = BehaviorObjectVO.class) })
	public BehaviorObjectVO getBehaviorObjectVO() {
		return behaviorObjectVO;
	}

	/**
	 * @param behaviorObjectVO
	 *            the behaviorObjectVO to set
	 */
	public void setBehaviorObjectVO(BehaviorObjectVO behaviorObjectVO) {
		this.behaviorObjectVO = behaviorObjectVO;
	}

}
