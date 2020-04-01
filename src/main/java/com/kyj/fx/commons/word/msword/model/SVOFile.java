/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.filemodel
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.kyj.fx.commons.word.msword.vo.SourceAnalysisDVO;
import com.kyj.fx.commons.word.msword.vo.TableDVO;

/**
 * @author KYJ
 *
 */
public class SVOFile extends VOFile {

	/**
	 * @param f
	 * @throws Exception
	 */
	public SVOFile(File f) throws Exception {
		super(f);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see programspecification.model.file.AbstractProgreamSpecFile#anaysis()
	 */
	@Override
	public List<SourceAnalysisDVO> anaysis() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SOURCE_FILE_TYPE getSourceFileType() {
		return SOURCE_FILE_TYPE.SVO;
	}

	@Override
	public FILE_TYPE getFileType() {
		return FILE_TYPE.JAVA;
	}

	@Override
	public List<TableDVO> getTableList() {
		return new ArrayList<TableDVO>();
	}

}
