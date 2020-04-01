/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2016. 12. 14.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.fxmisc.richtext.CodeArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.DialogUtil;
import com.kyj.fx.commons.utils.FileUtil;
import com.kyj.fx.commons.utils.ValueUtil;

import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

/**
 * @author KYJ
 *
 */
public class CodeAreaFileDragDropHelper<T extends CodeArea> extends AbstractFileDragDropHelper<T> {

	private static Logger LOGGER = LoggerFactory.getLogger(CodeAreaFileDragDropHelper.class);

	public CodeAreaFileDragDropHelper(T codeArea) {
		super(codeArea);
	}

	/*********************************************************/
	// 파일 드래그 드롭 처리.

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.voeditor.visual.component.text.AbstractDragDropHelper#
	 * onDagOver(javafx.scene.input.DragEvent)
	 */
	@Override
	public void onDagOver(DragEvent ev) {

		if (ev.getDragboard().hasFiles()) {
			if (ev.isConsumed())
				return;

			ev.consume();
			ev.acceptTransferModes(TransferMode.LINK);
			
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

		if (ev.getDragboard().hasFiles()) {

			if (ev.isConsumed())
				return;
			ev.consume();

			List<File> files = ev.getDragboard().getFiles();

			// tbDatabase.getItems().add(e)
			files.stream().findFirst().ifPresent(f -> {

				if (f.length() > dragDropLimitSize()) {

					DialogUtil.showMessageDialog("파일 용량이 너무 큽니다.");
					return;

				}
				
				
				setContent(FileUtil.readConversion(f));

			});
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
		node.replaceText(content);
		node.getUndoManager().mark();
	}

	private long limitSize = 60 * 1024 * 1024;
	/**
	 * 드래그 드롭시 파일 제한 사이즈 정의
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 10.
	 * @return
	 */
	protected long dragDropLimitSize() {
		return limitSize;
	}

	/*********************************************************/

}
