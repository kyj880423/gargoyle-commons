/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.file.conversion
 *	작성일   : 2018. 3. 6.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 파일 내용 변환작업을 대표하는 인터페이스 <br/>
 * 
 * @author KYJ
 *
 */
public interface FileContentConversionable {

	/**
	 * Read Stream <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 6.
	 * @param in
	 */
	public void in(InputStream in);

	/**
	 * Output Stream <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 6.
	 * @param out
	 */
	public void out(OutputStream out);

}
