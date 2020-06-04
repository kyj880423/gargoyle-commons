/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2016. 03. 26. 수정
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.kyj.fx.commons.models.ExcelColDVO;
import com.kyj.fx.commons.models.ExcelDataDVO;
import com.kyj.fx.commons.models.ExcelDataFomulaDVO;
import com.kyj.fx.commons.models.ExcelSVO;

import javafx.scene.control.TableView;

public class ExcelUtil {

	private static final String YYYY_MM_DD = "yyyy-MM-dd";

	/**
	 * 2014. 10. 3. KYJ
	 *
	 * @return
	 * @throws IOException
	 * @처리내용 : 엑셀파일을 생성한다. xlsx(최신)
	 */
	public static Workbook createNewWorkBookXlsx() throws IOException {
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
		return xssfWorkbook;
	}

	/**
	 * 2014. 10. 3. KYJ
	 *
	 * @return
	 * @throws IOException
	 * @처리내용 : 엑셀파일을 생성한다 xls(구)
	 */
	public static Workbook createNewWorkBookXls() throws IOException {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		return hssfWorkbook;
	}

	public static Workbook readXlsx(File filePath) throws IOException {
		return new XSSFWorkbook(new FileInputStream(filePath));
	}

	public static Workbook readXlsx(String filePath) throws IOException {
		return readXlsx(new File(filePath));
	}

	public static Workbook readXls(File filePath) throws IOException {
		return new HSSFWorkbook(new FileInputStream(filePath));
	}

	public static Workbook readXls(String filePath) throws IOException {
		return readXls(new File(filePath));
	}

	public static FileOutputStream getFileOutputStream(String fileName, String fileFormat) throws FileNotFoundException {
		return new FileOutputStream(fileName.concat("." + fileFormat));
	}

	public static Cell createCell(Sheet sheet, Object data, int row, int column) throws Exception {
		Row row2 = sheet.getRow(row);

		if (row2 == null) {
			row2 = sheet.createRow(row);
		}
		Cell createCell = null;
		try {
			createCell = row2.getCell(column);
			if (createCell == null) {
				createCell = row2.createCell(column);
			}
		} catch (NullPointerException e) {
			createCell = row2.createCell(column);
		}

		if(data instanceof ExcelDataFomulaDVO)
		{
			ExcelDataFomulaDVO d = (ExcelDataFomulaDVO) data;
			createCell.setCellFormula(d.getData().toString());
		}
		else if (data instanceof String) {
			if(((String) data).length() >= 32767)
				createCell.setCellValue(new XSSFRichTextString(data.toString()));
			else
				createCell.setCellValue((String) data);
		} else if (data instanceof Integer) {
			createCell.setCellValue((Integer) data);
		} else if (data instanceof Double) {
			createCell.setCellValue((Double) data);
		} else if (data instanceof Float) {
			createCell.setCellValue((Float) data);
		} else if (data == null) {
			createCell.setCellValue("");
		} else {
			createCell.setCellValue(data.toString());
//			throw new Exception("뭘 입력하신겁니까?");
		}
		
		

		return createCell;
	}

	public static String getValue(Sheet sheet, int row, int column) throws Exception {
		String temp = null;

		if (sheet != null) {
			Row rowObj = sheet.getRow(row);

			if (rowObj != null) {
				Cell cell = rowObj.getCell(column);
				temp = getValue(cell);
			}
		}
		return temp;
	}

