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
import com.kyj.fx.commons.word.msword.biz.JavaSourceAnalysis;
import com.kyj.fx.commons.word.msword.vo.MethodDVO;
import com.kyj.fx.commons.word.msword.vo.MethodParameterDVO;
import com.kyj.fx.commons.word.msword.vo.SourceAnalysisDVO;
import com.kyj.fx.commons.word.msword.vo.TableDVO;

/**
 * @author KYJ
 *
 */
public abstract class AbstractJavaProgramSpecFile implements IProgramSpecFile
{

	private static final String PARAMETER_GENERIC_TYPE = "<.{1,}>";

	// 파일
	private File f;

	private String projectName;
	// 패키지명
	private String strPackage;

	private List<SourceAnalysisDVO> listStatement;

	private InspectorSourceMeta meta;

	private JavaSourceAnalysis javaSourceAnalysis;

	private List<String> readLines;

	public AbstractJavaProgramSpecFile(File f) throws Exception
	{
		this.f = f;
		readLines = FileUtils.readLines(this.f, "UTF-8");
		meta = new InspectorSourceMeta(this.f.getName(), readLines);

		javaSourceAnalysis = new JavaSourceAnalysis(meta);
		listStatement = javaSourceAnalysis.findStatement();
		this.strPackage = javaSourceAnalysis.getPackageString();

	}

	public JavaSourceAnalysis getJavaSourceAnalysis()
	{
		return javaSourceAnalysis;
	}

	@Override
	public InspectorSourceMeta getInspectorSourceMeta()
	{
		return meta;
	}

	public final List<String> getReadLines()
	{
		return readLines;
	}

	/**
	 * 소스코드내 임포트문 반환
	 *
	 * @return
	 */
	public List<String> getImports()
	{
		return meta.getImportList();
	}

	/**
	 * 파일명 반환
	 *
	 * @return
	 */
	public String getFileName()
	{
		return f.getName();
	}

	/**
	 * 파일명 반환
	 *
	 * @return
	 */
	public String getFullFileName()
	{
		return f.getAbsolutePath();
	}

	/**
	 * 프로젝트명
	 *
	 * @return
	 */
	public String getProjectName()
	{
		return projectName;
	}

	/**
	 * 프로젝트명
	 *
	 * @param projectName
	 */
	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	/**
	 * 패키지반환
	 *
	 * @return
	 */
	public String getPackage()
	{
		return strPackage;
	}

	public void setPackage(String strPackage)
	{
		this.strPackage = strPackage;
	}

	public List<SourceAnalysisDVO> listStatement()
	{
		return listStatement;
	}

