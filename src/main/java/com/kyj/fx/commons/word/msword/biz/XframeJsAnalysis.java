/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.biz
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kyj.fx.commons.exceptions.ProgramSpecSourceErrException;
import com.kyj.fx.commons.word.msword.vo.BlockDVO;
import com.kyj.fx.commons.word.msword.vo.SourceAnalysisDVO;

public class XframeJsAnalysis implements SourceCodeAnalysis
{

	/* method list */
	private List<SourceAnalysisDVO> methodList = new ArrayList<SourceAnalysisDVO>();

	public static final String NOMAL_METHOD_STATEMENT = "(:?(function))(:?([\\s]{1,}))(?:([a-zA-Z0-9_]{0,}))(?:([a-zA-Z0-9_\\(\\)\\s,]){1,})[\\s]{0,}\\{";

	private InspectorSourceMeta inspectorSourceMeta;

	private List<BlockDVO> listBlock;

	public XframeJsAnalysis(InspectorSourceMeta inspectorSourceMeta)
	{
		this.inspectorSourceMeta = inspectorSourceMeta;
		this.listBlock = this.inspectorSourceMeta.listBlock();
		Pattern p = Pattern.compile(NOMAL_METHOD_STATEMENT);
		compile(p);
	}

	/**
	 * 2014. 6. 21. Administrator
	 *
	 * @param statement
	 * @return
	 * @처리내용 : 자바소스에 기술된 메소드들을 찾는다.
	 */
	public List<SourceAnalysisDVO> findStatement()
	{
		return methodList;
	}

	/**
	 * 2014. 6. 29. KYJ
	 *
	 * @param line
	 * @return
	 * @처리내용 : 라인에 해당하는 블록을 가져오고 사용했던 블록은 삭제
	 */
	private BlockDVO getBlockAndDeleteList(int line)
	{
		for (BlockDVO b : listBlock)
		{
			if (b.getStartindex() == line)
			{
				listBlock.remove(b);
				return b;
			}
		}
		return null;
	}

	public String getString(List<String> sourceCodeList)
	{
		StringBuilder sb = new StringBuilder();
		for (String str : sourceCodeList)
		{
			sb.append(str).append("\n");
		}
		return sb.toString();
	}

	/**
	 * 2014. 6. 21. Administrator
	 *
	 * @param p
	 * @param data
	 * @return
	 * @처리내용 : 패턴과 일치하는 내용을 찾아 반환
	 */

	public void compile(Pattern p)
	{

		String statement = this.inspectorSourceMeta.getSourceCode();
		Matcher m = p.matcher(statement);

		boolean result = m.find();
		int lineNumberSavePoint = 0;
		int lineNumberCnt = 0;

		while (result)
		{
			String group = m.group().trim();

			int start = m.start();
			int end = m.end();

			lineNumberCnt += getLineNumberCnt(statement.substring(lineNumberSavePoint, end));
			lineNumberSavePoint = end + 1;

			BlockDVO blockAndDeleteList = getBlockAndDeleteList(lineNumberCnt);
			/* if문이 true인 경우는 함수가 포함된 라인안에 브레이스가 있는경우임 */
			if (blockAndDeleteList == null)
			{
				lineNumberCnt = lineNumberCnt - 1;
				blockAndDeleteList = getBlockAndDeleteList(lineNumberCnt);
				lineNumberSavePoint = lineNumberSavePoint - 1;
			}
			if (blockAndDeleteList == null)
			{
				ProgramSpecSourceErrException e = new ProgramSpecSourceErrException(" 코드 블록에 문제가 있거나 , 프로그램 버그 발생. ");
				e.addDetailMsgList("2차 체크 코드 블록 BlockDVO null 주석문제일 가능성이 있음.");
				e.addDetailMsgList("group : ");
				e.addDetailMsgList(group);
				e.addDetailMsgList("start : ");
				e.addDetailMsgList(String.valueOf(start));
				e.addDetailMsgList("end : ");
				e.addDetailMsgList(String.valueOf(end));
				e.addDetailMsgList("lineNumberCnt : ");
				e.addDetailMsgList(String.valueOf(lineNumberCnt));
				e.addDetailMsgList("linkedList : ");
				e.addDetailMsgList(listBlock.toString());

				// BaseUtil.alert( "코드 블록에 문제가 있거나 , 프로그램 버그 발생." );
				// return;
				result = m.find();
				continue;
			}
			int startLine = blockAndDeleteList.getStartindex();
			int endLine = blockAndDeleteList.getEndindex();

			List<String> subList = this.inspectorSourceMeta.getSourceCodeList().subList(startLine - 1, endLine);
			String sourceCode = getString(subList);

			methodList.add(new SourceAnalysisDVO(group.replaceAll("\\{", "").trim(), start, end, startLine, endLine,
					this.inspectorSourceMeta.getFileName(), sourceCode));

			result = m.find();
		}

	}

	/**
	 * 메타정보 반환
	 *
	 * @return
	 */
	public InspectorSourceMeta getInspectorSourceMeta()
	{
		return inspectorSourceMeta;
	}

	public static int getLineNumberCnt(String str)
	{
		String[] split = str.split("\n");
		return split.length;
	}

}
