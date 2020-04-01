/**
 * KYJ
 * 2015. 9. 11.
 */
package com.kyj.fx.commons.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.mail.Mail;
import com.kyj.fx.commons.mail.MailConst;
import com.kyj.fx.commons.mail.Mailer;
import com.kyj.fx.commons.mail.SenderMailInfo;
import com.kyj.fx.commons.memory.ResourceLoader;

/**
 * @author KYJ
 *
 */
public class MailUtil {
	private static Logger LOGGER = LoggerFactory.getLogger(MailUtil.class);

	private MailUtil() {

	}

	/**
	 * 메일전송
	 *
	 * @Date 2015. 9. 13.
	 * @param mail
	 *            메일을 보내기 위한 정보가 담긴 객체
	 * @param mailContent
	 *            메일템플릿안에 들어갈 내용이 담기는 정보.
	 * @throws Exception
	 * @User KYJ
	 */
	@Deprecated
	public static void sendMail(List<Mail> mails, Map<String, Object> mailContent) throws Exception {
		Mailer bean = BeanUtil.getBean("mailer", Mailer.class);
		sendMail(bean, mails, mailContent);
	}

	@Deprecated
	public static void sendMail(Mailer mailer, List<Mail> mails, Map<String, Object> mailContent) {
		sendMail(mailer, mails, mailContent, err -> {
			LOGGER.error(ValueUtil.toString(err));
		});
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 14.
	 * @param mailer
	 * @param mails
	 * @param mailContent
	 * @param errorHandler
	 * @return
	 * @Deprecated
	 */
	@Deprecated
	public static void sendMail(Mailer mailer, List<Mail> mails, Map<String, Object> mailContent, Consumer<Exception> errorHandler) {
		try {
			VelocityContext merge = toVelocityContext(mailContent);
			SenderMailInfo sender = BeanUtil.getBean("mailSenderInfo", SenderMailInfo.class);
			mailer.sendMail(sender, mails, merge);

		} catch (Exception e) {
			if (errorHandler != null)
				errorHandler.accept(e);

		}
	}

	/**
	 * 메일전송 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 18.
	 * @param mail
	 *            메일을 보내기 위한 정보가 담긴 객체
	 * @param mailContent
	 *            메일템플릿안에 들어갈 내용이 담기는 정보.
	 * @return 메일전송 성공 여부
	 * @throws InvalidArgumentException
	 *             사용자 계정정보가 틀린경우
	 * 
	 * @throws Exception
	 */
	public static void sendMail(Mail mail, Map<String, Object> mailContent) throws Exception {

		SenderMailInfo sender = null;

		// 데이터베이스 관리 계정 존재하면 디비에 저장된 계정을 이용함.
		ResourceLoader createInstance = ResourceLoader.getInstance();
		if ("Y".equals(createInstance.get(ResourceLoader.SENDMAIL_CUSTOM_ACCOUNT_USE_YN, "Y"))) {

			String userId = createInstance.get(ResourceLoader.SENDMAIL_CUSTOM_USER_ID);
			String userPwd = createInstance.get(ResourceLoader.SENDMAIL_CUSTOM_USER_PASSWORD);

			String host = createInstance.get(ResourceLoader.SENDMAIL_CUSTOM_USER_HOST);
			String port = createInstance.get(ResourceLoader.SENDMAIL_CUSTOM_USER_PORT);
			String protocol = createInstance.get(ResourceLoader.SENDMAIL_CUSTOM_USER_PROTOCOL_TYPE, "SMPT");

			// if (ValueUtil.isEmpty(userId, userPwd)) {
			// throw new InvalidArgumentException("sender email id or password
			// is null");
			// }

			sender = new SenderMailInfo();
			sender.setSendUserId(userId);
			sender.setSendUserPassword(userPwd);

			sender.setHost(host);
			sender.setPort(port);

			try {
				SenderMailInfo.MailType type = SenderMailInfo.MailType.valueOf(protocol);
				sender.setType(type);
			} catch (Exception e) {
				// Nothing.
			}

		} else {
			sender = BeanUtil.getBean("mailSenderInfo", SenderMailInfo.class);
		}

		Mailer mailer = new Mailer();
		mailer.setEncoding("UTF-8");
		sendMail(sender, mail, mailer, mailContent);
	}

	/**
	 * 메일전송
	 *
	 * @Date 2015. 9. 13.
	 * @param mail
	 *            메일을 보내기 위한 정보가 담긴 객체
	 * @param mailContent
	 *            메일템플릿안에 들어갈 내용이 담기는 정보.
	 * @throws Exception
	 * @User KYJ
	 */
	@Deprecated
	public static void sendMail(SenderMailInfo sender, Mail mail, Map<String, Object> mailContent) throws Exception {
		try {
			Mailer bean = BeanUtil.getBean("mailer", Mailer.class);
			sendMail(sender, mail, bean, mailContent);
		} catch (Exception e) {
			// LOGGER.error(ValueUtil.toString(e));
			// return false;
			throw e;
		}
		// return true;
	}

	public static void sendMail(SenderMailInfo sender, Mail mail, Mailer mailer, Map<String, Object> mailContent) throws Exception {
		try {

			VelocityContext merge = toVelocityContext(mailContent);
			mailer.sendMail(sender, mail, merge);
		} catch (Exception e) {
			// LOGGER.error(ValueUtil.toString(e));
			// return false;
			throw e;
		}
		// return true;
	}

	/**
	 * Map객체를 VelocityContext 객체로 변환.
	 *
	 * @Date 2015. 9. 13.
	 * @param mailContent
	 * @return
	 * @User KYJ
	 */
	private static VelocityContext toVelocityContext(Map<String, Object> mailContent) {
		Iterator<String> iterator = mailContent.keySet().iterator();
		VelocityContext velocityContext = new VelocityContext();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object value = mailContent.get(key);

			velocityContext.put(key, value);
		}
		return velocityContext;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 14.
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static Template createTemplate(File url) throws Exception {
		return createTemplate(url.toURI().toURL());
	}

	public static Template createTemplate(String templateCont) throws Exception {
		return createTemplate(templateCont.getBytes());
	}

	public static Template createTemplate(byte[] templateCont) throws Exception {
		return createTemplate(new ByteArrayInputStream(templateCont));
	}

	public static Template createTemplate(URL url) throws Exception {
		Template createTemplate = null;
		try (InputStream openStream = url.openStream()) {
			createTemplate = createTemplate(openStream);
		}
		return createTemplate;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 14.
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static Template createTemplate(InputStream is) throws Exception {
		String readFileToString = ValueUtil.toString(is);
		RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
		StringReader reader = new StringReader(readFileToString);
		SimpleNode node = runtimeServices.parse(reader, "URLTemplate");
		Template template = new Template();
		template.setRuntimeServices(runtimeServices);
		template.setData(node);
		template.initDocument();
		return template;
	}

	/**
	 * 템플릿 정보를 얻어온다. templateName에 정보가 없다면 defaultTemplateName정보를 사용함.
	 *
	 * @Date 2015. 9. 13.
	 * @param velocityEngine
	 * @param templateName
	 * @param defaultTemplateName
	 * @return
	 * @User KYJ
	 */
	public static Template getTemplate(VelocityEngine velocityEngine, String templateName, String defaultTemplateName) {
		return getTemplate(velocityEngine, templateName, defaultTemplateName, MailConst.MAILER_DEFAULT_ENCODING);
	}

	/**
	 * 템플릿 정보를 얻어온다. templateName에 정보가 없다면 defaultTemplateName정보를 사용함.
	 *
	 * @Date 2015. 9. 13.
	 * @param velocityEngine
	 * @param templateName
	 * @param defaultTemplateName
	 * @param encoding
	 * @return
	 * @User KYJ
	 */
	public static Template getTemplate(VelocityEngine velocityEngine, String templateName, String defaultTemplateName, String encoding) {
		Template template = null;
		if (templateName == null) {
			template = velocityEngine.getTemplate("./templates/" + defaultTemplateName, encoding);
		} else {
			template = velocityEngine.getTemplate("./templates/" + velocityEngine, encoding);
		}
		return template;
	}
}
