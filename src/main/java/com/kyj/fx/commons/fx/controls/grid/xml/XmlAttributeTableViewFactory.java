/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid.xml
 *	작성일   : 2017. 12. 8.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kyj.fx.commons.utils.FxUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

/**
 * @author KYJ
 *
 */
public class XmlAttributeTableViewFactory {

	private List<String> columns;

	public XmlAttributeTableViewFactory(List<String> columns) {
		this.columns = columns;
	}

	public XmlAttributeTableViewFactory() {
	}

	public BiFunction<TableColumn<Node, String>, String, TableColumn<Node, String>> getColumnHandler() {
		return columnHandler;
	}

	public void setColumnHandler(BiFunction<TableColumn<Node, String>, String, TableColumn<Node, String>> columnHandler) {
		this.columnHandler = columnHandler;
	}

	/**
	 * Custom Column Handler.
	 * 
	 * @최초생성일 2017. 12. 9.
	 */
	private BiFunction<TableColumn<Node, String>, String, TableColumn<Node, String>> columnHandler = (tc, colName) -> {
		return tc;
	};

	public XmlAttributeTableView generate(NodeList list) {
		return generate(list, this.columns);
	}

	public XmlAttributeTableView generate(NodeList list, List<String> columns) {

		XmlAttributeTableView view = new XmlAttributeTableView();
		view.setEditable(true);

		ObservableList<TableColumn<Node, String>> tbColumns = FXCollections.observableArrayList();
		for (String column : columns) {

			TableColumn<Node, String> tc = new TableColumn<Node, String>();
			tc.setEditable(false);
			tc.setCellFactory(TextFieldTableCell.forTableColumn());
			tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Node, String>, ObservableValue<String>>() {

				@Override
				public ObservableValue<String> call(CellDataFeatures<Node, String> param) {

					NamedNodeMap attributes = param.getValue().getAttributes();
					if (attributes != null) {
						Node namedItem = attributes.getNamedItem(column);
						if (namedItem != null) {
							SimpleStringProperty simpleStringProperty = new SimpleStringProperty(namedItem.getNodeValue());
							simpleStringProperty.addListener(new ChangeListener<String>() {

								@Override
								public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
									if (newValue != null) {
										namedItem.setNodeValue(newValue);
									}
								}
							});
							return simpleStringProperty;
						}
					}

					return new SimpleStringProperty();

				}
			});

			tc.setText(column);

			if (columnHandler != null)
				tc = columnHandler.apply(tc, column);
			tbColumns.add(tc);
		}

		view.getColumns().setAll(tbColumns);

		// view.getItems().addAll(list)
		int itemCount = list.getLength();

		List<Node> arrayList = new ArrayList<Node>();
		for (int i = 0; i < itemCount; i++) {
			Node item = list.item(i);
			arrayList.add(item);
		}

		view.getItems().addAll(arrayList);

		view.getSelectionModel().setCellSelectionEnabled(true);
		view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		FxUtil.installClipboardKeyEvent(view);
		FxUtil.installFindKeyEvent(null, view);

		return view;
	}

}
