/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2015. 10. 9.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.ValueUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * 모든 데이터를 문자열화
 *
 * @author KYJ
 *
 */
public class CommonsBaseGridView<T extends AbstractDVO> extends TableView<T> {
	private static Logger LOGGER = LoggerFactory.getLogger(CommonsBaseGridView.class);
	public static final String COMMONS_CLICKED = CommonConst.COMMONS_FILEDS_COMMONS_CLICKED;
	private Class<T> clazz;

	private IColumnMapper<T> columnMapper;
	private IOptions options;

	public CommonsBaseGridView() {
		this.columnMapper = new BaseColumnMapper<>();
	}

	public CommonsBaseGridView(Class<T> clazz) {
		this(clazz, new ArrayList<T>());
	}

	public CommonsBaseGridView(Class<T> clazz, IOptions options) {
		this(clazz, FXCollections.emptyObservableList(), options);
	}

	public CommonsBaseGridView(Class<T> clazz, List<T> items) {
		this(clazz, items, createColumns(clazz), new BaseOptions());
	}

	public CommonsBaseGridView(Class<T> clazz, List<T> items, IOptions options) {
		this(clazz, items, createColumns(clazz), options);
	}

	public CommonsBaseGridView(Class<T> clazz, List<T> items, List<String> columns, IOptions options) {
		this.clazz = clazz;
		this.getItems().addAll(items);
		this.columnMapper = createColumnMapper();
		this.options = options;
		//		tableColumns = FXCollections.observableArrayList();

		List<TableColumn<T, ?>> tableColumns = extracted(clazz, columns, this, this.columnMapper, this.options);

		this.setEditable(true);

		this.getColumns().addAll(tableColumns);
	}

	public IColumnMapper<T> createColumnMapper() {
		return new BaseColumnMapper<>();
	}

	public void addItems(List<T> items) {
		this.getItems().addAll(items);
	}

	public void addItems(T item) {
		this.getItems().add(item);
	}

	/**
	 * name에 일치하는 컬럼인덱스를 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 11. 12.
	 * @param name
	 * @return
	 */
	public int getColumnIndex(final String name) {
		ObservableList<TableColumn<T, ?>> columns = this.getColumns();
		for (int i = 0; i < columns.size(); i++) {
			TableColumn<T, ?> tableColumn = columns.get(i);
			if (name.equals(tableColumn.getId())) {
				return i;
			}
		}
		return -1;
	}

	private static <T> List<String> createColumns(Class<T> clazz) {

		List<String> columns = new ArrayList<>();

		if (AbstractDVO.class.isAssignableFrom(clazz)) {
			try {
				Field fields = getField(AbstractDVO.class, COMMONS_CLICKED);
				append(columns, fields);
			} catch (NoSuchFieldException e) {
				LOGGER.error(ValueUtil.toString(e));
			}

		}
		Field[] declaredFields = getFields(clazz);

		append(columns, declaredFields);

		return columns;
	}

	private static <T> Field[] getFields(Class<T> clazz) {
		return clazz.getDeclaredFields();
	}

	private static <T> Field getField(Class<T> clazz, String name) throws NoSuchFieldException {
		return clazz.getDeclaredField(name);
	}

	private static void append(List<String> columns, Field... fields) {
		if (fields == null)
			return;

		for (Field field : fields) {
			columns.add(field.getName());
		}
	}

	private TableColumn<T, ?> generateTableColumns(Class<?> classType, IColumnMapper<T> mapper, String columnName) {
		return generateTableColumns(classType, this.columnMapper, columnName, this.options);
	}

	private static <T extends AbstractDVO> TableColumn<T, ?> generateTableColumns(Class<?> classType, IColumnMapper<T> mapper,
			String columnName, IOptions options) {
		return mapper.generateTableColumns(classType, columnName, options);
	}

	public IColumnMapper<T> getColumnMapper() {
		return columnMapper;
	}

	public IOptions getColumnNaming() {
		return options;
	}

	/**
	 * @param clazz
	 *            the clazz to set
	 */
	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 20.
	 * @return
	 */
	public Class<T> getClazz() {
		return this.clazz;
	}

	private static <T extends AbstractDVO> List<TableColumn<T, ?>> extracted(Class<T> clazz, List<String> columns, TableView<T> view,
			IColumnMapper<T> columnMapper, IOptions options) {

		List<TableColumn<T, ?>> tableColumns = FXCollections.observableArrayList();
		// options속성중 showRowNumber값이 true일경우 No.컬럼을 추가하고 로우개수를 표시한다.
		if (options.showRowNumber()) {
			TableColumn<T, Integer> numberColumn = new TableColumn<T, Integer>();
			numberColumn.setCellValueFactory(new NumberingCellValueFactory<>(view.getItems()));
			numberColumn.setText("No.");
			numberColumn.setPrefWidth(40);
			tableColumns.add(numberColumn);
		}

		for (String column : columns) {

			if (!options.useCommonCheckBox() && CommonConst.COMMONS_FILEDS_COMMONS_CLICKED.equals(column)) {
				continue;
			}

			if (!options.isCreateColumn(column))
				continue;

			try {
				try {
					Field declaredField = clazz.getDeclaredField(column);
					if (declaredField != null) {
						Class<?> type = declaredField.getType();
						tableColumns.add(generateTableColumns(type, columnMapper, column, options));
					}
				} catch (NoSuchFieldException e) {
					if (AbstractDVO.class.isAssignableFrom(clazz)) {
						Field superClassField = AbstractDVO.class.getDeclaredField(column);
						if (superClassField != null) {
							Class<?> type = superClassField.getType();
							tableColumns.add(generateTableColumns(type, columnMapper, column, options));
						}
					}
				}

			} catch (NoSuchFieldException | SecurityException e) {
				LOGGER.error(ValueUtil.toString(e));
			}

		}
		return tableColumns;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 7. 17. 
	 * @param model
	 * @param view
	 * @param options
	 */
	public static <T extends AbstractDVO> void install(Class<T> model, TableView<T> view, IOptions options) {
		List<String> createColumns = createColumns(model);
		if (view.getItems() == null) {
			view.getItems().addAll(FXCollections.observableArrayList());
		}

		List<TableColumn<T, ?>> columns = extracted(model, createColumns, view, new BaseColumnMapper<>(), options);

		view.getColumns().addAll(columns);
	}

}
