/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.io
 *	작성일   : 2019. 11. 21.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.utils.XMLUtils;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class GzXmlFile {

	private File file;
	private String cont;

	private GzXmlFile() {
	}
	
	/**
	 * @param file
	 */
	private GzXmlFile(File file) {
		this.file = file;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 21.
	 * @param f
	 * @return
	 * @throws FileNotFoundException
	 */
	public static GzXmlFile fromFile(File f) throws FileNotFoundException {
		if (f == null || !f.exists())
			throw new FileNotFoundException("File does not exists.");
		return new GzXmlFile(f);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 1. 30.
	 * @param f
	 * @return
	 * @throws FileNotFoundException
	 */
	public static GzXmlFile fromGXmlFile(File f) throws FileNotFoundException {
		GxmlFile fromFile = GxmlFile.fromFile(f);
		GzXmlFile gzXmlFile = new GzXmlFile(f);
		gzXmlFile.setStyleData(fromFile.getStylesheets());
		gzXmlFile.setXmlData(fromFile.getXmlData());
		gzXmlFile.reload();
		return gzXmlFile;
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
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				GzXmlZipUtil.unzip(this.file, out);
				cont = out.toString("utf-8");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
//			cont = FileUtil.readFile(this.file);
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
		return XMLUtils.transform(styleData, xmlData);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 5.
	 * @param out
	 * @throws Exception
	 */
	public void write(OutputStream out) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("xmlData", xmlData == null ? "" : xmlData);
		map.put("xsltData", styleData == null ? "" : styleData);
		GzXmlZipUtil.zip(out, ValueUtil.toJSONString(map).getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 1. 30. 
	 * @return
	 */
	public static GzXmlFile create() {
		return new GzXmlFile();
	}
}
