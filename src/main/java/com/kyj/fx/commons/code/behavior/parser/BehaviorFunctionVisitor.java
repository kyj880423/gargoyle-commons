/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.parser
 *	작성일   : 2018. 5. 8.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KYJ
 *
 */
public class BehaviorFunctionVisitor extends AbstractBehaviorVisitor implements BehaviorClassVisitable {

	private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorFunctionVisitor.class);
	private BehaviorClass vbClass;

	@Override
	public void visite(String code) {

		if (code == null)
			return;

		LOGGER.info("##[Function] out code ##  {}~{} ", super.getStartIdx(), super.getEndIdx());
		// LOGGER.info(code);
		// LOGGER.info("## ######## ##");

	}

	@Override
	public void setVbClass(BehaviorClass vbClass) {
		this.vbClass = vbClass;

	}

	@Override
	public BehaviorClass getVbClass() {
		return vbClass;
	}

}
