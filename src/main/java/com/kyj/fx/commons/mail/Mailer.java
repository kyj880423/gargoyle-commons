package com.kyj.fx.commons.mail;

import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.kyj.fx.commons.utils.ValueUtil;

/**
 * @author KYJ
 *
 */
public class Mailer {

	private static final Logger LOGGER = LoggerFactory.getLogger(Mailer.class);

	private JavaMailSenderImpl mailSender;

	/**
	 * @최초생성일 2017. 10. 14.
	 */
	private String mailTitle;
	/**
	 * @최초생성일 2017. 10. 14.
	 */
//	private String mailTemplate;
	/**
	 * @최초생성일 2017. 10. 14.
	 */
	private String encoding;
	/**
	 * @최초생성일 2017. 10. 14.
	 */
	private String mailUseYn;

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	// public void setMailFrom(String mailFrom) {
	// this.mailFrom = mailFrom;
	// }

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

//	public void setMailTemplate(String mailTemplate) {
//		this.mailTemplate = mailTemplate;
//	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}

	/**
	 * @param mailUseYn
	 *            the mailUseYn to set
	 */

	public void setMailUseYn(String mailUseYn) {
		this.mailUseYn = mailUseYn;
	}

	@Deprecated
	public void sendMail(SenderMailInfo mailSenderInfo, List<Mail> mails, VelocityContext velocityContext) throws Exception {

		for (Mail mail : mails) {
			sendMail(mailSenderInfo, mail, velocityContext);
		}
	}

	public void sendMail(SenderMailInfo mailSenderInfo, Mail mail, VelocityContext velocityContext) throws Exception {

		if (this.mailUseYn != null) {

			if ("N".equals(this.mailUseYn)) {
				throw new Exception("Mail Serivce's configuration is not set useYn Y ");
			}
		}

		JavaMailSenderImpl senderImpl = this.mailSender;
		if (senderImpl == null) {
			senderImpl = new JavaMailSenderImpl();
		}

		String _encoding = MailConst.MAILER_DEFAULT_ENCODING;

		// new MimeMailMessage(new MimeMessage());
		// SimpleMailMessage message = new SimpleMailMessage();
		MimeMessage message = senderImpl.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		String[] toArray = mail.getMailTo().stream().peek(a ->{
			LOGGER.debug("Recipient : {} " , a);
		}).toArray(String[]::new);
		helper.setTo(toArray);
		
		helper.setCc(mail.getMailCc().stream().toArray(String[]::new));

		helper.setBcc(mail.getBcc().stream().toArray(String[]::new));

		helper.setSentDate(new Date());

		if (mail.getMailSubject() != null) {
			helper.setSubject(mail.getMailSubject());
		} else {
			helper.setSubject(this.mailTitle);
		}

		/*
		 * Encoding fist. find Mailer property. <br/> second find by input
		 * parameter - SenderMailInfo. <br/>
		 * 
		 */
		if (ValueUtil.isNotEmpty(encoding))
			_encoding = encoding;

		if (ValueUtil.isNotEmpty(mailSenderInfo.getDefaultEncoding())) {
			_encoding = mailSenderInfo.getDefaultEncoding();
		}

		LOGGER.debug("encoding : {}" , _encoding);
		senderImpl.setDefaultEncoding(_encoding);

		if (mailSenderInfo != null) {

			Properties javaMailProperties = mailSenderInfo.getJavaMailProperties();

			if (javaMailProperties != null)
				senderImpl.setJavaMailProperties(javaMailProperties);

			String sendUserId = mailSenderInfo.getSendUserId();
			String sendUserPassword = mailSenderInfo.getSendUserPassword();
			if (ValueUtil.isNotEmpty(sendUserId)) {
				senderImpl.setUsername(sendUserId);
			}

			if (ValueUtil.isNotEmpty(senderImpl.getUsername())) {
				helper.setFrom(senderImpl.getUsername());
			} else {
				helper.setFrom("annoymous@gargoyle.com");
			}

			if (ValueUtil.isNotEmpty(sendUserPassword)) {
				senderImpl.setPassword(sendUserPassword);
			}

			String host = mailSenderInfo.getHost();
			if (ValueUtil.isNotEmpty(host))
				senderImpl.setHost(host);

			String port = mailSenderInfo.getPort();
			if (ValueUtil.isNotEmpty(port)) {
				try {

					senderImpl.setPort(Integer.parseInt(port));
				} catch (NumberFormatException e) {
					LOGGER.error(ValueUtil.toString(e));
					throw e;
				}
			}

			if (mailSenderInfo.isSsl()) {
				LOGGER.debug("smpts");
				senderImpl.setProtocol("smtps");
//				senderImpl.getJavaMailProperties().put("mail.smtps.ssl.trust", "*");
//				senderImpl.getJavaMailProperties().put("mail.smtp.auth", "true");
			}

			switch (mailSenderInfo.getType()) {
			case POP3:
				senderImpl.getJavaMailProperties().put("mail.pop3.starttls.enable", "true");
				senderImpl.getJavaMailProperties().put("mail.smtp.starttls.enable", "false");
				break;
			case SMPT:
				senderImpl.getJavaMailProperties().put("mail.pop3.starttls.enable", "false");
				senderImpl.getJavaMailProperties().put("mail.smtp.starttls.enable", "true");
				break;
			}

		}

		// senderImpl.getJavaMailProperties().put("mail.smtps.ssl.checkserveridentity",
		// "true");

		// senderImpl.getJavaMailProperties().put("mail.smtp.starttls.enable",
		// "true");

		/*
		 * #Mail Cont.# first find emailtemplate.
		 * 
		 * if is empty, mailCont will be send. if null , template will be send.
		 */
		String mailContent = mail.getMailContent();
		String contentType = mail.getContentType();

		if (ValueUtil.isEmpty(contentType)) {
			contentType = "text/plain";
		}

		if (ValueUtil.isEmpty(mailContent)) {
			mailContent = "-- Empty --";
			contentType = "text/plain";
		}

		if (mail.getEmailTemplate() != null) {
			Template template = mail.getEmailTemplate();
			template.setEncoding(_encoding);
			StringWriter stringWriter = new StringWriter();
			template.merge(velocityContext, stringWriter);
			mailContent = stringWriter.toString();
		}

		helper.setText(contentType, mailContent);

		// attachment
		List<AttachmentItem> attachmentItems = mail.getAttachmentItems();
		for (AttachmentItem item : attachmentItems) {
			helper.addAttachment(item.getDisplayName(), item.getDataSource());
		}

		senderImpl.send(message);

	}

	// private File writeTemplate(String templateName) {
	// File file = new File(templateName);
	// ClassLoader classLoader = getClass().getClassLoader();
	// try {
	// String result =
	// IOUtils.toString(classLoader.getResourceAsStream("templates/emailtemplate.vm"));
	//
	// FileUtils.writeStringToFile(file, result);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return file;
	// }
}