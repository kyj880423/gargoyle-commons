/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.vo
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.vo;

public class MethodParameterDVO {

	private String parameter;
	private String parameterType;
	// 메소드 유형 ??
	private String type;
	private String description;

	public MethodParameterDVO(String parameter, String parameterType, String type, String description) {
		super();
		this.parameter = parameter;
		this.parameterType = parameterType;
		this.type = type;
		this.description = description;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return parameterType + " " + parameter;
	}

}
