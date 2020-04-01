/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2016. 3. 16.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.kyj.fx.commons.exceptions.GargoyleException;

/**
 * XML 관련 처리 유틸리티 클래스
 *
 * @author KYJ
 *
 */
@SuppressWarnings("unchecked")
public class SAXPasrerUtil {

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(SAXPasrerUtil.class);

	/**
	 * 특정객체를 파일로 저장
	 *
	 * 단.. 클래스에 XmlRootElement 어노테이션이 붙어 있을것
	 * 
	 * <FIX> 2018.04.12 out이라는 Function을 새로 만들고 그 함수를 이용하도록 코드 수정 </FIX>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 3. 16.
	 * @param file
	 * @param job
	 * @throws Exception
	 */
	public static <T> void saveXml(File file, T job) throws Exception {
		if (file == null)
			throw new NullPointerException("File is null.");

		if (!file.exists())
			file.createNewFile();

		try (FileOutputStream out = new FileOutputStream(file, false)) {
			out(job, out);
		}
	}

	/**
	 * 파일로부터 xml 로딩
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 3. 16.
	 * @param file
	 * @param requireType
	 * @return
	 * @throws Exception
	 */

	public static <T> T loadXml(File file, Class<T> requireType) throws Exception {
		JAXBContext context = JAXBContext.newInstance(requireType);
		Unmarshaller um = context.createUnmarshaller();
		return (T) um.unmarshal(file);
	}

	/**
	 * 스트림 객체로부터 xml 로딩
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 3. 16.
	 * @param stream
	 * @param requireType
	 * @return
	 * @throws Exception
	 */
	public static <T> T loadXml(InputStream stream, Class<T> requireType) throws Exception {
		JAXBContext context = JAXBContext.newInstance(requireType);
		Unmarshaller um = context.createUnmarshaller();
		return (T) um.unmarshal(stream);
	}

	/********************************
	 * 작성일 : 2016. 6. 18. 작성자 : KYJ
	 *
	 *
	 * @param f
	 * @return
	 * @throws Exception
	 ********************************/
	public static List<String> getAllQNames(File f) throws Exception {
		try (FileInputStream is = new FileInputStream(f)) {
			List<String> allQNames = getAllQNames(is);
			return allQNames;
		}
	}

	/**
	 * 유틸리티 클래스에서 사용하는 디폴트 핸들러
	 *
	 * @author KYJ
	 *
	 */
	public static class SAXHandler extends Handler<String> {
		protected List<String> arrayList = new ArrayList<String>();

		@Override
		public void startElement(String url, String arg1, String qName, Attributes arg3) throws SAXException {
			if (ValueUtil.isNotEmpty(qName))
				arrayList.add(qName);
		}

		public List<String> getList() {
			return arrayList;
		}

	}

	public static abstract class Handler<T> extends DefaultHandler {
		protected List<T> arrayList = new ArrayList<T>();

		// @Override
		// public abstract void startElement(String url, String arg1, String
		// qName, Attributes arg3) throws SAXException;

		public List<T> getList() {
			return arrayList;
		}

	}

	public static List<String> getAllQNames(InputStream is) throws Exception {
		return getAllQNames(is, new SAXHandler());
	}

	public static List<String> getAllQNames(InputStream is, SAXHandler defaultHandler) throws Exception {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		sp.parse(is, defaultHandler);
		return defaultHandler.getList();
	}

	public static void simpleSaxHandler(InputStream is, DefaultHandler defaultHandler) throws Exception {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		sp.parse(is, defaultHandler);
	}

	public static <T> void getAll(InputStream is, DefaultHandler handler) throws Exception {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		sp.parse(is, handler);
	}

	public static <T> List<T> getAll(InputStream is, Handler<T> handler) throws Exception {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		sp.parse(is, handler);
		return handler.getList();
	}

	public static <T> List<T> getAll(InputStream is, Handler<T> handler, Consumer<Exception> exceptionHandler) {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			sp.parse(is, handler);
		} catch (Exception e) {
			exceptionHandler.accept(e);
		}
		return handler.getList();
	}

	/**
	 * @XmlRootElement 어노테이션이 포함된 클래스에 한해서 <br/>
	 *                 관련 클래스의 속성들을 <br/>
	 *                 XML 구성 문자열로 변환한다. <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 12.
	 * @param t
	 * @return
	 * @throws JAXBException
	 * @throws GargoyleException
	 */
	public static <T> String toString(T t) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out(t, out);
		return out.toString();
	}

	/**
	 * Object Xml 출력 함수
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 12.
	 * @param t
	 * @param out
	 * @throws JAXBException
	 * @throws GargoyleException
	 */
	public static <T> void out(T t, OutputStream out) throws Exception {

		if (t == null || t.getClass().getAnnotation(XmlRootElement.class) == null)
			throw new GargoyleException("this object is not contains XmlRootElement annotation");

		JAXBContext context = JAXBContext.newInstance(t.getClass());
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal(t, out);
	}

}
