/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.biz
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.template;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFTable;

import com.kyj.fx.commons.utils.DateUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.word.msword.core.MSWord;
import com.kyj.fx.commons.word.msword.interfaces.ITextureBlock;
import com.kyj.fx.commons.word.msword.vo.EtcDefineDVO;
import com.kyj.fx.commons.word.msword.vo.EtcDefineDVO.TYPE;
import com.kyj.fx.commons.word.msword.vo.ImportsDVO;
import com.kyj.fx.commons.word.msword.vo.MethodDVO;
import com.kyj.fx.commons.word.msword.vo.MethodParameterDVO;
import com.kyj.fx.commons.word.msword.vo.ProgramSpecSVO;
import com.kyj.fx.commons.word.msword.vo.Summarize;
import com.kyj.fx.commons.word.msword.vo.TableDVO;
import com.kyj.fx.commons.word.msword.vo.VocHistoryDVO;

public class ProgramSpecWordTemplate extends MSWord {

	private String docPath;
	private ProgramSpecSVO svo;

	public ProgramSpecWordTemplate(String docPath, ProgramSpecSVO svo) {
		this.docPath = docPath;
		this.svo = svo;
	}

	/**
	 * 기타 정의사항 , 변경이력 정의.
	 * 
	 * @param msWord
	 * @param svo
	 */
	public void foot(ProgramSpecSVO svo) {
		// 기타 정의사항 기술
		etcDefine(svo);
		// 페이지를 바꾼다.
		this.addNewPage();
		// 변경이력 기술
		changeHistory(this, svo);

	}

	/**
	 * 메소드에 대한 자세한 항목 기술.
	 * 
	 * @param msWord
	 * @param svo
	 */
	public void body(ProgramSpecSVO svo) {
		addBreak();
		// [START] #### ■ 프로그램 구성별 상세 사양서 작성(설계자) ####
		addText("■ 프로그램 구성별 상세 사양서 작성(설계자) ", MSWord.H4, true, false, false);
		addBreak();
		addText("0. Method 리스트와 Method 속성 ", true, false, false);

		// 전체 메소드에 대한 요약 정보 기술.
		List<List<String>> methodList = new ArrayList<List<String>>();
		// 0 row 헤더부분 기술
		{
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("No ");
			arr.add("Method ");
			arr.add("Level");
			arr.add("Visivility");
			arr.add("Description");
			methodList.add(arr);
		}
		// 1,2,3,4 row 공백 기술.
		List<MethodDVO> methodDVOList = svo.getMethodDVOList();
		if (methodDVOList.size() == 0) {
			for (int seq = 0; seq < 6; seq++) {
				ArrayList<String> arr = new ArrayList<String>();
				arr.add(seq + 1 + " ");
				arr.add("");
				arr.add("");
				arr.add("");
				arr.add("");
				methodList.add(arr);
			}
		} else {
			for (int seq = 0; seq < methodDVOList.size(); seq++) {
				MethodDVO dvo = methodDVOList.get(seq);
				ArrayList<String> arr = new ArrayList<String>();
				arr.add(seq + 1 + " ");
				arr.add(dvo.getMethodName());
				arr.add(dvo.getLevel());
				arr.add(dvo.getVisivility());
				arr.add(dvo.getDescription());
				methodList.add(arr);
			}
		}

		addToTable(methodList);
		methodDetail(svo.getMethodDVOList());

		// [END] #### ■ 프로그램 구성별 상세 사양서 작성(설계자) ####

		// [START] #### ■ 프로그램 구성별 상세 사양서 작성(개발자) → 프로그램 개발/테스트 완료 후 기술 ####
		addText("■ 프로그램 구성별 상세 사양서 작성(개발자) → 프로그램 개발/테스트 완료 후 기술", MSWord.H4, true, false, false);

		addText(" N/A ");
		// [END] #### ■ 프로그램 구성별 상세 사양서 작성(개발자) → 프로그램 개발/테스트 완료 후 기술 ####

		addNewPage();
	}

