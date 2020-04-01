/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.writer
 *	작성일   : 2018. 5. 16.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.writer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.kyj.fx.commons.code.behavior.reader.BehaviorReader;
import com.kyj.fx.commons.utils.BehaviorFileContentConversion;
import com.kyj.fx.commons.utils.FileUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.utils.XMLUtils;

/**
 * @author KYJ
 *
 */
/**
 * @author KYJ
 *
 */
public class BehaviorReadWriter implements Closeable {

	private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorReadWriter.class);
	private BehaviorReader reader;
	private boolean compress = true;
	private File out;

	/**
	 * wib or bfm 파일 <br/>
	 * 
	 * @param f
	 * @Deprecated not recommand just, use for read.
	 */
	@Deprecated
	public BehaviorReadWriter(BehaviorReader reader) {
		this.reader = reader;
	}

	/**
	 * @param in
	 * @throws FileNotFoundException
	 * @Deprecated not recommand just, use for read.
	 */
	@Deprecated
	public BehaviorReadWriter(InputStream in) throws FileNotFoundException {
		this.reader = new BehaviorReader(in);
	}

	public BehaviorReadWriter(File read, File out) throws FileNotFoundException {
		this.reader = new BehaviorReader(read);
		this.out = out;
	}



	public BehaviorReadWriter(File out) throws FileNotFoundException {
		this.out = out;
		this.reader = new BehaviorReader(out);
	}

	private Document dom;

	public void load() throws Exception {

		byte[] xmlScript = reader.readBehaviorByte();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);

		factory.setIgnoringComments(true);
		factory.setNamespaceAware(false);
		factory.setXIncludeAware(false);
		// use the factory to take an instance of the document builder
		DocumentBuilder db = factory.newDocumentBuilder();

		// parse using the builder to get the DOM mapping of the
		// XML file
		InputSource is = new InputSource(new ByteArrayInputStream(xmlScript));
		dom = db.parse(is);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @param bfm
	 * @throws Exception
	 */
	public void appendReference(File bfm) throws Exception {
		addReference(bfm);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @param loadWib
	 * @param bfm
	 * @throws Exception
	 */
	public void addReference(File bfm) throws Exception {
		Element documentElement = dom.getDocumentElement();
		NodeList elementsByTagName = dom.getElementsByTagName("Reference");
		boolean exists = false;
		int length = elementsByTagName.getLength();
		for (int i = 0; i < length; i++) {
			Node item = elementsByTagName.item(i);
			if (item.hasAttributes()) {
				Node namedItem = item.getAttributes().getNamedItem("FileName");

				if (ValueUtil.equals(bfm.getName(), namedItem.getNodeValue())) {
					documentElement.removeChild(item);
					exists = true;
					break;
				}
			}
		}

		if (!exists) {
			Element newReference = dom.createElement("Reference");
			newReference.setAttribute("FileName", bfm.getName());
			documentElement.appendChild(newReference);
		}

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @param loadWib
	 * @param bfm
	 * @return
	 * @throws Exception
	 */
	public boolean removeReference(File bfm) throws Exception {
		Element documentElement = dom.getDocumentElement();
		NodeList elementsByTagName = dom.getElementsByTagName("Reference");
		int length = elementsByTagName.getLength();
		for (int i = 0; i < length; i++) {
			Node item = elementsByTagName.item(i);
			if (item.hasAttributes()) {
				Node namedItem = item.getAttributes().getNamedItem("FileName");

				if (ValueUtil.equals(bfm.getName(), namedItem.getNodeValue())) {
					documentElement.removeChild(item);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @throws Exception
	 */
	public void write() throws Exception {
		write(this.out);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @param outputFile
	 * @throws Exception
	 */
	public void write(File outputFile) throws Exception {

		Transformer tr = TransformerFactory.newInstance().newTransformer();
		// tr.setOutputProperty(OutputKeys.INDENT, "yes");
		// tr.setOutputProperty(OutputKeys.METHOD, "xml");
		// tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		// tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
		// tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
		// "4");

		// send DOM to file
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		tr.transform(new DOMSource(dom), new StreamResult(out));

		try (FileOutputStream fos = new FileOutputStream(outputFile)) {
			byte[] dataArray = out.toByteArray();
			if (compress)
				dataArray = BehaviorFileContentConversion.compress(dataArray);

			fos.write(dataArray);
		}

	}

	/**
	 * 현재 DOM 구조를 Output으로 전달.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 23.
	 * @param out
	 * @throws Exception
	 */
	public void print(OutputStream out) throws Exception {
		XMLUtils.print(this.dom, out);
	}

	@Override
	public void close() throws IOException {
		if (this.reader != null)
			this.reader.close();

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 16.
	 * @param script
	 * @throws Exception
	 */
	public void setScript(String script) throws Exception {

		File wib = this.reader.getWib();
		if (wib == null)
			return;

		if (FileUtil.getFileExtension(wib).equalsIgnoreCase("bfm")) {
			XPath xPath = XPathFactory.newInstance().newXPath();
			XPathExpression compile = xPath.compile("//FunctionFile");
			Node scriptNode = (Node) compile.evaluate(dom, XPathConstants.NODE);
			scriptNode.setTextContent(script);
		} else {
			XPath xPath = XPathFactory.newInstance().newXPath();
			XPathExpression compile = xPath.compile("//Execution//Script");
			Node scriptNode = (Node) compile.evaluate(dom, XPathConstants.NODE);
			scriptNode.setTextContent(script);
		}

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 16.
	 * @param script
	 * @throws Exception
	 */
	public void setAuthor(String author) throws Exception {
		File wib = this.reader.getWib();
		if (wib == null)
			return;
		if (FileUtil.getFileExtension(wib).equalsIgnoreCase("bfm"))
			return;
		Node propertyNode = getPropertyNode();
		Node authorNode = propertyNode.getAttributes().getNamedItem("Author");
		authorNode.setTextContent(author);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 16.
	 * @param company
	 * @throws Exception
	 */
	public void setCompany(String company) throws Exception {
		File wib = this.reader.getWib();
		if (wib == null)
			return;
		if (FileUtil.getFileExtension(wib).equalsIgnoreCase("bfm"))
			return;
		Node propertyNode = getPropertyNode();
		Node authorNode = propertyNode.getAttributes().getNamedItem("Company");
		authorNode.setTextContent(company);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 16.
	 * @param email
	 * @throws Exception
	 */
	public void setEmail(String email) throws Exception {
		File wib = this.reader.getWib();
		if (wib == null)
			return;
		if (FileUtil.getFileExtension(wib).equalsIgnoreCase("bfm"))
			return;
		Node propertyNode = getPropertyNode();
		Node authorNode = propertyNode.getAttributes().getNamedItem("Email");
		authorNode.setTextContent(email);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 16.
	 * @param description
	 * @throws Exception
	 */
	public void setDescription(String description) throws Exception {

		File wib = this.reader.getWib();
		if (wib == null)
			return;
		if (FileUtil.getFileExtension(wib).equalsIgnoreCase("bfm"))
			return;
		Node propertyNode = getPropertyNode();
		Node authorNode = propertyNode.getAttributes().getNamedItem("Description");
		authorNode.setTextContent(description);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 16.
	 * @param version
	 * @throws Exception
	 */
	public void setVersion(String version) throws Exception {
		File wib = this.reader.getWib();
		if (wib == null)
			return;
		if (FileUtil.getFileExtension(wib).equalsIgnoreCase("bfm"))
			return;
		Node propertyNode = getPropertyNode();
		Node authorNode = propertyNode.getAttributes().getNamedItem("Version");
		authorNode.setTextContent(version);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 20.
	 * @param strDesignXml
	 * @throws Exception
	 */
	public void setDesignXml(String strDesignXml) throws Exception {
		setDesignXml(strDesignXml, "UTF-8");
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 20.
	 * @param strDesignXml
	 * @param encoding
	 * @throws Exception
	 */
	public void setDesignXml(String strDesignXml, String encoding) throws Exception {

		File wib = this.reader.getWib();
		if (wib == null)
			return;
		if (FileUtil.getFileExtension(wib).equalsIgnoreCase("bfm"))
			return;

		Document designXml = XMLUtils.fromString(strDesignXml, encoding);
		setDesignXml(designXml);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 18.
	 * @param objectNode
	 * @throws Exception
	 */
	public void setDesignXml(Document objectNode) throws Exception {

		File wib = this.reader.getWib();
		if (wib == null)
			return;
		if (FileUtil.getFileExtension(wib).equalsIgnoreCase("bfm"))
			return;

		Node designNode = getDesignNode();

		NodeList childNodes = designNode.getChildNodes();

		/*
		 * clear all. 부모랑 자식이 1:1 관계일때 1개의 자식만 있다 생각되지만 다른 노드들이 많음 -_-;
		 */
		for (int i = childNodes.getLength() - 1; i >= 0; i--)
			designNode.removeChild(childNodes.item(i));

		// clone 처리를 하지않는 이상.. java에서 xml 복사가 불가능
		Element documentElement = objectNode.getDocumentElement();
		Node cloneNode = documentElement.cloneNode(true);
		// 아래 API를 활용해야 다른 Document끼리 복사가 가능.
		designNode.getOwnerDocument().adoptNode(cloneNode);
		designNode.appendChild(cloneNode);
	}

	/**
	 * return Property node. <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 16.
	 * @return
	 * @throws XPathExpressionException
	 */
	private Node getPropertyNode() throws XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression compile = xPath.compile("//Properties");
		Node propertyNode = (Node) compile.evaluate(dom, XPathConstants.NODE);
		return propertyNode;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 18.
	 * @return
	 * @throws XPathExpressionException
	 */
	private Node getDesignNode() throws XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression compile = xPath.compile("//DesignerLayout");
		Node propertyNode = (Node) compile.evaluate(dom, XPathConstants.NODE);
		return propertyNode;
	}

	/**
	 * @return the out
	 */
	public final File getOut() {
		return out;
	}

}
