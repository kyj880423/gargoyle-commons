/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.io
 *	작성일   : 2019. 11. 21.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.kyj.fx.commons.utils.FileUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.utils.XMLUtils;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class GxmlFile {

	private File file;
	private String cont;
	private Map<String,Object> properties;
	
	/**
	 * @param file
	 */
	private GxmlFile(File file) {
		this.file = file;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 21.
	 * @param f
	 * @return
	 * @throws FileNotFoundException
	 */
	public static GxmlFile fromFile(File f) throws FileNotFoundException {
		if (f == null || !f.exists())
			throw new FileNotFoundException("File does not exists.");
		return new GxmlFile(f);
	}

	public void setProperties(Map<String,Object> prop) {
		this.properties = prop;
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 21.
	 */
	public void load() {
		reload();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 21.
	 */
	public void reload() {
		cont = null;
		// getString();
		getXmlData();
		getStylesheets();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 21.
	 * @return
	 */
	public String getString() {
		if (ValueUtil.isEmpty(cont)) {
			try {
				cont = new String(FileUtil.getBytes(this.file), "utf-8");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return cont;
	}

	private String styleData;

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 21.
	 * @return
	 */
	public String getStylesheets() {
		if (ValueUtil.isEmpty(styleData)) {
			String string = this.getString();
			JSONObject json = ValueUtil.toJSONObject(string);
			styleData = json.get("xsltData").toString();
		}
		return styleData;
	}

	private String xmlData;

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 21.
	 * @return
	 */
	public String getXmlData() {
		if (ValueUtil.isEmpty(xmlData)) {
			String string = this.getString();
			JSONObject json = ValueUtil.toJSONObject(string);
			xmlData = json.get("xmlData").toString();
		}
		return xmlData;
	}

	/**
	 * @param styleData
	 *            the styleData to set
	 */
	public void setStyleData(String styleData) {
		this.styleData = styleData;
	}

	/**
	 * @param xmlData
	 *            the xmlData to set
	 */
	public void setXmlData(String xmlData) {
		this.xmlData = xmlData;
	}

	private String transformed;

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 5.
	 * @return
	 * @throws Exception
	 */
	public String transform() throws Exception {
		if (ValueUtil.isEmpty(styleData))
			styleData = this.getStylesheets();
		if (ValueUtil.isEmpty(xmlData))
			xmlData = this.getXmlData();
		return transformed = XMLUtils.transform(styleData, xmlData, this.properties);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 2. 13.
	 * @param out
	 * @throws Exception
	 */
	public void writeTransform(OutputStream out) throws Exception {

		if (ValueUtil.isEmpty(this.transformed)) {
			transform();
			if (ValueUtil.isEmpty(this.transformed))
				throw new Exception("there is no data.");
		}
		
		out.write(this.transformed.getBytes("utf-8"));
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 5.
	 * @param out
	 * @throws IOException
	 */
	public void write(OutputStream out) throws IOException {
		Map<String, String> map = new HashMap<>();
		map.put("xmlData", xmlData == null ? "" : xmlData);
		map.put("xsltData", styleData == null ? "" : styleData);
		out.write(ValueUtil.toJSONString(map).getBytes(StandardCharsets.UTF_8));
	}
}
