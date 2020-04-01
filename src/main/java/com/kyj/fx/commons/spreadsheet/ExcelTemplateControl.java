/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.spreadsheets
 *	작성일   : 2016. 10. 28.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.spreadsheet;

import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.Tab;

/**
 * @author KYJ
 *
 */
public class ExcelTemplateControl extends Control {

	/* (non-Javadoc)
	 * @see javafx.scene.control.Control#createDefaultSkin()
	 */
	@Override
	protected Skin<?> createDefaultSkin() {
		return new ExcelTemplateSkin(this);
	}

	public ObservableList<Tab> getTabs() {
		return getExcelTemplateSkin().getTabs();
	}

	public ExcelTemplateSkin getExcelTemplateSkin() {
		return (ExcelTemplateSkin) this.getSkin();
	}
}
