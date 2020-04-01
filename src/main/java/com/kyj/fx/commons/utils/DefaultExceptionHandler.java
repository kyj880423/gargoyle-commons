/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Default Error Handler
 * 
 * @author KYJ
 *
 */
public class DefaultExceptionHandler implements ExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

	@Override
	public void handle(Exception t) {
		LOGGER.error(ValueUtil.toString(t));
	}

}
