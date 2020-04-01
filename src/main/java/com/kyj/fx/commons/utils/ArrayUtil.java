/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2019. 10. 29.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.utils;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class ArrayUtil {

	/**
	 * this is used to find the value is matched using equals api. <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 29.
	 * @param <T>
	 * @param target
	 * @param items
	 * @return
	 */
	public static <T> boolean exists(String target, @SuppressWarnings("unchecked") T... items) {

		if (items == null && target == null)
			return true;
		if (items == null)
			return false;

		for (T item : items) {
			if (ValueUtil.equals(target, item.toString()))
				return true;
		}
		return false;
	}

	/**
	 * This API is used to find out if a value exists between the array values
	 * and the text target. <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 29.
	 * @param <T>
	 * @param target
	 * @param items
	 * @return
	 */
	public static <T> boolean contains(String target, @SuppressWarnings("unchecked") T... items) {

		if (items == null && target == null)
			return true;
		if (items == null)
			return false;

		for (T item : items) {
			if (item.toString().contains(target))
				return true;
		}
		return false;
	}
}
