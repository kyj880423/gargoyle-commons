/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.io
 *	작성일   : 2020. 1. 30.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.io;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class GzXmlZipUtilTest {

	/**
	 * gxml 파일을 읽고  <br/>
	 * gzxml파일로 압축처리 후 복구한 데이터를 비교 <br/> 
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 1. 30.
	 * @throws Exception
	 */
	@Test
	public void convertGzXmlTest() throws Exception {

		String path = GzXmlZipUtilTest.class.getResource("Sample.gxml").getFile();

		File file = new File(path);

		if (!file.exists()) {
			System.err.println("File not found.");
			return;
		}

		// load gxml file.
		GxmlFile gFile = GxmlFile.fromFile(file);
		String gString = gFile.getString();

		//create gzxml file.
		File gzXmlFile = new File(file.getParentFile(), "Sample.gzxml");
		GzXmlFile create = GzXmlFile.create();
		create.setStyleData(gFile.getStylesheets());
		create.setXmlData(gFile.getXmlData());
		try (FileOutputStream out = new FileOutputStream(gzXmlFile, false)) {
			create.write(out);
		}

		GzXmlFile fromGXmlFile = GzXmlFile.fromFile(gzXmlFile);
		String gzString = fromGXmlFile.getString();

		System.out.println(gString);
		System.out.println();
		System.out.println(gzString);
		Assert.assertEquals(true, gString.contentEquals(gzString));
	}

}
