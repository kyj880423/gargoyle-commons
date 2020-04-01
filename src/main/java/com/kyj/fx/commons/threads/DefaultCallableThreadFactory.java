/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.thread
 *	작성일   : 2016. 11. 26.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.threads;

import java.io.IOException;
import java.util.function.Consumer;

import com.kyj.fx.commons.utils.ExceptionHandler;

/**
 * @author KYJ
 *
 */
public class DefaultCallableThreadFactory<R> implements CallableThreadFactory<R> {

	/*
	 * 스레드 처리 내용을 성공했을떄와 실패했을때로 분류한 모델
	 */
	@Override
	public Thread newThread(CloseableCallable<R> run, Consumer<R> onSuccess, ExceptionHandler onError) {

		Thread thread = new Thread() {

			@Override
			public void run() {
				try {
					R result = run.call();
					onSuccess.accept(result);
				} catch (Exception e) {
					onError.handle(e);
				} finally {
					try {
						run.close();
					} catch (IOException e) {
						onError.handle(e);
					}
				}
			}
		};
		thread.setName("Default-Callable-Thread-Factory-Trhead");

		return thread;
	}

}
