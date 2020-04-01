/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2017. 7. 18.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.FactoryConfigurationError;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

/**
 * @author KYJ
 *
 */
public class XMLFormatter implements Formatter {

	private static final Logger LOGGER = LoggerFactory.getLogger(XMLFormatter.class);

	/**
	 * 
	 */
	public XMLFormatter() {

	}

	/**
	 * return new Instance.
	 * 
	 * @return
	 */
	public static XMLFormatter newInstnace() {
		return new XMLFormatter();
	}

	private boolean suppressDeclaration = true;

	public void setSuppressDeclaration(boolean suppressDeclaration) {
		this.suppressDeclaration = suppressDeclaration;
	}

	// public static void main(String[] args) throws IOException,
	// DocumentException {
	//
	// StringBuffer sb = new StringBuffer();
	// sb.append(
	// "<MT_PP0032_LIMStoMESAsync_S xmlns=\"http://samsungbiologics.com/PP\"
	// xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"
	// xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"
	// xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
	// sb.append(
	// "<requestString
	// xmlns=\"\"><TransactionRequest><Header><PlantId></PlantId><SourceSystem>LIMS</SourceSystem><DestinationSystem>MES</DestinationSystem><TransactionId>0A3235DA-D1A2-4DDC-AA5C-5DDBAD3DD1DD</TransactionId><TransactionType>SampleInformationUpdate</TransactionType></Header><Data><BatchNo>3104367</BatchNo><BatchType>Routine</BatchType><Samples><Sample><SampleId>B00033010003</SampleId><SampleDescription>AEX
	// Equilibration Buffer
	// v2</SampleDescription><SampleFieldCode>S1B12FT</SampleFieldCode><SampleStatus>Prelogged</SampleStatus><Inventory><InventoryId>2301954</InventoryId><TargetVolumeAmount>5</TargetVolumeAmount><TargetVolumeUOM>mL</TargetVolumeUOM><ContainerDescription>NovaSeptum
	// Syringe</ContainerDescription><StorageTemperature>2-8C</StorageTemperature><PurposeOfSample>Retain</PurposeOfSample></Inventory><Inventory><InventoryId>2301955</InventoryId><TargetVolumeAmount>5</TargetVolumeAmount><TargetVolumeUOM>mL</TargetVolumeUOM><ContainerDescription>NovaSeptum
	// Syringe</ContainerDescription><StorageTemperature>2-8C</StorageTemperature><PurposeOfSample>Retain</PurposeOfSample></Inventory><Inventory><InventoryId>2301956</InventoryId><TargetVolumeAmount>5</TargetVolumeAmount><TargetVolumeUOM>mL</TargetVolumeUOM><ContainerDescription>NovaSeptum
	// Syringe</ContainerDescription><StorageTemperature>2-8C</StorageTemperature><PurposeOfSample>Endotoxin</PurposeOfSample></Inventory><Inventory><InventoryId>2301957</InventoryId><TargetVolumeAmount>100</TargetVolumeAmount><TargetVolumeUOM>mL</TargetVolumeUOM><ContainerDescription>NovaSeptum
	// Bag</ContainerDescription><StorageTemperature>2-8C</StorageTemperature><PurposeOfSample>Spec
	// Scan
	// Blank</PurposeOfSample></Inventory><Inventory><InventoryId>2301958</InventoryId><TargetVolumeAmount>100</TargetVolumeAmount><TargetVolumeUOM>mL</TargetVolumeUOM><ContainerDescription>NovaSeptum
	// Bag</ContainerDescription><StorageTemperature>2-8C</StorageTemperature><PurposeOfSample>Spec
	// Scan
	// Blank</PurposeOfSample></Inventory></Sample><Sample><SampleId>B00033010004</SampleId><SampleDescription>UFDF
	// Conditioning Buffer
	// v2</SampleDescription><SampleFieldCode>S1B14PF</SampleFieldCode><SampleStatus>Started</SampleStatus><Inventory><InventoryId>2301963</InventoryId><TargetVolumeAmount>5</TargetVolumeAmount><TargetVolumeUOM>mL</TargetVolumeUOM><ContainerDescription>Polystyrene
	// Tube</ContainerDescription><StorageTemperature>2-8C</StorageTemperature><PurposeOfSample>Endotoxin</PurposeOfSample></Inventory><Inventory><InventoryId>2301964</InventoryId><TargetVolumeAmount>5</TargetVolumeAmount><TargetVolumeUOM>mL</TargetVolumeUOM><ContainerDescription>Polystyrene
	// Tube</ContainerDescription><StorageTemperature>2-8C</StorageTemperature><PurposeOfSample>Retain</PurposeOfSample></Inventory><Inventory><InventoryId>2301965</InventoryId><TargetVolumeAmount>5</TargetVolumeAmount><TargetVolumeUOM>mL</TargetVolumeUOM><ContainerDescription>Polystyrene
	// Tube</ContainerDescription><StorageTemperature>2-8C</StorageTemperature><PurposeOfSample>Retain</PurposeOfSample></Inventory><Inventory><InventoryId>2301966</InventoryId><TargetVolumeAmount>100</TargetVolumeAmount><TargetVolumeUOM>mL</TargetVolumeUOM><ContainerDescription>Polystyrene
	// Bottle</ContainerDescription><StorageTemperature>2-8C</StorageTemperature><PurposeOfSample>Aliquot</PurposeOfSample></Inventory></Sample></Samples></Data></TransactionRequest></requestString>\n");
	// sb.append("</MT_PP0032_LIMStoMESAsync_S>\n");
	//
	// System.out.println(new XMLFormatter().format(sb.toString()));
	// }

