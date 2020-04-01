/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.mail
 *	작성일   : 2017. 10. 17.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.mail;

import java.io.File;
import java.util.List;

import com.kyj.fx.commons.mail.AttachmentItem;
import com.kyj.fx.commons.mail.Mail;

/**
 * @author KYJ
 *
 */
public class AttachmentHandler {

	private MailViewComposite mailComposite;

	public AttachmentHandler(MailViewComposite mailComposite) {
		this.mailComposite = mailComposite;
	}

	public void build() {
		Mail mail = this.mailComposite.getMail();
		List<File> attachments = this.mailComposite.getAttachments();

		for (File f : attachments) {
			mail.addAttachmentItems(AttachmentItem.createFileAttachmentItem(f));
		}

	}
}
