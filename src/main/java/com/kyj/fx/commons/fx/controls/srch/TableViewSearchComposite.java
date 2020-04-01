/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.popup
 *	작성일   : 2017. 10. 29.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.srch;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.models.SearchResultVO;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.fxloader.FXMLController;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * 텍스트기반 검색
 *
 * NotePad +프로그램을 베이스로 잡고 작성.
 *
 * @author KYJ
 *
 */
@FXMLController(value = "TextSearchView.fxml", css = "TextSearchView.css", isSelfController = true)
public class TableViewSearchComposite<T> extends BorderPane {
	private static Logger LOGGER = LoggerFactory.getLogger(TableViewSearchComposite.class);

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
	 * @최초생성일 2017. 10. 29.
	 */
	private ObjectProperty<SearchResultVO> searchResultVOProperty;

	private Window owner;

	/**
	 * 범위선택 라이오 박스를 선택했는지 유무
	 */
	private BooleanProperty isSelectScopeProperty = new SimpleBooleanProperty();

	@FXML
	private RadioButton rdoSelectScope;

	@FXML
	private RadioButton rdoGlobalScope;

	/**
	 * description text field
	 *
	 * @최초생성일 2017. 10. 29.
	 */
	@FXML
	private Label txtDesc;

	/**
	 * 바꾸기에서 찾기 텍스트
	 *
	 * @최초생성일 2017. 10. 29.
	 */
	@FXML
	private TextField txtFind;
	/**
	 * 바꾸기 텍스트
	 *
	 * @최초생성일 2017. 10. 29.
	 */
	@FXML
	private TextField txtReplace;

	/****************************************************************/
	@FXML
	public Button btnMatchedCount;

	private TableView<T> tbContent;

	@FXML
	private RadioButton rbDirUp, rbDirDown;

	private BiFunction<TableColumn<?, ?>, Object, Object> customConverter;

	public BiFunction<TableColumn<?, ?>, Object, Object> getCustomConverter() {
		return customConverter;
	}

	public void setCustomConverter(BiFunction<TableColumn<?, ?>, Object, Object> customConverter) {
		this.customConverter = customConverter;
	}

	/**
	 * 생성자
	 *
	 * @param parent
	 * @param content
	 */
	public TableViewSearchComposite(Window owner, TableView<T> tb) {

		FxUtil.loadRoot(TableViewSearchComposite.class, this, err -> {
			LOGGER.error(ValueUtil.toString(err));
		});

		this.searchResultVOProperty = new SimpleObjectProperty<>();
		slidingStartRowIndexProperty = new SimpleIntegerProperty();

		this.tbContent = tb;
		this.owner = owner;
	}

