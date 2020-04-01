/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2015. 10. 12.
 *	프로젝트 : SOS 미어캣 프로젝트
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.TableView;

/**
 * @author KYJ
 *
 */
public class CheckEvent<T extends AbstractDVO> extends Event {

	/**
	 * @최초생성일 2015. 10. 12.
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	public static final EventType<CheckEvent> CHECK_EVENT = new EventType<CheckEvent>(Event.ANY, "CHECK");

	private int rowIndex;
	private T item;
	private TableView<T> tableView;

	public CheckEvent() {
		super(CHECK_EVENT);
	}

	/**
	 * @return the rowIndex
	 */
	public int getRowIndex() {
		return rowIndex;
	}

	/**
	 * @param rowIndex
	 *            the rowIndex to set
	 */
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	/**
	 * @return the item
	 */
	public T getItem() {
		return item;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(T item) {
		this.item = item;
	}

	/**
	 * @return the tableView
	 */
	public TableView<T> getTableView() {
		return tableView;
	}

	/**
	 * @param tableView
	 *            the tableView to set
	 */
	public void setTableView(TableView<T> tableView) {
		this.tableView = tableView;
	}

}
