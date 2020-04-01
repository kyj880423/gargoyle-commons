/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2017. 7. 14.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.util.function.Consumer;

import org.controlsfx.control.PopOver;

import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

/**
 * @author KYJ
 *
 */
class CodeAreaClipboardItemListView extends ListView<ClipboardContent> {

	private PopOver window;

	public CodeAreaClipboardItemListView() {

		// List<ClipboardContent> clipBoardItems = parent.getClipBoardItems();
		setCellFactory(TextFieldListCell.forListView(new StringConverter<ClipboardContent>() {

			@Override
			public String toString(ClipboardContent object) {
				return object.getString();
			}

			@Override
			public ClipboardContent fromString(String string) {
				return null;
			}
		}));
		setOnKeyPressed(this::onKeyPress);
		setOnMouseClicked(this::onMouseClick);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 9.
	 * @param e
	 */
	void onMouseClick(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
			if (e.isConsumed())
				return;

			ClipboardContent selectedItem = getSelectionModel().getSelectedItem();
			if (selectItemAction(selectedItem))
				this.window.hide();

			e.consume();
		}
	}

	void onKeyPress(KeyEvent e) {
		if (e.getCode() == KeyCode.ESCAPE) {
			if (e.isConsumed())
				return;

			if (this.window != null) {
				this.window.hide();
			}

			e.consume();
		} else if (e.getCode() == KeyCode.ENTER) {
			if (e.isConsumed())
				return;

			ClipboardContent selectedItem = getSelectionModel().getSelectedItem();
			if (selectItemAction(selectedItem))
				this.window.hide();
			e.consume();
		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 9.
	 * @param selectedItem
	 */
	private boolean selectItemAction(ClipboardContent selectedItem) {
		if (selectedItem == null)
			return false;

		if (this.window != null) {
			if (selectItemAction != null) {
				selectItemAction.accept(selectedItem.getString());
			}
		}
		return true;
	}

	public void setPopOver(PopOver window) {
		this.window = window;
	}

	private Consumer<String> selectItemAction;

	public void onClose(Consumer<String> selectItemAction) {
		this.selectItemAction = selectItemAction;
	}
}
