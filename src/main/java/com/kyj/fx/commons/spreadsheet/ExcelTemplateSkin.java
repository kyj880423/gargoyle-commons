/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.spreadsheets
 *	작성일   : 2016. 10. 28.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.spreadsheet;

import com.sun.javafx.scene.control.skin.BehaviorSkinBase;

import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * @author KYJ
 *
 */
public class ExcelTemplateSkin extends BehaviorSkinBase<ExcelTemplateControl, ExcelTemplateBehavior> {

	private ExcelTemplateControl control;
	private TabPane root;

	/**
	 * @param excelTemplateControl
	 */
	public ExcelTemplateSkin(ExcelTemplateControl control) {
		super(control, new ExcelTemplateBehavior(control));
		this.control = control;

		root = new TabPane();
		root.setSide(Side.BOTTOM);
		getChildren().add(root);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 28.
	 * @return
	 */
	public ObservableList<Tab> getTabs() {
		return this.root.getTabs();
	}

}
