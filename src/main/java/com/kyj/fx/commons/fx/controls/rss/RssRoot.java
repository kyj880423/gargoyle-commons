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
 * @author KYJ (callakrsos@naver.com)
 *
 */
@XmlRootElement(name = "rss")
public class RssRoot {

	private RssChannel channel;

	/**
	 * @return the rssChannel
	 */
	@XmlElement(name = "channel")
	public RssChannel getChannel() {
		return channel;
	}

	/**
	 * @param rssChannel
	 *            the rssChannel to set
	 */
	public void setChannel(RssChannel channel) {
		this.channel = channel;
	}

}
