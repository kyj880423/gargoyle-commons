/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2018. 2. 21.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 * output like this. <br/>
 * <br/>
 * <table>
 * <tr>
 * <td>00000000 45 6c 66 46 69 6c 65 00 00 00 00 00 00 00 00 00</td>
 * </tr>
 * <tr>
 * <td>00000001 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00</td>
 * </tr>
 * <tr>
 * <td>00000002 80 00 00 00 01 00 03 00 00 10 01 00 00 00 00 00</td>
 * </tr>
 * <tr>
 * <td>00000003 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00</td>
 * </tr>
 * </table>
 * 
 * @author KYJ
 *
 */
public class DexConverter {
	private static Logger LOGGER = LoggerFactory.getLogger(DexConverter.class);
	private boolean appendLineDex;

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 21.
	 * @param text
	 * @return
	 */
	public String toFormatString(String text) {
		return toFormatString(text, StandardCharsets.UTF_8, StandardCharsets.UTF_8);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 21.
	 * @param text
	 * @param encoding
	 * @return
	 */
	public String toFormatString(String text, Charset from) {
		return toFormatString(text.getBytes(from), from);
	}
	

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 25. 
	 * @param text
	 * @param from
	 * @param to
	 * @return
	 */
	public String toFormatString(String text, Charset from, Charset to) {
		return toFormatString(text.getBytes(from), to);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 21.
	 * @param b
	 * @return
	 */
	public String toFormatString(byte[] b, Charset encoding) {

		try (ByteArrayInputStream in = new ByteArrayInputStream(b)) {
			return toFormatString(in, encoding);
		} catch (IOException e) {
			LOGGER.error(ValueUtil.toString(e));
		}

		return null;
	}


	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 25. 
	 * @param in
	 * @param encoding
	 * @return
	 */
	public String toFormatString(InputStream in, Charset encoding) {

		StringBuffer out = new StringBuffer();

		int temp = -1;
		int lineNumber = 0;
		final int newLineCount = 16;
		int currentCols = 0;
		boolean isBufferClear = false;
		try (InputStreamReader reader = new InputStreamReader(in, encoding)){
			StringBuffer itemHex = new StringBuffer();
			while ((temp = reader.read()) != -1) {

				String format = String.format("%2d ", temp);
				itemHex.append(format);

				currentCols++;

				if (currentCols % newLineCount == 0) {
					isBufferClear = true;
					itemHex.append("\n");

					if (appendLineDex) {
						String lineString = String.format("%08x ", lineNumber);
						String output = String.format("%s %s", lineString, itemHex.toString());
						out.append(output);
					} else {
						String output = String.format("%s", itemHex.toString());
						out.append(output);
					}

					itemHex.setLength(0);

					lineNumber++;
				}
			}

			// 데이터수가 너무 적은경우에는 아래 조건을 통해 출력될 수 있도록한다.
			if (!isBufferClear) {
				out.append(itemHex.toString());
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return out.toString();
	}

	/**
	 * @return the appendLineDex
	 */
	public boolean isAppendLineDex() {
		return appendLineDex;
	}

	/**
	 * @param appendLineDex
	 *            the appendLineDex to set
	 */
	public void setAppendLineDex(boolean appendLineDex) {
		this.appendLineDex = appendLineDex;
	}

}
