/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.contextmenu
 *	작성일   : 2017. 4. 10.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * 
 * 컨텐스트 메뉴를 관리하기 위한 매니저 클래스
 * 
 * @author KYJ
 *
 */
public class FxContextManager {

	private ContextMenu menu;
	private Node anchor;

	/** [start] 생성자 ****************************************************/
	public FxContextManager(Node anchor) {
		this(anchor, new ContextMenu());
	}

	public FxContextManager(Node anchor, MenuItem... items) {
		this(anchor, new ContextMenu(), items);
	}

	public FxContextManager(Node anchor, ContextMenu menu, MenuItem... items) {
		this.anchor = anchor;
		this.menu = menu;
		this.menu.getItems().addAll(items);
	}

	/** [end] 생성자 ****************************************************/

	public void addNewMenu(MenuItem item) {
		this.menu.getItems().add(item);
	}

	public MenuItem getMenuItem() {
		return new MenuItem();
	}

	public ContextMenu getContextMenu() {
		return this.menu;
	}

	/**
	 * 마우스 우클릭시 컨텍스트 메뉴를 보여주기 위한 기능을
	 * 추가함.
	 * @작성자 : KYJ
	 * @작성일 : 2017. 4. 10. 
	 */
	public void install() {
		this.anchor.addEventHandler(MouseEvent.MOUSE_CLICKED, this::applicationContextOnAction);
	}

	/**
	 * 기능을 해제하기위한 처리.
	 * @작성자 : KYJ
	 * @작성일 : 2017. 4. 10. 
	 */
	public void remove() {
		this.menu.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::applicationContextOnAction);
	}

	/**
	 * 컨테스트 메뉴가 보여질때 
	 * 아래 함수가 호출됨.
	 * @작성자 : KYJ
	 * @작성일 : 2017. 4. 10. 
	 * @param e
	 */
	protected void applicationContextOnAction(MouseEvent e) {
		this.menu.hide();
		if (e.getButton() == MouseButton.SECONDARY && e.getClickCount() == 1)
			this.menu.show(anchor, e.getScreenX(), e.getScreenY());
	}
}
