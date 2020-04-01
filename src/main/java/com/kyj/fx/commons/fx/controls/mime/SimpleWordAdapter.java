/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.word
 *	작성일   : 2016. 12. 26.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.mime;

import java.io.File;
import java.io.IOException;

import com.kyj.fx.commons.utils.DateUtil;
import com.kyj.fx.commons.word.msword.core.MSWord;

/**
 *
 * 컨텐츠를 파일 확장자 docx로 바꾼후 처리.
 *
 * 
 * @author KYJ
 *
 */
public class SimpleWordAdapter extends AbstractMimeAdapter {

	private String content;

	/**
	 * @param mimeFile
	 */
	private SimpleWordAdapter() {
		super(null);
	}

	public SimpleWordAdapter(String content) {
		super(null);
		this.content = content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.voeditor.visual.framework.word.AbstractMimeAdapter#getContent(
	 * )
	 */
	@Override
	public String getContent() {
		return this.content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.voeditor.visual.framework.word.AbstractMimeAdapter#tmpFileName
	 * ()
	 */
	@Override
	protected String tmpFileName() {
		String currentDateString = DateUtil.getCurrentDateString(DateUtil.SYSTEM_DATEFORMAT_YYYYMMDDHHMMSSS);
		String simpleTemplFileName = String.format("_%s.docx", currentDateString);
		return simpleTemplFileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.voeditor.visual.framework.word.AbstractMimeAdapter#toTempFile(
	 * )
	 */
	@Override
	public File toTempFile() {
		String content = getContent();

		MSWord msWord = new MSWord();
		msWord.addText(content);

		File mimeFile = createNewTempFile();

		try {
			msWord.close(mimeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mimeFile;
	}

}