	/**
	 * 메소드의 주석을 찾은 다음 반환
	 *
	 * @param methodName
	 *            접근지정자, 반환값, 메소드명.. exception등이 포함된 메소드명
	 * @return
	 */
	public String getMethodDescription(String methodName)
	{

		SourceAnalysisDVO d = null;

		// 파라미터로 들어온 메소드를 찾는다.
		for (SourceAnalysisDVO dvo : this.listStatement())
		{
			if (methodName.equals(dvo.getMethodName()))
			{
				d = dvo;
				break;
			}
		}
		// 못찾으면 리턴
		if (d == null)
		{
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
		for (int index = startLine; index >= 0; index--)
		{
			String code = codeList.get(index).trim();
			boolean commentFlag = false;

			/* 메소드문에 해당하는 라인이면 SKIP한다. */
			if (code.equals(methodName))
				continue;

			switch (CodeCommentFactory.getCommentContain(code))
			{
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
				if (!commentFlag)
				{
					/* 어노테이션이면 skip */
					if (code.startsWith("@"))
					{

					}
					/* 주석에 속하는 부분이면 skip */
					else if (code.startsWith("*"))
					{

					}
					/* 만약 일반 텍스트를 만난다면 루프문종료 */
					else
					{
						stopFor = true;
					}
				}
				break;
			}

			// 주석부분의 시작점을 찾았다면 그 문장을 반환한다.
			if (commentFlag)
			{
				StringBuffer sb = new StringBuffer();
				for (int i = findCommentStartLine; i <= findCommentEndLine; i++)
				{
					sb.append(codeList.get(i)).append("\n");
				}
				return sb.toString();
			}

			/*
			 * 만약 주석부분을 못찾게된 경우 루프를 멈춘다. 주석부분을 못찾는 기준은 주석문장이 아닌 소스코드에 해당하는
			 * 위치인경우임.
			 */
			if (stopFor)
			{
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

	public InspectorSourceMeta toInspectorSourceMeta()
	{
		return null;
	}

	/**
	 * 경로 및 확장자가 없는 단순한 이름
	 *
	 * @return
	 */
	public String getFileSimpleName()
	{
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
	public String toString()
	{
		return f.getName();
	}

	/**
	 * 타입 + {static} + 메소드명 +( 파라미터 ) 형식을 을 갖는 메소드 문자열로부터 정규화된 MethodDVO를 반환받는다.
	 *
	 * @param fullMethodName
	 * @return
	 * @throws ProgramSpecSourceException
	 */
	public static MethodDVO toMethodDVO(final String fullMethodName) throws ProgramSpecSourceException
	{

		String tempName = fullMethodName;
		MethodDVO methodDVO = new MethodDVO();

		// 접근지정자 추출.
		String accessModifiers = "";
		if (tempName.startsWith("public"))
		{
			accessModifiers = "public";
			tempName = tempName.substring(tempName.indexOf(accessModifiers) + accessModifiers.length(), tempName.length()).trim();
		} else if (tempName.startsWith("private"))
		{
			accessModifiers = "private";
			tempName = tempName.substring(tempName.indexOf(accessModifiers) + accessModifiers.length(), tempName.length()).trim();
		} else if (tempName.startsWith("protected"))
		{
			accessModifiers = "protected";
			tempName = tempName.substring(tempName.indexOf(accessModifiers) + accessModifiers.length(), tempName.length()).trim();
		} else if (tempName.startsWith("default"))
		{
			accessModifiers = "default";
			tempName = tempName.substring(tempName.indexOf(accessModifiers) + accessModifiers.length(), tempName.length()).trim();
		} else
		{
			accessModifiers = "default";
		}

		// 접근지정자 저장.
		methodDVO.setVisivility(accessModifiers);

		// static 존재유무 확인. static의 존재유무에 따라 instance인지 static인지 구분.
		int static_indexOf = tempName.indexOf("static ");
		if (static_indexOf != -1)
		{
			tempName = tempName.substring(static_indexOf + "static ".length(), tempName.length()).trim();
			methodDVO.setLevel("Static \nMethod");
		} else
		{
			methodDVO.setLevel("Instance \nMethod");
		}

		// 예외처리 존재하면 지움.
		int throw_indexOf = tempName.indexOf(" throws ");
		if (throw_indexOf != -1)
		{
			tempName = tempName.substring(0, throw_indexOf).trim();
		}

		// 파라미터부분 반환
		String parameterMatched = ValueUtil.regexMatch("\\([\\w\\W]{0,}\\)", tempName).trim();
		if (ValueUtil.isEmpty(parameterMatched))
		{
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
		for (int i = 0; i < args.length - 1; i++)
		{
			returnType += args[i] + " ";
		}
		if (returnType.isEmpty())
		{
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
	static List<MethodParameterDVO> convertParameterDVO(String parameterString)
	{

		List<MethodParameterDVO> rsltList = new ArrayList<MethodParameterDVO>();

		// 빈 문자열이면 빈값 반환
		if (ValueUtil.isEmpty(parameterString))
		{
			return rsltList;
		}

		String tempPram = parameterString;

		// 제네릭 타입의 존재여부를 확인한다.
		// 없다면 간단히 문자열을 나누고 찾음.
		if (tempPram.indexOf(PARAMETER_GENERIC_TYPE) != -1)
		{
			for (String parameterm : tempPram.split(","))
			{
				String[] split = parameterm.split("\\s+");

				MethodParameterDVO methodParameterDVO = new MethodParameterDVO(split[1], split[0], split[0], split[0]);
				rsltList.add(methodParameterDVO);
				System.out.println(parameterm.trim());
			}
		}
		// 제네릭타입이 존재한다면 ..
		else
		{
			// 1. 일시적으로 제네릭 타입은 제거함.
			// tempPram = tempPram.replaceAll(PARAMETER_GENERIC_TYPE, "");

			StringBuffer tempBuffer = new StringBuffer();
			int startCnt = 0;
			int endCnt = 0;
			// 2. 제네릭 타입을 제거한후 세미콜론으로 문자열을 나누면 파라미터의 갯수를 추측할 수 있다.
			for (String parameterm : tempPram.split(","))
			{
				String trim = parameterm.trim();
				List<String> regexSMatchs = ValueUtil.regexMatchs("<", trim);
				List<String> regexEMatchs = ValueUtil.regexMatchs(">", trim);

				if (regexSMatchs.size() == 0 && regexEMatchs.size() == 0)
				{
					MethodParameterDVO methodParameterDVO = null;

					// /파라미터 타입이 ... 인경우 []로 변환함
					String[] dotSplit = trim.trim().split("\\.\\.\\.");
					if (dotSplit.length > 1)
					{
						String type = ValueUtil.valueOf(dotSplit, 0, dotSplit.length - 1).trim() + "[]";
						methodParameterDVO = new MethodParameterDVO(dotSplit[dotSplit.length - 1].trim(), type, type, type);
					} else
					{
						String[] split = trim.trim().split("\\s+");
						methodParameterDVO = new MethodParameterDVO(split[1].trim(), split[0], split[0], split[0]);
					}
					rsltList.add(methodParameterDVO);
					System.out.println(methodParameterDVO.getParameterType() + " : " + methodParameterDVO.getParameter());

				} else if (regexSMatchs.size() > 0 && regexEMatchs.size() > 0)
				{
					startCnt += regexSMatchs.size();
					endCnt += regexEMatchs.size();
					tempBuffer.append(trim.trim());
					if (startCnt == endCnt)
					{
						startCnt = 0;
						endCnt = 0;

						MethodParameterDVO methodParameterDVO = convertGenericParameterDVO(tempBuffer.toString());
						rsltList.add(methodParameterDVO);
						System.out.println(methodParameterDVO.getParameterType() + " : " + methodParameterDVO.getParameter());
						tempBuffer.setLength(0);
					}
				} else if (regexSMatchs.size() > 0 && regexEMatchs.size() == 0)
				{

					startCnt += regexSMatchs.size();
					tempBuffer.append(trim.trim()).append(",");
				} else if (regexSMatchs.size() == 0 && regexEMatchs.size() > 0)
				{
					endCnt += regexEMatchs.size();
					tempBuffer.append(trim.trim());
					if (startCnt == endCnt)
					{
						startCnt = 0;
						endCnt = 0;

						MethodParameterDVO methodParameterDVO = convertGenericParameterDVO(tempBuffer.toString());
						System.out.println(methodParameterDVO.getParameterType() + " : " + methodParameterDVO.getParameter());
						rsltList.add(methodParameterDVO);
						tempBuffer.setLength(0);
					}
				}

			}
		}

		return rsltList;
	}

	/**
	 * 제네릭 파라미터를 갖는 데이터형으로부터 정규화된 MethodParameterDVO로 반환받는다.
	 *
	 * @param data
	 * @return
	 */
	private static MethodParameterDVO convertGenericParameterDVO(String data)
	{

		String[] split = data.trim().split("\\s+");
		String type = "";
		if (split.length > 2)
		{
			type = ValueUtil.valueOf(split, 0, split.length - 2);
		} else
		{
			type = split[0];
		}
		MethodParameterDVO methodParameterDVO = new MethodParameterDVO(split[split.length - 1], type, type, type);
		return methodParameterDVO;
	}
}
