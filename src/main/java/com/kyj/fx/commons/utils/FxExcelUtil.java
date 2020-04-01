/********************************
 *	프로젝트 : sos-client
 *	패키지   : com.samsung.sds.sos.client.util
 *	작성일   : 2016. 9. 6.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.excel.DefautExcelDataSetHandler;
import com.kyj.fx.commons.excel.ExcelColumnExpression;
import com.kyj.fx.commons.excel.IExcelDataSetHandler;
import com.kyj.fx.commons.excel.IExcelScreenHandler;
import com.kyj.fx.commons.excel.ITableColumnForExcel;
import com.kyj.fx.commons.exceptions.GargoyleException;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * 엑셀 export처리와 관련된 유틸리티 클래스
 * 
 * @author KYJ
 */
public class FxExcelUtil {

	private static final int EXCEL_SIZE_UNIT = IExcelDataSetHandler.EXCEL_SIZE_UNIT;
	private static final int EXCEL_VALIDATE_MAX_WITH = 255 * 256;

	//	Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
	private static final Logger LOGGER = LoggerFactory.getLogger(FxExcelUtil.class);

	private static final String $$META_COLUMN_MAX_HEIGHT$$ = "$$META_COLUMN_MAX_LENGTH$$";

	/**
	 * 디폴트 시트 네임명 패턴.
	 * @최초생성일 2016. 9. 6.
	 */
	private static final String DEFAULT_SHEET_NAME_FORMAT = "sheet%d";

	private static final int HEADER_ROW_INDEX = IExcelDataSetHandler.HEADER_ROW_INDEX;
	private static final int START_COLUMN_INDEX = IExcelDataSetHandler.START_COLUMN_INDEX;

	@SuppressWarnings("unused")
	private static final int DATA_ROW_INDEX = IExcelDataSetHandler.DATA_ROW_INDEX;

	//헤더 텍스트 폰트 흰색. 
	final static XSSFColor DEFAULT_HEADER_FONT_COLOR = new XSSFColor(new java.awt.Color(255, 255, 255));
	//헤더 전경색 파란색.
	final static XSSFColor DEFAULT_HEADER_FOREGROUND_COLOR = new XSSFColor(new java.awt.Color(91, 155, 213));

	/**
	 * 로고 이미지의 위치를 가리킴
	 * @최초생성일 2016. 9. 20.
	 */
	private static final String IMG_EXCEL_LOGO_PNG = "images/view/img/excel-logo.png";

