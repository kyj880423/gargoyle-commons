/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.grid
 *	작성일   : 2018. 7. 10.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.kyj.fx.commons.fx.controls.text.SimpleTextView;
import com.kyj.fx.commons.memory.StageStore;
import com.kyj.fx.commons.models.BigDataDVO;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.util.StringConverter;

/**
 * @author KYJ
 *
 */
public class MapToTableViewGenerator {

	private IntegerProperty startRowIndexProperty = new SimpleIntegerProperty();
	private IntegerProperty endRowIndexProperty = new SimpleIntegerProperty();
	private IntegerProperty startColIndexProperty = new SimpleIntegerProperty();
	private IntegerProperty endColIndexProperty = new SimpleIntegerProperty();

	private List<Map<String, Object>> query;
	private TableView<Map<String, Object>> tbResult;
	private boolean callLoad = false;

	/**
	 * @return the tbResult
	 */
	public TableView<Map<String, Object>> getTableView() {
		/* 사용자 실수를 방지하기 위한 예외처리. 반드시 load함수를 호출뒤 반환받게함. */
		if (!callLoad) {
			throw new RuntimeException("call to load function.");
		}
		return tbResult;
	}

	/**
	 * @param query
	 */
	public MapToTableViewGenerator(List<Map<String, Object>> query) {
		this.query = query;
	}

	public MapToTableViewGenerator() {
	}
	
	private Function<TableColumn<Map<String, Object>, Object>, TableColumn<Map<String, Object>, Object>> userCustom = (tb) -> tb;

	public final TableColumn<Map<String, Object>, Object> createTableColumn(String name) {
		TableColumn<Map<String, Object>, Object> e = new TableColumn<Map<String, Object>, Object>(name);
		e.setCellFactory(arg -> {
			return new DragSelectionCell();
		});
		e.setCellValueFactory(arg -> {
			Object value = arg.getValue().get(name);
			return new SimpleObjectProperty<>(value);
		});
		e.setComparator((a, b) -> {

			if (a == null && b == null)
				return 0;

			if (a == null)
				return -1;

			if (b == null)
				return -1;

			if (ValueUtil.isNumber(a.toString()) && ValueUtil.isNumber(b.toString())) {
				return Double.compare(Double.parseDouble(a.toString()), Double.parseDouble(b.toString()));
			} else {
				return com.kyj.fx.commons.utils.ValueUtil.compare(a.toString(), b.toString());
			}

		});

		return getUserCustom().apply(e);

	}

	/**
	 * @return the userCustom
	 */
	public Function<TableColumn<Map<String, Object>, Object>, TableColumn<Map<String, Object>, Object>> getUserCustom() {
		return userCustom;
	}

	/**
	 * @param userCustom
	 *            the userCustom to set
	 */
	public void setUserCustom(Function<TableColumn<Map<String, Object>, Object>, TableColumn<Map<String, Object>, Object>> userCustom) {
		this.userCustom = userCustom;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 10.
	 * @return
	 */
	public TableView<Map<String, Object>> load() {
		callLoad = true;

		if (query.isEmpty())
			return tbResult;

		/* 디폴트 속성 정의 */
		tbResult = new TableView<>();
		tbResult.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tbResult.getSelectionModel().setCellSelectionEnabled(true);
		FxUtil.installClipboardKeyEvent(tbResult);

		/* 컬럼정보를 생성함. */
		Map<String, Object> map = query.get(0);
		Iterator<String> iterator = map.keySet().iterator();
		ObservableList<TableColumn<Map<String, Object>, Object>> cols = FXCollections.observableArrayList();

		TableColumn<Map<String, Object>, Object> rowCol = createTableColumn("Index");
		rowCol.setCellValueFactory(new NumberingCellValueFactory(tbResult));
		cols.add(rowCol);

		while (iterator.hasNext()) {
			String column = iterator.next();
			TableColumn<Map<String, Object>, Object> e = createTableColumn(column);
			cols.add(e);
		}

		tbResult.getColumns().setAll(cols);

		/* 데이터를 추가함. */
		tbResult.getItems().addAll(query);

		return tbResult;
	}

	/**
	 * @author KYJ
	 *
	 */
	public class DragSelectionCell extends TextFieldTableCell<Map<String, Object>, Object> {
		public DragSelectionCell() {
			this.setConverter(new StringConverter<Object>() {
				@Override
				public String toString(Object object) {
					return object == null ? "" : object.toString();
				}

				@Override
				public Object fromString(String string) {
					return null;
				}
			});
			setOnDragDetected(event -> {
				startFullDrag();
				startRowIndexProperty.setValue(getIndex());
				startColIndexProperty.setValue(tbResult.getColumns().indexOf(getTableColumn()));
			});
			setOnMouseDragEntered(event -> {
				endRowIndexProperty.setValue(getIndex());
				endColIndexProperty.setValue(tbResult.getColumns().indexOf(getTableColumn()));
				event.consume();
			});
			setOnMouseClicked(event -> {
				if (tbResult.getColumns().indexOf(getTableColumn()) == 0) {
					startColIndexProperty.setValue(0);
					startRowIndexProperty.setValue(getIndex());
					endRowIndexProperty.setValue(getIndex());
					tableSelectCell();
				}

				if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {

					Object item = DragSelectionCell.this.getItem();
					if (item instanceof BigDataDVO) {
						BigDataDVO dataDVO = (BigDataDVO) item;

						// {
						FxUtil.createStageAndShow(new SimpleTextView(dataDVO.getValue()), stage -> {
							stage.initOwner(StageStore.getPrimaryStage());
						});

					} else if (item instanceof String) {
						FxUtil.createStageAndShow(new SimpleTextView(item.toString()), stage -> {
							stage.setAlwaysOnTop(true);
							stage.initOwner(StageStore.getPrimaryStage());
						});
					}

				}
			});

		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 10.
	 */
	private void tableSelectCell() {
		int starRowtIndex = startRowIndexProperty.get();
		int endRowIndex = endRowIndexProperty.get();
		int starColtIndex = startColIndexProperty.get();
		int endColIndex = endColIndexProperty.get();

		TableViewSelectionModel<Map<String, Object>> selectionModel = tbResult.getSelectionModel();
		selectionModel.clearSelection();
		if (starColtIndex == 0) {
			selectionModel.selectRange(starRowtIndex, tbResult.getColumns().get(0), endRowIndex,
					tbResult.getColumns().get(tbResult.getColumns().size() - 1));
		} else {
			selectionModel.selectRange(starRowtIndex, tbResult.getColumns().get(starColtIndex), endRowIndex,
					tbResult.getColumns().get(endColIndex));
		}
	}
}
