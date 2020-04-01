/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.design
 *	작성일   : 2018. 6. 20.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.design;

import java.util.function.BiConsumer;

import javafx.scene.Node;

/**
 * 사용자 정의 옵션처리가 필요한경우 상속받은후 구현
 * 
 * @author KYJ
 *
 */
public interface IUserCustomFxDrawAdapter {

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 20.
	 * @param customHandler
	 */
	public void setItemHandler(BiConsumer<Node, BehaviorPropertyVo> customHandler);

}
