/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.word
 *	작성일   : 2016. 12. 26.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.mime;

import java.io.UnsupportedEncodingException;

import com.kyj.fx.commons.utils.MimeHelper;

/**
 *
 *  HTML 컨텐츠를 Mime 타입의 컨텐츠로 바꾸기 위한 어댑터
 *
 *  HTML파일을 Mime타입으로 바꾸는 로직 기술.
 * @author KYJ
 *
 */
public class HtmlTextToMimeAdapter extends AbstractMimeAdapter {

	private String content;

	/**
	 * @param mimeFile
	 */
	private HtmlTextToMimeAdapter() {
		super(null);
	}

	public HtmlTextToMimeAdapter(String content) {
		super(null);
		this.content = content;
	}

	/* (non-Javadoc)
	 * @see com.kyj.fx.voeditor.visual.framework.word.AbstractMimeAdapter#getContent()
	 */
	@Override
	public String getContent() {
		String content = "";
		try {

			StringBuffer sb = new StringBuffer();
			sb.append("<html> \n");
			sb.append(htmlCharset());
			sb.append("<body> \n");
			sb.append(this.content).append(" \n");
			sb.append("</body> \n");
			sb.append("</html> \n");


			content = MimeHelper.toMime(sb.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return content;
	}

	protected String htmlCharset() {
		return "<meta charset = '"+charset()+"'/> ";
	}

	protected String charset(){
		return "UTF-8";
	}

	protected String contentType() {
		return "<meta content-type :  ";
	}

}
