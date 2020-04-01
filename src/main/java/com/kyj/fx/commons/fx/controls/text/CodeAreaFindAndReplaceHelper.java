/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2016. 12. 14.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import org.fxmisc.richtext.CodeArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.fx.controls.srch.TextSearchAndReplaceView;

import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * CodeArea의 FindAndReplace에 대한 공통기능을 처리할 수 있게 도와주는 클래스.
 * 
 * @author KYJ
 *
 */
public class CodeAreaFindAndReplaceHelper<T extends CodeArea> {
	
	private static Logger LOGGER = LoggerFactory.getLogger(CodeAreaFindAndReplaceHelper.class);

	private CodeArea codeArea;
	protected Menu menuSearch;
	protected MenuItem miFindReplace;
	// private EventHandler<? super MouseEvent> defaultSelectionHandler;;

	public CodeAreaFindAndReplaceHelper(T codeArea) {
		this.codeArea = codeArea;
	}

	/**
	 * FindAndReplace에 대한 메뉴를 정의.
	 * 
	 * @return
	 * @작성자 : KYJ
	 * @작성일 : 2017. 1. 13.
	 */
	public Menu createMenus() {
		menuSearch = new Menu("Search");
		miFindReplace = new MenuItem("Find/Replace");
		
		/*
		 *  17.09.29 Fix unexpected Shown Dialog.
		 * 
		 */
		
		// miFindReplace.setAccelerator(new KeyCodeCombination(KeyCode.F,
		// KeyCombination.CONTROL_DOWN));
		this.codeArea.addEventHandler(KeyEvent.KEY_PRESSED, ev->{
			if (ev.getCode() == KeyCode.F && ev.isControlDown() && !ev.isAltDown() && !ev.isShiftDown()) {
				findReplaceEvent(ev);
			}
		});
		miFindReplace.setOnAction(this::findReplaceEvent);
		menuSearch.getItems().add(miFindReplace);
		return menuSearch;
	}

	/**
	 * 찾기 바꾸기 이벤트
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 13.
	 * @param e
	 */
	public void findReplaceEvent(Event e) {

		if (e.isConsumed())
			return;

		ObservableValue<String> textProperty = codeArea.textProperty();
		TextSearchAndReplaceView textSearchView = new TextSearchAndReplaceView(codeArea, textProperty);

		textSearchView.setOnSearchResultListener((vo) -> {

			switch (vo.getSearchType()) {
			case SEARCH_SIMPLE: {
				int startIndex = vo.getStartIndex();
				int endIndex = vo.getEndIndex();
				
				codeArea.selectRange(startIndex, endIndex);
				// move to line
				codeArea.showParagraphAtTop(codeArea.getCurrentParagraph());
				LOGGER.debug(String.format("find text : %s startIdx :%d endIdx :%d", vo.getSearchText(), startIndex, endIndex));
				break;
			}
			case SEARCH_ALL: {
				int startIndex = vo.getStartIndex();
				String searchText = vo.getSearchText();
				String replaceText = vo.getReplaceText();

				// codeArea.getUndoManager().mark();
				// codeArea.replaceText(startIndex, (startIndex +
				// searchText.length()), replaceText);

				setContent(startIndex, startIndex + searchText.length(), replaceText);
				// codeArea.getUndoManager().mark();

				break;
			}
			}

		});

		textSearchView.setOnReplaceResultListener(vo -> {
			switch (vo.getReaplceType()) {
			case SIMPLE: {
				String reaplceResult = vo.getReaplceResult();
				setContent(reaplceResult);
				break;
			}
			case ALL: {
				String reaplceResult = vo.getReaplceResult();
				setContent(reaplceResult);
				break;
			}
			}
		});

		textSearchView.setOnSelectionMoveListener(vo -> {
			codeArea.selectRange(vo.getStartIndex(), vo.getEndIndex());
			codeArea.showParagraphAtTop(codeArea.getCurrentParagraph());
		});

		textSearchView.isSelectScopePropertyProperty().addListener((oba, oldval, newval) -> {
			if (newval)
				LOGGER.debug("User Select Locale Scope..");
			else
				LOGGER.debug("User Select Gloval Scope..");
		});

		codeArea.setOnMouseClicked(event -> {

			IndexRange selection = codeArea.getSelection();
			int start = selection.getStart();
			textSearchView.setSlidingStartIndexProperty(start);

		});

		textSearchView.show();

		// codeArea.setOnMouseClicked(defaultSelectionHandler);

		e.consume();
	}

	public void setContent(String content) {
		codeArea.getUndoManager().mark();
		codeArea.clear();
		codeArea.replaceText(0, 0, content);
		codeArea.getUndoManager().mark();
	}

	public void setContent(int start, int end, String text) {
		codeArea.getUndoManager().mark();
		codeArea.replaceText(start, end, text);
		codeArea.getUndoManager().mark();
	}

	// // 선택 범위 지정
	// EventHandler<? super MouseEvent> defaultSelectionHandler = event -> {
	// if (event.getClickCount() == 1) {
	// // codeArea.setStyleSpans(0,
	// // groupBackgroundColor(codeArea.getText(),
	// // codeArea.getCaretPosition()));
	// } else if (event.getClickCount() == 2) {
	// String selectedText = codeArea.getSelectedText();
	// if (ValueUtil.isNotEmpty(selectedText)) {
	// IndexRange selection = codeArea.getSelection();
	// String ltrimText = selectedText.replaceAll("^\\s+", "");
	// String firstStr = ltrimText.substring(0, 1).replaceAll(CHARACTERS_MATCH,
	// "");
	// int start = selection.getStart();
	// int end = selection.getEnd();
	// codeArea.selectRange(start + (selectedText.length() - ltrimText.length()
	// + 1 - firstStr.length()), end);
	// }
	// }
	// };
}
