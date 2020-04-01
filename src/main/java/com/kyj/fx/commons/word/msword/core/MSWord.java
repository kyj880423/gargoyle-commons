/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.core
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.word.msword.interfaces.ITableCustomProperty;

public class MSWord {

	/***********************
	 * 사용 FONT SIZE
	 **********************/
	public static final int H1 = 15;
	public static final int H2 = 14;
	public static final int H3 = 13;
	public static final int H4 = 12;
	public static final int H5 = 11;
	public static final int DEFAULT_FONT_SIZE = 10;

	private static final String DEFAULT_FONT_NAME = "Tahoma";

	// Create a new document from scratch
	private XWPFDocument doc;
	private String fontName;

	public MSWord() {
		doc = new XWPFDocument();
		this.fontName = DEFAULT_FONT_NAME;
	}

	/**
	 * 페이지 마진을 설정한다.
	 */
	public void applyPageMargin() {

		CTSectPr addNewSectPr = doc.getDocument().getBody().addNewSectPr();
		CTPageMar addNewPgMar = addNewSectPr.addNewPgMar();
		addNewPgMar.setLeft(BigInteger.valueOf(720L));
		addNewPgMar.setTop(BigInteger.valueOf(720L));
		addNewPgMar.setRight(BigInteger.valueOf(720L));
		addNewPgMar.setBottom(BigInteger.valueOf(720L));
	}

	public XWPFDocument getDoc() {
		return doc;
	}

	/**
	 * 텍스트 입력
	 *
	 * @param p
	 * @param text
	 * @return
	 */
	public XWPFParagraph addText(XWPFParagraph p, String text) {
		return this.addText(p, text, false, false, false);
	}

	/**
	 * 텍스트 입력
	 *
	 * @param p
	 *
	 * @param text
	 * @param bold
	 * @param italic
	 * @param strike
	 * @return
	 */
	public XWPFParagraph addText(XWPFParagraph p, String text, boolean bold, boolean italic, boolean strike) {
		return addText(p, text, DEFAULT_FONT_SIZE, bold, italic, strike);
	}

	/**
	 * 텍스트 입력
	 *
	 * @param p
	 * @param text
	 * @param fontSize
	 * @param bold
	 * @param italic
	 * @param strike
	 * @return
	 */
	public XWPFParagraph addText(XWPFParagraph p, String text, int fontSize, boolean bold, boolean italic, boolean strike) {

		KrXWPFRun r1 = new KrXWPFRun(p.createRun());

		r1.setText(text);
		r1.setBold(bold);
		r1.setItalic(italic);
		r1.setFontFamily(fontName);
		// r1.setUnderline(UnderlinePatterns.DOT_DOT_DASH);
		r1.setStrike(strike);
		r1.setFontSize(fontSize);
		p.setAlignment(ParagraphAlignment.LEFT);
		// p.setSpacingLineRule(LineSpacingRule.EXACT);

		return p;
	}

	/**
	 * 텍스트 입력
	 *
	 * @param text
	 */
	public void addBlockText(String text, ParagraphAlignment alignment) {
		XWPFParagraph p1 = doc.createParagraph();
		p1.setAlignment(alignment);
		p1.setBorderBottom(Borders.DOUBLE);
		p1.setBorderTop(Borders.DOUBLE);

		p1.setBorderRight(Borders.DOUBLE);
		p1.setBorderLeft(Borders.DOUBLE);
		p1.setBorderBetween(Borders.SINGLE);

		p1.setVerticalAlignment(TextAlignment.TOP);

		// p1.setSpacingBefore(5);
		// p1.setSpacingAfter(5);
		// KrXWPFRun r1 = new KrXWPFRun(p1.createRun());
		// r1.setFontSize(13);
		// r1.setText(text);
		// r1.setFontFamily(fontName);

		addText(p1, text);
	}

	/**
	 * 텍스트 입력
	 *
	 * @param text
	 * @return
	 */
	public XWPFParagraph addText(String text) {
		return addText(doc.createParagraph(), text);
	}

