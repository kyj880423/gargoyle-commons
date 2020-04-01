/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.commons.fx.controls.srch
 *	작성일   : 2016. 6. 12.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.srch;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.memory.StageStore;
import com.kyj.fx.commons.skin.SkinManager;
import com.kyj.fx.commons.utils.DialogUtil;
import com.kyj.fx.commons.utils.FxClipboardUtil;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.fxloader.FXMLController;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

/***************************
 *
 * @author KYJ
 *
 ***************************/
@FXMLController(value = "OpenClassResource.fxml", isSelfController = true)
public abstract class ResourceView<R> extends BorderPane {

	private static Logger LOGGER = LoggerFactory.getLogger(ResourceView.class);

	@FXML
	private MenuBar menuOptional;

	@FXML
	protected ListView<R> lvResources;

	@FXML
	private TextField txtFilter;
	@FXML
	private Button btnSelect;

	@FXML
	private Button btnCancel;

	private Stage stage = new Stage();

	// 자동완성기능인데 사용시 initialize함수와 함께 주석풀것
	// private AbstractFxVoAutoCompletionBinding<String> autoCompletionBinding;

	private List<R> resources;

	protected List<R> getResources() {
		return resources;
	}

	protected void setResources(List<R> loadClasses) {
		this.resources = loadClasses;
	}

	/**
	 * 팝업선택결과를 반환할 데이터 결과 객체 KYJ
	 */
	private ResultDialog<R> result = new ResultDialog<>();

	/**
	 * 사용자 정의 Consumer
	 *
	 * @최초생성일 2015. 10. 27.
	 */
	public ObjectProperty<Consumer<R>> consumer;

	@FXML
	private Button btnReflesh;

	@FXML
	private Label lblSubTitle;

	@FXML
	private Label txtStatus;

	/**
	 * 리소스 다이얼로그를 오픈한다.
	 *
	 * @throws Exception
	 */
	public ResourceView() {
		this("");
	}

	protected StringProperty data;

	/**
	 * 리소스 다이얼로그를 오픈한다.
	 *
	 * @param data
	 *            다이얼로그를 오픈할때 텍스트필드에 기본값으로 셋팅할 텍스트
	 * @throws Exception
	 */
	public ResourceView(String data) {
		this.data = new SimpleStringProperty(data);
		FxUtil.loadRoot(ResourceView.class, this, err -> LOGGER.error(ValueUtil.toString(err)));
	}

	/********************************
	 * 작성일 : 2016. 6. 11. 작성자 : KYJ <br/>
	 *
	 * 값이 true인경우 일치라고 판단하여 데이터를 필터링해주게된다. <br/>
	 *
	 *
	 * 2018.12.14 KYJ  <br/>
	 *   해당값은 equals로 처리해주기보단 contains 함수를 사용하여, 특정 키워드가 <br/>
	 *   포함되면 true를 반환할 수 있게 프로그램하는것을 권장. <br/>
	 * @param value
	 * 		data  value.
	 * @param text
	 * 		user search keyword
	 * @return
	 ********************************/
	public abstract boolean isMatch(R value, String text);

	/********************************
	 * 작성일 : 2016. 6. 11. 작성자 : KYJ
	 *
	 * ListView<R>에 표현할 converter를 구현한다.
	 *
	 * @return
	 ********************************/
	public abstract StringConverter<R> stringConverter();

	@FXML
	public void initialize() {

		if (data != null)
			txtFilter.setText(data.get());

		consumer = new SimpleObjectProperty<>();

		// 이벤트 리스너 등록 consumer가 변경될때마다 ResultDialog에 값을 set.
		consumer.addListener((oba, oldval, newval) -> {
			result.setConsumer(consumer.get());
		});

		resources = FXCollections.observableArrayList();

		lvResources.setCellFactory(TextFieldListCell.forListView(stringConverter()));

		/* Control + C 기능 적용 */

		lvResources.addEventHandler(KeyEvent.KEY_PRESSED, this::lvResourcesOnKeyPressed);

		// lvResources.getItems().addAll(loadClasses);

		// 자동완성기능 사용시 주석풀것
		// autoCompletionBinding = getCompletionBinding();

		txtFilter.textProperty().addListener(txtFilterChangeListener);
		txtFilter.setOnKeyPressed(this::txtFilterOnKeyPressed);

		btnReflesh.setOnMouseClicked(this::btnRefleshOnMouseClick);

		
		Platform.runLater(() -> {
			init();
			onInited();
		});
	}

