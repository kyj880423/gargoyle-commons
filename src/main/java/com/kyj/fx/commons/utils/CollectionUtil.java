/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 5. 30.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author KYJ
 *
 */
public class CollectionUtil {

	/**
	 * 배열순서가 짝수이면 키 -> 할당 <br/>
	 * 배열순서가 홀수이면 값 -> 할당 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 30.
	 * @param arr
	 * @return
	 */
	public static <V> Map<String, Object> toMap(Object... arr) {

		Map<String, Object> hashMap = new HashMap<String, Object>();
		String tmpKey = null;
		for (int i = 0; i < arr.length; i++) {
			if (i % 2 == 0) {
				if (arr[i] == null)
					throw new RuntimeException("Key can't be null");
				tmpKey = arr[i].toString();
			}

			else {
				hashMap.put(tmpKey, arr[i]);
				tmpKey = null;
			}

		}
		return hashMap;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 1.
	 * @param allowEmpty
	 * @param arr
	 * @return
	 */
	public static <V> Map<String, Object> toMapWithNotAllowEmpty(Object... arr) {

		Map<String, Object> hashMap = new HashMap<String, Object>();
		String tmpKey = null;
		for (int i = 0; i < arr.length; i++) {
			if (i % 2 == 0) {
				if (arr[i] == null)
					throw new RuntimeException("Key can't be null");
				tmpKey = arr[i].toString();
			}

			else {

				if (ValueUtil.isNotEmpty(arr[i]))
					hashMap.put(tmpKey, arr[i]);

				tmpKey = null;
			}

		}
		return hashMap;
	}

}