	public static String getValue(Cell cell) throws Exception {
		String temp = null;

		if (cell != null) {
			SimpleDateFormat f = new SimpleDateFormat(YYYY_MM_DD);

			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				// 셀값이 날짜일경우
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					temp = f.format(cell.getDateCellValue());
					// 셀값이 숫자일경우
				} else {
					temp = String.valueOf(cell.getNumericCellValue());
				}
				break;
			case Cell.CELL_TYPE_STRING:
				temp = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_FORMULA:
				temp = cell.getCellFormula();
				break;
			/*
			 * case Cell.CELL_TYPE_BLANK: temp = ""; break;
			 */
			case Cell.CELL_TYPE_BOOLEAN:
				temp = String.valueOf(cell.getBooleanCellValue());
				break;
			default:
				temp = "";
				break;
			}

		}
		return temp;
	}

	public boolean isNum(Sheet sheet, int rowInt, int column) {
		Row row = sheet.getRow(rowInt);
		Cell cell = row.getCell(column);
		boolean resultBoolean = false;
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			if (!HSSFDateUtil.isCellDateFormatted(cell)) {
				resultBoolean = true;
			}
		}
		return resultBoolean;
	}

	public static void createExcel(File excelFileName, ExcelSVO svo) throws Exception {
		createExcel(excelFileName.getAbsolutePath(), svo);
	}

	/**
	 * @param excelFileName
	 * @param svo
	 * @throws Exception
	 */
	public static void createExcel(String excelFileName, ExcelSVO svo) throws Exception {
		createExcel(excelFileName, svo, true);
	}

	/**
	 * 2015. 11. 22. KYJ
	 *
	 * @처리내용 : 엑셀파일을 생성한다.
	 * @param excelFileName
	 * @param svo
	 * @param appendExtension
	 *            확장자를 붙일지 여부
	 * @throws Exception
	 */
	public static void createExcel(String excelFileName, ExcelSVO svo, boolean appendExtension) throws Exception {
		Workbook createNewWorkBookXlsx = createNewWorkBookXlsx();

		Iterator<String> iterator = svo.iterator();

		while (iterator.hasNext()) {

			String sheetName = iterator.next();
			List<ExcelColDVO> colDvoList = svo.getColDvoList(sheetName);
			Sheet createSheet = createNewWorkBookXlsx.createSheet(sheetName);

			List<ExcelDataDVO> sheetExcelDVOList = svo.getSheetExcelDVO(sheetName);

			if (colDvoList != null) {

				for (ExcelColDVO dvo : colDvoList) {
					/* 자동 사이즈 조절 */
					Cell createCell = createCell(createSheet, dvo.getColName(), 0, dvo.getColSeq());
					applyColor(createNewWorkBookXlsx, dvo, createCell);
				}
			}

			if (sheetExcelDVOList != null) {

				for (ExcelDataDVO dvo : sheetExcelDVOList) {
					Cell createCell = createCell(createSheet, dvo.getData(), dvo.getRow() + 1, dvo.getCol());
					applyColor(createNewWorkBookXlsx, dvo, createCell);

				}
			}

		}
		String extension = "";
		if (appendExtension)
			extension = "xlsx";

		createNewWorkBookXlsx.write(getFileOutputStream(excelFileName, extension));

	}

	private static void applyColor(Workbook createNewWorkBookXlsx, ExcelColDVO dvo, Cell createCell) {
		Color backgroundColor = dvo.getBackgroundColor();
		if (backgroundColor != null) {
			XSSFCellStyle style = (XSSFCellStyle) createNewWorkBookXlsx.createCellStyle();
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style.setFillForegroundColor(new XSSFColor(backgroundColor));
			createCell.setCellStyle(style);
		}
	}

	private static void applyColor(Workbook createNewWorkBookXlsx, ExcelDataDVO dvo, Cell createCell) {
		Color backgroundColor = dvo.getBackgroundColor();
		if (backgroundColor != null) {
			XSSFCellStyle style = (XSSFCellStyle) createNewWorkBookXlsx.createCellStyle();
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style.setFillForegroundColor(new XSSFColor(backgroundColor));
			createCell.setCellStyle(style);
		}
	}

	/**
	 * 2014. 10. 3. KYJ
	 *
	 * @param createJtable
	 * @return
	 * @throws Exception
	 * @처리내용 : JTable의 데이터를 이용하여 엑셀파일을 생성한다.
	 */
	public static boolean createExcel(JTable createJtable, String makeFile) throws Exception {

		int rowCount = createJtable.getRowCount();
		int columnCount = createJtable.getColumnCount();
		TableModel model = createJtable.getModel();

		ExcelSVO svo = new ExcelSVO();

		TableColumnModel columnModel = createJtable.getColumnModel();

		ArrayList<ExcelColDVO> arrayList = new ArrayList<ExcelColDVO>();
		for (int j = 0; j < columnCount; j++) {
			TableColumn column = columnModel.getColumn(j);
			Object headerValue = column.getHeaderValue();
			arrayList.add(new ExcelColDVO(j, (String) headerValue));
		}

		for (int i = 0; i < rowCount; i++) {

			for (int j = 0; j < columnCount; j++) {
				Object valueAt = model.getValueAt(i, j);
				svo.setColDvoList("sheet1", arrayList);
				svo.addSheetExcelDVO("sheet1", new ExcelDataDVO(i, j, valueAt));
			}
		}

		createExcel(makeFile, svo);

		return false;
	}

	/**
	 * Export Excel <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 28. 
	 * @param <T>
	 * @param createJtable
	 * @param outFile
	 * @return 
	 * @throws Exception
	 */
	public static <T> boolean createExcel(TableView<T> createJtable, File outFile) throws Exception {
		FxUtil.exportExcelFile(outFile, createJtable);
		return outFile.exists();
	}
	/**
	 * 특정셀에 코멘트를 추가한다.
	 *
	 * @param sheet
	 * @param cell
	 * @param commentText
	 * @return
	 */
	public static void addComment(Sheet sheet, Cell cell, String commentText) {
		XSSFDrawing patr = (XSSFDrawing) sheet.createDrawingPatriarch();
		Comment comment = patr.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		comment.setString(new XSSFRichTextString(commentText));
		cell.setCellComment(comment);
	}

	/**
	 * 엑셀파일을 K로 컨버트
	 *
	 * @param selectFile
	 * @param convert
	 * @return
	 * @throws Exception
	 */
	public static <K> K toK(File selectFile, BiFunction<File, Workbook, K> convert) throws Exception {
		Workbook xlsx = readXlsx(selectFile);
		return convert.apply(selectFile, xlsx);
	}
}
