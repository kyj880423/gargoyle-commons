/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.word
 *	작성일   : 2016. 12. 23.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.mime;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.memory.ResourceLoader;
import com.kyj.fx.commons.utils.RuntimeClassUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.utils.EncrypUtil;

/**
 *
 * 비동기로 Word문서의 내용을 붙여넣고 실행처리할 수 있는 처리를 지원.
 *
 * @author KYJ
 *
 */
public class AsynchWordExecutor implements RuntimeExucteHandlerble {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AsynchWordExecutor.class);

	private AbstractMimeAdapter itemAdapter;

	public AsynchWordExecutor(AbstractMimeAdapter itemAdapter) {
		this.itemAdapter = itemAdapter;
	}

	/* (non-Javadoc)
	 * @see com.kyj.fx.voeditor.visual.framework.word.RuntimeExucteHandlerble#execute()
	 */
	public void execute() {
		//		RuntimeClassUtil.exeAsynchLazy(findWordLocationCommand(), encoding(), handler());
		RuntimeClassUtil.simpleExeAsynchLazy(executeWordCommand(), errorHandler());
	}

	public List<String> executeWordCommand() {

		String wRealPath = findWordRealPath();

		if (wRealPath == null) {
			String temp = onCannotFoundWordPath();
			if (temp == null)
				return Collections.emptyList();

			wRealPath = temp;
		}

		File tempFile = itemAdapter.toTempFile();

		Function<String[], List<String>> command = getCommand();
		List<String> apply = command.apply(new String[] { wRealPath, tempFile.getAbsolutePath() });
		return apply;

	}

	private String findWordRealPath() {
		String wPath = ResourceLoader.getInstance().get(ResourceLoader.MS_WORD_PATH);

		String wRealPath = null;
		if (ValueUtil.isNotEmpty(wPath)) {
			try {
				wPath = EncrypUtil.decryp(wPath);
				File wFile = new File(wPath);
				if (wFile.exists()) {
					if (wFile.getAbsolutePath().endsWith("WINWORD.exe")) {
						wRealPath = wFile.getAbsolutePath();
					}
				}
			} catch (Exception e) {
				LOGGER.error(ValueUtil.toString(e));
			}

		}
		return wRealPath;
	}

	/**
	 * word파일의 위치를 찾는 명령코드 Window 기준.
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 23.
	 * @return
	 */
	public List<String> findWordLocationCommand() {
		return findWordLocationCommand("15.0");
	}

	private List<String> findWordLocationCommand(String version) {

		List<String> command = new ArrayList<String>();
		command.add("REG");
		command.add("QUERY");
		command.add("HKCU\\Software\\Microsoft\\Office\\" + version + "\\Word\\Options");
		command.add("/v");
		command.add("PROGRAMDIR");

		return command;
	}

	/* (non-Javadoc)
	 * @see com.kyj.fx.voeditor.visual.framework.word.RuntimeExucteHandlerble#encoding()
	 */
	@Deprecated
	public String encoding() {
		return itemAdapter.getEncoding().name();
	}

	public Consumer<Exception> errorHandler() {
		return err -> LOGGER.error(ValueUtil.toString(err));
	}

	/* (non-Javadoc)
	 * @see com.kyj.fx.voeditor.visual.framework.word.RuntimeExucteHandlerble#handler()
	 */
	@Deprecated
	public BiConsumer<Integer, StringBuffer> handler() {
		BiConsumer<Integer, StringBuffer> convert = (code, buf) -> {

			File tempFile = itemAdapter.toTempFile();

			if (code != -1) {
				String str = buf.toString();
				String matchingStr = "REG_SZ";
				int indexOf = str.indexOf(matchingStr);
				if (indexOf != -1) {
					str = str.substring(indexOf + 6).trim() + File.separator + "WINWORD.exe";
					LOGGER.debug("레지스트리에서 검색된 MS WORD 경로 :  {}", str);
				} else {
					LOGGER.debug("wrod파일 경로를 찾을 수 없음.");
					str = onCannotFoundWordPath();
					return;
				}

				File wordFileLocation = new File(str);
				if (wordFileLocation.exists()) {
					try {
						Function<String[], List<String>> command = getCommand();
						List<String> apply = command.apply(new String[] { str, tempFile.getAbsolutePath() });

						RuntimeClassUtil.simpleExec(apply);
					} catch (Exception e) {
						LOGGER.error(ValueUtil.toString(e));
					}
				}

			} else {
				LOGGER.debug("Code Result is Invalide {}", code);
			}

		};
		return convert;
	}

	public Function<String[], List<String>> getCommand() {

		return args -> {
			return Arrays.asList(args);
		};
	}

	public String onCannotFoundWordPath() {

		String path = null;
		for (double i = 19; i >= 10; i--) {
			String version = String.format("%.1f", i);
			List<String> buf = findWordLocationCommand(version);
			try {
				List<String> exe = RuntimeClassUtil.exe(buf);
				Optional<String> reduce = exe.stream().reduce((str1, str2) -> str1.concat(" ").concat(str2));
				if (reduce.isPresent()) {
					String str = reduce.get();

					String matchingStr = "REG_SZ";
					int indexOf = str.indexOf(matchingStr);
					if (indexOf != -1) {
						path = str.substring(indexOf + 6).trim() + File.separator + "WINWORD.exe";
						break;
					}

				}

			} catch (Exception e) {
				LOGGER.error(ValueUtil.toString(e));
			}

		}

		if (path != null) {
			try {
				ResourceLoader.getInstance().put(ResourceLoader.MS_WORD_PATH, EncrypUtil.encryp(path));
			} catch (Exception e) {
				LOGGER.error(ValueUtil.toString(e));
			}
		}

		return path;
	}

}
