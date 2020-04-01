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

import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

/**
 * @author KYJ
 *
 */
public class CodeAreaStringDragDropHelper<T extends CodeArea> extends AbstractDragDropHelper<T> {

	private static Logger LOGGER = LoggerFactory.getLogger(CodeAreaStringDragDropHelper.class);

	public CodeAreaStringDragDropHelper(T codeArea) {
		super(codeArea);
	}

	/*********************************************************/
	// 파일 드래그 드롭 처리.

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.voeditor.visual.compondbo.LANGUAGESent.text.
	 * AbstractDragDropHelper# onDagOver(javafx.scene.input.DragEvent)
	 */
	@Override
	public void onDagOver(DragEvent ev) {

		if (ev.getDragboard().hasString()) {

			if (ev.isConsumed())
				return;

			ev.consume();
			ev.acceptTransferModes(TransferMode.COPY);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.voeditor.visual.component.text.AbstractDragDropHelper#
	 * onDragDropped(javafx.scene.input.DragEvent)
	 */
	@Override
	public void onDragDropped(DragEvent ev) {

		if (ev.getDragboard().hasString()) {

			if (ev.isConsumed())
				return;
			ev.consume();

			String content = ev.getDragboard().getString();
			setContent(content);
			ev.setDropCompleted(true);

		}

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 14.
	 * @param content
	 */
	public void setContent(String content) {
		node.getUndoManager().mark();
		node.clear();
		node.replaceText(0, 0, content);
		node.getUndoManager().mark();
	}

	/*********************************************************/

}