	/**
	 * XML Formatting 기능 지원.
	 * 
	 * 17.09.08 by kyj. XML 텍스트에 대한 인코딩때문에 에러 발생. XML텍스트를 StringReader로 읽은후 포멧팅
	 * 처리. (인코딩 처리에 대한 옵션을 제공해줌.)
	 * 
	 * 그후 원본 인코딩을 유지하기 위해 인코딩값이 있으면 그 인코딩값으로 재구성. (out.toString(charset) 참조.)
	 * 
	 */
	@Override
	public String format(String str) {

		if (str == null || str.isEmpty())
			return str;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document doc = null;
		String xmlEncoding = null;
		XMLWriter xmlWriter = null;
		SAXReader reader = null;

		// try {
		// XMLStreamReader createXMLStreamReader =
		// XMLInputFactory.newInstance().createXMLStreamReader(characterStream);
		// xmlEncoding = createXMLStreamReader.getEncoding();
		// } catch (XMLStreamException | FactoryConfigurationError e1) {
		// //e1.printStackTrace();
		// }

		try {
			reader = new SAXReader();

			// XMLStreamReader xsr =
			StringReader characterStream = new StringReader(str);
			InputSource in = new InputSource(characterStream);
			xmlEncoding = in.getEncoding();

			doc = reader.read(in);

			// String en1 = in.getEncoding();
			xmlEncoding = doc.getXMLEncoding();

			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setSuppressDeclaration(suppressDeclaration);
			// format.setTrimText(true);
			xmlWriter = new XMLWriter(out, format);
			xmlWriter.write(doc.getDocument());
		} catch (DocumentException | IOException e) {
			throw new RuntimeException(e);
		} catch (FactoryConfigurationError e) {
			throw new RuntimeException(e);
		} finally {
			close(xmlWriter);
		}
		if (ValueUtil.isNotEmpty(xmlEncoding)) {
			try {
				return out.toString(xmlEncoding);
			} catch (UnsupportedEncodingException e) {
				LOGGER.error(ValueUtil.toString(e));
				return out.toString();
			}
		}
		return out.toString();
	}

	/**
	 * 속성까지 정렬
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 1. 19.
	 * @param str
	 * @return
	 */
	public String formatWithAttributes(String str) {

		if (str == null || str.isEmpty())
			return str;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document doc = null;
		String xmlEncoding = null;
		XMLAttibuteFormatWriter xmlWriter = null;
		SAXReader reader = null;

		// try {
		// XMLStreamReader createXMLStreamReader =
		// XMLInputFactory.newInstance().createXMLStreamReader(characterStream);
		// xmlEncoding = createXMLStreamReader.getEncoding();
		// } catch (XMLStreamException | FactoryConfigurationError e1) {
		// //e1.printStackTrace();
		// }

		try {
			reader = new SAXReader();
			// reader.setValidation(false);

			// XMLStreamReader xsr =
			StringReader characterStream = new StringReader(str);
			InputSource in = new InputSource(characterStream);
			xmlEncoding = in.getEncoding();
			if (ValueUtil.isNotEmpty(xmlEncoding)) {
				reader.setEncoding(xmlEncoding);
			} else {
				reader.setEncoding("UTF-8");
			}

			// reader.setStripWhitespaceText(true);
			// reader.setIgnoreComments(true);

			doc = reader.read(in);

			// String en1 = in.getEncoding();
			xmlEncoding = doc.getXMLEncoding();

			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setSuppressDeclaration(suppressDeclaration);
			format.setExpandEmptyElements(true);
			format.setNewLineAfterNTags(10);
			format.setNewLineAfterDeclaration(true);
			// format.setXHTML(true);

			// StringWriter writer = new StringWriter();
			// JAXBContext jc = JAXBContext.newInstance(MODEL);
			// Marshaller marshaller = jc.createMarshaller();
			// marshaller.marshal(node, new WriteOutContentHandler(writer));
			// XMLSerializer serializer = new XMLSerializer(format);
			// Writer stringWriter = new StringWriter();
			// serializer.setOutputCharStream(stringWriter);

			// marshaller.marshal(target, new WriteOutContentHandler());
			// results = stringWriter.toString();

			xmlWriter = new XMLAttibuteFormatWriter(out, format);
			xmlWriter.write(doc.getDocument());
		} catch (DocumentException | IOException e) {
			throw new RuntimeException(e);
		} catch (FactoryConfigurationError e) {
			throw new RuntimeException(e);
		} finally {
			close(xmlWriter);
		}
		if (ValueUtil.isNotEmpty(xmlEncoding)) {
			try {
				return out.toString(xmlEncoding);
			} catch (UnsupportedEncodingException e) {
				LOGGER.error(ValueUtil.toString(e));
				return out.toString();
			}
		}
		return out.toString();

	}

	private void close(XMLAttibuteFormatWriter xmlWriter) {
		try {
			if (xmlWriter != null)
				xmlWriter.close();
		} catch (IOException e) {
		}
	}

	private void close(XMLWriter xmlWriter) {
		try {
			if (xmlWriter != null)
				xmlWriter.close();
		} catch (IOException e) {
		}
	}

	public String getEmptyString(int depth) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < depth; i++)
			sb.append(" ");
		return sb.toString();
	}

	@Override
	public String toUpperCase(String source) {

		return null;
	}

	@Override
	public String toLowerCase(String source) {

		return null;
	}

	@Override
	public String split(String source, int position) {

		return null;
	}

	/**
	 * xml 문장에서 개행문자를 제거. <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 18.
	 * @param str
	 * @return
	 */
	public String removeSpace(String str) {
		return ValueUtil.removeSpace(str);				
	}
}
