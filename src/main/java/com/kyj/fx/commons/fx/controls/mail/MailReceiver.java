/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.mail
 *	작성일   : 2017. 10. 16.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.mail;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ
 *
 */
public class MailReceiver {

	private StringProperty email;
	private StringProperty type;

	public MailReceiver() {
		email = new SimpleStringProperty();
		type = new SimpleStringProperty();
	}

	public MailReceiver(String email, String type) {
		this();
		this.email.set(email);
		this.type.set(type);
	}

	public final StringProperty emailProperty() {
		return this.email;
	}

	public final String getEmail() {
		return this.emailProperty().get();
	}

	public final void setEmail(final String email) {
		this.emailProperty().set(email);
	}

	public final StringProperty typeProperty() {
		return this.type;
	}

	public final String getType() {
		return this.typeProperty().get();
	}

	public final void setType(final String type) {
		this.typeProperty().set(type);
	}

}
