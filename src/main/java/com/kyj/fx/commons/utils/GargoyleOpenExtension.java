/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 3. 2.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Gargoyle에서 커스텀 파일 열기 기능을 지원하는 <br/>
 * 기능을 수행하기 위해서 아래 인터페이스 구현. <br/>
 * 
 * gargoyle.extension.properteis 파일에 정의된 확장자에 따라 최종적으로 해당 Component가 <br/> 
 * 확장자에 맞게 열리게된다. <br/>
 * <br/>
 * <b> 중요] 열기 클래스는 기본 생성자가 필수로 존재해야한다. <br/>
 * </b>
 * 
 * @author KYJ
 *
 */
public interface GargoyleOpenExtension {

	/**
	 * 열기 가능한 확장자인지 여부 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 2.
	 * @param file
	 * @return
	 */
	public boolean canOpen(File file);

	/**
	 * 열기 로직 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 2.
	 * @param file
	 */
	public void setOpenFile(File file);

	/**
	 * 
	 * 대상을 팝업으로 열지를 결정함. <br/>
	 * 
	 * 이 함수는 Parent 클래스를 상속받는 클래스만 
	 * 가능. <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 3. 
	 * @return
	 */
	public default boolean isPopupOpen(){
		return false;
	}

	/**
	 * 인코딩 설정 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 24. 
	 * @param charset
	 */
	public default void setEncoding(Charset charset) {
		//Nothing.
	}

}
