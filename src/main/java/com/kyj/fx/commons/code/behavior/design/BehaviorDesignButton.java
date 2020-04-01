/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.design
 *	작성일   : 2018. 7. 2.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.design;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;

/**
 * @author KYJ
 *
 */
class BehaviorDesignButton extends Button implements BehaviorControlable {

	private ObjectProperty<BehaviorObjectVO> behaviorObjectVO = new SimpleObjectProperty<>();

	public BehaviorDesignButton() {

	}

	@Override
	public void setBehaviorObjectVO(BehaviorObjectVO vo) {
		this.behaviorObjectVO.set(vo);
	}

	@Override
	public BehaviorObjectVO getBehaviorObjectVO() {
		return this.behaviorObjectVO.get();
	}

	@Override
	public ObjectProperty<BehaviorObjectVO> behaviorObjectProperty() {
		return behaviorObjectVO;
	}

	@Override
	public String getValueString() {
		/*Nothing.*/
		return null;
	}

}
