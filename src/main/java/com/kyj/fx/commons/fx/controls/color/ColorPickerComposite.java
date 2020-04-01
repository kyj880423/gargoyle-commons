/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.config.view
 *	작성일   : 2018. 3. 22.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.color;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.kyj.fx.commons.models.KeyPair;

import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * @author KYJ
 *
 */
public class ColorPickerComposite extends VBox {

	private Map<String, ColorPickItemTemplate> valueMap = new HashMap<>();

	/**
	 * 
	 * @param prop
	 *            setup value
	 */
	public ColorPickerComposite(String... keys) {
		this(Arrays.asList(keys));
	}

	/**
	 * @param keys
	 */
	public ColorPickerComposite(List<String> keys) {
		for (String key : keys) {
			valueMap.put(key, new ColorPickItemTemplate(key));
		}
		getChildren().addAll(valueMap.values());

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 22.
	 * @param key
	 * @return
	 */
	public Optional<Paint> getValue(String key) {
		ColorPickItemTemplate colorPickItemTemplate = valueMap.get(key);
		if (colorPickItemTemplate != null) {
			KeyPair<String, Paint> value = colorPickItemTemplate.getValue();
			if (value != null) {
				Paint v = value.getValue();
				if (v != null) {
					return Optional.of(v);
				}
			}
		}
		return Optional.empty();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 22.
	 * @param key
	 * @param value
	 */
	public void setValue(String key, Paint value) {
		ColorPickItemTemplate colorPickItemTemplate = valueMap.get(key);
		if (colorPickItemTemplate != null && value != null) {
			colorPickItemTemplate.setValue(value);
		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 22. 
	 * @param key
	 * @return
	 */
	public ObjectProperty<Color> valueProperty(String key) {
		ColorPickItemTemplate colorPickItemTemplate = valueMap.get(key);
		if (colorPickItemTemplate != null) {
			return colorPickItemTemplate.valueProperty();
		}
		return null;
	}
}
