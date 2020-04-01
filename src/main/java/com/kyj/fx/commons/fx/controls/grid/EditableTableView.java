/********************************
  * 프로젝트 : VisualFxVoEditor
  * 패키지   : com.kyj.fx.voeditor.visual.component.grid
  * 작성일   : 2016. 8. 23.
  * 작성자   : KYJ
  *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.functions.ConnectionSupplier;
import com.kyj.fx.commons.fx.controls.grid.EditableTableView.ColumnExpression;
import com.kyj.fx.commons.fx.controls.grid.EditableTableView.ValueExpression;
import com.kyj.fx.commons.utils.DbUtil;
import com.kyj.fx.commons.utils.ValueUtil;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

/***************************
 *
 * 데이터베이스 테이블과 1:1 관계를 가지는 테이블뷰를 생성하고 그 테이블과 관련된 CRUD를 처리할 수 있다.
 *
 * @author KYJ
 *
 ***************************/
public class EditableTableView extends TableView<Map<ColumnExpression, ObjectProperty<ValueExpression>>> {

	private static final String STYLE_MODIFIED_FIELD = "-fx-background-color:#AC777D";
	/**
	 * @최초생성일 2016. 8. 25.
	 */
	private static final String STYLE_PRIMARYKEY = "-fx-text-fill: #DD5044";
	private static final String STYLE_NULLABLE = "-fx-text-fill: #3D649B";

	private static Logger LOGGER = LoggerFactory.getLogger(EditableTableView.class);

	/**
	 * 데이터베이스로부터 한번에 가져올 수 있는 FECT_COUNT를 정의.
	 *
	 * @최초생성일 2016. 8. 25.
	 */
	private static final int FETCH_COUNT = DbUtil.DEFAULT_FETCH_SIZE;

	/**
	 * 데이터베이스로부터 가져올 수 있는 최대 MAX ROW를 정의한다.
	 *
	 * @최초생성일 2016. 8. 25.
	 */
	private static final int LIMIT_ROW_COUNT = DbUtil.DEFAULT_LIMIT_ROW_COUNT;

	/**
	 * 현재 보여지고 있는 테이블명에 대한 메타정보가 보관된다. 쿼리문 생성시 아래 변수에 바인드된 정보가 사용된다.
	 *
	 * @최초생성일 2016. 8. 25.
	 */
	private StringProperty tableName = new SimpleStringProperty();

	// private ObservableList<Map<ColumnExpression,
	// ObjectProperty<ValueExpression>>> addedList =
	// FXCollections.observableArrayList();
	private ObservableList<Map<ColumnExpression, ObjectProperty<ValueExpression>>> removedList = FXCollections.observableArrayList();

	private Map<String, ColumnExpression> columnMap = new ConcurrentHashMap<>();

	/**
	 * 실행이력정보. select문절만 저장처리.
	 *
	 * @최초생성일 2016. 9. 2.
	 */
	private LinkedList<String> history = new LinkedList<String>();
	private static final int HISTORY_LIMITED_SIZE = 30;

	private ConnectionSupplier connectionSupplier;

	public EditableTableView() {
		this.setEditable(true);

		// new ConcurrentLinkedQueue<>();
		getItems().addListener(itemChangeListener);

	}

	public void setConnectionSuppier(ConnectionSupplier connectionSupplier) {
		this.connectionSupplier = connectionSupplier;
	}
	
//	public void load() {
//		
//	}

