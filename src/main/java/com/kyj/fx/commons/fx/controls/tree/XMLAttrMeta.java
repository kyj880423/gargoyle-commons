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
public class XMLAttrMeta extends XMLMeta {

	private XMLMeta parent;

	/**
	 */
	public XMLAttrMeta() {

	}

	public XMLMeta getParent() {
		return parent;
	}

	public void setParent(XMLMeta parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return getName();
	}

}
