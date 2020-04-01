/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2017. 11. 1.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * File Encoding을 찾음. </br>
 * 
 * @author KYJ
 *
 */
public class FileEncodingFinder {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileEncodingFinder.class);
	private File f;

	/**
	 */
	public FileEncodingFinder(File f) {
		this.f = f;
	}

	protected RandomAccessFile getInputStream() throws IOException {
		return new RandomAccessFile(f, "r");
	}

	/**
	 * 파일 인코딩을 리턴 <br/>
	 * 다른 인코딩이 필요한 경우는 아직 고려안함.<br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 1.
	 * @return
	 * @throws IOException
	 */
	public String getEncoding() {
		if(f == null || !f.exists())
			return Charset.defaultCharset().name();
		
		try (RandomAccessFile r = getInputStream()) {

			int a = r.read();
			int b = r.read();
			int c = r.read();
			int d = r.read();

			LOGGER.debug("{} {} {} {}", a, b, c, d);
			if (a == 239 && b == 187 && c == 191)
				return "UTF-8";
			else if (a == 254 && b == 255)
				return "UTF-16BE";
			else if (a == 255 && b == 254)
				return "UTF-16LE";
			else if (a == 0 && b == 0 && c == 254 && d == 255)
				return "UTF-32BE";
			else if (a == 255 && b == 254 && c == 0 && d == 0)
				return "UTF-32LE";
			else if (a == 247 && b == 100 && c == 76)
				return "UTF-1";
			else {
				LOGGER.debug("{} - can't find encoding from file header. set Default UTF-8", this.f.getName());
			}
		} catch (IOException e) {
			LOGGER.warn(ValueUtil.toString(e));
		}

		return Charset.defaultCharset().name();
	}

}
