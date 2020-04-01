/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2016. 12. 14.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.Node;

/**
 * 파일 드래그 드롭 기능을 지원
 * 
 * @author KYJ
 *
 */
public abstract class AbstractFileDragDropHelper<T extends Node> extends AbstractDragDropHelper<T> {

	private static Logger LOGGER = LoggerFactory.getLogger(AbstractFileDragDropHelper.class);
	
	//19.1.2 bug... Remove Node. 
//	protected T node;

	public AbstractFileDragDropHelper(T node) {
		super(node);
	}

	/*********************************************************/

	/**
	 * 드래그 드롭시 파일 제한 사이즈 정의
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 10.
	 * @return
	 */
	protected long dragDropLimitSize() {
		return 5 * 1024 * 1024;
	}

	/*********************************************************/

}
