/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.vo
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.vo;

import java.util.ArrayList;
import java.util.List;

import com.kyj.fx.commons.fx.controls.grid.AbstractDVO;

/**
 * insp.biz.source.vo
 * 2014. 6. 24.
 * KYJ
 * @처리내용 : vo
 */

/**
 * @author KYJ
 */
public class MethodDVO extends AbstractDVO{
	// 메소드명
	private String methodName;

	/* 2015.02.26 주석 사유 : level로 대처. */
	// 메소드 유형
	// private String methodType;

	// 메소드의 주요 기능
	private String mainFunction;

	// 레벨 Instance Method or Static Method
	private String level;

	// 접근지정자 public 등.
	private String visivility;

	// 메소드 설명
	private String description;

	// 메소드 워크 플로우.. 이미지등.
	private byte[] image;

	// 메소드 워크 플로우
	private String flow;

	private String isNewOrChg;

	// 메소드에 대한 메타정보
	private MethodMetaDVO methodMetaDVO;

	// 메소드의 파라미터 정보
	private List<MethodParameterDVO> methodParameterDVOList;

	public MethodDVO() {

		super();
		// methodType = "Instance \nMethod";
		level = "Instance \nMethod";
		visivility = "public";
		isNewOrChg = "변경";
		methodMetaDVO = new MethodMetaDVO();
		methodParameterDVOList = new ArrayList<>();
	}

	public MethodMetaDVO getMethodMetaDVO() {
		return methodMetaDVO;
	}

	public void setMethodMetaDVO(MethodMetaDVO methodMetaDVO) {
		this.methodMetaDVO = methodMetaDVO;
	}

	public String getIsNewOrChg() {
		return isNewOrChg;
	}

	public void setIsNewOrChg(String isNewOrChg) {
		this.isNewOrChg = isNewOrChg;
	}

	public List<MethodParameterDVO> getMethodParameterDVOList() {
		return methodParameterDVOList;
	}

	public void setMethodParameterDVOList(List<MethodParameterDVO> methodParameterDVOList) {
		this.methodParameterDVOList = methodParameterDVOList;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	// public String getMethodType() {
	// return methodType;
	// }
	//
	// public void setMethodType(String methodType) {
	// this.methodType = methodType;
	// }

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getMainFunction() {
		return mainFunction;
	}

	public void setMainFunction(String mainFunction) {
		this.mainFunction = mainFunction;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getVisivility() {
		return visivility;
	}

	public void setVisivility(String visivility) {
		this.visivility = visivility;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public String getReturnType() {
		return methodMetaDVO.getReturnType();
	}

	public void setReturnType(String returnType) {
		this.methodMetaDVO.setReturnType(returnType);
	}

	public String getParameters() {
		return this.methodMetaDVO.getParameters();
	}

	@Override
	public String toString() {
		return "MethodDVO [methodName=" + methodName + ", methodType=" + level + "]";
	}

	/**
	 * 메소드에 대한 메타정보
	 *
	 * @author KYJ
	 *
	 */
	public class MethodMetaDVO {
		private String returnType = "void";

		String getReturnType() {
			return returnType;
		}

		void setReturnType(String returnType) {
			this.returnType = returnType;
		}

		String getParameters() {

			List<MethodParameterDVO> mlist = MethodDVO.this.getMethodParameterDVOList();
			if (mlist == null || mlist.size() == 0) {
				return "()";
			}
			StringBuffer sb = new StringBuffer();
			sb.append("(");
			for (MethodParameterDVO dvo : mlist) {
				sb.append(dvo.getType()).append(" ").append(dvo.getParameter()).append(",");
			}
			sb.setLength(sb.length() - 1);
			sb.append(")");

			return sb.toString();
		}
	}

}
