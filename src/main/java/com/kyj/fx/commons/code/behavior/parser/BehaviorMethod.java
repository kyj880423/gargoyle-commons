/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.parser
 *	작성일   : 2018. 5. 8.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.parser;

import java.util.List;

/**
 * 
 * Behavior Method Info
 * 
 * @author KYJ
 *
 */
public class BehaviorMethod {

	private int accessModifier;

	private String name;

	private List<BehaviorArgument> arguments;

	/**
	 * @return the accessModifier
	 */
	public int getAccessModifier() {
		return accessModifier;
	}

	/**
	 * @param accessModifier
	 *            the accessModifier to set
	 */
	public void setAccessModifier(int accessModifier) {
		this.accessModifier = accessModifier;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the arguments
	 */
	public List<BehaviorArgument> getArguments() {
		return arguments;
	}

	/**
	 * @param arguments
	 *            the arguments to set
	 */
	public void setArguments(List<BehaviorArgument> arguments) {
		this.arguments = arguments;
	}

}
