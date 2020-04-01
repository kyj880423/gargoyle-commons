/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

/**
 * 
 * 예외처리로직 구현.
 * 
 * @author KYJ
 *
 */
@FunctionalInterface
public interface ExceptionHandler {

	/**
	 * 예외 발생시 처리할 내용
	 *
	 * @Date 2015. 10. 18.
	 * @param t
	 * @User KYJ
	 */
	public void handle(Exception t);

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 30.
	 * @return
	 */
	public static DefaultExceptionHandler getDefaultHandler() {
		return new DefaultExceptionHandler();
	}

}
