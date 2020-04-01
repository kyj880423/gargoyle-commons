/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.file.conversion
 *	작성일   : 2018. 3. 6.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.InputSource;

/**
 * @author KYJ
 *
 */
public class XmlFileContentConversion extends AbstractStringFileContentConversion {

	private ByteArrayOutputStream out;

	/**
	 * @param size
	 *            buffer size
	 */
	public XmlFileContentConversion(int size) {
		out = new ByteArrayOutputStream(size);
	}

	public XmlFileContentConversion() {
		out = new ByteArrayOutputStream();
	}

	/*
	 * @Deprecated this class not used. <br/>
	 * 
	 */
	@Deprecated
	@Override
	public void out(OutputStream out) {
		// Nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.voeditor.visual.framework.file.conversion.
	 * AbstractFileContentConversion#getOut()
	 */
	@Override
	public OutputStream getOut() {
		return this.out;
	}

	/**
	 * 데이터 변환 처리 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 6.
	 * @return String 변환된 데이터
	 * @throws Exception
	 */
	@Override
	public String conversion() throws Exception {
		String str = null;

		org.w3c.dom.Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try (InputStream in = getIn()) {
			InputSource source = new InputSource(in);

			DocumentBuilder newDocumentBuilder = factory.newDocumentBuilder();
			doc = newDocumentBuilder.parse(source);
			XMLUtils.print(doc, out);
			
			str = out.toString("UTF-8");
		}

		return str;
	}
}