	/**
	 * 문서에 작성할 헤더부분.
	 * 
	 * @param msWord
	 * @param svo
	 */
	public void header(ProgramSpecSVO svo) {
		headerPage1(svo);
		headerPage2(svo);
		headerPage3(svo);
	}

	/**
	 * 사양서 명
	 * 
	 * @param msWord
	 * @param svo
	 */
	public void headerPage1(ProgramSpecSVO svo) {
		// TODO Auto-generated method stub

	}

	/**
	 * 사양서 버젼 관리
	 * 
	 * @param msWord
	 * @param svo
	 */
	public void headerPage2(ProgramSpecSVO svo) {
		// TODO Auto-generated method stub

	}

	/**
	 * 서브시스템을 정의함. MES2.0 명명규칙을 이용한 참조.
	 * 
	 * @param packageName
	 * @param string
	 * @return
	 */
	private static String getSubSystemName(String projectName, String packageName) {
		if (projectName != null) {
			if ("gmes2-base".equals(projectName)) {
				return "SM";
			}
		}
		String module = "ETC";
		if (ValueUtil.isEmpty(packageName)) {
			return module;
		}

		if (packageName.indexOf("com.samsung.gmes2.") >= 0) {
			// com.samsung.gmes2.모듈.
			String[] split = packageName.split("\\.");
			try {
				module = split[3];
			} catch (ArrayIndexOutOfBoundsException e) {
				// /N/A
			}

			if ("base".equals(module)) {
				module = "SM";
			} else if ("ifhub".equals(module)) {
				module = "INTERFACE";
			} else if ("Template".equals(module) || "sample".equals(module) || "eis".equals(module) || "commvo".equals(module)) {
				module = "ETC";
			}
		}

		return module.toUpperCase();
	}