	/**
	 * 텍스트 입력
	 *
	 * @param text
	 * @return
	 */
	public XWPFParagraph addText(String text, boolean bold, boolean italic, boolean strike) {
		return addText(doc.createParagraph(), text, bold, italic, strike);
	}

	/**
	 * 텍스트 입력
	 *
	 * @param text
	 * @return
	 */
	public XWPFParagraph addText(String text, int fontSize, boolean bold, boolean italic, boolean strike) {
		return addText(doc.createParagraph(), text, fontSize, bold, italic, strike);
	}

	/**
	 * 줄바꿈한다.
	 */
	public void addBreak() {
		XWPFParagraph p = doc.createParagraph();
		XWPFRun r1 = new KrXWPFRun(p.createRun());
		r1.addBreak();
	}

	/**
	 * 새로운 페이지로 넘긴다.
	 */
	public void addNewPage() {
		XWPFParagraph p = doc.createParagraph();
		XWPFRun r1 = p.createRun();
		r1.addBreak(BreakType.PAGE);
	}

	private static void addTableText(XWPFRun rh, String text) {
		if (text == null) {
			rh.setText("null");
			return;
		}

		String[] split = text.split("\n");
		int size = split.length;
		for (int i = 0; i < size; i++) {
			String d = split[i];
			rh.setText(ValueUtil.leftTrim(d));
			if (i < size - 1) {
				rh.addBreak();
			}
		}
	}

	public XWPFTable addToTable(List<List<String>> list) {
		return addToTable(list, null);
	}

	/**
	 * 테이블을 추가한다.
	 *
	 * List의 로우는 각 행을 의미. TreeSet은 테이블 컬럼Index 데이터를 의미.
	 *
	 * @param list
	 */
	public XWPFTable addToTable(List<List<String>> list, ITableCustomProperty property/*
																						* ,
																						* ThFunction
																						* <
																						* ,
																						* U
																						* ,
																						* V
																						* ,
																						* R
																						* >
																						*/) {

		if (list == null || list.isEmpty()) {
			return null;
		}

		// -- OR --
		// open an existing empty document with styles already defined
		// XWPFDocument doc = new XWPFDocument(new
		// FileInputStream("base_document.docx"));

		int rowSize = list.size();
		int colSize = list.get(0).size();

		XWPFTable table = doc.createTable(rowSize, colSize);

		// Set the table style. If the style is not defined, the table style
		// will become "Normal".
		{
			CTTblPr tblPr = table.getCTTbl().getTblPr();
			CTString styleStr = tblPr.addNewTblStyle();
			styleStr.setVal("StyledTable");
		}

		/* Table Width조절을 위한 작업 */
		{
			CTTbl ctTbl = table.getCTTbl();
			CTTblPr tblPr = ctTbl.getTblPr();
			CTTblWidth tblW = tblPr.getTblW();

			// 화면에 WIDTH를 딱 맞춤.
			tblW.setW(BigInteger.valueOf(5000));
			tblW.setType(STTblWidth.PCT);

		}

		// Get a list of the rows in the table
		// List<XWPFTableRow> rows = table.getRows();
		int rowCt = 0;
		int colCt = 0;

		for (List<String> set : list) {
			XWPFTableRow row = table.getRow(rowCt);
			CTTrPr trPr = row.getCtRow().addNewTrPr();
			// set row height; units = twentieth of a point, 360 = 0.25"
			CTHeight ht = trPr.addNewTrHeight();
			ht.setVal(BigInteger.valueOf(/* 360 */240));

			// get the cells in this row
			List<XWPFTableCell> cells = row.getTableCells();
			Iterator<String> it = set.iterator();

			while (it.hasNext()) {
				if (cells.size() == colCt) {
					row.createCell();
				}
				// get a table cell properties element (tcPr)
				XWPFTableCell cell = cells.get(colCt);

				CTTcPr tcpr = cell.getCTTc().addNewTcPr();
				// set vertical alignment to "center"

				CTVerticalJc va = tcpr.addNewVAlign();
				va.setVal(STVerticalJc.CENTER);
				// create cell color element
				CTShd ctshd = tcpr.addNewShd();
				ctshd.setColor("auto");
				ctshd.setVal(STShd.CLEAR);

				if (rowCt == 0) {
					// header row
					ctshd.setFill("FFFE99");
				} else if (rowCt % 2 == 0) {
					// even row
					// FFFFFF : 흰색
					ctshd.setFill("D3DFEE");
				} else {
					// odd row
					ctshd.setFill("EDF2F8");
				}

				// get 1st paragraph in cell's paragraph list
				XWPFParagraph para = cell.getParagraphs().get(0);
				// create a run to contain the content

				// 2015.3.17 fix
				// XWPFRun rh = para.createRun();
				KrXWPFRun rh = new KrXWPFRun(para.createRun());

				rh.setFontFamily(fontName);
				// style cell as desired
				if (colCt == colSize - 1) {
					// last column is 10pt Courier
					rh.setFontSize(DEFAULT_FONT_SIZE);

				}

				String content = it.next();
				if (rowCt == 0) {
					// header row
					rh.setText(content);
					rh.setFontSize(H5);
					rh.setBold(true);
					para.setAlignment(ParagraphAlignment.CENTER);
				} else if (rowCt % 2 == 0) {
					// even row

					addTableText(rh, content);
					rh.setSubscript(VerticalAlign.BASELINE);
					para.setAlignment(ParagraphAlignment.LEFT);
				} else {

					addTableText(rh, content);
					// odd row
					rh.setSubscript(VerticalAlign.BASELINE);
					para.setAlignment(ParagraphAlignment.LEFT);
				}

				colCt++;
			} // for cell
			colCt = 0;
			rowCt++;

		}
		if (property != null) {
			property.doCustom(table);
		}

		return table;
	}

