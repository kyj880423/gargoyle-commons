/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2017. 1. 6.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.StandardOpenOption;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.memory.ResourceLoader;
import com.kyj.fx.commons.threads.DemonTimerFactory;
import com.kyj.fx.commons.utils.DialogUtil;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.fxloader.FxPostInitialize;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;

/**
 * @author KYJ
 *
 */
@FXMLController(value = "LogView.fxml")
public class LogViewController implements Closeable {

	/**
	 * @최초생성일 2017. 1. 9.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LogViewController.class);

	private int seekSize = 3 * 1024 * 1024;

	private LogViewComposite composite;
	@FXML
	private CodeArea txtLog;

	private FileChannel fileChannel;

	@FXML
	private ToggleGroup ENCODING;
	@FXML
	private ToggleButton btnScrollLock;
	@FXML
	private Button btnStart;
	@FXML
	private Label lblStatus, lblTimerStatus;

	private CodeAreaHelper<CodeArea> helper;

	private BooleanProperty isScrollLock = new SimpleBooleanProperty();

	public LogViewController() throws Exception {

	}

	private ByteBuffer buffer;
	// Default Encoding UTF-8
	private ObjectProperty<Charset> charset = new SimpleObjectProperty<>();

	/**
	 * 인코딩 변경.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 1. 12.
	 * @param charset
	 */
	public void setCharset(Charset charset) {
		this.charset.set(charset);
	}

	@FXML
	public void initialize() {
		
		VirtualizedScrollPane<CodeArea> scrollPane = new VirtualizedScrollPane<>(txtLog);
		helper = new CodeAreaHelper<CodeArea>(txtLog) {

			@Override
			protected void init() {
				findAndReplaceHelper = new CodeAreaFindAndReplaceHelper<CodeArea>(codeArea);
				createMenus();
			}

		};
		txtLog.addEventHandler(KeyEvent.KEY_PRESSED, this::txtLogOnKeyPress);
		this.isStared.addListener((oba, o, n) -> {

			Platform.runLater(() -> {
				if (n) {
					lblStatus.setText("Started");
					btnStart.setDisable(true);
				} else {
					lblStatus.setText("Stopped");
					btnStart.setDisable(false);
				}
			});

		});

		btnStart.setOnAction(ev -> {
			start();
		});

		txtLog.setParagraphGraphicFactory(LineNumberFactory.get(txtLog));

		createContextMenu();
	}

	protected void createContextMenu() {
//		ContextMenu contextMenu = helper.getContextMenu();

		// txtLog.setContextMenu(contextMenu);

//		MenuItem e = new MenuItem("EMRService Log Viewer");
//		e.setOnAction(ev -> {
//			EMRServiceLogTableViewHelper helper = new EMRServiceLogTableViewHelper(this.txtLog.getText());
//
//			FxUtil.createStageAndShow(helper.getView(), stage -> {
//				stage.initOwner(FxUtil.getWindow(composite.getParent()));
//				stage.setTitle("EMRService Log Viewer");
//				stage.setWidth(1200d);
//				stage.setHeight(800d);
//			});
//
//			helper.start();
//		});
//		contextMenu.getItems().add(e);

	}

