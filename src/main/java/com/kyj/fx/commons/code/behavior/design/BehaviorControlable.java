/********************************
 *	프로젝트 : gargoyle-rax
 *	패키지   : com.kyj.fx.behavior.text
 *	작성일   : 2018. 7. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.design;

/**
 * 
 * Behavior 컨트롤을 생성할떄 해당 클래스를 상속받아 처리 <br/>
 * 
 * @author KYJ
 *
 */
public interface BehaviorControlable extends BehaviorPannelable {

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 18.
	 * @return
	 */
	public String getValueString();
}
