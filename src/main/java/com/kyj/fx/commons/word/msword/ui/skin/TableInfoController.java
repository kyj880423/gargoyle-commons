/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.ui.skin
 *	작성일   : 2016. 7. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.ui.skin;

import com.kyj.fx.fxloader.FXMLController;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

/***************************
 * 
 * @author KYJ
 *
 ***************************/
@FXMLController("TableInfoView.fxml")
public class TableInfoController {

	@FXML
	private BorderPane borTableDefine;

	@FXML
	private BorderPane borTableCollect;

	public void setBorTableDefine(Node center) {
		borTableDefine.setCenter(center);
	}

	public void setBorTableCollect(Node center) {
		borTableCollect.setCenter(center);
	}

	@FXML
	public void btnDownOnAction() {

	}

	@FXML
	public void btnUpOnAction() {

	}
}
