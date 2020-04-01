/**
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 * KYJ
 * 2015. 10. 10.
 */
package com.kyj.fx.commons.fx.controls.grid;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;

/**
 * UPDATE 상태값을 처리하기 위한 이벤트
 * 
 * @author KYJ
 *
 */
public class GridBaseTableCellValueChangeEvent<T> extends ActionEvent implements EventHandler<GridBaseTableCellValueChangeEvent> {

	private int rowIndex;
	private Object item;
	private TableColumn<T, Object> tableColumn;
	private TableRow tableRow;

	/**
	 * The only valid EventType for the ActionEvent.
	 */
	public static final EventType<GridBaseTableCellValueChangeEvent> GRID_BASE_TABLE_CELL_CHANGE_EVENT = new EventType<GridBaseTableCellValueChangeEvent>(
			Event.ANY, "GridBaseTableCellChangeEvent");

	/**
	 * Creates a new {@code ActionEvent} with an event type of {@code ACTION}.
	 * The source and target of the event is set to {@code NULL_SOURCE_TARGET}.
	 */
	public GridBaseTableCellValueChangeEvent() {
		super();
	}

	/**
	 * KYJ
	 */
	private static final long serialVersionUID = 1L;

	public GridBaseTableCellValueChangeEvent(Object source, EventTarget target) {
		super(source, target);
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public Object getItem() {
		return item;
	}

	public void setItem(Object item) {
		this.item = item;
	}

	/**
	 * @return the tableColumn
	 */
	public TableColumn<T, Object> getTableColumn() {
		return tableColumn;
	}

	/**
	 * @param tableColumn
	 *            the tableColumn to set
	 */
	public void setTableColumn(TableColumn<T, Object> tableColumn) {
		this.tableColumn = tableColumn;
	}

	/**
	 * @return the tableRow
	 */
	public TableRow getTableRow() {
		return tableRow;
	}

	/**
	 * @param tableRow
	 *            the tableRow to set
	 */
	public void setTableRow(TableRow tableRow) {
		this.tableRow = tableRow;
	}

	@Override
	public void handle(GridBaseTableCellValueChangeEvent event) {
		event.handle(this);
	}

}
