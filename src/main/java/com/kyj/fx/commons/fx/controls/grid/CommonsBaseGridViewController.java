/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2015. 10. 9.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

/**
 * @author KYJ
 *
 */
public class CommonsBaseGridViewController extends CommonsBaseGridView {
	public CommonsBaseGridViewController() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("CommonsBaseGridView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		loader.load();
	}
}
