/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.design
 *	작성일   : 2018. 7. 2.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.design;

import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 * 
 * GUI에 ID를 표시하기 위해 사용되는 컨트롤
 * 
 * @author KYJ
 *
 */
class BehaviorIdLabel extends Label {

	public BehaviorIdLabel() {
		super();
	}

	public BehaviorIdLabel(String text, Node graphic) {
		super(text, graphic);
	}

	public BehaviorIdLabel(String text) {
		super(text);
	}

}
