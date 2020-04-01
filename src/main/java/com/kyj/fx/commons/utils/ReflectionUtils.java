/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 12. 14.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class ReflectionUtils {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 14. 
	 * @param c
	 * @return
	 */
	public static Constructor<?>[] getAllConstructor(Class<?> c) {
		return c.getDeclaredConstructors();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 15. 
	 * @param c
	 * @return
	 */
	public static Method[] getDeclareMethods(Class<?> c) {
		return c.getDeclaredMethods();
	}
}
