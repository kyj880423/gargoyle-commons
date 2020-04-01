/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.thread
 *	작성일   : 2016. 11. 26.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.threads;

import java.util.function.Consumer;

import com.kyj.fx.commons.utils.ExceptionHandler;

/**
 * @author KYJ
 *
 */
/**
 * @author KYJ (callakrsos@naver.com)
 *
 * @param <R>
 */
public class DemonThreadFactory<R> {

	private CallableThreadFactory<R> defaultCallableThreadFactory;

	public DemonThreadFactory() {
		defaultCallableThreadFactory = new DefaultCallableThreadFactory<R>();
	}

	public DemonThreadFactory(CallableThreadFactory<R> factory) {
		defaultCallableThreadFactory = factory;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 3.
	 * @return
	 */
	public static <R> DemonThreadFactory<R> newInstance() {
		return new DemonThreadFactory<R>();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 6. 14. 
	 * @param run
	 * @param onSuccess
	 * @param onError
	 * @return
	 */
	public Thread newThread(CloseableCallable<R> run, Consumer<R> onSuccess, ExceptionHandler onError) {
		return defaultCallableThreadFactory.newThread(run, onSuccess, onError);
	}
}
