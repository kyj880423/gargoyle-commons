/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2019. 9. 7.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class RequestUtilTest {

	@Test
	public void test() throws MalformedURLException, Exception {
		File out = new File("1671.html");

		if (out.exists()) {
			out.delete();
		}
		RequestUtil.requestDownload(new URL("https://cafe.naver.com/bluegrayfn1yo/1671"), out);

		Assert.assertNotNull(out);
		Assert.assertNotEquals(0, out.length());
		out.delete();
	}

}
