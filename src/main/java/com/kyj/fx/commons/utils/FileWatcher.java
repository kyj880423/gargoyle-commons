/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 8. 3.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.mariadb.jdbc.internal.logging.Logger;
import org.mariadb.jdbc.internal.logging.LoggerFactory;

/**
 * 
 * can be fired multiple times. because of base on operation system event.
 * 
 * @author KYJ
 *
 */
public abstract class FileWatcher implements Runnable, Closeable {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileWatcher.class);

	private WatchService watcher;

	private Path path;
	private File target;
	private Kind<?>[] events;
	private boolean isLoad;
	private boolean isStop;

	/**
	 * Creates a WatchService and registers the given directory
	 */

	public FileWatcher(File target) {
		this.target = target;
		this.path = target.getAbsoluteFile().getParentFile().toPath();
		this.events = new Kind[] { StandardWatchEventKinds.ENTRY_MODIFY };
	}

	public FileWatcher(File target, Kind<?>... events) {
		this.target = target;

		this.path = target.getAbsoluteFile().getParentFile().toPath();
		this.events = events;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 19.
	 * @throws IOException
	 */
	public void load() throws IOException {
		if (isLoad) {
			throw new RuntimeException("Already Loaded.");
		}
		isStop = false;
		this.watcher = FileSystems.getDefault().newWatchService();
		this.path.register(this.watcher, this.events);
		isLoad = true;
	}

	public void stop() {
		isStop = true;
	}

	@Override
	public void run() {
		try {
			WatchKey key = null;
			Path targetPath = target.toPath();
			while ((key = this.watcher.take()) != null) {

				for (WatchEvent<?> event : key.pollEvents()) {
					Kind<?> kind = event.kind();
					Path context = (Path) event.context();

					// System.out.println(context);
					if (targetPath.equals(context)) {
						onUpdate(kind, context);
					}

					if (isStop) {
						break;
					}
				}
				key.reset();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 19.
	 * @param kind
	 * @param context
	 */
	public abstract void onUpdate(Kind<?> kind, Path context);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		this.isStop = true;
		this.watcher.close();
	}

}