	/****************************************************************/
	// 초기화 함수
	/****************************************************************/
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
		// btnMatchedCount.setDisable(true);
		Platform.runLater(() -> txtFindTextContent.requestFocus());
	}

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

	/****************************************************************/

	/**
	 * 검색 처리를 진행하며 검색이 완료되면 searchResultVOProperty에 값을 set함과 동시에 부가정보 (
	 * slidingStartIndexProperty, slidingEndIndexProperty )를 갱신함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 12. 14.
	 * @param content
	 */
	private int find() {
		return find(txtFindTextContent.getText());
	}

	/**
	 * 검색 처리를 진행하며 검색이 완료되면 searchResultVOProperty에 값을 set함과 동시에 부가정보 (
	 * slidingStartIndexProperty, slidingEndIndexProperty )를 갱신함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 29.
	 * @param content
	 * @param function
	 *            부가적으로 처리할 내용을 기술.
	 */
	private int find(String findWord) {
		return find(txtFindTextContent.getText(), v -> v);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 29.
	 * @param findWord
	 *            찾을 문자열
	 * @param content
	 * @param function
	 */
	private int find(String findWord, Function<SearchResultVO, SearchResultVO> function) {
		if (ValueUtil.isEmpty(findWord))
			return -1;

		ObservableList<T> items = this.tbContent.getItems();
		ObservableList<TableColumn<T, ?>> columns = this.tbContent.getColumns();

		int size = items.size();
		if (rbDirDown.isSelected()) {
			for (int i = slidingStartRowIndexProperty.get(), max = size; i < max; i++) {
				boolean find = find(findWord, function, columns, size, i, t -> true);
				if (find)
					break;
			}
		}

		if (rbDirUp.isSelected()) {
			for (int i = slidingStartRowIndexProperty.get(); i >= 0; i--) {
				boolean find = find(findWord, function, columns, size, i, t -> true);
				if (find)
					break;
			}
		}
		return slidingStartRowIndexProperty.get();
	}

	private boolean find(String findWord, Function<SearchResultVO, SearchResultVO> function, ObservableList<TableColumn<T, ?>> columns,
			int size, int index, Predicate<SearchResultVO> isBreak) {
		for (TableColumn<T, ?> c : columns) {
			String content = FxUtil.getDisplayText(c, index, this.customConverter).toString();

			int startIdx = content.indexOf(findWord);

			if (startIdx >= 0) {
				int endIdx = startIdx + findWord.length();

				SearchResultVO value = new SearchResultVO();
				value.setSearchText(findWord);
				value.setStartIndex(startIdx);
				value.setEndIndex(endIdx);

				if (function != null)
					value = function.apply(value);

				searchResultVOProperty.set(value);

				int nextIdx = index;
				if (rbDirDown.isSelected()) {
					if (size - 1 > nextIdx)
						nextIdx++;
				} else {
					if (nextIdx > 0)
						nextIdx--;
				}

				slidingStartRowIndexProperty.set(nextIdx);

				this.tbContent.scrollTo(index);
				TableViewSelectionModel<T> selectionModel = this.tbContent.getSelectionModel();
				if (selectionModel.isCellSelectionEnabled()) {
					selectionModel.select(index, c);
				} else {
					selectionModel.select(index);
				}

				if (isBreak.test(value)) {
					return true;
				}
			}
			// else {
			// slidingStartRowIndexProperty.set(0);
			// }
		}
		return false;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 29.
	 */
	public void findAll() {
		findAll(txtFindTextContent.getText(), null);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 29.
	 * @param function
	 */
	public void findAll(Function<SearchResultVO, SearchResultVO> function) {
		findAll(txtFindTextContent.getText(), function);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 29.
	 * @param findWord
	 * @param function
	 */
	private void findAll(String findWord, Function<SearchResultVO, SearchResultVO> function) {
		if (ValueUtil.isEmpty(findWord))
			return;

		ObservableList<T> items = this.tbContent.getItems();
		ObservableList<TableColumn<T, ?>> columns = this.tbContent.getColumns();

		for (int i = 0, max = items.size(); i < max; i++) {
			find(findWord, function, columns, max, i, v -> false);
		}

	}

	/**
	 * 찾기탭에서 찾기
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 29.
	 * @param e
	 */
	@FXML
	public void btnFindNextOnMouseClick(MouseEvent e) {
		find();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 29.
	 */
	@FXML
	public void btnReplaceOnFindOnMouseClick() {
		// Nothing.
	}

	@FXML
	public void txtFindTextContentOnKeyPress(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			find();
		}
	}

	/**
	 * 닫기
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 29.
	 * @param e
	 */
	@FXML
	public void btnCloseOnMouseClick(MouseEvent e) {
		Scene scene = getScene();
		Stage window = (Stage) scene.getWindow();
		window.close();
	}

	/**
	 * 모두찾기
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 29.
	 */
	@FXML
	public void btnFindAllOnMouseClick() {
		// String content = contentProperty.get();
		// findAll(content, null);
	}

	/**
	 * 일치하는수
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 29.
	 */
	@FXML
	public void btnMatchCountOnMouseClick() {

		final String findText = this.txtFindTextContent.getText();
		if (ValueUtil.isEmpty(findText))
			return;

		AtomicInteger count = new AtomicInteger(0);
		findAll(v -> {
			count.getAndAdd(1);
			return v;
		});
		txtDesc.setText(String.format("Found Word \" %s \" : count : %d ", findText, count.get()));

	}

	/**
	 * 바꾸기탭에서 찾기 마우스 클릭 이벤트
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 29.
	 * @param e
	 */
	@FXML
	public void txtFindOnKeyClick(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			btnReplaceOnFindOnMouseClick();
		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 29.
	 */
	public void show() {
		Stage stage = new Stage();
		LOGGER.debug("SHOW SimpleSQLResult View.....");
		LOGGER.debug("call executeSQL function....");

		Scene scene = new Scene(this/* , 1100, 700 */);
		stage.setScene(scene);

		stage.setResizable(false);
		// stage.setAlwaysOnTop(true);
		stage.initModality(Modality.NONE);
		stage.initOwner(owner);
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

	/* [종료] prop 속성 */

}
