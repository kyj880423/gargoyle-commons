/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.vo
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.vo;

/**
 * @author KYJ
 */
public class SourceAnalysisDVO
{

	private String methodName;

	private int methodNameStartLine;

	private int methodNameEndLine;

	/* 메소드 블록의 시작라인을 반환 */
	private int methodBlockStart;

	/* 메소드 블록의 종료라인을 반환 */
	private int methodBlockEnd;

	/* 메소드내에 기술된 소스 */
	private String statement;

	/* 파일명 */
	private String fileName;

	private String savedDemensionFileName;

	public SourceAnalysisDVO()
	{

	}

	public SourceAnalysisDVO(String methodName, int methodNameStartLine, int methodNameEndLine, int methodBlockStart, int methodBlockEnd,
			String fileName, String statement)
	{
		this();
		this.methodName = methodName;
		this.methodNameStartLine = methodNameStartLine;
		this.methodNameEndLine = methodNameEndLine;
		this.methodBlockStart = methodBlockStart;
		this.methodBlockEnd = methodBlockEnd;
		this.statement = statement;
		this.fileName = fileName;

	}

	public String getMethodName()
	{
		return methodName;
	}

	public void setMethodName(String methodName)
	{
		this.methodName = methodName;
	}

	public int getStartLine()
	{
		return methodNameStartLine;
	}

	public void setStartLine(int startLine)
	{
		this.methodNameStartLine = startLine;
	}

	public int getEndLine()
	{
		return methodNameEndLine;
	}

	public void setEndLine(int endLine)
	{
		this.methodNameEndLine = endLine;
	}

	@Override
	public String toString()
	{
		return methodName;
	}

	public int getBlockEnd()
	{
		return methodBlockEnd;
	}

	public void setBlockEnd(int blockEnd)
	{
		this.methodBlockEnd = blockEnd;
	}

	public int getMethodNameStartLine()
	{
		return methodNameStartLine;
	}

	public void setMethodNameStartLine(int methodNameStartLine)
	{
		this.methodNameStartLine = methodNameStartLine;
	}

	public int getMethodNameEndLine()
	{
		return methodNameEndLine;
	}

	public void setMethodNameEndLine(int methodNameEndLine)
	{
		this.methodNameEndLine = methodNameEndLine;
	}

	public int getMethodBlockStart()
	{
		return methodBlockStart;
	}

	public void setMethodBlockStart(int methodBlockStart)
	{
		this.methodBlockStart = methodBlockStart;
	}

	public int getMethodBlockEnd()
	{
		return methodBlockEnd;
	}

	public void setMethodBlockEnd(int methodBlockEnd)
	{
		this.methodBlockEnd = methodBlockEnd;
	}

	public String getStatement()
	{
		return statement;
	}

	public void setStatement(String statement)
	{
		this.statement = statement;
	}

	public String getSavedDemensionFileName()
	{
		return savedDemensionFileName;
	}

	public void setSavedDemensionFileName(String savedDemensionFileName)
	{
		this.savedDemensionFileName = savedDemensionFileName;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

}
