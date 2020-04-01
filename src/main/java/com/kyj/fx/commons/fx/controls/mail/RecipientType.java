/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.mail
 *	작성일   : 2017. 10. 17.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.mail;

/**
 * @author KYJ
 *
 */
public enum RecipientType {
	TO("TO"), CC("CC"), BCC("BCC");

	private String name;

	RecipientType(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}