	private ListChangeListener<Map<ColumnExpression, ObjectProperty<ValueExpression>>> itemChangeListener = new ListChangeListener<Map<ColumnExpression, ObjectProperty<ValueExpression>>>() {

		@Override
		public void onChanged(
				javafx.collections.ListChangeListener.Change<? extends Map<ColumnExpression, ObjectProperty<ValueExpression>>> c) {

			if (c.next()) {
				if (c.wasAdded()) {
					List<? extends Map<ColumnExpression, ObjectProperty<ValueExpression>>> addedSubList = c.getAddedSubList();
					for (Map<ColumnExpression, ObjectProperty<ValueExpression>> m : addedSubList) {
						m.put(ColumnExpression.INSTANCE_NEW_ROW_META(), new SimpleObjectProperty<>(new ValueExpression()));

						for (TableColumn<Map<ColumnExpression, ObjectProperty<ValueExpression>>, ?> tc : getColumns()) {
							ColumnExpression _col = (ColumnExpression) tc.getUserData();
							ValueExpression valueExpression = new ValueExpression();

							valueExpression.setRealValue(null);
							valueExpression.setDisplayText("");

							valueExpression.isNew = true;
							valueExpression.isPrimaryKey = _col.isPrimaryColumn;
							valueExpression.setColumnExpression(_col);
							m.put(_col, new SimpleObjectProperty<>(valueExpression));
						}

						// Iterator<ColumnExpression> iterator =
						// m.keySet().iterator();
						// while(iterator.hasNext())
						// {
						// ColumnExpression next = iterator.next();
						// ObjectProperty<ValueExpression> objectProperty =
						// m.get(next);
						// ValueExpression valueExpression =
						// objectProperty.get();
						//
						// }

					}
				} else if (c.wasRemoved()) {
					removedList.addAll(c.getRemoved());
				}
			}
		}

	};

	/**
	 * 테이블명을 입력하면 select문을 생성하여 데이터를 조회후 그리드에 데이터를 맵핑하는 처리를한다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 8. 25.
	 * @param tableName
	 *            조회하고자하는 테이블명을 기입한다.
	 * @throws Exception
	 */
	public void readByTableName(String sql, String tableName) throws Exception {
		readByTableName(sql, tableName, true);
	}

	class TableSet {
		public TableSet(String cat, String schema, String name) {
			this.cat = cat;
			this.schema = schema;
			this.name = name;
		}

		String cat;
		String schema;
		String name;
	}

