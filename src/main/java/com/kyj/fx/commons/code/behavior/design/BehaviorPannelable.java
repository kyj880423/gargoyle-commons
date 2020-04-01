/********************************
 *	프로젝트 : gargoyle-rax
 *	패키지   : com.kyj.fx.behavior.text
 *	작성일   : 2018. 7. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.design;

import javafx.beans.property.ObjectProperty;

/**
 * 
 * Behavior 패널 <br/>
 * 
 * @author KYJ
 *
 */
public interface BehaviorPannelable {

	/**
	 * set design object <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 18.
	 * @param vo
	 */
	public void setBehaviorObjectVO(BehaviorObjectVO vo);

	/**
	 * get design object <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 18.
	 * @return
	 */
	public BehaviorObjectVO getBehaviorObjectVO();

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 18.
	 * @return
	 */
	public ObjectProperty<BehaviorObjectVO> behaviorObjectProperty();
}
