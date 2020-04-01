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

public abstract class XframeFile extends AbstractXframeProgramSpecFile
{

	public XframeFile(File f) throws Exception
	{
		super(f);
		// TODO Auto-generated constructor stub
	}

	@Override
	public FILE_TYPE getFileType()
	{
		return FILE_TYPE.JAVASCRIPT;
	}

	@Override
	public List<SourceAnalysisDVO> anaysis()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TableDVO> getTableList()
	{
		// TODO 주석
		// String menuId = getFileSimpleName();
		//
		// // 결과값 반환 list
		List<TableDVO> arrayList = new ArrayList<TableDVO>();
		//
		// StringBuffer sb = new StringBuffer();
		// sb.append("SELECT A.TAB_NM AS TABLE_NAME, C. COMMENTS \n");
		// sb.append("FROM TBD_SM_SCREEN_TAB A, ALL_TABLES B , ALL_TAB_COMMENTS
		// C\n");
		// sb.append("WHERE 1=1\n");
		// sb.append("AND A.SCREEN_ID = '" + menuId + "'\n");
		// sb.append("AND A.TAB_NM = B.TABLE_NAME\n");
		// sb.append("AND A.TAB_NM = C.TABLE_NAME \n");
		// sb.append("AND C.OWNER = 'GMES20DBA'\n");
		// sb.append("AND A.DEL_YN = 'N'\n");
		// sb.append("AND A.USE_YN = 'Y'\n");
		// sb.toString();
		//
		// try {
		// List<Map<String, Object>> select =
		// DbOracleUtil.select(sb.toString());
		// if (ValueUtil.isNotEmpty(select)) {
		// for (Map<String, Object> map : select) {
		// if (map == null)
		// continue;
		//
		// String tableName = (String) map.get("TABLE_NAME");
		// String comment = (String) map.get("COMMENTS");
		// if (ValueUtil.isNotEmpty(tableName)) {
		// TableDVO tableDVO = new TableDVO();
		// tableDVO.setTableId(tableName);
		// tableDVO.setTableName(comment);
		// // TODO CRUD가 뭐지...ㅋ
		// tableDVO.setCrud("");
		// arrayList.add(tableDVO);
		// }
		// }
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		return arrayList;
	}

}
