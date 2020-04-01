/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2017. 07. 12.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.fx.controls.grid.skin.CTableView;
import com.kyj.fx.commons.utils.ValueUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

/**
 *
 * FixedTableView를 테스트하기위한 모델
 *
 * @author KYJ
 *
 */
public class FixedBaseGridView<T extends AbstractDVO> extends CTableView<T> {
	private static Logger LOGGER = LoggerFactory.getLogger(FixedBaseGridView.class);
	public static final String COMMONS_CLICKED = CommonConst.COMMONS_FILEDS_COMMONS_CLICKED;
	private Class<T> clazz;

	private ObservableList<TableColumn<T, ?>> tableColumns;
	private IColumnMapper<T> columnMapper;
	private IOptions options;

	public FixedBaseGridView() {
		this.columnMapper = new BaseColumnMapper<>();
	}

	public FixedBaseGridView(Class<T> clazz) {
		this(clazz, new ArrayList<T>());
	}

	public FixedBaseGridView(Class<T> clazz, IOptions options) {
		this(clazz, FXCollections.emptyObservableList(), options);
	}

	public FixedBaseGridView(Class<T> clazz, List<T> items) {
		this(clazz, items, createColumns(clazz), new BaseOptions());
	}

	public FixedBaseGridView(Class<T> clazz, List<T> items, IOptions options) {
		this(clazz, items, createColumns(clazz), options);
	}

	public FixedBaseGridView(Class<T> clazz, List<T> items, List<String> columns, IOptions options) {
		this.clazz = clazz;
		this.getItems().addAll(items);
		this.columnMapper = createColumnMapper();
		this.options = options;
		tableColumns = FXCollections.observableArrayList();

		extracted(clazz, columns);

		this.setEditable(true);

		this.getColumns().addAll(tableColumns);
	}

	private void extracted(Class<T> clazz, List<String> columns) {

		// options속성중 showRowNumber값이 true일경우 No.컬럼을 추가하고 로우개수를 표시한다.
		if (options.showRowNumber()) {
			TableColumn<T, Integer> numberColumn = new TableColumn<T, Integer>();
			numberColumn.setCellValueFactory(new NumberingCellValueFactory<>(this.getItems()));
			numberColumn.setText("No.");
			numberColumn.setPrefWidth(40);
			this.getColumns().add(numberColumn);
		}

		for (String column : columns) {

			if (!options.useCommonCheckBox() && CommonConst.COMMONS_FILEDS_COMMONS_CLICKED.equals(column)) {
				continue;
			}

			if (!options.isCreateColumn(column))
				continue;

			try {
				try {
					Field declaredField = this.clazz.getDeclaredField(column);
					if (declaredField != null) {
						Class<?> type = declaredField.getType();
						tableColumns.add(generateTableColumns(type, column));
					}
				} catch (NoSuchFieldException e) {
					if (AbstractDVO.class.isAssignableFrom(clazz)) {
						Field superClassField = AbstractDVO.class.getDeclaredField(column);
						if (superClassField != null) {
							Class<?> type = superClassField.getType();
							tableColumns.add(generateTableColumns(type, column));
						}
					}
				}

			} catch (NoSuchFieldException | SecurityException e) {
				LOGGER.error(ValueUtil.toString(e));
			}

		}

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

	private TableColumn<T, ?> generateTableColumns(Class<?> classType, String columnName) {
		return columnMapper.generateTableColumns(classType, columnName, options);
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

}