	/**
	 * 수직셀 병합
	 *
	 * @param table
	 * @param col
	 * @param fromRow
	 * @param toRow
	 *            절대위치
	 */
	public void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {

		for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {

			XWPFTableCell cell = table.getRow(rowIndex).getCell(col);

			if (rowIndex == fromRow) {
				// The first merged cell is set with RESTART merge value
				cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
			} else {
				// Cells which join (merge) the first one, are set with CONTINUE
				cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
			}
		}
	}

	/**
	 * 수평셀 병합
	 *
	 * @param table
	 * @param rowNum
	 * @param colNum
	 * @param span
	 */
	public void mergeCellHorizon(XWPFTable table, int rowNum, int colNum, int span) {
		XWPFTableRow row = table.getRow(rowNum);
		XWPFTableCell cell = row.getCell(colNum);

		CTDecimalNumber grdSpan = cell.getCTTc().getTcPr().getGridSpan();
		if (grdSpan == null) {
			grdSpan = cell.getCTTc().getTcPr().addNewGridSpan();
		}

		grdSpan.setVal(BigInteger.valueOf((long) span));
	}

	/**
	 * 테이블의 특정 로우의 컬럼에 해당하는 부분의 셀을 나눔. 나누기는 하나.. 컬럼의 셀수가 일치하지 않고 틀어짐.
	 *
	 * @param table
	 * @param rowNum
	 * @param colNum
	 * @param span
	 */
	private static void spanCellsAcrossRow(XWPFTable table, int rowNum, int colNum, int span) {
		XWPFTableRow row = table.getRow(rowNum);
		XWPFTableCell cell = row.getCell(colNum);

		CTDecimalNumber grdSpan = cell.getCTTc().getTcPr().getGridSpan();
		if (grdSpan == null) {
			grdSpan = cell.getCTTc().getTcPr().addNewGridSpan();
		}

		grdSpan.setVal(BigInteger.valueOf((long) span));

		/*
		 * row가 0인경우 바로 아래래 로우의 컬럼수도 일치하도록 셀을 추가한다. 추가하지않는경우 컬럼셀의 수가 불일치한 상태로
		 * 문서가 만들어진다.
		 */

		// if (rowNum == 0)
		// {
		// addTableCell(DIRECTION._0, table, rowNum, span);
		// } else
		// {
		// addTableCell(DIRECTION.UP_DOWN, table, rowNum, span);
		// }

	}

