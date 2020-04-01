/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.thread
 *	작성일   : 2017. 4. 27.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.threads;

import java.util.Timer;

/**
 * 
 *  데몬형 타이머 클래스를 제공하기 위한 팩토리 클래스
 *  
 * @author KYJ
 *
 */
public class DemonTimerFactory {

	private DemonTimerFactory() {

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 4. 27. 
	 * @return
	 */
	public static Timer newInsance() {
		return newInsance("Gargoyle-Timer");
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 4. 27. 
	 * @param name
	 * @return
	 */
	public static Timer newInsance(String name) {
		return new Timer(name, true);
	}

}
