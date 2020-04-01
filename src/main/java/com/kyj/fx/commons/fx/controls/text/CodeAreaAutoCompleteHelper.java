/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.text
 *	작성일   : 2019. 3. 13.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.util.List;
import java.util.function.Supplier;

import org.fxmisc.richtext.CodeArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.models.BehaviorReferenceVO;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Popup;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class CodeAreaAutoCompleteHelper<T extends CodeArea> {

	private static Logger LOGGER = LoggerFactory.getLogger(CodeAreaAutoCompleteHelper.class);

	protected T codeArea;

	private Popup auoCompletePopup;
	private AutoComplete autoComplate;
	private List<BehaviorReferenceVO> references;
	private Supplier<AutoComplete> createAutoCompleteSupl = () -> {
		return new AutoComplete();
	};

	public CodeAreaAutoCompleteHelper(T codeArea) {
		this.codeArea = codeArea;
	}

	public void setReference(List<BehaviorReferenceVO> references) {
		this.references = references;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 13.
	 */
	public void init() {
		/* [start add keyword] */
		autoComplate = createAutoCompleteSupl.get();
		/* [end add keyword] */
		codeArea.addEventHandler(KeyEvent.KEY_RELEASED, this::codeAreaCloseAutoPopupOnKeyRelease);
		codeArea.addEventHandler(KeyEvent.KEY_TYPED, this::codeAreaOnKeyType);
		codeArea.caretPositionProperty().addListener(caretPositionChange);
	}

	/**
	 * @return the createAutoCompleteSupl
	 */
	public Supplier<AutoComplete> getCreateAutoCompleteSupl() {
		return createAutoCompleteSupl;
	}

	/**
	 * @param createAutoCompleteSupl
	 *            the createAutoCompleteSupl to set
	 */
	public void setCreateAutoCompleteSupl(Supplier<AutoComplete> createAutoCompleteSupl) {
		this.createAutoCompleteSupl = createAutoCompleteSupl;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 13.
	 * @param e
	 */
	public void codeAreaCloseAutoPopupOnKeyRelease(KeyEvent e) {

		KeyCode code = e.getCode();
		if (code.isLetterKey())
			return;

		if (KeyCode.ESCAPE == e.getCode() && auoCompletePopup != null) {
			if (e.isConsumed())
				return;
			e.consume();
			auoCompletePopup.hide();
		} else if (KeyCode.ENTER == e.getCode() && auoCompletePopup != null) {
			if (e.isConsumed())
				return;
			e.consume();
			auoCompletePopup.hide();
		} else if (KeyCode.SPACE == e.getCode() && auoCompletePopup != null) {
			if (e.isConsumed())
				return;
			e.consume();
			auoCompletePopup.hide();
		} else if (KeyCode.SEMICOLON == e.getCode()) {
			if (e.isConsumed())
				return;
			e.consume();
			auoCompletePopup.hide();
		}
		// else if (auoCompletePopup != null)
		// auoCompletePopup.hide();
	}

	private final ChangeListener<Integer> caretPositionChange = (observable, oldPosition, newPosition) -> {
		if (auoCompletePopup != null)
			auoCompletePopup.hide();
	};

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 13.
	 * @param e
	 */
	public void codeAreaOnKeyType(KeyEvent event) {
		if (!event.isConsumed())
			return;
		event.consume();

		int position = codeArea.getCaretPosition();
		String query = autoComplate.getQuery(codeArea, position);

		if (auoCompletePopup == null) {
			auoCompletePopup = new Popup();
		} else {
			auoCompletePopup.hide();
		}

		if (!query.trim().isEmpty()) {
			ListView<String> suggestionsList = autoComplate.getSuggestionsList(query);
			if (suggestionsList.getItems().size() != 0) {
				auoCompletePopup.getContent().clear();
				auoCompletePopup.getContent().add(suggestionsList);
				Bounds pointer = codeArea.caretBoundsProperty().getValue().get();
				auoCompletePopup.show(codeArea, pointer.getMaxX(), pointer.getMinY());
				suggestionsList.setOnKeyReleased(ev -> {
					if (ev.getCode() == KeyCode.ESCAPE) {
						if (auoCompletePopup == null) {
							auoCompletePopup = new Popup();
						}
					} else if (ev.getCode() == KeyCode.ENTER) {
						extracted(suggestionsList);
					}

				});
				suggestionsList.setOnMouseClicked(clickEvent -> {
					extracted(suggestionsList);
				});
			}
		} else {
			auoCompletePopup.hide();
		}

	}

	private void extracted(ListView<String> suggestionsList) {
		String word = suggestionsList.getSelectionModel().getSelectedItem();
		if (word == null)
			return;

		String string = codeArea.getSelectedText();

		Platform.runLater(() -> {
			int start = codeArea.getSelection().getStart();
			LOGGER.debug("string value : {}, selection start {} ", string, start);

			int findWhiteSpaceStartInddex = getIndexOfValideWhiteSpace(start);
			if (findWhiteSpaceStartInddex == -1) {
				codeArea.getUndoManager().mark();
				codeArea.appendText(word);
			} else {

				// bug fix.
				int anchor = codeArea.getAnchor();
				int end = anchor + string.length();
				codeArea.replaceText(findWhiteSpaceStartInddex, end, word);
			}

			// codeArea.replaceText(position - query.length(),
			// position, word);
			// // autoComplate.getKeywords().get(word);
			// String suggestion =
			// suggestionsList.getSelectionModel().getSelectedItem();
			// if (suggestion != null) {
			// int wordPosition = position + word.length() -
			// query.length();
			// // insert(codeArea, wordPosition, suggestion);
			// } else {
			// codeArea.moveTo(position + word.length() -
			// query.length());
			// }
		});
		auoCompletePopup.hide();
	}

	protected int getIndexOfValideWhiteSpace(String string) {
		for (int i = string.length() - 1; i >= 0; i--) {
			if (Character.isWhitespace(string.charAt(i))) {
				return i + 1;
			}
		}
		return 0;
	}

	protected int getIndexOfValideWhiteSpace(int currentIdx) {

		for (int i = currentIdx - 1; i >= 0; i--) {

			String text = codeArea.getText(i, i + 1);
			if (" ".equals(text)) {
				return i + 1;
			} else if ("\n".equals(text)) {
				return i + 1;
			} else if ("\t".equals(text)) {
				return i + 1;
			}
		}
		return 0;
	}

	// private static void insert(CodeArea codeArea, int position, String
	// packagePath) {
	// String formattedPackage = String.format(IMPORT_PACKAGE_FORMAT,
	// packagePath);
	// if (!codeArea.getText().contains(packagePath)) {
	// int impIndex = codeArea.getText().indexOf("import");
	// codeArea.insertText((impIndex == -1) ? 0 : impIndex, packagePath);
	// int cursorPosition = packagePath.length() + position;
	// codeArea.moveTo(cursorPosition);
	// }
	// }
}
