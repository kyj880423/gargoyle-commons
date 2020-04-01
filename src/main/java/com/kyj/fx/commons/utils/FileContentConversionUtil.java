/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2018. 3. 6.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.File;
import java.nio.charset.Charset;

/**
 * @author KYJ
 *
 */
class FileContentConversionUtil {

	public static String conversion(File f) {
		GargoyleFileContConversionPolicy m = new GargoyleFileContConversionPolicy();
		return m.conversion(f);
	}
	
	/**
	 * 파일 타입에 따라 적절하게 내용을 <br/>
	 * 사람이 읽을 수 있는 텍스트로 변환하는 API <br/>
	 * 
	 * gargoyle.cont.conversion.properties파일에 등록된 내용을 참조 <br/>
	 * Conversion 클래스는 AbstractStringFileContentConversion.java와 상속관계여야만 함 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 6.
	 * @param f
	 * @param encoding
	 * @return
	 */
	public static String conversion(File f, Charset encoding) {
		GargoyleFileContConversionPolicy m = new GargoyleFileContConversionPolicy();
		return m.conversion(f, encoding);
	}
}
