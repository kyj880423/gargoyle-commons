package com.kyj.fx.commons.utils;

import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

public class BehaviorFileContentConversionTest {

	@Test
	public void test() throws Exception {
		BehaviorFileContentConversion c = new BehaviorFileContentConversion();
		String message = "hello";
		byte[] compress = c.compress(message.getBytes("UTF-8"));
		String x = new String(compress, StandardCharsets.UTF_8);
		System.out.println(x);

		String decompress = c.decompress(compress);

		System.out.println(decompress);
		Assert.assertEquals(message, decompress);

	}

}
