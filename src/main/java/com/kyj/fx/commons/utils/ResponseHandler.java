/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2017. 6. 2.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author KYJ
 *
 */
public abstract class ResponseHandler<T> implements BiFunction<InputStream, Integer, T> {

	/**
	 * 컨텐츠 인코딩
	 * @최초생성일 2017. 6. 2.
	 */
	private String contentEncoding;
	/**
	 * 헤더필드
	 * @최초생성일 2017. 6. 2.
	 */
	private Map<String, List<String>> headers;
	/**
	 * 응답코드
	 * @최초생성일 2017. 6. 2.
	 */
	private int responseCode;

	public String getContentEncoding() {
		return contentEncoding;
	}

	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public void setHeaderFields(Map<String, List<String>> headerFields) {
		headers = new HashMap<>(headerFields.size());
		headers.putAll(headerFields);
//		Collections.copy(headerFields, headers);
	}

	public Map<String, List<String>> getHeaders() {
		return this.headers;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public abstract T apply(InputStream is, Integer code);

}
