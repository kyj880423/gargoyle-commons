/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.memory
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.memory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.FileUtil;
import com.kyj.fx.commons.utils.ValueUtil;

import kyj.Fx.dao.wizard.memory.IFileBaseConfiguration;

/**
 * config.properties파일의 내용을 읽어들여온다.
 *
 * 프로그램이 가동되기 위한 환경정보들을 관리한다. ReadOnly속성들
 *
 * @author KYJ
 *
 */
public final class ConfigResourceLoader implements IFileBaseConfiguration {

	private static Logger LOGGER = LoggerFactory.getLogger(ConfigResourceLoader.class);
	/**
	 * 프로퍼티값이 파일을 로드하라는 의미를 갖음
	 *
	 * @최초생성일 2016. 1. 22.
	 */
	private static final String $FILE = "$file";

	/**
	 * 자바 파일을 로드하라는 의미를 갖음
	 *
	 * @최초생성일 2016. 1. 22.
	 */
	@Deprecated
	private static final String $JAVA = "$java";

	/**
	 * 사용자가 파일트리에서 선택한 경로 정보
	 *
	 * @최초생성일 2015. 10. 15.
	 */
	public static final String VOEDITOR_DEFAULT_TYPE_NAME = "voeditor.default.type.name";

	/**
	 * DVO가 갖는 기본적인 상속 클래스
	 *
	 * @최초생성일 2015. 10. 15.
	 */
	public static final String VOEDITOR_DEFAULT_EXTENDS_CLASS = "voeditor.default.extends.class";

	/**
	 * DAO가 갖는 기본적인 상속 클래스
	 *
	 * @최초생성일 2015. 10. 15.
	 */
	public static final String DAO_WIZARD_DEFAULT_EXTENDS_CLASS = "daowizard.default.extends.class";

	/**
	 * 테이블 컬럼조회 SQL 반환 KYJ
	 */
	public static final String SQL_COLUMN = "sql.columns.{driver}";
	/**
	 * 테이블 목록조회 SQL 반환 KYJ
	 */
	public static final String SQL_TABLES = "sql.tables.{driver}";

	/**
	 * 모든 테이블 조회 SQL 반환 KYJ
	 */
	public static final String ALL_TABLES = "all.tables.{driver}";

	/**
	 * 뷰 목록 SQL 반환 KYJ
	 */
	public static final String SQL_VIEWS = "sql.views.{driver}";

	/**
	 * 핑테스트 SQL 반환
	 */
	public static final String SQL_PING = "sql.ping.{driver}";

	/**
	 * SQL 전체를 조회하는 용도가 아닌 특정개수만큼만 조회처리하기 위한 SQL문을 반환한다.
	 *
	 * @최초생성일 2015. 10. 21.
	 */
	public static final String SQL_LIMIT_WRAPPER = "sql.limit.wrapper.{driver}";

	public static final String SQL_TABLE_COMMENT_WRAPPER = "sql.table.comment.{driver}";
	public static final String SQL_TABLE_COLUMNS_WRAPPER = "sql.table.columns.{driver}";
	public static final String SQL_TABLE_CREATE_WRAPPER = "sql.table.create.{driver}";
	public static final String SQL_TABLE_INDEX_WRAPPER = "sql.table.index.{driver}";

	/**
	 * SQL_ROW_WRAPPER 에 사용자 SQL에 들어가는 부분을 일컫는 Velocity값
	 *
	 * @최초생성일 2015. 10. 21.
	 */
	public static final String USER_SQL = "usersql";

	private static Properties properties;
	private static ConfigResourceLoader loader;
	private static final String FILE_NAME = "META-INF/config.properties";

	public static final String USE_PROXY_YN = "use.proxy.yn";

	public static final String DBMS_SUPPORT = "dbms.support";

	public static final String DBMS_ORACLE = "dbms.Oracle";

	public static final String DBMS_H2 = "dbms.H2";

	public static final String DBMS_SQLITE = "dbms.Sqlite";

	public static final String START_ROW = "startRow";

	public static final String MAX_ROW = "maxRow";

	public static final String DML_KEYWORD = "dml.keywords";
	/**
	 * 클래스로더가 로드될때 로드하지않을 특정 디렉토리명을 기술한 키값 2016-10-24 byj use
	 * FILTER_NOT_SRCH_DIR_NAME_CLASS_TYPE or
	 * FILTER_NOT_SRCH_DIR_NAME_SOURCE_TYPE 탐색 유형을 좀 더 구체적으로 정의함, 컴파일 or 소스
	 */
	@Deprecated
	public static final String FILTER_NOT_SRCH_DIR_NAME = "filter.not.srch.dir.name";

	public static final String FILTER_NOT_SRCH_DIR_NAME_CLASS_TYPE = "filter.not.srch.dir.name.class.type";

	public static final String FILTER_NOT_SRCH_DIR_NAME_SOURCE_TYPE = "filter.not.srch.dir.name.source.type";

	public static final String VO_RESOURCE_FILTERING_NAME = "vo.resource.filtering.name";

	public static final String FILE_ENCODING = "file.encoding";
	public static final String SUN_JNU_ENCODING = "sun.jnu.encoding";
	public static final String APP_NAME = "app.name";
	public static final String GARGOYLE_APP = "gargoyleApp";

