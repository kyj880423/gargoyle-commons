/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2017. 1. 6.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.io.File;
import java.io.IOException;

import com.kyj.fx.commons.exceptions.GagoyleRuntimeException;
import com.kyj.fx.commons.fx.controls.CloseableParent;
import com.kyj.fx.commons.utils.FxUtil;

import javafx.scene.layout.BorderPane;

/**
 * @author KYJ
 *
 */

public class LogViewComposite extends CloseableParent<BorderPane> {

	/**
	 * 모니터 대상이 되는 파일.
	 * @최초생성일 2017. 1. 12.
	 */
	private File monitoringFile;

	private LogViewController controller;

	public LogViewComposite() throws Exception {
		super(/*insert parent node */new BorderPane());
		getParent().setCenter(FxUtil.load(LogViewController.class, null, null, c -> {
			this.controller = c;
			c.setComposite(this);
		}));
	}
	public LogViewComposite(File target) throws Exception {
		this();
		this.monitoringFile = target;
	}

	/**
	 * 모니터링할 대상 파일을 변경한다.
	 *
	 * 만약 모니터링중이라면 에러발생.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2017. 1. 13.
	 * @param target
	 * @throws GagoyleRuntimeException
	 *    모니터링중인 상태라면 발생.
	 */
	public void setFile(File target) throws GagoyleRuntimeException{

		if(getController().isRunning())
		{
			throw new GagoyleRuntimeException("Monitor status is Running.");
		}

		this.monitoringFile = target;
	}

	/**
	 * 로그 모니터링 대상이되는 파일 리턴
	 * @작성자 : KYJ
	 * @작성일 : 2017. 1. 6.
	 * @return
	 */
	File getMonitoringFile() {
		return this.monitoringFile;
	}

	/**
	 * 컨트롤러 클래스 리턴
	 * @작성자 : KYJ
	 * @작성일 : 2017. 1. 6.
	 * @return
	 */
	LogViewController getController() {
		return this.controller;
	}

	/**
	 * 모니터링을 시작함.
	 * @작성자 : KYJ
	 * @작성일 : 2017. 1. 13.
	 */
	public void start() {
		getController().start();
	}

	/**
	 * 모니터링을 종료함.
	 * @작성자 : KYJ
	 * @작성일 : 2017. 1. 13.
	 * @return
	 */
	public final boolean stop() {
		try {
			close();
			return getController().isRunning();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.kyj.fx.voeditor.visual.main.layout.CloseableParent#close()
	 */
	@Override
	public void close() throws IOException {
		if (controller != null)
			controller.close();
	}

}