	protected abstract void init();

	protected void onInited() {
		txtFilter.requestFocus();

		// Scene scene = getScene();

		this.addEventHandler(KeyEvent.KEY_PRESSED, sceneKeyEvent);

	}

	/**
	 * 팝업호출
	 *
	 * @Date 2015. 10. 17.
	 * @return
	 * @User KYJ
	 */
	public ResultDialog<R> show() {
		return show(null, true);
	}

	/********************************
	 * 작성일 : 2016. 6. 12. 작성자 : KYJ
	 *
	 * 팝업호출
	 *
	 * @param modal
	 * @return
	 ********************************/
	public ResultDialog<R> show(boolean modal) {

		return show(null, modal);
	}

	/**
	 * 팝업호출
	 *
	 * @Date 2015. 10. 17.
	 * @return
	 * @User KYJ
	 */
	public ResultDialog<R> show(Window owner, boolean modal) {
		// if (stage != null) {
		// if (modal) {
		// stage.showAndWait();
		// } else
		// stage.show();
		// }

		stage = new Stage();
		Scene scene = new Scene(this);
		scene.getStylesheets().add(SkinManager.getInstance().getSkin());
		stage.setScene(scene);

		stage.setTitle(titleProperty.get());

		// stage.setResizable(false);
		if (modal) {
			stage.initModality(Modality.APPLICATION_MODAL);
			if (owner != null) {
				stage.initOwner(owner);
			} else {
				stage.initOwner(StageStore.getPrimaryStage());
			}

			stage.showAndWait();
		} else {

			stage.show();
		}

		return result;
	}

	/**
	 * 창을 닫음
	 *
	 * @Date 2015. 10. 17.
	 * @User KYJ
	 */
	public void close() {
		if (this.stage != null)
			this.stage.close();
	}

	/**
	 * 타이틀 정의.
	 *
	 * @최초생성일 2016. 6. 12.
	 */
	private StringProperty titleProperty = new SimpleStringProperty(getClass().getSimpleName());

	public String getTitle() {
		return titleProperty.get();
	}

	public void setTitle(String title) {
		titleProperty.set(title);
	}

	/**
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 27.
	 * @param event
	 */
	@FXML
	public void btnSelectOnMouseClick(MouseEvent event) {
		R selectedItem = lvResources.getSelectionModel().getSelectedItem();

		if (ValueUtil.isEmpty(selectedItem)) {
			DialogUtil.showMessageDialog("행을 선택하세요.");
			return;
		}

		result.setClickButton(btnSelect);
		result.setStatus(ResultDialog.SELECT);
		result.setData(selectedItem);
		
		close();
	}

	@FXML
	public void btnCancelOnMouseClick(MouseEvent event) {
		result.setClickButton(btnCancel);
		result.setStatus(ResultDialog.CANCEL);
		result.setData(null);
		close();
	}

	public abstract void btnRefleshOnMouseClick(MouseEvent event);

	@FXML
	public void lvResourcesMouseClick(MouseEvent event) {
		if (event.getClickCount() == 2) {
			btnSelectOnMouseClick(event);
		}
	}

	/**
	 * 텍스트 필터 텍스트 변경 리스너
	 * 
	 * @최초생성일 2016. 8. 5.
	 */
	private ChangeListener<? super String> txtFilterChangeListener = (oba, oldval, newval) -> {

		Platform.runLater(() -> {

			if (newval == null)
				return;

			lvResources.getItems().clear();

			List<R> collect = resources.stream().filter(r -> {
				return isMatch(r, newval);
			}).collect(Collectors.toList());

			lvResources.getItems().addAll(collect);
		});

	};

