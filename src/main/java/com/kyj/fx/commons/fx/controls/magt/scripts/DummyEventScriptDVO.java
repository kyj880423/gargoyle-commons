/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.magt.scripts
 *	작성일   : 2019. 11. 15.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.magt.scripts;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class DummyEventScriptDVO extends EventScriptDVO {

	public DummyEventScriptDVO(String scriptPath) {
		super("", "", scriptPath);
	}
	public DummyEventScriptDVO(String scriptPath, String name) {
		super("", "", scriptPath, name);
	}
}
