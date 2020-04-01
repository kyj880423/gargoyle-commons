/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2016. 8. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import javafx.stage.FileChooser.ExtensionFilter;

/***************************
 *
 * @author KYJ
 *
 ***************************/
public interface GargoyleExtensionFilters {

	public static final String EXTENSION_COMMONS = "*.";

	public static final String ALL_NAME = "All files";
	public static final String ALL = "*.*";
	public static final ExtensionFilter ALL_FILTER = new ExtensionFilter(ALL_NAME, ALL);

	public static final String PNG_NAME = "png files (*.png)";
	public static final String PNG_EXTENSION = "png";
	public static final String PNG = EXTENSION_COMMONS + PNG_EXTENSION;
	public static final ExtensionFilter PNG_EXTENSION_FILTER = new ExtensionFilter(PNG_NAME, PNG);

	public static final String FX_CSS_NAME = "CSS files (*.css)";
	public static final String FX_CSS_EXTENSION = "css";
	public static final String FX_CSS = EXTENSION_COMMONS + FX_CSS_EXTENSION;

	public static final String EXE_NAME = "exe files (*.exe)";
	public static final String EXE_EXTENSION = "exe";
	public static final String EXE = EXTENSION_COMMONS + EXE_EXTENSION;

	public static final String SQL_NAME = "SQL files (*.sql)";
	public static final String SQL_EXTENSION = "sql";
	public static final String SQL = EXTENSION_COMMONS + SQL_EXTENSION;

	public static final String XLS_NAME = "Excel files (*.xls)";
	public static final String XLS_EXTENSION = "xls";
	public static final String XLS = EXTENSION_COMMONS + XLS_EXTENSION;
	public static final ExtensionFilter XLS_FILTER = new ExtensionFilter(XLS_NAME, XLS);

	public static final String XLSX_NAME = "Excel files (*.xlsx)";
	public static final String XLSX_EXTENSION = "xlsx";
	public static final String XLSX = EXTENSION_COMMONS + XLSX_EXTENSION;
	public static final ExtensionFilter XLSX_FILTER = new ExtensionFilter(XLSX_NAME, XLSX);

	public static final String DOCX_NAME = "Doc files (*.docx)";
	public static final String DOCX_EXTENSION = "docx";
	public static final String DOCX = EXTENSION_COMMONS + DOCX_EXTENSION;
	public static final ExtensionFilter DOCX_FILTER = new ExtensionFilter(DOCX_NAME, DOCX);

	public static final String DOC_NAME = "Doc files (*.doc)";
	public static final String DOC_EXTENSION = "doc";
	public static final String DOC = EXTENSION_COMMONS + DOC_EXTENSION;

	public static final String PROPERTIES_NAME = "Property files (*.properties)";
	public static final String PROPERTIES_EXTENSION = "properties";
	public static final String PROPERTIES = EXTENSION_COMMONS + PROPERTIES_EXTENSION;

	public static final String FONT_NAME = "Font files (*.ttf)";
	public static final String FONT_EXTENSION = "ttf";
	public static final String FONT = EXTENSION_COMMONS + FONT_EXTENSION;

	public static final String HTML_NAME = "html files (*.html)";
	public static final String HTML_EXTENSION = "html";
	public static final String HTML = EXTENSION_COMMONS + HTML_EXTENSION;

	public static final String MP3_NAME = "mp3 files (*.mp3)";
	public static final String MP3_EXTENSION = "mp3";
	public static final String MP3 = EXTENSION_COMMONS + MP3_EXTENSION;

	public static final String XML_NAME = "xml files (*.xml)";
	public static final String XML_EXTENSION = "xml";
	public static final String XML = EXTENSION_COMMONS + XML_EXTENSION;
	public static final ExtensionFilter XML_FILTER = new ExtensionFilter(XML_NAME, XML);

	/* Recipe File. */
	public static final String RAX_NAME = "rax files (*.rax)";
	public static final String RAX_EXTENSION = "rax";
	public static final String RAX = EXTENSION_COMMONS + RAX_EXTENSION;
	public static final ExtensionFilter RAX_FILTER = new ExtensionFilter(RAX_NAME, RAX);

	/* VB File */
	public static final String WIB_NAME = "wib files (*.wib)";
	public static final String WIB_EXTENSION = "wib";
	public static final String WIB = EXTENSION_COMMONS + WIB_EXTENSION;
	public static final ExtensionFilter WIB_FILTER = new ExtensionFilter(WIB_NAME, WIB);

	/* VB Function File */
	public static final String BFM_NAME = "bfm files (*.bfm)";
	public static final String BFM_EXTENSION = "bfm";
	public static final String BFM = EXTENSION_COMMONS + BFM_EXTENSION;
	public static final ExtensionFilter BFM_FILTER = new ExtensionFilter(BFM_NAME, BFM);
	
	
	/* VBS Function File */
	public static final String VBS_NAME = "vbs files (*.vbs)";
	public static final String VBS_EXTENSION = "vbs";
	public static final String VBS = EXTENSION_COMMONS + VBS_EXTENSION;
	public static final ExtensionFilter VBS_FILTER = new ExtensionFilter(VBS_NAME, VBS);
	
}
