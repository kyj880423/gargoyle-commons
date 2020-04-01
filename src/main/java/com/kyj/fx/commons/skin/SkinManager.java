/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.momory
 *	작성일   : 2016. 2. 2.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.skin;

import com.kyj.fx.fxloader.FxSkinManager;

/**
 *
 * 스킨정보를 관리하는 매니저
 *
 * @author KYJ
 *
 */
public final class SkinManager extends FxSkinManager {

//	private static Logger LOGGER = LoggerFactory.getLogger(SkinManager.class);

	/**
	 * 인스턴스
	 */
	private static SkinManager manager;

	/**
	 * 생성방지를 위해 private로선언
	 */
	private SkinManager() {
	}

	/**
	 * 스킨 인스턴스 리턴
	 *
	 * @return
	 */
	public static SkinManager getInstance() {
		if (manager == null)
			manager = new SkinManager();
		return manager;
	}

}
