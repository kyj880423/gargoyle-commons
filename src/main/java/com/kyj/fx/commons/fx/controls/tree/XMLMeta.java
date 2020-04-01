/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.tree
 *	작성일   : 2017. 10. 23.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.tree;

/**
 * @author KYJ
 *
 */
public class XMLMeta {

	private String name;
	private String text;

	/**
	 */
	public XMLMeta() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return name;
	}

}
