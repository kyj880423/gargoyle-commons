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
public class BehaviorSubProcedureVisitor extends AbstractBehaviorVisitor implements BehaviorClassVisitable {

	private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorSubProcedureVisitor.class);
	private int count;
	private BehaviorClass vbClass;

	@Override
	public void visite(String code) {

		LOGGER.info("##[SubProcedure] out code ##  {}~{} ", super.getStartIdx(), super.getEndIdx());
		// LOGGER.info(code);
		// LOGGER.info("## ######## ##");

	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	public void addCount() {
		count++;
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
