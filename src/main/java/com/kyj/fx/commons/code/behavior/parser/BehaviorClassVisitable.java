/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.parser
 *	작성일   : 2018. 5. 28.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.parser;

/**
 * 
 * behavior class 방문시 표시
 * 
 * @author KYJ
 *
 */
public interface BehaviorClassVisitable {

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 28.
	 * @param name
	 */
	public void setVbClass(BehaviorClass vbClass);

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 28.
	 * @return
	 */
	public BehaviorClass getVbClass();
}
