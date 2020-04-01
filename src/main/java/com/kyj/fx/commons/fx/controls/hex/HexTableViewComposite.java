/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.hex
 *	작성일   : 2019. 10. 29.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.hex;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.threads.DemonThreadFactory;
import com.kyj.fx.commons.utils.ArrayUtil;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.Hex;
import com.kyj.fx.commons.utils.HexConverter;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.fxloader.FXMLController;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;

/**
 * 
 * Hex Viewer <br/>
 * 
 * @author KYJ (callakrsos@naver.com)
 *
 */
@FXMLController(value = "HexTableView.fxml", css = "HexTableView.css", isSelfController = true)
public class HexTableViewComposite extends BorderPane {
	private static final Logger LOGGER = LoggerFactory.getLogger(HexTableViewComposite.class);

	@FXML
	private TableView<Map<String, Number>> tbHex;
	@FXML
	private TableColumn<Map<String, Number>, Number> tc00, tc01, tc02, tc03, tc04, tc05, tc06, tc07, tc08, tc09, tc0a, tc0b, tc0c, tc0d,
			tc0e, tc0f;
	@FXML
	private TableColumn<Map<String, Number>, String> tcDecode, tcOffset;

	final String COL_NAME[] = { "tc00", "tc01", "tc02", "tc03", "tc04", "tc05", "tc06", "tc07", "tc08", "tc09", "tc0a", "tc0b", "tc0c",
			"tc0d", "tc0e", "tc0f" };
	@FXML
	private TextField txtSelectionCell, txtSelectionHex, txtSelectionDecoded;

	private ObjectProperty<File> fileValue = new SimpleObjectProperty<File>();

	public HexTableViewComposite() {
		FxUtil.loadRoot(HexTableViewComposite.class, this, err -> {
			LOGGER.error(ValueUtil.toString(err));
		});
	}

	@FXML
	public void initialize() {
		tbHex.getSelectionModel().setCellSelectionEnabled(true);
		tbHex.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		FxUtil.installClipboardKeyEvent(tbHex);
		FxUtil.installFindKeyEvent(FxUtil.getWindow(this), tbHex);
		fileValue.addListener((oba, o, n) -> {
			fromFile(n, items -> {

				if (Platform.isFxApplicationThread())
					getItem().setAll(items);
				else
					Platform.runLater(() -> {
						getItem().setAll(items);
					});
			});
		});

		// AutoMapping.
		for (String fieldName : COL_NAME) {
			try {
				Field declaredField = HexTableViewComposite.class.getDeclaredField(fieldName);
				if (!declaredField.isAccessible())
					declaredField.setAccessible(true);
				TableColumn<Map<String, Number>, Number> tc = (TableColumn<Map<String, Number>, Number>) declaredField.get(this);
				tc.setCellValueFactory(param -> {
					Map<String, Number> value = param.getValue();
					Number integer = value.get(fieldName.replace("tc", ""));
					SimpleIntegerProperty simpleIntegerProperty = new SimpleIntegerProperty();
					// SimpleStringProperty simpleStringProperty = new
					// SimpleStringProperty();
					if (integer != null)
						simpleIntegerProperty.set(integer.intValue());
					// simpleIntegerProperty.set();
					return simpleIntegerProperty;
				});
				tc.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Number>() {

					@Override
					public String toString(Number object) {
						return String.format("%02x", object.intValue());
					}

					@Override
					public Number fromString(String string) {
						return null;
					}
				}));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		tcDecode.setCellValueFactory(param -> {
			Map<String, Number> value = param.getValue();
			Optional<String> reduce = value.keySet().stream().sorted().map(k -> Character.toString((char) value.get(k).intValue()))
					.reduce((a, b) -> {
						return a + b;
					});
			if (reduce.isPresent())
				return new SimpleStringProperty(reduce.get());
			return new SimpleStringProperty();
		});

		tcOffset.setCellValueFactory(param -> {
			Map<String, Number> value = param.getValue();
			long offset = value.get("offset").longValue();
			return new SimpleStringProperty(String.format("%08x", offset));
		});

		tbHex.getSelectionModel().getSelectedCells().addListener(new ListChangeListener<TablePosition>() {

			@Override
			public void onChanged(Change<? extends TablePosition> c) {
				if (c.next()) {
					ObservableList<? extends TablePosition> list = c.getList();
					if (list.isEmpty())
						return;
					TablePosition tablePosition = list.get(0);
					int row = tablePosition.getRow();
					int column = tablePosition.getColumn();
					TableColumn<Map<String, Number>, ?> column2 = tbHex.getColumns().get(column);
					//
					boolean exists = ArrayUtil.exists("tc" + column2.getText().toLowerCase(), COL_NAME);
					if (!exists) {
						return;
					}

					Number value = tbHex.getItems().get(row).get(column2.getText().toLowerCase());
					if (value == null)
						return;
					// Number n = (Number) value;
					LOGGER.debug("{}", value);

					txtSelectionCell.setText(String.format("r : %d c :%d", row, column));
					txtSelectionHex.setText(String.format("%02d", value.intValue()));
					// byte[] decode = Hex.decode(value.toString());

					Object s = FxUtil.getValue(tcDecode, row);
					String substring = s.toString().substring(column, column + 1);
					txtSelectionDecoded.setText(substring);

				}

			}
		});

	}

	public ObservableList<Map<String, Number>> getItem() {
		return this.tbHex.getItems();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 29.
	 * @param file
	 * @return
	 */
	public static void fromFile(File file, Consumer<List<Map<String, Number>>> onAceppt) {
		Thread newThread = DemonThreadFactory.newInstance().newThread(() -> {
			return new HexConverter().fromFileToMap(file);
		}, a -> {
			@SuppressWarnings("unchecked")
			List<Map<String, Number>> items = (List<Map<String, Number>>) a;
			onAceppt.accept(items);
		}, err -> {
			LOGGER.error(ValueUtil.toString(err));
		});
		newThread.start();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 29.
	 * @return
	 */
	public final ObjectProperty<File> fileValueProperty() {
		return this.fileValue;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 29.
	 * @return
	 */
	public final File getFileValue() {
		return this.fileValueProperty().get();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 29.
	 * @param fileValue
	 */
	public final void setFileValue(final File fileValue) {
		this.fileValueProperty().set(fileValue);
	}

}
