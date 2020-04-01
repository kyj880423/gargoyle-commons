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

import org.apache.commons.io.FileUtils;

import com.kyj.fx.commons.code.java.comment.CodeCommentFactory;
import com.kyj.fx.commons.exceptions.ProgramSpecSourceException;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.word.msword.biz.InspectorSourceMeta;
import com.kyj.fx.commons.word.msword.biz.XframeJsAnalysis;
import com.kyj.fx.commons.word.msword.vo.MethodDVO;
import com.kyj.fx.commons.word.msword.vo.MethodParameterDVO;
import com.kyj.fx.commons.word.msword.vo.SourceAnalysisDVO;
import com.kyj.fx.commons.word.msword.vo.TableDVO;

/**
 * @author KYJ
 *
 */
public abstract class AbstractXframeProgramSpecFile implements IProgramSpecFile {

	// 파일
	private File f;

	private String projectName;

	private List<SourceAnalysisDVO> listStatement;

	private InspectorSourceMeta meta;

	public AbstractXframeProgramSpecFile(File f) throws Exception {
		this.f = f;
		@SuppressWarnings("unchecked")
		List<String> readLines = FileUtils.readLines(this.f, "UTF-8");
		meta = new InspectorSourceMeta(this.f.getName(), readLines);

		if (SOURCE_FILE_TYPE.XFRAME_JS == this.getSourceFileType()) {
			XframeJsAnalysis xframeJsAnalysis = new XframeJsAnalysis(meta);
			listStatement = xframeJsAnalysis.findStatement();
		}

	}

	/**
	 * 파일명 반환
	 *
	 * @return
	 */
	public String getFileName() {
		return f.getName();
	}

	/**
	 * 파일명 반환
	 *
	 * @return
	 */
	public String getFullFileName() {
		return f.getAbsolutePath();
	}

	/**
	 * 프로젝트명
	 *
	 * @return
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * 프로젝트명
	 *
	 * @param projectName
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public List<SourceAnalysisDVO> listStatement() {
		return listStatement;
	}

	/**
	 * 메소드의 주석을 찾은 다음 반환
	 *
	 * @param methodName
	 *            접근지정자, 반환값, 메소드명.. exception등이 포함된 메소드명
	 * @return
	 */
	public String getMethodDescription(String methodName) {

		SourceAnalysisDVO d = null;

		// 파라미터로 들어온 메소드를 찾는다.
		for (SourceAnalysisDVO dvo : this.listStatement()) {
			if (methodName.equals(dvo.getMethodName())) {
				d = dvo;
				break;
			}
		}
		// 못찾으면 리턴
		if (d == null) {
			return "";
		}

		/*
		 * 메소드의 시작 라인지점. [주의] 메소드의 시작지점은 상황에 따라서 메소드의 라인이 반환될수있다. 아래에 if문으로
		 * 메소드라인이면 continue문이 존재하는데 그 지점은 skip하여 진행한다.
		 */
		int startLine = d.getMethodBlockStart() - 2;
		List<String> codeList = meta.getSourceCodeList();
		boolean stopFor = false;
		int findCommentStartLine = 0;
		int findCommentEndLine = 0;

		// 주석문장을 찾기위한 루프문
		for (int index = startLine; index >= 0; index--) {
			String code = codeList.get(index).trim();
			boolean commentFlag = false;

			/* 메소드문에 해당하는 라인이면 SKIP한다. */
			if (code.equals(methodName))
				continue;

			switch (CodeCommentFactory.getCommentContain(code)) {
			/* 주석끝부분만 존재 */
			case 0x001:
				findCommentEndLine = index;
				commentFlag = false;
				break;
			/* 주석시작부만존재 */
			case 0x010:
				/* 코멘트 시작부 시작 */
				findCommentStartLine = index;
				commentFlag = true;
				break;
			/* 주석이 시작끝이 다 포함됨 */
			case 0x011:
				findCommentStartLine = index;
				findCommentEndLine = index;
				commentFlag = true;
				break;
			/* 빈값 */
			case 0x100:
				break;
			/* 일반텍스트 */
			default:
				/* 주석이 아니면서 */
				if (!commentFlag) {
					/* 어노테이션이면 skip */
					if (code.startsWith("@")) {

					}
					/* 주석에 속하는 부분이면 skip */
					else if (code.startsWith("*")) {

					}
					/* 만약 일반 텍스트를 만난다면 루프문종료 */
					else {
						stopFor = true;
					}
				}
				break;
			}

			// 주석부분의 시작점을 찾았다면 그 문장을 반환한다.
			if (commentFlag) {
				StringBuffer sb = new StringBuffer();
				for (int i = findCommentStartLine; i <= findCommentEndLine; i++) {
					sb.append(codeList.get(i)).append("\n");
				}
				return sb.toString();
			}

			/*
			 * 만약 주석부분을 못찾게된 경우 루프를 멈춘다. 주석부분을 못찾는 기준은 주석문장이 아닌 소스코드에 해당하는
			 * 위치인경우임.
			 */
			if (stopFor) {
				break;
			}

		}
		return "";
	}

