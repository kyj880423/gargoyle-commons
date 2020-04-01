/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.word
 *	작성일   : 2016. 12. 27.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.mime;

/**
 * @author KYJ
 *
 */
public class SimpleContentAdapter extends AbstractMimeAdapter {

	private String content;

	/**
	 * @param mimeFile
	 */
	private SimpleContentAdapter() {
		super(null);
	}

	public SimpleContentAdapter(String content) {
		super(null);
		this.content = content;
	}

	/* (non-Javadoc)
	 * @see com.kyj.fx.voeditor.visual.framework.word.AbstractMimeAdapter#getContent()
	 */
	@Override
	public String getContent() {
		return this.content;
	}

}
