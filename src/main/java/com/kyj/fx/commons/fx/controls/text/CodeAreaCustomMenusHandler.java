/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2016. 10. 20.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import org.fxmisc.richtext.CodeArea;

import javafx.scene.control.ContextMenu;

/**
 * 특화된 컨텍스트 메뉴에 대한 처리를 지원하기 위해 생성
 * @author KYJ
 *
 */
public interface CodeAreaCustomMenusHandler<T extends CodeArea> {

	/**
	 * CodeArea에서 메뉴를 추가할경우 아래를 구현.
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 20.
	 * @param codeArea
	 * @param contextMenu
	 */
	public void customMenus(T codeArea, ContextMenu contextMenu);

}
