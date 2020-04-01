/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.design
 *	작성일   : 2018. 8. 16.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.design;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.kyj.fx.commons.fx.controls.grid.AbstractDVO;

/**
 * @author KYJ
 *
 */
@XmlRootElement(name = "Properties")
public class BehaviorProperties extends AbstractDVO {

	private String version;
	private String author;
	private String company;
	private String email;
	private String description;
	private String usage;

	/**
	 * @return the version
	 */
	@XmlAttribute(name = "Version")
	public String getVersion() {
		return version;
	}

	/**
	 * @return the author
	 */
	@XmlAttribute(name = "Author")
	public String getAuthor() {
		return author;
	}

	/**
	 * @return the company
	 */
	@XmlAttribute(name = "Company")
	public String getCompany() {
		return company;
	}

	/**
	 * @return the email
	 */
	@XmlAttribute(name = "Email")
	public String getEmail() {
		return email;
	}

	/**
	 * @return the description
	 */
	@XmlAttribute(name = "Description")
	public String getDescription() {
		return description;
	}

	/**
	 * @return the usage
	 */
	@XmlAttribute(name = "Usage")
	public String getUsage() {
		return usage;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @param company
	 *            the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param usage
	 *            the usage to set
	 */
	public void setUsage(String usage) {
		this.usage = usage;
	}

}
