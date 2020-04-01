/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2016. 2. 4.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.functions;

/**
 * 데이터베이스 수행처리에 대한 상태 등록
 *
 * 사용방법 ::
 *
 * 리스너를 사용하고자하는 클래스에 아래 인터페이스를 등록하고
 *
 * DBUtil.registQuertyListener(this)를 이용하여 리스너를 등록하고
 *
 * 인터페이스함수를 구현한다.
 *
 * 쿼리가 실행될때마다 onQuertying함수가 호출됨.
 * @author KYJ
 *
 */
public interface DbExecListener {
	/**
	 * 쿼리가 돌아가기 시작하면 처리할 내용을 기술한다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 2. 12.
	 * @param query
	 *            실행되는 쿼리내용
	 */
	public void onQuerying(String query);
}
