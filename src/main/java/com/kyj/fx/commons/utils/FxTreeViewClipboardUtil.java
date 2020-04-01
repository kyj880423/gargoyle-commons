/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2016. 11. 16.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.lang.reflect.Method;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;

/***************************
 *
 * @author KYJ
 *
 ***************************/
class FxTreeViewClipboardUtil {

	private static StringConverter getConverter(TreeCell cell) {
		StringConverter converter = null;
		try {
			Method m = cell.getClass().getMethod("converterProperty");
			if (m != null) {
				Object object = m.invoke(cell);
				if (object != null && object instanceof ObjectProperty) {
					ObjectProperty<StringConverter> convert = (ObjectProperty<StringConverter>) object;
					converter = convert.get();
				}
			}
		} catch (Exception e) {
			// Nothing...
		}
		return converter;
	}

	/**
	 * ctrl + c copy 처리 <br/>
	 * 17.12.06 <br/>
	 * BUG FIX <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 16.
	 * @param table
	 */
	public static void installCopyPasteHandler(TreeView<?> table) {
		table.addEventHandler(KeyEvent.KEY_PRESSED, e -> {

			if (e.isConsumed())
				return;
			if (e.isControlDown() && e.getCode() == KeyCode.C) {

				ObservableList<?> selectedItems = table.getSelectionModel().getSelectedItems();
				StringBuilder sb = new StringBuilder();

				Callback cellFactory = table.getCellFactory();
				for (Object v : selectedItems) {
					if (v instanceof TreeItem) {
						TreeItem ti = (TreeItem) v;
						Object value = ti.getValue();

						Object cellObj = cellFactory.call(table);
						if (cellObj == null) {
							sb.append("");
						} else if (cellObj instanceof TreeCell) {
							TreeCell treeCell = (TreeCell) cellObj;
							String text = treeCell.getText();

							StringConverter converter = getConverter(treeCell);
							if (converter != null) {
								text = converter.toString(value);
							}

							sb.append(text).append("\n");
						} else {
							sb.append(cellObj.toString()).append("\n");
						}
						//

					} else {
						// TODO :: 첫번째 컬럼(행 선택 기능)도 빈값으로 복사됨..
						// 행변경시
						if (v != null) {
							sb.append(v.toString());
							sb.append("\n");
						}
					}

				}
				FxClipboardUtil.putString(sb.toString());
				e.consume();
			}

		});
	}
}
