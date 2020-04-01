/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.main.layout
 *	작성일   : 2015. 10. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.ExceptionHandler;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.fxloader.FxPostInitialize;

import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * 단순한 텍스트를 보여주는 용도로만 사용되됨.
 *
 * @author KYJ
 *
 */
@FXMLController(value = "SimpleTextView.fxml", isSelfController = true)
public class JavaTextView extends BorderPane {
	private static Logger LOGGER = LoggerFactory.getLogger(JavaTextView.class);

	private JavaTextArea javaTextArea;

	private boolean showButtons;
	/**
	 * 버튼박스
	 */
	@FXML
	private HBox hboxButtons;

	@FXML
	private Button btnClose;

	// private StringProperty textProperty;
	// private Stage stage = new Stage();

	public JavaTextView() {
		this("", true, e -> LOGGER.error(ValueUtil.toString(e)));
	}

	public JavaTextView(@NamedArg("content") String content) {
		this(content, true, e -> LOGGER.error(ValueUtil.toString(e)));
	}

	public JavaTextView(String content, boolean showButtons) {
		this(content, showButtons, e -> LOGGER.error(ValueUtil.toString(e)));
	}

	/**
	 * 임시적으로 텍스트를 저장함.  <br/>
	 * 임시적으로 저장하지 않아서 관리되는 경우 <br/> 
	 * 텍스트 스타일링이 제대로 처리되지않음.  <br/>
	 * 
	 * @최초생성일 2018. 6. 21.
	 */
	private StringProperty tempContent = new SimpleStringProperty();

	public JavaTextView(String content, boolean showButtons, ExceptionHandler handler) {
		LOGGER.debug("JavaTextView Constructor called");
		this.tempContent.set(content);

		this.showButtons = showButtons;
		FxUtil.loadRoot(JavaTextView.class, this, ex -> handler.handle(ex));
	}

	@FxPostInitialize
	public void initPost() {

	}

	@FXML
	public void initialize() {
		javaTextArea = new JavaTextArea();
		hboxButtons.setVisible(showButtons);
		setCenter(javaTextArea);
		javaTextArea.setContent(this.tempContent.get());
	}

	public BooleanProperty btnCloseVisibleProperty() {
		return this.btnClose.visibleProperty();
	}

	public void setBtnCloseVisible(boolean visible) {
		this.btnClose.setVisible(visible);
	}

	public boolean getBtnVisible() {
		return this.btnClose.isVisible();
	}

	public void setContent(String content) {
		javaTextArea.setContent(content);
	}

	public String getContent() {
		return javaTextArea.getContent();
	}

	public void setEditable(boolean editable) {
		javaTextArea.setEditable(editable);
	}

	// public void show(double width, double height) throws IOException {
	// show(StageStore.getPrimaryStage(), width, height);
	// }
	//
	// public void show(Window root, double width, double height) throws
	// IOException {
	//
	// btnClose.setOnMouseClicked(new EventHandler<MouseEvent>() {
	//
	// @Override
	// public void handle(MouseEvent event) {
	// close();
	// }
	// });
	//
	// Scene scene = new Scene(this, width, height);
	// stage.setTitle("JavaTextView Popup");
	// stage.setScene(scene);
	// stage.initOwner(root);
	// stage.show();
	//
	// }

	// public void show() throws IOException {
	// show(1100, 700);
	// }
	//
	// private void close() {
	// stage.close();
	// }

	@FXML
	public void miOpenOnAction() {}
	@FXML
	public void openDexStringOnAction() {}
	@FXML
	public void miSaveOnAction() {}
	@FXML
	public void miSaveAsOnAction() {}
}
