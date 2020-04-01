/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2017. 1. 13.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import org.fxmisc.richtext.CodeArea;

import com.kyj.fx.commons.utils.ValueUtil;

import javafx.event.EventHandler;
import javafx.scene.control.IndexRange;
import javafx.scene.input.MouseEvent;

/**
 * @author KYJ
 *
 */
public class CodeAreaDefaultSelectionHandler implements EventHandler<MouseEvent> {

	public static final String CHARACTERS_MATCH = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";

	private CodeArea codeArea;
	public CodeAreaDefaultSelectionHandler(CodeArea codeArea){
		this.codeArea = codeArea;
	}
	
	/* (non-Javadoc)
	 * @see javafx.event.EventHandler#handle(javafx.event.Event)
	 */
	@Override
	public void handle(MouseEvent event) {
		if (event.getClickCount() == 1) {
			// codeArea.setStyleSpans(0,
			// groupBackgroundColor(codeArea.getText(),
			// codeArea.getCaretPosition()));
		} else if (event.getClickCount() == 2) {
			String selectedText = codeArea.getSelectedText();
			if (ValueUtil.isNotEmpty(selectedText)) {
				IndexRange selection = codeArea.getSelection();
				String ltrimText = selectedText.replaceAll("^\\s+", "");
				String firstStr = ltrimText.substring(0, 1).replaceAll(CHARACTERS_MATCH, "");
				int start = selection.getStart();
				int end = selection.getEnd();
				codeArea.selectRange(start + (selectedText.length() - ltrimText.length() + 1 - firstStr.length()), end);
			}
		}
	}

}
