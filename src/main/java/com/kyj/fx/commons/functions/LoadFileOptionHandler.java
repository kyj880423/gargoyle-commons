/**
 * package : com.kyj.fx.voeditor.visual.component.sql.functions
 *	fileName : LoadFileOption.java
 *	date      : 2015. 12. 24.
 *	user      : KYJ
 */
package com.kyj.fx.commons.functions;

import java.io.File;
import java.util.List;
import java.util.function.Function;

/**
 * 파일을 로드할시에 처리할 옵션.
 *
 * @author KYJ
 *
 */
public class LoadFileOptionHandler {

	/**
	 * 인코딩
	 */
	private String encoding = "UTF-8";

	/**
	 * 예외발생여부
	 */
	private boolean isOccurException;

	/**
	 * 예외가 발생한경우 입력됨.
	 */
	private Exception exception;

	/**
	 * 확장자 필터
	 */
	private List<String> fileNameLikeFilter;

	/**
	 * File이 존재하지않는경우 치환할 내용을 기술. 기본값은 null 리턴.
	 * @최초생성일 2016. 7. 26.
	 */
	private Function<File, String> fileNotFoundThan = file -> null;

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public boolean isOccurException() {
		return isOccurException;
	}

	public void setOccurException(boolean isOccurException) {
		this.isOccurException = isOccurException;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		if (exception != null)
			this.isOccurException = true;

		this.exception = exception;
	}

	public List<String> getFileNameLikeFilter() {
		return fileNameLikeFilter;
	}

	public void setFileNameLikeFilter(List<String> fileNameLikeFilter) {
		this.fileNameLikeFilter = fileNameLikeFilter;
	}

	/**
	 * @return the fileNotFoundThan
	 */
	public Function<File, String> getFileNotFoundThan() {
		return fileNotFoundThan;
	}

	/**
	 * @param fileNotFoundThan the fileNotFoundThan to set
	 */
	public void setFileNotFoundThan(Function<File, String> fileNotFoundThan) {
		this.fileNotFoundThan = fileNotFoundThan;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 23.
	 * @return
	 */
	public static LoadFileOptionHandler getDefaultHandler() {
		return new LoadFileOptionHandler();
	}

}
