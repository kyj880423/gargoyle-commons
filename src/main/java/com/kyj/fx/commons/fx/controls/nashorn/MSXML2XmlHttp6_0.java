/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.nashorn
 *	작성일   : 2019. 1. 24.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.nashorn;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import com.kyj.fx.commons.threads.ExecutorDemons;
import com.kyj.fx.commons.utils.RequestUtil;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class MSXML2XmlHttp6_0 implements Runnable {

	public String method = "GET";
	public String url;
	public boolean async;
	private List<Header> header = new ArrayList<Header>();
	public String body;
	public String responseText;

	public MSXML2XmlHttp6_0() {

	}

	public void open(String method, String url, boolean async) {
		this.method = method;
		this.url = url;
		this.async = async;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 24.
	 * @param key
	 * @param value
	 */
	public void setRequestHeader(String key, String value) {
		this.header.add(new BasicHeader(key, value));
	}

	/**	
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 24.
	 * @param body
	 * @throws Exception
	 */
	public void send(String body) {
		this.body = body;
		if (async)
			ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(this);
		else
			this.run();

	}

	public String getResponseText() {
		return responseText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		int size = header.size();
		Header[] headers = new Header[size];
		for (int i = 0; i < size; i++) {
			headers[i] = header.get(i);
		}

		try {
			this.responseText = RequestUtil.CookieBase.request(new Supplier<HttpRequestBase>() {
				@Override
				public HttpRequestBase get() {

					switch (method) {
					case "POST":
						HttpPost httpPost = new HttpPost(url);
						httpPost.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
						return httpPost;
					default: {
						HttpGet get = new HttpGet(url);
						return get;
					}
					}
				}
			}, headers, RequestUtil.DEFAULT_REQUEST_HANDLER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
