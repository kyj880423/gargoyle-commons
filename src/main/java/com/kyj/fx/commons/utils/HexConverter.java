/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2019. 3. 10.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Hex문자열로 변환
 * 
 * @author KYJ
 *
 */
public class HexConverter {
	// private static Logger LOGGER =
	// LoggerFactory.getLogger(HexConverter.class);
	private boolean appendLineDex;

	/**
	 * @최초생성일 2019. 10. 29.
	 */
	private Charset encoding = StandardCharsets.UTF_8;

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 29.
	 * @return
	 */
	public Charset getEncoding() {
		return encoding;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 29.
	 * @param encoding
	 */
	public void setEncoding(Charset encoding) {
		this.encoding = encoding;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 29.
	 * @param in
	 * @return
	 */
	public String toFormatString(InputStream in) {
		return toFormatString(in, this.encoding);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 10.
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
		try (InputStreamReader reader = new InputStreamReader(in, encoding)) {
			StringBuffer itemHex = new StringBuffer();
			while ((temp = reader.read()) != -1) {

				String.format("%02x ", temp);
				String format = Integer.toHexString(temp);
				itemHex.append(format).append(" ");

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
	 * 파일로 hex데이터를 읽은값을 반환한다 <br/>
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 29.
	 * @param file
	 * @return
	 */
	public List<Map<String, Number>> fromFileToMap(File file) {
		// StringBuffer out = new StringBuffer();
		List<Map<String, Number>> out = new ArrayList<>();

		if (!file.exists())
			return out;
		int readVal = -1;
		long currentCols = 0;
		try (FileInputStream in = new FileInputStream(file)) {
			TreeMap<String, Number> map = null;
			while ((readVal = in.read()) != -1) {
				
				if (currentCols % 16 == 0) {
					map = new TreeMap<String, Number>();
//					currentCols = (currentCols >> 4) - 1;
					map.put("offset", currentCols);
					out.add(map);
					
				}
				
				
				map.put(String.format("%02x", currentCols % 16), readVal);
				currentCols++;
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return out;
	}

}
