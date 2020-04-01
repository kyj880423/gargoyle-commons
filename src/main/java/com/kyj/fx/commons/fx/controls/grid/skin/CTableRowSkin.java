package com.kyj.fx.commons.fx.controls.grid.skin;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import com.sun.javafx.scene.control.behavior.TableRowBehavior;
import com.sun.javafx.scene.control.skin.TableRowSkinBase;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.sun.javafx.tk.Toolkit;

import javafx.animation.FadeTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.StyleOrigin;
import javafx.css.StyleableObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewFocusModel;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class CTableRowSkin<T> extends TableRowSkinBase<T, TableRow<T>, CellBehaviorBase<TableRow<T>>, TableCell<T, ?>> {

	private CTableView<T> tableView;
	private CTableViewSkin<T> tableViewSkin;

	public CTableRowSkin(TableRow<T> tableRow) {
		super(tableRow, new TableRowBehavior<T>(tableRow));

		this.tableView = (CTableView<T>) tableRow.getTableView();
		updateTableViewSkin();

		super.init(tableRow);

		registerChangeListener(tableRow.tableViewProperty(), "TABLE_VIEW");
	}

	@Override
	protected void handleControlPropertyChanged(String p) {
		super.handleControlPropertyChanged(p);
	}

	@Override
	protected TableCell<T, ?> getCell(TableColumnBase tcb) {
		TableColumn tableColumn = (TableColumn<T, ?>) tcb;
		TableCell cell = (TableCell) tableColumn.getCellFactory().call(tableColumn);

		// we set it's TableColumn, TableView and TableRow
		cell.updateTableColumn(tableColumn);
		cell.updateTableView(tableColumn.getTableView());
		cell.updateTableRow(getSkinnable());

		return cell;
	}

	@Override
	protected ObservableList<TableColumn<T, ?>> getVisibleLeafColumns() {
		return tableView.getVisibleLeafColumns();
	}

	@Override
	protected void updateCell(TableCell<T, ?> cell, TableRow<T> row) {
		cell.updateTableRow(row);
	}

	@Override
	protected DoubleProperty fixedCellSizeProperty() {
		return tableView.fixedCellSizeProperty();
	}

	@Override
	protected boolean isColumnPartiallyOrFullyVisible(TableColumnBase tc) {
		return tableViewSkin == null ? false : isColumnPartiallyOrFullyVisible((TableColumn) tc);
	}

	boolean isColumnPartiallyOrFullyVisible(TableColumn<T, ?> col) {
		if (col == null || !col.isVisible())
			return false;

		return true;
		// double scrollX = flow.getHbar().getValue();
		//
		// // work out where this column header is, and it's width (start -> end)
		// double start = 0;
		// ObservableList<TableColumn<T, ?>> visibleLeafColumns = tableView.getVisibleLeafColumns();
		//
		// for (int i = 0, max = visibleLeafColumns.size(); i < max; i++) {
		// TableColumnBase<T, ?> c = visibleLeafColumns.get(i);
		// if (c.equals(col))
		// break;
		// start += c.getWidth();
		// }
		// double end = start + col.getWidth();
		//
		// // determine the width of the table
		// final Insets padding = getSkinnable().getPadding();
		// double headerWidth = getSkinnable().getWidth() - padding.getLeft() + padding.getRight();
		//
		// return (start >= scrollX || end > scrollX) && (start < (headerWidth + scrollX) || end <= (headerWidth + scrollX));
	}

	@Override
	protected TableColumn<T, ?> getTableColumnBase(TableCell<T, ?> cell) {
		return cell.getTableColumn();
	}

	@Override
	protected ObjectProperty<Node> graphicProperty() {
		return null;
	}

	@Override
	protected Control getVirtualFlowOwner() {
		return getSkinnable().getTableView();
	}

	private void updateTableViewSkin() {
		CTableView<T> tableView = (CTableView<T>) getSkinnable().getTableView();
		if (tableView.getSkin() instanceof TableViewSkin) {
			tableViewSkin = (CTableViewSkin) tableView.getSkin();
		}
	}

	@Override
	protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
		switch (attribute) {
		case SELECTED_ITEMS: {
			// FIXME this could be optimised to iterate over cellsMap only
			// (selectedCells could be big, cellsMap is much smaller)
			List<Node> selection = new ArrayList<>();
			int index = getSkinnable().getIndex();
			for (TablePosition<T, ?> pos : tableView.getSelectionModel().getSelectedCells()) {
				if (pos.getRow() == index) {
					TableColumn<T, ?> column = pos.getTableColumn();
					if (column == null) {
						/* This is the row-based case */
						column = tableView.getVisibleLeafColumn(0);
					}
					TableCell<T, ?> cell = cellsMap.get(column).get();
					if (cell != null)
						selection.add(cell);
				}
				return FXCollections.observableArrayList(selection);
			}
		}
		case CELL_AT_ROW_COLUMN: {
			int colIndex = (Integer) parameters[1];
			TableColumn<T, ?> column = tableView.getVisibleLeafColumn(colIndex);
			if (cellsMap.containsKey(column)) {
				return cellsMap.get(column).get();
			}
			return null;
		}
		case FOCUS_ITEM: {
			TableViewFocusModel<T> fm = tableView.getFocusModel();
			TablePosition<T, ?> focusedCell = fm.getFocusedCell();
			TableColumn<T, ?> column = focusedCell.getTableColumn();
			if (column == null) {
				/* This is the row-based case */
				column = tableView.getVisibleLeafColumn(0);
			}
			if (cellsMap.containsKey(column)) {
				return cellsMap.get(column).get();
			}
			return null;
		}
		default:
			return super.queryAccessibleAttribute(attribute, parameters);
		}
	}

	static final Map<Control, Double> maxDisclosureWidthMap = new WeakHashMap<Control, Double>();
	private int fullRefreshCounter = DEFAULT_FULL_REFRESH_COUNTER;
	private static final int DEFAULT_FULL_REFRESH_COUNTER = 100;
	private boolean fixedCellSizeEnabled;
	private double fixedCellSize;

	@Override
	protected void layoutChildren(double x, final double y, final double w, final double h) {
		System.out.println("`````````" + x + " " + y + " " + w + " " + h);
		checkState();
		if (cellsMap.isEmpty())
			return;

		ObservableList<? extends TableColumnBase> visibleLeafColumns = getVisibleLeafColumns();
		if (visibleLeafColumns.isEmpty()) {
			super.layoutChildren(x, y, w, h);
			return;
		}

		TableRow<T> control = getSkinnable();

		///////////////////////////////////////////
		// indentation code starts here
		///////////////////////////////////////////
		double leftMargin = 0;
		double disclosureWidth = 0;
		double graphicWidth = 0;
		boolean indentationRequired = isIndentationRequired();
		boolean disclosureVisible = isDisclosureNodeVisible();
		int indentationColumnIndex = 0;
		Node disclosureNode = null;
		if (indentationRequired) {
			// Determine the column in which we want to put the disclosure node.
			// By default it is null, which means the 0th column should be
			// where the indentation occurs.
			TableColumnBase<?, ?> treeColumn = getTreeColumn();
			indentationColumnIndex = treeColumn == null ? 0 : visibleLeafColumns.indexOf(treeColumn);
			indentationColumnIndex = indentationColumnIndex < 0 ? 0 : indentationColumnIndex;

			int indentationLevel = getIndentationLevel(control);
			if (!isShowRoot())
				indentationLevel--;
			final double indentationPerLevel = getIndentationPerLevel();
			leftMargin = indentationLevel * indentationPerLevel;

			// position the disclosure node so that it is at the proper indent
			Control c = getVirtualFlowOwner();
			final double defaultDisclosureWidth = maxDisclosureWidthMap.containsKey(c) ? maxDisclosureWidthMap.get(c) : 0;
			disclosureWidth = defaultDisclosureWidth;

			disclosureNode = getDisclosureNode();
			if (disclosureNode != null) {
				disclosureNode.setVisible(disclosureVisible);

				if (disclosureVisible) {
					disclosureWidth = disclosureNode.prefWidth(h);
					if (disclosureWidth > defaultDisclosureWidth) {
						maxDisclosureWidthMap.put(c, disclosureWidth);

						// RT-36359: The recorded max width of the disclosure node
						// has increased. We need to go back and request all
						// earlier rows to update themselves to take into account
						// this increased indentation.
						final VirtualFlow<TableRow<T>> flow = getVirtualFlow();
						final int thisIndex = getSkinnable().getIndex();
						for (int i = 0; i < flow.getCellCount(); i++) {
							TableRow<T> cell = flow.getCell(i);
							if (cell == null || cell.isEmpty())
								continue;
							cell.requestLayout();
							cell.layout();
						}
					}
				}
			}
		}
		///////////////////////////////////////////
		// indentation code ends here
		///////////////////////////////////////////

		// layout the individual column cells
		double width;
		double height;

		final double verticalPadding = snappedTopInset() + snappedBottomInset();
		final double horizontalPadding = snappedLeftInset() + snappedRightInset();
		final double controlHeight = control.getHeight();
		final double hbarValue = tableViewSkin.getHBar().getValue();

		/**
		 * RT-26743:TreeTableView: Vertical Line looks unfinished. We used to not do layout on cells whose row exceeded the number of items,
		 * but now we do so as to ensure we get vertical lines where expected in cases where the vertical height exceeds the number of
		 * items.
		 */
		int index = control.getIndex();

		double fixedColumnWidth = 0;
		// List<TableCell<T, ?>> fixedCells = new ArrayList<>();

		if (index < 0/* || row >= itemsProperty().get().size() */)
			return;

		for (int column = 0, max = cells.size(); column < max; column++) {
			TableCell<T, ?> tableCell = cells.get(column);
			TableColumnBase<T, ?> tableColumn = getTableColumnBase(tableCell);

			// if(column == 2 || column == 3)
			// continue;
			boolean isVisible = true;
			if (fixedCellSizeEnabled) {
				// we determine if the cell is visible, and if not we have the
				// ability to take it out of the scenegraph to help improve
				// performance. However, we only do this when there is a
				// fixed cell length specified in the TableView. This is because
				// when we have a fixed cell length it is possible to know with
				// certainty the height of each TableCell - it is the fixed value
				// provided by the developer, and this means that we do not have
				// to concern ourselves with the possibility that the height
				// may be variable and / or dynamic.
				isVisible = isColumnPartiallyOrFullyVisible(tableColumn);

				height = fixedCellSize;
			} else {
				height = Math.max(controlHeight, tableCell.prefHeight(-1));
				height = snapSize(height) - snapSize(verticalPadding);
			}

			/**
			 * FOR FIXED COLUMNS
			 */
			boolean isFixedCell = false;
			double tableCellX = 0;
			if ( /*tableView.getFixedColumns().contains(tableColumn)*/  tableView.getFixedColumnIndex() >= column ) {
				if (tableColumn.isVisible()) {
					double fixedWidth = snapSize(tableCell.prefWidth(-1)) - snapSize(horizontalPadding);
					tableCellX = Math.abs(hbarValue - x + fixedColumnWidth);
					fixedColumnWidth += fixedWidth;
					isVisible = true;
					isFixedCell = true;
				}
			}

			if (isVisible) {
				if (fixedCellSizeEnabled && tableCell.getParent() == null) {
					getChildren().add(tableCell);
				}

				width = snapSize(tableCell.prefWidth(-1)) - snapSize(horizontalPadding);

				// Added for RT-32700, and then updated for RT-34074.
				// We change the alignment from CENTER_LEFT to TOP_LEFT if the
				// height of the row is greater than the default size, and if
				// the alignment is the default alignment.
				// What I would rather do is only change the alignment if the
				// alignment has not been manually changed, but for now this will
				// do.
				final boolean centreContent = h <= 24.0;

				// if the style origin is null then the property has not been
				// set (or it has been reset to its default), which means that
				// we can set it without overwriting someone elses settings.
				final StyleOrigin origin = ((StyleableObjectProperty<?>) tableCell.alignmentProperty()).getStyleOrigin();
				if (!centreContent && origin == null) {
					tableCell.setAlignment(Pos.TOP_LEFT);
				}
				// --- end of RT-32700 fix

				///////////////////////////////////////////
				// further indentation code starts here
				///////////////////////////////////////////
				if (indentationRequired && column == indentationColumnIndex) {
					if (disclosureVisible) {
						double ph = disclosureNode.prefHeight(disclosureWidth);

						if (width > 0 && width < (disclosureWidth + leftMargin)) {
							fadeOut(disclosureNode);
						} else {
							fadeIn(disclosureNode);
							disclosureNode.resize(disclosureWidth, ph);

							disclosureNode.relocate(x + leftMargin,
									centreContent ? (h / 2.0 - ph / 2.0) : (y + tableCell.getPadding().getTop()));
							disclosureNode.toFront();
						}
					}

					// determine starting point of the graphic or cell node, and the
					// remaining width available to them
					ObjectProperty<Node> graphicProperty = graphicProperty();
					Node graphic = graphicProperty == null ? null : graphicProperty.get();

					if (graphic != null) {
						graphicWidth = graphic.prefWidth(-1) + 3;
						double ph = graphic.prefHeight(graphicWidth);

						if (width > 0 && width < disclosureWidth + leftMargin + graphicWidth) {
							fadeOut(graphic);
						} else {
							fadeIn(graphic);

							graphic.relocate(x + leftMargin + disclosureWidth,
									centreContent ? (h / 2.0 - ph / 2.0) : (y + tableCell.getPadding().getTop()));

							graphic.toFront();
						}
					}
				}
				///////////////////////////////////////////
				// further indentation code ends here
				///////////////////////////////////////////

				tableCell.resize(width, height);
//				tableCell.requestLayout();

				System.out.println(x + " " + (x + tableCellX) + " " + fixedColumnWidth);
//				tableCell.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, null, null)));
				if (isFixedCell && x < fixedColumnWidth) {

//					tableCell.getStyleClass().clear();
					
					
					System.out.println("hidden -- " + x);
//					tableCell.setBackground(tableCell.getTableRow().getBackground());
//					tableCell.setStyle(tableCell.getTableRow().getStyle());
					tableCell.toFront();
					
					
				}
				
				tableCell.relocate(x + tableCellX, snappedTopInset());
				tableCell.requestLayout();
			} else {
				if (fixedCellSizeEnabled) {
					getChildren().remove(tableCell);
				}
				width = snapSize(tableCell.prefWidth(-1)) - snapSize(horizontalPadding);
			}

			x += width;
		}
		tableViewSkin.fixedColumnWidth = fixedColumnWidth;
	}

	private VirtualFlow<TableRow<T>> getVirtualFlow() {
		Parent p = getSkinnable();
		while (p != null) {
			if (p instanceof VirtualFlow) {
				return (VirtualFlow<TableRow<T>>) p;
			}
			p = p.getParent();
		}
		return null;
	}

	// There appears to be a memory leak when using the stub toolkit. Therefore,
	// to prevent tests from failing we disable the animations below when the
	// stub toolkit is being used.
	// Filed as RT-29163.
	private static boolean IS_STUB_TOOLKIT = Toolkit.getToolkit().toString().contains("StubToolkit");
	// lets save the CPU and not do animations when on embedded platforms
	private static boolean DO_ANIMATIONS = !IS_STUB_TOOLKIT && !PlatformUtil.isEmbedded();
	private static final Duration FADE_DURATION = Duration.millis(200);

	private void fadeOut(final Node node) {
		if (node.getOpacity() < 1.0)
			return;

		if (!DO_ANIMATIONS) {
			node.setOpacity(0);
			return;
		}

		final FadeTransition fader = new FadeTransition(FADE_DURATION, node);
		fader.setToValue(0.0);
		fader.play();
	}

	private void fadeIn(final Node node) {
		if (node.getOpacity() > 0.0)
			return;

		if (!DO_ANIMATIONS) {
			node.setOpacity(1);
			return;
		}

		final FadeTransition fader = new FadeTransition(FADE_DURATION, node);
		fader.setToValue(1.0);
		fader.play();
	}

	private void recreateCells() {
		if (cellsMap != null) {
			Collection<Reference<TableCell<T, ?>>> cells = cellsMap.values();
			Iterator<Reference<TableCell<T, ?>>> cellsIter = cells.iterator();
			while (cellsIter.hasNext()) {
				Reference<TableCell<T, ?>> cellRef = cellsIter.next();
				TableCell<T, ?> cell = cellRef.get();
				if (cell != null) {
					cell.updateIndex(-1);
					cell.getSkin().dispose();
					cell.setSkin(null);
				}
			}
			cellsMap.clear();
		}

		ObservableList<? extends TableColumnBase/* <T,?> */> columns = getVisibleLeafColumns();

		cellsMap = new WeakHashMap<TableColumnBase, Reference<TableCell<T, ?>>>(columns.size());
		fullRefreshCounter = DEFAULT_FULL_REFRESH_COUNTER;
		getChildren().clear();

		for (TableColumnBase col : columns) {
			if (cellsMap.containsKey(col)) {
				continue;
			}

			// create a TableCell for this column and store it in the cellsMap
			// for future use
			createCell(col);
		}
	}

	protected void updateCells(boolean resetChildren) {
		// To avoid a potential memory leak (when the TableColumns in the
		// TableView are created/inserted/removed/deleted, we have a 'refresh
		// counter' that when we reach 0 will delete all cells in this row
		// and recreate all of them.
		if (resetChildren) {
			if (fullRefreshCounter == 0) {
				recreateCells();
			}
			fullRefreshCounter--;
		}

		// if clear isn't called first, we can run into situations where the
		// cells aren't updated properly.
		final boolean cellsEmpty = cells.isEmpty();
		cells.clear();

		TableRow<T> skinnable = getSkinnable();
		final int skinnableIndex = skinnable.getIndex();
		final List<? extends TableColumnBase/* <T,?> */> visibleLeafColumns = getVisibleLeafColumns();

		for (int i = 0, max = visibleLeafColumns.size(); i < max; i++) {
			TableColumnBase<T, ?> col = visibleLeafColumns.get(i);
			System.out.println(col.getText());
			TableCell<T, ?> cell = null;

			if (cellsMap.containsKey(col)) {
				cell = cellsMap.get(col).get();

				// the reference has been gc'd, remove key entry from map
				if (cell == null) {
					cellsMap.remove(col);
				}
			}

			if (cell == null) {
				// if the cell is null it means we don't have it in cache and
				// need to create it
				cell = createCell(col);
			}

			updateCell(cell, skinnable);
			cell.updateIndex(skinnableIndex);
			cells.add(cell);
		}

		// update children of each row
		if (!fixedCellSizeEnabled && (resetChildren || cellsEmpty)) {
			getChildren().setAll(cells);
		}
	}

	private TableCell<T, ?> createCell(TableColumnBase col) {
		// we must create a TableCell for this table column
		TableCell<T, ?> cell = getCell(col);

		// and store this in our HashMap until needed
		cellsMap.put(col, new WeakReference<>(cell));

		return cell;
	}
}
