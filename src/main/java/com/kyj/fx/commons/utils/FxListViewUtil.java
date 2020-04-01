/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2018. 4. 9.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.util.Optional;

import com.kyj.fx.commons.memory.StageStore;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;

/**
 * @author KYJ
 *
 */
public class FxListViewUtil {

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 9.
	 * @param listview
	 * @param converter
	 */
	public static <T> void installFindKeyEvent(ListView<T> listview, StringConverter<T> converter) {
		ListViewFindHelper.install(StageStore.getPrimaryStage(), listview, converter);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 9.
	 * @param listview
	 * @param converter
	 */
	public static <T> void installFindKeyEvent(Stage owner, ListView<T> listview, StringConverter<T> converter) {
		ListViewFindHelper.install(owner, listview, converter);
	}

	/********************************
	 * 작성일 : 2016. 9. 3. 작성자 : KYJ
	 *
	 * TableView 키이벤트를 등록
	 *
	 * @param tb
	 ********************************/
	public static <T> void installClipboardKeyEvent(ListView<T> tv, StringConverter<T> converter) {

		tv.addEventHandler(KeyEvent.KEY_RELEASED, ev -> {

			// Copy
			if (KeyCode.C == ev.getCode() && ev.isControlDown() && !ev.isAltDown() && !ev.isShiftDown()) {

				if (ev.isConsumed())
					return;
				ObservableList<T> selectedItems = tv.getSelectionModel().getSelectedItems();
				if (selectedItems != null) {
					Optional<String> reduce = selectedItems.stream().map(v -> {
						return converter.toString(v);
					}).reduce((str1, str2) -> {
						return str1.concat("\n").concat(str2);
					});

					reduce.ifPresent(v -> {
						FxClipboardUtil.putString(v);
					});
				}
				ev.consume();
			}

		});
	}

	/**
	 * CTRL + L 버튼을 누를때 해당 라인을 이동하는 이벤트 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 17.
	 * @param tv
	 */
	public static <T> void installMoveLineEvent(ListView<T> tv) {
		tv.addEventHandler(KeyEvent.KEY_RELEASED, ev -> {

			if (KeyCode.L == ev.getCode() && ev.isControlDown() && !ev.isAltDown() && !ev.isShiftDown()) {

				if (ev.isConsumed())
					return;
				ev.consume();
				Optional<Pair<String, String>> showInputDialog = DialogUtil.showInputDialog(tv, "Go to Line", "Input Line Number",
						str -> ValueUtil.isNumber(str));

				showInputDialog.ifPresent(v -> {
					String value = v.getValue();
					try {
						int intValue = Integer.parseInt(value);
						tv.scrollTo(intValue);
					} catch (Exception e) {
					}
				});
			}

		});
	}

}
