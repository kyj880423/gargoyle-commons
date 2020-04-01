/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2019. 1. 11.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.utils;

import java.util.function.BiConsumer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class FxTreeViewUtil {

	/**
	 * 
	 * install keyClick event. <br/>
	 * 
	 * if user key click , this function find the matched preffix text. and then
	 * is selected the row <br/>
	 * 
	 * find value from toString method. <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 11.
	 * @param tv
	 * @param converter
	 */
	public static <T> void installKeywordMoveEvent(TreeView<T> tv) {
		installKeywordMoveEvent(tv, null);
	}

	/**
	 * install keyClick event. <br/>
	 * 
	 * if user key click , this function find the matched preffix text. and then
	 * is selected the row <br/>
	 * 
	 * find value by converter method. <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 11.
	 * @param tv
	 * @param converter
	 */
	public static <T> void installKeywordMoveEvent(TreeView<T> tv, StringConverter<T> converter) {
		tv.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {

			// 특수키가 눌려진경우는 Skip.
			if (!ev.getCode().isLetterKey()) {
				return;
			}
			
			if(ev.isControlDown() || ev.isShiftDown() || ev.isAltDown())
				return;
			
			if (ev.isConsumed())
				return;
			ev.consume();

			String preffix = ev.getText().toLowerCase();

			MultipleSelectionModel<TreeItem<T>> selectionModel = tv.getSelectionModel();
			
			//tree item index.
			
			TreeItem<T> selectedItem = selectionModel.getSelectedItem();
			if (selectedItem == null)
				return;
			
			
			TreeItem<T> parent = selectionModel.getSelectedItem().getParent();
			ObservableList<TreeItem<T>> items = FXCollections.emptyObservableList();
			if (parent != null) {
				items = parent.getChildren();
			}

			int selectedIndex = items.indexOf(selectedItem);
			
//			tv.setOnScrollTo(new EventHandler<ScrollToEvent<Integer>>() {
//				
//				@Override
//				public void handle(ScrollToEvent<Integer> event) {
//					Integer scrollTarget = event.getScrollTarget();
//					
//				}
//			});
			int size = items.size();
			BiConsumer<TreeItem<T>, Integer> findFunction = (ti, idx) -> {
				selectionModel.clearSelection();
				selectionModel.select(ti);
				
				// move to selected index.
				tv.scrollTo(selectionModel.getSelectedIndex());
			};

			if (find(items, preffix, converter, selectedIndex + 1, size, findFunction)) {
				return;
			}
			find(items, preffix, converter, 0,  selectedIndex , findFunction);

		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 11.
	 * @param items
	 * @param preffix
	 * @param converter
	 * @param startIdx
	 * @param endIdx
	 * @return
	 */
	private static <T> boolean find(ObservableList<TreeItem<T>> items, String preffix, StringConverter<T> converter, int startIdx,
			int endIdx, BiConsumer<TreeItem<T>, Integer> findFunction) {
		boolean isFound = false;

		if (startIdx >= endIdx)
			return false;

		for (int i = startIdx; i < endIdx; i++) {

			TreeItem<T> tmp = items.get(i);
			T data = tmp.getValue();

			String string = null;

			if (converter == null)
				string = data.toString().toLowerCase();
			else
				string = converter.toString(data).toLowerCase();

			if (string == null)
				continue;

			if (string.startsWith(preffix)) {
				findFunction.accept(tmp, i);
				isFound = true;
				break;
			}
		}

		return isFound;
	}

}
