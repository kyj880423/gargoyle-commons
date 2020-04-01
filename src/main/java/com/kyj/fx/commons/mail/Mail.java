package com.kyj.fx.commons.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.velocity.Template;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Mail {

	private StringProperty mailFrom;

	private ObservableList<String> mailTo;

	private ObservableList<String> mailCc;

	private ObservableList<String> mailBcc;

	private StringProperty mailSubject;

	private StringProperty mailContent;

	// private StringProperty templateName;

	private StringProperty contentType;

	private List<AttachmentItem> attachmentItems = new ArrayList<>();

	private ObjectProperty<Template> emailTemplate = new SimpleObjectProperty<>();

	public Mail() {
		mailTo = FXCollections.observableArrayList();
		mailFrom = new SimpleStringProperty();
		mailCc = FXCollections.observableArrayList();
		mailBcc = FXCollections.observableArrayList();
		mailSubject = new SimpleStringProperty();
		mailContent = new SimpleStringProperty();
		// templateName = new SimpleStringProperty();
		contentType = new SimpleStringProperty("text/html");
	}

	@Override
	public String toString() {
		StringBuilder lBuilder = new StringBuilder();
		lBuilder.append("Mail From:- ").append(getMailFrom());
		lBuilder.append("Mail To:- ").append(getMailToString());
		// lBuilder.append("Mail Cc:- ").append(getMailCc());
		// lBuilder.append("Mail Bcc:- ").append(getMailBcc());
		lBuilder.append("Mail Subject:- ").append(getMailSubject());
		// lBuilder.append("Mail Send Date:- ").append(getMailSendDate());
		lBuilder.append("Mail Content:- ").append(getMailContent());
		return lBuilder.toString();
	}

	public String getMailToString() {
		Optional<String> reduce = this.mailTo.stream().reduce((str1, str2) -> str1.concat(",").concat(str2));
		if (reduce.isPresent())
			return reduce.get();
		return "";
	}

	public void setMailCc(List<String> cc) {
		this.mailCc.addAll(cc);
	}

	public ObservableList<String> getMailCc() {
		return this.mailCc;
	}

	public void setMailBcc(List<String> bcc) {
		this.mailBcc.addAll(bcc);
	}

	public ObservableList<String> getBcc() {
		return this.mailBcc;
	}

	public void setMailTo(List<String> mailTo) {
		this.mailTo.addAll(mailTo);
	}

	public void setMailTo(String... mailTo) {
		this.mailTo.addAll(Arrays.asList(mailTo));
	}

	public void addMailTo(String mailTo) {
		this.mailTo.add(mailTo);
	}

	public ObservableList<String> getMailTo() {
		return mailTo;
	}

	public final StringProperty mailFromProperty() {
		return this.mailFrom;
	}

	public final String getMailFrom() {
		return this.mailFromProperty().get();
	}

	public final void setMailFrom(final String mailFrom) {
		this.mailFromProperty().set(mailFrom);
	}

	public final StringProperty mailSubjectProperty() {
		return this.mailSubject;
	}

	public final String getMailSubject() {
		return this.mailSubjectProperty().get();
	}

	public final void setMailSubject(final String mailSubject) {
		this.mailSubjectProperty().set(mailSubject);
	}

	public final StringProperty mailContentProperty() {
		return this.mailContent;
	}

	public final String getMailContent() {
		return this.mailContentProperty().get();
	}

	public final void setMailContent(final String mailContent) {
		this.mailContentProperty().set(mailContent);
	}

	// public final StringProperty templateNameProperty() {
	// return this.templateName;
	// }
	//
	// public final String getTemplateName() {
	// return this.templateNameProperty().get();
	// }
	//
	// public final void setTemplateName(final String templateName) {
	// this.templateNameProperty().set(templateName);
	// }

	public final StringProperty contentTypeProperty() {
		return this.contentType;
	}

	public final String getContentType() {
		return this.contentTypeProperty().get();
	}

	public final void setContentType(final String contentType) {
		this.contentTypeProperty().set(contentType);
	}

	public void setMailTo(String email) {
		this.mailTo.add(email);
	}

	public final List<AttachmentItem> getAttachmentItems() {
		return attachmentItems;
	}

	public final void addAttachmentItems(AttachmentItem attachmentItem) {
		this.attachmentItems.add(attachmentItem);
	}

	public final void setAttachmentItems(List<AttachmentItem> attachmentItems) {
		this.attachmentItems = attachmentItems;
	}

	/**
	 * 수신자 리스트를 clear
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 17.
	 */
	public void clearAllRecipient() {
		this.mailTo.clear();
		this.mailCc.clear();
		this.mailBcc.clear();
	}

	public final ObjectProperty<Template> emailTemplateProperty() {
		return this.emailTemplate;
	}

	public final Template getEmailTemplate() {
		return this.emailTemplateProperty().get();
	}

	public final void setEmailTemplate(final Template emailTemplate) {
		this.emailTemplateProperty().set(emailTemplate);
	}

	// public void setMailTo(String ... email) {
	// setMailTo(email);
	// }

}