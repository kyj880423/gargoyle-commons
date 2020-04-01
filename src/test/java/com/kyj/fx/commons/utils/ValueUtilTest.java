package com.kyj.fx.commons.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.junit.Assert;
import org.junit.Test;

public class ValueUtilTest {

	@Test
	public void ignoreEndWithTest() {
		boolean flag = ValueUtil.ignoreEndWith("test.vbs", ".vbs");
		Assert.assertTrue(flag);

		flag = ValueUtil.ignoreEndWith("test.Vbs", ".vbs");
		Assert.assertTrue(flag);

		flag = ValueUtil.ignoreEndWith("test.VBS", ".vbs");
		Assert.assertTrue(flag);

		flag = ValueUtil.ignoreEndWith("test.java", ".vbs");
		Assert.assertFalse(flag);
	}

	@Test
	public void wildcardMatch() {

		boolean wildcardMatch = FilenameUtils.wildcardMatch("sample", "sa*", IOCase.INSENSITIVE);
		Assert.assertTrue(wildcardMatch);
	}

}
