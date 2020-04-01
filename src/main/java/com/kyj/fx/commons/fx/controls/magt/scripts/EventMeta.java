/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.magt.scripts
 *	작성일   : 2019. 11. 15.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.magt.scripts;

/**
 * 
 * 이벤트 정보에 기술.
 * 
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EventMeta {

	private ReleaseScriptComposite composite;
	/**
	 * @최초생성일 2019. 11. 15.
	 */
	private Object sourceObject;
	private AbstractEventManager eventManager;

	public EventMeta(Object sourceObject) {
		super();
		this.sourceObject = sourceObject;
	}

	/**
	 * @return the sourceObject
	 */
	public Object getSourceObject() {
		return sourceObject;
	}

	/**
	 * @param sourceObject
	 *            the sourceObject to set
	 */
	public void setSourceObject(Object sourceObject) {
		this.sourceObject = sourceObject;
	}

	public static EventMeta create(ReleaseScriptComposite composite, Object sourceObject, AbstractEventManager manager) {
		EventMeta eventMeta = new EventMeta(sourceObject);
		eventMeta.composite = composite;
		eventMeta.eventManager = manager;
		return eventMeta;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 15.
	 * @return
	 */
	public final AbstractEventManager getEventManager() {
		return this.eventManager;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 15.
	 * @return
	 */
	public final ReleaseScriptComposite getReleaseScriptComposite() {
		return this.composite;
	}

}