	private void readByTableName(String sql, String tableName, boolean appendHist) throws Exception {
		LOGGER.debug(sql);
		getColumns().clear();
		getItems().clear();
		removedList.clear();
		columnMap.clear();

		try (Connection connection = connectionSupplier.get()) {

			// CHECK TABLE
			List<TableSet> tables = DbUtil.tables(connection, tableName, rs -> {
				try {
					String cat = rs.getString(1);
					String schama = rs.getString(2);
					String name = rs.getString(3);

					// because of api is like pattern, check again the table name.
					
					if (ValueUtil.equalsIgnoreCase(tableName.toUpperCase(), name.toUpperCase()))
						return new TableSet(cat, schama, name);

				} catch (Exception e) {

				}
				return null;
			});
			List<String> pks = new ArrayList<>();

			if (tables != null) {

				if (tables.size() > 1)
					LOGGER.warn("want!!! the table count more then 1");

				TableSet tableSet = tables.get(0);
				pks.addAll(DbUtil.pks(connection, tableSet.cat, tableSet.schema, tableSet.name));
			}

			Map<String, Boolean> columnsToMap = DbUtil.columnsToMap(connection, tableName, rs -> {
				try {
					return rs.getString(4);
				} catch (Exception e) {
					return null;
				}

			}, rs -> {
				try {
					// 18. IS_NULLABLE String => ISO rules are used to determine
					// the nullability for a column. ◦ YES --- if the column can
					// include NULLs
					// ◦ NO --- if the column cannot include NULLs
					// ◦ empty string --- if the nullability for the column is
					// unknown

					return Boolean.valueOf("YES".equals(rs.getString(18)));
				} catch (Exception e) {
					return false;
				}
			});

			LOGGER.debug("nullable columns ? {} ", columnsToMap);
			DbUtil.select(connection, sql, FETCH_COUNT, LIMIT_ROW_COUNT,
					new BiFunction<ResultSetMetaData, ResultSet, List<Map<String, Object>>>() {

						@Override
						public List<Map<String, Object>> apply(ResultSetMetaData t, ResultSet u) {

							try {
								int columnCount = t.getColumnCount();

								for (int i = 1; i <= columnCount; i++) {

									String columnName = t.getColumnName(i);
									ColumnExpression columnExp = new ColumnExpression(columnName);
									columnExp.isPrimaryColumn = pks.contains(columnName);
									columnExp.isNullableColumn = columnsToMap.containsKey(columnName)
											&& (columnsToMap.get(columnName) == true);
									columnExp.setColumnType(t.getColumnType(i));

									TableColumn<Map<ColumnExpression, ObjectProperty<ValueExpression>>, ValueExpression> e = new TableColumn<>(
											columnName);

									e.setUserData(columnExp);
									e.setCellValueFactory(DynamicCallback.fromTableColumn(columnExp));
									e.setCellFactory(DEFAULT_CELL_FACTORY);

									e.setEditable(true);

									columnMap.put(columnName, columnExp);

									getColumns().add(e);
								}

								while (u.next()) {

									Map<ColumnExpression, ObjectProperty<ValueExpression>> hashMap = new HashMap<>();

									for (int i = 1; i <= columnCount; i++) {
										String columnName = t.getColumnName(i);
										ColumnExpression columnExp = columnMap.get(columnName);// new
																								// ColumnExpression(columnName);

										ValueExpression valueExp = new ValueExpression();
										valueExp.displayText.set(u.getString(columnName));
										valueExp.isPrimaryKey = pks.contains(columnName);
										valueExp.realValue.set(u.getObject(columnName));
										valueExp.setColumnExpression(columnExp);
										hashMap.put(columnExp, new SimpleObjectProperty<>(valueExp));
									}

									getItems().removeListener(itemChangeListener);
									getItems().add(hashMap);
									getItems().addListener(itemChangeListener);

								}

							} catch (SQLException e) {
								LOGGER.error(ValueUtil.toString(e));
							}
							return null;
						}

					});

			if (appendHist) {

				if (history.size() >= HISTORY_LIMITED_SIZE) {
					history.removeFirst();
				}

				history.add(sql);
			}

			this.tableName.set(tableName);
		}

	}

	public void addNewItem(Map<ColumnExpression, ObjectProperty<ValueExpression>> newValue) {
		getItems().removeListener(itemChangeListener);
		getItems().add(newValue);
		getItems().addListener(itemChangeListener);
	}

	/**
	 * 저장 기능을 구현
	 *
	 * @throws Exception
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 8. 25.
	 */
	public void save() throws Exception {

		// List<String> saveSqlList = new ArrayList<>();

		List<String> saveList = new ArrayList<String>();
		STATUS statements1 = getStatements(DML.INSERT, (status, list) -> {
			saveList.addAll(list);
			return status;
		});

		STATUS statements2 = getStatements(DML.UPDATE, (status, list) -> {
			saveList.addAll(list);
			return status;
		});

		STATUS statements3 = getStatements(DML.DELETE, (status, list) -> {
			saveList.addAll(list);
			return status;
		});

		if (statements1 == STATUS.OK && statements2 == STATUS.OK && statements3 == STATUS.OK) {

			try (Connection con = connectionSupplier.get()) {
				if (!saveList.isEmpty()) {
					int transactionedScope = DbUtil.getTransactionedScope(con, saveList, (list) -> {
						return list;
					}, ex -> {
						throw new RuntimeException(ex);
					});
					if (transactionedScope == -1) {
						sendFailMessage("트랜잭션 처리중 에러 발생");
						// throw new GargoyleException("Execute Fail.");
					} else {
						readByTableName(history.getLast(), tableName.getValue(), false);
						sendTransactionSuccesslMessage("작업 성공 - " + saveList.size() + " 건");
					}
				} else {
					sendFailMessage("처리할 데이터가 없습니다.");
				}
			}

		} else {
			// throw new GargoyleException("Invalide status.");
			sendFailMessage("데이터 처리중 에러 발생");
		}

	}

