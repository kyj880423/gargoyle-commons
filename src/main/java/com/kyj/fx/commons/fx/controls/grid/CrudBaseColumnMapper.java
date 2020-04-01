/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2015. 10. 9.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

/**
 * @author KYJ
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CrudBaseColumnMapper<T extends AbstractDVO> implements IColumnMapper<T> {
	private static Logger LOGGER = LoggerFactory.getLogger(CrudBaseColumnMapper.class);

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

		TableColumn tableColumn = null;

		// 2015.11.18 커스텀 컬럼값을 정의한경우 대체.
		TableColumn<T, ?> customTableColumn = options.customTableColumn(columnName);
		if (customTableColumn != null) {
			tableColumn = customTableColumn;
			PropertyValueFactory<T, ?> value = new PropertyValueFactory<>(columnName);
			tableColumn.setCellValueFactory(value);
		}
		// [시작] 베이스 컬럼
		else {
			tableColumn = new TableColumn<>();
			BooleanProperty editableProperty = tableColumn.editableProperty();

			CommboInfo<?> comboBox = options.comboBox(columnName);
			if (comboBox == null) {
				tableColumn.setCellFactory(column -> {
					if (classType == BooleanProperty.class && options.useCommonCheckBox()) {
						return checkBox((TableColumn<T, ?>) column);
					} else {
						TextFieldTableCell<T, Object> textField = textField(classType, columnName, options);

						// [시작]이게 필요한건지는 일단 지켜보도록함.
						textField.editableProperty().bind(editableProperty);
						textField.disableProperty().bind(editableProperty.not());
						// [끝]이게 필요한건지는 일단 지켜보도록함.

						return textField;
					}

				});
				PropertyValueFactory<T, ?> value = new PropertyValueFactory<>(columnName);
				tableColumn.setCellValueFactory(value);

			} else {
				tableColumn = commboBox(classType, columnName, options);
			}
			tableColumn.setResizable(true);
			// 무조건 true로 지정. 대신 cell에 편집가능 수준정보 조절함.
			tableColumn.setEditable(true);
		}
		// [끝] 베이스 컬럼

		// 헤더 텍스트
		if (CommonConst.COMMONS_FILEDS_COMMONS_CLICKED.equals(columnName)) {
			tableColumn.setText("");
		} else {
			String headerName = options.convert(columnName);
			tableColumn.setText(headerName);
		}

		tableColumn.setStyle(options.style(columnName));
		// 기타 속성지정
		setColumnProperty(tableColumn, columnName, options);
		tableColumn.setId(columnName);

		//보임속성 추가.
		tableColumn.setVisible(options.visible(columnName));
		
		return tableColumn;
	}

	private CommboBoxTableColumn<T, Object> commboBox(Class<?> classType, String columnName, IOptions naming) {

		CommboInfo<?> comboBox = naming.comboBox(columnName);
		ObservableList codeList = (ObservableList) comboBox.getCodeList();
		String code = comboBox.getCode();
		String codeNm = comboBox.getCodeNm();

		Supplier<ChoiceBoxTableCell<T, Object>> supplier = () -> {
			ChoiceBoxTableCell<T, Object> choiceBoxTableCell = new ChoiceBoxTableCell<T, Object>(codeList) {
				@Override
				public void startEdit() {
					Object vo = tableViewProperty().get().getItems().get(super.getIndex());
					if (vo instanceof AbstractDVO) {
						AbstractDVO _abstractvo = (AbstractDVO) vo;

						if (Objects.equals(CommonConst._STATUS_CREATE, _abstractvo.get_status())) {
							boolean editable = naming.editable(columnName);
							if (!editable)
								return;

							super.startEdit();
						} else if (Objects.equals(CommonConst._STATUS_UPDATE, _abstractvo.get_status())) {
							boolean editable = naming.editable(columnName);
							if (!editable)
								return;

							NonEditable annotationClass = getAnnotationClass(_abstractvo.getClass(), NonEditable.class,
									columnName);
							if (annotationClass != null) {
								LOGGER.debug("non start Edit");

							} else {
								super.startEdit();
								LOGGER.debug("start Edit");
							}
						}
					}
				}

			};

			EventDispatcher originalDispatcher = choiceBoxTableCell.getEventDispatcher();
			choiceBoxTableCell.setEventDispatcher((event, tail) -> {
				return choiceBoxTableCellCellEventDispatcher(choiceBoxTableCell, originalDispatcher, event, tail);
			});

			// 아직까지는 codeList가 쓰일일이 없어서 주석처리함.. 과연 필요한 케이스가 생길지...?
			choiceBoxTableCell.setConverter(new CommboBoxStringConverter<Object>(/* codeList, */ code, codeNm));
			return choiceBoxTableCell;
		};

		CommboBoxTableColumn<T, Object> combobox = new CommboBoxTableColumn<T, Object>(supplier, columnName, codeList,
				code, codeNm);

		return combobox;
	}

	private TextFieldTableCell<T, Object> textField(Class<?> classType, String columnName, IOptions naming) {
		TextFieldTableCell<T, Object> textFieldTableCell = new TextFieldTableCell<T, Object>() {

			/*
			 * (non-Javadoc)
			 *
			 * @see javafx.scene.control.cell.TextFieldTableCell#startEdit()
			 */
			@Override
			public void startEdit() {

				Object vo = tableViewProperty().get().getItems().get(getIndex());
				if (vo instanceof AbstractDVO) {

					AbstractDVO _abstractvo = (AbstractDVO) vo;
					if (Objects.equals(CommonConst._STATUS_CREATE, _abstractvo.get_status())) {

						// 아래와 같은형태가 되어선안됨.
						// if (annotationClass != null &&
						// naming.editable(columnName)) {
						// 새로 추가된 항목은 NotEditable항목 편집가능하게 함.

						boolean editable = naming.editable(columnName);
						if (!editable)
							return;

						super.startEdit();
						LOGGER.debug("start Edit");
					} else if (Objects.equals(CommonConst._STATUS_UPDATE, _abstractvo.get_status())) {

						boolean editable = naming.editable(columnName);
						if (!editable)
							return;

						NonEditable annotationClass = getAnnotationClass(_abstractvo.getClass(), NonEditable.class,
								columnName);
						if (annotationClass != null) {
							LOGGER.debug("non start Edit");

						} else {
							super.startEdit();
							LOGGER.debug("start Edit");
						}

					}
				} else {
					LOGGER.debug("start Edit");
					super.startEdit();
				}

			}

		};

		StringConverter<Object> converter = naming.stringConverter(columnName);
		if (converter == null) {
			textFieldTableCell.setConverter(converter(textFieldTableCell, classType));
		} else {
			textFieldTableCell.setConverter(converter);
		}

		EventDispatcher originalDispatcher = textFieldTableCell.getEventDispatcher();
		textFieldTableCell.setEventDispatcher((event, tail) -> {
			return textFieldCellEventDispatcher(textFieldTableCell, originalDispatcher, event, tail);
		});
		return textFieldTableCell;
	}

	/**
	 * 어노테이션 클래스를 반환, 없으면 null 리턴
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 21.
	 * @param annotationClass
	 * @param fieldName
	 * @return
	 */
	private static <T extends Annotation> T getAnnotationClass(Class<?> targetClass, Class<T> annotationClass,
			String fieldName) {
		try {
			Field declaredField = targetClass.getDeclaredField(fieldName);
			declaredField.setAccessible(true);
			return declaredField.getDeclaredAnnotation(annotationClass);
		} catch (Exception e) {
		}
		return null;
	}

	private CheckBoxTableCell<T, Object> checkBox(TableColumn<T, ?> column) {
		CheckBox columnheaderCheckbox = new CheckBox();
		// columnheaderCheckbox.setStyle("-fx-background-color:white;
		// -fx-border-color :black; -fx-border-width:1;");
		column.setGraphic(columnheaderCheckbox);
		column.setSortable(false);
		columnheaderCheckbox.addEventHandler(ActionEvent.ACTION, event -> {
			if (columnheaderCheckbox.isSelected()) {
				setSelectAll(column, true);
			} else {
				setSelectAll(column, false);
			}
		});

		return new CheckBoxTableCell<T, Object>() {
			@Override
			public void updateItem(Object item, boolean empty) {
				super.updateItem(item, empty);
			}

			@Override
			public void startEdit() {
				//TODO 추가 작업 예상됨. 현재는 기본컬럼으로 CheckBox형태가 나올것같지않음.
				super.startEdit();
			}

		};
	}

	/**
	 * 공통 체크 헤더행을 flag값에 따라 선택 혹은 해제한다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 14.
	 * @param column
	 * @param flag
	 */
	private void setSelectAll(TableColumn<T, ?> column, boolean flag) {
		column.getTableView().getItems().stream().forEach(vo -> {
			if (vo instanceof AbstractDVO) {
				vo.setClicked(flag);
			}
		});
	}

	private Event textFieldCellEventDispatcher(TextFieldTableCell<T, Object> cell, EventDispatcher originalDispatcher,
			Event event, EventDispatchChain tail) {

		// 편집후 마우스 엔터인경우 이벤트 else
		if (event instanceof ActionEvent) {

			tail.append(new EventDispatcher() {
				@Override
				public Event dispatchEvent(Event event, EventDispatchChain tail) {
					int index = cell.getIndex();
					Object item = cell.getTableRow().getItem();
					TableColumn<T, Object> _tableColumn = cell.getTableColumn();
					TableRow _tableRow = cell.getTableRow();

					TableView<T> tableView = cell.getTableView();

					GridBaseTableCellValueChangeEvent dispatchEvent = new GridBaseTableCellValueChangeEvent();
					dispatchEvent.setItem(item);
					dispatchEvent.setRowIndex(index);
					dispatchEvent.setTableRow(_tableRow);
					dispatchEvent.setTableColumn(_tableColumn);
					Event.fireEvent(tableView, dispatchEvent);

					return event;
				}
			});
		}
		return originalDispatcher.dispatchEvent(event, tail);
	}

	private Event choiceBoxTableCellCellEventDispatcher(ChoiceBoxTableCell<T, Object> cell,
			EventDispatcher originalDispatcher, Event event, EventDispatchChain tail) {

		// 편집후 마우스 엔터인경우 이벤트 else
		if (event instanceof ActionEvent) {

			tail.append(new EventDispatcher() {
				@Override
				public Event dispatchEvent(Event event, EventDispatchChain tail) {
					int index = cell.getIndex();
					Object item = cell.getTableRow().getItem();
					TableColumn<T, Object> _tableColumn = cell.getTableColumn();
					TableRow _tableRow = cell.getTableRow();

					TableView<T> tableView = cell.getTableView();

					GridBaseTableCellValueChangeEvent dispatchEvent = new GridBaseTableCellValueChangeEvent();
					dispatchEvent.setItem(item);
					dispatchEvent.setRowIndex(index);
					dispatchEvent.setTableRow(_tableRow);
					dispatchEvent.setTableColumn(_tableColumn);
					Event.fireEvent(tableView, dispatchEvent);

					return event;
				}
			});
		}
		return originalDispatcher.dispatchEvent(event, tail);

	}

	private void setColumnProperty(TableColumn tableColumn, String columnName, IOptions naming) {

		// 사이즈
		int columnSize = naming.columnSize(columnName);
		if (columnSize > 0) {
			tableColumn.setPrefWidth(columnSize);
		}
	}

	/**
	 * 타입 컨버터
	 *
	 * @param textFieldTableCell
	 *
	 * @Date 2015. 10. 8.
	 * @param classType
	 * @return
	 * @User KYJ
	 */
	private StringConverter<Object> converter(TextFieldTableCell<T, Object> textFieldTableCell, Class<?> classType) {

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
