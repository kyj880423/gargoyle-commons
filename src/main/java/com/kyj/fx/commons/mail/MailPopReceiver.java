/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.mail
 *	작성일   : 2017. 5. 31.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.mail;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.URLName;

import com.sun.mail.pop3.POP3SSLStore;

/**
 * @author KYJ
 *
 */
public class MailPopReceiver implements Runnable {

	private String popHost;
	private String type = "pop3";
	private String hostEmailAddr;
	private String password;
	private String userName = "gargoyle-email-user";
	private boolean debug = true;
	private InetSocketAddress proxy;

	public MailPopReceiver(String popHost, String storeType, String hostEmailAddr, String password) {
		this.popHost = popHost;
		this.hostEmailAddr = hostEmailAddr;
		this.password = password;
	}

	public void receiveEmail() {
		try {
			// 1) get the session object
			Properties properties = System.getProperties(); // new Properties();
			properties.put("mail.pop3.host", popHost);
			properties.put("mail.user", userName);
			properties.put("mail.from", hostEmailAddr);

			properties.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

			properties.put("mail.pop3.socketFactory.port", "995");
			properties.put("mail.pop3.port", "995");

			properties.setProperty("mail.pop3.socketFactory.fallback", "false");

			// 프록시

			if (this.proxy != null) {
				String hostName = proxy.getHostName();
				int port = proxy.getPort();
				String value = port + "";

				properties.put("proxySet", "true");
				properties.put("socksProxyHost", hostName);
				properties.put("socksProxyPort", value);

				properties.put("http.proxyHost", hostName);
				properties.put("http.proxyPort", port + "");
				properties.put("https.proxyHost", hostName);
				properties.put("https.proxyPort", port + "");

				// #####################################
				properties.setProperty("proxySet", "true");
				properties.setProperty("socksProxyHost", hostName);
				properties.setProperty("socksProxyPort", port + "");

				properties.setProperty("http.proxyHost", hostName);
				properties.setProperty("http.proxyPort", port + "");
				properties.setProperty("https.proxyHost", hostName);
				properties.setProperty("https.proxyPort", port + "");
			}

			Authenticator authenticator = new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(hostEmailAddr, password);
				}
			};
			Session emailSession = Session.getInstance(properties, authenticator);
			// Session emailSession = Session.getDefaultInstance(properties);
			emailSession.setDebug(debug);

			// 2) create the POP3 store object and connect with the pop server
			// POP3Store emailStore = (POP3Store) emailSession.getStore(type);
			URLName url = new URLName(type, popHost, 995, "", hostEmailAddr, password);

			POP3SSLStore emailStore = new POP3SSLStore(emailSession, url);

			emailStore.connect();
			System.out.println("connected.");

			// 3) create the folder object and open it
			Folder emailFolder = emailStore.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			// 4) retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();
			for (int i = 0; i < messages.length; i++) {
				Message message = messages[i];
				System.out.println("---------------------------------");
				System.out.println("Email Number " + (i + 1));
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("Text: " + message.getContent().toString());
			}

			// 5) close the store and folder objects
			emailFolder.close(false);
			emailStore.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public InetSocketAddress getProxy() {
		return proxy;
	}

	public void setProxy(InetSocketAddress proxy) {
		this.proxy = proxy;
	}

	@Override
	public void run() {

	}

}
