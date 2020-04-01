/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.read
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.parser;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

@SuppressWarnings("resource")
public class DocxFileParser {
	public String docxFileContentParser(String fileName) {
		try {
			FileInputStream fs = new FileInputStream(new File(fileName));
			OPCPackage d = OPCPackage.open(fs);
			if (fileName.endsWith(".docx")) {
				XWPFWordExtractor xw = new XWPFWordExtractor(d);
				return xw.getText();
			} else if (fileName.endsWith(".pptx")) {
				XSLFPowerPointExtractor xp = new XSLFPowerPointExtractor(d);
				return xp.getText();
			} else if (fileName.endsWith(".xlsx")) {
				XSSFExcelExtractor xe = new XSSFExcelExtractor(d);
				xe.setFormulasNotResults(true);
				xe.setIncludeSheetNames(true);
				return xe.getText();
			}
		} catch (Exception e) {
			System.out.println("# DocxFileParser Error :" + e.getMessage());
		}
		return "";
	}

	public static void main(String args[]) {
		String filePath = "fullPath";
		System.out.println(new DocxFileParser().docxFileContentParser(filePath));
	}
}
