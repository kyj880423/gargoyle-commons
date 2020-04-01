/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.memory
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.functions.PrimaryStageCloseable;
import com.kyj.fx.commons.utils.DialogUtil;
import com.kyj.fx.commons.utils.ValueUtil;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

/**
 *
 * Application의 메인 스테이지를 기억한다.
 *
 * @author KYJ
 *
 */
public class StageStore {

	private static Logger LOGGER = LoggerFactory.getLogger(StageStore.class);

	private static List<PrimaryStageCloseable> listeners = new ArrayList<PrimaryStageCloseable>();

	/********************************
	 * 작성일 : 2016. 7. 26. 작성자 : KYJ
	 *
	 * 프로그램 종료시 처리할 내용을 구현할 이벤트를 등록하는 함수.
	 *
	 * @param listener
	 ********************************/
	public static void addPrimaryStageCloseListener(PrimaryStageCloseable listener) {
		listeners.add(listener);
	}

	/**
	 * 어플리케이션이 종료될때 처리할 이벤트를 구현한다.
	 *
	 * PrimaryStageCloseable을 구현하고
	 * addPrimaryStageCloseListener(PrimaryStageCloseable) 함수에 리스너를 등록한 함수는 프로그램
	 * 종료시 처리할 이벤트가 실행되게 된다.
	 *
	 * 주로 화면에 대한 Resource 해제등의 로직이 대상이됨.
	 *
	 * @최초생성일 2016. 7. 26.
	 */
	public static EventHandler<WindowEvent> onPrimaryStageCloseRequest = event -> {

		Optional<Pair<String, String>> showYesOrNoDialog = DialogUtil.showYesOrNoDialog("Exit?", "Exit???");

		if (showYesOrNoDialog.isPresent()) {
			Pair<String, String> pair = showYesOrNoDialog.get();
			if ("Y".equals(pair.getValue())) {

				for (PrimaryStageCloseable c : listeners) {
					try {
						c.closeRequest();
					} catch (Exception e) {
						LOGGER.error(ValueUtil.toString(e));
					}

				}

			} else {
				event.consume();
			}
		}

	};

	private StageStore() {

	}

	/**
	 * 어플리케이션에 사용할 기본 기능을 세팅한다 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 23.
	 * @param primaryStage
	 */
	public static void installStageStore(Stage primaryStage) {
		primaryStage.setOnCloseRequest(onPrimaryStageCloseRequest);
	}

	/**
	 * 메인스테이지
	 *
	 * @최초생성일 2015. 11. 27.
	 */
	private static Stage primaryStage;

	public final static Stage getPrimaryStage() {
		return primaryStage;
	}

	public static synchronized void setPrimaryStage(Stage primaryStage) {
		StageStore.primaryStage = primaryStage;
	}

}
