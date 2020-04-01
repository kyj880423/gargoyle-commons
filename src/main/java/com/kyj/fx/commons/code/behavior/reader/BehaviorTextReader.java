/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.reader
 *	작성일   : 2019. 10. 14.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.code.behavior.reader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.kyj.fx.commons.utils.AbstractStringFileContentConversion;

/**
 * 
 * 사람이 읽을 수 있는 텍스트의 경우 아래 클래스를 사용 <br/>
 * 
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class BehaviorTextReader extends BehaviorReader {

	private String cont;

	public BehaviorTextReader(String name, String cont) throws FileNotFoundException, UnsupportedEncodingException {
		super((InputStream) null);
		this.cont = cont;
		setName(name);
	}

	@Override
	protected AbstractStringFileContentConversion createConversion() {
		return new AbstractStringFileContentConversion() {

			@Override
			public String conversion() throws Exception {
				return cont;
			}
		};
	}

	@Override
	public String readBehavior() throws Exception {
		return super.readBehavior();
	}

}
