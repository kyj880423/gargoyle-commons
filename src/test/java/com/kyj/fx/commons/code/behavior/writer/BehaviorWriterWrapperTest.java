package com.kyj.fx.commons.code.behavior.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.w3c.dom.Document;

import com.kyj.fx.commons.code.behavior.BehaviorEmptyFile;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.utils.XMLUtils;

public class BehaviorWriterWrapperTest {

	@Test
	public void addReferenceTest() throws Exception {

		File outDir = new File("test");
		if (!outDir.exists())
			outDir.mkdirs();

		try (InputStream in = BehaviorWriterWrapperTest.class.getResourceAsStream("TEST_FUNCTION.wib")) {
			BehaviorReadWriter wrapper = new BehaviorReadWriter(in);
			wrapper.load();
			wrapper.appendReference(new File("APPEND_FUNCTIONS.bfm"));
			wrapper.print(System.out);
			wrapper.write(new File(outDir, "ZTEST_Gargoyle-Behavior-Test.wib"));
		}
	}

	@Test
	public void removeReferenceTest() throws Exception {
		File outDir = new File("test");
		if (!outDir.exists()) {
			System.err.println("Test Dir does not exists.");
			return;
		}

		File out = new File(outDir, "ZTEST_Gargoyle-Behavior-Test.wib");
		try (BehaviorReadWriter wrapper = new BehaviorReadWriter(out)) {
			wrapper.load();

			System.out.println("Before Read. #########################################");
			wrapper.print(System.out);

			wrapper.removeReference(new File("REMOVE_FUNCTIONS.bfm"));

			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println("After Removed. #########################################");
			wrapper.print(System.out);
		}

	}

	@Test
	public void emptyFileCreate() throws Exception {

		{
			System.out.println("basic ######################");
			InputStream in = BehaviorEmptyFile.generateNewXmlInputStream();
			String string = ValueUtil.toString(in);
			System.out.println(string);
		}

		{
			System.out.println("encryp ######################");

			InputStream in = BehaviorEmptyFile.generateNewInputStream();
			String string = ValueUtil.toString(in);
			System.out.println(string);
		}
	}

	/**
	 * behavior script 및 메타정보를 update하는 테스트 코드 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 16.
	 * @throws Exception
	 */
	@Test
	public void setScriptTest() throws Exception {
		File file = new File("EmptyBehavior.wib");
		if (!file.exists())
			file.createNewFile();
		BehaviorEmptyFile.generateNewFile(file);

		try (BehaviorReadWriter wrapper = new BehaviorReadWriter(file)) {
			wrapper.load();

			System.out.println("Before Read. #########################################");
			wrapper.print(System.out);

			wrapper.setScript("Dim a");

			wrapper.setAuthor("kyjun.kim");
			wrapper.setEmail("callakrsos@naver.com");
			wrapper.setVersion("1.0.0");
			wrapper.setDescription("first behavior");
			wrapper.setCompany("hello world");

			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println("After  #########################################");
			wrapper.print(System.out);
		}

	}

	/**
	 * 디자인 영역에 대한 편집이후 <br/>
	 * 정상적으로 디자인영역이 들어갔는지  테스트하기 위한 코드 <br/>
	 *  
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 18. 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void setDesignXmlTest() throws FileNotFoundException, IOException, Exception {
		File file = new File("EmptyBehavior.wib");
		if (!file.exists())
			file.createNewFile();
		BehaviorEmptyFile.generateNewFile(file);

		try (BehaviorReadWriter writer = new BehaviorReadWriter(file)) {
			writer.load();

			System.out.println("Before Read. #########################################");
			writer.print(System.out);

			writer.setScript("Dim a");
			writer.setAuthor("kyjun.kim");
			writer.setEmail("callakrsos@naver.com");
			writer.setVersion("1.0.0");
			writer.setDescription("first behavior");
			writer.setCompany("hello world");

			Document doc = XMLUtils.fromString("<Object type='test' name='behavior'/>", "UTF-8");
			writer.setDesignXml(doc);
			
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println("After  #########################################");
			writer.print(System.out);
			
			writer.write(new File("setDesignXmlTest.wib"));
		}
	}
}
