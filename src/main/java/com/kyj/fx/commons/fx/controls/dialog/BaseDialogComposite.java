/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.popup
 *	작성일   : 2016. 8. 23.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.dialog;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.fxloader.FXMLController;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * 에러내용을 보여주기 위한 뷰.
 *
 * @author KYJ
 *
 */
@FXMLController(value = "BasaeDialog.fxml", isSelfController = true)
public class BaseDialogComposite extends BorderPane {

	private static Logger LOGGER = LoggerFactory.getLogger(BaseDialogComposite.class);

	/**
	 * 다이얼로그 제목
	 *
	 * @최초생성일 2016. 10. 04.
	 */
	private String title = "";
	/**
	 * 본문 제목
	 * 
	 * @최초생성일 2016. 10. 4.
	 */
	private String header = "";

	private ObservableList<Button> buttons = FXCollections.observableArrayList();

	@FXML
	private Label lblHeader;

	@FXML
	private HBox hboxButtons;

	@FXML
	private ImageView ivErr;

	@FXML
	private BorderPane borContent;

	private Stage stage;

	/**
	 * @param title
	 * @param content
	 */
	public BaseDialogComposite(String title, String header) {
		try {
			this.title = title;
			this.header = header;
			FxUtil.loadRoot(BaseDialogComposite.class, this);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	/**
	 * @param ex
	 */
	public BaseDialogComposite() {
		this("", "");
	}

	@FXML
	public void initialize() {
		// this.ivErr.setImage(new
		// Image(ClassLoader.getSystemResourceAsStream("META-INF/images/nodeicons/dialog-error.png")));
		lblHeader.setText(this.header);

		this.buttons.addListener(new ListChangeListener<Button>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Button> c) {
				if (c.next()) {
					if (c.wasAdded()) {
						hboxButtons.getChildren().addAll(c.getAddedSubList());
					} else if (c.wasRemoved()) {
						hboxButtons.getChildren().removeAll(c.getAddedSubList());
					}
					// else if (c.wasReplaced()) {
					// hboxButtons.getChildren().replaceAll(t -> t);
					// }
				}
			}
		});

	}

	public void addButton(Button btn) {
		this.buttons.add(btn);
	}

	/**
	 * 다이얼로그 OPEN.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 14.
	 * @param node
	 */
	public void show(Node node, Consumer<Stage> action) {
		Window _root = null;
		if (node != null) {
			Scene _scene = node.getScene();
			if (_scene != null) {
				_root = _scene.getWindow();
			}
		}
		show(_root, action);
	}

	/**
	 * 다이얼로그 OPEN.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 04.
	 * @param root
	 */
	public void show(Window root, Consumer<Stage> action) {

		stage = new Stage();
		stage.setTitle(this.title);
		Scene scene = new Scene(this);
		scene.setOnKeyPressed(this::sceneOnKeyPressed);
		stage.setScene(scene);

		stage.initOwner(root);
		if (action != null) {
			action.accept(stage);
		}

		stage.showAndWait();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 4.
	 * @param graphic
	 */
	public void setGraphic(Node graphic) {
		this.borContent.setCenter(graphic);
	}

	/********************************
	 * 작성일 : 2016. 10. 4. 작성자 : KYJ
	 *
	 * 키 이벤트
	 *
	 * 구현내용 : esc키를 누르면 창이 닫힘.
	 * 
	 * @param e
	 ********************************/
	public void sceneOnKeyPressed(KeyEvent e) {

		// ESC
		if (e.getCode() == KeyCode.ESCAPE) {
			if (stage != null)
				stage.close();
		}
	}

	@FXML
	public void btnOkOnAction() {
		if (this.stage != null)
			this.stage.close();
	}

}
