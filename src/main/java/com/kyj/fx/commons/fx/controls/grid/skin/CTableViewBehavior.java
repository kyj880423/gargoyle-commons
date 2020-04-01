package com.kyj.fx.commons.fx.controls.grid.skin;

import com.sun.javafx.scene.control.behavior.TableViewBehavior;

import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;

public class CTableViewBehavior<S, T> extends TableViewBehavior<ObservableList<TableCell<S, T>>> {

	public CTableViewBehavior(TableView<ObservableList<TableCell<S, T>>> control) {
		super(control);
	}
}
