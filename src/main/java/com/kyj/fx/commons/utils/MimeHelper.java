/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2016. 5. 31.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.apache.poi.util.IOUtils;

/**
 * @author KYJ
 *
 */
public final class MimeHelper {

	private MimeHelper() {
	}

	/**
	 *
	 * File을 MIME (HTML기반) 이미지로 교체함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 5. 31.
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String imageTo(File file) {
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			return imageTo(fileInputStream);
		} catch (Exception e) {
			throw new RuntimeException("can't converting file.");
		}
	}

	/**
	 * Stream 을 MIME (HTML기반) 이미지로 교체함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 5. 31.
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String imageTo(InputStream is) throws IOException {
		byte[] byteArray = IOUtils.toByteArray(is);

		StringBuilder b = new StringBuilder();
		// BASE64로 컨버팅.
		byteArray = Base64.getMimeEncoder().encode(byteArray);

		b.append(new String("<img src=\"data:image/jpg;base64,"));
		b.append(new String(byteArray));
		b.append(new String("\" alt=\"\" width=\"683\" height=\"420\" />"));
		return b.toString();
	}

	/**
	 * Str을 MIME (HTML기반) 이미지로 교체함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 5. 31.
	 * @param str
	 * @return
	 */
	public static String stringTo(String str) {
		return new StringBuffer().append("<p>").append(str).append("<p>").toString();
	}

	private static String DEFAULT_MIME_HEADER =
			/* Define] Mime Default Header */
			"MIME-Version: 1.0 \n" + /**/
					"Content-Type: multipart/mixed; \n" + /**/
					"charset=\"utf-8\" \n" + /**/
					"Content-Transfer-Encoding: base64\n"; /**/

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 22.
	 * @param body
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String toMime(String body) throws UnsupportedEncodingException {
		return toMime(DEFAULT_MIME_HEADER, body);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 22.
	 * @param header
	 * @param body
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String toMime(String header, String body) throws UnsupportedEncodingException {
		StringBuilder preffix = new StringBuilder();
		String encode = new String(Base64.getMimeEncoder().encode(body.getBytes()));
		// String encode = Base64Utils.encode(body.getBytes(encoding));

		preffix.append(header).append("\n").append(encode);
		return preffix.toString();
	}

}
