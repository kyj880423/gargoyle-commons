package com.kyj.fx.commons.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;

import org.junit.Test;

public class FileWatcherTest {

//	private File file;
//
//	@Test
//	public void fileWatcherTest() throws IOException, InterruptedException {
//
//		file = new File("file-watcher-test.txt");
//		if (!file.exists())
//			file.createNewFile();
//		FileWatcher fileWatcher = new FileWatcher(file) {
//
//			@Override
//			public void onUpdate(Kind<?> kind, Path context) {
//				System.out.println(kind + " " + context);
//			}
//
//		};
//		fileWatcher.load();
//
//		Thread thread = new Thread(fileWatcher);
//		thread.start();
//
//		Thread.sleep(100000);
//	}
//
//	@Test
//	public void fileWatcherPollTest() throws IOException, InterruptedException {
//
//		file = new File("file-watcher-test.txt");
//		if (!file.exists())
//			file.createNewFile();
//		FileWatcherPoll fileWatcher = new FileWatcherPoll(file) {
//
//			@Override
//			public void onUpdate(Kind<?> kind, Path context) {
//				System.out.println(kind + " " + context);
//			}
//
//		};
//		fileWatcher.load();
//
//		Thread thread = new Thread(fileWatcher);
//		thread.start();
//
//		Thread.sleep(100000);
//	}
}
