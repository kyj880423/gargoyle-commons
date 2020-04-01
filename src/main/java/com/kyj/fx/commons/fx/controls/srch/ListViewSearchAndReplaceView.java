/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.popup
 *	작성일   : 2017. 06. 05
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.srch;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.models.ReplaceResultVO;
import com.kyj.fx.commons.models.SearchResultVO;
import com.kyj.fx.commons.utils.ValueUtil;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * 리스트뷰안의 텍스트를 탐색하는경우 사용
 *
 * UI는 NotePad +프로그램을 베이스로 잡고 작성.
 *
 * @author KYJ
 *
 */
public class ListViewSearchAndReplaceView<T> extends BorderPane {
	private static Logger LOGGER = LoggerFactory.getLogger(ListViewSearchAndReplaceView.class);

	/**
	 * 본문 컨텐츠
	 *
	 * @최초생성일 2015. 12. 14.
	 */
	// private StringProperty contentProperty;

	/**
	 * 검색 시작 인덱스
	 *
	 * @최초생성일 2015. 12. 14.
	 */
	private IntegerProperty slidingStartRowIndexProperty;

	/**
	 * 찾기탭에서 찾을 내용 텍스트
	 *
	 * @최초생성일 2015. 12. 14.
	 */
	@FXML
	private TextField txtFindTextContent;

	@FXML
	private TabPane tabPane;

	/**
	 * 찾은 내용에 대한 메타정보
	 *
	 * @최초생성일 2015. 12. 14.
	 */
	private ObjectProperty<SearchResultVO> searchResultVOProperty;

	private ObjectProperty<ReplaceResultVO> replaceResultVOProperty;

	private ObjectProperty<SearchResultVO> selectionMoveProperty;

	private ListView<T> parent;

	/**
	 * 범위선택 라이오 박스를 선택했는지 유무
	 */
	private BooleanProperty isSelectScopeProperty = new SimpleBooleanProperty();

	@FXML
	private RadioButton rdoSelectScope, rdoGlobalScope;

	/**
	 * description text field
	 *
	 * @최초생성일 2015. 12. 28.
	 */
	@FXML
	private Label txtDesc;

	/**
	 * 바꾸기에서 찾기 텍스트
	 *
	 * @최초생성일 2015. 12. 28.
	 */
	@FXML
	private TextField /* 바꾸기에서 찾기 텍스트 */ txtFind, /* 바꾸기 텍스트 */txtReplace;

	@FXML
	private Accordion accFindAll;

	@FXML
	private ListView<SearchResultVO> lvFindAll;
	@FXML
	private RadioButton rboUp, rboDown;
	@FXML
	private Tab tabFind, tabReplace;
	/**
	 * 모두찾기에서 찾은 데이터를 화면에 표시할 텍스트 포멧을 정의한다.
	 * 
	 * @최초생성일 2017. 2. 2.
	 */
	public static final String FIND_ALL_TEXT_FORMAT = " Start Index %d   End Index %d   Keyword : %s";
	/****************************************************************/

	/****************************************************************/
	/*
	 * 사용자가 필수적으로 처리해야할 이벤트
	 */
	/**
	 * 검색결과를 발견했을때 처리할 내용을 등록한다.
	 *
	 * @param consumer
	 */
	public void setOnSearchResultListener(Consumer<SearchResultVO> consumer) {
		searchResultVOProperty.addListener((oba, oldval, newval) -> {
			consumer.accept(newval);
		});
	}

	/**
	 * 검색결과를 발견했을때 처리할 내용을 등록한다.
	 *
	 * @param consumer
	 */
	public void setOnReplaceResultListener(Consumer<ReplaceResultVO> consumer) {
		replaceResultVOProperty.addListener((oba, oldval, newval) -> {
			consumer.accept(newval);
		});
	}

	/**
	 * 선택된 행으로 이동처리할때의 내용을 기술하는 로직을 등록
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 2. 3.
	 * @param consumer
	 */
	public void setOnSelectionMoveListener(Consumer<SearchResultVO> consumer) {
		selectionMoveProperty.addListener((oba, oldval, newval) -> {
			if (newval != null)
				consumer.accept(newval);
		});
	}

	/****************************************************************/
	/****************************************************************/

	private StringConverter<T> converter;

