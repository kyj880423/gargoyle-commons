/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.memory
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.memory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.kyj.fx.commons.utils.ValueUtil;

import kyj.Fx.dao.wizard.memory.IFileBaseConfiguration;

/**
 * 사용자 정의 속성들 Writable속성
 *
 * @author KYJ
 *
 */
/**
 * @author KYJ
 *
 */
/**
 * @author KYJ
 *
 */
public class ResourceLoader implements IFileBaseConfiguration {

	public static final String SVN_USER_ID = "svn.userId";
	public static final String SVN_USER_PASS = "svn.userPass";
	public static final String SVN_URL = "svn.url";
	public static final String SVN_PATH = "svn.path";
	public static final String SVN_PATH_WINDOW = "svn.path.window";
	public static final String SVN_REPOSITORIES = "svn.repositores";

	/* SVN 키 */
	// public static final String SVN_USER_ID = SCMKeywords.SVN_USER_ID;
	// public static final String SVN_USER_PASS = SCMKeywords.SVN_USER_PASS;
	// public static final String SVN_PATH = SCMKeywords.SVN_PATH;
	/**
	 * @최초생성일 2016. 4. 3.
	 */
	// public static final String SVN_REPOSITORIES =
	// SCMKeywords.SVN_REPOSITORIES;

	public static final String BASE_KEY_JDBC_DRIVER = "jdbc.driver";
	public static final String BASE_KEY_JDBC_URL = "jdbc.url";
	public static final String BASE_KEY_JDBC_ID = "jdbc.id";
	public static final String BASE_KEY_JDBC_PASS = "jdbc.pass";
	public static final String BASE_KEY_JDBC_INFO = "database.info";
	public static final String BASE_KEY_IS_ENCRYP_JDBC_PASS = "jdbc.is.encryp.pass";

	/**
	 * DAO Wizard에서 사용하는 커스텀 db조회 세팅.
	 * 
	 * @최초생성일 2017. 11. 21.
	 */
	public static final String CUSTOM_DAOWIZARD_KEY_JDBC_DRIVER = "custom.daowizard.key.jdbc.driver";
	public static final String CUSTOM_DAOWIZARD_KEY_JDBC_URL = "custom.daowizard.key.jdbc.url";
	public static final String CUSTOM_DAOWIZARD_KEY_JDBC_PASS = "custom.daowizard.key.jdbc.pass";
	public static final String CUSTOM_DAOWIZARD_KEY_JDBC_ID = "custom.daowizard.key.jdbc.id";

	/**
	 * DAO WIWZARD에서 데이터를 조회하기 위한 별도의 데이터베이스 선택시 사용.
	 * @최초생성일 2019. 3. 27.
	 */
	public static final String CUSTOM_DAOWIZARD_READING_DATABASE_ALIAS = "custom.daowizard.key.jdbc.reading.database.alias";
	/**
	 * 워크스페이스 정보를 관리하는 키 <br/> 
	 *  이 값으로 워크스페이스 위치를 확인.
	 *  KYJ
	 */
	public static final String BASE_DIR = "base.dir";

	/* 프록시 정보를 세팅한다. */
	public static final String HTTP_PROXY_HOST = "http.proxyHost";
	public static final String HTTP_PROXY_PORT = "http.proxyPort";
	public static final String HTTPS_PROXY_HOST = "https.proxyHost";
	public static final String HTTPS_PROXY_PORT = "https.proxyPort";
	public static final String DBMS_SUPPORT = "dbms.support";

	/* Supported Databases */
	public static final String DBMS_SUPPORT_ORACLE = "Oracle";
	public static final String DBMS_SUPPORT_POSTGRE = "Postgre";
	public static final String DBMS_SUPPORT_MY_SQL = "Mysql";
	public static final String DBMS_SUPPORT_H2 = "H2";
	public static final String DBMS_SUPPORT_Sqlite = "Sqlite";
	public static final String DBMS_SUPPORT_DERBY = "Derby";
	/* 17.08.30 ADD */
	public static final String DBMS_SUPPORT_MS_SQL = "Mssql";

