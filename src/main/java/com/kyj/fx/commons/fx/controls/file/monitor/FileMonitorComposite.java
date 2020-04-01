/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.file.monitor
 *	작성일   : 2018. 8. 3.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.file.monitor;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.memory.StageStore;
import com.kyj.fx.commons.utils.DialogUtil;
import com.kyj.fx.commons.utils.FileWatchMonitor;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.fxloader.FxPostInitialize;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * @author KYJ
 *
 */
@FXMLController(value = "FileMonitorView.fxml", isSelfController = true)
public class FileMonitorComposite extends BorderPane implements Closeable {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileMonitorComposite.class);

	private ObjectProperty<FileWatchMonitor> fileWatcher = new SimpleObjectProperty<>();
	@FXML
	private TextField txtFilePath;
	@FXML
	private TextArea txtLog;
	@FXML
	private Label lblStatus;
	@FXML
	private Button btnStart;

	/**
	 */
	public FileMonitorComposite() {

		FxUtil.loadRoot(FileMonitorComposite.class, this, err -> {
			LOGGER.error(ValueUtil.toString(err));
		});
	}

	@FxPostInitialize
	public void after() {

	}

	@FXML
	public void initialize() {
		this.txtFilePath.textProperty().addListener((oba, o, n) -> {

			boolean isDisable = true;
			if (ValueUtil.isNotEmpty(n)) {
				File file = new File(n);
				if (file.exists() && file.isDirectory())
					isDisable = false;
			}

			this.btnStart.setDisable(isDisable);
		});
	}

	@FXML
	public void btnSearchOnAction() {
		File d = DialogUtil.showDirectoryDialog(StageStore.getPrimaryStage());
		if (d != null) {
			this.txtFilePath.setText(d.getAbsolutePath());
		}

	}

	@FXML
	public void btnStartOnAction() {
		String path = this.txtFilePath.getText();
		File file = new File(path);

		if (!file.exists()) {
			lblStatus.setText("Path does not exists.");
			return;
		}

		try {
			FileWatchMonitor fileWatcher = this.fileWatcher.get();
			if (fileWatcher != null) {
				fileWatcher.stop();
			}

			fileWatcher = new FileWatchMonitor();
			fileWatcher.setDir(file.toPath());
			fileWatcher.load();

			fileWatcher.addEvent((kind, p) -> {
				
				Platform.runLater(() -> {
					this.txtLog.appendText(kind.name() + " - " + p + "\n");
				});

			});

			Thread thread = new Thread(fileWatcher);
			thread.setName("File Monitor Thread.");
			thread.setDaemon(true);
			thread.start();

			lblStatus.setText("Monitor Started. ");
			this.fileWatcher.set(fileWatcher);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void btnStopOnAction() {
		FileWatchMonitor fileWatcher = this.fileWatcher.get();
		if (fileWatcher == null) {
			return;
		}

		try {
			fileWatcher.stop();
			this.btnStart.setDisable(false);
			lblStatus.setText("Monitor Stoped. ");
		} catch (IOException e) {
			DialogUtil.showExceptionDailog(e);
		}

	}

	@Override
	public void close() throws IOException {
		FileWatchMonitor fileWatchMonitor = this.fileWatcher.get();
		if (fileWatchMonitor != null)
			fileWatchMonitor.stop();
	}

}
