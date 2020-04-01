/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.rss
 *	작성일   : 2018. 10. 27.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.rss;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * bean strucutre reference site : https://ko.wikipedia.org/wiki/RSS <br/>
 * 
 * 
 * RSS 2.0 Bean Class <br/>
 * 
 * @author KYJ (callakrsos@naver.com)
 *
 */
@XmlRootElement(name = "item")
public class RssItem {
	private RssTitle title;
	private RssLink link;
	private RssDescription description;
	private RssPubDate pubDate;
	private RssGuid guid;

	/**
	 * @return the title
	 */
	@XmlElement(name = "title")
	public RssTitle getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(RssTitle title) {
		this.title = title;
	}

	/**
	 * @return the link
	 */
	@XmlElement(name = "link")
	public RssLink getLink() {
		return link;
	}

	/**
	 * @param link
	 *            the link to set
	 */
	public void setLink(RssLink link) {
		this.link = link;
	}

	/**
	 * @return the description
	 */
	@XmlElement(name = "description")
	public RssDescription getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(RssDescription description) {
		this.description = description;
	}

	/**
	 * @return the pubDate
	 */
	@XmlElement(name = "pubDate")
	public RssPubDate getPubDate() {
		return pubDate;
	}

	/**
	 * @param pubDate
	 *            the pubDate to set
	 */
	public void setPubDate(RssPubDate pubDate) {
		this.pubDate = pubDate;
	}

	/**
	 * @return the guid
	 */
	@XmlElement(name = "guid")
	public RssGuid getGuid() {
		return guid;
	}

	/**
	 * @param guid
	 *            the guid to set
	 */
	public void setGuid(RssGuid guid) {
		this.guid = guid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[title=" + title + ", link=" + link + ", description=" + description + ", pubDate=" + pubDate + "]";
	}

}
