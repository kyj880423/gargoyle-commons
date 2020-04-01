/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.excel
 *	작성일   : 2017. 3. 31.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.excel;

import java.io.File;
import java.util.function.Consumer;

import com.kyj.fx.commons.utils.DateUtil;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * 
 *  DialogUtil.showFileSaveDialog
 *  함수에서 사용되는 엑셀파일관 관련된 
 *  FileChooser 핸들러
 * @author KYJ
 *
 */
public class ExcelFileChooserHandler implements Consumer<FileChooser> {

	@Override
	public void accept(FileChooser option) {

		option.setInitialFileName(DateUtil.getCurrentDateString(DateUtil.SYSTEM_DATEFORMAT_YYYYMMDDHHMMSS));
		option.getExtensionFilters().add(new ExtensionFilter("Excel files (*.xlsx)", "*.xlsx"));
		option.getExtensionFilters().add(new ExtensionFilter("Excel files (*.xls)", "*.xls"));
		option.getExtensionFilters().add(new ExtensionFilter("All files", "*.*"));
		option.setTitle("Save Excel");
		option.setInitialDirectory(new File(System.getProperty("user.home")));

	}

}
