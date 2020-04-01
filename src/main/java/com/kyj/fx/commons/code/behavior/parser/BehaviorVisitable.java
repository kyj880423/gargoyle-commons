/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.parser
 *	작성일   : 2018. 5. 8.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.parser;

/**
 * @author KYJ
 *
 */
public interface BehaviorVisitable {

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 8.
	 * @param code
	 */
	public void visite(String code);

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 8.
	 * @param startToken
	 */
	public void setStartIdx(int idx);

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 8.
	 * @param startToken
	 */
	public void setEndIdx(int idx);
}
