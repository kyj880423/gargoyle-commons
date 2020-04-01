/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.cx
 *	작성일   : 2018. 5. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.cx;

import java.io.File;
import java.util.Optional;

import org.apache.commons.lang.SystemUtils;

import com.kyj.fx.commons.memory.StageStore;
import com.kyj.fx.commons.utils.DialogUtil;
import com.kyj.fx.commons.utils.FileUtil;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.GargoyleExtensionFilters;
import com.kyj.fx.commons.utils.IdGenUtil;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.util.Pair;

/**
 * Excel Export MenuItem. <br/>
 * 
 * @author KYJ
 *
 */
public class ExcelExportMenu<T> extends MenuItem {

	private TableView<T> tv;

	public ExcelExportMenu(TableView<T> tv) {
		this("Export Excel", tv);
	}

	public ExcelExportMenu(String text, TableView<T> tv) {
		this.tv = tv;
		this.setText(text);
		this.setOnAction(this::excelExportMenuOnAction);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 15.
	 * @param e
	 */
	protected void excelExportMenuOnAction(ActionEvent e) {

		if (e.isConsumed())
			return;
		e.consume();
//		System.out.println(e.getSource());

		File excel = DialogUtil.showFileSaveCheckDialog(StageStore.getPrimaryStage(), chooser -> {
			chooser.getExtensionFilters().addAll(GargoyleExtensionFilters.XLSX_FILTER, GargoyleExtensionFilters.XLS_FILTER);
			chooser.setInitialDirectory(new File(SystemUtils.USER_HOME));
			chooser.setInitialFileName(IdGenUtil.generate() + "." + GargoyleExtensionFilters.XLSX_EXTENSION);
		});

		if (excel != null) {
			FxUtil.exportExcelFile(excel, this.tv);

			Optional<Pair<String, String>> showYesOrNoDialog = DialogUtil.showYesOrNoDialog("Open Excel", "Job Complate.\nOpen Excel File ? ");
			showYesOrNoDialog.ifPresent(pair -> {
				if ("Y".equals(pair.getValue())) {
					FileUtil.openFile(excel);
				}
			});
		}

		
	}

}
