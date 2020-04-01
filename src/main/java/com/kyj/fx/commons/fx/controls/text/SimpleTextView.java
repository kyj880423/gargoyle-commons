/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.popup
 *	작성일   : 2015. 10. 19.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.functions.GargoyleTabPanable;
import com.kyj.fx.commons.functions.PrimaryStageCloseable;
import com.kyj.fx.commons.fx.controls.dock.tab.DockTab;
import com.kyj.fx.commons.fx.controls.dock.tab.DockTabPane;
import com.kyj.fx.commons.fx.controls.hex.HexTableViewComposite;
import com.kyj.fx.commons.fx.controls.mail.MailViewCompositeWrapper;
import com.kyj.fx.commons.fx.controls.mime.AbstractMimeAdapter;
import com.kyj.fx.commons.fx.controls.mime.AsynchWordExecutor;
import com.kyj.fx.commons.fx.controls.mime.HtmlTextToMimeAdapter;
import com.kyj.fx.commons.fx.controls.mime.MimeToHtmlAdapter;
import com.kyj.fx.commons.fx.controls.mime.NamoMimeToHtmlAdapter;
import com.kyj.fx.commons.fx.controls.mime.SimpleWordAdapter;
import com.kyj.fx.commons.memory.StageStore;
import com.kyj.fx.commons.models.KeyPair;
import com.kyj.fx.commons.threads.DemonThreadFactory;
import com.kyj.fx.commons.threads.ExecutorDemons;
import com.kyj.fx.commons.utils.DexConverter;
import com.kyj.fx.commons.utils.DialogUtil;
import com.kyj.fx.commons.utils.ExceptionHandler;
import com.kyj.fx.commons.utils.FileUtil;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.FxUtil.SaveAsModel;
import com.kyj.fx.commons.utils.GargoyleMultiOpenTabPolicy;
import com.kyj.fx.commons.utils.Hex;
import com.kyj.fx.commons.utils.JsonFormatter;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.utils.XMLFormatter;
import com.kyj.fx.fxloader.FXMLController;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.util.Pair;

/**
 *
 * 이 기능을 쓸때.. 대용량 텍스트를 읽어오는경우 멈춤현상이 있다.
 *
 * 데이터 길이가 짧은 텍스트 기반을 읽어오는 경우만 사용해야한다.
 *
 * @author KYJ
 *
 */
@FXMLController(value = "Simple-Menu-TextView.fxml", isSelfController = true)
public class SimpleTextView extends BorderPane implements PrimaryStageCloseable, GargoyleTabPanable, GargoyleMultiOpenTabPolicy {

	private static Logger LOGGER = LoggerFactory.getLogger(SimpleTextView.class);

	/**
	 * @최초생성일 2017. 10. 20.
	 */
	public static final String APP_NAME = "SimpleTextView";
	/**
	 * @최초생성일 2016. 10. 6.
	 */
	private static final String POSISION_FORMAT = "line : %d selectionStart : %d selectionEnd : %d column : %d  anchor : %d caret : %d";

	// private String content;
	private boolean showButtons;
	// private TextArea javaTextArea;
	protected CodeArea codeArea;

	protected CodeAreaHelper<CodeArea> helper;

	private ObjectProperty<Charset> encoding = new SimpleObjectProperty<>(StandardCharsets.UTF_8);
	@FXML
	private MenuItem miSaveAs;
	/**
	 * 버튼박스
	 */
	@FXML
	private HBox hboxButtons;

	@FXML
	private Button btnClose;

	@FXML
	private MenuBar mb;

	private Label lblLineInfo = new Label();

	/**
	 * @최초생성일 2019. 10. 21.
	 */
	private ObjectProperty<File> saveTemp = new SimpleObjectProperty<>();
	
	
	/**
	 * @param readFile
	 * @param showButtons
	 * @throws IOException
	 */
	public SimpleTextView(File readFile, boolean showButtons) throws IOException {
		this(FileUtils.readFileToString(readFile), showButtons);
		this.saveTemp.set(readFile);
	}

