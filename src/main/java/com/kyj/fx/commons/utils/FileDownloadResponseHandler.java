/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2019. 9. 7.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class FileDownloadResponseHandler extends ResponseHandler<File> {

	private File out;

	public FileDownloadResponseHandler(File out) {
		this.out = out;
	}

	@Override
	public File apply(InputStream is, Integer code) {
		try {
			FileUtil.writeFile(this.out, is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return this.out;
	}

}
