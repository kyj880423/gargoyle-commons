/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.vo
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.vo;

import java.util.ArrayList;
import java.util.List;

import com.kyj.fx.commons.word.msword.model.IProgramSpecFile;

/**
 * 프로그램 사양서를 작성하기 위해 종합적인 메타정보를 기록하는 데이터셋
 *
 * @author KYJ
 *
 */
public class ProgramSpecSVO {
	/**
	 * 추상화된 검사 대상 파일
	 */
	private IProgramSpecFile file;
	/**
	 * 메소드항목을 정의할 데이터셋
	 */
	private List<MethodDVO> methodDVOList;

	/**
	 * 테이블정의 항목에 관한 데이터를 정리할 데이터셋
	 */
	private List<TableDVO> tableDVOList;

	/**
	 * VOC 이력을 정리할 데이터셋
	 */
	private List<VocHistoryDVO> vocHistoryDVOList;

	/**
	 * 사용자 정의 항목에 정의할 데이터셋
	 */
	private EtcDefineDVO etcDefineDVO;

	/**
	 * 임포트에 관한 데이터 정리
	 */
	private ImportsDVO importsDVO;

	/**
	 * 개발자 프로그램에 대한 메타정보를 기록
	 */
	private UserSourceMetaDVO userSourceMetaDVO;

	public ProgramSpecSVO() {
		super();
		methodDVOList = new ArrayList<MethodDVO>();
		importsDVO = new ImportsDVO();
		vocHistoryDVOList = new ArrayList<VocHistoryDVO>();
		tableDVOList = new ArrayList<TableDVO>();
		etcDefineDVO = new EtcDefineDVO();
		userSourceMetaDVO = new UserSourceMetaDVO();
	}

	public IProgramSpecFile getFile() {
		return file;
	}

	public void setFile(IProgramSpecFile file) {
		this.file = file;
	}

	public List<MethodDVO> getMethodDVOList() {
		return methodDVOList;
	}

	public void setMethodDVOList(List<MethodDVO> methodDVOList) {
		this.methodDVOList = methodDVOList;
	}

	public List<TableDVO> getTableDVOList() {
		return tableDVOList;
	}

	public void setTableDVOList(List<TableDVO> tableDVOList) {
		this.tableDVOList = tableDVOList;
	}

	public List<VocHistoryDVO> getVocHistoryDVOList() {
		return vocHistoryDVOList;
	}

	public void setVocHistoryDVOList(List<VocHistoryDVO> vocHistoryDVOList) {
		this.vocHistoryDVOList = vocHistoryDVOList;
	}

	public EtcDefineDVO getEtcDefineDVO() {
		return etcDefineDVO;
	}

	public void setEtcDefineDVO(EtcDefineDVO etcDefineDVO) {
		this.etcDefineDVO = etcDefineDVO;
	}

	public ImportsDVO getImportsDVO() {
		return importsDVO;
	}

	public void setImportsDVO(ImportsDVO importsDVO) {
		this.importsDVO = importsDVO;
	}

	public UserSourceMetaDVO getUserSourceMetaDVO() {
		return userSourceMetaDVO;
	}

	public void setUserSourceMetaDVO(UserSourceMetaDVO userSourceMetaDVO) {
		this.userSourceMetaDVO = userSourceMetaDVO;
	}

}
