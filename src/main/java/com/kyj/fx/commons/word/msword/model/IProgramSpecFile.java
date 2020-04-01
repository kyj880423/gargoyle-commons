/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.filemodel
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.model;

import java.util.List;

import com.kyj.fx.commons.word.msword.biz.InspectorSourceMeta;
import com.kyj.fx.commons.word.msword.vo.TableDVO;

/**
 * @author KYJ 프로그램 사양서 관리 인터페이스 객체
 */
public interface IProgramSpecFile
{

	/**
	 * 파일에 대한 유형 정의
	 *
	 * @author KYJ
	 *
	 */
	public enum FILE_TYPE
	{
		JAVA, JAVASCRIPT, XML, SQL
	};

	/**
	 * 소스파일에 대한 유형 상세
	 *
	 * @author KYJ
	 *
	 */
	public enum SOURCE_FILE_TYPE
	{
		XFRAME_JS, XFRAME_XML, APP, BIZ, DEM, DQM, VO, DVO, SVO
	};

	/**
	 * XFRAME의 XML인지 JS인 타입 확인
	 *
	 * @return
	 */
	public SOURCE_FILE_TYPE getSourceFileType();

	public InspectorSourceMeta getInspectorSourceMeta();

	/**
	 * 파일명 반환
	 *
	 * @return
	 */
	public String getFileName();

	public FILE_TYPE getFileType();

	public List<TableDVO> getTableList();

}
