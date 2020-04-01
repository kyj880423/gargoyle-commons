/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.cx
 *	작성일   : 2018. 5. 23.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.cx;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * 
 * TableView에 특정색상을 Mark 처리 할 수 있게 도움을 주는 유틸리티 클래스이다. <br/>
 * CSS 기반으로 작성되기 때문에 본 API 사용시 주의가 필요하다. 
 * ex) 색상 초기화시 setStyle("") 값을 주기때문에 기존에 작성된 CSS에 대한 영향을 받을 수 있다 <br/>
 * <br/>
 * @author KYJ
 *
 *
 *
 * 2018.12.26KYJ 
 * clear과 clearAll 항목을 추가하였으며 <br/>
 * 기존에 문자열로 입력된 Color를 enum으로 바꿈. <br/> 
 */
public class MarkTableRowMenu<T> extends Menu {

	/**
	 * Default Color setup.
	 * 
	 * @최초생성일 2018. 5. 24.
	 */
	private Colors[] defaults = Colors.values();

	HighlitRowFactory rowfactory = new HighlitRowFactory();

	final ObservableMap<Integer, Colors> highlightRows = FXCollections.observableHashMap();

	private TableView<T> tv;

	public MarkTableRowMenu(TableView<T> tv) {
		this("Mark", tv);
	}

	public MarkTableRowMenu(String text, TableView<T> tv) {
		this.tv = tv;

		this.setText(text);
		this.tv.setRowFactory(rowfactory);
		getItems().setAll(Stream.of(defaults).map(v -> {
			MenuItem menuItem = new MenuItem(v.name());
			menuItem.setOnAction(ev -> {
				markTableRowMenuOnAction(v);
			});
			return menuItem;
		}).collect(Collectors.toList()));
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 23.
	 * @param value
	 */
	protected void markTableRowMenuOnAction(Colors color) {
		ObservableList<Integer> selectedIndices = tv.getSelectionModel().getSelectedIndices();
		if ("clearAll".equals(color.name())) {
			highlightRows.clear();
		} 
		else {
			for (Integer idx : selectedIndices)
				highlightRows.put(idx, color);
		}

	}

	/**
	 * @author KYJ
	 *
	 */
	class HighlitRowFactory implements Callback<TableView<T>, TableRow<T>> {

		@Override
		public TableRow<T> call(TableView<T> param) {

			final TableRow<T> row = new TableRow<T>() {
				@Override
				protected void updateItem(T person, boolean empty) {
					super.updateItem(person, empty);

					int index = getIndex();
					if (highlightRows.containsKey(index)) {
						Colors color = highlightRows.get(index);
						setStyle("-fx-background-color :  " + color);
					} else {
						setStyle("");
					}
				}
			};

			highlightRows.addListener(new MapChangeListener<Integer, Colors>() {

				@Override
				public void onChanged(Change<? extends Integer, ? extends Colors> change) {

					Colors addedColors = change.getValueAdded();
					if (addedColors != null) {
						String valueAdded = addedColors.name();

						if ("clearAll".equals(valueAdded)) {
							row.setStyle("");
							return;
						}

						if (row.getIndex() == change.getKey() && row.getIndex() >= 0) {

							if (!"clear".equals(valueAdded) && highlightRows.containsKey(change.getKey())) {
								row.setStyle("-fx-background-color :  " + valueAdded);
							} else {
								row.setStyle("");
							}

						}
					}

				}

			});

			return row;
		}

	}

}