	/**
	 * 소스코드 분석
	 *
	 * @return
	 */
	public abstract List<SourceAnalysisDVO> anaysis();

	/**
	 *
	 */
	public void toProgreamSpec() {

	}

	public InspectorSourceMeta toInspectorSourceMeta() {
		return null;
	}

	/**
	 * 경로 및 확장자가 없는 단순한 이름
	 *
	 * @return
	 */
	public String getFileSimpleName() {
		String simple = this.f.getName();
		return simple.substring(0, simple.indexOf('.'));
	}

	/**
	 * 소스ID를 이용하여 MES 화면 ID에 해당하는 단순한 이름.
	 *
	 * @return
	 */
	public abstract List<TableDVO> getTableList();

	@Override
	public String toString() {
		return f.getName();
	}

	public static MethodDVO toMethodDVO(String fullMethodName) throws ProgramSpecSourceException {

		String tempName = fullMethodName;
		MethodDVO methodDVO = new MethodDVO();
		methodDVO.setLevel("");

		// 파라미터부분 반환
		String parameterMatched = ValueUtil.regexMatch("\\([\\w\\W]{0,}\\)", tempName).trim();
		if (ValueUtil.isEmpty(parameterMatched)) {
			throw new ProgramSpecSourceException("[버그]파라미터 블록 확인바람.");
		}

		// 파라미터값 추출.
		int startParam_indexOf = parameterMatched.indexOf("(");
		int endParam_indexOf = parameterMatched.indexOf(")");

		String parameterContent = parameterMatched.substring(startParam_indexOf + 1, endParam_indexOf);
		System.out.println(parameterContent);
		List<MethodParameterDVO> convertParameterDVO = convertParameterDVO(parameterContent);
		System.out.println(convertParameterDVO);

		// Simple MethodName 추출.
		int indexOf = tempName.indexOf(parameterMatched);
		// [시작] 2015.3.5 파라미터에 [] 문자가 존재할 경우 정규식으로 인식하는 문제때문에 교체.
		// tempName = tempName.replaceAll(parameterMatched, "").trim();
		tempName = tempName.substring(0, indexOf).trim();
		// [끝] 2015.3.5 파라미터에 [] 문자가 존재할 경우 정규식으로 인식하는 문제때문에 교체.

		String[] args = tempName.split("\\s+");
		String simpleMethodName = args[args.length - 1];

		// 리턴타입 추출.
		String returnType = "";
		for (int i = 0; i < args.length - 1; i++) {
			returnType += args[i] + " ";
		}
		if (returnType.isEmpty()) {
			returnType = "void";
		}

		methodDVO.setReturnType(returnType);
		methodDVO.setMethodName(simpleMethodName);
		methodDVO.setMethodParameterDVOList(convertParameterDVO);
		return methodDVO;
	}

	/**
	 * 파라미터 스트링으로부터 정규화된 데이터셋으로 변환한다.
	 *
	 * @param parameterString
	 * @return
	 */
	static List<MethodParameterDVO> convertParameterDVO(String parameterString) {

		List<MethodParameterDVO> rsltList = new ArrayList<MethodParameterDVO>();

		// 빈 문자열이면 빈값 반환
		if (ValueUtil.isEmpty(parameterString)) {
			return rsltList;
		}

		String tempPram = parameterString;

		for (String parameterm : tempPram.split(",")) {
			MethodParameterDVO methodParameterDVO = new MethodParameterDVO(parameterm.trim(), "var", "var", "var");
			rsltList.add(methodParameterDVO);
			System.out.println(parameterm.trim());
		}

		return rsltList;
	}
}
