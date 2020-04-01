package com.kyj.fx.commons.code.behavior.design;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.ImageView;

/**
 * @author KYJ
 *
 */
public class BehaviorDesignImageView extends ImageView implements BehaviorControlable {

	private ObjectProperty<BehaviorObjectVO> behaviorObjectVO = new SimpleObjectProperty<>();

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
		return null;
	}
}
