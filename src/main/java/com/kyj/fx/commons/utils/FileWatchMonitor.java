/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 8. 3.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.mariadb.jdbc.internal.logging.Logger;
import org.mariadb.jdbc.internal.logging.LoggerFactory;

/**
 * @author KYJ
 *
 */
public class FileWatchMonitor implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileWatchMonitor.class);

	private WatchService watcher;
	private Map<WatchKey, Path> keys;
	private List<BiConsumer<Kind<?>, Path>> onUpdates = new ArrayList<>();
	private Path dir;
	private boolean isLoad;
	private boolean isWorking;

	/**
	 * Creates a WatchService and registers the given directory
	 */
	public FileWatchMonitor() {
		this.keys = new HashMap<WatchKey, Path>();
		this.onUpdates.add((kind, path) -> {
			LOGGER.debug("{} : {} ", kind.name(), path);
		});
	}

	/**
	 * @return the dir
	 */
	public Path getDir() {
		return dir;
	}

	/**
	 * @param dir
	 *            the dir to set
	 */
	public void setDir(Path dir) {
		this.dir = dir;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 3.
	 * @throws IOException
	 */
	public void load() throws IOException {

		if (isLoad) {
			throw new RuntimeException("Already Loaded.");
		}

		this.watcher = FileSystems.getDefault().newWatchService();
		walkAndRegisterDirectories(this.dir);

		isLoad = true;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 3.
	 * @return
	 */
	public boolean isStop() {
		return !isWorking;
	}

	/*
	 * Async Start.
	 */
	@Override
	public void run() {
		if (!isLoad)
			throw new RuntimeException("not loaded..");
		
		processEvents();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 3.
	 * @throws IOException
	 */
	public void stop() throws IOException {
		if (watcher != null) {
			isWorking = false;
			watcher.close();
		}

	}

	/**
	 * Register the given directory with the WatchService; This function will be
	 * called by FileVisitor
	 */
	private void registerDirectory(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		keys.put(key, dir);
	}

	/**
	 * Register the given directory, and all its sub-directories, with the
	 * WatchService.
	 */
	private void walkAndRegisterDirectories(final Path start) throws IOException {
		// register directory and sub-directories
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				registerDirectory(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * Process all events for keys queued to the watcher
	 */
	void processEvents() {
		for (;;) {
			isWorking = true;
			// wait for key to be signalled
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}

			Path dir = keys.get(key);
			if (dir == null) {
				System.err.println("WatchKey not recognized!!");
				continue;
			}

			List<WatchEvent<?>> pollEvents = key.pollEvents();
			LOGGER.debug("{}", pollEvents.size());
			for (WatchEvent<?> event : pollEvents) {
				@SuppressWarnings("rawtypes")
				WatchEvent.Kind kind = event.kind();

				// Context for directory entry event is the file name of entry
				@SuppressWarnings("unchecked")
				Path name = ((WatchEvent<Path>) event).context();
				Path child = dir.resolve(name);

				// print out event
				Kind<?> kind2 = event.kind();
				if (onUpdates != null)
					onUpdates.forEach(action -> {
						try {
							action.accept(kind2, child);
						} catch (Exception e) {
							e.printStackTrace();
						}
					});

				// if directory is created, and watching recursively, then
				// register it and its sub-directories
				if (kind == ENTRY_CREATE) {
					try {
						if (Files.isDirectory(child)) {
							walkAndRegisterDirectories(child);
						}
					} catch (IOException x) {
						// do something useful
					}
				}
			}

			// reset key and remove from set if directory no longer accessible
			boolean valid = key.reset();
			if (!valid) {
				keys.remove(key);

				// all directories are inaccessible
				if (keys.isEmpty()) {
					break;
				}
			}
		}
	}

	/**
	 * @return the onUpdates
	 */
	public List<BiConsumer<Kind<?>, Path>> getOnUpdates() {
		return onUpdates;
	}

	/**
	 * @param onUpdates
	 *            the onUpdates to set
	 */
	public void setOnUpdates(List<BiConsumer<Kind<?>, Path>> onUpdates) {
		this.onUpdates = onUpdates;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 3.
	 * @param ev
	 * @return
	 */
	public boolean addEvent(BiConsumer<Kind<?>, Path> ev) {
		return this.onUpdates.add(ev);
	}

}
