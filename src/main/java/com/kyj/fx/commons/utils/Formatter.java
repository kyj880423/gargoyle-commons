package com.kyj.fx.commons.utils;

/**
 * Formatter contract
 *
 */
public interface Formatter {
	/**
	 * Format the source string. <br/>
	 *
	 * @param source
	 *            The original string
	 *
	 * @return format result
	 */
	public String format(String source);

	/**
	 * 
	 * toUppercase <br/>
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 2. 
	 * @param source
	 * @return
	 */
	public default String toUpperCase(String source) {
		return source.toUpperCase();
	};

	/**
	 * toLowerCase <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 2. 
	 * @param source
	 * @return
	 */
	public default String toLowerCase(String source) {
		return source.toLowerCase();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 2. 
	 * @param source
	 * @param position
	 * @return
	 */
	public String split(String source, int position);

}