	private void sendTransactionSuccesslMessage(String message) {
		if (null != setOnTransactionSucessListener) {
			setOnTransactionSucessListener.accept(message);
		}
	}

	private void sendFailMessage(String message) {
		if (null != onFailListener) {
			onFailListener.accept(message);
		}
	}

	private STATUS getStatements(DML dml, BiFunction<STATUS, List<String>, STATUS> function) {

		List<String> saveSqlList = new ArrayList<>();

		List<Map<ColumnExpression, ObjectProperty<ValueExpression>>> items = Collections.emptyList();
		switch (dml) {

		case UPDATE:
			items = getItems().stream().filter(m -> {
				return !ColumnExpression.isContainsNewRowMeata(m);
			}).collect(Collectors.toList());
			break;

		case DELETE:
			items = this.removedList;
			break;

		case INSERT:
			items = getItems().stream().filter(m -> {
				return ColumnExpression.isContainsNewRowMeata(m);
			}).collect(Collectors.toList());
			break;

		default:
			break;
		}

		if (items.isEmpty())
			return STATUS.OK;

		int modifiedCnt = 0;

		// 키값이 존재하지않는 테이블인경우 where조건은 수정되지않는 컬럼으로 사용.
		boolean isNotContainsPrimaryTable = false;

		// 키값도 존재하지않을뿐더러 변경된 컬럼도 없는경우
		boolean isNotContainsModifiedTable = false;

		if (items != null && !items.isEmpty()) {
			long count = items.get(0).values().stream().filter(vo -> vo.getValue().isPrimaryKey).count();

			// 키값 존재유무에 따른 플래그 update
			if (count == 0)
				isNotContainsPrimaryTable = true;

			// 키값이 없는 상태에서 수정된 컬럼도 없는 경우 상태 update
			if (count == 0) {
				long modifiedCount = items.get(0).values().stream().filter(vo -> vo.getValue().isModified).count();
				if (modifiedCount == 0)
					isNotContainsModifiedTable = true;
			}

		}

		for (Map<ColumnExpression, ObjectProperty<ValueExpression>> m : items) {

			StringBuffer whereStatement = new StringBuffer();
			StringBuffer setStatement = new StringBuffer();
			StringBuffer columnsStatement = new StringBuffer();
			StringBuffer valuesStatement = new StringBuffer();

			Iterator<ColumnExpression> iterator = m.keySet().iterator();
			modifiedCnt = 0;
			while (iterator.hasNext()) {

				ColumnExpression colummExp = iterator.next();

				if (colummExp.isNewRowMeta())
					continue;

				if (colummExp.isMetadata)
					continue;

				// else {
				ObjectProperty<ValueExpression> value = m.get(colummExp);
				ValueExpression valueExp = value.get();

				if (valueExp.isPrimaryKey) {

					// 기본키값이 빈 경우 메세지.
					if (valueExp.getDisplayText() == null /*
															 * || valueExp.
															 * getDisplayText().
															 * isEmpty()
															 */) {
						return function.apply(STATUS.PK_VAL_IS_EMPTY, Collections.emptyList());
					}
					whereStatement.append(String.format("and %s  = '%s'", colummExp, valueExp.getRealValue()));
				}

				if (valueExp.isModified) {
					setStatement.append(String.format("%s  = %s,", colummExp, getValue(colummExp,
							valueExp) /* valueExp.getDisplayText() */));
					modifiedCnt++;

					if (isNotContainsPrimaryTable) {
						whereStatement.append(String.format("and %s  = '%s'", colummExp, valueExp.getRealValue()));
					}

				}

				if ((!valueExp.isPrimaryKey && !valueExp.isModified) && (isNotContainsPrimaryTable || isNotContainsModifiedTable)) {
					if (null != valueExp.getRealValue())
						whereStatement.append(String.format("and %s  = '%s'", colummExp, valueExp.getRealValue()));
					else
						whereStatement.append(String.format("and %s  = null", colummExp));
				}

				columnsStatement.append(String.format("%s,", colummExp));
				valuesStatement.append(String.format("'%s',", valueExp.getDisplayText()));
				// }

			}
			int setStatementLength = setStatement.length();
			if (setStatementLength != 0) {
				setStatement.setLength(setStatementLength - 1);
			}

			int columnsStatementLength = columnsStatement.length();
			if (columnsStatementLength != 0) {
				columnsStatement.setLength(columnsStatementLength - 1);
			}

			int valuesStatementLength = valuesStatement.length();
			if (valuesStatementLength != 0) {
				valuesStatement.setLength(valuesStatementLength - 1);
			}

			switch (dml) {
			case INSERT:
				saveSqlList.add(String.format("insert into %s (%s) values (%s)", this.tableName.get(), columnsStatement.toString(),
						valuesStatement.toString()));
				break;
			case UPDATE:
				if (modifiedCnt > 0) {
					saveSqlList.add(String.format("update %s set %s where 1=1 %s", this.tableName.get(), setStatement.toString(),
							whereStatement.toString()));
				}
				break;
			case DELETE:
				saveSqlList.add(String.format("delete from %s where 1=1 %s", this.tableName.get(), whereStatement.toString()));
				break;
			}

		}
		return function.apply(STATUS.OK, saveSqlList);

	}