	/**
	 * 
	 * 전반적인 메소드에 대한 정보를 기록. 본문 요약 정보
	 * 
	 * @param msWord
	 * @param svo
	 * @throws ParseException
	 */
	public void headerPage3(ProgramSpecSVO svo) {

		// [START] #### 선언부 테이블에 전체적인 내용 기술 ####
		List<List<String>> list = new ArrayList<List<String>>();

		// 0 row
		{
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("프로그램 사양서");
			list.add(arr);
		}
		// 1 row
		{
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("a.시스템 명");
			arr.add(" - ");
			arr.add("b.시스템");
			arr.add(" - ");
			arr.add("c.서브시스템");
			arr.add(getSubSystemName(svo.getUserSourceMetaDVO().getProjectName(), svo.getUserSourceMetaDVO().getPackages()));
			list.add(arr);
		}
		// 2 row
		{
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("d.프로그램ID");

			arr.add(svo.getUserSourceMetaDVO().getSimpleFileName());
			arr.add("e.작성자");
			arr.add(svo.getUserSourceMetaDVO().getUserPcName());
			arr.add("f.작성일");
			arr.add(DateUtil.getCurrentDateString("yyyy-MM-dd"));
			list.add(arr);
		}
		// 3 row
		{
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("g.프로그램명");
			// 프로그램명정의
			String programName = svo.getUserSourceMetaDVO().getProgramName();
			arr.add(programName == null ? "" : programName);
			list.add(arr);
		}
		// 4 row
		{
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("h.개발 유형");
			arr.add(svo.getFile().getFileType().toString());
			arr.add("i.프로그램 유형");
			arr.add(" - ");

			list.add(arr);
		}
		// 5 row
		{
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("j. 프로그램 개요");
			arr.add(" - ");

			list.add(arr);
		}

		XWPFTable table = addToTable(list);

		mergeCellHorizon(table, 0, 0, 6);
		mergeCellHorizon(table, 3, 1, 6);
		mergeCellHorizon(table, 4, 1, 2);
		mergeCellHorizon(table, 4, 3, 6);
		mergeCellHorizon(table, 5, 1, 6);

		// [END] #### 선언부 테이블에 전체적인 내용 기술 ####
		// 공백
		addBreak();

		addText("■. 화면/Report/비즈니스 로직 설계", MSWord.H4, true, false, false);
		addBreak();
		addText("■ Object  리스트", true, false, false);

		List<List<String>> objList = new ArrayList<List<String>>();

		// 0 row 테이블 헤더.
		{
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("순번 ");
			arr.add("Object ID ");
			arr.add("주요 기능 ");
			arr.add("신규/변경 ");
			objList.add(arr);
		}
		// [START] #### 데이터부 ####

		List<MethodDVO> methodDVOList = svo.getMethodDVOList();
		// 아래부분에 Object ID 목록에 사용하기 위함.

		for (int i = 0; i < methodDVOList.size(); i++) {
			MethodDVO methodDVO = methodDVOList.get(i);
			if (methodDVO == null || methodDVO.getMethodName() == null) {
				continue;
			}

			ArrayList<String> arr = new ArrayList<String>();
			// seq
			arr.add(String.valueOf((i + 1)));
			// 메소드명
			arr.add(methodDVO.getMethodName());
			// 주요기능-- Main펑션이 빈경우 Description으로 설정됨.
			arr.add(ValueUtil.isEmpty(methodDVO.getMainFunction()) ? methodDVO.getDescription() : methodDVO.getMainFunction());
			// 신규 or 변경
			arr.add(methodDVO.getIsNewOrChg());

			objList.add(arr);
		}

		// [END] #### 데이터부 ####
		addToTable(objList);

		addBreak();

		// [START] #### Object 선언부(패키지) ####
		addText("■ Object  선언", true, false, false);
		ImportsDVO importsDVO = svo.getImportsDVO();
		if (importsDVO != null) {
			List<String> imports = importsDVO.getImports();
			doNotEmptyTextureScope(imports, new ITextureBlock() {
				@Override
				public void doScope(String text) {
					addText(text);
				}

				@Override
				public void emptyThen() {
					addText("N/A");
				}
			});
		} else {
			addText("디폴트 (N/A)");
		}
		// [END] #### Object 선언부(패키지) ####
		addBreak();

		// [START] #### ObjectID (Object 리스트에 기술된 Object ID와 일치하게 기술함) ####
		addText("■ Object ID (Object 리스트에 기술된 Object ID와 일치하게 기술함) ", true, false, false);

		for (int i = 0; i < methodDVOList.size(); i++) {
			MethodDVO methodDVO = methodDVOList.get(i);
			if (methodDVO == null || methodDVO.getMethodName() == null) {
				continue;
			}
			String visivility = methodDVO.getVisivility();
			String methodName = methodDVO.getMethodName();
			String returnType = methodDVO.getReturnType();
			String parameters = methodDVO.getParameters();

			addText(new StringBuffer().append((i + 1)).append(". ").append(visivility).append(" ").append(returnType).append(" ")
					.append(methodName).append(" ").append(parameters).toString());
		}

		// [END] #### (Object 리스트에 기술된 Object ID와 일치하게 기술함) ####
		addBreak();
		// [START] #### Table 리스트(프로그램에서 사용되는 Table을 모두 기술) ####

		addText("  ○ Table 리스트(프로그램에서 사용되는 Table을 모두 기술)", true, false, false);

		List<List<String>> tableList = new ArrayList<List<String>>();
		// 0 row 헤더부분 기술
		{
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("Table ID");
			arr.add("Table 명");
			arr.add("CRUD");
			tableList.add(arr);
		}

		List<TableDVO> tableDVOList = svo.getTableDVOList();
		int DEFAULT_ROW = 4;
		// /
		if (tableDVOList != null) {
			for (TableDVO dvo : tableDVOList) {
				ArrayList<String> arr = new ArrayList<String>();
				arr.add(dvo.getTableId());
				arr.add(dvo.getTableName());
				arr.add(dvo.getCrud());
				tableList.add(arr);
			}
		}

		// 여유 공간이 남는다면 빈공간을 추가한다.
		int loopCnt = DEFAULT_ROW;
		if (tableDVOList != null) {
			loopCnt = DEFAULT_ROW - tableDVOList.size();
		}

		for (int i = 0; i < loopCnt; i++) {
			ArrayList<String> arr = new ArrayList<String>();
			arr.add(" ");
			arr.add(" ");
			arr.add(" ");
			tableList.add(arr);
		}
		// /

		addToTable(tableList);
		// [END] #### Table 리스트(프로그램에서 사용되는 Table을 모두 기술) ####

	}