	public SimpleTextView() {
		this("", true, null);
	}

	public SimpleTextView(String content) {
		this(content, true, null);
	}

	public SimpleTextView(String content, boolean showButtons) {
		this(content, showButtons, null);
	}

	public SimpleTextView(String content, boolean showButtons, ExceptionHandler handler) {
		// this.content = content;
		this.showButtons = showButtons;

		FxUtil.loadRoot(SimpleTextView.class, this, err -> {
			if (handler == null) {
				err.printStackTrace();
			} else {
				handler.handle(err);
			}
		});
		if (content == null)
			codeArea.appendText("");
		else
			codeArea.appendText(content);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 21.
	 * @return
	 */
	public String getText() {
		return codeArea.getText();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 21.
	 * @param value
	 */
	public void setWrapText(boolean value) {
		codeArea.setWrapText(value);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 21.
	 * @param editable
	 */
	public void setEditable(boolean editable) {
		codeArea.setEditable(editable);
	}

	@FXML
	public void initialize() {
		codeArea = new GargoyleCodeArea();

		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
		codeArea.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// Tab 없이 처리되는 클래스들도 있음....
				DockTab tab2 = SimpleTextView.this.tab;
				if (tab2 != null) {
					if (tab2.getText().charAt(0) == '*') {
						return;
					}
					String modifyTabName = "*".concat(tab2.getText());
					tab2.setText(modifyTabName);
				}

			}
		});

		codeArea.setOnKeyPressed(this::codeAreaKeyClick);
		initHelpers();
		initGraphics();

		// miSaveAs.setAccelerator(new KeyCodeCombination(KeyCode.S,
		// KeyCombination.CONTROL_DOWN));

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 14.
	 * @param e
	 */
	protected void codeAreaKeyClick(KeyEvent e) {
		getHelper().codeAreaKeyClick(e);
	}

	/**
	 * 메뉴바 보임여부 결정
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 9. 29.
	 * @param visible
	 */
	public void setMenubarVisible(boolean visible) {
		this.mb.setVisible(visible);
	}

	protected void initHelpers() {
		this.helper = new CodeAreaHelper<CodeArea>(codeArea) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.kyj.fx.commons.fx.controls.text.CodeAreaHelper#
			 * codeAreaKeyClick(javafx.scene.input.KeyEvent)
			 */
			@Override
			public void codeAreaKeyClick(KeyEvent ev) {

				if (ev.getCode() == KeyCode.S && ev.isControlDown()) {
					if (ev.isConsumed())
						return;
					miSaveOnAction();
					ev.consume();
				} else if (ev.getCode() == KeyCode.N && ev.isControlDown()) {
					if (ev.isConsumed())
						return;

					ev.consume();
				} else {
					super.codeAreaKeyClick(ev);
				}

			}

		};
		this.helper.customMenuHandler(new CodeAreaCustomMenusHandler<CodeArea>() {

			@Override
			public void customMenus(CodeArea codeArea, ContextMenu contextMenu) {

				{
					Menu miMail = new Menu("Mail");
					MenuItem miSmtpMail = new MenuItem("SMTP Mail");
					miMail.getItems().add(miSmtpMail);

					miSmtpMail.setOnAction(ev -> {
						MailViewCompositeWrapper wrapper = new MailViewCompositeWrapper(codeArea.getSelectedText());
						FxUtil.createStageAndShow(wrapper, stage -> {
							stage.setTitle(MailViewCompositeWrapper.getName());
						});
						// SharedMemory.getSystemLayoutViewController().loadNewSystemTab(MailViewCompositeWrapper.getName(),
						// wrapper);

					});
					contextMenu.getItems().add(miMail);

				}

				{
					Menu menuAppicationCode = new Menu("Application Code");

					{
						MenuItem e = new MenuItem("Show Application StringBuffer Code [Java]");
						e.setOnAction(ev -> {
							FxUtil.EasyFxUtils.showJavaApplicationCode(codeArea.getSelectedText());
						});
						menuAppicationCode.getItems().add(e);

					}

					{
						MenuItem e = new MenuItem("Show Application List Code [Java]");
						e.setOnAction(ev -> {
							FxUtil.EasyFxUtils.showJavaApplicationCode(codeArea.getSelectedText());
						});
						menuAppicationCode.getItems().add(e);
					}

					{
						MenuItem e = new MenuItem("Show Application Code [C#]");
						e.setOnAction(ev -> {
							FxUtil.EasyFxUtils.showDotNetApplicationCode(codeArea.getSelectedText());
						});
						menuAppicationCode.getItems().add(e);
					}

					{
						MenuItem e = new MenuItem("Show Application Code [VBS]");
						e.setOnAction(ev -> {
							FxUtil.EasyFxUtils.showVbsApplicationCode(codeArea.getSelectedText());
						});
						menuAppicationCode.getItems().add(e);
					}

					contextMenu.getItems().add(menuAppicationCode);
				}

				{
					MenuItem e = new MenuItem("Open With XML Viewer");
					e.setOnAction(ev -> {
						// codeArea.getSelectedText()
						XMLEditor xmlEditor = new XMLEditor();
						// SharedMemory.getSystemLayoutViewController().loadNewSystemTab("XML
						// Viewer", xmlEditor);

						FxUtil.createStageAndShow(xmlEditor, stage -> {
							stage.setTitle("XML Viewer");
						});

						String selectedText = codeArea.getSelectedText();
						if (selectedText.isEmpty()) {
							xmlEditor.setText(codeArea.getText());
						} else {
							xmlEditor.setText(selectedText);
						}

					});
					contextMenu.getItems().add(e);
				}

			}
		});
	}

