/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2018. 3. 5.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.File;
import java.util.List;
import java.util.function.BiFunction;

import org.fxmisc.richtext.CodeArea;

import com.kyj.fx.commons.fx.controls.text.CodeAreaFileDragDropHelper;

import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * @author KYJ
 * @Deprecated implementation AbstractDragDropHelper
 */
@Deprecated
public class FxDragDropHelper {

	public FxDragDropHelper() {

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 5.
	 * @param target
	 * @param handle
	 */
	public <N extends javafx.scene.Node> void installFileLinkDragDrop(N target, BiFunction<N, Dragboard, Boolean> handle) {

		// target.addEventHandler(MouseEvent.DRAG_DETECTED, ev -> {
		// startFullDrag();
		// });

		EventHandler<? super DragEvent> dragOverHandle = event -> {
			// if (event.getGestureSource() != target) {

			if (event.isConsumed())
				return;

			event.consume();

			/* allow for both copying and moving, whatever user chooses */
			event.acceptTransferModes(TransferMode.LINK);
			// }
			// event.consume();
		};

		EventHandler<? super DragEvent> droppedHandle = event -> {
			if (event.isConsumed())
				return;

			event.consume();

			Dragboard db = event.getDragboard();
			boolean success = false;
			success = handle.apply(target, db);
			/*
			 * let the source know whether the string was successfully
			 * transferred and used
			 */
			event.setDropCompleted(success);

		};

		target.addEventHandler(DragEvent.DRAG_OVER, dragOverHandle);
		target.addEventHandler(DragEvent.DRAG_DROPPED, droppedHandle);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 5.
	 * @param target
	 */
	public void installFileLinkDragDrop(TextField target) {

		BiFunction<TextField, Dragboard, Boolean> textFieldClipboardHandle = (t, d) -> {

			if (d.hasFiles()) {
				List<File> files = d.getFiles();
				String path = files.get(0).getAbsolutePath();
				t.setText(path);
			}

			return true;
		};

		installFileLinkDragDrop(target, textFieldClipboardHandle);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 16.
	 * @param target
	 */
	public void installFileLinkDragDrop(TextArea target) {

		BiFunction<TextArea, Dragboard, Boolean> textFieldClipboardHandle = (t, d) -> {

			if (d.hasFiles()) {
				List<File> files = d.getFiles();
				String path = files.get(0).getAbsolutePath();
				t.setText(path);
			}

			return true;
		};

		installFileLinkDragDrop(target, textFieldClipboardHandle);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 16.
	 * @param target
	 */
	public void installFileLinkDragDrop(CodeArea target) {

		new CodeAreaFileDragDropHelper<>(target);
		//
		// BiFunction<CodeArea, Dragboard, Boolean> textFieldClipboardHandle =
		// (t, d) -> {
		//
		// if (d.hasFiles()) {
		// List<File> files = d.getFiles();
		// String path = files.get(0).getAbsolutePath();
		// t.replaceText(path);
		// }
		//
		// return true;
		// };
		//
		// installFileLinkDragDrop(target, textFieldClipboardHandle);
	}

}
