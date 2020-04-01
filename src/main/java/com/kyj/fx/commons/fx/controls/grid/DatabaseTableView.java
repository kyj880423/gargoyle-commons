/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.main.layout
 *	작성일   : 2015. 10. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.kyj.fx.commons.functions.ConnectionSupplier;
import com.kyj.fx.commons.functions.DabaseInfo;
import com.kyj.fx.commons.memory.ConfigResourceLoader;
import com.kyj.fx.commons.memory.StageStore;
import com.kyj.fx.commons.utils.DbUtil;
import com.kyj.fx.commons.utils.DialogUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.utils.VoWizardUtil;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kyj.Fx.dao.wizard.core.model.vo.TableDVO;
import kyj.Fx.dao.wizard.core.model.vo.TableMasterDVO;
import kyj.Fx.dao.wizard.core.model.vo.TableModelDVO;

/**
 * @author KYJ
 *
 */
public class DatabaseTableView extends BorderPane {

	private static final String TITLE = "TableView";
	public static final String TYPE_TABLE = "Table";
	public static final String TYPE_VIEW = "View";

	@FXML
	private Button btnSearch;

	@FXML
	private Button btnSelect;

	@FXML
	private ComboBox<String> choType;

	@FXML
	private TextField txtTableName;

	@FXML
	private TableView<TableMasterDVO> tbMaster;

	@FXML
	private TableView<TableModelDVO> tbModelMst;
	private ContextMenu contextMenuTbModelMst;
	private Stage stage;

	private TableDVO tableDVO;

	private ConnectionSupplier conSupplier = new ConnectionSupplier() {

		@Override
		public Connection get() {
			try {
				return DbUtil.getConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	};

	public DatabaseTableView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DatabaseTableView.fxml"));
		// loader.setLocation();
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
			// String skin = SkinManager.getInstance().getSkin();
			// load.getStylesheets().add(skin);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * return used Connection Suppier <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 10. 5.
	 * @return
	 */
	public ConnectionSupplier getConnectionSupplier() {
		return this.conSupplier;
	}

	/**
	 * 팝업을 오픈해주며 tableDVO를 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 16.
	 * @return
	 */
	public TableDVO show() {
		Scene scene = new Scene(this);
		// scene.getStylesheets().add(SkinManager.getInstance().getSkin());
		stage = new Stage();
		stage.setTitle(TITLE);
		stage.setScene(scene);
		// stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(StageStore.getPrimaryStage());
		stage.showAndWait();
		return tableDVO;
	}

	/**
	 * close
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 16.
	 */
	private void close() {
		if (stage != null)
			stage.close();
	}

	@FXML
	void initialize() {
		contextMenuTbModelMst = new ContextMenu();

		txtTableName.setOnKeyReleased(event -> {
			if (KeyCode.ENTER == event.getCode()) {
				String tableName = txtTableName.getText();
				requestTables(tableName);
			}
		});

		btnSearch.setOnMouseClicked(event -> {
			String name = txtTableName.getText();

			String selectedItem = choType.getSelectionModel().getSelectedItem();
			switch (selectedItem) {
			case TYPE_TABLE:
				requestTables(name);
				break;

			case TYPE_VIEW:
				requestViews(name);
			}

		});

		tbMaster.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				tbModelMst.getItems().clear();
				TableMasterDVO selectedItem = tbMaster.getSelectionModel().getSelectedItem();
				try {
					if (selectedItem == null)
						return;
					List<TableModelDVO> list = listColumns(selectedItem);
					tbModelMst.getItems().addAll(list);
				} catch (Exception e) {
					DialogUtil.showExceptionDailog(e);
				}
			}
		});

		btnSelect.setOnMouseClicked(event -> {
			if (tbMaster.getItems().isEmpty() || tbModelMst.getItems().isEmpty()) {
				return;
			}
			TableMasterDVO tbMDVO = tbMaster.getSelectionModel().getSelectedItem();
			// String tableName = selectedItem.getTableName();
			// String dvoName = ValueUtil.toDVOName(tableName);
			// selectedItem.setClassName( dvoName );
			ObservableList<TableModelDVO> items = tbModelMst.getItems();

			tableDVO = new TableDVO(tbMDVO, items);

			close();
		});

		MenuItem menuDelete = new MenuItem("삭제");
		menuDelete.setOnAction(event -> {
			TableViewSelectionModel<TableModelDVO> selectionModel = tbModelMst.getSelectionModel();
			int selectedIndex = selectionModel.getSelectedIndex();
			if (selectedIndex >= 0) {
				tbModelMst.getItems().remove(selectedIndex);
				selectionModel.clearSelection();
			}
		});