	public final CodeAreaHelper<CodeArea> getHelper() {
		return this.helper;
	}

	protected void initGraphics() {
		hboxButtons.setVisible(showButtons);
		if (!showButtons) {
			hboxButtons.setMinHeight(0d);
			hboxButtons.setMaxHeight(0d);
			hboxButtons.setPrefHeight(0d);
		}

		codeArea.selectionProperty().addListener((oba, oldval, newval) -> {
			int start = newval.getStart();
			int end = newval.getEnd();
			int caretColumn = codeArea.getCaretColumn();

			String format = String.format(POSISION_FORMAT, codeArea.getCurrentParagraph() + 1, start + 1, end + 1, caretColumn + 1,
					codeArea.getAnchor(), codeArea.getCaretPosition());

			lblLineInfo.setText(format);
		});

		lblLineInfo.setPrefHeight(USE_COMPUTED_SIZE);
		lblLineInfo.setMinHeight(USE_COMPUTED_SIZE);
		lblLineInfo.setMaxHeight(USE_COMPUTED_SIZE);

		VirtualizedScrollPane<CodeArea> scrollPane = new VirtualizedScrollPane<>(codeArea);
		setCenter(scrollPane);
		setBottom(lblLineInfo);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.voeditor.visual.framework.PrimaryStageCloseable#closeRequest()
	 */
	@Override
	public void closeRequest() {
	}

	/**
	 * @return the helper
	 */
	// public final CodeAreaHelper<CodeArea> getHelper() {
	// return helper;
	// }

	/**
	 * 마임 형태를 웹형태로 바꿔서 뷰를 보여줌
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 29.
	 */
	@FXML
	public void miOpenNamoMsWordOnAction() {
		String content = codeArea.getText();
		try {
			AsynchWordExecutor executor = new AsynchWordExecutor(
					/* new HtmlTextToMimeAdapter(content) */ new HtmlTextToMimeAdapter(content));
			executor.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Mime 데이터타입을 Html컨텐츠형태로 조회하기 위한 웹 뷰를 오픈함.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 29.
	 */
	@FXML
	public void miOpenNamoWebViewOnAction() {

		String content = codeArea.getText();

		try {

			javafx.application.Platform.runLater(() -> {
				try {

					AbstractMimeAdapter adapter = new NamoMimeToHtmlAdapter(content);
					// FileWriter fileWriter = new FileWriter(new
					// File("sample.html"));
					String html = adapter.getContent();

					FxUtil.openBrowser(codeArea, html, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void miOpenHtmlWordOnAction() {
		String content = codeArea.getText();
		try {
			AsynchWordExecutor executor = new AsynchWordExecutor(new SimpleWordAdapter(content));
			executor.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void miOpenHtmlWevViewOnAction() {
		String content = codeArea.getText();

		try {
			WebView webView = new WebView();
			webView.getEngine().loadContent(content);

			FxUtil.createStageAndShow(webView, stage -> {
				stage.setAlwaysOnTop(true);
				stage.initOwner(getScene().getWindow());
				stage.focusedProperty().addListener((oba, o, n) -> {
					if (!n)
						stage.close();

				});
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	@FXML
	public void miSaveOnAction() {
		File file = saveTemp.get();
		if (file == null || !file.exists()) {
			miSaveAsOnAction();
		} else {
			try {

				FileUtil.writeFile(file, this.codeArea.getText(), encoding.get());
				if (tab != null) {
					tab.setText(file.getName());
				}
				
				DialogUtil.showMessageDialog("Save Complete.");
				
			} catch (IOException e) {
				DialogUtil.showExceptionDailog(e);
			}
		}
	}

	/**
	 * menuItem 다른이름으로 저장.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 27.
	 */
	@FXML
	public void miSaveAsOnAction() {

		File saveAsFx = FxUtil.saveAsFx(getScene().getWindow(), new SaveAsModel() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.kyj.fx.commons.utils.FxUtil.SaveAsModel#getEncoding()
			 */
			@Override
			public Charset getEncoding() {
				return encoding.get();
			}

			@Override
			public String getContent() {
				return codeArea.getText();
			}

			@Override
			public Consumer<Exception> onError() {
				return ex -> {
					DialogUtil.showExceptionDailog(ex);
				};
			}

			@Override
			public void onSuccess(File f) {
				if (tab != null) {
					tab.setText(f.getName());
				}

			}

		});
		saveTemp.set(saveAsFx);

		// File saveAs =
		// DialogUtil.showFileSaveCheckDialog(getScene().getWindow(),
		// chooser->{});
		// if(saveAs!=null && saveAs.exists())
		// {
		// FileUtil.writeFile(saveAs, codeArea.getText(),
		// Charset.forName("UTF-8"), err ->
		// LOGGER.error(ValueUtil.toString(err)));
		// }
	}

	public CodeArea getCodeArea() {
		return this.codeArea;
	}

	@FXML
	public void miBase64EncodeOnAction() {
		String text = codeArea.getText();
		String encodeToString = Base64.getEncoder().encodeToString(text.getBytes(encoding.get()));
		codeArea.getUndoManager().mark();
		codeArea.replaceText(encodeToString);
	}

	@FXML
	public void miBase64DecodeOnAction() {
		try {
			String text = codeArea.getText();
			byte[] b = Base64.getDecoder().decode(text.getBytes(encoding.get()));
			codeArea.getUndoManager().mark();
			codeArea.replaceText(new String(b, encoding.get()));
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
			DialogUtil.showMessageDialog(FxUtil.getWindow(this), "Invalide Base64 Content.");
		}

	}

	/**
	 * Mime 텍스트를 HTML 텍스트로 변환
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 6. 14.
	 */
	@FXML
	public void miToHtmlCodeOnAction() {

		try {
			MimeToHtmlAdapter adapter = new MimeToHtmlAdapter(this.codeArea.getText());
			String content = adapter.getContent();
			codeArea.getUndoManager().mark();
			codeArea.replaceText(content);

		} catch (UnsupportedEncodingException e) {
			LOGGER.error(ValueUtil.toString(e));
			DialogUtil.showMessageDialog(FxUtil.getWindow(this), "Unsupported Encoding. ");
		}

	}

	@FXML
	public void prettyFormatOnAction() {
		String stringPrettyFormat = new JsonFormatter().format(codeArea.getText());
		codeArea.getUndoManager().mark();
		codeArea.replaceText(stringPrettyFormat);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 3.
	 */
	@FXML
	public void removeSpaceJsonFormatOnAction() {
		String stringPrettyFormat = new JsonFormatter().removeSpaces(codeArea.getText());
		codeArea.getUndoManager().mark();
		codeArea.replaceText(stringPrettyFormat);
	}

	@FXML
	public void toStringOnAction() {
		codeArea.getUndoManager().mark();
		byte[] decode = Hex.decode(codeArea.getText());
		codeArea.replaceText(new String(decode, encoding.get()));
	}

	@FXML
	public void toHexOnAction() {
		codeArea.getUndoManager().mark();
		String str = codeArea.getText();
		codeArea.replaceText(String.valueOf(Hex.encode(str.getBytes(encoding.get()))));
	}

	@FXML
	public void miEncodeOnAction() {
		codeArea.getUndoManager().mark();
		String str = codeArea.getText();

		codeArea.replaceText(StringEscapeUtils.escapeJava(str));
	}

	@FXML
	public void miDecodeOnAction() {
		codeArea.getUndoManager().mark();
		String str = codeArea.getText();
		codeArea.replaceText(StringEscapeUtils.unescapeJava(str));
	}

	@FXML
	public void miShowAppCodeOnAction() {
		FxUtil.EasyFxUtils.showApplicationCode(this.getCodeArea().getText());
	}

	@FXML
	public void miRemoveSpaciesOnAction() {

		codeArea.getUndoManager().mark();
		String str = codeArea.getText();
		str = XMLFormatter.newInstnace().removeSpace(str);
		codeArea.replaceText(str);
	}

	/**
	 * XML 포멧 기능 확장
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 9. 8.
	 */
	@FXML
	public void prettyXmlFormatOnAction() {
		try {
			String format = XMLFormatter.newInstnace().formatWithAttributes(codeArea.getText());
			// String format =
			// XMLFormatter.newInstnace().format(codeArea.getText());
			codeArea.getUndoManager().mark();
			codeArea.replaceText(format);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 9.
	 */
	@FXML
	public void miURLEncodeOnAction() {
		try {
			String encode = URLEncoder.encode(codeArea.getText(), encoding.get().name());
			codeArea.getUndoManager().mark();
			codeArea.replaceText(encode);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 9.
	 */
	@FXML
	public void miURLDecodeOnAction() {
		try {
			String decode = URLDecoder.decode(codeArea.getText(), encoding.get().name());
			codeArea.getUndoManager().mark();
			codeArea.replaceText(decode);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	@FXML
	public void miOpenOnAction() {
		File file = DialogUtil.showFileDialog(chooser -> {
		});
		// encoding.get().name()
		if (file != null && file.exists()) {

			// LoadFileOptionHandler handler = new LoadFileOptionHandler();
			// handler.setEncoding(encoding.get().name());
			// this.codeArea.replaceText(FileUtil.readFile(file, handler));
			this.codeArea.replaceText(FileUtil.readConversion(file));

			this.saveTemp.set(file);

			if (this.tab != null) {
				tab.setText(file.getName());
			}

		}

	}

	private DockTabPane tabpane;

	@Override
	public void setTabPane(DockTabPane tabpane) {
		this.tabpane = tabpane;

	}

	private DockTab tab;

	@Override
	public void setTab(DockTab tab) {
		this.tab = tab;
	}

	/**
	 * 비동기로 수정 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 7. 3.
	 */
	@FXML
	public void openDexStringOnAction() {
		final File file = DialogUtil.showFileDialog(chooser -> {
		});

		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
			if (file != null && file.exists()) {
				try (InputStream in = new FileInputStream(file)) {
					String call = new DexConverter().toFormatString(in, encoding.get());
					this.codeArea.replaceText(call);
				} catch (IOException e) {
					this.codeArea.insertText(0, 0, ValueUtil.toString(e));
					LOGGER.error(ValueUtil.toString(e));
					DialogUtil.showExceptionDailog(e);
				}
			}
		});
	}

	/**
	 * 비동기로 수정 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 7. 3.
	 */
	@FXML
	public void openHexStringOnAction() {
		final File file = DialogUtil.showFileDialog(chooser -> {
		});
		
		if(file == null)
			return;
		
		HexTableViewComposite c = new HexTableViewComposite();
		FxUtil.createStageAndShow(c);
		c.setFileValue(file);

//		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
//
//			if (file != null && file.exists()) {
//				try (InputStream in = new FileInputStream(file)) {
//					String call = new HexConverter().toFormatString(in, encoding.get());
//
//					Platform.runLater(() -> {
//						this.codeArea.replaceText(call);
//					});
//
//				} catch (IOException e) {
//					this.codeArea.insertText(0, 0, ValueUtil.toString(e));
//					LOGGER.error(ValueUtil.toString(e));
//					DialogUtil.showExceptionDailog(e);
//				}
//			}
//
//		});
	}

	@FXML
	public void rmiUTF8OnAction() {
		encoding.set(StandardCharsets.UTF_8);
	}

	@FXML
	public void rmiUTF16LEOnAction() {
		encoding.set(StandardCharsets.UTF_16LE);
	}

	/**
	 * 텍스트 전후로 특정 텍스트를 붙여넣는 작업 수행 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 11.
	 */
	@FXML
	public void miTextAppendingOnAction() {

		Optional<Pair<String, HashMap<String, Object>>> showMultiInputDialog = DialogUtil.showMultiInputDialog(StageStore.getPrimaryStage(),
				"Input", "Input Values", KeyPair.stringValues("preffix", "", "suffix", ""));

		showMultiInputDialog.ifPresent(pairs -> {

			DemonThreadFactory.newInstance().newThread(() -> {
				HashMap<String, Object> value = pairs.getValue();
				Object preffix = value.get("preffix");
				Object suffix = value.get("suffix");

				String text = this.codeArea.getText();
				String[] texts = text.split("\n");
				StringBuilder sb = new StringBuilder();
				for (String str : texts) {

					if (preffix != null)
						sb.append(preffix);

					sb.append(str);

					if (suffix != null)
						sb.append(suffix);

					sb.append(System.lineSeparator());
				}
				return sb.toString();
			}, onSuccess, onError).start();

		});

	}

	Consumer<Object> onSuccess = str -> {
		Platform.runLater(() -> {
			this.codeArea.replaceText(str.toString());
		});

	};

	ExceptionHandler onError = err -> {
		Platform.runLater(() -> {
			DialogUtil.showExceptionDailog(err);
		});
	};

	/**
	 * 멀티라인으로 구성된 텍스트르 하나의 라인으로 구분자를 추가하여 만드는 작업 수행
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 11.
	 */
	@FXML
	public void miTextReduceOnAction() {

		Optional<Pair<String, String>> showInputDialog = DialogUtil.showInputDialog("Input Separator Text", "Input Separator Text");

		showInputDialog.ifPresent(pair -> {

			final String separator = pair.getValue();

			String text = this.codeArea.getText();

			DemonThreadFactory.newInstance().newThread(() -> {
				String[] texts = text.split("\n");
				StringBuilder sb = new StringBuilder();
				for (String t : texts) {
					sb.append(t).append(separator);
				}
				return sb.toString();
			}, onSuccess, onError).start();
		});

	}

}
