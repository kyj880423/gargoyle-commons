/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author KYJ
 *
 */
public class RaxSAXPasrerUtil {

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
			throw new RuntimeException("this object is not contains XmlRootElement annotation");

		JAXBContext context = JAXBContext.newInstance(t.getClass());
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal(t, out);
	}

}