	/**
	 * 텍스트필터 키 클릭 이벤트.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 8. 5.
	 * @param e
	 */
	private void txtFilterOnKeyPressed(KeyEvent e) {

		switch (e.getCode()) {
		case DOWN: {
			// lvResources.getSelectionModel().select(0);
			lvResources.getSelectionModel().selectFirst();
			lvResources.requestFocus();
			e.consume();
		}
			break;

		case UP: {

			lvResources.getSelectionModel().selectLast();
			lvResources.requestFocus();
			e.consume();
		}
			break;
		default:
			break;
		}

	}

	/**
	 * lvResources 그리드 키 이벤트
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 8. 5.
	 * @param ev
	 */
	public void lvResourcesOnKeyPressed(KeyEvent ev) {
		if (KeyCode.C == ev.getCode() && !ev.isControlDown() && !ev.isShiftDown() && !ev.isAltDown()) {
			R selectedItem = lvResources.getSelectionModel().getSelectedItem();
			String string = stringConverter().toString(selectedItem);
			FxClipboardUtil.putString(string);
			ev.consume();
		} else if (ev.getCode() == KeyCode.UP && !ev.isControlDown() && !ev.isShiftDown() && !ev.isAltDown()) {
			int selectedIndex = lvResources.getSelectionModel().getSelectedIndex();
			if (selectedIndex == 0) {
				txtFilter.requestFocus();
			}
		} else if (ev.getCode() == KeyCode.DOWN && !ev.isControlDown() && !ev.isShiftDown() && !ev.isAltDown()) {
			int idx = lvResources.getSelectionModel().getSelectedIndex();
			if (idx == (lvResources.getItems().size() - 1)) {
				txtFilter.requestFocus();
			}
		} else if (ev.getCode() == KeyCode.ENTER && !ev.isControlDown() && !ev.isShiftDown() && !ev.isAltDown()) {
			if (ev.isConsumed())
				return;

			btnSelectOnMouseClick(null);
		} else if (KeyCode.ESCAPE == ev.getCode()) {
			sceneKeyEvent.handle(ev);
		}
	}

	/**
	 * 화면에 대한 전역선택 키 이벤트.
	 * 
	 * @최초생성일 2016. 8. 5.
	 */
	private EventHandler<? super KeyEvent> sceneKeyEvent = ev -> {
		if (ev.getCode() == KeyCode.ESCAPE && !ev.isControlDown() && !ev.isShiftDown() && !ev.isAltDown()) {
			Scene scene = getScene();
			if (scene != null) {
				Window window = scene.getWindow();
				if (window instanceof Stage) {
					((Stage) window).close();
				} else {
					window.hide();
				}
				// Stage window = (Stage) scene.getWindow();
				// if (window != null)
				// window.close();
			}
		}
	};

	/**
	 * @return the consumer
	 */
	public Consumer<R> getConsumer() {
		return consumer.get();
	}

	/**
	 * @param consumer
	 *            the consumer to set
	 */
	public void setConsumer(Consumer<R> consumer) {
		this.consumer.set(consumer);
	}

	public final TextField getTxtFilter() {
		return txtFilter;
	}

	/**
	 * 상태바에 메세지를 남긴다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 20.
	 * @param message
	 */
	public void setStatusMessage(String message) {
		txtStatus.setText(message);
	}

	public void setSubTitle(String subTitle) {
		lblSubTitle.setText(subTitle);
	}

	/**
	 * 메뉴를 사용하고하는 화면에선 아래 MenuBar를 리턴받아 구현.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 22.
	 * @return
	 */
	public MenuBar getMenuOptional() {
		return menuOptional;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public Button getSelectButton() {
		return btnSelect;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 9.
	 * @return
	 */
	public ResultDialog<R> getResult() {
		return result;
	}

}
