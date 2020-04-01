/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.threads
 *	작성일   : 2019. 9. 23.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.threads;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class DemonTimerManager {

	public static ConcurrentHashMap<String, Timer> items = new ConcurrentHashMap<>();
	private static final Logger LOGGER = LoggerFactory.getLogger(DemonTimerManager.class);
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 23.
	 * @param name
	 * @return
	 */
	private static Timer newInstance(String name) {

		if (isContains(name)) {
			LOGGER.info(" The previous job is canceled because a job with the same name is running. >> " + name );
			cancel(name);
			LOGGER.info(" The previous job was canceled. >> " + name );
		}

		Timer newInsance = DemonTimerFactory.newInsance(name);
		items.put(name, newInsance);
		return newInsance;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 23.
	 * @param name
	 * @param task
	 * @param delay
	 * @param period
	 */
	public static void doSchedule(String name, TimerTask task, long delay, long period) {
		Timer newInstance = newInstance(name);
		newInstance.schedule(task, delay, period);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 23.
	 * @param name
	 * @return
	 */
	public static boolean isContains(String name) {
		return items.containsKey(name);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 23.
	 * @param name
	 */
	public static void cancel(String name) {
		if (items.containsKey(name)) {
			Timer timer = items.get(name);
			if (timer != null) {
				timer.cancel();
			}

			items.remove(name);
		}
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 23.
	 */
	public static void cancelAll() {
		items.forEachValue(3, timer -> {
			if (timer != null)
				timer.cancel();
		});

		if (items.size() > 10) {
			Optional<Timer> findFirst = items.values().stream().filter(t -> t != null).findFirst();
			findFirst.ifPresent(t -> t.purge());
		}
	}

}
