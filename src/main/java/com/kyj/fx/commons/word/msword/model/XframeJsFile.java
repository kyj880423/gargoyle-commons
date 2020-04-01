/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.filemodel
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.model;

import java.io.File;

import com.kyj.fx.commons.word.msword.biz.InspectorSourceMeta;

public class XframeJsFile extends XframeFile
{

	public XframeJsFile(File f) throws Exception
	{
		super(f);
	}

	@Override
	public SOURCE_FILE_TYPE getSourceFileType()
	{
		return SOURCE_FILE_TYPE.XFRAME_JS;
	}

	@Override
	public InspectorSourceMeta getInspectorSourceMeta()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
