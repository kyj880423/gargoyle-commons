/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.file.conversion
 *	작성일   : 2018. 3. 6.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 데이터 변환 처리 추상 클래스 <br/>
 * 
 * @author KYJ
 *
 */
public abstract class AbstractStringFileContentConversion extends AbstractFileContentConversion<String> {

	private InputStream in;
	private OutputStream out;
	private Charset encoding = StandardCharsets.UTF_8;

	/**
	 * @param size
	 *            buffer size
	 */
	public AbstractStringFileContentConversion(int size) {
		out = new ByteArrayOutputStream(size);
	}

	public AbstractStringFileContentConversion() {
		out = new ByteArrayOutputStream();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 6. 
	 * @param encoding
	 */
	public void setEncoding(Charset encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the encoding
	 */
	public Charset getEncoding() {
		return encoding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.voeditor.visual.framework.file.conversion.
	 * FileContentConversionable#in(java.io.InputStream)
	 */
	@Override
	public void in(InputStream in) {
		this.in = in;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.voeditor.visual.framework.file.conversion.
	 * FileContentConversionable#out(java.io.OutputStream)
	 */
	@Override
	public void out(OutputStream out) {
		// nothing.
	}

	/**
	 * @return the in
	 */
	public InputStream getIn() {
		return in;
	}

	/**
	 * @return the out
	 */
	public OutputStream getOut() {
		return out;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.voeditor.visual.framework.file.conversion.DataConversionable#
	 * conversion()
	 */
	public abstract String conversion() throws Exception;

}
