/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2016. 10. 6.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.math.BigDecimal;
import java.util.Optional;

import org.controlsfx.control.PopOver;
import org.fxmisc.richtext.CodeArea;

import com.kyj.fx.commons.utils.DialogUtil;
import com.kyj.fx.commons.utils.SqlFormatter;
import com.kyj.fx.commons.utils.ValueUtil;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

/**
 *
 * 코드 처리 관련 Helper 클래스 <br/>
 * <br/>
 * CodeArea클래스와 연관된 모든 공통처리내용이 구현된다. <br/>
 * <br/>
 * 2017.01.13 FindAndReplace를 별도의 Helper 클래스로 변경처리. by kyj. <br/>
 * 2018.12.04 String 관련 드래그 드롭 기능 추가. <br/>
 * <br/>
 * 
 * @author KYJ
 *
 */
public class CodeAreaHelper<T extends CodeArea> {

	// private static Logger LOGGER =
	// LoggerFactory.getLogger(CodeAreaHelper.class);

	public static final String CHARACTERS_MATCH = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";

	protected T codeArea;
	protected SqlFormatter sqlFormatter = new SqlFormatter();
	protected CodeAreaMoveLineHelper codeMoveDeligator;
	protected AbstractDragDropHelper<T> dragDropHelper;
	protected CodeAreaFindAndReplaceHelper<T> findAndReplaceHelper;
	protected AbstractDragDropHelper<T> stringDragDropHelper;

	protected MenuItem menuMoveToLine;
	protected MenuItem miToUppercase;
	protected MenuItem miToLowercase;

	// 선택 범위 지정
	protected EventHandler<? super MouseEvent> defaultSelectionHandler;

	private ObjectProperty<ContextMenu> menu = new SimpleObjectProperty<ContextMenu>();

