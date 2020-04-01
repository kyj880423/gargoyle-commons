/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2019. 1. 29.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.utils;

import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class FileNameUtilTest {

	/**
	 * wildCardMatch test <br/>
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 29. 
	 */
	@Test
	public void wildcardMatchTest() {
		boolean wildcardMatch = FilenameUtils.wildcardMatch("hello", "h*");
		Assert.assertTrue(wildcardMatch);

		wildcardMatch = FilenameUtils.wildcardMatch("Hello", "h*");
		Assert.assertFalse(wildcardMatch);

		wildcardMatch = FilenameUtils.wildcardMatch("Hello", "H????");
		Assert.assertTrue(wildcardMatch);

	}
}
