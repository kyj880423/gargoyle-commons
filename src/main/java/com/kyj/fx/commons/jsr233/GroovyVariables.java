/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.jsr233
 *	작성일   : 2019. 10. 28.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.jsr233;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public enum GroovyVariables {
	ARGS("args"), BASEDIR("BASEDIR"), ENGINE("ENGINE");
	String varName;

	GroovyVariables(String varName) {
		this.varName = varName;
	}

	public String getVarName() {
		return this.varName;
	}
}
