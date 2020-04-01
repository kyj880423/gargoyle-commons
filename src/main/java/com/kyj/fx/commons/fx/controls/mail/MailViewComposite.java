/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.mail
 *	작성일   : 2017. 10. 10.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.mail;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.functions.ListViewFileCellFactory;
import com.kyj.fx.commons.fx.controls.grid.NumberingCellValueFactory;
import com.kyj.fx.commons.mail.Mail;
import com.kyj.fx.commons.utils.DialogUtil;
import com.kyj.fx.commons.utils.ExceptionHandler;
import com.kyj.fx.commons.utils.FileUtil;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.IdGenUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.fxloader.InstanceTypes;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * 
 * 메일 보내기 기능을 지원하는 뷰
 * 
 * 
 * 
 * 어떤 에러가 발생시 http://fiddle.tinymce.com/8Ffaab/1 아래 사이트에서 테스트 코드 작성해보고 시도해볼것.
 * 
 * @author KYJ
 *
 */

@FXMLController(value = "MailView.fxml", isSelfController = true)
class MailViewComposite extends BorderPane implements Closeable {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailViewComposite.class);
	@FXML
	private TextField txtSubject, txtRecipient;
	@FXML
	private WebView wbAprvCont;
	@FXML
	private TableView<MailReceiver> tbReceiver;
	@FXML
	private ListView<File> tbAttachment;
	@FXML
	private TableColumn<MailReceiver, Integer> tcNo;
	@FXML
	private TableColumn<MailReceiver, String> tcEmail, tcType;

	@FXML
	private RadioButton rbTo, rboCc, rboBcc;

	@FXML
	private CheckBox chkAttachments;

	@FXML
	private Button btnSearch, btnDel;

	@FXML
	private Label lblAttachmentCount;

	@FXML
	private ChoiceBox<ContentType> choContentType;
	/**
	 * 메일관련 webview가 로드되었는지 확인
	 * 
	 * @최초생성일 2017. 10. 18.
	 */
	private BooleanProperty webViewLoaded = new SimpleBooleanProperty();

	private Mail mail;

	private WebEngine engine;
	private AttachmentHandler attachmentHandler;
	private SendMailHandler sendMailHandler;
	private RecipientHandler recipientHandler;

	public MailViewComposite() {
		this.mail = new Mail();
		FxUtil.loadRoot(MailViewComposite.class, this, err -> {
			LOGGER.error(ValueUtil.toString(err));
		});
	}

	private String initCont;

	public MailViewComposite(String initCont) {
		this.mail = new Mail();
		this.initCont = initCont;
		FxUtil.loadRoot(MailViewComposite.class, this, err -> {
			LOGGER.error(ValueUtil.toString(err));
		});
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 16.
	 * @return
	 */
	private RecipientType getSelectedType() {
		if (rbTo.isSelected()) {
			return RecipientType.TO;
		} else if (rboCc.isSelected())
			return RecipientType.CC;
		else if (rboBcc.isSelected())
			return RecipientType.BCC;
		return RecipientType.TO;
	}

	@FXML
	public void initialize() {
		mail.mailSubjectProperty().bind(txtSubject.textProperty());
		attachmentHandler = new AttachmentHandler(this);
		sendMailHandler = new SendMailHandler(this);

		sendMailHandler.setOnSuccessHandler(str -> {
			DialogUtil.showMessageDialog(FxUtil.getWindow(MailViewComposite.this), "메일 전송 성공");
		});
		sendMailHandler.setErrorHandler(sendMailFailureHandler);
		recipientHandler = new RecipientHandler(this);

		tbAttachment.setCellFactory(new ListViewFileCellFactory());
		try {
			engine = wbAprvCont.getEngine();

			engine.setOnError(ev -> {
				LOGGER.error(ValueUtil.toString(ev.getException()));
			});

			ChangeListener<State> listener = new ChangeListener<State>() {

				@Override
				public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
					if (newValue == State.SUCCEEDED) {

						if (ValueUtil.isNotEmpty(initCont)) {

							String[] split = initCont.split("\n");
							Optional<String> reduce = Stream.of(split).map(str -> "<p>".concat(str).concat("</p>")).reduce((str1, str2) -> {
								return str1.concat(str2);
							});

							reduce.ifPresent(str -> {
								setText(str);
							});

						}

						engine.getLoadWorker().stateProperty().removeListener(this);
						webViewLoaded.set(true);
					}
				}
			};

			engine.getLoadWorker().stateProperty().addListener(listener);

			File file = new File("javascript/tinymce/index.html");
			if (file.exists()) {
				engine.load(file.toURI().toURL().toExternalForm());
			} else {
				DialogUtil.showMessageDialog("the e-mail template file does not exists. " + file.getPath());
			}

			// engine.executeScript("
			// document.getElementById('gargoyle-textarea').innerHTML= '<p>" +
			// initCont + "</p> ' ");

			// setText(initCont);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		tcNo.setCellValueFactory(new NumberingCellValueFactory<>(tbReceiver));
		tcEmail.setCellValueFactory(param -> {
			return param.getValue().emailProperty();
		});

		tcType.setCellValueFactory(param -> {
			return param.getValue().typeProperty();
		});

		chkAttachments.selectedProperty().addListener(chkAttachmentsListener);
		tbAttachment.getItems().addListener(tbAttachmentListChangeListener);

		choContentType.getItems().addAll(ContentType.values());
		choContentType.getSelectionModel().select(ContentType.html);

		wbAprvCont.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ev) {

				if (ev.isControlDown() && ev.getCode() == KeyCode.V) {

					Clipboard systemClipboard = Clipboard.getSystemClipboard();
					Set<DataFormat> contentTypes = systemClipboard.getContentTypes();
					LOGGER.debug("{}", contentTypes);
					Image image = systemClipboard.getImage();
					if (image != null) {

						try {

							LOGGER.debug("image.");

							BufferedImage in = SwingFXUtils.fromFXImage(image, null);

							try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
								ImageIO.write(in, "png", out);
								// File output = new
								// File(FileUtil.getTempGagoyle(),
								// DateUtil.getCurrentDateString() + ".png");
								// ImageIO.write(in, "png", output);
								String imageData = Base64.getEncoder().encodeToString(out.toByteArray());

								setText(String.format("<img src=data:image/jpeg;base64,%s></img>", imageData));

							}
						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					// Object content =
					// systemClipboard.getContent(DataFormat.RTF);
					// LOGGER.debug("{}", content.toString());
					// LOGGER.debug("ctrl + v");
				}
			}
		});
	}

	/**
	 * 첨부파일 리스트
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 17.
	 * @return
	 */
	public List<File> getAttachments() {
		return tbAttachment.getItems();
	}

	public List<MailReceiver> getRecipients() {
		return tbReceiver.getItems();
	}

	public final Mail getMail() {
		return mail;
	}

	/**
	 * 메일 전송 실패시 처리할 내용 구현
	 * 
	 * @최초생성일 2017. 10. 18.
	 */
	private ExceptionHandler sendMailFailureHandler = err -> DialogUtil.showMessageDialog(FxUtil.getWindow(MailViewComposite.this),
			"메일 전송 실패.", err.getMessage());

	private ListChangeListener<File> tbAttachmentListChangeListener = new ListChangeListener<File>() {

		@Override
		public void onChanged(javafx.collections.ListChangeListener.Change<? extends File> c) {
			if (c.next()) {
				if (c.wasAdded() || c.wasRemoved() || c.wasUpdated()) {
					lblAttachmentCount.setText(String.valueOf(c.getList().size()));
				}
			}

		}
	};

	private ChangeListener<Boolean> chkAttachmentsListener = new ChangeListener<Boolean>() {

		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			btnSearch.setDisable(newValue);
			btnDel.setDisable(newValue);
		}
	};

	public boolean canWrite() {
		return webViewLoaded.get();
	}

	public void setText(String content) {

		if (!canWrite()) {
			ChangeListener<Boolean> listener = new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					LOGGER.debug("Changed..... {} ", newValue);
					if (newValue) {

						StringBuffer sb = new StringBuffer();
						sb.append("setTimeout(function(){\n");
						sb.append(String.format("tinymce.activeEditor.setContent('%s');", content));
						sb.append("tinymce.activeEditor.focus();\n");
						sb.append(" \n");
						sb.append("},100)\n");

						// LOGGER.debug(sb.toString());
						engine.executeScript(sb.toString());
						webViewLoaded.removeListener(this);
					}

				}
			};

			webViewLoaded.addListener(listener);
		} else {

			// StringBuffer sb = new StringBuffer();
			// sb.append("setTimeout(function(){\n");
			// sb.append(String.format("tinymce.activeEditor.setContent('%s');",
			// content));
			// sb.append("tinymce.activeEditor.focus();\n");
			// sb.append(" \n");
			// sb.append("},100)\n");
			// engine.executeScript(sb.toString());
			engine.executeScript(String.format("tinymce.activeEditor.setContent('%s');", content));
		}

	}

	@FXML
	public void btnSendOnAction() {
		// Object executeScript = engine
		// .executeScript(" function outText(){ return
		// tinymce.activeEditor.getContent({format: 'raw'}); } outText(); ");
		Object executeScript = engine.executeScript("  tinymce.activeEditor.getContent({format: 'raw'}); ");
		// 본문 타입
		mail.setContentType(choContentType.getSelectionModel().getSelectedItem()
				.toString() /* "text/html" */);

		// 본문
		mail.setMailContent(executeScript.toString());

		// 첨부파일 처리

		this.attachmentHandler.build();
		this.recipientHandler.build();
		this.sendMailHandler.sendMail();
	}

	@FXML
	public void btnAddRecipientOnAction() {
		String email = txtRecipient.getText();
		if (ValueUtil.isNotEmpty(email)) {

			String regexMatch = ValueUtil.regexMatch(ValueUtil.EMAIL_VALIDATION_EXP, email);
			if (ValueUtil.isNotEmpty(regexMatch)) {
				tbReceiver.getItems().add(new MailReceiver(email, getSelectedType().name()));
				this.txtRecipient.setText("");
			}
		}
	}

	@FXML
	public void txtRecipientOnKeyPress(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			btnAddRecipientOnAction();
		}
	}

	@Override
	public void close() {
		Platform.runLater(() -> {
			if (engine != null)
				engine.load("about:blank");
		});

	}

	@FXML
	public void rbToOnAction() {
		changeSelectedType();

	}

	private void changeSelectedType() {
		MailReceiver selectedItem = tbReceiver.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			selectedItem.setType(getSelectedType().toString());
		}

	}

	@FXML
	public void rboCcOnAction() {
		changeSelectedType();
	}

	@FXML
	public void rboBccOnAction() {
		changeSelectedType();
	}

	@FXML
	public void btnSearchOnAction() {
		List<File> files = DialogUtil.showMultiFileDialog(FxUtil.getWindow(this), chooser -> {

		});

		tbAttachment.getItems().addAll(files);
	}

	@FXML
	public void btnRemoveRecipientOnAction() {
		ObservableList<MailReceiver> selectedItems = tbReceiver.getSelectionModel().getSelectedItems();
		tbReceiver.getItems().removeAll(selectedItems);
	}

	@FXML
	public void btnDeOnAction() {
		ObservableList<File> selectedItems = tbAttachment.getSelectionModel().getSelectedItems();
		tbAttachment.getItems().removeAll(selectedItems);
	}

	@FXML
	public void RecipientDownRecipientOnAction() {
		int selectedIndex = tbReceiver.getSelectionModel().getSelectedIndex();
		if (selectedIndex == -1)
			return;

		ObservableList<MailReceiver> items = tbReceiver.getItems();
		if (selectedIndex < items.size() - 1) {

			MailReceiver a = items.get(selectedIndex);
			MailReceiver b = items.get(selectedIndex + 1);
			items.set(selectedIndex + 1, a);
			items.set(selectedIndex, b);
		}
	}

	@FXML
	public void btnUpRecipientOnAction() {
		int selectedIndex = tbReceiver.getSelectionModel().getSelectedIndex();
		if (selectedIndex == -1)
			return;

		if (selectedIndex >= 1) {
			ObservableList<MailReceiver> items = tbReceiver.getItems();

			MailReceiver a = items.get(selectedIndex);
			MailReceiver b = items.get(selectedIndex - 1);
			items.set(selectedIndex - 1, a);
			items.set(selectedIndex, b);
		}
	}

	@FXML
	public void btnCloseOnAction() {
		this.close();
	}
}
