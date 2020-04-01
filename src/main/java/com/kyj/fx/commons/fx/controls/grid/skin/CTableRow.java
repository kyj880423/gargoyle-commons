/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.sun.javafx.scene.control.skin
 *	작성일   : 2017. 7. 5.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid.skin;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Skin;
import javafx.scene.control.TableRow;

/**
 * @author KYJ
 *
 */
public class CTableRow<T> extends TableRow<T> {

	/**
	* When the row is fixed, it may have a shift from its original position
	* which we need in order to layout the cells properly and also for the
	* rectangle selection.
	*/
	DoubleProperty verticalShift = new SimpleDoubleProperty();

	@Override
	protected Skin<?> createDefaultSkin() {
		return new CTableRowSkin<>(this);
	}

}
