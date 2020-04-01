/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import com.kyj.fx.commons.memory.StageStore;

import javafx.stage.Stage;

/**
 * @author KYJ
 *
 */
public class RaxSharedMemory {

	/**
	 * return PrimaryStage.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 18.
	 * @return
	 */
	public static Stage getPrimaryStage() {
		return StageStore.getPrimaryStage();
	}

}
