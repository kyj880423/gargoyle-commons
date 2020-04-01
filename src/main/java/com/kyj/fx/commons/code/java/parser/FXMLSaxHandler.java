/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : DaoWizardView
 *	작성일   : 2016. 06. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.java.parser;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.kyj.fx.commons.utils.ValueUtil;

import javafx.fxml.FXMLLoader;

/***************************
 *
 * FXML 파서 처리를 위한 핸들러.
 *
 * @author KYJ
 *
 ***************************/
public class FXMLSaxHandler extends DefaultHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(FXMLSaxHandler.class);
	/**
	 * 컨트롤러 키워드
	 *
	 * @최초생성일 2016. 6. 18.
	 */
	private static final String FX_CONTROLLER = FXMLLoader.FX_NAMESPACE_PREFIX + ":" + FXMLLoader.FX_CONTROLLER_ATTRIBUTE;

	private XMLOutputFactory factory;
	private XMLStreamWriter writer;

	private Set<String> qNames = new HashSet<>();

	private List<FXTextElement> fxTextValues = new ArrayList<>();

	/**
	 * @param out
	 *            파서처리가 끝난후 개발자는 out값을 가지고 후처리를 진행한다.
	 * @throws FileNotFoundException
	 * @throws XMLStreamException
	 */
	public FXMLSaxHandler(OutputStream out) throws FileNotFoundException, XMLStreamException {
		factory = XMLOutputFactory.newInstance();
		writer = factory.createXMLStreamWriter(out);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void startDocument() throws SAXException {

		try {
			writer.writeStartDocument("UTF-8", "1.0");
			writer.writeCharacters(System.lineSeparator());
		} catch (XMLStreamException e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void processingInstruction(String arg0, String arg1) throws SAXException {
		super.processingInstruction(arg0, arg1);

		/* import문같은경우 xml 파서 처리에 어려움이 있다. */
		if ("import".equals(arg0)) {

			// javafx 에서 클래스선언을 의미하는 키워드로 사용되나 정상적인 표준 XML표기 방법은 아님. XML처리를 위해
			// 특수한 처리가 필요함.
			String format = "<?import %s?>";

			try {

				String str = String.format(format, arg1);

				// CData형태로 import문에 대한 정보를 처리하고 본 클래스를 다루는 개발자는 그 결과를 다시 변환해서
				// 사용하도록 해야한다.
				writer.writeCData(str);
				writer.writeCharacters(System.lineSeparator());
			} catch (XMLStreamException e) {
				LOGGER.error(ValueUtil.toString(e));
			}
		}

	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		//
		try {

			if (ValueUtil.isNotEmpty(qName))
				qNames.add(qName);

			//
			writer.writeStartElement(uri, qName, qName);

			for (int i = 0; i < attributes.getLength(); i++) {
				String value = attributes.getValue(i);
				String localQName = attributes.getQName(i);
				// 미리보기 기능을 구현하기위해 FXML에서 사용되는 특수한 처리를 의미하는 값들은 제거해준다.
				if (value != null) {
					if (value.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX))
						continue;

					// 경로 표시.
					else if (value.startsWith(FXMLLoader.RELATIVE_PATH_PREFIX))
						continue;

					// 다국어처리시 아래 리소스. 2016-06-19 다국어 지원을 위해 주석을 다시 해제.
					// else if
					// (value.startsWith(FXMLLoader.RESOURCE_KEY_PREFIX))
					// continue;

					// 컨트롤러의 정의는 생략처리.
					else if (FX_CONTROLLER.equals(localQName))
						continue;

					//javafx에서 text속성에 대한 정보를 발견하면 fxTextValues에 추가한다.
					if ("text".equals(localQName)) {
						this.fxTextValues.add(new FXTextElement(uri, localName, qName, value));
					}

					writer.writeAttribute(attributes.getQName(i), value);
				}

			}
			writer.writeCharacters(System.lineSeparator());
		} catch (XMLStreamException e) {
			LOGGER.error(ValueUtil.toString(e));
		}

	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void endElement(String arg0, String arg1, String arg2) throws SAXException {
		try {
			writer.writeEndElement();
		} catch (XMLStreamException e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	/********************************
	 * 작성일 : 2016. 6. 19. 작성자 : KYJ
	 *
	 * 문서파싱과정에서 QName(여기서는 Node객체이름들을 리턴.)
	 *
	 * @return
	 ********************************/
	public Set<String> getAllQNames() {
		return qNames;
	}

	/********************************
	 * 작성일 : 2016. 6. 19. 작성자 : KYJ
	 *
	 * FXML에서 파싱의 결과로 찾아낸 text속성을 리턴.
	 *
	 * @return
	 ********************************/
	public List<FXTextElement> getFxTextValues() {
		return fxTextValues;
	}

}