	/**
	 * 엑셀파일을 생성한다. xlsx(최신)
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 6. 
	 * @return
	 * @throws IOException
	 */
	public static Workbook createNewWorkBookXlsx() throws IOException {
		Workbook xssfWorkbook = new XSSFWorkbook();
		return xssfWorkbook;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 7. 
	 * @param screen
	 * @param exportExcelFile
	 * @param tableViewList
	 * @param overrite
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void createExcel(IExcelScreenHandler screen, File exportExcelFile, List<TableView> tableViewList, boolean overrite)
			throws Exception {

		IExcelScreenHandler screenHandler = screen;

		/*
		 * Key : Sheet
		 * Value - Key :  ExcelColumnExpression
		 * Value - Value :  Object
		 */
		LinkedHashMap<String, LinkedHashMap<ExcelColumnExpression, List<Object>>> dataSet = new LinkedHashMap<>();
		Map<String, Map<String, String>> metadata = new HashMap<>();

		int sheetIndex = 0;
		for (TableView table : tableViewList) {

			Predicate<TableView<?>> useTableViewForExcel = screenHandler.useTableViewForExcel();
			if (useTableViewForExcel != null) {
				if (!useTableViewForExcel.test(table)) {
					continue;
				}
			}

			//Sheet.
			String sheetName = screenHandler.toSheetName(table);
			if (sheetName == null) {
				sheetName = String.format(DEFAULT_SHEET_NAME_FORMAT, sheetIndex++);
			}

			ObservableList<TableColumn> columns = table.getColumns();

			//계층형 테이블컬럼의 모든 값을 찾아냄.
			ArrayList<ExcelColumnExpression> allColumnsList = new ArrayList<ExcelColumnExpression>();
			int maxLevel = getMaxLevel(columns, /*ExcelColumnExpression :: 계층형 테이블컬럼들을 일렬로 찾아낸 리스트 */allColumnsList,
					screenHandler.useTableColumnForExcel());

			dataSet.put(sheetName, getDataSource(screen, table, allColumnsList));

			HashMap<String, String> meta = new HashMap<String, String>();
			meta.put($$META_COLUMN_MAX_HEIGHT$$, String.valueOf(maxLevel));
			metadata.put(sheetName, meta);

		}

		if (dataSet.isEmpty())
			dataSet.put("empty", new LinkedHashMap<>());

		createExcel(screenHandler, exportExcelFile, dataSet, metadata, overrite);
	}

	/**
	 *  엑셀파일을 생성한다.
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 6.
	 *  
	 * @param exportExcelFile 
	 *   	export하려는 파일 확장자는 .xlsx 사용 권고
	 *   
	 * @param dataSource 
	 *      데이터셋.  개발자 혼동(순서)을 피하기 위해 LinkedMap(순서유지)을 강제적으로 사용
	 *      값은 Map<Sheet명, Map<컬럼메타,List<값>> 순 매핑이되어있다.
	 * 
	 * @param handler
	 *     데이터 컨버터, 데이터내에 특화가 필요한 처리가 필요한경우 구현하여 사용하도록한다.
	 *     
	 * @throws Exception
	 */
	public static void createExcel(File exportExcelFile,
			LinkedHashMap<String, LinkedHashMap<ExcelColumnExpression, List<Object>>> dataSource,
			IExcelDataSetHandler<Sheet, LinkedHashMap<ExcelColumnExpression, List<Object>>> handler,
			Map<String, Map<String, String>> metadata, boolean overwrite) throws Exception {

		//파일이 이미 존재하는 상황에서 overrite하려는 경우 에러.
		if (exportExcelFile.exists() && !overwrite) {
			throw new GargoyleException(String.format("output File : {%s} already exists.!", exportExcelFile.getName()));
		}

		Workbook createNewWorkBookXlsx = createNewWorkBookXlsx();

		//시트를 의미하는 iterator.
		Iterator<String> iterator = dataSource.keySet().iterator();
		int sheetIndex = 0;
		while (iterator.hasNext()) {
			String sheetName = iterator.next();
			Sheet createSheet = createNewWorkBookXlsx.createSheet(sheetName);

			Map<String, String> meta = metadata.get(sheetName);
			if (meta == null || meta.isEmpty())
				continue;

			//각 헤더컬럼의 개수(높이)
			String columnMaxHeight = meta.get($$META_COLUMN_MAX_HEIGHT$$);
			int maxColumnRowSize = ValueUtil.decode(columnMaxHeight, val -> Integer.parseInt(val), () -> 0);

			LinkedHashMap<ExcelColumnExpression, List<Object>> linkedHashMap = dataSource.get(sheetName);
			createHeaders(handler, createSheet, linkedHashMap, maxColumnRowSize);

			//컬럼 + 데이터리스트로 이루어진 데이터셋
			LinkedHashMap<ExcelColumnExpression, List<Object>> dataSet = dataSource.get(sheetName);
			drawBody(handler, createSheet, maxColumnRowSize, dataSet);

			short lastColumnNum = (short) dataSet.size();//(short) (createSheet.getRow(maxColumnRowSize + 1).getLastCellNum() - 1);
			IExcelScreenHandler screenHandler = handler.getExcelScreenHandler();

			if (handler.isApplyAutoFit()) {
				for (int i = 0; i < lastColumnNum; i++) {
					createSheet.autoSizeColumn(i);
				}
			}

			if (screenHandler != null) {
				//사용자 정의 sheet 처리를 지원한다.
				screenHandler.customSheetHandle(sheetIndex, createSheet);
				sheetIndex++;
			}

			//로고 이미지 처리.
			Utils.createDefaultLogo(createSheet);

			createSheet.setAutoFilter(new CellRangeAddress((maxColumnRowSize), (maxColumnRowSize), START_COLUMN_INDEX, lastColumnNum));
			createSheet.createFreezePane(0, (maxColumnRowSize + 1));

		}

		//사용자 정의 workbook 처리를 지원함.
		IExcelScreenHandler excelScreenHandler = handler.getExcelScreenHandler();
		if (excelScreenHandler != null)
			excelScreenHandler.customWorkbookHandle(createNewWorkBookXlsx);

		//파일 write처리.
		try (FileOutputStream fileOutputStream = new FileOutputStream(exportExcelFile)) {
			createNewWorkBookXlsx.write(fileOutputStream);
		}

	}

	/**
	 * 다중헤더를 갖는 컬럼의 가장 높은 크기를 리턴하는 함수 + 
	 * List<ExcelColumnExpression>배열에 헤더순서대로 값을 input해줌.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 7. 
	 * @param columns
	 * @param allColumnsList
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static int getMaxLevel(ObservableList<TableColumn> columns, List<ExcelColumnExpression> allColumnsList,
			ITableColumnForExcel use) {
		int maxLevel = 0;
		int SIZE = columns.size();
		for (int i = 0; i < SIZE; i++) {
			TableColumn col = columns.get(i);

			if (!col.isVisible())
				continue;

			int level = appendAllColumns(null, allColumnsList, col, /*시작은 0레벨부터.*/HEADER_ROW_INDEX, use);
			if (maxLevel < level)
				maxLevel = level;

		}
		return maxLevel;
	}

	/**
	 * UI 및 TableView로부터 Excel데이터생성을 위한 체계화된 데이터 
	 * LinkedHashMap객체를 리턴.
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 7. 
	 * @param screen
	 * @param table
	 * @param allColumnsList 
	 *    계층형 테이블컬럼들을 일렬로 찾아낸 리스트
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	//	static LinkedHashMap<ExcelColumnExpression, List<Object>> getDataSource(AbstractScreen<?> screen, TableView table,
	//			ArrayList<ExcelColumnExpression> allColumnsList) {
	//		return getDataSource((IExcelScreenHandler) screen, table, allColumnsList);
	//	}

	/**
	 * UI 및 TableView로부터 Excel데이터생성을 위한 체계화된 데이터 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 19. 
	 * @param mapper
	 * @param table
	 * @param allColumnsList
	 * @return
	 */
	static LinkedHashMap<ExcelColumnExpression, List<Object>> getDataSource(IExcelScreenHandler mapper, TableView table,
			ArrayList<ExcelColumnExpression> allColumnsList) {
		LinkedHashMap<ExcelColumnExpression, List<Object>> dataSet = new LinkedHashMap<>();
		//ExcelColumnExpression :: 계층형 테이블컬럼들을 일렬로 찾아낸 리스트
		//		List<ExcelColumnExpression> allColumnsList = new ArrayList<ExcelColumnExpression>();
		@SuppressWarnings("unchecked")
		ObservableList<TableColumn> items = table.getItems();
		int size = items.size();

		//		LinkedHashMap<ExcelColumnExpression, List<Object>> tableValue = new LinkedHashMap<>();
		//특화 헤더를 매핑.
		for (ExcelColumnExpression c : allColumnsList) {

			//			if (!c.isVisible())
			//				continue;

			String columnHeaderMapper = mapper.columnHeaderMapper(table, c.getTableColumn());
			c.setDisplayText(columnHeaderMapper);

			List<Object> values = new ArrayList<Object>();
			int columnIndex = c.getIndex();
			for (int rowIndex = 0; rowIndex < size; rowIndex++) {
				Object userValue = mapper.valueMapper(table, c.getTableColumn(), columnIndex, rowIndex);
				values.add(userValue);
			}
			dataSet.put(c, values);
		}
		return dataSet;
	}

	/**
	 * 엑셀 데이터부 처리.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 19. 
	 * @param handler
	 * @param createSheet
	 * @param maxColumnRowSize
	 * @param dataSet
	 */
	private static void drawBody(IExcelDataSetHandler<Sheet, LinkedHashMap<ExcelColumnExpression, List<Object>>> handler, Sheet createSheet,
			int maxColumnRowSize, LinkedHashMap<ExcelColumnExpression, List<Object>> dataSet) {
		handler.accept(maxColumnRowSize, createSheet, dataSet);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 6. 
	 * @param exportExcelFile
	 *      export하려는 파일 확장자는 .xlsx 사용 권고
	 *      
	 * @param dataSource
	 *      데이터셋.  개발자 혼동(순서)을 피하기 위해 LinkedMap(순서유지)을 강제적으로 사용
	 *      값은 Map<Sheet명, Map<컬럼메타,List<값>> 순 매핑이되어있다.
	 *      
	 * @throws Exception
	 */
	static void createExcel(IExcelScreenHandler screenHandler, File exportExcelFile,
			LinkedHashMap<String, LinkedHashMap<ExcelColumnExpression, List<Object>>> dataSource, Map<String, Map<String, String>> metadata,
			boolean overrite) throws Exception {
		DefautExcelDataSetHandler defaultHandler = new DefautExcelDataSetHandler();
		defaultHandler.setExcelScreenHandler(screenHandler);
		createExcel(exportExcelFile, dataSource, defaultHandler, metadata, overrite);
	}

	/**
	 * 테이블뷰의 모든 컬럼을 찾기위한 재귀함수.
	 * 
	 * (( 설마 테이블컬럼이 무한대로 있는 화면은 없겠지. ))
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 6. 
	 * @param parent 계층형 컬럼의 부모를 가리킴.
	 * @param pocket 컬럼이 일렬로 저정된다.
	 * @param index 트리레벨을 가리킴. 다른말로하면 컬럼의 순서를 가리킴.
	 * @param col
	 * @param level 계층형 컬럼의 현재 레벨을 가리킴.
	 * @param use 엑셀처리에 사용할 컬럼인지 처리여부
	 * @return 가장 Depth가 긴 레벨을 리턴.
	 */
	static <T> int appendAllColumns(ExcelColumnExpression parent, List<ExcelColumnExpression> pocket, TableColumn<T, ?> col, int level,
			ITableColumnForExcel use) {
		int maxLevel = level;
		int myLevel = level;
		if (col == null /*|| !col.isVisible()*/)
			return maxLevel;

		if (use != null) {
			boolean test = use.test(col.getTableView(), col);
			if (!test)
				return maxLevel;
		}

		ExcelColumnExpression columnExpr = new ExcelColumnExpression();
		String text = ValueUtil.isEmpty(col.getText()) ? "-" : col.getText();
		boolean visible = col.isVisible();
		double width = col.getWidth();

		columnExpr.setRealText(text);
		columnExpr.setTableColumn(col);
		columnExpr.setIndex(pocket.isEmpty() ? 0 : pocket.get(pocket.size() - 1).getIndex() + 1);
		columnExpr.setLevel(myLevel);
		columnExpr.setVisible(visible);
		columnExpr.setParent(parent);

		LOGGER.debug("col : {} , isVisible : {} , level : {}, width : {} ", text, col.isVisible(), myLevel, width);
		pocket.add(columnExpr);

		if (parent != null) {
			parent.getChildrens().add(columnExpr);
		}

		List<TableColumn<T, ?>> columns = col.getColumns();
		if (columns == null || columns.isEmpty()) {
			return maxLevel;
		} else {

			int visibledColumnCount = 0;
			for (int i = 0; i < columns.size(); i++) {
				TableColumn<T, ?> subCol = columns.get(i);

				if (use.test(subCol.getTableView(), subCol)) {
					int _level = appendAllColumns(columnExpr, pocket, subCol, (myLevel + 1), use);
					if (maxLevel < _level)
						maxLevel = _level;
					visibledColumnCount++;
				}
			}
			List<ExcelColumnExpression> subList = pocket.subList(pocket.size() - visibledColumnCount, pocket.size());
			//			if (parent != null) {
			//fix indexㅒ

			int idx = 0;
			for (ExcelColumnExpression c : subList) {

				//				if (c.isVisible()) {
				c.setIndex(columnExpr.getIndex() + idx);
				idx++;
				//				}

			}

			return maxLevel;
		}

	}

	/**
	 * @param sheetConverter 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 6. 
	 * @param createSheet
	 * @param headers
	 * @param maxHeaderLength 
	 * @throws Exception
	 */
	private static void createHeaders(IExcelDataSetHandler<Sheet, LinkedHashMap<ExcelColumnExpression, List<Object>>> sheetConverter,
			Sheet createSheet, LinkedHashMap<ExcelColumnExpression, List<Object>> linkedHashMap, final int maxColumnRowSize)
					throws Exception {

		Set<ExcelColumnExpression> keySet = linkedHashMap.keySet();
		Iterator<ExcelColumnExpression> iterator = keySet.iterator();

		while (iterator.hasNext()) {
			ExcelColumnExpression next = iterator.next();
			//			sheetConverter.createCell(createSheet, next.displayText, HEADER_ROW_INDEX, next.index);
			drawHeader(sheetConverter, createSheet, next, maxColumnRowSize/*, HEADER_ROW_INDEX*/);

		}
	}

	/**
	 * 헤더부분을 실제 엑셀에 처리하기 위한 코드.
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 7. 
	 * @param sheetConverter
	 * @param createSheet
	 * @param columnExpr
	 * @param startRow
	 * @throws Exception
	 */
	private static void drawHeader(IExcelDataSetHandler<Sheet, LinkedHashMap<ExcelColumnExpression, List<Object>>> sheetConverter,
			Sheet createSheet, ExcelColumnExpression columnExpr, final int maxLevel) throws Exception {

		Cell createCell = sheetConverter.createCell(createSheet, columnExpr.getDisplayText(), (/*startRow +*/ columnExpr.getLevel()),
				START_COLUMN_INDEX + columnExpr.getIndex());

		List<ExcelColumnExpression> childrens = columnExpr.getChildrens();
		if (childrens != null && !childrens.isEmpty()) {
			int horizontalSize = childrens.size();
			for (ExcelColumnExpression subColumnExpr : childrens) {
				if (subColumnExpr.isVisible()) {
					drawHeader(sheetConverter, createSheet, subColumnExpr, maxLevel);
				}
			}
			/* 셀병합. Horizontal Merge  부모 레벨.*/
			{

				int level = columnExpr.getParent() == null ? HEADER_ROW_INDEX : columnExpr.getParent().getLevel();
				int index = columnExpr.getIndex() + START_COLUMN_INDEX;
				CellRangeAddress cellMerge = cellMerge(createSheet, level, level, index, (index + horizontalSize - 1));
				border(cellMerge, createSheet);
				headerStyle(createCell);
			}

		} else {
			/*셀병합. Vertical Merge*/
			//case1 자식레벨은 없고 , 본인 레벨이 max 레벨이 낮은경우
			int level = columnExpr.getLevel();
			int index = columnExpr.getIndex() + START_COLUMN_INDEX;
			if (level < maxLevel) {
				CellRangeAddress cellMerge = cellMerge(createSheet, level, maxLevel, index, index);
				//				style(createCell, DEFAULT_HEADER_STYLE);
				border(cellMerge, createSheet);
			}

			/* 현재 레벨.*/
			{
				headerStyle(createCell, style -> {
					style.setBorderTop(CellStyle.BORDER_THIN);
					style.setBorderLeft(CellStyle.BORDER_THIN);
					style.setBorderRight(CellStyle.BORDER_THIN);
					style.setBorderBottom(CellStyle.BORDER_THIN);
				});

				//컬럼 width 지정.
				width(createSheet, columnExpr, index);
			}

		}

	}

	/**
	 * 컬럼 width를 지정한다.
	 * 엑셀의 경우 max width 사이즈 제한이 있기때문에 유효성 검증 로직이 들어가 있다.
	 * 
	 * EXCEL_VALIDATE_MAX_WITH -> 엑셀최대 너비 ( Sheet 클래스내 setColumnWidth 함수 주석 참조.)
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 20. 
	 * @param createSheet
	 * @param columnExpr
	 * @param colIndex
	 */
	private static void width(Sheet createSheet, ExcelColumnExpression columnExpr, int colIndex) {
		int prefWidth = (int) (columnExpr.getTableColumn().getWidth() * EXCEL_SIZE_UNIT);
		if (EXCEL_VALIDATE_MAX_WITH < prefWidth) {
			prefWidth = EXCEL_VALIDATE_MAX_WITH - 1;
		}
		createSheet.setColumnWidth(colIndex, prefWidth);
	}

	/**
	 * 디폴트 헤더 스타일 정의.
	 * @최초생성일 2016. 9. 8.
	 */
	private static final Consumer<XSSFCellStyle> DEFAULT_HEADER_STYLE = new Consumer<XSSFCellStyle>() {

		@Override
		public void accept(XSSFCellStyle style) {
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			//전경색
			defaultForeGround(style);
		}

		private void defaultForeGround(XSSFCellStyle style) {
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style.setFillForegroundColor(DEFAULT_HEADER_FOREGROUND_COLOR);
		}

	};

	/**
	 * 셀 스타일 처리.
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 8. 
	 * @param cell
	 */
	static void headerStyle(Cell cell) {
		style(cell, DEFAULT_HEADER_STYLE, null);

	}

	/**
	 * 셀 스타일 처리.
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 8. 
	 * @param cell
	 * @param options
	 */
	static void headerStyle(Cell cell, Consumer<XSSFCellStyle> options) {
		style(cell, DEFAULT_HEADER_STYLE, options);
	}

	/**
	 * 셀 스타일 처리.
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 8. 
	 * @param cell
	 * @param defaultStyle 
	 *    전역적으로 설정할 디폴트 스타일
	 * @param options
	 */
	static void style(Cell cell, Consumer<XSSFCellStyle> defaultStyle, Consumer<XSSFCellStyle> options) {
		Workbook workbook = cell.getSheet().getWorkbook();

		XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();

		//default style.
		defaultStyle.accept(style);

		/*
		 * 2016-09-09 싱글톤형태로 사용시 에러발생. 내부적으로 생성하고 관리해야함.
		 */
		//		if (DEFAULT_FONT == null) {
		XSSFFont DEFAULT_FONT = (XSSFFont) workbook.createFont();
		DEFAULT_FONT.setColor(DEFAULT_HEADER_FONT_COLOR);
		DEFAULT_FONT.setBold(true);
		//		}
		style.setFont(DEFAULT_FONT);

		if (options != null)
			options.accept(style);
		cell.setCellStyle(style);

	}

	/**
	 * 셀 머지.
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 8. 
	 * @param createSheet
	 * @param firstRow
	 * @param lastRow
	 * @param firstCol
	 * @param lastCol
	 */
	static CellRangeAddress cellMerge(Sheet createSheet, int firstRow, int lastRow, int firstCol, int lastCol) {
		CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);

		/*
		 * 2016-09-09. cell이 1개일경우 exception이 발생되므로 조건처리.
		 * visible되는 셀이 아닌경우에 예외가 발생한다. 
		 * by kyj.
		 */
		if (region.getNumberOfCells() > 1)
			createSheet.addMergedRegion(region);
		return region;
	}

	/**
	 * Border 설정.
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 8. 
	 * @param region
	 * @param sheet
	 */
	static void border(CellRangeAddress region, Sheet sheet) {
		Workbook wb = sheet.getWorkbook();
		//		sheet.addMergedRegion(region);
		final short borderMediumDashed = CellStyle.BORDER_THIN;
		RegionUtil.setBorderBottom(borderMediumDashed, region, sheet, wb);
		RegionUtil.setBorderTop(borderMediumDashed, region, sheet, wb);
		RegionUtil.setBorderLeft(borderMediumDashed, region, sheet, wb);
		RegionUtil.setBorderRight(borderMediumDashed, region, sheet, wb);
	}

	/**
	 * @author KYJ
	 *
	 */
	final static class Utils {

		static byte[] logoImageCache = null;

		/**
		 * 
		 * 내부적 cache사용으로 이미지가 바뀌는경우 코드를 고쳐야함.! 
		 * 
		 * @작성자 : KYJ
		 * @작성일 : 2016. 9. 9. 
		 * @return
		 * @throws Exception
		 */
		final static byte[] getDefaultLogoImage() throws Exception {
			//
			URL resource = ClassLoader.getSystemClassLoader().getResource(IMG_EXCEL_LOGO_PNG);
			if(resource == null)
				return null;
			//			File file = new File(resource.toURI());
			return Utils.imageToBytes(resource);
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2016. 9. 9. 
		 * @param sheet
		 * @throws Exception
		 */
		final static void createDefaultLogo(Sheet sheet) throws Exception {
			Workbook workbook = sheet.getWorkbook();
			byte[] defaultLogoImage = getDefaultLogoImage();
			if(defaultLogoImage == null)
				return;
			int pictureIdx = workbook.addPicture(defaultLogoImage, Workbook.PICTURE_TYPE_PNG);

			CreationHelper creationHelper = workbook.getCreationHelper();
			ClientAnchor anchor = creationHelper.createClientAnchor(); //new XSSFClientAnchor();
			//			anchor.setAnchorType(AnchorType.MOVE_DONT_RESIZE);
			Drawing createDrawingPatriarch = sheet.createDrawingPatriarch();
			anchor.setDx1(0);
			anchor.setCol1(0);
			anchor.setRow1(0);

			//#1 테이블 셀의 너비에 의존적이지않게 사이즈조절.
			anchor.setAnchorType(AnchorType.MOVE_DONT_RESIZE);
			Picture createPicture = createDrawingPatriarch.createPicture(anchor, pictureIdx);
			//#2 테이블 셀의 너비에 의존적이지않게 사이즈조절.
			createPicture.resize();
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2016. 9. 9. 
		 * @param imgPath
		 * @return
		 * @throws IOException
		 */
		static byte[] imageToBytes(URL resource) throws IOException {
			//			byte[] logoImageCache = new byte[] { 0, 0, 0 };
			if (logoImageCache != null)
				return logoImageCache;

			try (InputStream openStream = resource.openStream()) {
				try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
					/* 2016-09-22 by kyj 
					 * byte로 읽는경우 배포할때 문제가 발생. 아마 인코딩문제일것으로 추측.
					 */
					//					byte[] read = new byte[1024];
					int read = -1;
					while ((read = openStream.read()) != -1) {
						//						LOGGER.debug("{}", read);
						byteArrayOutputStream.write(read);
					}
					byteArrayOutputStream.flush();
					logoImageCache = byteArrayOutputStream.toByteArray();
				}
			}

			return logoImageCache;
		}

		//		public static BufferedImage getBufferedImage(File path) throws IOException {
		//			BufferedImage img = ImageIO.read(path);
		//			return img;
		//		}

		//		public static byte[] resize(BufferedImage dimg, int newW, int newH) {
		//			int w = dimg.getWidth();
		//			int h = dimg.getHeight();
		//			//			BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
		//
		//			Graphics2D g = null;
		//			try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
		//
		//				g = dimg.createGraphics();
		//				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		//				g.drawImage(dimg, 0, 0, newW, newH, 0, 0, w, h, Color.black, null);
		//
		//				ImageIO.write(dimg, "png", out);
		//				return out.toByteArray();
		//			} catch (IOException e) {
		//				e.printStackTrace();
		//			} finally {
		//				g.dispose();
		//			}
		//			return new byte[] { 0, 0, 0 };
		//		}

		//		public static byte[] resize(File path, int w, int h) throws IOException {
		//			BufferedImage tmpImage = getBufferedImage(path);
		////			tmpImage.createGraphics();
		//			tmpImage.flush();
		//			Graphics graphics = tmpImage.getGraphics();
		//			sun.awt.image.ToolkitImage scaled = (ToolkitImage) tmpImage.getScaledInstance(w, h, Image.SCALE_SMOOTH);
		//			BufferedImage buffered = scaled.getBufferedImage();
		//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//			ImageIO.write(buffered, "png", baos);
		//			baos.flush();
		//			byte[] byteArray = baos.toByteArray();
		//			baos.close();
		//			return byteArray;
		//		}

	}

}
