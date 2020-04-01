/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 10. 12.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.utils;

import java.util.function.Predicate;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class GargoylePredicate {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 12.
	 * @return
	 */
	public static <T> Predicate<T> notNull() {
		return v -> v != null;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 12.
	 * @return
	 */
	public static <T> Predicate<T> notEmpty() {
		return v -> ValueUtil.isNotEmpty(v);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 12.
	 * @return
	 */
	public static <T> Predicate<T> empty() {
		return v -> ValueUtil.isEmpty(v);
	}
}
