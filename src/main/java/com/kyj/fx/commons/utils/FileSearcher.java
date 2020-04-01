/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 8. 21.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KYJ
 *
 */
public class FileSearcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileSearcher.class);

	private File root;
	private int maxLevel = 1;

	private String[] fileExtensions;
	
	private Predicate<File> directoryFilter;
	/**
	 * 최종 집계함수 find()에서 제외시킬 파일이 있는경우 처리 
	 * @최초생성일 2019. 3. 21.
	 */
	private Predicate<File> collectFilter = f -> true;

	public static final Predicate<File> DEFAULT_FILTER = new Predicate<File>() {

		/*
		 * 탐색하지않을 파일명을 기입한다. 디폴트로는 소스 디렉토리가 존재하는 위치에 있는 대상은 필터링된다.
		 */
		// List<String> exceptNames =
		// ConfigResourceLoader.getInstance().getValues(ConfigResourceLoader.FILTER_NOT_SRCH_DIR_NAME_SOURCE_TYPE,
		// ",");

		@Override
		public boolean test(File file) {
			return true; // !exceptNames.contains(file.getName());
		}
	};

	/**
	 * return wildcard filter.<br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 25.
	 * @param wildcard
	 * @return
	 */
	public static Predicate<File> wildCardFilter(final String wildcard) {
		return new Predicate<File>() {
			WildcardFileFilter filter = new WildcardFileFilter(wildcard);

			@Override
			public boolean test(File t) {
				return filter.accept(t);
			}

		};
	}

	/**
	 * @return the collectFilter
	 */
	public Predicate<File> getCollectFilter() {
		return collectFilter;
	}

	/**
	 * @param collectFilter the collectFilter to set
	 */
	public void setCollectFilter(Predicate<File> collectFilter) {
		this.collectFilter = collectFilter;
	}

	/**
	 * @return the filter
	 */
	public final Predicate<File> getFilter() {
		return directoryFilter;
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	// public final void setFilter(Predicate<File> filter) {
	// this.filter = filter;
	// }

	public FileSearcher(File root) {
		this(root, -1, new String[] {}, DEFAULT_FILTER);
	}

	public FileSearcher(File root, Predicate<File> directoryFilter) {
		this(root, -1, new String[] {}, directoryFilter);
	}

	public FileSearcher(File root, int maxLevel, String[] fileExtensions) {
		this(root, -1, fileExtensions, DEFAULT_FILTER);
	}

	public FileSearcher(File root, int maxLevel, String[] fileExtensions, Predicate<File> directoryFilter) {
		this.root = root;
		this.maxLevel = maxLevel;
		this.fileExtensions = fileExtensions;
		this.directoryFilter = directoryFilter;
	}
	
	public FileSearcher(File root, int maxLevel, List<String> fileExtensions, Predicate<File> directoryFilter) {
		this.root = root;
		this.maxLevel = maxLevel;
		this.fileExtensions = fileExtensions.stream().toArray(String[]::new);
		this.directoryFilter = directoryFilter;
	}

	public List<File> find() {
		return find(file -> file);
	}

	public <T> List<T> find(Function<File, T> func) {
		List<T> findFiles = new ArrayList<>();
		recursive(findFiles, root, func);
		return findFiles;
	}

	private <T> void recursive(List<T> findFiles, File file, Function<File, T> func) {
		recursive(findFiles, file, 0, func);
	}

	private <T> void recursive(List<T> findFiles, File file, int currentLevel, Function<File, T> func) {
		//this is directory filter
		if (!getFilter().test(file)) {
			LOGGER.debug("탐색하지 않음. {} ", file.getAbsolutePath());
			return;
		}
		/*
		 * 일단 워크스페이스를 선택한경우라고 가정하고 워크스페이스내에 폴더들을 순차적으로 돌아보면서 classpath의 존재유무를 찾고
		 * 존재하는케이스는 따로 모아놓는다. 파일레벨은 워크스페이스(0레벨)-프로젝트(1레벨)로 가정하여 1레벨까지만 이동한다.
		 */

		// 원하는 파일을 찾은경우 리스트에 추가하고 종료

		if (file.isFile()) {

			// if(this.filter!=null)
			// {
			// if(this.filter.test(file))
			// {
			// return;
			// }
			// }

			if (fileExtensions != null && fileExtensions.length != 0) {
				for (String extension : fileExtensions) {
					if (file.getName().endsWith(extension)) {
						if (collectFilter.test(file))
							findFiles.add(func.apply(file));
						break;
					}
				}
			}

			else {
				if (collectFilter.test(file))
					findFiles.add(func.apply(file));
			}
			return;
		}

		/*
		 * maxlevel보다 높은경우 탐색 중단. -1일경우 파일의 끝까지 탐색
		 */
		if (maxLevel != -1) {
			if (maxLevel < currentLevel) {
				// 디렉토리인경우만 출력
				if (file.isDirectory())
					LOGGER.debug("[레벨끝] 탐색 중단. " + file.toString() + " 레벨 : " + currentLevel);
				return;
			}
		}

		// 디렉토리인경우 레벨을 늘리고 재탐색
		if (file.isDirectory()) {
			File[] listFiles = file.listFiles();
			if (listFiles == null)
				return;
			for (File subFile : listFiles) {
				// 디렉토리인경우 하위 레벨 재탐색.
				recursive(findFiles, subFile, (currentLevel + 1), func);
			}
		}
	}

}
