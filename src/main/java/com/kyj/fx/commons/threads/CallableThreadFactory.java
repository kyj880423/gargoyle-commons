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
public interface CallableThreadFactory<R> {

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 26.
	 * @param run
	 * @param onSuccess
	 * @param onError
	 * @return
	 */
	Thread newThread(CloseableCallable<R> run, Consumer<R> onSuccess, ExceptionHandler onError);

}
