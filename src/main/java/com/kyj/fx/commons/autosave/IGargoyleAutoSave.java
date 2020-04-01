/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.text
 *	작성일   : 2019. 9. 23.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.autosave;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.TimerTask;

import com.kyj.fx.commons.threads.DemonTimerManager;
import com.kyj.fx.commons.utils.FileUtil;
import com.kyj.fx.commons.utils.ValueUtil;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public interface IGargoyleAutoSave extends Closeable {

	/**
	 * backup 주기 second <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 23.
	 * @return
	 */
	public default int inteval() {
		return 60 * 1000;
	};

	/**
	 * 스케줄러 사용 유무 결정 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 23.
	 * @return
	 */
	public boolean useAutoSaveScheduler();

	/**
	 * 자동 저장 처리가되는 대상 파일.
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 30.
	 * @return
	 */
	public File scheduleAutoSaveFile();

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 30.
	 * @return
	 */
	public InputStream scheduleAutoSaveContent();

	/**
	 * AutoSave 수행 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 23.
	 * @param clazz
	 * @param inteval
	 */
	public default void onAutoSaveAction(AutoSaveEvent e) {

	};

	/**
	 * 작업을 실행하지않게 설정 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 30.
	 * @return
	 */
	public default boolean pause() {
		return false;
	}

	/**
	 * 
	 * AutoSave 수행처리를 위한 스케줄 정의 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 23.
	 */
	public default TimerTask autoSaveSchedule() {
		return new TimerTask() {

			@Override
			public void run() {

				if (pause())
					return;

				AutoSaveEvent event = new AutoSaveEvent();
				event.setInteval(inteval());
				event.setCaller(IGargoyleAutoSave.this);
				InputStream in = null;
				try {
					File target = scheduleAutoSaveFile();
					if (!target.exists())
						target.createNewFile();
					in = scheduleAutoSaveContent();

					if (in == null) {
						event.setFailedReson("input stream is null.");
						event.setPass(false);
						return;
					}

					writeSave(target, in);
					event.setPass(true);
				} catch (Exception e) {
					e.printStackTrace();
					event.setFailedReson(ValueUtil.toString(e));
					event.setPass(false);
				} finally {
					onAutoSaveAction(event);
					if (in != null)
						try {
							in.close();
						} catch (IOException e) {
							/* Nothing. */}
				}
			}
		};
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 30.
	 * @param target
	 * @param in
	 * @throws Exception
	 */
	public default void writeSave(File target, InputStream in) throws Exception {
		FileUtil.writeFile(target, in);

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 24.
	 * @return
	 */
	public default String scheduleName() {
		return getClass().getName() + "-auto-save-scheduler";
	};

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 24.
	 */
	public default void scheduleStop() {
		DemonTimerManager.cancel(scheduleName());
	}

	@Override
	default void close() throws IOException {
		scheduleStop();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 23.
	 */
	public default void startAutoSaveScheduler() {
		if (useAutoSaveScheduler()) {
			if (inteval() == -1)
				return;
			TimerTask task = autoSaveSchedule();
			if (task == null)
				return;
			int inteval = inteval();
			DemonTimerManager.doSchedule(scheduleName(), task, inteval, inteval);
		}
	}

}
