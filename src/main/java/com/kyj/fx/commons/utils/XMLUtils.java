/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author KYJ
 *
 */
public class XMLUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(XMLUtils.class);

	/**
	 */
	public XMLUtils() {

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 5.
	 * @param data
	 * @param xpath
	 * @return
	 */
	public static Optional<NodeList> toXpathNodes(String data, String xpath) {
		return toXpathValue(data, xpath, (doc, exp) -> {
			NodeList result = (NodeList) exp.evaluate(doc, XPathConstants.NODESET);
			return result;
		});
	}

	public static Optional<NodeList> toXpathNodes(String data, String xpath, Charset encoding) {
		return toXpathValue(data, xpath, encoding, (doc, exp) -> {
			NodeList result = (NodeList) exp.evaluate(doc, XPathConstants.NODESET);
			return result;
		}, err -> {
			LOGGER.error(ValueUtil.toString(err));
		});
	}

	public static Optional<NodeList> toXpathNodes(String data, String xpath, ExceptionHandler errorHandler) {
		return toXpathValue(data, xpath, null, (doc, exp) -> {
			NodeList result = (NodeList) exp.evaluate(doc, XPathConstants.NODESET);
			return result;
		}, errorHandler);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 5.
	 * @param data
	 * @param xpath
	 * @param errorHandler
	 * @return
	 */
	public static Optional<NodeList> toXpathNodes(String data, String xpath, Charset encoding, ExceptionHandler errorHandler) {
		return toXpathValue(data, xpath, encoding, (doc, exp) -> {
			NodeList result = (NodeList) exp.evaluate(doc, XPathConstants.NODESET);
			return result;
		}, errorHandler);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 5.
	 * @param data
	 * @param xpath
	 * @return
	 */
	public static Optional<String> toXpathText(String data, String xpath) {
		return toXpathValue(data, xpath, (doc, exp) -> {
			return exp.evaluate(doc, XPathConstants.STRING).toString();
		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 20.
	 * @param data
	 * @param xpath
	 * @return
	 */
	public static Optional<Node> toXpathNode(String data, String xpath) {
		return toXpathValue(data, xpath, (doc, exp) -> {
			return (Node) exp.evaluate(doc, XPathConstants.NODE);
		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 20.
	 * @param data
	 * @param xpath
	 * @param converter
	 * @return
	 */
	public static <T> Optional<T> toXpath(String data, String xpath, XPathExpressionFunction<XPathExpression, T> converter) {
		return toXpathValue(data, xpath, converter);
	}

	/**
	 * @author KYJ
	 *
	 * @param <T>
	 * @param <R>
	 */
	@FunctionalInterface
	public static interface XPathExpressionFunction<T, R> {
		public R apply(org.w3c.dom.Document doc, T t) throws Exception;
	};

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 5.
	 * @param data
	 * @param xpath
	 * @param converter
	 * @return
	 */
	private static <T> Optional<T> toXpathValue(String data, String xpath, XPathExpressionFunction<XPathExpression, T> converter) {
		return toXpathValue(data, xpath, null, converter, ex -> {
			LOGGER.error(ValueUtil.toString(ex));
		});
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 5.
	 * @param data
	 * @param xpath
	 * @param converter
	 * @param exHandler
	 * @return
	 */
	private static <T> Optional<T> toXpathValue(String data, String xpath, Charset encoding,
			XPathExpressionFunction<XPathExpression, T> converter, ExceptionHandler exHandler) {
		try {
				org.w3c.dom.Document doc = null;
				doc = load(data, encoding);
				
				XPath xPath = XPathFactory.newInstance().newXPath();
				XPathExpression compile = xPath.compile(xpath);
				return Optional.of(converter.apply(doc, compile));


		} catch (Exception exp) {
			if (exHandler != null)
				exHandler.handle(exp);
			else
				LOGGER.error(ValueUtil.toString(exp));
		}
		return Optional.empty();
	}


	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 1. 29. 
	 * @param data
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static org.w3c.dom.Document load(String data) throws SAXException, IOException, ParserConfigurationException {
		return load(data, StandardCharsets.UTF_8);
	}
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 1. 29. 
	 * @param data
	 * @param encoding
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static org.w3c.dom.Document load(String data,  Charset encoding) throws SAXException, IOException, ParserConfigurationException {
		org.w3c.dom.Document doc = null;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);

		factory.setIgnoringComments(true);
		factory.setNamespaceAware(false);
		factory.setXIncludeAware(false);

		if (encoding == null)
			encoding = StandardCharsets.UTF_8;

		try (InputStream in = new ByteArrayInputStream(data.getBytes(encoding))) {
			InputSource source = new InputSource(in);

			DocumentBuilder newDocumentBuilder = factory.newDocumentBuilder();
			doc = newDocumentBuilder.parse(source);
		}
		return doc;
	}
	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 8.
	 * @param doc
	 * @param out
	 * @throws Exception
	 */
	public static void print(Document doc, OutputStream out) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		Result result = new StreamResult(out);
		transformer.transform(source, result);
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 1. 10. 
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static String toString(Document doc) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		print(doc, out);
		return out.toString();
	}
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 1. 10. 
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static String toString(Node doc) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		print(doc, out);
		return out.toString();
	}
	
	public static String toString(NodeList doc) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for(int i=0, len = doc.getLength();  i< len;  i++)
			print(doc.item(i), out );
		return out.toString();
	}
	
	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 18.
	 * @param node
	 * @param out
	 * @throws Exception
	 */
	public static void print(Node node, OutputStream out) throws Exception {
		print(node, out, false);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 4. 8.
	 * @param node
	 * @param out
	 * @param removeMetaTag
	 * @throws Exception
	 */
	public static void print(Node node, OutputStream out, boolean removeMetaTag) throws Exception {
		TransformerFactory newInstance = TransformerFactory.newInstance();

		Transformer transformer = newInstance.newTransformer();

		if (removeMetaTag) {
			// remove ?xml version .. meta tag
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		}

		DOMSource xmlSource = new DOMSource(node);

		StreamResult outputTarget = new StreamResult(out);
		transformer.transform(xmlSource, outputTarget);
	}

	/**
	 * @author KYJ
	 *
	 */
	public static class JaxbUtils {
		/**
		 * @작성자 : KYJ
		 * @작성일 : 2018. 5. 31.
		 * @param out
		 * @param v
		 * @return
		 * @throws JAXBException
		 * @throws TransformerFactoryConfigurationError
		 * @throws TransformerConfigurationException
		 */
		public static <T> void write(File out, T v) throws Exception {
			try (FileOutputStream stream = new FileOutputStream(out)) {
				write(stream, v);
			}
		}

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2019. 11. 6. 
		 * @param <T>
		 * @param out
		 * @param v
		 * @throws Exception
		 */
		public static <T> void write(OutputStream out, T v) throws Exception {
			JAXBContext context = JAXBContext.newInstance(v.getClass());
			Marshaller createMarshaller = context.createMarshaller();

			DOMResult domResult = new DOMResult();

			// createMarshaller.setProperty(name, value);
			createMarshaller.marshal(v, domResult);

			// try format xml
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
//			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			StreamResult outputTarget = new StreamResult(out);
			DOMSource xmlSource = new DOMSource(domResult.getNode());
			transformer.transform(xmlSource, outputTarget);

		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2018. 5. 31.
		 * @param stream
		 * @param requireType
		 * @throws Exception
		 */
		public static <T> T load(InputStream stream, Class<T> requireType) throws Exception {
			JAXBContext context = JAXBContext.newInstance(requireType);
			Unmarshaller um = context.createUnmarshaller();
			return (T) um.unmarshal(stream);
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2018. 6. 18.
		 * @param source
		 * @param requireType
		 * @return
		 * @throws Exception
		 */
		public static <T> T load(InputSource source, Class<T> requireType) throws Exception {
			JAXBContext context = JAXBContext.newInstance(requireType);
			Unmarshaller um = context.createUnmarshaller();
			return (T) um.unmarshal(source);
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2018. 6. 18.
		 * @param source
		 * @param requireType
		 * @return
		 * @throws Exception
		 */
		public static <T> T load(String source, Class<T> requireType) throws Exception {
			return load(new InputSource(new StringReader(source)), requireType);
		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 18.
	 * @param xslt
	 * @param dataXml
	 * @return
	 * @throws Exception
	 */
	public static String transform(String xslt, String dataXml) throws Exception {
		return transform(xslt.getBytes("UTF-8"), dataXml.getBytes("UTF-8"));
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 2. 13. 
	 * @param xslt
	 * @param dataXml
	 * @param prop
	 * @return
	 * @throws Exception
	 */
	public static String transform(String xslt, String dataXml, Map<String,Object> prop) throws Exception {
		return transform(xslt.getBytes("UTF-8"), dataXml.getBytes("UTF-8"), prop);
	}
	

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 18.
	 * @param xslt
	 * @param dataXml
	 * @return
	 * @throws Exception
	 */
	public static String transform(byte[] xslt, byte[] dataXml) throws Exception {
		return transform(xslt, dataXml, null);
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 2. 13. 
	 * @param xslt
	 * @param dataXml
	 * @param prop
	 * @return
	 * @throws Exception
	 */
	public static String transform(byte[] xslt, byte[] dataXml, Map<String,Object> prop) throws Exception {
		// TransformerFactory fac = TransformerFactory.newInstance();
		StreamSource xlstSource = new StreamSource(new ByteArrayInputStream(xslt));
		StreamSource dataSource = new StreamSource(new ByteArrayInputStream(dataXml));
		return transform(xlstSource, dataSource, prop);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 18.
	 * @param xlstSource
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	public static String transform(StreamSource xlstSource, StreamSource dataSource) throws Exception {
		return transform(xlstSource, dataSource, null);
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 2. 13. 
	 * @param xlstSource
	 * @param dataSource
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	public static String transform(StreamSource xlstSource, StreamSource dataSource, Map<String,Object> properties) throws Exception {
		
		TransformerFactory fac = TransformerFactory.newInstance();
		
		Transformer newTransformer = fac.newTransformer(xlstSource);
		if(properties!=null) {
			Iterator<Entry<String, Object>> it = properties.entrySet().iterator();
			while(it.hasNext())
			{
				Entry<String, Object> next = it.next();
				newTransformer.setParameter(next.getKey(), next.getValue());		
			}
		}
		
		
		newTransformer.setErrorListener(new ErrorListener() {

			@Override
			public void warning(TransformerException exception) throws TransformerException {
				LOGGER.error(ValueUtil.toString(exception));

			}

			@Override
			public void fatalError(TransformerException exception) throws TransformerException {
				LOGGER.error(ValueUtil.toString(exception));

			}

			@Override
			public void error(TransformerException exception) throws TransformerException {
				LOGGER.error(ValueUtil.toString(exception));
			}
		});
		
		
		newTransformer.setParameter("date", new java.util.Date());
		
		
		OutputFormat format = new OutputFormat();
        format.setIndent(true);
        format.setNewlines(true);
        format.setTrimText(false);
	
        
        StringWriter out = new StringWriter();
		Result r= new StreamResult(out);
		newTransformer.transform(dataSource, r);
		String string = out.toString();
		return string;
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com) @작성일 : 2018. 12. 18. @param
	 *      string @param encoding @return @throws Exception @throws
	 */
	public static Document fromString(String string, String encoding) throws Exception {
		return fromStream(new ByteArrayInputStream(string.getBytes(encoding)));
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 18.
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static Document fromStream(InputStream in) throws Exception {
		DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
		InputSource source = new InputSource(in);
		Document parse = newInstance.newDocumentBuilder().parse(source);
		return parse;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 19.
	 * @return
	 * @throws ParserConfigurationException
	 */
	public static Document newDocument() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();

		Document document = db.newDocument();
		return document;
	}

}
