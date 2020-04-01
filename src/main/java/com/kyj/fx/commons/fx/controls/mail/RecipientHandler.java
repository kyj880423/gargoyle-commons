/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.mail
 *	작성일   : 2017. 10. 17.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.mail;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.mail.Mail;

import javafx.collections.ObservableList;

/**
 * @author KYJ
 *
 */
public class RecipientHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecipientHandler.class);

	private MailViewComposite mailComposite;

	public RecipientHandler(MailViewComposite mailComposite) {
		this.mailComposite = mailComposite;
	}

	public void build() {
		Mail mail = this.mailComposite.getMail();
		// 수신자 리스트를 비움
		mail.clearAllRecipient();

		List<MailReceiver> recipients = this.mailComposite.getRecipients();

		for (MailReceiver r : recipients) {

			LOGGER.debug("[{}] - {} ", r.getType(), r.getEmail());
			switch (r.getType()) {
			case "CC":
				ObservableList<String> mailCc = mail.getMailCc();

				mailCc.add(r.getEmail());
				break;
			case "BCC":
				ObservableList<String> bcc = mail.getBcc();
				bcc.add(r.getEmail());
				break;
			default:
				ObservableList<String> mailTo = mail.getMailTo();
				mailTo.add(r.getEmail());
				break;
			}

		}

	}
}