	/* Database Driver */
	public static final String ORG_MARIADB_JDBC_DRIVER = "org.mariadb.jdbc.Driver";
	public static final String ORG_POSTGRESQL_DRIVER = "org.postgresql.Driver";
	public static final String ORACLE_JDBC_DRIVER_ORACLEDRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String ORG_H2_DRIVER = "org.h2.Driver";
	public static final String ORG_SQLITE_JDBC = "org.sqlite.JDBC";
	public static final String ORG_APACHE_DERBY_JDBC = "org.apache.derby.jdbc.ClientDriver";
	public static final String ORG_MSSQL_JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	public static final String START_URL = "start.url";

	/**
	 * 씬빌더 위치정보
	 *
	 * @최초생성일 2016. 6. 19.
	 */
	public static final String SCENEBUILDER_LOCATION = "scenebuilder.location";

	/**
	 * 사용자가 파일트리에서 선택한 경로 정보
	 *
	 * @최초생성일 2015. 10. 15.
	 */
	public static final String USER_SELECT_LOCATION_PATH = "user.select.location.path";

	private Properties dbProperties;
	private static ResourceLoader loader;

	public static final String FILE_NAME = "UserConf.properties";
	public static final String DATABASE_COLUMN_ORDER = "database.column.order";
	public static final String DBMS = "dbms";

	/**
	 * 컬럼 크기가 큰 경우 데이터 맵핑을 생략할건지 유무
	 *
	 * @최초생성일 2016. 2. 11.
	 */
	public static final String SKIP_BIG_DATA_COLUMN = "skip.big.data.column";
	/**
	 * 쿼리결과에 대해 MAX로우 처리를 할지
	 *
	 * @최초생성일 2016. 2. 11.
	 */
	public static final String APPLY_MAX_ROW_COUNT = "apply.max.row.count";

	/**
	 * PMD에서 유저가 선택한 상태 값 배열
	 *
	 * @최초생성일 2016. 10. 14.
	 */
	public static final String PMD_SELECTED_PRIORITY_VALUES = "pmd.selected.priority.values";

	public static final String MS_WORD_PATH = "msword.path";

	/**
	 * 로그뷰 화면에서 사용되는 텍스트 인코딩
	 * 
	 * @최초생성일 2017. 1. 12.
	 */
	public static final String LOGVIEW_ENCODING = "logview.encoding";

	/**
	 * 사용자가 마지막으로 설정했던 유튜브 다운로드 위치
	 * 
	 * @최초생성일 2017. 5. 26.
	 */
	public static final String UTUBE_LAST_DOUWNLOAD_LOCATION = "utube.last.download.location";

	/**
	 * postgre pgadmin 설치 경로
	 * 
	 * @최초생성일 2017. 6. 13.
	 */
	public static final String POSTGRE_PGADMIN_BASE_DIR = "postgre.pgadmin.base.dir";

	/* 다국어 관련 프로퍼티 */
	/**
	 * 프로그램 시작시 다국어 정보를 요청할지 여부
	 * 
	 * @최초생성일 2017. 9. 17.
	 */
	public static final String LANGUAGE_RELOAD_ON_STARTUP_YN = "language.reload.on.startup.yn";
	/**
	 * 다국어 관련정보를 요청할 url 주소
	 * 
	 * @최초생성일 2017. 9. 17.
	 */
	public static final String LANGUAGE_REQUEST_URL = "language.request.url";
	/**
	 * 다국어 코드
	 * 
	 * @최초생성일 2017. 9. 17.
	 */
	public static final String LANGUAGE_CODE = "lang.code";
	/**
	 * 다국어 파일이 저장되는 위치
	 * 
	 * @최초생성일 2017. 9. 17.
	 */
	public static final String LANGUAGE_STORED_PROPERTIES_LOCATION = "language.store.properties.location";

	/**
	 * Send Mail 속성 키
	 * 
	 * @최초생성일 2017. 10. 18.
	 */
	public static final String SENDMAIL_CUSTOM_ACCOUNT_USE_YN = "sendmail.custom.account.use.yn";
	public static final String SENDMAIL_CUSTOM_USER_ID = "sendmail.custom.user.id";
	public static final String SENDMAIL_CUSTOM_USER_PASSWORD = "sendmail.custom.user.password";

	public static final String SENDMAIL_CUSTOM_USER_HOST = "sendmail.custom.user.host";
	public static final String SENDMAIL_CUSTOM_USER_PORT = "sendmail.custom.user.port";
	// SMPT OR POP3 . default SMTP
	public static final String SENDMAIL_CUSTOM_USER_PROTOCOL_TYPE = "sendmail.custom.user.protocol.type";

