/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.kyj.fx.commons.fx.controls.dialog.BaseDialogComposite;
import com.kyj.fx.commons.fx.controls.dialog.ExceptionDialogComposite;
import com.kyj.fx.commons.memory.StageStore;
import com.kyj.fx.commons.models.KeyPair;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;

/**
 * @author KYJ
 *
 */
public class DialogUtil {

	public static final String MEMO_LOCAL_USER = "memo.local.user";

	public static Optional<Pair<String, String>> showInputDialog(Node owner, String title, String message) {
		return showInputDialog(owner.getScene().getWindow(), title, message, "", null);
	}

	public static Optional<Pair<String, String>> showInputDialog(Node owner, String title, String message, Predicate<String> satisfied) {
		return showInputDialog(FxUtil.getWindow(owner), title, message, "", satisfied);
	}

	public static Optional<Pair<String, String>> showInputDialog(Window owner, String title, String message) {
		return showInputDialog(owner, title, message, "", null);
	}

	public static Optional<Pair<String, String>> showInputDialog(String title, String message) {
		return showInputDialog(null, title, message, "", null);
	}

	public static Optional<Pair<String, String>> showInputDialog(String title, String message, String inputValue) {
		return showInputDialog(null, title, message, inputValue, null);
	}

	public static Optional<Pair<String, String>> showInputDialog(Window owner, String title, String message, String inputValue,
			Predicate<String> satisfied) {

		BaseDialogComposite composite = new BaseDialogComposite(title, message);
		Button btnOk = new Button("OK");
		btnOk.setMinWidth(80d);
		Button btnCancel = new Button("Cancel");
		btnCancel.setMinWidth(80d);
		composite.addButton(btnOk);
		composite.addButton(btnCancel);

		TextField text = new TextField();
		if (ValueUtil.isNotEmpty(inputValue)) {
			text.setText(inputValue);
		}

		text.setOnAction(ev -> {

			btnOk.fireEvent(FxUtil.mouseEventForDummy());

		});

		composite.setGraphic(text);
		Optional<Pair<String, String>> empty = Optional.empty();
		SimpleObjectProperty<Optional<Pair<String, String>>> prop = new SimpleObjectProperty<>(empty);

		// Modal
		composite.show(owner, stage -> {
			stage.initModality(Modality.APPLICATION_MODAL);
			text.requestFocus();

			text.addEventHandler(KeyEvent.KEY_RELEASED, ev -> {
				if (ev.getCode() == KeyCode.ENTER) {

					Optional<Pair<String, String>> pair = Optional.of(new Pair<>("OK", text.getText()));
					prop.set(pair);

					if (satisfied != null) {

						if (satisfied.test(text.getText())) {
							stage.close();
						}
					}

				} else {

					if (satisfied != null) {
						if (satisfied.test(text.getText())) {
							btnOk.setDisable(false);
						} else
							btnOk.setDisable(true);

					}
				}
			});

			btnOk.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
				Optional<Pair<String, String>> pair = Optional.of(new Pair<>("OK", text.getText()));
				prop.set(pair);
				stage.close();
			});

			btnCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
				stage.close();
			});

		});

		return prop.get();
	}

	
	/**
	 * 여러개의 입력값을 받기 위한 다이얼로그 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 11.
	 * @param owner
	 * @param title
	 * @param message
	 * @param pairs
	 * @return
	 */
	public static Optional<Pair<String, HashMap<String, Object>>> showMultiInputDialog(Window owner, String title, String message,
			List<KeyPair<String, Object>> pairs) {
		return showMultiInputDialog(owner, title, message, pairs , a-> true, (a, b) -> true);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 19. 
	 * @param owner
	 * @param title
	 * @param message
	 * @param pairs
	 * @param validation
	 * @return
	 */
	public static Optional<Pair<String, HashMap<String, Object>>> showMultiInputDialog(Window owner, String title, String message,
			List<KeyPair<String, Object>> pairs, BiFunction<String,String, Boolean> validation) {
		return showMultiInputDialog(owner, title, message, pairs , a-> true, validation);
	}
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 19. 
	 * @param owner
	 * 			parent stage
	 * @param title
	 * @param message
	 * @param pairs	
	 * 				Key-Value pair.
	 * @param isFieldUse	
	 * 					KeyPair에서 해당 필드를 사용할지 여부 결정 
	 * @param validation
	 * 					값이 유효하면 true, 그렇지 않으면 false 를 리턴하게 설정 
	 * @return
	 */
	public static Optional<Pair<String, HashMap<String, Object>>> showMultiInputDialog(Window owner, String title, String message,
			List<KeyPair<String, Object>> pairs,  Predicate<String> isFieldUse, BiFunction<String,String, Boolean> validation) {

		BaseDialogComposite composite = new BaseDialogComposite(title, message);
		Button btnOk = new Button("OK");
		btnOk.setMinWidth(80d);
		Button btnCancel = new Button("Cancel");
		btnCancel.setMinWidth(80d);
		composite.addButton(btnOk);
		composite.addButton(btnCancel);

		VBox vBox = new VBox();
		if (pairs != null) {

			for (KeyPair<String, Object> pair : pairs) {

				String lblText = pair.getKey();
				Object defValueText = pair.getValue();

				if(!isFieldUse.test(lblText))
					continue;
				
				Label label = new Label(lblText);
				label.setMinWidth(120d);
				TextField textField = new TextField(defValueText == null ? "" : defValueText.toString());
				textField.setMinWidth(200d);
				HBox hBox = new HBox(5, label, textField);
				vBox.getChildren().add(hBox);

			}

			composite.setGraphic(vBox);

		}

		Optional<Pair<String, HashMap<String, Object>>> empty = Optional.empty();
		SimpleObjectProperty<Optional<Pair<String, HashMap<String, Object>>>> prop = new SimpleObjectProperty<>(empty);

		// Modal
		composite.show(owner, stage -> {
			stage.initModality(Modality.APPLICATION_MODAL);

			btnOk.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {

				ObservableList<Node> children = vBox.getChildren();
				// List<KeyPair<String, String>> values = new
				// ArrayList<>(children.size());
				HashMap<String, Object> hashMap = new HashMap<>();
				for (Node n : children) {
					HBox hbox = (HBox) n;
					ObservableList<Node> children2 = hbox.getChildren();
					Label l = (Label) children2.get(0);
					TextField t = (TextField) children2.get(1);
					
					if(!validation.apply(l.getText(), t.getText()))
					{
						Rectangle value = new Rectangle(5, 5, Color.RED);
						l.setGraphic(value);
						Tooltip.install(value, new Tooltip("Essential"));
						return;
					}
					else
					{
						hashMap.put(l.getText(), t.getText());
						l.setGraphic(null);
					}
						
				}

				Optional<Pair<String, HashMap<String, Object>>> pair = Optional.of(new Pair<>("Y", hashMap));
				prop.set(pair);
				stage.close();
			});

			btnCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
				stage.close();
			});

		});

		return prop.get();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 1. 31.
	 * @return
	 */
	public static Consumer<Exception> exceptionHandler() {
		return err -> {
			DialogUtil.showExceptionDailog(err);
		};
	}

	public static void showExceptionDailog(Throwable ex) {
		showExceptionDailog(null, ex, "The exception stacktrace was:\n" + ValueUtil.toString(ex));
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 26.
	 * @param owner
	 * @param ex
	 */
	public static void showExceptionDailog(Window owner, Throwable ex) {
		showExceptionDailog(owner, ex, ex.getMessage(), ValueUtil.toString(ex));
	}

	/**
	 * Exception Dialog 예외가 발생햇을때 표시할 다이얼로그
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 8. 22.
	 * @param owner
	 * @param ex
	 * @param message
	 */
	public static void showExceptionDailog(Node owner, Throwable ex, String message) {
		showExceptionDailog(getWindow(owner), ex, ex.getMessage(), message);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 26.
	 * @param owner
	 * @param ex
	 * @param title
	 * @param message
	 */
	public static void showExceptionDailog(Node owner, Throwable ex, String title, String message) {
		showExceptionDailog(getWindow(owner), ex, title, message);
	}

	private static Window getWindow(Node owner) {
		Window _owner = null;
		if (owner != null) {
			Scene scene = owner.getScene();
			if (scene != null) {
				_owner = scene.getWindow();
			}
		}

		if (_owner == null) {
			_owner = StageStore.getPrimaryStage();
		}
		return _owner;
	}

	/**
	 * Exception Dialog 예외가 발생햇을때 표시할 다이얼로그
	 *
	 * @param ex
	 */
	public static void showExceptionDailog(Window owner, Throwable ex, String title, String message) {
		/*
		 * 2016-08-23 기존에 사용하던 에러 다이얼로그는 사용되지않음.
		 *
		 * 이유는 팝업에 대한 우선순위 핸들링처리가 불가.
		 *
		 * ex) A팝업이 화면에 떠있는 상태에서 , 또 다른 B 팝업이 뜬 상태에서 에러 다이얼로그가 보여지는경우
		 *
		 * 연관성이 없는 A팝업이 화면 최상단으로 올라오는 버그가 있음. by kyj.
		 *
		 *
		 */
		// Alert alert = new Alert(AlertType.ERROR);
		// alert.setTitle("Exception Dialog");
		//
		// alert.setHeaderText(message);
		// alert.setContentText(ex.getMessage());
		//
		// // Create expandable Exception.
		// StringWriter sw = new StringWriter();
		// PrintWriter pw = new PrintWriter(sw);
		// ex.printStackTrace(pw);
		// String exceptionText = sw.toString();
		//
		// Label label = new Label(message);
		//
		// TextArea textArea = new TextArea(exceptionText);
		// textArea.setEditable(false);
		// textArea.setWrapText(true);
		//
		// textArea.setMaxWidth(Double.MAX_VALUE);
		// textArea.setMaxHeight(Double.MAX_VALUE);
		// GridPane.setVgrow(textArea, Priority.ALWAYS);
		// GridPane.setHgrow(textArea, Priority.ALWAYS);
		//
		// GridPane expContent = new GridPane();
		// expContent.setMaxWidth(Double.MAX_VALUE);
		// expContent.add(label, 0, 0);
		// expContent.add(textArea, 0, 1);
		//
		// alert.getDialogPane().setExpandableContent(expContent);
		// alert.initOwner(owner);
		// alert.showAndWait();

		Platform.runLater(() -> {
			new ExceptionDialogComposite(ex, title, message).show(owner);
		});

	}

	public static File showFileDialog(Consumer<FileChooser> option) {
		Stage s = new Stage();
		s.initOwner(StageStore.getPrimaryStage());
		File showFileDialog = showFileDialog(s, option);
		s.close();
		return showFileDialog;
	}

	public static File showFileDialog() {

		File f = showFileDialog(StageStore.getPrimaryStage(), option -> {
		});

		return f;
	}

	public static File showFileDialog(final Window ownerWindow) {
		return showFileDialog(ownerWindow, option -> {
		});
	}

	/**
	 * 파일다이얼로그 오픈
	 *
	 * @Date 2015. 10. 12.
	 * @param ownerWindow
	 * @param option
	 * @return
	 * @User KYJ
	 */
	public static File showFileDialog(final Window ownerWindow, Consumer<FileChooser> option) {
		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Open Resource File");

		installDefaultPath(fileChooser);
		option.accept(fileChooser);

		File file = fileChooser.showOpenDialog(ownerWindow);
		applyLastPath(file);
		return file;
	}

	private static void applyLastPath(File file) {
		if (file != null && file.exists())
			PreferencesUtil.getDefault().put(PreferencesUtil.KEY_LAST_SELECTED_PATH, file.getAbsolutePath());
	}

	/********************************
	 * 작성일 : 2016. 6. 19. 작성자 : KYJ
	 *
	 *
	 * 최근 설정했는 경로로 세팅.
	 *
	 *
	 * @param fileChooser
	 ********************************/
	public static void installDefaultPath(FileChooser fileChooser) {
		File initDir = getInitDir();
		if (initDir != null) {
			if (initDir.isDirectory()) {
				if (fileChooser != null) {
					fileChooser.setInitialDirectory(initDir);
				}
			} else {
				File parentFile = initDir.getParentFile();
				if (fileChooser != null) {
					fileChooser.setInitialDirectory(parentFile);
				}
			}
		}

	}

	public static void installDefaultPath(DirectoryChooser fileChooser) {
		File initDir = getInitDir();
		if (initDir != null) {
			if (initDir.isDirectory()) {
				if (fileChooser != null) {
					fileChooser.setInitialDirectory(initDir);
				}
			} else {
				File parentFile = initDir.getParentFile();
				if (fileChooser != null) {
					fileChooser.setInitialDirectory(parentFile);
				}
			}
		}
	}

	public static File getInitDir() {
		String path = PreferencesUtil.getDefault().get(PreferencesUtil.KEY_LAST_SELECTED_PATH, "");
		if (ValueUtil.isNotEmpty(path)) {
			File file = new File(path);
			if (file.exists()) {
				if (file.isDirectory())
					return file;
				else
					return file.getParentFile();
			}
		}
		return null;
	}

	/**
	 * 멀티 파일 다이얼로그 오픈
	 *
	 * @Date 2015. 10. 12.
	 * @param ownerWindow
	 * @param option
	 * @return
	 * @User KYJ
	 */
	public static List<File> showMultiFileDialog(final Window ownerWindow, Consumer<FileChooser> option) {
		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Open Resource File");

		installDefaultPath(fileChooser);
		option.accept(fileChooser);

		List<File> files = fileChooser.showOpenMultipleDialog(ownerWindow);

		if (files == null || files.isEmpty())
			return Collections.emptyList();

		applyLastPath(files.get(files.size() - 1));

		return files;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 1. 31.
	 * @return
	 */
	public static File showFileSaveCheckDialog() {
		File saveFile = showFileSaveDialog(StageStore.getPrimaryStage(), option -> {

		});
		return saveFile;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 1. 31.
	 * @param option
	 * @return
	 */
	public static File showFileSaveCheckDialog(Consumer<FileChooser> option) {
		File saveFile = showFileSaveDialog(StageStore.getPrimaryStage(), option);
		return saveFile;
	}

	public static File showFileSaveCheckDialog(final Window ownerWindow) {
		File saveFile = showFileSaveDialog(ownerWindow, option -> {

		});
		return saveFile;
	}

	/**
	 * 파일저장다이얼로그,
	 *
	 * showFileSaveDialog와 달리 파일존재여부도 함께 체크해준다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 2. 5.
	 * @param ownerWindow
	 * @param option
	 * @return
	 */
	public static File showFileSaveCheckDialog(final Window ownerWindow, Consumer<FileChooser> option) {
		File saveFile = showFileSaveDialog(ownerWindow, option);
		return saveFile;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 20.
	 * @param option
	 * @return
	 */
	public static File showFileSaveDialog(Consumer<FileChooser> option) {
		return showFileSaveDialog(StageStore.getPrimaryStage(), option);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 22.
	 * @param ownerWindow
	 * @param option
	 * @return
	 */
	public static File showFileSaveDialog(final Window ownerWindow, Consumer<FileChooser> option) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");

		installDefaultPath(fileChooser);

		option.accept(fileChooser);

		File result = fileChooser.showSaveDialog(ownerWindow);
		if (result != null)
			applyLastPath(result.getParentFile());

		return result;
	}

	public static Optional<Pair<String, String>> showYesOrNoDialog(String title, String message) {
		return showYesOrNoDialog(StageStore.getPrimaryStage(), title, message, null, null);
	}

	public static Optional<Pair<String, String>> showYesOrNoDialog(Stage stage, String title, String message) {
		return showYesOrNoDialog(stage, title, message, null, null);
	}

	public static Optional<Pair<String, String>> showYesOrNoDialog(Stage stage, String title, String message,
			Consumer<Dialog<Pair<String, String>>> dialogHandler) {
		return showYesOrNoDialog(stage, title, message, null, dialogHandler);
	}

	// public static Optional<Pair<String, String>> showYesOrNoDialog(String
	// title, String message) {
	// return showYesOrNoDialog(SharedMemory.getPrimaryStage(), title, message,
	// consumer, null);
	// }

	public static Optional<Pair<String, String>> showYesOrNoDialog(Stage stage, String title, String message,
			Consumer<? super Pair<String, String>> consumer, Consumer<Dialog<Pair<String, String>>> dialogHandler) {

		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.setHeaderText(message);

		// Set the button types.
		ButtonType yesBtn = new ButtonType("Yes", ButtonData.YES);
		ButtonType noBtn = new ButtonType("No", ButtonData.NO);

		dialog.getDialogPane().getButtonTypes().addAll(yesBtn, noBtn);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == yesBtn) {
				return new Pair<>("RESULT", "Y");
			} else if (dialogButton == noBtn) {
				return new Pair<>("RESULT", "N");
			}
			return null;
		});

		dialog.initOwner(stage);
		if (dialogHandler != null)
			dialogHandler.accept(dialog);

		Optional<Pair<String, String>> result = dialog.showAndWait();

		if (consumer != null)
			result.ifPresent(consumer);

		return result;

	}

	/**
	 * info Dialog 메세지 다이얼로그
	 *
	 * @param message
	 */
	public static void showMessageDialog(String message) {
		showMessageDialog(StageStore.getPrimaryStage(), message);
	}

	public static void showMessageDialog(Window initOwner, String title, String message) {
		showMessageDialog((Stage) initOwner, title, "", message, alert -> {
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(initOwner);
			alert.showAndWait();
		});
	}

	/**
	 * show info Dialog info Dialog
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 12.
	 * @param initOwner
	 * @param message
	 */
	public static void showMessageDialog(Window initOwner, String message) {

		showMessageDialog((Stage) initOwner, "Info", "", message, alert -> {
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(initOwner);
			alert.showAndWait();
		});

	}

	/**
	 * show info Dialog info Dialog
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 12.
	 * @param initOwner
	 * @param message
	 */
	public static void showMessageDialog(Stage initOwner, String message) {
		showMessageDialog(initOwner, "Info", "", message, alert -> {
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.showAndWait();
		});

	}

	/**
	 * show info Dialog
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 12.
	 * @param initOwner
	 * @param title
	 * @param headerText
	 * @param message
	 * @param apply
	 */
	public static void showMessageDialog(Stage initOwner, String title, String headerText, String message, Consumer<Alert> apply) {

		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(title);
			alert.setHeaderText(headerText);
			alert.setContentText(message);
			if (initOwner == null)
				alert.initOwner(StageStore.getPrimaryStage());
			else
				alert.initOwner(initOwner);
			apply.accept(alert);
		});

		// Dialog<Pair<String, String>> dialog = new Dialog<>();
		// dialog.setTitle(title);
		// dialog.setHeaderText(headerText);
		// dialog.setContentText(message);
		// dialog.initOwner(initOwner);
		// apply.accept(dialog);
	}

	public static File showDirectoryDialog(final Window ownerWindow) {
		return showDirectoryDialog(ownerWindow, null);
	}

	/**
	 *
	 *
	 * 디렉토리 선택 다이얼로그 오픈
	 *
	 * @Date 2015. 10. 12.
	 * @param ownerWindow
	 * @param option
	 * @return
	 * @User KYJ
	 */
	public static File showDirectoryDialog(final Window ownerWindow, Consumer<DirectoryChooser> option) {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Open Resource Directory");
		installDefaultPath(chooser);
		if (option != null)
			option.accept(chooser);

		File showDialog = chooser.showDialog(ownerWindow);

		applyLastPath(showDialog);
		return showDialog;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 17.
	 * @return
	 */
	public static File showDirectoryDialog() {
		return showDirectoryDialog(new Stage(), (c) -> {
		});
	}

	/**
	 * 
	 * 2018.12.17 default owner가 PrimaryStage에서 newStage로 수정 <br/>
	 * PrimaryStage로 설정시 Fx 프로그램이 지속적으로 팅기는 현상 존재. <br/>
	 * 
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @param option
	 */
	public static File showDirectoryDialog(Consumer<DirectoryChooser> option) {
		return showDirectoryDialog(new Stage(), option);
	}

	public static File showDirSaveDialog(Window ownerWindow, Consumer<DirectoryChooser> option) {
		return showDirSaveDialog(ownerWindow, getInitDir(), option);
	}

	public static File showDirSaveDialog(Window ownerWindow, File initDir, Consumer<DirectoryChooser> option) {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setInitialDirectory(initDir);

		chooser.setTitle("Directory");
		if (option != null)
			option.accept(chooser);

		File selectedDir = chooser.showDialog(ownerWindow);
		if (selectedDir != null && selectedDir.exists()) {
			applyLastPath(selectedDir);
		}

		return selectedDir;
	}

}
