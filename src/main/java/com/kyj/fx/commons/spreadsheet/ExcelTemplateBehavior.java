/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.spreadsheets
 *	작성일   : 2016. 10. 28.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.spreadsheet;

import java.util.ArrayList;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;

/**
 * @author KYJ
 *
 */
public class ExcelTemplateBehavior extends BehaviorBase<ExcelTemplateControl> {

	private static final ArrayList<KeyBinding> keys = new ArrayList<>();

	/**
	 * @param control
	 * @param keyBindings
	 */
	public ExcelTemplateBehavior(ExcelTemplateControl control) {
		super(control, keys);
	}

}
