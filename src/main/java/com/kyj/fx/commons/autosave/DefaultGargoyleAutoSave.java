/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.autosave
 *	작성일   : 2019. 10. 30.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.autosave;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class DefaultGargoyleAutoSave implements IGargoyleAutoSave {

	@Override
	public boolean useAutoSaveScheduler() {
		return false;
	}

	@Override
	public File scheduleAutoSaveFile() {
		return null;
	}

	@Override
	public InputStream scheduleAutoSaveContent() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.commons.autosave.IGargoyleAutoSave#writeSave(java.io.File,
	 * java.io.InputStream)
	 */
	@Override
	public void writeSave(File target, InputStream in) throws Exception {
		IGargoyleAutoSave.super.writeSave(target, in);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 30.
	 */
	public void start() {
		startAutoSaveScheduler();
	}

	@Override
	public boolean pause() {
		return IGargoyleAutoSave.super.pause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.commons.autosave.IGargoyleAutoSave#close()
	 */
	@Override
	public void close() throws IOException {
		IGargoyleAutoSave.super.close();
	}

}
