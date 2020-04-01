/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.word
 *	작성일   : 2016. 12. 23.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.mime;

import java.util.function.BiConsumer;

/**
 * RuntimeClassUtil 사용처리와 관계되어 처리할 인터페이스 함수들을 구현.
 * @author KYJ
 *
 */
public interface RuntimeExucteHandlerble {

	/**
	 * 실행 코드에 대한 내용이 기술됨.
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 23.
	 */
	public void execute();

	/**
	 * 인코딩
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 23.
	 * @return
	 */
	public String encoding();

	/**
	 * 작업처리 핸들러
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 23.
	 * @return
	 */
	public BiConsumer<Integer, StringBuffer> handler();

}
