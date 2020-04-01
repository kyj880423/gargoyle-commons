/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.rss
 *	작성일   : 2018. 10. 27.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.rss;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * bean strucutre reference site : https://ko.wikipedia.org/wiki/RSS <br/>
 * 
 * 
 * RSS 2.0 Bean Class <br/>
 * 
 * @author KYJ (callakrsos@naver.com)
 *
 */
@XmlRootElement(name = "channel")
public class RssChannel {

	private RssTitle title;
	private RssDescription description;
	private RssLink link;
	private List<RssItem> items;

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
	 * @return the rssItem
	 */
	@XmlElements(value = { @XmlElement(name = "item", type = RssItem.class) })
	public List<RssItem> getItems() {
		return items;
	}

	/**
	 * @param rssItem
	 *            the rssItem to set
	 */
	public void setItems(List<RssItem> items) {
		this.items = items;
	}

}