	public void methodDetail(List<MethodDVO> dvoList) {

		addBreak();
		int seq = 0;
		for (MethodDVO dvo : dvoList) {
			methodDetail(++seq, dvo);
		}
	}

	public void methodDetail(int seq, MethodDVO dvo) {
		int DEFAULT_ROW = 4;
		addText(seq + ". ".concat(dvo.getMethodName()), true, false, false);
		// 전체 메소드에 대한 요약 정보 기술.
		List<List<String>> methodList = new ArrayList<List<String>>();
		// 0 row 메소드명 기술
		{
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("Method 명 ");
			arr.add(dvo.getMethodName());
			methodList.add(arr);
		}
		// 1 row 메소드 유형 기술
		{
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("Method 유형 ");
			arr.add(dvo.getLevel());
			methodList.add(arr);
		}
		// 2 row 부턴 메소드 파라미터 기술.
		{
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("Parameters");
			arr.add("Parameter");
			arr.add("유형(M/O)");
			arr.add("Type");
			arr.add("Description");
			methodList.add(arr);
		}

		List<MethodParameterDVO> mList = dvo.getMethodParameterDVOList();

		if (mList != null) {
			for (MethodParameterDVO m : mList) {
				ArrayList<String> arr = new ArrayList<String>();
				arr.add("");
				arr.add(m.getParameter());
				arr.add(m.getParameterType());
				arr.add(m.getType());
				arr.add(m.getDescription());
				methodList.add(arr);
			}
		}

		// 여유 공간이 남는다면 빈공간을 추가한다.
		int loopCnt = DEFAULT_ROW;
		int mListSize = mList == null ? 0 : mList.size();
		if (mList != null) {
			loopCnt = DEFAULT_ROW - mListSize;
		}

		for (int i = 0; i < loopCnt; i++) {
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("");
			arr.add("");
			arr.add("");
			arr.add("");
			arr.add("");
			methodList.add(arr);
		}

		XWPFTable table = addToTable(methodList);
		mergeCellHorizon(table, 0, 1, 4);
		mergeCellHorizon(table, 1, 1, 4);
		mergeCellsVertically(table, 0, 2, ((loopCnt < 0) ? (mListSize + 2) : (mListSize + loopCnt + 2)));

		if (dvo.getFlow() != null) {
			String[] split = dvo.getFlow().split("\n");
			for (String c : split) {
				// " "를 넣어주면 /t 기능이 msword상에서 적용됨.
				addText(" " + c);
			}
		}
		addBreak();
	}

	/**
	 * 4. 기타 정의 사항
	 * 
	 * @param msWord
	 * @param svo
	 */
	private void etcDefine(ProgramSpecSVO svo) {

		// [START] #### ⓝ 4. 기타 정의 사항 ####
		// addText("n. 기타 정의 사항", true, false, false);

		EtcDefineDVO etcDefineDVO = svo.getEtcDefineDVO();

		List<Summarize> summarizeList = etcDefineDVO.toSummarize(true);

		for (Summarize s : summarizeList) {
			if (TYPE.TITLE == s.group) {
				addText(s.text, MSWord.H4, s.bold, false, false);
			} else {
				addText(s.text, s.bold, false, false);
			}

		}

	}

