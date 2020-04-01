/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.file.conversion
 *	작성일   : 2018. 4. 16.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author KYJ
 *
 */
public class BehaviorBase64ContentConversion extends AbstractStringFileContentConversion {

	private String base64 = "";
	private Charset charset = StandardCharsets.UTF_16LE;

	/**
	 * @param size
	 *            buffer size
	 */
	public BehaviorBase64ContentConversion(String base64) {
		this.base64 = base64;
	}

	public BehaviorBase64ContentConversion() {

	}

	/**
	 * @return the charset
	 */
	public Charset getCharset() {
		return charset;
	}

	/**
	 * @param charset
	 *            the charset to set
	 */
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	/*
	 * @Deprecated this class not used. <br/>
	 * 
	 */
	@Deprecated
	@Override
	public void out(OutputStream out) {
		// Nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.voeditor.visual.framework.file.conversion.
	 * AbstractFileContentConversion#getOut()
	 * 
	 * @Deprecated Not used.
	 */
	@Override
	@Deprecated
	public OutputStream getOut() {
		return null;
	}

	/**
	 * 데이터 변환 처리 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 16.
	 * @return String 변환된 데이터
	 * @throws Exception
	 */
	@Override
	public String conversion() throws Exception {
		if (ValueUtil.isEmpty(this.base64))
			return "";

		byte[] decode = Base64.getDecoder().decode(this.base64);

		return new String(decode, charset);
	}
}
