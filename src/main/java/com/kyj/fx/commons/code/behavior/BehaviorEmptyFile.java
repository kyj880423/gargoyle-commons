/********************************
 *	프로젝트 : gargoyle-rax
 *	패키지   : com.kyj.fx.behavior.text
 *	작성일   : 2018. 5. 16.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.BehaviorFileContentConversion;
import com.kyj.fx.commons.utils.ValueUtil;

/**
 * 
 * 기본 파일을 생성하기 위한 작업 <br/>
 * 
 * @author KYJ
 *
 */
public class BehaviorEmptyFile {

	private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorEmptyFile.class);

	/**
	 * createNewFile <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @param file
	 */
	public static void generateNewFile(File file) {

		byte[] bytes = new byte[2048];
		try (InputStream in = generateNewInputStream()) {
			try (FileOutputStream writer = new FileOutputStream(file)) {
				int read = -1;
				while ((read = in.read(bytes)) != -1) {
					writer.write(bytes, 0, read);
				}
			}

		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 23. 
	 * @return
	 */
	public static InputStream generateNewXmlInputStream() {
		return BehaviorEmptyFile.class.getResourceAsStream("BehaviorEmpty.xml");
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 23. 
	 * @return
	 */
	public static InputStream generateNewInputStream() {
		try (InputStream in = generateNewXmlInputStream()) {
			byte[] bytes = ValueUtil.toByte(in);
			byte[] compress = BehaviorFileContentConversion.compress(bytes);

			return new ByteArrayInputStream(compress);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}

		return new InputStream() {

			@Override
			public int read() throws IOException {
				return -1;
			}

		};
	}
}
