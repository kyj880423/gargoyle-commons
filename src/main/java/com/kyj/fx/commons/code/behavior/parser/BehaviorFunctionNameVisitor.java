/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.parser
 *	작성일   : 2018. 5. 8.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.ValueUtil;

/**
 * @author KYJ
 *
 */
public abstract class BehaviorFunctionNameVisitor extends BehaviorFunctionVisitor implements BehaviorClassVisitable {

	private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorFunctionNameVisitor.class);

	private BehaviorClass vbClass;

	@Override
	public final void visite(String code) {
		ValueUtil.Match convert = new ValueUtil.Match() {

			@Override
			public String apply(String text) {
				return text;
			}
		};

		String name = ValueUtil.regexMatch("^(?i)Function[ ]+(.+)\\(.{0,}\\)", code, 1, convert);
		if (ValueUtil.isEmpty(name)) {
			// name = ValueUtil.regexMatch("^(?i)Function[ ]+(.+)\\(.{0,}\\)",
			// code, 1, str -> str);
		}

		// setStartIdx();
		setEndIdx(getStartIdx() + convert.getEnd());

		visiteMethodName(name);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 11. 27.
	 * @param name
	 */
	public abstract void visiteMethodName(String name);

	@Override
	public void setVbClass(BehaviorClass vbClass) {
		this.vbClass = vbClass;

	}

	@Override
	public BehaviorClass getVbClass() {
		return vbClass;
	}

}
