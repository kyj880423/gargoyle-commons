/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.sun.javafx.scene.control.skin
 *	작성일   : 2017. 7. 5.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid.skin;

import static impl.org.controlsfx.i18n.Localization.asKey;
import static impl.org.controlsfx.i18n.Localization.localize;

import org.controlsfx.control.spreadsheet.SpreadsheetView;

import com.sun.javafx.scene.control.skin.NestedTableColumnHeader;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;

import impl.org.controlsfx.spreadsheet.CellView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumnBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.WindowEvent;

/**
 * @author KYJ
 *
 */
public class CNestedTableColumnHeader extends NestedTableColumnHeader {

	private TableViewSkinBase skin;

	public CNestedTableColumnHeader(final TableViewSkinBase skin, final TableColumnBase tc) {
		super(skin, tc);
		this.skin = skin;
//		((CTableView) skin.getSkinnable()).fixingColumnsAllowedProperty().addListener(new ChangeListener<Boolean>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//				CellView.getValue(() -> {
//					if (tc != null) {
//						tc.setContextMenu(getColumnContextMenu());
//					}
//
//				});
//			}
//		});
	}

	private MenuItem fixItem;

//	protected ContextMenu getColumnContextMenu() {
//		if (isColumnFixable()) {
//			final ContextMenu contextMenu = new ContextMenu();
//
//			this.fixItem = new MenuItem(localize(asKey("spreadsheet.column.menu.fix"))); //$NON-NLS-1$
//			contextMenu.setOnShowing(new EventHandler<WindowEvent>() {
//
//				@Override
//				public void handle(WindowEvent event) {
//					if (!isFixed()) {
//						fixItem.setText(localize(asKey("spreadsheet.column.menu.fix"))); //$NON-NLS-1$
//					} else {
//						fixItem.setText(localize(asKey("spreadsheet.column.menu.unfix"))); //$NON-NLS-1$
//					}
//				}
//			});
//			fixItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("pinSpreadsheetView.png")))); //$NON-NLS-1$
//			fixItem.setOnAction(new EventHandler<ActionEvent>() {
//				@Override
//				public void handle(ActionEvent arg0) {
//					if (!isFixed()) {
//						setFixed(true);
//					} else {
//						setFixed(false);
//					}
//				}
//			});
//			contextMenu.getItems().addAll(fixItem);
//
//			return contextMenu;
//		} else {
//			return new ContextMenu();
//		}
//	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		layoutFixedColumns();
	}

	protected void layoutFixedColumns() {

		final CTableViewSkin tbSkin = ((CTableViewSkin) getTableViewSkin());
		CTableView view = (CTableView) tbSkin.getSkinnable();

		double hbarValue = tbSkin.getHBar().getValue();

		final int labelHeight = (int) getChildren().get(0).prefHeight(-1);
		//fixed컬럼 시작 위치. 헤더만 정의됨
		double fixedColumnWidth = 0;
		double x = snappedLeftInset() ;
		int max = getColumnHeaders().size();

//		ObservableList fixedColumns = view.getFixedColumns();
		int fixedColumnIndex = view.getFixedColumnIndex();
		ObservableList columns = view.getColumns();
		max = max > columns.size() ? columns.size() : max;
		for (int j = 0; j < max; j++) {
			final TableColumnHeader n = getColumnHeaders().get(j);
			final double prefWidth = snapSize(n.prefWidth(-1));
			n.setPrefHeight(24.0);
			//If the column is fixed
			if (fixedColumnIndex >= j /*fixedColumns.indexOf(columns.get(j)) != -1*/) {
				double tableCellX = 0;
				//If the column is hidden we have to translate it
				if (hbarValue + fixedColumnWidth > x) {

					tableCellX = Math.abs(hbarValue - x + fixedColumnWidth);

					n.toFront();
					fixedColumnWidth += prefWidth;
				}
				n.relocate(x + tableCellX , labelHeight + snappedTopInset());
			}

			x += prefWidth;
		}

	}

	/**
	 * Fix this column to the left if possible, although it is recommended that
	 * you call {@link #isColumnFixable()} before trying to fix a column.
	 *
	 * If you want to fix several columns (because of a span for example), add
	 * all the columns directly in {@link SpreadsheetView#getFixedColumns() }.
	 * Always use {@link SpreadsheetView#areSpreadsheetColumnsFixable(java.util.List)
	 * } before.
	 *
	 * @param fixed
	 */
//	public void setFixed(boolean fixed) {
//		if (fixed) {
//
//			((CTableView) skin.getSkinnable()).getFixedColumns().add(this);
//		} else {
//			((CTableView) skin.getSkinnable()).getFixedColumns().removeAll(this);
//		}
//	}

	/**
	 * Return whether this column is fixed or not.
	 * 
	 * @return true if this column is fixed.
	 */
//	public boolean isFixed() {
//		return ((CTableView) skin.getSkinnable()).getFixedColumns().contains(this);
//	}
//
//	private boolean canFix;

	//
	//	/**
	//	* Indicate whether this column can be fixed or not. Call that method before
	//	* calling {@link #setFixed(boolean)} or adding an item to
	//	* {@link SpreadsheetView#getFixedColumns()}.
	//	*
	//	* A column cannot be fixed alone if any cell inside the column has a column
	//	* span superior to one.
	//	*
	//	* @return true if this column is fixable.
	//	*/
//	public boolean isColumnFixable() {
//		CTableViewSkin skin = (CTableViewSkin) getTableViewSkin();
//		return canFix && ((CTableView) skin.getSkinnable()).isFixingColumnsAllowed();
//	}
	
	
}
