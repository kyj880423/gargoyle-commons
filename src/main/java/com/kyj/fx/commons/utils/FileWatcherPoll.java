/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 8. 3.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import com.kyj.fx.commons.threads.ExecutorDemons;

/**
 * 
 * 
 * commons-io 기반의 파일 변경을 추적하는 객체로써 <br/> 
 * <br/>
 * FileWatcher를 사용하면 OS기반의 파일 변경을 감지하므로 이벤트가 여러번 발생될 수 있다.<br/>
 * <br/>
 * connons-io에서는 이전 파일에 대한 메타정보를 가지고 차이가 발생했는지를 검사하는 객체로<br/>
 * <br/>
 * 여러번 호출되는 이벤트에 대한 우려가 해소되지만, 프로그램내에서 루프를 돌려 검사하므로 리소스 비용이 더 높다.<br/>
 * <br/> 
 *  
 * @author KYJ
 *
 */
public abstract class FileWatcherPoll implements Runnable, Closeable {

	private File path;
	private File target;
	private FileAlterationMonitor monitor;
	private boolean isLoad;
	private boolean isStop;

	public static final WatchEvent.Kind<Object> KIND_OVERFLOW = StandardWatchEventKinds.OVERFLOW;
	public static final WatchEvent.Kind<Path> KIND_CREATE = StandardWatchEventKinds.ENTRY_CREATE;
	public static final WatchEvent.Kind<Path> KIND_DELETE = StandardWatchEventKinds.ENTRY_DELETE;
	public static final WatchEvent.Kind<Path> KIND_MODIFY = StandardWatchEventKinds.ENTRY_MODIFY;
	
	
	public FileWatcherPoll(File target) {
		this.target = target.getAbsoluteFile();
		this.path = target.getAbsoluteFile().getParentFile();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 19.
	 * @throws Exception
	 */
	public void load() {

		if (isLoad) {
			throw new RuntimeException("Already Loaded.");
		}

		FileAlterationObserver observer = new FileAlterationObserver(path, new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (target.getParentFile().equals(pathname.getParentFile())) {
					if (target.getName().equals(pathname.getName()))
						return true;
				}
				return false;
			}
		});
		monitor = new FileAlterationMonitor(100);
		FileAlterationListener listener = new FileAlterationListenerAdaptor() {
			@Override
			public void onFileCreate(File file) {
				// code for processing creation event
			}

			@Override
			public void onFileDelete(File file) {
				// code for processing deletion event
			}

			@Override
			public void onFileChange(File file) {
				// if (target.equals(file))
				onUpdate(StandardWatchEventKinds.ENTRY_MODIFY, file.toPath());
			}
		};
		monitor.setThreadFactory(threadFactory);
		observer.addListener(listener);
		monitor.addObserver(observer);
		isLoad = true;

	}
	
	/**
	 * @최초생성일 2019. 3. 20.
	 */
	private ThreadFactory threadFactory = ExecutorDemons.newDefaultThreadFactory("FileWatchPoll");
	

	/**
	 * @return the threadFactory
	 */
	public ThreadFactory getThreadFactory() {
		return threadFactory;
	}

	/**
	 * @param threadFactory the threadFactory to set
	 */
	public void setThreadFactory(ThreadFactory threadFactory) {
		this.threadFactory = threadFactory;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 19.
	 * @param kind
	 * @param context
	 */
	public abstract void onUpdate(Kind<?> kind, Path context);

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 19. 
	 */
	public void stop() {
		isStop = true;
		try {
			monitor.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		try {
			this.isStop = true;
			monitor.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			monitor.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
