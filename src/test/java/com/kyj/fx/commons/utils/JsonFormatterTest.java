package com.kyj.fx.commons.utils;

import org.junit.Test;

public class JsonFormatterTest {

	@Test
	public void test() {
		StringBuffer sb = new StringBuffer();
		sb.append("{\n");
		sb.append("  \"sub\": \"1234567890\",\n");
		sb.append("  \"name\": \"John Doe\",\n");
		sb.append("  \"iat\": 1516239022,\n");
		sb.append("  \"rule\": \"admin\"\n");
		sb.append("}\n");
		sb.toString();
		
		String removeSpaces = new JsonFormatter().removeSpaces(sb.toString());
		
		System.out.println(removeSpaces);
	}

}
