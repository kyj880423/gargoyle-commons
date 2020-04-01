/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework
 *	작성일   : 2016. 5. 22.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.functions;

/**
 * PrimaryStage가 닫힐때 처리되는 내용을 기술함. Main.java 파일 참조.
 *
 * @author KYJ
 *
 */
public interface PrimaryStageCloseable {

	/**
	 * 메인 스테이지가 닫힐때 처리할 내용을 기술한다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 4. 26.
	 */
	public void closeRequest();
}
