package com.kyj.fx.commons.fx.controls.grid.skin;

import com.sun.javafx.scene.control.skin.NestedTableColumnHeader;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class CTableHeaderRow extends TableHeaderRow {

	public CTableHeaderRow(TableViewSkinBase skin) {
		super(skin);
	}

	@Override
	protected CNestedTableColumnHeader createRootHeader() {
		return new CNestedTableColumnHeader(getTableSkin(), null);
	}

	@Override
	protected void updateScrollX() {
		super.updateScrollX();
		requestLayout();
		CNestedTableColumnHeader rootHeader = (CNestedTableColumnHeader) getRootHeader();
		rootHeader.layoutFixedColumns();

	}

}
