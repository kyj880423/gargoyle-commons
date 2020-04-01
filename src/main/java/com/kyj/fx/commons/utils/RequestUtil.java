/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2016. 11. 3.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.functions.GargoyleHostNameVertifier;
import com.kyj.fx.commons.functions.GargoyleSSLVertifier;

/**
 * @author KYJ
 *
 */
public class RequestUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);
	private static HostnameVerifier hostnameVerifier = (arg0, arg1) -> {
		return true;
	};

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 6.
	 * @param url
	 * @param response
	 * @param b
	 * @throws Exception
	 */
	public static <T> T req200(URL url, BiFunction<InputStream, Charset, T> response, boolean autoClose) throws Exception {
		String protocol = url.getProtocol();

		if ("http".equals(protocol))
			return request200(url, response, autoClose);
		else if ("https".equals(protocol)) {
			return reqeustSSL200(url, response, autoClose);
		}

		return reqeustSSL200(url, response, autoClose);
	}

	public static <T> T req(URL url, ResponseHandler<T> response) throws Exception {
		String protocol = url.getProtocol();

		if ("http".equals(protocol))
			return request(url, response);
		else if ("https".equals(protocol)) {
			return requestSSL(url, response);
		}

		return requestSSL(url, response, true);
	}

	public static <T> T requestSSL(URL url, ResponseHandler<T> response) throws Exception {
		return requestSSL(url, response, true);
	}

	public static ResponseHandler<String> DEFAULT_REQUEST_HANDLER = new ResponseHandler<String>() {

		@Override
		public String apply(InputStream is, Integer code) {
			if (code == 200) {
				return ValueUtil.toString(is);
			}
			else
			{
				LOGGER.error(ValueUtil.toString(is));
			}
//			LOGGER.warn("{}", ValueUtil.toString(is));
			return ValueUtil.toString(is);
		}
	};

	public static String requestSSL(URL url) throws Exception {
		return request(url, DEFAULT_REQUEST_HANDLER, true);
	}

	public static <T> T requestSSL(URL url, ResponseHandler<T> response, boolean autoClose) throws Exception {

		// SSLContext ctx = SSLContext.getInstance("TLS");

		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		InputStream is = null;
		T result = null;
		try {
			conn.setDefaultUseCaches(true);
			conn.setUseCaches(true);

			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:12.0) Gecko/20100101 Firefox/12.0");
			conn.setRequestProperty("Accept-Encoding", "UTF-8");
			// conn.setRequestProperty("Connection", "keep-alive");

			conn.setRequestProperty("Accept", "text/html");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("Accept-Encoding", "UTF-8");
			conn.setRequestProperty("Accept-Language", "KR");
			// conn.setRequestProperty("Cache-Control", "no-store");
			// conn.setRequestProperty("Pragma", "no-cache");

			conn.setHostnameVerifier(hostnameVerifier);

			conn.setConnectTimeout(6000);

			conn.connect();
			try {
				is = conn.getInputStream();
			}catch(FileNotFoundException|UnknownHostException e)
			{
				LOGGER.error(ValueUtil.toString(e));
				is = new InputStream() {
					
					@Override
					public int read() throws IOException {
						return -1;
					}
				};
			}
			String contentEncoding = conn.getContentEncoding();
			LOGGER.debug("code : [{}] [{}] URL : {} ,  ", conn.getResponseCode(), contentEncoding, url.toString());
			response.setContentEncoding(contentEncoding);
			response.setResponseCode(conn.getResponseCode());
			Map<String, List<String>> headerFields = conn.getHeaderFields();
			response.setHeaderFields(headerFields);
			result = response.apply(is, conn.getResponseCode());

		} finally {

			if (autoClose) {
				if (is != null)
					is.close();

				if (conn != null)
					conn.disconnect();
			}

		}

		return result;
	}

	public static <T> T reqeustSSL200(URL url, Map<String, String> header, BiFunction<InputStream, Charset, T> response, boolean autoClose)
			throws Exception {
		return reqeustSSL200(url, header, null, response, autoClose);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 15.
	 * @param url
	 * @param header
	 * @param response
	 * @param autoClose
	 * @return
	 * @throws Exception
	 */
	public static <T> T reqeustSSL200(URL url, Map<String, String> header, String content, BiFunction<InputStream, Charset, T> response,
			boolean autoClose) throws Exception {

		// SSLContext ctx = SSLContext.getInstance("TLS");

		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

		// conn.setDefaultSSLSocketFactory();
		InputStream is = null;
		T result = null;
		try {
			conn.setDefaultUseCaches(true);
			conn.setUseCaches(true);

			if (header != null) {
				Iterator<String> iterator = header.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String value = header.get(key);
					conn.setRequestProperty(key, value);
				}
			}

			conn.setHostnameVerifier(hostnameVerifier);

			conn.setConnectTimeout(6000);

			// if (content != null) {
			// conn.setDoOutput(true);
			// OutputStreamWriter out = new OutputStreamWriter(
			// conn.getOutputStream());
			// out.write(content);
			// }
			// conn.getOutputStream().write();

			conn.connect();

			// BufferedReader in = new BufferedReader(
			// new InputStreamReader(
			// conn.getInputStream()));
			// String decodedString;
			// while ((decodedString = in.readLine()) != null) {
			// System.out.println(decodedString);
			// }
			// in.close();
			is = conn.getInputStream();

			String contentType = conn.getContentType();
			String contentEncoding = conn.getContentEncoding();

			if (ValueUtil.isNotEmpty(contentEncoding)) {
				Optional<String> map = Stream.of(contentType.split(";")).filter(txt -> txt.toLowerCase().contains("charset")).findFirst()
						.map(v -> {
							return v.substring(v.indexOf("=") + 1);
						});
				contentEncoding = map.isPresent() ? map.get() : "UTF-8";
			}

			try {
				contentEncoding = Charset.forName(contentEncoding).name();
			} catch (Exception e) {

			}

			LOGGER.debug("code : [{}] [{}] URL : {} ,  ", conn.getResponseCode(), contentEncoding, url.toString());

			if (200 == conn.getResponseCode()) {
				result = response.apply(is, Charset.forName(contentEncoding));
			}

		} finally {

			if (autoClose) {
				if (is != null)
					is.close();

				if (conn != null) {
					conn.disconnect();
				}

			}

		}

		return result;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 15.
	 * @param url
	 * @param response
	 * @param autoClose
	 * @return
	 * @throws Exception
	 */
	public static <T> T reqeustSSL200(URL url, BiFunction<InputStream, Charset, T> response, boolean autoClose) throws Exception {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:12.0) Gecko/20100101 Firefox/12.0");
		param.put("Accept-Encoding", "UTF-8");
		param.put("Accept", "text/html");
		param.put("Accept-Charset", "UTF-8");
		param.put("Accept-Encoding", "UTF-8");
		param.put("Accept-Language", "KR");
		return reqeustSSL200(url, param, response, autoClose);
	}

	public static <T> T request(URL url, ResponseHandler<T> response) throws Exception {
		return request(url, response, true);
	}

	public static <T> T request200(URL url, BiFunction<InputStream, Charset, T> response, boolean autoClose) throws Exception {
		return request200(url, null, response, autoClose);
	}

	public static <T> T request200(URL url, byte[] out, BiFunction<InputStream, Charset, T> response, boolean autoClose) throws Exception {

		URLConnection openConnection = url.openConnection();
		HttpURLConnection conn = (HttpURLConnection) openConnection;
		InputStream is = null;
		T result = null;
		try {

			conn.setDefaultUseCaches(true);
			conn.setUseCaches(true);

			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:12.0) Gecko/20100101 Firefox/12.0");
			conn.setRequestProperty("Accept-Encoding", "UTF-8");
			// conn.setRequestProperty("Connection", "keep-alive");

			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("Accept-Encoding", "UTF-8");
			conn.setRequestProperty("Accept-Language", "KR");

			conn.setConnectTimeout(6000);
			conn.setReadTimeout(6000);

			if (out != null) {
				conn.setDoOutput(true);
				OutputStream stream = conn.getOutputStream();
				stream.write(out);
			}

			conn.connect();

			is = conn.getInputStream();

			String contentType = conn.getContentType();
			String contentEncoding = conn.getContentEncoding();
			// int contentLength = conn.getContentLength();
			Map<String, List<String>> headerFields = conn.getHeaderFields();
			try {
				Optional<String> findAny = headerFields.keySet().stream().filter(f -> f != null).filter(str -> {

					if ("Accept-Encoding".equals(str))
						return true;

					return str.toLowerCase().indexOf("charset") >= 0;
				}).findAny();

				if (findAny.isPresent()) {
					String wow = findAny.get();
					List<String> list = headerFields.get(wow);
					wow = list.get(0);
					if (Charset.isSupported(wow)) {
						contentEncoding = wow;
					}
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

			// String charset = "UTF-8";
			if (ValueUtil.isEmpty(contentEncoding)) {
				if (contentType != null) {
					Optional<String> map = Stream.of(contentType.split(";")).filter(txt -> txt.toLowerCase().contains("charset"))
							.findFirst().map(v -> {
								return v.substring(v.indexOf("=") + 1);
							});
					if (map.isPresent())
						contentEncoding = map.get();
					else {
						String headerField = conn.getHeaderField("Accept-Charset");
						if (ValueUtil.isNotEmpty(headerField))
							contentEncoding = headerField;
					}
				}
			}

			if (ValueUtil.isEmpty(contentEncoding)) {
				contentEncoding = "UTF-8";
				LOGGER.debug("force encoding 'UTF-8' -  what is encoding ?????  ########################################");
			}

			LOGGER.debug("code : [{}] [{}] URL : {} ,  ", conn.getResponseCode(), contentEncoding, url.toString());

			if (200 == conn.getResponseCode()) {
				result = response.apply(is, Charset.forName(contentEncoding));
			}

		} finally {

			if (autoClose) {
				if (is != null)
					is.close();

				if (conn != null)
					conn.disconnect();
			}

		}
		return result;

	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 7. 
	 * @param url
	 * @param out
	 * @return
	 * @throws Exception
	 */
	public static File requestDownload(URL url , File out) throws Exception {
		return request(url, new FileDownloadResponseHandler(out));
	}
	
	public static <T> T request(URL url, ResponseHandler<T> response, boolean autoClose) throws Exception {

		URLConnection openConnection = url.openConnection();

		HttpURLConnection conn = (HttpURLConnection) openConnection;

		InputStream is = null;
		T result = null;
		try {

			conn.setDefaultUseCaches(true);
			conn.setUseCaches(true);

			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:12.0) Gecko/20100101 Firefox/12.0");
			conn.setRequestProperty("Accept-Encoding", "UTF-8");

			conn.setRequestProperty("Accept", "text/html");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("Accept-Encoding", "UTF-8");
			conn.setRequestProperty("Accept-Language", "KR");

			conn.setConnectTimeout(6000);

			conn.connect();

			LOGGER.debug("code : [{}] [{}] URL : {} ,  ", conn.getResponseCode(), url.toString());

			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream();
				response.setContentEncoding(conn.getContentEncoding());
				Map<String, List<String>> headerFields = conn.getHeaderFields();
				response.setHeaderFields(headerFields);
				response.setResponseCode(conn.getResponseCode());
			} else {
				is = new InputStream() {

					@Override
					public int read() throws IOException {
						return -1;
					}
				};
			}

			// LOGGER.debug(conn.getPermission().toString());
			result = response.apply(is, conn.getResponseCode());

		} finally {

			if (autoClose) {
				if (is != null)
					is.close();

				if (conn != null)
					conn.disconnect();
			}

		}
		return result;
	}

	public static class CookieBase {

		private static BasicCookieStore cookieStore = new BasicCookieStore();

		public static <T> T request(String url, Map<String, String> data, Function<CloseableHttpResponse, T> res) throws Exception {
			return request(url, null, data, res);
		}

		public static <T> Function<CloseableHttpResponse, T> forEntry(Function<HttpEntity, T> res) {
			return response -> {
				Stream.of(response.getAllHeaders()).forEach(header -> {
					LOGGER.debug("{} : {} ", header.getName(), header.getValue());
				});

				HttpEntity entity = response.getEntity();
				return res.apply(entity);
			};
		}

		public static <T> T request(String url, Map<String, String> data, BiFunction<InputStream, Integer, T> res) throws Exception {
			return request(url, null, data, res);
		}

		public static <T> T request(String url, Header[] headers, Map<String, String> data, Function<CloseableHttpResponse, T> res)
				throws Exception {

			T rslt = null;
			CloseableHttpResponse response = null;
			CloseableHttpClient httpclient = null;

			// 시작 쿠키관리
			List<Cookie> cookies = cookieStore.getCookies();
			LOGGER.debug("Get cookies...  : " + cookies);

			try {

				HttpEntityEnclosingRequestBase http = new HttpPost(url);
				LOGGER.debug(url);

				if (headers != null) {
					for (Header header : headers) {
						if (header == null)
							continue;
						http.addHeader(header);
					}
				}

				HttpClientBuilder b = HttpClientBuilder.create();
				b = b.setDefaultCookieStore(cookieStore);
				b = b.setSslcontext(GargoyleSSLVertifier.defaultContext());
				b = b.setHostnameVerifier(GargoyleHostNameVertifier.defaultVertifier());

				httpclient = b.build(); // HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
				// httpclient = HttpClients.createDefault();

				// 시작 서버로 보낼 데이터를 묶음.

				List<NameValuePair> dataArr = new ArrayList<NameValuePair>();

				if (ValueUtil.isNotEmpty(data)) {
					Iterator<String> iterator = data.keySet().iterator();
					while (iterator.hasNext()) {
						String key = iterator.next();
						String value = data.get(key);
						dataArr.add(new BasicNameValuePair(key, value));
					}

				}

				UrlEncodedFormEntity sendEntityData = new UrlEncodedFormEntity(dataArr, "UTF-8");
				http.setEntity(sendEntityData);
				// 끝 서버로 보낼 데이터를 묶음.

				/* 프록시 체크 */
				// if (USE_PROXY) {
				// HttpHost proxy = new HttpHost(PROXY_URL, PROXY_PORT, "http");
				// response = httpclient.execute(proxy, http);
				// }
				// else {
				response = httpclient.execute(http);
				// }

				rslt = res.apply(response);

			} finally {

				if (response != null)
					EntityUtils.consume(response.getEntity());

				if (httpclient != null) {
					httpclient.close();
				}

			}

			return rslt;

		}

		public static <T> T request(String url, Header[] headers, String data, BiFunction<InputStream, Integer, T> res) throws Exception {

			T rslt = null;
			CloseableHttpResponse response = null;
			CloseableHttpClient httpclient = null;

			// 시작 쿠키관리
			List<Cookie> cookies = cookieStore.getCookies();
			LOGGER.debug("Get cookies...  : " + cookies);
			// 종료 쿠키관리

			try {

				HttpEntityEnclosingRequestBase http = new HttpPost(url);
				LOGGER.debug(url);

				if (headers != null) {
					for (Header header : headers) {
						if (header == null)
							continue;
						http.addHeader(header);
					}
				}

				HttpClientBuilder b = HttpClientBuilder.create();
				b = b.setDefaultCookieStore(cookieStore);
				b = b.setSslcontext(GargoyleSSLVertifier.defaultContext());
				b = b.setHostnameVerifier(GargoyleHostNameVertifier.defaultVertifier());
				httpclient = b.build();

				// httpclient = HttpClients.createDefault();

				// 시작 서버로 보낼 데이터를 묶음.

				StringEntity sendEntityData = new StringEntity(data, "UTF-8");
				http.setEntity(sendEntityData);
				// 끝 서버로 보낼 데이터를 묶음.

				/* 프록시 체크 */
				response = httpclient.execute(http);

				Stream.of(response.getAllHeaders()).forEach(h -> {
					LOGGER.debug("[[Response Cookie]] {} : {} ", h.getName(), h.getValue());
				});

				HttpEntity entity = response.getEntity();

				rslt = res.apply(entity.getContent(), response.getStatusLine().getStatusCode());

			} finally {

				if (response != null)
					EntityUtils.consume(response.getEntity());

				if (httpclient != null) {
					httpclient.close();
				}

			}
			return rslt;
		}

		public static <T> T request(String url, Header[] headers, Map<String, String> data, BiFunction<InputStream, Integer, T> res)
				throws Exception {

			T rslt = null;
			CloseableHttpResponse response = null;
			CloseableHttpClient httpclient = null;

			// 시작 쿠키관리
			List<Cookie> cookies = CookieBase.getCookies();// cookieStore.getCookies();
			LOGGER.debug("Request cookies...  : ");
			cookies.forEach(c -> {
				LOGGER.debug("[req] k : {} v : {}", c.getName(), c.getValue());
			});
			// 종료 쿠키관리

			try {

				HttpEntityEnclosingRequestBase http = new HttpPost(url);
				LOGGER.debug(url);

				if (headers != null) {
					for (Header header : headers) {
						if (header == null)
							continue;
						http.addHeader(header);
					}
				}
				HttpClientBuilder b = HttpClientBuilder.create();
				b = b.setDefaultCookieStore(cookieStore);
				b = b.setSslcontext(GargoyleSSLVertifier.defaultContext());
				b = b.setHostnameVerifier(GargoyleHostNameVertifier.defaultVertifier());
				httpclient = b.build();
				// setHostnameVerifier(hostnameVerifier)

				// httpclient = HttpClients.createDefault();

				// 시작 서버로 보낼 데이터를 묶음.

				List<NameValuePair> dataArr = new ArrayList<NameValuePair>();

				if (ValueUtil.isNotEmpty(data)) {
					Iterator<String> iterator = data.keySet().iterator();
					while (iterator.hasNext()) {
						String key = iterator.next();
						String value = data.get(key);
						dataArr.add(new BasicNameValuePair(key, value));
					}

				}

				UrlEncodedFormEntity sendEntityData = new UrlEncodedFormEntity(dataArr, "UTF-8");
				http.setEntity(sendEntityData);
				// 끝 서버로 보낼 데이터를 묶음.

				/* 프록시 체크 */
				// if (USE_PROXY) {
				// HttpHost proxy = new HttpHost(PROXY_URL, PROXY_PORT, "http");
				// response = httpclient.execute(proxy, http);
				// }
				// else {
				response = httpclient.execute(http);
				// }

				LOGGER.debug("Response headers ...   ");
				Stream.of(response.getAllHeaders()).forEach(h -> {
					LOGGER.debug("[res] k : {}  v : {} ", h.getName(), h.getValue());
				});

				HttpEntity entity = response.getEntity();

				rslt = res.apply(entity.getContent(), response.getStatusLine().getStatusCode());

				LOGGER.debug("Res cookies...  : ");
				CookieBase.getCookies().forEach(c -> {
					LOGGER.debug("[res] k : {} v : {}", c.getName(), c.getValue());
				});

			} finally {

				if (response != null)
					EntityUtils.consume(response.getEntity());

				if (httpclient != null) {
					httpclient.close();
				}

			}
			return rslt;
		}

		/**
		 * 
		 * put , delete, get <br/>
		 * 
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2018. 11. 5.
		 * @param reqEntry
		 * @param headers
		 * @param res
		 * @return
		 * @throws Exception
		 */
		public static <T> T request(Supplier<HttpRequestBase> reqEntry, Header[] headers, BiFunction<InputStream, Integer, T> res)
				throws Exception {

			T rslt = null;
			CloseableHttpResponse response = null;
			CloseableHttpClient httpclient = null;

			// 시작 쿠키관리
			List<Cookie> cookies = cookieStore.getCookies();
			LOGGER.debug("Request cookies...  : ");
			cookies.forEach(c -> {
				LOGGER.debug("[req] k : {} v : {}", c.getName(), c.getValue());
			});
			// 종료 쿠키관리

			try {

				HttpRequestBase http = reqEntry.get();
				// HttpGet http = new HttpGet(url);
				LOGGER.debug("{}", http.getURI());

				if (headers != null) {
					for (Header header : headers) {
						if (header == null)
							continue;
						http.addHeader(header);
					}
				}

				// httpclient =
				// HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();

				HttpClientBuilder b = HttpClientBuilder.create();
				b = b.setDefaultCookieStore(cookieStore);
				b = b.setSslcontext(GargoyleSSLVertifier.defaultContext());
				b = b.setHostnameVerifier(GargoyleHostNameVertifier.defaultVertifier());
				httpclient = b.build();

				// httpclient = HttpClients.createDefault();

				/* 프록시 체크 */
				response = httpclient.execute(http);

				Stream.of(response.getAllHeaders()).forEach(h -> {
					LOGGER.debug("[[Res Header]] {} : {} ", h.getName(), h.getValue());
				});

				HttpEntity entity = response.getEntity();

				rslt = res.apply(entity.getContent(), response.getStatusLine().getStatusCode());

				LOGGER.debug("Res cookies...  : ");
				CookieBase.getCookies().forEach(c -> {
					LOGGER.debug("[res] k : {} v : {}", c.getName(), c.getValue());
				});

			} finally {

				if (response != null)
					EntityUtils.consume(response.getEntity());

				if (httpclient != null) {
					httpclient.close();
				}

			}
			return rslt;
		}

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2018. 11. 5.
		 * @param url
		 * @param headers
		 * @param res
		 * @return
		 * @throws Exception
		 */
		public static <T> T requestGet(String url, Header[] headers, BiFunction<InputStream, Integer, T> res) throws Exception {
			return request(() -> {
				return new HttpGet(url);
			}, headers, res);
		}

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2019. 9. 2. 
		 * @param <T>
		 * @param url
		 * @param headers
		 * @param res
		 * @return
		 * @throws Exception
		 */
		public static <T> T requestPost(String url, Header[] headers, BiFunction<InputStream, Integer, T> res) throws Exception {
			return request(() -> {
				return new HttpPost(url);
			}, headers, res);
		}
		
		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2019. 9. 2. 
		 * @param <T>
		 * @param url
		 * @param headers
		 * @param data
		 * @param res
		 * @return
		 * @throws Exception
		 */
		public static <T> T requestPost(String url, Header[] headers, Map<String, String> data, Function<CloseableHttpResponse,  T> res) throws Exception {
			return request( url, headers,  data, res);
		}
		
		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2018. 11. 5.
		 * @param url
		 * @param headers
		 * @param res
		 * @return
		 * @throws Exception
		 */
		public static <T> T requestPut(String url, Header[] headers, BiFunction<InputStream, Integer, T> res) throws Exception {
			return request(() -> {
				return new HttpPut(url);
			}, headers, res);
		}

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2018. 11. 5.
		 * @param url
		 * @param headers
		 * @param res
		 * @return
		 * @throws Exception
		 */
		public static <T> T requestDelete(String url, Header[] headers, BiFunction<InputStream, Integer, T> res) throws Exception {
			return request(() -> {
				return new HttpDelete(url);
			}, headers, res);
		}

		/**
		 * read only cookies.
		 * 
		 * @return
		 * @작성자 : KYJ
		 * @작성일 : 2017. 3. 22.
		 */
		public static List<Cookie> getCookies() {
			return cookieStore.getCookies();
		}

		public static void addCookies(Cookie... cookies) {
			cookieStore.addCookies(cookies);
		}

		public static void addCookie(Cookie cookie) {
			cookieStore.addCookie(cookie);
		}

		public static void addCookie(String key, String value) {
			addCookie(new BasicClientCookie(key, value));
		}

	}

}
