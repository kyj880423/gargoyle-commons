/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.text
 *	작성일   : 2019. 3. 13.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.util.function.Supplier;

import org.fxmisc.richtext.CodeArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.ValueUtil;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Popup;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class CodeAreaTipHelper<T extends CodeArea> {

	private static Logger LOGGER = LoggerFactory.getLogger(CodeAreaTipHelper.class);

	protected T codeArea;

	private Popup auoCompletePopup;

	private TipText tipText;
	private Supplier<TipText> tipTextImpl = () -> {
		return new TipText();
	};

	public CodeAreaTipHelper(T codeArea) {
		this.codeArea = codeArea;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 13.
	 */
	public void init() {
		tipText = tipTextImpl.get();

		codeArea.addEventHandler(MouseEvent.MOUSE_CLICKED, this::codeAreaCloseAutoPopupOnMouseClick);
		codeArea.addEventHandler(KeyEvent.KEY_RELEASED, this::codeAreaCloseAutoPopupOnKeyRelease);
		// codeArea.addEventHandler(KeyEvent.KEY_TYPED,
		// this::codeAreaOnKeyType);
		codeArea.caretPositionProperty().addListener(caretPositionChange);
	}

	/**
	 * @return the createAutoCompleteSupl
	 */
	public Supplier<TipText> getTipTextSupl() {
		return tipTextImpl;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 16.
	 * @param tipText
	 */
	public void setCreateAutoCompleteSupl(Supplier<TipText> tipTextImpl) {
		this.tipTextImpl = tipTextImpl;
	}

	public void codeAreaCloseAutoPopupOnMouseClick(MouseEvent e) {
		if (auoCompletePopup != null)
			auoCompletePopup.hide();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 13.
	 * @param e
	 */
	public void codeAreaCloseAutoPopupOnKeyRelease(KeyEvent event) {

		// if (auoCompletePopup != null)
		// auoCompletePopup.hide();
		// KeyCode code = event.getCode();
		// if (code.isLetterKey())
		// return;
		//
		// if (KeyCode.ESCAPE == event.getCode() && auoCompletePopup != null) {
		// if (event.isConsumed())
		// return;
		// event.consume();
		//
		// } else if (KeyCode.ENTER == event.getCode() && auoCompletePopup !=
		// null) {
		// if (event.isConsumed())
		// return;
		// event.consume();
		// auoCompletePopup.hide();
		// } else if (KeyCode.SPACE == event.getCode() && auoCompletePopup !=
		// null) {
		// if (event.isConsumed())
		// return;
		// event.consume();
		// auoCompletePopup.hide();
		// } else if (KeyCode.SEMICOLON == event.getCode()) {
		// if (event.isConsumed())
		// return;
		// event.consume();
		// auoCompletePopup.hide();
		// }

		if (KeyCode.F2 == event.getCode()) {
			int position = codeArea.getCaretPosition();
			String query = tipText.getText(codeArea, position);

			if (auoCompletePopup == null) {
				auoCompletePopup = new Popup();
			} else {
				auoCompletePopup.hide();
			}

			if (ValueUtil.isNotEmpty(query)) {
				auoCompletePopup.getContent().clear();

				BorderPane container = new BorderPane();
				auoCompletePopup.getContent().add(container);

				tipText.tipTextViewer(container, query);

				Bounds pointer = codeArea.caretBoundsProperty().getValue().get();
				auoCompletePopup.show(codeArea, pointer.getMaxX(), pointer.getMinY());
				container.setOnKeyReleased(this::codeAreaOnKeyType);
			}

		}

	}

	private final ChangeListener<Integer> caretPositionChange = (observable, oldPosition, newPosition) -> {
		if (auoCompletePopup != null)
			auoCompletePopup.hide();
	};

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 16.
	 * @param event
	 */
	public void codeAreaOnKeyType(KeyEvent event) {

		if (auoCompletePopup != null) {

			// Control + C이면 팝업 닫지않음.
			if (KeyCode.ESCAPE == event.getCode())
				auoCompletePopup.hide();
		}

	}

}
