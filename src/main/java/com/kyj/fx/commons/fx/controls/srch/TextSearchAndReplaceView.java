/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.popup
 *	작성일   : 2015. 12. 14.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.srch;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.lang.StringUtils;
import org.fxmisc.richtext.CodeArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.models.ReplaceResultVO;
import com.kyj.fx.commons.models.ReplaceResultVO.REPLACE_TYPE;
import com.kyj.fx.commons.models.SearchResultVO;
import com.kyj.fx.commons.utils.DialogUtil;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 텍스트기반 검색
 *
 * NotePad +프로그램을 베이스로 잡고 작성.
 *
 * @author KYJ
 *
 */
/**
 * @author KYJ
 *
 */
public class TextSearchAndReplaceView extends BorderPane {
	private static Logger LOGGER = LoggerFactory.getLogger(TextSearchAndReplaceView.class);

	private FXMLLoader loader;
	/**
	 * 본문 컨텐츠
	 *
	 * @최초생성일 2015. 12. 14.
	 */
	private StringProperty contentProperty;

	/**
	 * 검색 시작 인덱스
	 *
	 * @최초생성일 2015. 12. 14.
	 */
	private IntegerProperty slidingStartIndexProperty;

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

	private Parent parent;

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
	private TextField /* 바꾸기에서 찾기 텍스트*/ txtFind, /*바꾸기 텍스트*/txtReplace;

	@FXML
	private Accordion accFindAll;

	@FXML
	private ListView<SearchResultVO> lvFindAll;

