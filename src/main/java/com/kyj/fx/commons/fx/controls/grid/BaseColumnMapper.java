/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2015. 10. 9.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

/**
 * @author KYJ
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BaseColumnMapper<T> implements IColumnMapper<T> {

	/**
	 * 컬럼 제너레이터
	 *
	 * @Date 2015. 10. 8.
	 * @param classType
	 * @return
	 * @User KYJ
	 */
	@Override
	public TableColumn<T, ?> generateTableColumns(Class<?> classType, String columnName, IOptions options) {

		TableColumn tableColumn = new TableColumn<>();

		// 2015.11.18 커스텀 컬럼은 cellFactory를 가져다 쓰지않음.

		tableColumn.setCellFactory(cell -> {
			TextFieldTableCell<T, Object> textFieldTableCell = new TextFieldTableCell<>();

			StringConverter<Object> stringConverter = options.stringConverter(columnName);
			if (stringConverter != null) {
				textFieldTableCell.setConverter(stringConverter);
			} else {
				textFieldTableCell.setConverter(converter(classType));
			}

			return textFieldTableCell;
		});

		// 2015.11.18 커스텀 컬럼값을 정의한경우 대체.
		TableColumn<T, ?> customTableColumn = options.customTableColumn(columnName);
		if (customTableColumn != null) {
			tableColumn = customTableColumn;
		}
		tableColumn.setStyle(options.style(columnName));

		// 2015.11.18 커스텀 컬럼은 ValueFactory는 기본적으로 사용
		PropertyValueFactory<T, ?> value = new PropertyValueFactory<>(columnName);
		tableColumn.setCellValueFactory(value);

		tableColumn.setId(columnName);

		// 컬럼의 속성을 정의
		setColumnProperty(tableColumn, columnName, options);
		tableColumn.setResizable(true);
		tableColumn.setEditable(false);

		
		
		tableColumn.setVisible(options.visible(columnName));

		tableColumn.setText(options.convert(columnName));

		return tableColumn;
	}

	private void setColumnProperty(TableColumn tableColumn, String columnName, IOptions naming) {

		// 사이즈
		int columnSize = naming.columnSize(columnName);
		if (columnSize > 0)
			tableColumn.setMinWidth(columnSize);

	}

	/**
	 * 타입 컨버터
	 *
	 * @Date 2015. 10. 8.
	 * @param classType
	 * @return
	 * @User KYJ
	 */
	private StringConverter<Object> converter(Class<?> classType) {
		return new StringConverter<Object>() {

			@Override
			public String toString(Object object) {
				if (object == null)
					return "";

				return object.toString();
			}

			@Override
			public Object fromString(String string) {

				if (Property.class.isAssignableFrom(classType)) {
					if (classType == BooleanProperty.class) {
						return Boolean.valueOf(string);
					}
				} else if (classType == boolean.class || classType == Boolean.class) {
					return Boolean.valueOf(string);
				}
				return string;
			}
		};
	}

}
