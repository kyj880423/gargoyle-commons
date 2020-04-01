/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.sun.javafx.scene.control.skin
 *	작성일   : 2017. 7. 6.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid.skin;

import java.util.List;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.sun.javafx.scene.control.skin.VirtualScrollBar;

import javafx.geometry.Insets;
import javafx.scene.control.IndexedCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * @author KYJ
 *
 */
public class CVirtualFlow<T extends IndexedCell> extends VirtualFlow<T> {

	private CTableView<?> tableView;

	public void init(CTableView<?> tableView) {
		this.tableView = tableView;
	}

	@SuppressWarnings("restriction")
	public VirtualScrollBar getVerticalbar() {
		return getVbar();
	}

	@SuppressWarnings("restriction")
	public VirtualScrollBar getHorizontalBar() {
		return getHbar();
	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();

		//이부분이 중요
		if (tableView != null) {
			/*
			 * 이 부분은 테이블 row를 의미함. fixed를 사용하기 위해선 갱신시켜줘야되는것으로 보임.
			 * 
			 */
			for (CTableRow<T> cell : (List<CTableRow<T>>) getCells()) {
				//				int index = cell.getIndex();
				//				System.out.println(index);
				cell.requestLayout();

//				cell.requestFocus();
			}
		}

	}

	/**
	 * With that comparator we can lay out our rows in the reverse order. That is to say from the bottom to the very top. In that manner we
	 * are sure that our spanning cells will COVER the cell below so we don't have any problems with missing hovering, the editor jammed
	 * etc. <br/>
	 *
	 * The only problem is for the fixed column but the {@link #getTopRow(int) } now returns the very first row and allow us to put some
	 * privileged TableCell in it if they feel the need to be on top in term of z-order.
	 *
	 * FIXME The best would be to put a TreeList of something like that in order not to sort the rows everytime, need investigation..
	 */
	//	private static final Comparator<GridRow> ROWCMP = new Comparator<GridRow>() {
	//		@Override
	//		public int compare(GridRow firstRow, GridRow secondRow) {
	//			// o1.getIndex() < o2.getIndex() ? -1 : +1;
	//			return secondRow.getIndex() - firstRow.getIndex();
	//		}
	//	};

	/**
	 * Sort the rows so that they stay in order for layout
	 */
	//	private void sortRows() {
	//		final List<GridRow> temp = (List<GridRow>) getCells();
	//		final List<GridRow> tset = new ArrayList<>(temp);
	//		Collections.sort(tset, ROWCMP);
	//		for (final TableRow<ObservableList<SpreadsheetCell>> r : tset) {
	//			r.toFront();
	//		}
	//	}

}