	/**
	 * 5. 변경이력
	 * 
	 * @param msWord
	 * @param svo
	 *            VOC이력 정보.. 데이터는 modifyDate로 정렬되었다고 가정.
	 */
	private void changeHistory(MSWord msWord, ProgramSpecSVO svo) {
		final int DEFAULT_ROW = 12;
		List<VocHistoryDVO> vocHistoryDVOList = svo.getVocHistoryDVOList();

		msWord.addText("■ 변경이력", MSWord.H4, true, false, false);

		// 이력 정보 기술
		List<List<String>> vocHistoryList = new ArrayList<List<String>>();
		// 0 row 헤더 기술
		{
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("변경일자");
			arr.add("VOC No");
			arr.add("변경 레코드");
			arr.add("설계자");
			arr.add("변경 내용");
			vocHistoryList.add(arr);
		}

		if (vocHistoryDVOList != null) {
			for (VocHistoryDVO dvo : vocHistoryDVOList) {
				ArrayList<String> arr = new ArrayList<String>();
				// 변경일자
				try {
					String yyyy = dvo.getModifyDate().substring(0, 4);
					String mm = dvo.getModifyDate().substring(4, 6);
					String dd = dvo.getModifyDate().substring(6, 8);
					Calendar instance = Calendar.getInstance();
					instance.set(Integer.parseInt(yyyy), Integer.parseInt(mm), Integer.parseInt(dd));

					String currentDate = DateUtil.getDateString(instance.getTimeInMillis(), DateUtil.SYSTEM_DATEFORMAT_YYYY_MM_DD);
					arr.add(currentDate);
				} catch (Exception e) {
					arr.add(dvo.getModifyDate());
				}

				// VOC번호
				arr.add(dvo.getVocNo());
				// 변경레코드
				arr.add(dvo.getChgRecord());
				// 설계자
				arr.add(dvo.getPlannerName());
				// 변경내용
				arr.add(dvo.getChgContent());
				vocHistoryList.add(arr);
			}
		}

		// 여유 공간이 남는다면 빈공간을 추가한다.
		int loopCnt = DEFAULT_ROW;
		if (vocHistoryDVOList != null) {
			loopCnt = DEFAULT_ROW - vocHistoryDVOList.size();
		}
		for (int i = 0; i < loopCnt; i++) {
			ArrayList<String> arr = new ArrayList<String>();
			// 변경일자
			arr.add("");
			// VOC번호
			arr.add("");
			// 변경레코드
			arr.add("");
			// 설계자
			arr.add("");
			// 변경내용
			arr.add("");
			vocHistoryList.add(arr);

		}

		msWord.addToTable(vocHistoryList);

		// 기타 설명 내용 추가.

		msWord.addText("※ 본 페이지는 각 조직의 운영 특성을 고려하여 개정이력표 뒤에 작성해도 무방함. ", true, false, false);
		msWord.addText("   단, 개정이력표 뒤에 작성하는 경우는 본 페이지는 작성하지 않음.(선택) ", true, false, false);
		msWord.addText("※ 설계 변경 시 본문상 변경부분(최신 버전 기준)은 서체 구분 표기 바랍니다. ", true, false, false);
		msWord.addText("   (서체지정 > 바탕색 지정 : 노란색) 이전 버전 수정 내용은 색상구분 해제함 ", true, false, false);

	}

	/**
	 * 프로그램 사양서의 내용을 작성
	 */
	public void write() {
		// 페이지 마진을 설정.
		applyPageMargin();
		// 본문 헤더 작성
		header(svo);
		// 본문 내용 작성
		body(svo);
		// 기타정의사항, VOC 사항 작성
		foot(svo);
	}

	public void close() throws IOException {
		super.close(this.docPath);
	}

	/**
	 * list의 데이터가 비어있지 않는 데이터를 block에 전달.
	 * 
	 * @param list
	 * @param block
	 */
	public static void doNotEmptyTextureScope(List<String> list, ITextureBlock block) {
		doNotEmptyTextureScope(list, block, true);
	}

	/**
	 * list의 데이터가 비어있지 않는 데이터를 block에 전달.
	 * 
	 * @param list
	 * @param block
	 * @param autoNumbering
	 *            텍스트에 자동으로 넘버링값을 추가할지 유무
	 */
	public static void doNotEmptyTextureScope(List<String> list, ITextureBlock block, boolean autoNumbering) {
		if (list == null || list.isEmpty()) {
			block.emptyThen();
			return;
		}

		int seq = 1;
		for (String str : list) {
			if (str != null) {
				if (autoNumbering) {
					block.doScope(String.valueOf(seq++).concat(". ").concat(str));
				} else {
					block.doScope(str);
				}
			}

		}
	}

}