	enum STATUS {

		OK("success"), PK_VAL_IS_EMPTY("Primary Value is Empty.");

		private String str;

		STATUS(String str) {
			this.str = str;
		}
	}

	enum DML {
		INSERT, UPDATE, DELETE
	}

	/**
	 * columnType에 맞는 텍스트Value형태를 리턴한다.
	 *
	 * 정수형 -> dot가 없음 문자열 -> dot가 있음.
	 *
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 8. 25.
	 * @param ce
	 * @param ve
	 * @return
	 */
	public String getValue(ColumnExpression ce, ValueExpression ve) {

		int type = ce.getColumnType();
		String displayText = ve.getDisplayText();
		String returnValue = "";
		switch (type) {
		/* [시작] 문자열 */
		case java.sql.Types.CHAR:
			returnValue = singleDot(displayText);
			break;
		case java.sql.Types.BLOB:
			returnValue = singleDot(displayText);
			break;
		case java.sql.Types.CLOB:
			returnValue = singleDot(displayText);
			break;
		case java.sql.Types.NCHAR:
			returnValue = singleDot(displayText);
			break;
		case java.sql.Types.NCLOB:
			returnValue = singleDot(displayText);
			break;
		case java.sql.Types.VARCHAR:
			returnValue = singleDot(displayText);
			break;
		case java.sql.Types.LONGNVARCHAR:
			returnValue = singleDot(displayText);
			break;

		/* [시작] 정수 or 실수 */
		case java.sql.Types.NUMERIC:
			returnValue = displayText;
			break;
		case java.sql.Types.INTEGER:
			returnValue = displayText;
			break;
		case java.sql.Types.DOUBLE:
			returnValue = displayText;
			break;
		/* [끝] 정수 or 실수 */

		case java.sql.Types.BOOLEAN:
			returnValue = displayText;
			break;

		default:
			LOGGER.info("# uncatched data type. {}   #########################", type);
			returnValue = singleDot(displayText);
			break;
		}

		return returnValue;
	}

	private static final String singleDot(String value) {
		if (value == null)
			return "''";
		return "'".concat(value).concat("'");
	}

