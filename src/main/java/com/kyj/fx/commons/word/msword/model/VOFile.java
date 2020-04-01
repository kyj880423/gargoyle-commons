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
public class VOFile extends AbstractJavaProgramSpecFile {

	public VOFile(File f) throws Exception {
		super(f);
		// TODO Auto-generated constructor stub
	}

	@Override
	public SOURCE_FILE_TYPE getSourceFileType() {
		return SOURCE_FILE_TYPE.DVO;
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
		//주석
//		String fileSimpleName = getFileSimpleName();
//		String demName = fileSimpleName.substring(0, fileSimpleName.toUpperCase().lastIndexOf("VO"));
//		// 결과값 반환 list
		List<TableDVO> arrayList = new ArrayList<TableDVO>();
//
//		StringBuffer sb = new StringBuffer();
//		sb.append("SELECT A.TABLE_NAME, B.COMMENTS\n");
//		sb.append("FROM   ALL_TABLES A, ALL_TAB_COMMENTS B  \n");
//		sb.append(" WHERE  A.TABLE_NAME = B.TABLE_NAME(+)\n");
//		sb.append("AND  A.TABLE_NAME = '" + DbOracleUtil.getTableName(demName) + "'\n");
//		sb.append("AND B.OWNER = 'GMES20DBA'\n");
//		sb.append(" \n");
//		sb.toString();
//
//		try {
//			List<Map<String, Object>> select = DbOracleUtil.select(sb.toString());
//			if (ValueUtil.isNotEmpty(select)) {
//				for (Map<String, Object> map : select) {
//					if (map == null)
//						continue;
//
//					String tableName = (String) map.get("TABLE_NAME");
//					String comment = (String) map.get("COMMENTS");
//					if (ValueUtil.isNotEmpty(tableName)) {
//						TableDVO tableDVO = new TableDVO();
//						tableDVO.setTableId(tableName);
//						tableDVO.setTableName(comment);
//						// TODO CRUD가 뭐지...ㅋ
//						tableDVO.setCrud("");
//						arrayList.add(tableDVO);
//					}
//				}
//			}
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}

		return arrayList;
	}
}
