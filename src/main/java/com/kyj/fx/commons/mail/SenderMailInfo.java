/**
 * 
 */
package com.kyj.fx.commons.mail;

import java.util.Properties;

import javax.mail.Session;

/**
 * @author user
 *
 */
public class SenderMailInfo {
	/**
	 * 메일 전송자에 대한 계정입력. ex) 네이버id@naver.com
	 */
	private String sendUserId;
	/**
	 * 메일전송자의 비밀번호
	 */
	private String sendUserPassword;

	public String host;

	public String port;

	private boolean ssl;

	public MailType type = MailType.SMPT;

	public enum MailType {
		SMPT, POP3;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public MailType getType() {
		return type;
	}

	public void setType(MailType type) {
		this.type = type;
	}

	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the sendUserId
	 */
	public String getSendUserId() {
		return sendUserId;
	}

	/**
	 * @param sendUserId
	 *            the sendUserId to set
	 */
	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	/**
	 * @return the sendUserPassword
	 */
	public String getSendUserPassword() {
		return sendUserPassword;
	}

	/**
	 * @param sendUserPassword
	 *            the sendUserPassword to set
	 */
	public void setSendUserPassword(String sendUserPassword) {
		this.sendUserPassword = sendUserPassword;
	}

	public String getDefaultEncoding() {
		return "UTF-8";
	}

	public Session getSession() {
		return null;
	}

	public Properties getJavaMailProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHost() {
		return this.host;
	}

	/**
	 * @return the ssl
	 */
	public boolean isSsl() {
		return ssl;
	}

	/**
	 * @param ssl
	 *            the ssl to set
	 */
	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

}
