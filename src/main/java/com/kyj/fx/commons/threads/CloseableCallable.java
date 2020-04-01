/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.thread
 *	작성일   : 2016. 11. 26.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.threads;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * @author KYJ
 *
 */
public interface CloseableCallable<R> extends Callable<R>, Closeable {

	@Override
	default void close() throws IOException {

	}

}
