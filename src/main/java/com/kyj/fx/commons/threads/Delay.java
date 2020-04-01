/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.threads
 *	작성일   : 2019. 6. 14.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.threads;

import java.util.function.Consumer;

import com.kyj.fx.commons.utils.ExceptionHandler;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class Delay {

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 6. 14.
	 * @param mills
	 * @param run
	 */
	public void delay(long mills, Runnable run) {
		CloseableCallable<Boolean> timer = () -> {
			Thread.sleep(mills);
			return true;
		};
		Consumer<Boolean> onSuccess = t -> ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(run);
		DemonThreadFactory<Boolean> newInstance = new DemonThreadFactory<>();
		Thread newThread = newInstance.newThread(timer, onSuccess, new ExceptionHandler() {

			@Override
			public void handle(Exception t) {
				throw new RuntimeException(t);
			}
		});
		newThread.start();
	}
}
