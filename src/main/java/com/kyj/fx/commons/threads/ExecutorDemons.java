/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.thread
 *	작성일   : 2016. 10. 5.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 데몬스레드를 생성
 *
 * @author KYJ
 *
 */
public class ExecutorDemons {

	/**
	 * default thread-name.
	 * 
	 * @최초생성일 2016. 10. 12.
	 */
	private static final String DEAMON_THREAD_NAME = "[Gargoye-Excutor-Deamon-Thread]";

	/**
	 * 분석시 이름을 확인할 수 있는 구조로 수정.
	 * 
	 * @author KYJ
	 *
	 */
	static final class GargoyleThreadFactory implements ThreadFactory {
		private String name;

		GargoyleThreadFactory(String name) {
			this.name = name;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
		 */
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r); // Executors.defaultThreadFactory().newThread(r);
			t.setDaemon(true);
			t.setName(name);
			return t;
		}

	}

	/*********************************************************************/
	/**
	 * Gargoyle에서 기본 항목으로 사용하기위한 객체. <br/>
	 * 
	 * @최초생성일 2017. 11. 5.
	 */
	private static ExecutorService gargoyleSystemExecutorService;
	static {
		gargoyleSystemExecutorService = newFixedThreadExecutor(3);
	}

	/**
	 * 
	 * default thread executor. </br>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 5.
	 * @return
	 */
	public static ExecutorService getGargoyleSystemExecutorSerivce() {
		return gargoyleSystemExecutorService;
	}

	/*********************************************************************/

	/**
	 * 
	 * create single thread executor <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 5.
	 * @return
	 */
	public static ExecutorService newSingleThreadExecutor() {
		return newSingleThreadExecutor(DEAMON_THREAD_NAME);
	}

	/**
	 * 
	 * create single thread executor <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 5.
	 * @param name
	 * @return
	 */
	public static ExecutorService newSingleThreadExecutor(String name) {
		return Executors.newSingleThreadExecutor(newDefaultThreadFactory(name));
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 20. 
	 * @param name
	 * @return
	 */
	public static ThreadFactory newDefaultThreadFactory(String name) {
		return new GargoyleThreadFactory(name);
	}
	
	/**
	 * create fixed thread executor <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 5.
	 * @param count
	 * @return
	 */
	public static ExecutorService newFixedThreadExecutor(int count) {
		return newFixedThreadExecutor(DEAMON_THREAD_NAME, count);
	}

	/**
	 * create fixed thread executor <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 5.
	 * @param name
	 * @param count
	 * @return
	 */
	public static ExecutorService newFixedThreadExecutor(String name, int count) {
		return Executors.newFixedThreadPool(count, new GargoyleThreadFactory(name));
	}

}
