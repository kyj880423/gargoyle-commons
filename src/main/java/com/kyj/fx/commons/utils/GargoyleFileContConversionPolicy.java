/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 3. 2.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KYJ
 *
 */
public class GargoyleFileContConversionPolicy {

	private static final Logger LOGGER = LoggerFactory.getLogger(GargoyleFileContConversionPolicy.class);
	private static final File EXTENSION_PROPERTY = new File("gargoyle.cont.conversion.properties");
	private Properties properties;

	private static GargoyleFileContConversionPolicy INSTANCE;

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 2.
	 * @return
	 */
	public synchronized static GargoyleFileContConversionPolicy getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GargoyleFileContConversionPolicy();
		}
		return INSTANCE;
	}

	public GargoyleFileContConversionPolicy() {
		if (EXTENSION_PROPERTY.exists()) {
			properties = new Properties();
			try (FileInputStream inStream = new FileInputStream(EXTENSION_PROPERTY)) {
				properties.load(inStream);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @작성자 : KYJ <br/>
	 * @작성일 : 2018. 3. 2. <br/>
	 * @param openFile <br/>
	 * 
	 * 
	 * 2019. 01. 24 default encoding이 UTF-8이 아닌 <br/>, 
	 * 파일 인코딩이 존재하는지 체크한후 불러들임. <br/>
	 */
	public String conversion(File openFile) {
		return conversion(openFile, FileUtil.getFileExtension(openFile), Charset.forName(FileUtil.findEncoding(openFile)));
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 6.
	 * @param openFile
	 * @param encoding
	 * @return
	 */
	public String conversion(File openFile, Charset encoding) {
		return conversion(openFile, FileUtil.getFileExtension(openFile), encoding);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 2.
	 * @param openFile
	 * @param extensionName
	 * @param encoding
	 */
	public String conversion(File openFile, String extensionName, Charset encoding) {
		if(openFile == null || !openFile.exists())
			throw new NullPointerException("File empty");
		
		if(openFile.isDirectory())
			throw new RuntimeException(openFile.getName() + " is a directory.");
		
		try (FileInputStream in = new FileInputStream(openFile)) {
			return conversion(extensionName, in, encoding);
		} catch (Exception e) {
			throw new RuntimeException("GargoyleFileContConversionPolicy Read Failed. " + openFile.getName(), e);
		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 14.
	 * @param extensionName
	 * @param in
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public String conversion(String extensionName, InputStream in, Charset charset) throws Exception {
		if(this.properties == null)
			throw new Exception("gargoyle.cont.conversion.properties does not loaded. ");
		Object object = this.properties.get(extensionName.toLowerCase());
		String cont = "";

		// 등록되어 있지 않으면 일반 텍스트 리턴
		if (ValueUtil.isEmpty(object))
			return ValueUtil.toString(in, charset);

		String strClazz = object.toString();

		Class<?> forName = Class.forName(strClazz);

		if (forName != null) {
			Object newInstance = forName.newInstance();

			// check essential implementation
			if (newInstance != null && newInstance instanceof AbstractStringFileContentConversion) {
				AbstractStringFileContentConversion ext = (AbstractStringFileContentConversion) newInstance;
				ext.in(in);
				ext.setEncoding(charset);
				cont = ext.conversion();
			}
		}

		return cont;
	}
}
