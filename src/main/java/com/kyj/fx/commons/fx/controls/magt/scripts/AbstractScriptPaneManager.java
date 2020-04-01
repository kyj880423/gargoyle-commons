/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.magt.scripts
 *	작성일   : 2019. 11. 18.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.magt.scripts;

import org.fxmisc.richtext.CodeArea;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public abstract class AbstractScriptPaneManager {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 18.
	 * @return
	 */
	public abstract CodeArea createScriptPane();

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 18.
	 * @return
	 */
	public abstract ReleaseScriptTab createScriptPaneTab(EventScriptDVO onRead);

}
