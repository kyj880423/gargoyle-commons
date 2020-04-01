/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.prop
 *	작성일   : 2018. 10. 8.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.prop;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ObjectUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.fxloader.FXMLController;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
@FXMLController(value = "PropertyView.fxml", isSelfController = true)
public class PropertyComposite<T> extends BorderPane {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyComposite.class);
	@FXML
	private GridPane gpProperty;

	private ObjectProperty<T> item = new SimpleObjectProperty<>();
	private Class<T> type;

	public PropertyComposite(Class<T> type) {
		this.type = type;
		FxUtil.loadRoot(PropertyComposite.class, this, err -> {
			LOGGER.error(ValueUtil.toString(err));
		});

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 8.
	 */
	@FXML
	public void initialize() {
		recreateGridPane();
		this.item.addListener((oba, o, n) -> {
			recreateGridPane(n);
		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 8.
	 */
	void recreateGridPane() {
		recreateGridPane(null);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 8.
	 */
	void recreateGridPane(T object) {
		clearItems();
		updateItems(object);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 8.
	 */
	void clearItems() {
		ObservableList<RowConstraints> rowConstraints = this.gpProperty.getRowConstraints();
		int size = rowConstraints.size();
		rowConstraints.removeAll(rowConstraints.subList(1, size));
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 8.
	 * @param t
	 */
	void updateItems(T t) {

		Map<String, Object> keys = ObjectUtil.getKeys(() -> {
			return new TreeMap<>();
		}, type, a -> true);

		keys.keySet().forEach(key -> {

			ObservableList<RowConstraints> rowConstraints = this.gpProperty.getRowConstraints();
			int size = rowConstraints.size();
			rowConstraints.add(new RowConstraints(50, 50, 50));

			Label createDefaultLabel = createDefaultLabel(ValueUtil.capitalize(key), null);
			
			

			this.gpProperty.add(createDefaultLabel, 0, size);

			if (t != null) {
				Object declaredFieldValue = ObjectUtil.getDeclaredFieldValue(t, key);
				String v = ValueUtil.decode(declaredFieldValue, a -> a.toString(), () -> "");
				this.gpProperty.add(createDefaultTextField(v), 1, size);
			}
		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 8.
	 * @param text
	 * @param graphics
	 * @return
	 */
	Label createDefaultLabel(String text, Node graphics) {
		Label label = new Label(text, graphics);
		label.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		label.setPadding(new Insets(0, 0, 0, 10));
		label.setBackground(FxUtil.getBackgroundColor(Color.web("DDDDDD"), 1));
		FxUtil.installTooltip(label);
		return label;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 8. 
	 * @param text
	 * @return
	 */
	TextField createDefaultTextField(String text) {
		TextField label = new TextField(text);
		label.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		label.setPadding(new Insets(0, 0, 0, 10));
		label.setEditable(false);
		return label;
	}

	public final ObjectProperty<T> itemProperty() {
		return this.item;
	}

	public final T getItem() {
		return this.itemProperty().get();
	}

	public final void setItem(final T item) {
		this.itemProperty().set(item);
	}

}
