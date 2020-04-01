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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.word.msword.vo.BlockDVO;
import com.kyj.fx.commons.word.msword.vo.SourceAnalysisDVO;

public class JavaSourceAnalysis implements SourceCodeAnalysis
{

	private static Logger LOGGER = LoggerFactory.getLogger(JavaSourceAnalysis.class);

	// 자바 패키지를 찾는 정규식.
	private static final String PACKAGE_REGEX = "\\s+package\\s+[_.0-9a-zA-Z]{1,};";

	/* method list */
	private List<SourceAnalysisDVO> methodList = new ArrayList<SourceAnalysisDVO>();

	/* 접근지정자+static+final+반환값+함수명+(파라미터) */
	// public static final String NOMAL_METHOD_STATEMENT =
	// "((?:(public|private|default|protected|))(?:(\\sstatic|))(?:(\\sfinal|))(?:(\\svoid|[a-zA-Z0-9_\\[\\]<>]{0,}))(?:(\\s\\w{0,})))\\([a-zA-Z0-9_.,\\[\\]<>\\s]{0,}\\)";

	/* 접근지정자+static+final+반환값+함수명+(파라미터) while, new, catch, */
	// public static final String NOMAL_METHOD_STATEMENT2 =
	// "(?:(public|private|default|protected)(?:(\\sstatic|))(?:(\\sfinal|))(?:(\\svoid|[a-zA-Z0-9_\\[\\]<>]{1,}))(?:(\\s\\w{0,})))\\([a-zA-Z0-9_.,\\[\\]<>!=\\s]{0,}\\)";

	/* 접근지정자+static+final+반환값+함수명+(파라미터) + return 바로, exception영역까지 추가 */
	@Deprecated
	public static final String NOMAL_METHOD_STATEMENT3 = "((?:(public|private|default|protected|))(?:(\\sstatic|))(?:(\\sfinal|))(?:(\\svoid|[a-zA-Z0-9_\\[\\]<>]{1,}))(?:(\\s\\w{1,})))\\([a-zA-Z0-9_.,\\[\\]<>\\s]{0,}\\)";

	/* 추상적 제네릭 타입 추가 */
	@Deprecated
	public static final String NOMAL_METHOD_STATEMENT4 = "((?:(!Override|))(?:(public|private|default|protected|))(?:([a-zA-Z0-9<>\\s,]{0,}|))(?:(\\sstatic\\s\\sfinal\\s|\\sstatic\\s|\\sfinal\\s|\\s))(?:(void\\s|[a-zA-Z0-9_\\[\\]<>]{1,}\\s))(?:(\\w{1,})))(?:(\\()|\\s\\()[a-zA-Z0-9_.,\\[\\]<>\\s]{0,}(?:(\\))|\\s\\))(?:(\\sthrows\\s\\w{1,}|throws\\s\\w{1,}|))";

	/* Override포함되는 경우 제거 */
	@Deprecated
	public static final String NOMAL_METHOD_STATEMENT5 = "((?:(public|private|default|protected|(!Override|\\s))(?:[a-zA-Z0-9,\\[\\]_\\s<>]{1,}|))(?:(\\sstatic\\s\\sfinal\\s|\\sstatic\\s|\\sfinal\\s|\\s))(?:(void\\s|[a-zA-Z0-9_\\[\\]<>]{1,}\\s))(?:(\\w{1,})))(?:(\\()|\\s\\()[a-zA-Z0-9_.,\\[\\]<>\\s]{0,}(?:(\\))|\\s\\))(?:(\\sthrows\\s\\w{1,}|throws\\s\\w{1,}|))[\\s]{0,}\\{";

	/* throw Exception부분과 메소드 파라미터 끝나는부분에 특수공백이 많이 포함되는 케이스가 존재하므로 보안 */
	public static final String NOMAL_METHOD_STATEMENT6 = "((?:(public|private|default|protected|(!Override|\\s))(?:[a-zA-Z0-9,\\[\\]_\\s<>]{1,}|))(?:(\\sstatic\\s\\sfinal\\s|\\sstatic\\s|\\sfinal\\s|\\s))(?:(void\\s|[a-zA-Z0-9_\\[\\]<>]{1,}\\s))(?:(\\w{1,})))(?:(\\()|\\s\\()[a-zA-Z0-9_.,\\[\\]<>\\s]{0,}(?:(\\))|\\s\\))(\\s){0,}(?:(\\sthrows\\s\\w{1,}|throws\\s\\w{1,}|))[\\s]{0,}\\{";

	public static final String PREVENT_CATCH = "catch";

	public static final String PREVENT_NEW = "new";

	public static final String PREVENT_IF = "if";

	public static final String PREVENT_WHILE = "while";

	public static final String PREVENT_RETURN = "return";

	private InspectorSourceMeta inspectorSourceMeta;

