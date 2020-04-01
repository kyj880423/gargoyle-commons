/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.cx
 *	작성일   : 2018. 12. 26.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.cx;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * 
 * ListView에 특정색상을 Mark 처리 할 수 있게 도움을 주는 유틸리티 클래스이다. <br/>
 * CSS 기반으로 작성되기 때문에 본 API 사용시 주의가 필요하다. 
 * ex) 색상 초기화시 setStyle("") 값을 주기때문에 기존에 작성된 CSS에 대한 영향을 받을 수 있다 <br/>
 * <br/>
 * @author KYJ
 *
 */
public class MarkListRowMenu<T> extends Menu {

	/**
	 * Default Color setup.
	 * 
	 * @최초생성일 2018. 5. 24.
	 */
	private Colors[] defaults = Colors.values();

	HighlitRowFactory rowfactory = new HighlitRowFactory();

	final ObservableMap<Integer, Colors> highlightRows = FXCollections.observableHashMap();

	private ListView<T> tv;
	private StringConverter<T> stringConverter = new StringConverter<T>() {

		@Override
		public String toString(T object) {
			return object.toString();
		}

		@Override
		public T fromString(String string) {
			return null;
		}
	};

	public MarkListRowMenu(ListView<T> tv) {
		this("Mark", tv);
	}

	public MarkListRowMenu(String text, ListView<T> tv) {
		this.tv = tv;
		this.setText(text);
		this.tv.setCellFactory(rowfactory);
		// this.tv.setRowFactory(rowfactory);
		getItems().setAll(Stream.of(defaults).map(v -> {
			MenuItem menuItem = new MenuItem(v.name());
			menuItem.setOnAction(ev -> {
				markTableRowMenuOnAction(v);
			});
			return menuItem;
		}).collect(Collectors.toList()));
	}

	/**
	 * @return the stringConverter
	 */
	public StringConverter<T> getStringConverter() {
		return stringConverter;
	}

	/**
	 * @param stringConverter
	 *            the stringConverter to set
	 */
	public void setStringConverter(StringConverter<T> stringConverter) {
		this.stringConverter = stringConverter;
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
	class HighlitRowFactory implements Callback<ListView<T>, ListCell<T>> {

		@Override
		public ListCell<T> call(ListView<T> param) {

			final ListCell<T> row = new ListCell<T>() {
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

					if (stringConverter != null) {
						String string = stringConverter.toString(person);
						setText(string);
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

//					Colors removedColors = change.getValueRemoved();
//					if (removedColors != null) {
//						if (row.getIndex() == change.getKey() && row.getIndex() >= 0) {
//							if (highlightRows.containsKey(change.getKey())) {
//								row.setStyle("");
//							}
//						}
//					}
				}

			});

			return row;
		}

	}

}
