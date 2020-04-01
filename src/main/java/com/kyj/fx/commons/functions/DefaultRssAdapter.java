/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.functions
 *	작성일   : 2018. 10. 27.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.functions;

import java.io.InputStream;
import java.net.URL;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.fx.controls.rss.RssRoot;
import com.kyj.fx.commons.utils.ExceptionHandler;
import com.kyj.fx.commons.utils.RequestUtil;
import com.kyj.fx.commons.utils.ResponseHandler;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.utils.XMLUtils;

/**
 * 
 * Rss Feed to beans <br/>
 * 
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class DefaultRssAdapter implements Function<URL, RssRoot> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRssAdapter.class);

	/**
	 * to Rss Object <br/>
	 * 
	 * @최초생성일 2018. 10. 27.
	 */
	private ResponseHandler<RssRoot> rssHandler = new ResponseHandler<RssRoot>() {
		@Override
		public RssRoot apply(InputStream is, Integer code) {
			try {
				return XMLUtils.JaxbUtils.load(is, RssRoot.class);
			} catch (Exception e) {
				getExceptionHandler().handle(e);
			}
			return null;
		}

	};
	/**
	 * exception handler<br/>
	 * 
	 * @최초생성일 2018. 10. 27.
	 */
	private ExceptionHandler defaultExceptionHandler = new ExceptionHandler() {
		@Override
		public void handle(Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	};

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 27.
	 * @return
	 */
	public ResponseHandler<RssRoot> getHandler() {
		return rssHandler;
	}

	public ExceptionHandler getExceptionHandler() {
		return defaultExceptionHandler;
	}

	/**
	 * @return the rssHandler
	 */
	public ResponseHandler<RssRoot> getRssHandler() {
		return rssHandler;
	}

	/**
	 * @param rssHandler
	 *            the rssHandler to set
	 */
	public void setRssHandler(ResponseHandler<RssRoot> rssHandler) {
		this.rssHandler = rssHandler;
	}

	/**
	 * @return the defaultExceptionHandler
	 */
	public ExceptionHandler getDefaultExceptionHandler() {
		return defaultExceptionHandler;
	}

	/**
	 * @param defaultExceptionHandler
	 *            the defaultExceptionHandler to set
	 */
	public void setDefaultExceptionHandler(ExceptionHandler defaultExceptionHandler) {
		this.defaultExceptionHandler = defaultExceptionHandler;
	}

	@Override
	public RssRoot apply(URL url) {
		RssRoot request = null;
		try {
			request = RequestUtil.request(url, getHandler());
		} catch (Exception e) {
			getExceptionHandler().handle(e);
		}

		return request;
	}

}
