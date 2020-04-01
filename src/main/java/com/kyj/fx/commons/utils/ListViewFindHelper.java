/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2017. 6. 5.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import com.kyj.fx.commons.fx.controls.srch.ListViewSearchAndReplaceView;

import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * @author KYJ
 *
 */
public class ListViewFindHelper extends ListViewHelper {

	public static <T> void install(ListView<T> listview, StringConverter<T> converter) {
		installKeyHanlder(null, listview, converter);
	}
	
	public static <T> void install(Stage owner, ListView<T> listview, StringConverter<T> converter) {
		installKeyHanlder(owner, listview, converter);
	}

	private static <T> void installKeyHanlder(Stage owner, ListView<T> listview, StringConverter<T> converter) {
		listview.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {

			if (ev.getCode() == KeyCode.F && ev.isControlDown()) {
				ListViewSearchAndReplaceView<T> finder = new ListViewSearchAndReplaceView<>(listview, converter);

				// 문자열을 찾은다음 할 행동.
				finder.setOnFound(t -> {
					int rowIndex = t.getRowIndex();
					listview.getSelectionModel().select(rowIndex);
					listview.scrollTo(rowIndex);
				});

				finder.show();
			}
		});
	}
}
