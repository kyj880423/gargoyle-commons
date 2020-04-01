/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 10. 12.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class Conversion {

	private static final Logger LOGGER = LoggerFactory.getLogger(Conversion.class);

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 12.
	 * @param type
	 * @param value
	 * @return
	 */
	public static Object convert(String type, String value) {

		Object v = value;

		if ("java.lang.Integer".equals(type)) {
			v = Integer.parseInt(value, 10);
		}

		else if ("java.lang.Double".equals(type)) {
			v = Double.parseDouble(value);
		}

		else if ("java.lang.Long".equals(type)) {
			v = Long.parseLong(value, 10);
		}

		if (type == null || type.length() == 0) {
			LOGGER.warn("not yet declared types.");
		}

		return v;
	}

}