	StringConverter<ValueExpression> converter = new StringConverter<ValueExpression>() {

		@Override
		public String toString(ValueExpression object) {
			if (object == null)
				return "";
			return object.toString();
		}

		@SuppressWarnings("rawtypes")
		@Override
		public ValueExpression fromString(String string) {

			// Map<ColumnExpression, ObjectProperty<ColumnExpression>>
			// selectedItem =
			// EditableTableView.this.getSelectionModel().getSelectedItem();
			ObservableList<TablePosition> selectedCells = EditableTableView.this.getSelectionModel().getSelectedCells();
			TablePosition tablePosition = selectedCells.get(0);
			TableColumn tableColumn = tablePosition.getTableColumn();
			String columnName = tableColumn.getText();

			Map<ColumnExpression, ObjectProperty<ValueExpression>> map = getItems().get(tablePosition.getRow());

			// 신규 추가된 로우
			ObjectProperty<ValueExpression> objectProperty = map.get(new ColumnExpression(columnName));
			if (objectProperty == null) {
				ColumnExpression userData = (ColumnExpression) tableColumn.getUserData();
				ValueExpression initialValue = new ValueExpression();
				initialValue.setDisplayText(string);
				initialValue.setRealValue(null);
				initialValue.isPrimaryKey = userData.isPrimaryColumn;
				initialValue.isNew = true;
				initialValue.setColumnExpression(columnMap.get(columnName));
				objectProperty = new SimpleObjectProperty<>(initialValue);
				map.put(userData, objectProperty);

				return initialValue;
			}

			// 변경처리되는 로우
			ValueExpression cellData = objectProperty.get();
			// Object cellData =
			// tableColumn.getCellData(tablePosition.getRow());

			if (cellData != null /* cellData instanceof ValueExpression */) {
				ValueExpression cExp = (ValueExpression) cellData;
				cExp.setDisplayText(string);

				// 신규로우의 경우는 isModified 처리를 하지않는다. 그렇지않으면 update구문으로 인식한다.
				if (!ColumnExpression.isContainsNewRowMeata(map)) {
					cExp.isModified = true;
				}

				return cExp;
			}

			ValueExpression columnExpression = new ValueExpression();
			columnExpression.displayText.set(string);
			columnExpression.isNew = true;
			columnExpression.setColumnExpression(columnMap.get(columnName));
			return columnExpression;
		}
	};

