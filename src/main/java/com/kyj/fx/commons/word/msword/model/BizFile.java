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
public class BizFile extends BusinessFile {

	public BizFile(File f) throws Exception {
		super(f);
	}

	@Override
	public SOURCE_FILE_TYPE getSourceFileType() {
		return SOURCE_FILE_TYPE.BIZ;
	}

	@Override
	public List<SourceAnalysisDVO> anaysis() {

		return null;
	}

	@Override
	public FILE_TYPE getFileType() {
		return FILE_TYPE.JAVA;
	}

	@Override
	public List<TableDVO> getTableList() {
		// TODO 처리..?
		return new ArrayList<TableDVO>();
	}

}
