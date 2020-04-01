/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.nashorn
 *	작성일   : 2019. 1. 24.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.nashorn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.utils.XMLUtils;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class DOMDocument {
	private Map<String, String> header = new HashMap<String, String>();
	public Charset encoding = StandardCharsets.UTF_8;
	XPath xPath = XPathFactory.newInstance().newXPath();
	org.w3c.dom.Document doc = null;
	
	
	public String toString() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			XMLUtils.print(doc, out);
			return out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "<<NULL>>";
	}
	public void setProperty(String key, String value) {
		/*N/A*/
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 12. 
	 * @param url
	 * @throws Exception
	 */
	public boolean load(String url) {
		MSXML2XmlHttp6_0 x = new MSXML2XmlHttp6_0();
		boolean flag = false;
		try {
			x.url = url;
			x.send("");
			loadXML(x.responseText);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public void loadXML(String xml) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			factory.setNamespaceAware(false);
			factory.setXIncludeAware(false);
			

			if (encoding == null)
				encoding = StandardCharsets.UTF_8;
			// try(ByteArrayInputStream in = new
			// ByteArrayInputStream(data.getBytes())){

			try (InputStream in = new ByteArrayInputStream(xml.getBytes(encoding))) {
				InputSource source = new InputSource(in);

				DocumentBuilder newDocumentBuilder = factory.newDocumentBuilder();
				doc = newDocumentBuilder.parse(source);

				// XPathExpression compile = xPath.compile(xpath);
				// return Optional.of(converter.apply(doc, compile));
			}

		} catch (Exception exp) {
			this.parseError = exp;
//			exp.printStackTrace();
			// if (exHandler != null)
			// exHandler.handle(exp);
			// else
//			LOGGER.error(ValueUtil.toString(exp));
		}
	}
	
	public Exception parseError;

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 24.
	 * @param xpath
	 * @return
	 * @throws XPathExpressionException
	 */
	public NodeList selectNodes(String xpath) throws XPathExpressionException {
		XPathExpression exp = xPath.compile(xpath);
		return (NodeList) exp.evaluate(doc, XPathConstants.NODESET);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 24.
	 * @param xpath
	 * @return
	 * @throws XPathExpressionException
	 */
	public Node selectSingleNode(String xpath) throws XPathExpressionException {
		XPathExpression exp = xPath.compile(xpath);
		Node n = (Node) exp.evaluate(doc, XPathConstants.NODE);
		return new DOMNode(n);
	}
	
}
