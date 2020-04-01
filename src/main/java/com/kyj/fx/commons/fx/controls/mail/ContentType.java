/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.mail
 *	작성일   : 2017. 10. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.mail;

/**
 * @author KYJ
 *
 */
public enum ContentType {
	text("text/plain"), html("text/html");

	String contentType;

	ContentType(String contentType) {
		this.contentType = contentType;
	}

	public String toString() {
		return this.contentType;
	}
}
