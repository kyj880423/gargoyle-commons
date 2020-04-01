/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.sun.javafx.scene.control.skin
 *	작성일   : 2017. 7. 5.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid.skin;

import com.sun.javafx.scene.control.skin.TableViewSkin;

import javafx.scene.control.IndexedCell;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableRow;

/**
 * @author KYJ
 *
 */
@SuppressWarnings("restriction")
public class CTableViewSkin<T> extends TableViewSkin<T> {

	private CTableView<T> tableView;
	private CVirtualFlow cflow;

	public CTableViewSkin(CTableView<T> tableView) {
		super(tableView);
		this.tableView = tableView;
		this.cflow = (CVirtualFlow) super.flow;

	}

	@Override
	protected CTableHeaderRow createTableHeaderRow() {
		return new CTableHeaderRow(this);
	}

	@Override
	protected CVirtualFlow createVirtualFlow() {
		CVirtualFlow<IndexedCell> cVirtualFlow = new CVirtualFlow<>();
		cVirtualFlow.init((CTableView<?>) getSkinnable());
		return cVirtualFlow;
	}

	/** {@inheritDoc} */
	@Override
	public TableRow<T> createCell() {
		TableRow<T> cell;

		if (tableView.getRowFactory() != null) {
			cell = tableView.getRowFactory().call(tableView);
		} else {
			cell = new CTableRow<T>();
		}

		cell.updateTableView(tableView);
		return cell;
	}

	protected void onSelectLeftCell() {
		scrollHorizontally();
	}

	protected void onSelectRightCell() {
		scrollHorizontally();
	}

	public ScrollBar getHBar() {
		if (getFlow() != null) {
			return getFlow().getHorizontalBar();
		}
		return null;
	}

	public ScrollBar getVBar() {
		return getFlow().getVerticalbar();
	}

	CVirtualFlow<?> getFlow() {
		return (CVirtualFlow<?>) flow;
	}

	double fixedColumnWidth = 0;

	@Override
	public void scrollHorizontally() {
		super.scrollHorizontally();
	}

}
