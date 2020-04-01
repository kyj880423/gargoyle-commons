/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.functions
 *	작성일   : 2015. 11. 2.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.functions;

/**
 * 트랜잭션 범위
 *
 * @author KYJ
 *
 */
@FunctionalInterface
public interface BiTransactionScope<T, U> {

	/**
	 * 트랜잭션 범위 처리
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 6.
	 * @param t
	 * @param u
	 * @return 결과값
	 * @throws Exception
	 */
	public int scope(T t, U u) throws Exception;

}
