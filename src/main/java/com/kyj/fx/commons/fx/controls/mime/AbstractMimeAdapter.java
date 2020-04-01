/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.word
 *	작성일   : 2016. 12. 26.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.mime;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.DateUtil;
import com.kyj.fx.commons.utils.FileUtil;
import com.kyj.fx.commons.utils.ValueUtil;

/**
 *
 *  아이템 어댑터.
 *  Mime타입의 데이터르 컨버팅하기 위한 처리가 기술된 추상클래스.
 *
 * @author KYJ
 *
 */
public abstract class AbstractMimeAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMimeAdapter.class);

	protected File mimeFile;

	public AbstractMimeAdapter(File mimeFile) {
		this.mimeFile = mimeFile;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 26.
	 * @return
	 */
	public abstract String getContent();

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 14. 
	 * @return
	 */
	public Charset getEncoding() {
		return Charset.forName("UTF-8");
	}
	
	protected File parentFile() {
		return FileUtil.getTempFileSystem();
	}

	protected String tmpFileName() {
		String currentDateString = DateUtil.getCurrentDateString(DateUtil.SYSTEM_DATEFORMAT_YYYYMMDDHHMMSSS);
		String simpleTemplFileName = String.format("_%s.html", currentDateString);
		return simpleTemplFileName;
	}

	protected File createNewTempFile() {
		return new File(parentFile(), tmpFileName());
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 26.
	 * @return
	 */
	public File toTempFile() {
		File mimeFile = createNewTempFile();
		try {
			FileUtil.writeFile(mimeFile, getContent(), getEncoding());

			FileUtil.openFile(mimeFile);

		} catch (IOException e) {
			LOGGER.error(ValueUtil.toString(e));
		}
		return mimeFile;
	}
}
