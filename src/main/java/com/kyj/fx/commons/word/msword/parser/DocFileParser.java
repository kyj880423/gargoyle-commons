/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.read
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.parser;

import java.io.FileInputStream;

import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MSword files. doc, xls, ppt 파서
 * 
 * @author Mubin Shrestha
 */
@SuppressWarnings("resource")
public class DocFileParser {
	private static Logger LOGGER = LoggerFactory.getLogger(DocFileParser.class);

	public String DocFileContentParser(String fileName) {

		POIFSFileSystem fs = null;
		try {
			fs = new POIFSFileSystem(new FileInputStream(fileName));
			if (fileName.endsWith(".doc")) {
				HWPFDocument doc = new HWPFDocument(fs);
				WordExtractor we = new WordExtractor(doc);
				return we.getText();
			} else if (fileName.endsWith(".xls")) {
				ExcelExtractor ex = new ExcelExtractor(fs);
				ex.setFormulasNotResults(true);
				ex.setIncludeSheetNames(true);
				return ex.getText();
			} else if (fileName.endsWith(".ppt")) {
				PowerPointExtractor extractor = new PowerPointExtractor(fs);
				return extractor.getText();
			}

		} catch (Exception e) {
			LOGGER.debug("document file cant be indexed");
		}
		return "";
	}
}