	Callback<TableColumn<Map<ColumnExpression, ObjectProperty<ValueExpression>>, ValueExpression>, TableCell<Map<ColumnExpression, ObjectProperty<ValueExpression>>, ValueExpression>> DEFAULT_CELL_FACTORY = new Callback<TableColumn<Map<ColumnExpression, ObjectProperty<ValueExpression>>, ValueExpression>, TableCell<Map<ColumnExpression, ObjectProperty<ValueExpression>>, ValueExpression>>() {

		@Override
		public TableCell<Map<ColumnExpression, ObjectProperty<ValueExpression>>, ValueExpression> call(
				TableColumn<Map<ColumnExpression, ObjectProperty<ValueExpression>>, ValueExpression> param) {

			TextFieldTableCell<Map<ColumnExpression, ObjectProperty<ValueExpression>>, ValueExpression> cell = new TextFieldTableCell<Map<ColumnExpression, ObjectProperty<ValueExpression>>, ValueExpression>(
					converter) {

				protected boolean isChangedValue(ValueExpression oldItem, ValueExpression newItem) {

					// System.out.println(oldItem.getRealValue());
					// System.out.println(newItem.getDisplayText());
					String realValueString = (oldItem == null || oldItem.getRealValue() == null) ? "" : oldItem.getRealValue().toString();
					String displayString = (newItem == null || newItem.getDisplayText() == null) ? "" : newItem.getDisplayText();

					if (realValueString.isEmpty() && displayString.isEmpty())
						return false;

					return !StringUtils.equals(realValueString, displayString);
					// return !Objects.equal();
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * javafx.scene.control.TableCell#commitEdit(java.lang.Object)
				 */
				@Override
				public void commitEdit(ValueExpression newValue) {

					boolean changedValue = isChangedValue(getItem(), newValue);
					if (changedValue) {
						LOGGER.debug("commitEdit : {} ", changedValue);
						newValue.isModified = true;
					}

					super.commitEdit(newValue);
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * javafx.scene.control.cell.TextFieldTableCell#updateItem(java.
				 * lang.Object, boolean)
				 */
				@Override
				public void updateItem(ValueExpression item, boolean empty) {
					super.updateItem(item, empty);

					if (!empty) {
						if (item.isModified()) {
							setStyle(STYLE_MODIFIED_FIELD);
						} else if (item.getColumnExpression().isPrimaryColumn) {
							setStyle(STYLE_PRIMARYKEY);
						} else if (!item.getColumnExpression().isNullableColumn()) {
							setStyle(STYLE_NULLABLE);
						} else {
							setStyle(null);
						}
					} else {
						setStyle(null);
					}

				}

			};

			ColumnExpression userData = (ColumnExpression) param.getUserData();
			if (userData != null && userData.isPrimaryColumn) {
				cell.setStyle(STYLE_PRIMARYKEY);
			}
			// else {
			// cell.setStyle(null);
			// }
			return cell;
		}
	};

	public static class ColumnExpression {

		/**
		 * 새로 추가된 로우인지 확인
		 *
		 * @최초생성일 2016. 8. 25.
		 */
		private static final String $_NEW_ROW$ = "$NewRow$";

		private String columnName;
		private boolean isPrimaryColumn;
		private boolean isNullableColumn;
		private boolean isMetadata;
		private int columnType = java.sql.Types.NULL;

		public ColumnExpression(String columnName) {
			this.columnName = columnName;
		}

		/**
		 *
		 * @작성자 : KYJ
		 * @작성일 : 2016. 8. 25.
		 * @param columnType
		 */
		public void setColumnType(int columnType) {
			this.columnType = columnType;
		}

		/**
		 * java.sql.Types 클래스 참조.
		 *
		 * @작성자 : KYJ
		 * @작성일 : 2016. 8. 25.
		 * @return
		 */
		public int getColumnType() {
			return this.columnType;
		}

		/**
		 * @return the columnName
		 */
		public final String getColumnName() {
			return columnName;
		}

		private static ColumnExpression NEW_ROW_META;

		/**
		 * 새로 추가된 행인지에 대한 메타정보를 제공함.
		 *
		 * @작성자 : KYJ
		 * @작성일 : 2016. 8. 25.
		 * @return
		 */
		public static ColumnExpression INSTANCE_NEW_ROW_META() {
			if (NEW_ROW_META == null) {
				NEW_ROW_META = new ColumnExpression($_NEW_ROW$);
				NEW_ROW_META.isMetadata = true;
			}
			return NEW_ROW_META;
		}

		public boolean isNullableColumn() {
			return isNullableColumn;
		}

		public void setNullableColumn(boolean isNullableColumn) {
			this.isNullableColumn = isNullableColumn;
		}

		public static boolean isContainsNewRowMeata(Map<ColumnExpression, ?> map) {
			return map.containsKey(INSTANCE_NEW_ROW_META());
		}

		public static boolean isNewRowMeta(ColumnExpression colExp) {
			return colExp.isNewRowMeta();
		}

		public boolean isNewRowMeta() {
			return this.isMetadata && $_NEW_ROW$.equals(this.columnName);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ColumnExpression other = (ColumnExpression) obj;
			if (columnName == null) {
				if (other.columnName != null)
					return false;
			} else if (!columnName.equals(other.columnName))
				return false;
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return columnName;
		}

	}

	/**
	 * 데이터 모델.
	 *
	 * @author KYJ
	 *
	 */
	public class ValueExpression implements Comparable<ValueExpression>, Cloneable {

		private StringProperty displayText = new SimpleStringProperty();
		private ObjectProperty<Object> realValue = new SimpleObjectProperty<>();
		private boolean isPrimaryKey;
		private boolean isModified;
		private boolean isNew;
		private ColumnExpression columnExp;

		@Override
		public String toString() {
			return displayText.get();
		}

		public boolean isNew() {
			return isNew;
		}

		public void setNew(boolean isNew) {
			this.isNew = isNew;
		}

		/**
		 * @return the columnExp
		 */
		public final ColumnExpression getColumnExpression() {
			return columnExp;
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2016. 8. 25.
		 * @param columnExp
		 */
		public void setColumnExpression(ColumnExpression columnExp) {
			this.columnExp = columnExp;
		}

		public final StringProperty displayTextProperty() {
			return this.displayText;
		}

		public final java.lang.String getDisplayText() {
			return this.displayTextProperty().get();
		}

		public final void setDisplayText(final java.lang.String displayText) {
			this.displayTextProperty().set(displayText);
		}

		public final ObjectProperty<Object> realValueProperty() {
			return this.realValue;
		}

		public final java.lang.Object getRealValue() {
			return this.realValueProperty().get();
		}

		public final void setRealValue(final java.lang.Object realValue) {
			this.realValueProperty().set(realValue);
		}

		/**
		 * @return the isModified
		 */
		public final boolean isModified() {
			return isModified;
		}

		public void setModified(boolean f) {
			this.isModified = f;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(ValueExpression o) {

			if (o == null || o.displayText.get() == null)
				return -1;

			if (this.displayText.get() == null)
				return 1;

			return ValueUtil.compare(this.displayText.get(), o.displayText.get());
		}

		@Override
		public ValueExpression clone() throws CloneNotSupportedException {

			ValueExpression valueExpression = new ValueExpression();
			valueExpression.isModified = this.isModified;
			valueExpression.isNew = this.isNew;
			valueExpression.displayText.set(this.displayText.get());
			valueExpression.columnExp = this.columnExp;
			valueExpression.displayText.set(this.displayText.get());
			valueExpression.realValue.set(this.realValue.get());
			return valueExpression; // super.clone();
		}
	}

	/**
	 * 데이터베이스로부터 조회된 데이터 바인드처리하는 역할.
	 *
	 * @author KYJ
	 *
	 */
	static class DynamicCallback implements
			Callback<TableColumn.CellDataFeatures<Map<ColumnExpression, ObjectProperty<ValueExpression>>, ValueExpression>, ObservableValue<ValueExpression>> {

		ColumnExpression columnName;

		DynamicCallback(ColumnExpression columnName) {
			this.columnName = columnName;
		}

		@Override
		public ObservableValue<ValueExpression> call(
				CellDataFeatures<Map<ColumnExpression, ObjectProperty<ValueExpression>>, ValueExpression> param) {
			return param.getValue().get(columnName);
		}

		/**
		 * DynamicCallback 생성하는 static 함수.
		 *
		 * @작성자 : KYJ
		 * @작성일 : 2016. 8. 25.
		 * @param columnName
		 * @return
		 */
		static DynamicCallback fromTableColumn(ColumnExpression columnName) {
			return new DynamicCallback(columnName);
		}

	}

	public final StringProperty tableNameProperty() {
		return this.tableName;
	}

	public final java.lang.String getTableName() {
		return this.tableNameProperty().get();
	}

	public final void setTableName(final java.lang.String tableName) {
		this.tableNameProperty().set(tableName);
	}

	private Consumer<String> setOnTransactionSucessListener;

	private Consumer<String> onFailListener;

	/**
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 28.
	 * @param object
	 */
	public void setOnTransactionSucessListener(Consumer<String> setOnTransactionSucessListener) {
		this.setOnTransactionSucessListener = setOnTransactionSucessListener;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 28.
	 * @param onFailListener
	 */
	public void setOnFailListener(Consumer<String> onFailListener) {
		this.onFailListener = onFailListener;
	}

}