	public static final String VOEDITOR_SUFFIX_NAME = "voeditor.suffix.name";
	public static final String VOEDITOR_PREFFIX_NAME = "voeditor.preffix.name";

	/**
	 * Behavior 기본 경로
	 * 
	 * @최초생성일 2018. 2. 19.
	 */
	public static final String BEHAVIOR_BASE_DIR = "behavior.base.dir";

	private String[] baseKeys = { BASE_KEY_JDBC_INFO, BASE_KEY_JDBC_DRIVER, BASE_KEY_JDBC_URL, BASE_KEY_JDBC_ID, BASE_KEY_JDBC_PASS,
			SKIP_BIG_DATA_COLUMN, APPLY_MAX_ROW_COUNT, SVN_REPOSITORIES,
			/* 17.11.22 */
			VOEDITOR_SUFFIX_NAME, VOEDITOR_PREFFIX_NAME,
			/* 18.02.19 */
			BEHAVIOR_BASE_DIR };

	public static synchronized ResourceLoader getInstance() {
		if (loader == null) {
			loader = new ResourceLoader();
		}
		return loader;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 28.
	 * @return
	 */
	public static ResourceLoader createInstance() {
		return new ResourceLoader();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 22.
	 * @return
	 */
	public static ResourceLoader createInstance(Properties properties) {
		return new ResourceLoader(properties);
	}

	private ResourceLoader() {
		dbProperties = ResourceLoaderDbProperties.getInstance();
		initialize();
	}

	private ResourceLoader(Properties properties) {
		this.dbProperties = properties;
		initialize();
	}

	public boolean reload() {

		if (dbProperties instanceof ResourceLoaderDbProperties) {
			try {
				dbProperties.clear();
				dbProperties.load((InputStream) null);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private void initialize() {

		if (dbProperties instanceof ResourceLoaderDbProperties) {
			try {
				dbProperties.load((InputStream) null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 기본키값이 존재하지않으면 로드
	 *
	 * @Date 2015. 10. 15.
	 * @param properties
	 * @User KYJ
	 */
	private void baseKeyLoad(Properties properties) {
		for (String key : baseKeys) {
			if (properties.containsKey(key))
				continue;
			if (APPLY_MAX_ROW_COUNT.equals(key) || SKIP_BIG_DATA_COLUMN.equals(key)) {
				properties.put(key, "true");
			} else if (VOEDITOR_SUFFIX_NAME.equals(key)) {
				properties.put(key, "DVO");
			}
			properties.put(key, "");
		}

		for (String key : properties.keySet().toArray(new String[0])) {
			if (key.startsWith("database.info.") && !properties.containsKey(key)) {
				properties.put(key, "");
			}
		}
	}

	public void initDataBaseInfo() {
		for (String key : dbProperties.keySet().toArray(new String[0])) {
			if (key.startsWith("database.info.")) {
				dbProperties.remove(key);
			}
		}
	}

	public synchronized void putAll(Map<String, Object> bufMap) {
		dbProperties.putAll(bufMap);
		store();
	}

	public synchronized void putAll(Properties prop) {
		prop.entrySet().stream().forEach(e -> {
			dbProperties.put(e.getKey(), e.getValue());
		});

		store();
	}

	public synchronized void put(String key, String value) {
		dbProperties.put(key, value);
		store();
	}

	/**
	 * 파일에 적용처리
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 11. 4.
	 */
	private void store() {
		
//		 FileOutputStream out = null;
//		 try {
//			 out = new FileOutputStream(FILE_NAME);
//			 dbProperties.store(out, "User Conf...");
//		 } catch (Exception e) {
//			 e.printStackTrace();
//		 } finally {
//			 try {
//			 if (out != null)
//				 out.close();
//			 } catch (IOException e) {
//				 e.printStackTrace();
//			 }
//		 }
		 
		try {
			dbProperties.store((Writer) null, "User Conf...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String get(String key, String defaultVal) {
		String val = get(key);
		if (ValueUtil.isEmpty(val))
			return defaultVal;
		return val;
	}

	public String get(String key) {

		if (dbProperties instanceof ResourceLoaderDbProperties) {
			String property = dbProperties.getProperty(key);
			if (property == null || property.isEmpty())
				return ConfigResourceLoader.getInstance().get(key);
			return property;
		}

		return dbProperties.getProperty(key);
	}

	public String get(String key, int roopCount) {
		roopCount++;
		String property = dbProperties.getProperty(key);
		if (property == null || property.isEmpty())
			if (dbProperties instanceof ResourceLoaderDbProperties)
				return ConfigResourceLoader.getInstance().get(key, roopCount);
		return property;
	}

	/**
	 * 키에 해당하는 값을 deliminater로 분리시킨후 리턴한다..
	 *
	 * @param key
	 * @param deliminater
	 * @return
	 */
	public List<String> getValues(String key, String deliminater) {
		Object object = dbProperties.get(key);
		if (object != null)
			return Stream.of(object.toString().split(deliminater)).map(str -> str.trim()).collect(Collectors.toList());
		return Collections.emptyList();
	}

	public Enumeration<Object> keySet() {
		return dbProperties.keys();
	}

	public Set<Entry<Object, Object>> getEntry() {
		return dbProperties.entrySet();
	}

	// public Map<String, Object> toMap() {
	// return new HashedMap(properties);
	// }

	public String getFileName() {
		return FILE_NAME;
	}

	/**
	 * 환경변수에 캐릭터셋 저장
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 1. 24.
	 * @param charset
	 */
	public static void saveCharset(String charset) {
		CharsetManagement.saveCharset(charset);
	}

	/**
	 * 환경변수에 저장된 캐릭터셋 로드
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 1. 24.
	 * @return
	 */
	public static String loadCharset() {
		return CharsetManagement.loadCharset();
	}

	/**
	 * 환경변수 캐릭터셋 관리.
	 * 
	 * @author KYJ
	 *
	 */
	private static class CharsetManagement {

		/**
		 * 저장
		 * 
		 * @작성자 : KYJ
		 * @작성일 : 2017. 1. 12.
		 * @param charset
		 */
		public static void saveCharset(String charset) {
			getInstance().put(ResourceLoader.LOGVIEW_ENCODING, charset);
		}

		/**
		 * 로드
		 * 
		 * @작성자 : KYJ
		 * @작성일 : 2017. 1. 12.
		 * @return
		 */
		public static String loadCharset() {
			return getInstance().get(ResourceLoader.LOGVIEW_ENCODING, "UTF-8");
		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 2. 20.
	 * @param key
	 * @return
	 * @throws ParseException
	 */
	public JSONObject getJsonObject(String key) throws ParseException {
		return getJsonObject(key, new JSONObject());
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 2. 20.
	 * @param string
	 * @return
	 * @throws ParseException
	 */
	public JSONObject getJsonObject(String key, JSONObject defaultVal) throws ParseException {
		String string = get(key);
		if (ValueUtil.isEmpty(string))
			return defaultVal;
		return (JSONObject) new JSONParser().parse(string);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 2. 20.
	 * @param key
	 * @return
	 * @throws ParseException
	 */
	public JSONArray getJsonArray(String key) throws ParseException {
		return getJsonArray(key, new JSONArray());
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 2. 20.
	 * @param string
	 * @return
	 * @throws ParseException
	 */
	public JSONArray getJsonArray(String key, JSONArray defaultVal) throws ParseException {
		String string = get(key);
		return (JSONArray) new JSONParser().parse(string);
	}

	public Set<Entry<Object, Object>> entrySet() {
		return dbProperties.entrySet();
	}

	/**
	 * 데이터 삭제.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 28.
	 * @param keys
	 */
	public void clearKeys(List<String> keys) {
		if (dbProperties instanceof ResourceLoaderDbProperties) {
			((ResourceLoaderDbProperties) dbProperties).clearKeys(keys);
		} else {
			for (String key : keys)
				dbProperties.remove(key);
		}

		//
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 17.
	 * @param keys
	 * @return
	 */
//	public int deleteAllKey(List<String> keys) {
//		if (dbProperties instanceof ResourceLoaderDbProperties) {
//			return ((ResourceLoaderDbProperties) dbProperties).deleteAllKey(keys);
//		} else {
//			for (String key : keys)
//				dbProperties.remove(key);
//		}
//		store();
//		return -1;
//	}
}
