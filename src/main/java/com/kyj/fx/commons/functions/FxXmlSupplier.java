/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.xml
 *	작성일   : 2018. 3. 9.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.functions;

import javafx.scene.Node;

/**
 *
 * XML 내용을 getXml하여 읽을 수 잇는 기능을 지원하는 객체에 사용 <br/>
 * 
 * @author KYJ
 *
 */
public interface FxXmlSupplier {

	/**
	 * 
	 * return xml <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 9.
	 * @return
	 */
	public String getXml();

	/**
	 * 해당 인터페이스를 사용하는 javafx 노드를 리턴하는걸 권고한다. <br/>
	 * 
	 * 팝업창을 호출하는 부모에 대한 메타정보를 주입하는데 사용한다 . <br/>
	 * 
	 * this를 리턴하는걸 권고 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 9.
	 * @return
	 */
	public Node getNode();

}
