/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.jsr233
 *	작성일   : 2019. 10. 24.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.jsr233;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public enum JythonVariables {
	ARGS("__args__"), BASEDIR("__BASEDIR__"), ENGINE("__ENGINE__");
	String varName;

	JythonVariables(String varName) {
		this.varName = varName;
	}

	public String getVarName() {
		return this.varName;
	}
}
