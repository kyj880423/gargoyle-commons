/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.text
 *	작성일   : 2019. 1. 16.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import com.kyj.fx.commons.utils.FileUtil;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class TextAreaDragDropHelper extends AbstractDragDropHelper<TextArea> {

	public TextAreaDragDropHelper(TextArea node) {
		super(node);
	}

	@Override
	public void onDagOver(DragEvent ev) {

		if (ev.isConsumed())
			return;

		if (ev.getDragboard().hasString() || ev.getDragboard().hasFiles()) {
			ev.consume();
			ev.acceptTransferModes(TransferMode.COPY);
		}
	}

	@Override
	public void onDragDropped(DragEvent ev) {

		if (ev.isConsumed())
			return;
		ev.consume();

		Dragboard dragboard = ev.getDragboard();
		if (dragboard.hasFiles()) {
			List<File> files = dragboard.getFiles();
			if(files.isEmpty())
				return;
			File file = files.get(0);
			String encoding = FileUtil.getFindEncoding(file);
			Charset charset = Charset.forName(encoding);
			FileUtil.asynchRead(file, charset, str->{
				Platform.runLater(()->{
					setContent(str);
				});
			});
			
			
		}
		else if (dragboard.hasString()) {
			String content = dragboard.getString();
			setContent(content);
			ev.setDropCompleted(true);
		}
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 16.
	 * @param content
	 */
	public void setContent(String content) {
		node.setText(content);
	}
}
