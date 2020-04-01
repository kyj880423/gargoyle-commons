/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.popup
 *	작성일   : 2016. 8. 23.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.dialog;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.fxloader.FXMLController;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * 에러내용을 보여주기 위한 뷰.
 *
 * @author KYJ
 *
 */
@FXMLController(value = "ExceptionDialog.fxml", isSelfController = true)
public class ExceptionDialogComposite extends BorderPane {

	private static Logger LOGGER = LoggerFactory.getLogger(ExceptionDialogComposite.class);

	/**
	 * 다이얼로그 제목
	 * 
	 * @최초생성일 2016. 8. 23.
	 */
	private static final String EXCEPTION_DIALOG_TITLE = "ExceptionDialog";

	@FXML
	private Label lblHeader;

	@FXML
	private TextArea txtConent;
	@FXML
	private Button btnOk;
	@FXML
	private ImageView ivErr;

	private Throwable ex;

	private Stage stage;

	private String title;

	private String userContent;

	public ExceptionDialogComposite(String content) {
		this(null, "Error", content);
	}

	public ExceptionDialogComposite(Throwable ex) {
		this(ex, ex.getMessage(), ValueUtil.toString(ex));
	}

	public ExceptionDialogComposite(Throwable ex, String conent) {
		this(ex, ex.getMessage(), conent);
	}

	public ExceptionDialogComposite(Throwable ex, String title, String conent) {
		try {
			this.ex = ex;
			this.title = title;
			this.userContent = conent;
			FxUtil.loadRoot(ExceptionDialogComposite.class, this);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	@FXML
	public void initialize() {

		InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream("META-INF/images/nodeicons/dialog-error.png");
		if(systemResourceAsStream!=null)
			this.ivErr.setImage(new Image(systemResourceAsStream));

		lblHeader.setText(this.title);
		txtConent.setText(this.userContent);

		// if (this.ex != null) {
		// lblHeader.setText(this.ex.getMessage());
		// if (this.userContent.isEmpty())
		// txtConent.setText(ValueUtil.toString(this.ex));
		// else
		// txtConent.setText(this.userContent);
		// } else {
		// lblHeader.setText(this.title);
		//
		// }

	}

	/**
	 * 다이얼로그 OPEN.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 8. 23.
	 * @param node
	 */
	public void show(Node node) {
		Window _root = null;
		if (node != null) {
			Scene _scene = node.getScene();
			if (_scene != null) {
				_root = _scene.getWindow();
			}
		}
		show(_root);
	}

	/**
	 * 다이얼로그 OPEN.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 8. 23.
	 * @param root
	 */
	public void show(Window root) {

		stage = new Stage();
		stage.setTitle(this.title);
		Scene scene = new Scene(this);
		scene.setOnKeyPressed(this::sceneOnKeyPressed);
		stage.setScene(scene);

		stage.initOwner(root);
		stage.showAndWait();
	}

	/********************************
	 * 작성일 : 2016. 9. 4. 작성자 : KYJ
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
