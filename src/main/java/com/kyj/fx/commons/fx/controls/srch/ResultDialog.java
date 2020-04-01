/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.popup
 *	작성일   : 2015. 10. 17.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.srch;

import java.util.function.Consumer;

import javafx.scene.control.Button;

/**
 * 팝업을 띄운후 처리결과로 사용하기 위한 객체
 * 
 * @author KYJ
 *
 */
public class ResultDialog<T> {

	public static int OK = 0;
	public static int CANCEL = 1;
	public static int YES = 2;
	public static int NO = 3;
	public static int SELECT = 4;

	/**
	 * 상태값을 지정 KYJ
	 */
	private int status;
	/**
	 * 사용자 정의 데이터 KYJ
	 */
	private T data;
	/**
	 * 필요시 사용할 버튼 정보 KYJ
	 */
	private Button clickButton;
	/**
	 * 필요시 사용할 컨슈머 KYJ
	 */
	private Consumer<T> consumer;

	/**
	 * KYJ
	 * 
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * KYJ
	 * 
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * KYJ
	 * 
	 * @return the userData
	 */
	public T getData() {
		return data;
	}

	/**
	 * KYJ
	 * 
	 * @param data
	 *            the userData to set
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * KYJ
	 * 
	 * @return the clickButton
	 */
	public Button getClickButton() {
		return clickButton;
	}

	/**
	 * KYJ
	 * 
	 * @param clickButton
	 *            the clickButton to set
	 */
	public void setClickButton(Button clickButton) {
		this.clickButton = clickButton;
	}

	/**
	 * KYJ
	 * 
	 * @return the consumer
	 */
	public Consumer<T> getConsumer() {
		return consumer;
	}

	/**
	 * KYJ
	 * 
	 * @param consumer
	 *            the consumer to set
	 */
	public void setConsumer(Consumer<T> consumer) {
		this.consumer = consumer;
	}

	public void consume() {
		if (this.consumer != null)
			this.consumer.accept(getData());
	}

}
