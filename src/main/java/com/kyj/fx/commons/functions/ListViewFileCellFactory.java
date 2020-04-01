package com.kyj.fx.commons.functions;

import java.io.File;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ListViewFileCellFactory implements Callback<ListView<File>, ListCell<File>> {

	@Override
	public ListCell<File> call(ListView<File> param) {
		return new ListCell<File>() {

			@Override
			protected void updateItem(File item, boolean empty) {
				super.updateItem(item, empty);

				if (!empty) {
					setText(String.format("%s       ( %,d KB )", item.getName(), (item.length() / 1024)));
				} else {
					setText("");
				}
			}

		};
	}

}