	/**
	 * 생성자
	 *
	 * @param parent
	 * @param content
	 */
	public ListViewSearchAndReplaceView(ListView<T> parent, StringConverter<T> converter) {
		this.converter = converter;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ListViewSearchAndReplaceView.class.getResource("TextSearchAndReplaceView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
			this.searchResultVOProperty = new SimpleObjectProperty<>();
			this.replaceResultVOProperty = new SimpleObjectProperty<>();
			this.selectionMoveProperty = new SimpleObjectProperty<>();
			slidingStartRowIndexProperty = new SimpleIntegerProperty();

			this.parent = parent;
		} catch (IOException e) {
			LOGGER.error(ValueUtil.toString(e));
		}

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				T selectedItem = parent.getSelectionModel().getSelectedItem();
				String selectedText = "";
				if (selectedItem != null) {
					Callback<ListView<T>, ListCell<T>> cellFactory = parent.getCellFactory();

					if (cellFactory == null) {
						selectedText = selectedItem.toString();
					} else {
						ListCell<T> call = cellFactory.call(parent);
						if (converter != null) {
							selectedText = converter.toString(selectedItem);
						} else {

							if (call instanceof TextFieldListCell) {
								StringConverter<T> converter = ((TextFieldListCell<T>) call).getConverter();
								selectedText = converter.toString(selectedItem);
							} else {
								selectedText = selectedItem.toString();
							}

						}
					}
				}

				txtFind.setText(selectedText);
				txtFindTextContent.setText(selectedText);
				txtFindTextContent.requestFocus();
			}
		});

		// Listview finder에서는 disable. 2017-08-07
		rdoGlobalScope.setDisable(true);
		rdoSelectScope.setDisable(true);
		tabReplace.setDisable(true);

	}

	/**
	 * 탐색
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 6. 7.
	 * @return
	 */
	private int find() {
		int pos = rboUp.isSelected() ? -1 : 1;
		return find(txtFindTextContent.getText(), slidingStartRowIndexProperty.get() + pos, pos, null);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017.06.05
	 * @param findWord
	 *            찾을 문자열
	 * @param startRowIdx
	 *            찾을 row 시작번호
	 * @param pos
	 *            -1 upper 1 lower
	 * @param function
	 *            변환function
	 */
	private int find(String findWord, int startRowIdx, int pos, Function<SearchResultVO, SearchResultVO> function) {

		ObservableList<T> items = parent.getItems();
		int length = items.size();
		int i = startRowIdx;
		if (i == -1)
			i = 0;

		while (true) {
			T item = items.get(i);

			int indexOf = converter.toString(item).indexOf(findWord);
			if (indexOf >= 0) {

				int endIdx = indexOf + findWord.length();

				SearchResultVO value = new SearchResultVO();
				value.setSearchText(findWord);
				value.setStartIndex(indexOf);
				value.setEndIndex(endIdx);

				if (function != null)
					value = function.apply(value);

				searchResultVOProperty.set(value);
				slidingStartRowIndexProperty.set(i);

				if (onFound.get() != null)
					onFound.get().accept(new OnFound(i, value));
				return i;
			}

			i += pos;

			if (i < 0)
				break;
			if (length <= i)
				break;

		}
		return -1;
	}

	@FXML
	public void initialize() {
		rdoSelectScope.selectedProperty().addListener((oba, oldval, newval) -> {
			if (newval)
				isSelectScopeProperty.set(true);
		});

		rdoGlobalScope.selectedProperty().addListener((oba, oldval, newval) -> {
			if (newval)
				isSelectScopeProperty.set(false);
		});

		lvFindAll.setCellFactory(callback -> new ListCell<SearchResultVO>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see javafx.scene.control.Cell#updateItem(java.lang.Object,
			 * boolean)
			 */
			@Override
			protected void updateItem(SearchResultVO item, boolean empty) {
				super.updateItem(item, empty);

				if (empty) {
					setText("");
				} else {

					if (item == null) {
						setText("");
						return;
					}
					// Format Text :: Start Index %d End Index %d Keyword : %s
					setText(String.format(FIND_ALL_TEXT_FORMAT, item.getStartIndex(), item.getEndIndex(), item.getSearchText()));
					// setText(item.get);
				}
			}

		});

		lvFindAll.setOnMouseClicked(ev -> {

			if (ev.getClickCount() == 2 && ev.getButton() == MouseButton.PRIMARY) {
				SearchResultVO selectedItem = lvFindAll.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					this.selectionMoveProperty.set(selectedItem);
				}
			}
		});
	}

	/**
	 * 찾기탭에서 찾기
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 12. 28.
	 * @param e
	 */
	@FXML
	public void btnFindNextOnMouseClick(MouseEvent e) {
		find();
	}

	@FXML
	public void btnFindAllOnMouseClick() {

	}

	@FXML
	public void btnCloseOnMouseClick() {
		Scene scene = getScene();

		if (scene.getWindow() != null) {
			Stage window = (Stage) scene.getWindow();
			window.close();
		}
	}

	@FXML
	public void txtFindOnKeyClick(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			find();
		}
	}

	@FXML
	public void btnMatchCountOnMouseClick() {
		showMatchCount();
	}

	@Deprecated
	@FXML
	public void btnReplaceOnFindOnMouseClick() {
		// Nothing.
	}

	@Deprecated
	@FXML
	public void btnReplaceOnMouseClick() {
		// Nothing.
	}

	@Deprecated
	@FXML
	public void btnReplaceAllOnMouseClick(KeyEvent e) {
		// Nothing.
	}

	@FXML
	public void txtFindTextContentOnKeyPress(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			find();
		}
	}

	/**
	 * 일치하는 검색 정보를 화면에 표시
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 2. 3.
	 */
	private void showMatchCount() {
		final String findText = this.txtFindTextContent.getText();
		showMatchCount(findText);
	}

	/**
	 * 일치하는 검색 정보를 화면에 표시
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 2. 3.
	 * @param findText
	 * @param content
	 */
	private void showMatchCount(String findWord) {
		if (ValueUtil.isEmpty(findWord))
			return;

		ObservableList<T> items = parent.getItems();
		int length = items.size();

		long count = Stream.iterate(0, a -> a + 1).limit(length).parallel().filter(i -> {
			T item = items.get(i);
			return converter.toString(item).indexOf(findWord) >= 0;
		}).count();

		txtDesc.setText(String.format("Found Word \" %s \" : count : %d ", findWord, count));
	}

	private Stage owner;

	public void initOwner(Stage owner) {
		this.owner = owner;
	}

	/**
	 *
	 *
	 * @Date 2015. 10. 21.
	 * @throws IOException
	 * @User KYJ
	 */
	public void show() {
		Stage stage = new Stage();
		stage.initOwner(this.owner);

		LOGGER.debug("SHOW SimpleSQLResult View.....");
		LOGGER.debug("call executeSQL function....");

		Scene scene = new Scene(this/* , 1100, 700 */);
		stage.setScene(scene);

		stage.setResizable(false);
		// stage.setAlwaysOnTop(true);
		stage.initModality(Modality.NONE);
		stage.initOwner(parent.getScene().getWindow());
		stage.setTitle(tabPane.getSelectionModel().getSelectedItem().getText());
		tabPane.getSelectionModel().selectedItemProperty().addListener((oba, oldval, newval) -> {
			String text = newval.getText();
			stage.setTitle(text);
		});

		// esc키를 누르면 팝업 close
		stage.addEventHandler(KeyEvent.ANY, event -> {

			KeyCode code = event.getCode();
			if (KeyCode.ESCAPE == code) {
				event.consume();
				stage.close();
			}
		});
		stage.centerOnScreen();
		stage.showAndWait();
	}

	/* [시작] prop 속성 */

	public final IntegerProperty slidingStartIndexPropertyProperty() {
		return this.slidingStartRowIndexProperty;
	}

	public final int getSlidingStartIndexProperty() {
		return this.slidingStartIndexPropertyProperty().get();
	}

	public final void setSlidingStartIndexProperty(final int slidingStartIndexProperty) {
		this.slidingStartIndexPropertyProperty().set(slidingStartIndexProperty);
	}

	public final ObjectProperty<SearchResultVO> searchResultVOPropertyProperty() {

		// this.searchResultVOProperty;
		// return new ReadOnlyObjectWrapper<>(searchResultVOProperty.get());
		return this.searchResultVOProperty;
	}

	public final SearchResultVO getSearchResultVOProperty() {
		return this.searchResultVOPropertyProperty().get();
	}

	public final void setSearchResultVOProperty(final SearchResultVO searchResultVOProperty) {
		this.searchResultVOPropertyProperty().set(searchResultVOProperty);
	}

	public final BooleanProperty isSelectScopePropertyProperty() {
		return this.isSelectScopeProperty;
	}

	public final boolean isIsSelectScopeProperty() {
		return this.isSelectScopePropertyProperty().get();
	}

	public final void setIsSelectScopeProperty(final boolean isSelectScopeProperty) {
		this.isSelectScopePropertyProperty().set(isSelectScopeProperty);
	}

	private ObjectProperty<Consumer<OnFound>> onFound = new SimpleObjectProperty<>();

	public void setOnFound(Consumer<OnFound> onFound) {
		this.onFound.set(onFound);
	}

	/* [종료] prop 속성 */

	/**
	 * 리스튜뷰의 아이템을 찾을때 관련된 메타데이터
	 * 
	 * @author KYJ
	 *
	 */
	public static class OnFound {

		/**
		 * 찾아낸 item row 인덱스
		 * 
		 * @최초생성일 2017. 6. 7.
		 */
		private int rowIndex;
		/**
		 * 찾아낸 문자열에 대한 메타정보
		 * 
		 * @최초생성일 2017. 6. 7.
		 */
		private SearchResultVO resultVo;

		OnFound(int rowIndex, SearchResultVO resultVo) {
			this.rowIndex = rowIndex;
			this.resultVo = resultVo;
		}

		public int getRowIndex() {
			return rowIndex;
		}

		void setRowIndex(int rowIndex) {
			this.rowIndex = rowIndex;
		}

		public SearchResultVO getResultVo() {
			return resultVo;
		}

		void setResultVo(SearchResultVO resultVo) {
			this.resultVo = resultVo;
		}

	}
}
