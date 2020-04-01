/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.mail
 *	작성일   : 2020. 4. 1.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.mail;

import java.util.Arrays;

import org.apache.velocity.VelocityContext;
import org.junit.Test;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class MailerTest {

	@Test
	public void sendMailTest() throws Exception {

		Mailer mailer = new Mailer();
		VelocityContext velocityContext = new VelocityContext();

		SenderMailInfo mailSenderInfo = new SenderMailInfo();

		mailSenderInfo.setHost("smtp.gmail.com");
		mailSenderInfo.setPort("465");
		mailSenderInfo.setSendUserId("구글 계정");
		mailSenderInfo.setSendUserPassword("앱 비밀번호");
		mailSenderInfo.setSsl(true);

		Mail mail = new Mail();
		mail.setMailSubject("Hello!!");
		mail.setMailContent("Hello~~~");

		mail.setMailCc(Arrays.asList("수신자"));
		mailer.sendMail(mailSenderInfo, mail, velocityContext);

	}

}
