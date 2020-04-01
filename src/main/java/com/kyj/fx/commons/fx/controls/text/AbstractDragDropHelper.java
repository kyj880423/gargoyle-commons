/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.commons.fx.controls.text
 *	작성일   : 2018. 12. 5.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.Node;
import javafx.scene.input.DragEvent;

/**
 * 드래그 드롭 기능을 지원
 * 
 * @author KYJ
 *
 */
public abstract class AbstractDragDropHelper<T extends Node> {

	private static Logger LOGGER = LoggerFactory.getLogger(AbstractDragDropHelper.class);

	protected T node;

	public AbstractDragDropHelper(T node) {
		this.node = node;
		init(this.node);

		// AS-IS remove.
		// this.node.setOnDragOver(this::onDagOver);
		// this.node.setOnDragDropped(this::onDragDropped);
	}

	/**
	 * initialize handler. </br>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 16.
	 * @param node
	 */
	protected void init(T node) {
		this.node.addEventHandler(DragEvent.DRAG_OVER, this::onDagOver);
		this.node.addEventHandler(DragEvent.DRAG_DROPPED, this::onDragDropped);
	}

	public abstract void onDagOver(DragEvent ev);

	public abstract void onDragDropped(DragEvent ev);

}