	/**
	 * 운영체제 기반 파일 오픈처리
	 * 
	 * @최초생성일 2016. 9. 2.
	 */
	public static final String FILE_OPEN_NOT_INPROCESSING_EXTENSION = "file.open.not.inprocessing.extensions";

	/**
	 * 스키마라는 개념이 없는 DBMS의 Driver들을 나열함.
	 *
	 * @최초생성일 2016. 8. 10.
	 */
	public static final String NOT_EXISTS_SCHEMA_DRIVER_NAMES = "not.exists.schema.driver.names";
	/**
	 * SVN 기본 설정 항목들이 기록된 키
	 *
	 * @최초생성일 2016. 4. 2.
	 */
	public static final String SVN_BASE_KEYS = "svn.base.keys";

	public static final String ABOUT_PAGE_URL = "about.page.url";

	public static ConfigResourceLoader getInstance() {
		if (loader == null) {
			loader = new ConfigResourceLoader();
			loader.initialize();
		}
		return loader;
	}

	private ConfigResourceLoader() {
		initialize();
	}

	private void initialize() {
		properties = new Properties();
		try {
			ClassLoader classLoader = ConfigResourceLoader.class.getClassLoader();
			properties.load(classLoader.getResourceAsStream(FILE_NAME));
		} catch (IOException e) {
			LOGGER.warn(ValueUtil.toString(e));
//			e.printStackTrace();
		} catch (NullPointerException e) {
			LOGGER.warn(ValueUtil.toString(e));
		}

	}

	public String get(String key, String driver) {
		return get(key, driver, "", null);
	}

	public String getOrDefault(String key, String defaultValue) {
		return get(key, null, defaultValue, null);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 15.
	 * @param key
	 *            환경변수 키값.
	 * @param driver
	 *            데이터베이스 관련 변수값으로 jdbc 드라이이버 클래스명.
	 * @param mapper
	 *            메모리에 저장된 값을 리턴받은후에 후처리할 특수 변수 매핑처리를 한다.
	 * @return
	 */
	public String get(String key, String driver, String defValue, Function<String, String> mapper) {
		String property = "";
		if (key.endsWith("{driver}")) {
			// String driver =
			// ResourceLoader.getInstance().get(ResourceLoader.BASE_KEY_JDBC_DRIVER);'
			String newKey = "";
			if (driver != null)
				newKey = key.replaceAll("\\{driver\\}", driver);
			property = properties.getProperty(newKey, defValue);
		} else {
			property = properties.getProperty(key, defValue);
		}

		if (isFileLoadValue(property)) {
			String filePath = getFilePathByVar(property);
			// ClassLoader.getSystemResourceAsStream(filePath);
			InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(filePath);
			try {
				property = FileUtil.readToString(is);

			} catch (Exception e) {
				LOGGER.error(ValueUtil.toString(e));
			}

		}

		/*
		 * else if (isJavaLoadValue(property)) { property =
		 * getJavaByVar(property);
		 *
		 * }
		 */

		if (mapper != null)
			property = mapper.apply(property);

		return property;

	}

	/**
	 * 변수값이 파일을 호출하는 의미를 갖는다면?
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 1. 22.
	 * @param value
	 * @return
	 */
	public boolean isFileLoadValue(String value) {
		return value != null && value.startsWith($FILE);
	}

	/**
	 * 변수값에 특정 키값의 존재를 확인하는 함수. 즉. 값에 $java가 붙어 있는지 여부를 리턴함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 15.
	 * @param value
	 * @return
	 */
	@Deprecated
	public boolean isJavaLoadValue(String value) {
		return value != null && value.startsWith($JAVA);
	}

	public String getFilePathByVar(String value) {
		final String concat = $FILE.concat("$");
		int indexOf = value.indexOf(concat);
		if (indexOf >= 0)
			return value.substring(indexOf + concat.length(), value.length());
		return "";
	}

	@Deprecated
	public String getJavaByVar(String value) {
		final String concat = $JAVA.concat("$");
		int indexOf = value.indexOf(concat);
		if (indexOf >= 0)
			return value.substring(indexOf + concat.length(), value.length());
		return "";
	}

	public String get(String key) {
		return get(key, 0);
	}

	public String get(String key, int roopCount) {
		roopCount++;

		/*
		 * 서로 의존관계에 있는 함수가 서로 맞물려 무한루프에 빠지는 케이스가 존재함. 이 케이스는 프로그램 초기 구동시에 발생하는데
		 * 무한루프에 빠지는걸 방지하기 위해 일정횟수이상에는 공백을 리턴함.
		 */
		if (roopCount >= 5)
			return "";

		String driver = ResourceLoader.getInstance().get(ResourceLoader.BASE_KEY_JDBC_DRIVER, roopCount);
		return get(key, driver);
	}

	/**
	 * 키에 해당하는 값을 deliminater로 분리시킨후 리턴한다..
	 *
	 * @param key
	 * @param deliminater
	 * @return
	 */
	public List<String> getValues(String key, String deliminater) {
		Object object = properties.get(key);
		if (object != null)
			return Stream.of(object.toString().split(deliminater)).map(str -> str.trim()).collect(Collectors.toList());
		return Collections.emptyList();
	}

	public String getFileName() {
		return FILE_NAME;
	}

	public Set<Entry<Object, Object>> getEntry() {
		return properties.entrySet();
	}

}