	/**
	 * txt 찾기 바꾸기 키 이벤트.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 1. 24.
	 * @param e
	 */
	public void txtLogOnKeyPress(KeyEvent e) {
		if (KeyCode.F == e.getCode() && e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {
			if (!e.isConsumed()) {
				helper.findAndReplaceHelper.findReplaceEvent(new ActionEvent());
				e.consume();
			}
		}
	}

	@FxPostInitialize
	public void after() {
		String encoding = ResourceLoader.loadCharset();
		if (Charset.isSupported(encoding)) {
			charset.set(Charset.forName(encoding));
		} else {
			LOGGER.info("does not supported encoding {} , default utf-8 setting. ", encoding);
			encoding = "UTF-8";
			charset.set(Charset.forName(encoding));
		}
		// 설정에 저장된 인코딩셋을 불러와 디폴트로 선택되게함.
		ObservableList<Toggle> toggles = ENCODING.getToggles();
		toggles.stream().map(tg -> {

			if (tg instanceof RadioMenuItem) {
				RadioMenuItem r = (RadioMenuItem) tg;
				if (r.getText().toUpperCase().equals(charset.get().name().toUpperCase())) {
					return r;
				}
			}
			return null;
		}).filter(v -> v != null).findFirst().ifPresent(rmi -> {
			rmi.setSelected(true);
		});

		// 캐릭터셋이 변경될때 환경변수에 등록하는 과정
		this.charset.addListener((oba, o, newCharset) -> {
			String name = newCharset.name();
			if (ValueUtil.isEmpty(name)) {
				return;
			}
			ResourceLoader.saveCharset(name);

		});

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 9. 17.
	 * @throws IOException
	 */
	public void createChannel() throws IOException {
		File monitoringFile = composite.getMonitoringFile();

		if (fileChannel != null) {
			fileChannel.close();
			fileChannel = null;
		}

		fileChannel = FileChannel.open(monitoringFile.toPath(), StandardOpenOption.READ);
		buffer = ByteBuffer.allocate(seekSize);
	}

	/**
	 * 마지막에 읽어들인 byte 위치를 임시저장한다. 저장된 위치를 기준으로 파일이 변경되면 그 위치부터 새롭게 추가된 텍스트를
	 * 읽어온다.
	 * 
	 * @최초생성일 2017. 1. 11.
	 */
	private LongProperty mark = new SimpleLongProperty(-1L);

	private void readAction(long lastModified) {
		try {

			int byteCount;

			long length = composite.getMonitoringFile().length();
			int totalPage = (int) (length / seekSize) - (length % seekSize > 0 ? 0 : 1);

			long lastMarkedPosition = mark.get();
			if (lastMarkedPosition <= 0)
				fileChannel.position(totalPage * seekSize);
			else
				fileChannel.position(lastMarkedPosition);

			// 미리 저장된 포지션이후의 모든 텍스트를 읽어들이기 위한 do~while문
			do {
				byteCount = fileChannel.read(buffer);

				if (byteCount != -1) {

					long position = fileChannel.position();
					mark.set(position);

					buffer.flip();

					onModified(charset.get().decode(buffer).toString());

					buffer.clear();

				}

			} while (byteCount != -1);

		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	/**
	 * 파일이 변경되었는지 감시하기 위한 Timer , 디폴트값은 0.5초마다 체크.
	 * 
	 * @최초생성일 2017. 1. 12.
	 */
	private Timer fileWatcher;
	/**
	 * 파일의 내용을 읽어들일때 마지막으로 수정된 일자를 체크하기 위한 변수
	 * 
	 * @최초생성일 2017. 1. 12.
	 */
	private LongProperty lastModifiedTime = new SimpleLongProperty(-1L);
	/**
	 * 파일의 내용을 읽어들일때 마지막으로 읽은 파일의 크기를 체크하기위한 변수
	 * 
	 * @최초생성일 2017. 1. 12.
	 */
	private LongProperty lastLength = new SimpleLongProperty(-1L);

	private BooleanProperty isStared = new SimpleBooleanProperty(false);

	public boolean isStarted() {
		return isStared.get();
	}

	private AtomicBoolean isRunning = new AtomicBoolean(false);

	public boolean isRunning() {
		return isRunning.get();
	}

	private void watchFile() {

		if (fileWatcher != null)
			fileWatcher.cancel();

		if (null == fileWatcher) {

			fileWatcher = DemonTimerFactory.newInsance("Gargoyle Log Monitor Timer");// new
																						// Timer();

			TimerTask task = new TimerTask() {

				private int failCount;

				@Override
				public void run() {

					if (composite.getMonitoringFile() == null)
						return;

					if (fileChannel == null)
						return;

					long lastModified = composite.getMonitoringFile().lastModified();
					long lastReadTime = lastModifiedTime.get();

					long space = -1L;
					try {
						boolean open = fileChannel.isOpen();
						// LOGGER.debug("{}", open);
						Platform.runLater(() -> {
							lblTimerStatus.setText(open ? "Active" : "inActive");
						});
						space = fileChannel.size();
					} catch (IOException e) {
						failCount++;
						LOGGER.error(ValueUtil.toString(e));
						if (failCount > 10) {

							Platform.runLater(() -> {
								lblTimerStatus.setText("inActive");
								DialogUtil.showMessageDialog("Can't Connect Nerwork. ");
							});

							this.cancel();

							try {
								close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}

						}

					}

					// Debug.
					// LOGGER.debug(" file space : " + space + " // cached space
					// : " + lastLength.get());

					/*
					 * 처음에는 수정된 일짜 정보 기준으로 처리하였으나, OS영역에서 파일 내용이 변경되어도 반영이 잘
					 * 이루어지지않음. 그래서 파일 사이즈 기준으로 변경유무를 추가. lastReadTime == -1 의
					 * 조건은 최초에 읽어들인경우 파일 내용을 읽어들이기 위한 조건처리이다.
					 */
					if (lastReadTime == -1 || (space != lastLength.get()) || (lastModified > lastReadTime)) {

						// Debug
						// System.out.println("work ## ");

						readAction(lastModified);

						lastLength.set(space);
						lastModifiedTime.set(lastModified);

					}
					isRunning.lazySet(true);

				}
			};
			fileWatcher.scheduleAtFixedRate(task, 0, watchDelayTimeMills());

		}
	}

	/**
	 * 파일의 변화를 감지하기위해 대기하는 시간. 단위 millsSecond
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 1. 13.
	 * @return
	 */
	protected long watchDelayTimeMills() {
		return 500;
	}

	// private ObservableList<Chagne> onChangeListener =
	// FXCollections.observableArrayList();

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 1. 10.
	 * @param string
	 */
	private void onModified(String string) {

		Platform.runLater(() -> {

			this.txtLog.appendText(string /* , !isScrollLock.get() */);
//			this.txtLog.moveTo(this.txtLog.getParagraphs().size());
//			this.txtLog.scrollYBy(100d);
//			txtLog.requestFollowCaret();
		});

		/* Create Change Model */
		// Chagne chg = new Chagne();
		// chg.setContent(string);
		// onChangeListener.forEach(v -> {
		//
		// });
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 1. 6.
	 * @param logViewComposite
	 */
	public void setComposite(LogViewComposite composite) {
		this.composite = composite;
	}

	/**
	 * file close
	 * 
	 * @throws IOException
	 * @작성자 : KYJ
	 * @작성일 : 2017. 1. 9.
	 */
	@Override
	public void close() throws IOException {
		LOGGER.debug("Close Reqeust.");
		isStared.set(false);
		isRunning.set(false);
		// monitorFile.close();
		if (fileWatcher != null)
			fileWatcher.cancel();

		if (fileChannel != null) {
			fileChannel.close();
		}

		if (buffer != null) {
			buffer.flip();
			buffer.clear();
			buffer = null;
		}
		fileWatcher = null;
		LOGGER.debug("Close Complate!");
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 1. 11.
	 */
	public void start() {

		if (isRunning.get())
			throw new RuntimeException("Already started. ");

		Platform.runLater(() -> {

			try {
				createChannel();
			} catch (IOException e) {
				e.printStackTrace();
			}

			isStared.set(true);
			watchFile();

		});
		// Thread thread = new Thread(() -> {
		//
		//
		//
		// });
		//
		// thread.setDaemon(true);
		// thread.start();

	}

	@FXML
	public void rmiUtf8OnAction() {
		this.setCharset(Charset.forName("UTF-8"));
	}

	@FXML
	public void rmiEucKr8OnAction() {
		this.setCharset(Charset.forName("EUC-KR"));
	}

	@FXML
	public void miClearOnActionAction() {
		this.txtLog.clear();
	}

	/**
	 * 부모 Stage 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 1. 13.
	 * @return
	 */
	Window getWindow() {
		return this.composite.getParent().getScene().getWindow();
	}

	@FXML
	public void miSaveAsOnAction() {
		FxUtil.saveAsFx(getWindow(), () -> txtLog.getText());
	}

	public static class Chagne {
		private String content;

		/**
		 * @return the content
		 */
		public final String getContent() {
			return content;
		}

		/**
		 * @param content
		 *            the content to set
		 */
		final void setContent(String content) {
			this.content = content;
		}

	}

	@FXML
	public void btnScrollLockOnAction() {
		boolean value = !isScrollLock.get();
		isScrollLock.set(value);
	}

}
