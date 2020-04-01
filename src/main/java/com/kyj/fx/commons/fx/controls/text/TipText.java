/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.text
 *	작성일   : 2019. 3. 16.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import org.fxmisc.richtext.CodeArea;

import com.kyj.fx.commons.utils.FxUtil;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class TipText {

	private String tipText;

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 16.
	 * @param codeArea
	 * @param position
	 * @return
	 */
	public String getText(CodeArea codeArea, int position) {
		return this.tipText;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 16.
	 * @param text
	 * @return
	 */
	public void tipTextViewer(BorderPane container, String text) {

		// VBox container = new VBox();

		TextArea textArea = new TextArea(text);
		textArea.setEditable(false);

//		Label lblTitle = new Label("Hello World! - Title");
//		HBox titleContainer = new HBox(lblTitle);
//		HBox.getHgrow(titleContainer);
//		container.setTop(titleContainer);
		container.setCenter(textArea);

		Platform.runLater(() -> {

//			titleContainer.setStyle("-fx-border-color : black; ");
			textArea.setStyle("-fx-padding : 0.0px, 0.0px, 5.0px, 0.0px");
			container.setBackground(FxUtil.getBackgroundColor(Color.web("FFFFE1")));
			Region region = (Region) textArea.lookup(".content");
			region.setBackground(FxUtil.getBackgroundColor(Color.web("FFFFE1")));

		});

	}

	/**
	 * @return the tipText
	 */
	public String getTipText() {
		return tipText;
	}

	/**
	 * @param tipText
	 *            the tipText to set
	 */
	public void setTipText(String tipText) {
		this.tipText = tipText;
	}

}
