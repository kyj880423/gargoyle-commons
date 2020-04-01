/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.parser
 *	작성일   : 2018. 5. 28.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KYJ
 *
 */
public class BehaviorClassVisitor extends AbstractBehaviorVisitor {

	private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorClassVisitor.class);

	@Override
	public void visite(String code) {

		if (code == null)
			return;

		LOGGER.info("##[Class] out code ##  {}~{} ", super.getStartIdx(), super.getEndIdx());
//		LOGGER.info(code);
//		LOGGER.info("## ######## ##");

	}

}
