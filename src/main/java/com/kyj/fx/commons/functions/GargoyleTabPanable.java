/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework
 *	작성일   : 2017. 9. 27.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.functions;

import com.kyj.fx.commons.fx.controls.dock.tab.DockTab;
import com.kyj.fx.commons.fx.controls.dock.tab.DockTabPane;

/**
 * Gargoyle 메인Frame에서 호출된 TabPane이면 구현할 수 있도록 한다.
 * 
 * @author KYJ
 *
 */
public interface GargoyleTabPanable {

	/**
	 * Gargoyle의 메인에서 현재 tabpane을 추가. <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 20.
	 * @param tabpane
	 * @Deprecated will be deprecated
	 */
	@Deprecated
	public void setTabPane(DockTabPane tabpane);

	/**
	 * 
	 * Gargoyle의 메인에서 현재 tab을 추가.(활성화된 탭) <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 20.
	 * @param tab
	 * @Deprecated will be deprecated
	 */
	@Deprecated
	public void setTab(DockTab tab);
	
	
	
	/**
	 * replace dockfx migration for java 11
	 *  
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 21. 
	 * @param station
	 */
//	public default void setDockStation(DockStation station) {
//		
//	};
	
	
	/**
	 * replace dockfx migration for java 11
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 21. 
	 * @param n
	 */
//	public default void setDockNode(DockNode n) {
//		
//	};

}
