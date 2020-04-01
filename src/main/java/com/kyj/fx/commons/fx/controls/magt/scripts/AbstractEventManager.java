/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.magt.scripts
 *	작성일   : 2019. 11. 15.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.magt.scripts;

import java.util.List;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public abstract class AbstractEventManager<T extends EventScriptDVO> {

	/**
	 * 로딩시 호출<br/>
	 * 트리를 구성할 스크립트에 대한 정보를 읽어온다.<br/>
	 * 
	 * 리턴되는 데이터에서 EventScriptDVO 클래스 안의 scriptPath에 대한 정보를 바탕으로 트리가 구성된다 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 15.
	 * @param d
	 * @return
	 * @throws Exception
	 */
	public abstract List<T> onLoad() throws Exception;

	/**
	 * 저장 요청시 호출된다 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 15.
	 * @param d
	 * @return
	 * @throws Exception
	 */
	public abstract T onSave(T d) throws Exception;

	/**
	 * 삭제 요청시 처리할 행위를 기술한다 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 15.
	 * @param d
	 * @return
	 * @throws Exception
	 */
	public abstract boolean onDelete(T d) throws Exception;

	/**
	 * 신규로 등록할 아이템에 대한 내용을 기술한다<br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 15.
	 * @param d
	 * @return
	 * @throws Exception
	 */
	public abstract T onNew(T d) throws Exception;

	/**
	 * 스크립트 컨텐츠의 내용을 읽기 요청했을때 발생 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 18.
	 * @param d
	 * @return
	 * @throws Exception
	 */
	public abstract T onRead(T d) throws Exception;

}
