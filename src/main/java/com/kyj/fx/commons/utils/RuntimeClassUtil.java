/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * runtime execution
 *
 * @author KYJ
 *
 */
public class RuntimeClassUtil {

	private static Logger LOGGER = LoggerFactory.getLogger(RuntimeClassUtil.class);

	private static final String DEFAULT_ENCODING = "UTF-8";

	public static List<String> exe(List<String> args) throws Exception {
		return exe(DEFAULT_ENCODING, args);
	}

	/********************************
	 * 작성일 : 2016. 6. 19. 작성자 : KYJ
	 *
	 * 단순 실행처리.
	 *
	 * @param args
	 * @throws Exception
	 ********************************/
	public static void simpleExec(List<String> args) throws Exception {
		try {

			if (args == null || args.isEmpty())
				return;

			ProcessBuilder pb = new ProcessBuilder(args);
			pb.start();
		} catch (IOException e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	private static ProcessBuilder getBuilder(String... args) {
		return new ProcessBuilder(args);
	}

	private static ProcessBuilder getBuilder(List<String> args) {
		return new ProcessBuilder(args);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 14. 
	 * @param encode
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static List<String> exe(String encode, String... args) throws Exception {
		return exe(encode, getBuilder(args));
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 14. 
	 * @param encode
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static List<String> exe(String encode, List<String> args) throws Exception {
		return exe(encode, getBuilder(args));
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 14. 
	 * @param encode
	 * @param pb
	 * @return
	 * @throws Exception
	 */
	public static List<String> exe(String encode, ProcessBuilder pb) throws Exception {

		List<String> resultList = new ArrayList<String>();
		InputStream i = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		Process p = null;
		try {
			p = pb.start();
			p.waitFor(100, TimeUnit.MILLISECONDS);
			i = p.getInputStream();
			isr = new InputStreamReader(i, encode);
			br = new BufferedReader(isr);

			String temp = null;

			while ((temp = br.readLine()) != null) {
				resultList.add(ValueUtil.rightTrim(temp) + "\n");
			}

			br.close();
			isr.close();
			i.close();
			p.destroy();
			p.exitValue();

		} catch (IOException e) {
			e.printStackTrace();
			if (br != null) {
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (i != null) {
				try {
					i.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (p != null) {
				p.destroy();
				p.exitValue();

			}

		}

		return resultList;
	}

	// public static List<String> exe(String encode, List<String> args) throws
	// Exception {
	//
	// List<String> resultList = new ArrayList<String>();
	// ProcessBuilder pb = new ProcessBuilder(args);
	// InputStream i = null;
	// InputStreamReader isr = null;
	// BufferedReader br = null;
	// Process p = null;
	// try {
	// p = pb.start();
	// p.waitFor(100, TimeUnit.MILLISECONDS);
	// i = p.getInputStream();
	// isr = new InputStreamReader(i, encode);
	// br = new BufferedReader(isr);
	//
	// String temp = null;
	//
	// while ((temp = br.readLine()) != null) {
	// resultList.add(ValueUtil.rightTrim(temp) + "\n");
	// }
	//
	// br.close();
	// isr.close();
	// i.close();
	// p.destroy();
	// p.exitValue();
	//
	// } catch (IOException e) {
	// e.printStackTrace();
	// if (br != null) {
	// try {
	// br.close();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// }
	// if (isr != null) {
	// try {
	// isr.close();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// }
	// if (i != null) {
	// try {
	// i.close();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// }
	//
	// } finally {
	// if (br != null) {
	// try {
	// br.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// if (p != null) {
	// p.destroy();
	// p.exitValue();
	//
	// }
	//
	// }
	//
	// return resultList;
	// }

	public static void addShutdownHook(Runnable run) {
		addShutdownHook(new Thread(run));
	}

	public static void addShutdownHook(Thread t) {
		Runtime.getRuntime().addShutdownHook(t);
	}

	/**
	 * 파일을 오픈함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 4. 11.
	 * @param file
	 * @return
	 */
	public static boolean openFile(File file) {
		return FileUtil.openFile(file);
	}

	private static BiFunction<File, InputStream, Void> FILE_WRITER_HANDLER = new BiFunction<File, InputStream, Void>() {

		@Override
		public Void apply(File outputFile, InputStream in) {
			if (outputFile == null || in == null)
				return null;

			try (BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
				try (FileWriter writer = new FileWriter(outputFile, true)) {

					String temp = null;
					while ((temp = br.readLine()) != null) {
						LOGGER.debug(temp);
						writer.write(temp);
						writer.write(System.lineSeparator());
					}

				} catch (IOException e) {
					LOGGER.error(ValueUtil.toString(e));
				}
			} catch (IOException e1) {
				LOGGER.error(ValueUtil.toString(e1));
			}

			return null;
		}
	};

	/**
	 * 실행하되 실행된 로그 정보를 outputFile위치로 로그를 write한다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 4. 6.
	 * @param args
	 * @param outputFile
	 * @return
	 * @throws Exception
	 */
	public static int exe(List<String> args, File outputFile) throws Exception {
		return exe(args, outputFile, FILE_WRITER_HANDLER);
	}

	static int exe(List<String> args, File outputFile, BiFunction<File, InputStream, Void> streamHandler) throws Exception {
		int result = -1;
		ProcessBuilder pb = new ProcessBuilder(args);
		pb.redirectErrorStream(true);

		Process p = null;
		InputStream is = null;

		try {
			p = pb.start();

			is = p.getInputStream();
			streamHandler.apply(outputFile, is);

			p.waitFor(10, TimeUnit.SECONDS);

			p.destroy();
			result = p.exitValue();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return result;
	}

	public static int exe(List<String> args, Consumer<String> messageReceiver) throws Exception {
		return exe(args, Charset.defaultCharset(), messageReceiver);
	}

	static class TThread extends Thread implements Closeable {
		protected Process p;
		protected OutputStream out;
		protected boolean closed;

		public TThread(Process p, OutputStream out) {
			this.p = p;
			this.out = out;
		}

		public InputStream getInputStream() {
			return this.p.getInputStream();
		}

		public InputStream getErrorStream() {
			return this.p.getErrorStream();
		}

		public boolean isClosed() {
			return closed;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.Closeable#close()
		 */
		@Override
		public void close() throws IOException {

			if (p != null) {
				p.destroy();
			}

			InputStream inputStream = getInputStream();
			if (inputStream != null)
				inputStream.close();
			InputStream errorStream = getErrorStream();
			if (errorStream != null)
				errorStream.close();

		}

	}

	public static int exe(List<String> args, OutputStream out, OutputStream err) throws Exception {
		int result = -1;
		ProcessBuilder pb = new ProcessBuilder(args);
		pb.redirectErrorStream(true);

		Process p = null;
		TThread outThread = null;
		TThread errThread = null;
		try {
			p = pb.start();

			outThread = new TThread(p, out) {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Thread#run()
				 */
				@Override
				public void run() {

					try {
						InputStream inputStream = p.getInputStream();
						p.waitFor(10, TimeUnit.SECONDS);

						byte[] b = new byte[4096];

						while (inputStream.read(b) != -1) {
							this.out.write(b);
						}

						this.out.flush();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						closed = true;
					}
				}

			};

			errThread = new TThread(p, err) {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Thread#run()
				 */
				@Override
				public void run() {

					try {
						InputStream inputStream = p.getErrorStream();
						p.waitFor(10, TimeUnit.SECONDS);

						byte[] b = new byte[4096];

						while (inputStream.read(b) != -1) {
							out.write(b);
						}
						out.flush();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						closed = true;
					}
				}

			};

			outThread.start();
			errThread.start();

			while (p.isAlive()) {
				Thread.sleep(1000);
			}
			while (!outThread.isClosed() && !outThread.isClosed()) {
				Thread.sleep(1000);
			}

			result = p.exitValue();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (p != null) {
				p.destroy();
			}

			if (outThread != null)
				outThread.close();

			if (errThread != null)
				errThread.close();

		}
		return result;
	}

	public static int exe(List<String> args, Charset encoding, Consumer<String> messageReceiver) throws Exception {
		int result = -1;
		ProcessBuilder pb = new ProcessBuilder(args);
		pb.redirectErrorStream(true);

		Process p = null;
		BufferedReader br = null;

		try {
			p = pb.start();

			if (encoding != null)
				br = new BufferedReader(new InputStreamReader(p.getInputStream(), encoding));
			else
				br = new BufferedReader(new InputStreamReader(p.getInputStream()));

			p.waitFor(10, TimeUnit.SECONDS);
			String temp = null;

			while ((temp = br.readLine()) != null) {
				if (messageReceiver != null)
					messageReceiver.accept(temp);
			}
			p.destroy();
			result = p.exitValue();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
		}
		return result;
	}

	public static void pipe(String[] args, AtomicBoolean forceStop, Consumer<String> messageReceiver)
			throws InterruptedException, IOException {
		pipe(args, Charset.forName("UTF-8"), forceStop, messageReceiver);
	}

	public static void pipe(String[] args, Charset encoding, AtomicBoolean forceStop, Consumer<String> messageReceiver)
			throws InterruptedException, IOException {
		ProcessBuilder pb = new ProcessBuilder(args);
		pb.redirectErrorStream(true);

		Process p = null;
		BufferedReader br = null;

		p = pb.start();

		br = new BufferedReader(new InputStreamReader(p.getInputStream(), encoding));
		p.waitFor(10, TimeUnit.SECONDS);
		String temp = null;

		// Monitor 수행을 중단하기 위한 스레드
		new Thread(new PipeSteop(p, forceStop), "Stop-monitor").start();

		while ((temp = br.readLine()) != null) {
			messageReceiver.accept(temp);
		}

	}

	static class PipeSteop implements Runnable {
		Process p;
		AtomicBoolean forceStop;

		PipeSteop(Process p, AtomicBoolean forceStop) {
			this.p = p;
			this.forceStop = forceStop;
		}

		@Override
		public void run() {
			if (forceStop.get()) {
				p.destroy();
			}
		}

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 22.
	 * @param args
	 * @param convert
	 *            Integer : lineNum InputStream : dataStream
	 * @return
	 * @throws Exception
	 */
	/* @Deprecated 테스트를 더 해봐야함. */

	public static void exeAsynchLazy(List<String> args, BiConsumer<Integer, String> convert) {
		exeAsynchLazy(args, "UTF-8", convert, null);
	}

	public static void exeAsynchLazy(List<String> args, String encoding, BiConsumer<Integer, String> convert) {
		exeAsynchLazy(args, encoding, convert, null);
	}

	public static void simpleExeAsynchLazy(List<String> args, Consumer<Exception> errorHandler) {
		simpleExeAsynchLazy(args, errorHandler, null );
	}

	public static void simpleExeAsynchLazy(List<String> args) {
		Thread thread = new SimpleAsynch(args);
		thread.setDaemon(true);
		thread.setName("simple-exeAsynchLazy");
		thread.start();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 28. 
	 * @param args
	 * @param errorHandler
	 * @param options
	 */
	public static void simpleExeAsynchLazy(List<String> args, Consumer<Exception> errorHandler, Consumer<Thread> options) {
		Thread thread = new SimpleAsynch(args);
		thread.setDaemon(true);
		thread.setName("simple-exeAsynchLazy");
		if(options!=null)
			options.accept(thread);
		thread.start();
	}
	
	public static void exeAsynchLazy(List<String> args, String encoding, BiConsumer<Integer, String> convert,
			Consumer<Exception> errorHandler) {
		Thread thread = new Asynch(args, encoding, convert, errorHandler);
		thread.setDaemon(true);
		thread.setName("exeAsynchLazy");
		thread.start();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 27.
	 * @param args
	 * @param encoding
	 * @param convert
	 */
	public static void exeAsynchLazy(List<String> args, String encoding, Consumer<String> convert) {
		exeAsynchLazy(args, encoding, convert, null);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 27.
	 * @param args
	 * @param encoding
	 * @param message
	 * @param errorHandler
	 */
	public static void exeAsynchLazy(List<String> args, String encoding, Consumer<String> message, Consumer<Exception> errorHandler) {
		Thread thread = new AsynSingle(args, encoding, message, errorHandler);
		thread.setDaemon(true);
		thread.setName("exeAsynchLazy");
		thread.start();
	}

	public static void exeSynch(List<String> args, String encoding, BiConsumer<Integer, String> convert, Consumer<Exception> errorHandler) {
		exeSynch(args, encoding, null, convert, errorHandler);
	}

	public static void exeSynch(List<String> args, String encoding, Consumer<ProcessBuilder> pbPrerequire,
			BiConsumer<Integer, String> convert, Consumer<Exception> errorHandler) {
		Asynch thread = new Asynch(args, encoding, convert, errorHandler);

		thread.setPbPrerequire(pbPrerequire);

		thread.setDaemon(true);
		thread.setName("exeAsynchLazy");
		thread.run();
	}

	private static class SimpleAsynch extends Thread {
		List<String> args;
		Consumer<Exception> errorHandler;

		public SimpleAsynch(List<String> args) {
			this.args = args;
		}

		public SimpleAsynch(List<String> args, Consumer<Exception> errorHandler) {
			this.args = args;
			this.errorHandler = errorHandler;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			try {
				RuntimeClassUtil.simpleExec(args);
			} catch (Exception e) {
				if (null != errorHandler)
					errorHandler.accept(e);
				else {
					LOGGER.error(ValueUtil.toString(e));
					throw new RuntimeException(e);
				}

			}
		}

	}

	/**
	 * @author KYJ
	 *
	 */
	private static class Asynch extends Thread {
		List<String> args;
		String encoding;
		BiConsumer<Integer, String> consumer;
		Consumer<Exception> errorHandler;
		Consumer<ProcessBuilder> pbPrerequire;

		public Asynch(List<String> args, String encoding, BiConsumer<Integer, String> consumer, Consumer<Exception> errorHandler) {
			this.args = args;
			this.encoding = encoding;
			this.consumer = consumer;
			this.errorHandler = errorHandler;
			setName("RuntimeClassUtil-AsynchThread");
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2017. 12. 13.
		 * @return
		 */
		public Consumer<ProcessBuilder> getPbPrerequire() {
			return pbPrerequire;
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2017. 12. 13.
		 * @param pbPrerequire
		 */
		public void setPbPrerequire(Consumer<ProcessBuilder> pbPrerequire) {
			this.pbPrerequire = pbPrerequire;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {

			ProcessBuilder pb = new ProcessBuilder(args);
			pb.redirectErrorStream(true);
			int result = -1;
			Process p = null;
			BufferedReader br = null;
			// StringBuffer sb = new StringBuffer();

			try {
				// pb.directory(new File("C:\\SVN_WORKSPACE"));
				// Map<String, String> environment = pb.environment();

				if (pbPrerequire != null)
					pbPrerequire.accept(pb);

				// LOGGER.debug("Print Env : \n{} ",
				// ValueUtil.toString(environment));
				p = pb.start();

				br = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName(encoding)));
				p.waitFor(10, TimeUnit.SECONDS);

				String temp = null;
				while ((temp = br.readLine()) != null) {
					consumer.accept(result, temp);
				}

				p.destroy();
				result = p.exitValue();
			} catch (Exception e) {
				if (errorHandler != null)
					LOGGER.error(ValueUtil.toString(e));
				else
					errorHandler.accept(e);
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {

						if (errorHandler != null)
							LOGGER.error(ValueUtil.toString(e));
						else
							errorHandler.accept(e);
					}
				}
			}

		}

	}

	/**
	 * @author KYJ
	 *
	 */
	static class AsynSingle extends Thread {

		List<String> args;
		String encoding;
		Consumer<String> consumer;
		Consumer<Exception> errorHandler;
		Consumer<ProcessBuilder> pbPrerequire;

		public AsynSingle(List<String> args, String encoding, Consumer<String> consumer, Consumer<Exception> errorHandler) {
			this.args = args;
			this.encoding = encoding;
			this.consumer = consumer;
			this.errorHandler = errorHandler;
			setName("RuntimeClassUtil-AsynchThread");
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2017. 12. 13.
		 * @return
		 */
		public Consumer<ProcessBuilder> getPbPrerequire() {
			return pbPrerequire;
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2017. 12. 13.
		 * @param pbPrerequire
		 */
		public void setPbPrerequire(Consumer<ProcessBuilder> pbPrerequire) {
			this.pbPrerequire = pbPrerequire;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {

			ProcessBuilder pb = new ProcessBuilder(args);
			pb.redirectErrorStream(true);
			Process p = null;
			BufferedReader br = null;
			// StringBuffer sb = new StringBuffer();

			try {
				// pb.directory(new File("C:\\SVN_WORKSPACE"));
				// Map<String, String> environment = pb.environment();

				if (pbPrerequire != null)
					pbPrerequire.accept(pb);

				// LOGGER.debug("Print Env : \n{} ",
				// ValueUtil.toString(environment));
				p = pb.start();

				br = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName(encoding)));
				p.waitFor(10, TimeUnit.SECONDS);

				String temp = null;
				StringBuffer sb = new StringBuffer();
				while ((temp = br.readLine()) != null) {
					sb.append(temp).append("\n");
				}

				if (consumer != null)
					consumer.accept(sb.toString());

				p.destroy();
				p.exitValue();
			} catch (Exception e) {
				if (errorHandler != null)
					LOGGER.error(ValueUtil.toString(e));
				else
					errorHandler.accept(e);
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {

						if (errorHandler != null)
							LOGGER.error(ValueUtil.toString(e));
						else
							errorHandler.accept(e);
					}
				}
			}

		}

	}

}
