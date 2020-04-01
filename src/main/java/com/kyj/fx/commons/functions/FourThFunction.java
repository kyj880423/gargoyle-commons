/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.functions
 *	작성일   : 2017. 11. 24.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.functions;

/**
 * 파라미터가 4개인 Function 인터페이스
 * 
 * @author KYJ
 *
 */
@FunctionalInterface
public interface FourThFunction<T, U, V, W, R> {

	public R apply(T t, U u, V v, W w);
}