	// package
	private String packageString = "";
	// 디폴트 패키지인지 유무
	private boolean isDefaultPackage = true;

	private List<BlockDVO> listBlock;

	public JavaSourceAnalysis(InspectorSourceMeta inspectorSourceMeta)
	{

		this.inspectorSourceMeta = inspectorSourceMeta;
		this.listBlock = this.inspectorSourceMeta.listBlock();

		Pattern p = Pattern.compile(NOMAL_METHOD_STATEMENT6);
		compile(p);
	}

	/**
	 * 소스메타정보 반환
	 *
	 * @return
	 */
	public InspectorSourceMeta getInspectorSourceMeta()
	{
		return inspectorSourceMeta;
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
	 * @return the packageString
	 */
	public String getPackageString()
	{
		return packageString;
	}

	/**
	 * @return the isDefaultPackage
	 */
	public boolean isDefaultPackage()
	{
		return isDefaultPackage;
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

		// 패키지 찾기.

		List<String> packageStrings = ValueUtil.regexMatchs(PACKAGE_REGEX, statement);
		if (isDefaultPackage && ValueUtil.isNotEmpty(packageStrings))
		{
			// TODO 수정예상됨. 인덱스를 0으로 강제로 셋팅함. 허나 패키지NAME은 한개이긴함.
			this.packageString = packageStrings.get(0);
			isDefaultPackage = false;
		}

		while (result)
		{
			String group = m.group();

			/*
			 * 특정 로직부분에 걸리는 부분때문에 추가. ex) return query(sql.toString() , data ,
			 * new RowMapper() { public Object mapRow(ResultSet resultSet, int
			 * row)
			 */
			if (ValueUtil.leftTrim(group).startsWith(","))
			{
				result = m.find();
				continue;
			}
			// int catchIndexOf = group.indexOf(PREVENT_CATCH);
			boolean catchIndexOf = group.matches(PREVENT_CATCH);
			// int newIndexOf = group.indexOf(PREVENT_NEW);
			boolean newIndexOf = group.matches(PREVENT_NEW);
			// int ifIndexOf = group.indexOf(PREVENT_IF);
			boolean ifIndexOf = group.matches(PREVENT_IF);
			// int whileIndexOf = group.indexOf(PREVENT_WHILE);
			boolean whileIndexOf = group.matches(PREVENT_WHILE);
			// int returnIndexOf = group.indexOf(PREVENT_RETURN);
			boolean returnIndexOf = group.matches(PREVENT_RETURN);

			/*
			 * if (catchIndexOf == -1 && newIndexOf == -1 && ifIndexOf == -1 &&
			 * whileIndexOf == -1 && returnIndexOf == -1)
			 */
			if (!catchIndexOf && !newIndexOf && !ifIndexOf && !whileIndexOf && !returnIndexOf)
			{

				int start = m.start();
				int end = m.end();

				String methodName = statement.substring(lineNumberSavePoint, end);

				lineNumberCnt += getLineNumberCnt(methodName);
				lineNumberSavePoint = end + 1;

				BlockDVO blockAndDeleteList = getBlockAndDeleteList(lineNumberCnt);
				/* if문이 true인 경우는 함수가 포함된 라인안에 브레이스가 있는경우임 */
				if (blockAndDeleteList == null)
				{
					blockAndDeleteList = getBlock(lineNumberCnt, 0);
					lineNumberSavePoint = lineNumberSavePoint - 1;
				}
				LOGGER.debug(String.format(" lineNumberCnt : %d linNuberSavePoint : %d ", lineNumberCnt, lineNumberSavePoint));

				int startLine = blockAndDeleteList.getStartindex();
				int endLine = blockAndDeleteList.getEndindex();

				LOGGER.debug(String.format("startLine : %d  endLine %d \n", startLine, endLine));
				List<String> subList = this.inspectorSourceMeta.getSourceCodeList().subList(startLine - 1, endLine);
				String sourceCode = getString(subList);

				methodList.add(new SourceAnalysisDVO(group.replaceAll("\\{", "").trim(), start, end, startLine, endLine,
						this.inspectorSourceMeta.getFileName(), sourceCode));

			}

			result = m.find();
		}

	}

	public static int getLineNumberCnt(String str)
	{
		String[] split = str.split("\n");
		return split.length;
	}

	BlockDVO getBlock(int lineNumberCnt, int loopCnt)
	{
		/* 무한루프 방지용 */
		if (loopCnt >= 100)
		{
			return null;
		}
		lineNumberCnt = lineNumberCnt - 1;
		BlockDVO blockAndDeleteList = getBlockAndDeleteList(lineNumberCnt);
		if (blockAndDeleteList == null)
		{
			loopCnt++;
			blockAndDeleteList = getBlock(lineNumberCnt, loopCnt);
		}
		return blockAndDeleteList;
	}
}
