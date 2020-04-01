/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.main.layout
 *	작성일   : 2016. 2. 22.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.RuntimeClassUtil;
import com.kyj.fx.commons.utils.ValueUtil;

import javafx.scene.Parent;

/**
 *
 * 자원 클로징 처리를 위한
 *
 * 특정 이미지뷰같은 경우 빠른 퍼포멘스를 표현하기 위해
 *
 * 객체 자체에서 리소스를 close하지않고 핸들링해야하는 케이스에 사용한다.
 *
 * 사용되는 케이스는 SystemLayoutViewController에서
 *
 * 새로운 탭을 여는 이벤트 동작에서 주로 사용되며
 *
 * Tab이 close되는 시점에 호출된다.
 *
 * @author KYJ
 *
 */
public abstract class CloseableParent<T extends Parent> implements Closeable {
	private static final Logger LOGGER = LoggerFactory.getLogger(CloseableParent.class);
	private T parent;
	private Thread hook;

	public CloseableParent(T parent) {
		this(parent, true);
	}

	/**
	 * @param parent
	 *            UI에 표현되는 레이아웃
	 * 
	 * @param registShutdownhook
	 *            어플리케이션이 종료될때 한번 더 리소스 해제작업 수행 여부 디폴트값 true
	 * 
	 */
	public CloseableParent(T parent, boolean registShutdownhook) {
		super();
		this.parent = parent;

		if (registShutdownhook) {
			// 반환되지 않은 리소스가 있을지를 대비해, 프로그램이 종료되기 전에 한번더 close요청처리를 한다.
			hook = new Thread(() -> {
				try {
					if (parent != null) {
						LOGGER.debug("Request Close  Resource ... : " + parent.getClass());
						CloseableParent.this.close();
					}
				} catch (IOException e) {
					LOGGER.error(ValueUtil.toString(e));
					e.printStackTrace();
				}
			}, "CloseableParent");
			RuntimeClassUtil.addShutdownHook(hook);
		}

	}

	public T getParent() {
		return parent;
	}

	public void setParent(T parent) {
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Closeable#close()
	 */
	@Override
	public abstract void close() throws IOException;

}
