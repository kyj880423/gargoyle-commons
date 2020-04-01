/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.rss
 *	작성일   : 2018. 10. 27.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.rss;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * bean strucutre reference site : https://ko.wikipedia.org/wiki/RSS <br/>
 * 
 * 
 * RSS 2.0 Bean Class <br/>
 * @author KYJ (callakrsos@naver.com)
 *
 */
@XmlRootElement(name = "description")
public class RssDescription {


	private String content;

	/**
	 * @return the content
	 */
	@XmlValue
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return content;
	}

}