	enum DIRECTION {
		_0, UP_DOWN;

	}

	private static void addTableCell(DIRECTION dir, XWPFTable table, int currentRow, int span) {
		int dirRowIndex = 0;
		if (DIRECTION._0 == dir) {
			dirRowIndex = currentRow + 1;
		} else if (DIRECTION.UP_DOWN == dir) {
			dirRowIndex = currentRow - 1;
		}

		XWPFTableRow belowRow = table.getRow(dirRowIndex);

		if (belowRow != null) {
			// 병합된 span만큼 셀을 추가한다.
			for (int i = 0; i < span - 1; i++) {
				belowRow.createCell();
			}
		}
	}

	/**
	 * 사진 추가.
	 *
	 * @param imgFile
	 * @param id
	 * @param width
	 * @param height
	 */
	public void addPicture(final String imgFile, final int id, int width, int height) {
		XWPFParagraph p = doc.createParagraph();
		XWPFRun r1 = new KrXWPFRun(p.createRun());

		addPicture(imgFile, id, width, height, r1);
	}

	/**
	 * 사진추가.
	 *
	 * @param imgFile
	 * @param id
	 * @param width
	 * @param height
	 * @param run
	 */
	public void addPicture(final String imgFile, final int id, int width, int height, final XWPFRun run) {
		FileInputStream fileInputStream = null;

		try {
			fileInputStream = new FileInputStream(imgFile);
			final String blipId = run.getDocument().addPictureData(fileInputStream, Document.PICTURE_TYPE_JPEG);

			final int EMU = 9525;
			width *= EMU;
			height *= EMU;
			// String blipId =
			// getAllPictures().get(id).getPackageRelationship().getId();

			final CTInline inline = run.getCTR().addNewDrawing().addNewInline();

			final String picXml = "" + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
					+ "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
					+ "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" + "         <pic:nvPicPr>"
					+ "            <pic:cNvPr id=\"" + id + "\" name=\"Generated\"/>" + "            <pic:cNvPicPr/>"
					+ "         </pic:nvPicPr>" + "         <pic:blipFill>" + "            <a:blip r:embed=\"" + blipId
					+ "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>" + "            <a:stretch>"
					+ "               <a:fillRect/>" + "            </a:stretch>" + "         </pic:blipFill>" + "         <pic:spPr>"
					+ "            <a:xfrm>" + "               <a:off x=\"0\" y=\"0\"/>" + "               <a:ext cx=\"" + width
					+ "\" cy=\"" + height + "\"/>" + "            </a:xfrm>" + "            <a:prstGeom prst=\"rect\">"
					+ "               <a:avLst/>" + "            </a:prstGeom>" + "         </pic:spPr>" + "      </pic:pic>"
					+ "   </a:graphicData>" + "</a:graphic>";

			XmlToken xmlToken = null;
			xmlToken = XmlToken.Factory.parse(picXml);
			inline.set(xmlToken);

			inline.setDistT(0);
			inline.setDistB(0);
			inline.setDistL(0);
			inline.setDistR(0);

			final CTPositiveSize2D extent = inline.addNewExtent();
			extent.setCx(width);
			extent.setCy(height);

			final CTNonVisualDrawingProps docPr = inline.addNewDocPr();
			docPr.setId(id);
			docPr.setName("Picture " + id);
			docPr.setDescr("Generated");
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			// close streams
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (final IOException ioEx) {
					// can be ignored
				}
			}
		}

	}

	/**
	 * 스트림을 닫고 문서를 만든다.
	 *
	 * @param docxName
	 * @throws IOException
	 */
	public void close(String docxName) throws IOException {
		close(new File(docxName));
	}

	public void close(File docxName) throws IOException {
		// write the file
		FileOutputStream out = new FileOutputStream(docxName);
		doc.write(out);
		out.close();
	}

}