	public CodeAreaHelper(T codeArea) {
		this.codeArea = codeArea;
		createContextMenu();
		init();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 9.
	 */
	protected void createContextMenu() {
		menu.set(new ContextMenu());
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 5.
	 */
	protected void init() {
		defaultSelectionHandler = new CodeAreaDefaultSelectionHandler(codeArea);
		this.codeArea.setOnMouseClicked(defaultSelectionHandler);
//		this.codeArea.addEventHandler(MouseEvent.MOUSE_CLICKED, defaultSelectionHandler);
		this.codeMoveDeligator = new CodeAreaMoveLineHelper(codeArea);
		this.dragDropHelper = new CodeAreaFileDragDropHelper<>(codeArea);
		this.stringDragDropHelper = new CodeAreaStringDragDropHelper<>(codeArea);
		this.findAndReplaceHelper = new CodeAreaFindAndReplaceHelper<>(codeArea);

		createMenus();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 9.
	 * @return
	 */
	public ContextMenu getContextMenu() {
		return this.getMenu();
	}

	public CodeArea getCodeArea() {
		return this.codeArea;
	}

	/**
	 *
	 * 2016-10-27 키 이벤트를 setAccelerator를 사용하지않고 이벤트 방식으로 변경 이유 : 도킹기능을 적용하하면
	 * setAccelerator에 등록된 이벤트가 호출안됨
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 27.
	 */
	public void createMenus() {

		Menu menuSearch = findAndReplaceHelper.createMenus();

		menuMoveToLine = new MenuItem("Move to line");
		miToUppercase = new MenuItem("To Uppercase");
		miToLowercase = new MenuItem("To Lowercase");

		menuMoveToLine.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
		menuMoveToLine.setOnAction(this::moveToLineEvent);
		miToUppercase.setAccelerator(new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
		miToUppercase.setOnAction(this::toUppercaseEvent);
		miToLowercase.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
		miToLowercase.setOnAction(this::toLowercaseEvent);

		this.menu.get().getItems().addAll(menuSearch, menuMoveToLine, miToUppercase, miToLowercase);

		this.codeArea.setOnContextMenuRequested(ev -> {

			ContextMenu cm = getContextMenu();
			if (cm.isShowing())
				return;

			double x = ev.getScreenX();
			double y = ev.getScreenY();
			getContextMenu().show(codeArea, x, y);
		});

		this.codeArea.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			ContextMenu cm = getContextMenu();
			cm.hide();
		});

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 20.
	 * @param appendable
	 */
	public void customMenuHandler(CodeAreaCustomMenusHandler<T> appendable) {
		appendable.customMenus(this.codeArea, this.menu.get());
	}

	public void setContent(String content) {
		codeArea.getUndoManager().mark();
		codeArea.clear();
		codeArea.replaceText(0, 0, content);
		// codeArea.getUndoManager().mark();
	}

	public void setContent(int start, int end, String text) {
		codeArea.getUndoManager().mark();
		codeArea.replaceText(start, end, text);
		// codeArea.getUndoManager().mark();
	}

	public String getSqlFormat(String sql) {
		return sqlFormatter.format(sql);
	}

	public void replaceSelection(String selection) {
		codeArea.getUndoManager().mark();
		codeArea.replaceSelection(selection);
		// codeArea.getUndoManager().mark();
	}

	public void appendContent(String content) {
		if (content == null) {
			content = "";
		}

		codeArea.getUndoManager().mark();
		codeArea.appendText(content);
		// codeArea.getUndoManager().mark();
	}

	public String getSelectedText() {
		String selectedText = codeArea.getSelectedText();
		if (ValueUtil.isEmpty(selectedText)) {
			String fullText = codeArea.getText();
			int caretPosition = codeArea.getCaretPosition();

			// bugfix
			String split = sqlFormatter.split(fullText, caretPosition);
			return split;
		}
		return selectedText;
	}

	public String getText() {
		return codeArea.getText();
	}

	/**
	 * Sql 포멧처리.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 23.
	 */
	public void doSqlFormat() {
		String selectedText = codeArea.getSelectedText();
		if (ValueUtil.isNotEmpty(selectedText)) {
			// codeArea.replaceSelection(sqlFormatter.format(selectedText));
			replaceSelection(getSqlFormat(selectedText));
		} else {
			String format = getSqlFormat(codeArea.getText());
			// 2016.2.15 undo,redo처리를 위해 setContent로 변경
			// codeArea.clear();
			// codeArea.appendText(format);
			setContent(format);
		}
	}

	protected void toUppercaseEvent(KeyEvent e) {
		if (e.getCode() == KeyCode.U && (e.isControlDown() && !e.isAltDown() && !e.isShiftDown())) {
			if (e.isConsumed())
				return;
			toUppercaseEvent(e);
			e.consume();
		}
	}

	protected void toLowercaseEvent(KeyEvent e) {
		if (e.getCode() == KeyCode.L && (e.isControlDown() && !e.isAltDown() && !e.isShiftDown())) {

			if (e.isConsumed())
				return;
			toLowercaseEvent(e);
			e.consume();
		}
	}

	protected void toUppercaseEvent(Event e) {
		String selectedText = codeArea.getSelectedText();
		if (ValueUtil.isNotEmpty(selectedText)) {
			// codeArea.replaceSelection(sqlFormatter.toUpperCase(selectedText));
			replaceSelection(sqlFormatter.toUpperCase(selectedText));
		}
	}

	protected void toLowercaseEvent(Event e) {
		String selectedText = codeArea.getSelectedText();
		if (ValueUtil.isNotEmpty(selectedText)) {
			// codeArea.replaceSelection(sqlFormatter.toLowerCase(selectedText));
			replaceSelection(sqlFormatter.toLowerCase(selectedText));
		}
	}

	protected void moveToLineEvent(Event e) {
		Optional<Pair<String, String>> showInputDialog = DialogUtil.showInputDialog(this.codeArea, "Go to Line", "Input Line Number",
				str -> ValueUtil.isNumber(str));

		showInputDialog.ifPresent(v -> {
			String value = v.getValue();
			BigDecimal bigDecimal = new BigDecimal(value);
			int intValue = bigDecimal.intValue();
			moveToLine(intValue);
		});
		e.consume();
	}

	/**
	 * 키클릭 이벤트 처리
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 12. 14.
	 * @param e
	 */
	public void codeAreaKeyClick(KeyEvent e) {

		// CTRL + F
		if (KeyCode.F == e.getCode() && e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {
			if (!e.isConsumed()) {
				findAndReplaceHelper.findReplaceEvent(new ActionEvent());
				e.consume();
			}

		}
		// CTRL + L
		else if (KeyCode.L == e.getCode() && e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {
			if (e.isConsumed())
				return;
			e.consume();
			moveToLineEvent(new ActionEvent());
		}
		// CTRL + U
		else if (KeyCode.U == e.getCode() && e.isControlDown() && e.isShiftDown() && !e.isAltDown()) {
			if (!e.isConsumed()) {
				toUppercaseEvent(new ActionEvent());
				e.consume();
			}
		}
		// CTRL + SHIFT +  L
		else if (KeyCode.L == e.getCode() && e.isControlDown() && e.isShiftDown() && !e.isAltDown()) {
			if (e.isConsumed()) 
				return;
				e.consume();
				toLowercaseEvent(new ActionEvent());
			
		}
		// CTRL + SHIFT + V
		else if (KeyCode.V == e.getCode() && e.isControlDown() && e.isShiftDown() && !e.isAltDown()) {

			if (codeArea == null)
				return;

			if (codeArea instanceof GargoyleCodeArea) {
				// 18.4.24 RICHTEXT를 업뎃하면서 아래 코드 일시중지
				if (e.isConsumed())
					return;

				GargoyleCodeArea c = (GargoyleCodeArea) codeArea;
				IndexRange selection = codeArea.getSelection();
				CodeAreaClipboardItemListView view = new CodeAreaClipboardItemListView();
				view.onClose(str -> {
					codeArea.replaceText(selection, str);
				});
				view.getItems().addAll(c.getClipBoardItems());

				view.setPrefSize(800d, 600d);
				PopOver popOver = new PopOver(view);
				view.setPopOver(popOver);
				popOver.show(codeArea);

				e.consume();
			}

		}
		//////////////////////////////////////////////////////////////////////////////////////
		/* 선택된 행의 selection을 이동시키기 위한 처리 tab, shift + tab */
		// Tab
		else if (e.getCode() == KeyCode.TAB && (!e.isControlDown() && !e.isShiftDown())) {

			// 18.5.13 일시적으로 제거 - consumed 상태로 리턴됨.
			// if (e.isConsumed())
			// return;

			String selectedText = codeArea.getSelectedText();
			IndexRange selection = codeArea.getSelection();
			int start = selection.getStart();
			if (ValueUtil.isEmpty(selectedText))
				return;

			String tabbing = ValueUtil.tapping(selectedText);
			replaceSelection(tabbing);
			IndexRange selection2 = codeArea.getSelection();
			int end = selection2.getEnd();
			codeArea.selectRange(start, end);

			e.consume();
		}
		// Shift + Tab
		else if (e.getCode() == KeyCode.TAB && (!e.isControlDown() && e.isShiftDown())) {

			// if (e.isConsumed())
			// return;

			String selectedText = codeArea.getSelectedText();
			IndexRange selection = codeArea.getSelection();

			// 행만 선택하고 탭키를 누른경우
			if (selection.getStart() == selection.getEnd()) {
				codeArea.selectLine();
				selectedText = codeArea.getSelectedText();
				selection = codeArea.getSelection();
				String tabbing = ValueUtil.reverseTapping(selectedText);
				replaceSelection(tabbing);
				codeArea.selectRange(selection.getStart(), selection.getStart());
			} else {

				/*
				 * 2017-07-02
				 * 
				 * 더이상 탭이 진행될수 없는 상태의 텍스트인경우 불필요한 부분까지 selection되는 부분을 해결한다.
				 * 라인갯수를 추가적으로 알아야하기때문에 ValueUtil.tabpping 함수를 사용하지않는다.
				 */
				String tabbing = selectedText;
				/**/

				String[] split = tabbing.split("\n");
				Integer removedLength = 0;
				StringBuffer sb = new StringBuffer();
				if (split != null) {

					/*
					 * 17.01.04 로직수정
					 * 
					 * 언탭이후 선택된 라인행을 정확하게 선택하기 위한 로직구성. 정규식을 버리려하였으나 버릴순없음. -
					 * 케이스1 공백이 없어진 행라인을 찾은뒤 변화없는 코드 작성
					 */
					// Optional<String> reduce = Stream.of(split).map(str -> {
					// // 탭이거나 공백이 포함되는 문자 치환
					//
					// return str.replaceAll("^(\t|[ ]{1,3})", "");
					//
					// // return str;
					// }).reduce((str1, str2) ->
					// str1.concat("\n").concat(str2));
					// if (reduce.isPresent()) {
					// tabbing = reduce.get();
					// }

					for (int i = 0, max = split.length; i < max; i++) {
						String str = split[i];
						if (str.startsWith("\t")) {
							removedLength++;
						} else if (str.startsWith("   ")) {
							removedLength += 3;
						} else if (str.startsWith("  ")) {
							removedLength += 2;
						} else if (str.startsWith(" ")) {
							removedLength += 1;
						}
						sb.append(str.replaceAll("^(\t|[ ]{1,3})", ""));
						if (i != max - 1)
							sb.append("\n");
					}
					tabbing = sb.toString();
				}

				// 원본텍스트와 다른경우에만 변화를 준다.
				if (!selectedText.equals(tabbing)) {
					replaceSelection(tabbing);
					codeArea.selectRange(selection.getStart(), selection.getEnd() - removedLength);
				}

			}
			e.consume();
		}
		//////////////////////////////////////////////////////////////////////////////////////

		else {
			codeArea.getUndoManager().mark();
		}

	}

	/**
	 * 특정라인으로 이동처리하는 메소드
	 *
	 * 특정라인블록 전체를 선택처리함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 4.
	 * @param moveToLine
	 */
	public void moveToLine(int moveToLine) {
		codeMoveDeligator.moveToLine(moveToLine);
	}

	public void moveToLine(int moveToLine, int startCol) {
		codeMoveDeligator.moveToLine(moveToLine, startCol);
	}

	public void moveToLine(int moveToLine, int startCol, int endCol) {
		codeMoveDeligator.moveToLine(moveToLine, startCol, endCol);
	}

	public void moveToLineByIdx(int startIndex, int endIndex) {
		codeArea.selectRange(startIndex, endIndex);
		moveToLine(codeArea.getCurrentParagraph() + 1);
	}

	public Integer getCurrentLine() {
		return codeMoveDeligator.getCurrentLine();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 28.
	 * @return
	 */
	public IndexRange getSelection() {
		return codeArea.getSelection();
	}

	public final ObjectProperty<ContextMenu> menuProperty() {
		return this.menu;
	}

	public final ContextMenu getMenu() {
		return this.menuProperty().get();
	}

	public final void setMenu(final ContextMenu menu) {
		this.menuProperty().set(menu);
	}

	public void setContextMenu(ContextMenu menu) {
		this.setMenu(menu);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 13. 
	 * @return
	 */
	public ObservableValue<String> contentProperty() {
		return codeArea.textProperty();
	}

}
