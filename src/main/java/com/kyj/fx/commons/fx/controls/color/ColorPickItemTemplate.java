/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.config.view
 *	작성일   : 2018. 3. 22.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.color;

import com.kyj.fx.commons.models.KeyColorPair;
import com.kyj.fx.commons.models.KeyPair;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * @author KYJ
 *
 */
public class ColorPickItemTemplate extends HBox {

	private KeyPair<String, Paint> keyPair = new KeyColorPair();
	private double spacing = 5d;
	private ColorPicker colorPicker;

	public ColorPickItemTemplate(String key) {
		keyPair.setKey(key);
		colorPicker = new ColorPicker();
		colorPicker.valueProperty().addListener((oba, o, n) -> {
			keyPair.setValue(n);
		});
		setSpacing(spacing);
		getChildren().addAll(new Label(key), colorPicker);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 22.
	 * @return
	 */
	public KeyPair<String, Paint> getValue() {
		return keyPair;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 22.
	 * @param value
	 */
	public void setValue(Paint value) {
		keyPair.setValue(value);
		colorPicker.setValue((Color) value);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 22.
	 * @return
	 */
	public ObjectProperty<Color> valueProperty() {
		return colorPicker.valueProperty();
	}
}