		// MenuItem menuDistinct = new MenuItem("중복제거");
		// menuDistinct.setOnAction(event -> {
		// ObservableList<TableModelDVO> items = tbModelMst.getItems();
		// items.stream().filter(distinctByKey(new Function<TableModelDVO,
		// ObservableList<TableModelDVO>>(){
		//
		// @Override
		// public ObservableList<TableModelDVO> apply(TableModelDVO t) {
		// // TODO Auto-generated method stub
		// return null;
		// }));

		contextMenuTbModelMst.getItems().add(menuDelete);
		tbModelMst.setContextMenu(contextMenuTbModelMst);
	}

	// public static <T> Predicate<T> distinctByKey(Function<? super T, Object>
	// keyExtractor) {
	// Map<Object, Boolean> seen = new ConcurrentHashMap<>();
	// return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) ==
	// null;
	// }

	/**
	 * 테이블 목록 조회 요청
	 *
	 * @Date 2015. 10. 16.
	 * @param tableName
	 * @User KYJ
	 */
	private void requestTables(String tableName) {
		if (tableName == null || tableName.isEmpty())
			return;
		try {
			tbMaster.getItems().clear();
			List<TableMasterDVO> list = listTable(tableName);
			tbMaster.getItems().addAll(list);
		} catch (Exception e) {
			DialogUtil.showExceptionDailog(e);
		}
	}

	private void requestViews(String viewName) {
		if (viewName == null || viewName.isEmpty())
			return;
		try {
			tbMaster.getItems().clear();
			List<TableMasterDVO> list = listView(viewName);
			tbMaster.getItems().addAll(list);
		} catch (Exception e) {
			DialogUtil.showExceptionDailog(e);
		}
	}

	/**
	 * 테이블 컬럼 조회
	 *
	 * @Date 2015. 10. 16.
	 * @param selectedItem
	 * @return
	 * @throws Exception
	 * @User KYJ
	 */
	private List<TableModelDVO> listColumns(TableMasterDVO selectedItem) throws Exception {
		try (Connection con = this.conSupplier.get()) {
			return VoWizardUtil.listColumns(con, selectedItem);
		}
	}

	/**
	 * 테이블 조회
	 *
	 * @Date 2015. 10. 16.
	 * @param tableName
	 * @return
	 * @throws Exception
	 * @User KYJ
	 */
	private List<TableMasterDVO> listTable(String tableName) throws Exception {
		try (Connection con = this.conSupplier.get()) {
			return VoWizardUtil.listTable(con, tableName);
		}
	}

	/**
	 * TODO 뷰 조회
	 *
	 * @Date 2015. 10. 16.
	 * @param tableName
	 * @return
	 * @throws Exception
	 * @User KYJ
	 */
	private List<TableMasterDVO> listView(String viewName) throws Exception {
		try (Connection con = this.conSupplier.get()) {
			String sql = ConfigResourceLoader.getInstance().get(ConfigResourceLoader.SQL_VIEWS);

			Map<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("viewName", viewName);

			String csql = ValueUtil.getVelocityToText(sql, hashMap);
			return DbUtil.selectBeans(con, csql, 10, -1, new RowMapper<TableMasterDVO>() {

				@Override
				public TableMasterDVO mapRow(ResultSet rs, int rowNum) throws SQLException {
					TableMasterDVO tableMasterDVO = new TableMasterDVO();
					String tableName = rs.getString("TABLE_NAME");
					tableMasterDVO.setTableName(tableName);
					String className = ValueUtil.toDVOName(tableName);
					tableMasterDVO.setClassName(className);
					// tableMasterDVO.setDescription(rs.getString("COMMENTS"));
					return tableMasterDVO;

				}
			});

			// return DbUtil.select(con , sql, hashMap, n);
		}

	}

	static class DynamicConnectionSupplier extends ConnectionSupplier {
		private DabaseInfo d;

		public DynamicConnectionSupplier(DabaseInfo d) {
			this.d = d;
		}

		@Override
		public String getUrl() {
			return d.getUrl();
		}

		@Override
		public String getUsername() {
			return d.getId();
		}

		@Override
		public String getPassword() {
			return d.getPassword();
		}

		@Override
		public String getDriver() {
			return d.getDriver();
		}

	}

	@FXML
	public void btnDatabaseOnAction() {
		DabaseInfo showAndWait = DatabaseURLSelectView.showAndWait();
		if (showAndWait != null) {
			conSupplier = new DynamicConnectionSupplier(showAndWait);
		}
	}
}