	/**
	 * 모두찾기에서 찾은 데이터를 화면에 표시할 텍스트 포멧을 정의한다.
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

	/**
	 * 생성자
	 *
	 * @param parent
	 * @param content
	 */
	public TextSearchAndReplaceView(CodeArea parent, ObservableValue<String> content) {
		loader = new FXMLLoader();
		loader.setLocation(TextSearchAndReplaceView.class.getResource("TextSearchAndReplaceView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
			this.contentProperty = new SimpleStringProperty();
			this.contentProperty.bind(content);
			this.searchResultVOProperty = new SimpleObjectProperty<>();
			this.replaceResultVOProperty = new SimpleObjectProperty<>();
			this.selectionMoveProperty = new SimpleObjectProperty<>();
			slidingStartIndexProperty = new SimpleIntegerProperty();

			this.parent = parent;
		} catch (IOException e) {
			LOGGER.error(ValueUtil.toString(e));
		}

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String selectedText = parent.getSelectedText();
				txtFind.setText(selectedText);
				txtFindTextContent.setText(selectedText);
				txtFindTextContent.requestFocus();
			}
		});

	}

	/**
	 * 검색 처리를 진행하며 검색이 완료되면 searchResultVOProperty에 값을 set함과 동시에 부가정보 (
	 * slidingStartIndexProperty, slidingEndIndexProperty )를 갱신함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 12. 28.
	 */
	private int find() {
		return find(this.txtFindTextContent.getText(), contentProperty.get());
	}

	/**
	 * 검색 처리를 진행하며 검색이 완료되면 searchResultVOProperty에 값을 set함과 동시에 부가정보 (
	 * slidingStartIndexProperty, slidingEndIndexProperty )를 갱신함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 12. 14.
	 * @param content
	 */
	private int find(String content) {
		return find(content, txtFindTextContent.getText());
	}

	/**
	 * 검색 처리를 진행하며 검색이 완료되면 searchResultVOProperty에 값을 set함과 동시에 부가정보 (
	 * slidingStartIndexProperty, slidingEndIndexProperty )를 갱신함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 12. 28.
	 * @param content
	 * @param function
	 *            부가적으로 처리할 내용을 기술.
	 */
	private int find(String content, Function<SearchResultVO, SearchResultVO> function) {
		return find(txtFindTextContent.getText(), content, function);
	}

	private int find(String findWord, String content) {
		return find(findWord, content, null);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2015. 12. 28.
	 * @param findWord
	 *            찾을 문자열
	 * @param content
	 * @param function
	 */
	private int find(String findWord, String _content, Function<SearchResultVO, SearchResultVO> function) {
		
		if (_content == null || _content.isEmpty())
			return -1;
		
		String content = _content.toLowerCase();
		
		int startIdx = indexOf(content, findWord, slidingStartIndexProperty.get());
		int findStartIndex = -1;
		if (startIdx >= 0) {
			findStartIndex = startIdx;
			int endIdx = startIdx + findWord.length();

			SearchResultVO value = new SearchResultVO();
			value.setSearchText(findWord);
			value.setStartIndex(startIdx);
			value.setEndIndex(endIdx);

			if (function != null)
				value = function.apply(value);

			searchResultVOProperty.set(value);
			slidingStartIndexProperty.set(endIdx);

		} else {
			slidingStartIndexProperty.set(0);
		}
		return findStartIndex;
	}

	private void findAll(String content, Function<SearchResultVO, SearchResultVO> function) {
		findAll(txtFindTextContent.getText(), content, function);
	}

	private void findAll(String findword, String content, Function<SearchResultVO, SearchResultVO> function) {

		if (ValueUtil.isEmpty(findword)) {
			DialogUtil.showMessageDialog(FxUtil.getWindow(this), "찾을 단어를 입력해야합니다.");
			return;
		}

		if (content == null || content.isEmpty())
			return;

		int startIdx = 0;
		
		String lowerContent = content.toLowerCase();
		while (startIdx >= 0) {
			startIdx = indexOf(lowerContent, findword, startIdx);
			if (startIdx == -1)
				break;

			int endIdx = startIdx + findword.length();
			SearchResultVO value = new SearchResultVO();
			value.setSearchText(  content.substring(startIdx, findword.length())   );
			value.setStartIndex(startIdx);
			value.setEndIndex(endIdx);
			LOGGER.debug(String.format("FindContent : %s , %d", content.subSequence(startIdx, endIdx), startIdx));
			if (function != null)
				value = function.apply(value);
			searchResultVOProperty.set(value);

			startIdx = endIdx;
		}
	}

	public int findAll(String content, Consumer<SearchResultVO> action) throws NullPointerException {
		return findAll(txtFindTextContent.getText(), content, action);
	}

	public int findAll(String findword, String content, Consumer<SearchResultVO> action) throws NullPointerException {

		int foundCount = -1;
		if (action == null)
			throw new NullPointerException("action is null");

		if (ValueUtil.isEmpty(findword)) {
			DialogUtil.showMessageDialog(FxUtil.getWindow(this), "찾을 단어를 입력해야합니다.");
			return foundCount;
		}

		if (content == null || content.isEmpty())
			return foundCount;

		String lowerContent = content.toLowerCase();
		int startIdx = 0;
		while (startIdx >= 0) {
			startIdx = indexOf(lowerContent, findword, startIdx);

			if (startIdx == -1)
				break;

			int endIdx = startIdx + findword.length();
			SearchResultVO value = new SearchResultVO();
			value.setSearchText( content.substring( startIdx, endIdx ) );
			value.setStartIndex(startIdx);
			value.setEndIndex(endIdx);
			//			LOGGER.debug(String.format("FindContent : %s , %d", content.subSequence(startIdx, endIdx), startIdx));

			action.accept(value);
			foundCount++;
			startIdx = endIdx;
		}

		return foundCount;
	}

	private void reaplce(String findText, String replaceText) {
		String content = contentProperty.get().toLowerCase();

		int startIndex = indexOf(content, findText, slidingStartIndexProperty.get());
		
		if (startIndex >= 0) {
			String preContent = content.substring(0, startIndex);
			String afterContent = content.substring(startIndex);
//			afterContent = afterContent.replaceFirst(findText, replaceText);
			afterContent = StringUtils.replaceOnce(afterContent, findText, replaceText);

			StringBuffer sb = new StringBuffer();
			sb.append(preContent);
			sb.append(afterContent);

			ReplaceResultVO replaceResultVO = new ReplaceResultVO();
			replaceResultVO.setReaplceType(REPLACE_TYPE.SIMPLE);
			replaceResultVO.setSearchText(findText);
			replaceResultVO.setReplaceText(replaceText);
			replaceResultVO.setReaplceResult(sb.toString());
			replaceResultVOProperty.set(replaceResultVO);
			slidingStartIndexProperty.set(startIndex);
		}
		/* 일치하는 텍스트를 못찾은경우 다시 0번으로 이동한후 검색처리함. */
		else {
			slidingStartIndexProperty.set(0);
			int findStartIndex = find(findText, content, null);
			/*
			 * 특화처리. 검색된 문자열의 시작인덱스를 다시 slidingStartIndexProperty에 집어넣음. 이렇게
			 * 처리하지않으면 찾은문자열의 끝부분부터 찾게된다. 이 API의 목적은 replace가 목적이므로 시작인덱스를
			 * 넣어줘야함.
			 */
			slidingStartIndexProperty.set(findStartIndex);
		}

	}

	private void reaplceAll() {
		// 찾기를 한후.
		String content = contentProperty.get();
		String findText = txtFind.getText();
		String replaceText = txtReplace.getText();

		String reaplceResult = StringUtils.replace(content, findText, replaceText);
//		String reaplceResult = content.replaceAll(findText, replaceText);
		ReplaceResultVO replaceResultVO = new ReplaceResultVO();
		replaceResultVO.setReaplceType(REPLACE_TYPE.ALL);
		replaceResultVO.setSearchText(findText);
		replaceResultVO.setReplaceText(replaceText);
		replaceResultVO.setReaplceResult(reaplceResult);
		replaceResultVOProperty.set(replaceResultVO);
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

			/* (non-Javadoc)
			 * @see javafx.scene.control.Cell#updateItem(java.lang.Object, boolean)
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
					//  Format Text ::   Start Index %d   End Index %d   Keyword : %s
					setText(String.format(FIND_ALL_TEXT_FORMAT, item.getStartIndex(), item.getEndIndex(), item.getSearchText()));
					//							setText(item.get);
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

	/**
	 * 바꾸기탭에서 찾기
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 12. 28.
	 * @param e
	 */
	@FXML
	public void btnReplaceOnFindOnMouseClick() {
		String content = contentProperty.get();
		int findStartIndex = find(txtFind.getText(), content);
		/*
		 * 특화처리. 검색된 문자열의 시작인덱스를 다시 slidingStartIndexProperty에 집어넣음. 이렇게 처리하지않으면
		 * 찾은문자열의 끝부분부터 찾게된다. 이 API의 목적은 replace가 목적이므로 시작인덱스를 넣어줘야함.
		 */
		slidingStartIndexProperty.set(findStartIndex);
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
	 * @작성일 : 2015. 12. 28.
	 * @param e
	 */
	@FXML
	public void btnCloseOnMouseClick(MouseEvent e) {
		Scene scene = getScene();
		Stage window = (Stage) scene.getWindow();
		window.close();
	}

	/**
	 * 바꾸기
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 12. 28.
	 */
	@FXML
	public void btnReplaceOnMouseClick() {
		// 찾기를 한후.
		String findText = txtFind.getText();
		String replaceText = txtReplace.getText();
		reaplce(findText, replaceText);
	}

	/**
	 * 모두 바꾸기
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 12. 28.
	 */
	@FXML
	public void btnReplaceAllOnMouseClick() {
		reaplceAll();
	}

	/**
	 * 모두찾기
	 * @throws NullPointerException
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 12. 28.
	 */
	@FXML
	public void btnFindAllOnMouseClick() throws NullPointerException {
		String content = contentProperty.get();

		this.lvFindAll.getItems().clear();
		int findAll = findAll(content, vo -> {
			this.lvFindAll.getItems().add(vo);
		});

		//한개라도 찾으면.
		if (findAll != -1) {
			//일치하는 텍스트 정보를 화면에 표시
//			showMatchCount();
			txtDesc.setText(String.format("Found Word \" %s \" : count : %d ", txtFindTextContent.getText(), findAll + 1));
			accFindAll.getPanes().get(0).setExpanded(true);
		}

		//		accFindAll

	}

	/**
	 * 일치하는수
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 12. 28.
	 */
	@FXML
	public void btnMatchCountOnMouseClick() {
		showMatchCount();
	}

	/**
	 * 일치하는 검색 정보를 화면에 표시
	 * @작성자 : KYJ
	 * @작성일 : 2017. 2. 3.
	 */
	private void showMatchCount() {
		final String findText = this.txtFindTextContent.getText();
		showMatchCount(findText);
	}
	/**
	 * 일치하는 검색 정보를 화면에 표시
	 * @작성자 : KYJ
	 * @작성일 : 2017. 2. 3.
	 * @param findText
	 */
	private void showMatchCount(String findText) {
		final String content = contentProperty.get();
		showMatchCount(findText, content);
	}

	/**
	 * 일치하는 검색 정보를 화면에 표시
	 * @작성자 : KYJ
	 * @작성일 : 2017. 2. 3.
	 * @param findText
	 * @param content
	 */
	private void showMatchCount(String findText, String content) {
		if (ValueUtil.isEmpty(findText))
			return;

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				int matchIndex = 0;
				int matchCount = -1;
				do {
					matchIndex = indexOf(content, findText, matchIndex);
					matchIndex += 1;
					matchCount++;
				} while (matchIndex > 0);
				txtDesc.setText(String.format("Found Word \" %s \" : count : %d ", findText, matchCount));
			}
		});
	}

	/**
	 * 바꾸기탭에서 찾기 마우스 클릭 이벤트
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 12. 28.
	 * @param e
	 */
	@FXML
	public void txtFindOnKeyClick(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			btnReplaceOnFindOnMouseClick();
		}
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
		LOGGER.debug("SHOW SimpleSQLResult View.....");
		LOGGER.debug("call executeSQL function....");

		Scene scene = new Scene(this/* , 1100, 700 */);
		stage.setScene(scene);

		stage.setResizable(false);
		// stage.setAlwaysOnTop(true);
		stage.initModality(Modality.NONE);
		
		stage.initOwner(/*parent.getScene().getWindow()*/ FxUtil.getWindow(parent));
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

	
	/**
	 * find keyword ignore case.
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 11. 
	 * @param content
	 * @param find
	 * @param startIdx
	 * @return
	 */
	public int indexOf(String content, String find, int startIdx) {
		String lowerFind = find.toLowerCase();
		int indexOf = content.indexOf(lowerFind, startIdx);
		return indexOf;
	}
	
	/* [시작] prop 속성 */
	public final StringProperty contentPropertyProperty() {
		return this.contentProperty;
	}

	public final java.lang.String getContentProperty() {
		return this.contentPropertyProperty().get();
	}

	public final void setContentProperty(final java.lang.String contentProperty) {
		this.contentPropertyProperty().set(contentProperty);
	}

	public final IntegerProperty slidingStartIndexPropertyProperty() {
		return this.slidingStartIndexProperty;
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
