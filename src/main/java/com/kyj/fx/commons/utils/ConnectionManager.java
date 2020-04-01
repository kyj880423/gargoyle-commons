/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2015. 10. 16.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.exceptions.GargoyleConnectionFailException;
import com.kyj.fx.commons.memory.ResourceLoader;
import com.kyj.utils.EncrypUtil;

/**
 * @author KYJ
 *
 */
abstract class ConnectionManager {
	private static Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);
	/**
	 * DB 콘넥션 객체
	 */
	private static Connection con;

	/**
	 * SHUTDOWNHOOK 등록변수
	 */
	private static boolean isRegistShutDownHook = false;

	/**
	 * 2014. 7. 2. KYJ
	 *
	 * @처리내용 : 프로그램 종료시 반드시 종료될수있도록 shutDownHook 등록
	 */
	private static void addShutDownHook() {

		RuntimeClassUtil.addShutdownHook(new Thread(new Runnable() {
			public void run() {
				if (con != null) {
					try {
						LOGGER.debug("Oracle ShutDownHook 실행.. Connection 종료시작");
						close();
						LOGGER.debug("Oracle ShutDownHook 실행.. Connection 종료완료");

					} catch (Exception e) {
						try {
							close();
						} catch (Exception e1) {
							LOGGER.error(ValueUtil.toString(e1));
						}
					}

				}
			}
		}, "DBConnShutDownHookThread"));
	}

	public static Connection getConnection(String driver, String url, String id, String password) throws Exception {
		return getConnection(driver, url, id, password, getLoginTimeout());
	}

	public static Connection getConnection(String driver, String url, String id, String password, int loginTimeoutSec) throws Exception {
		if (con != null) {
			close(con);
		}
		// if(ValueUtil.isNotEmpty(driver))
		Class.forName(driver);

		DriverManager.setLoginTimeout(loginTimeoutSec);
		// Driver d = DriverManager.getDriver(url);

		Connection connection = null;
		if (ValueUtil.isEmpty(id, password)) {
			connection = DriverManager.getConnection(url);
		} else
			connection = DriverManager.getConnection(url, id, password);

		return connection;
	}

	public static Connection getConnection(String driver, String url, Properties prop) throws Exception {
		if (con != null) {
			close(con);
		}
		Class.forName(driver);
		DriverManager.setLoginTimeout(getLoginTimeout());
		Connection connection = DriverManager.getConnection(url, prop);
		return connection;
	}

	public static Connection getConnection(String url, String id, String password) throws Exception {
		if (con != null) {
			close(con);
		}
		Class.forName(getDriver());
		DriverManager.setLoginTimeout(getLoginTimeout());
		Connection connection = DriverManager.getConnection(url, id, password);
		return connection;
	}

	/**
	 * DataSource를 초기화 시킨다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 11. 4.
	 */
	public static void cleanDataSource() {
		dataSourceCache.values().forEach(v -> {
			v.close(true);
			dataSourceCache.clear();
		});
	}

	public static DataSource getDataSource() throws Exception {
		return getDataSource(getDriver(), getUrl(), getUserId(), getPassword());
	}

	private static Map<String, DataSource> dataSourceCache = new ConcurrentHashMap<>();

	public static DataSource getDataSource(String driver, String url, String id, String pass) throws Exception {
		return getDataSource(driver, url, id, pass, null);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 4.
	 * @param driver
	 * @param url
	 * @param id
	 * @param pass
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	public static DataSource getDataSource(String driver, String url, String id, String pass, Consumer<DataSource> handler)
			throws Exception {
		final String key = String.format("%s^%s^%s^%s", driver, url, id, pass);
		DataSource dataSource = null;

		synchronized (dataSourceCache) {
			if (dataSourceCache.containsKey(key)) {
				dataSource = dataSourceCache.get(key);
				dataSource.checkAbandoned();
			}

			if (dataSource == null) {
				// 비밀번호는 입력안하는경우도 있기때문에 검증에서 제외
				/*
				 * 2016-08-10 id도 입력안하는 경우가 있음 sqlite. by kyj
				 */
				if (ValueUtil.isEmpty(driver, url)) {
					throw new GargoyleConnectionFailException("Driver or url is empty.");
				}

				dataSource = new DataSource();
				dataSource.setDriverClassName(driver);
				dataSource.setUrl(url);
				dataSource.setUsername(id);
				dataSource.setPassword(pass);
				dataSource.setDefaultAutoCommit(false);
				dataSource.setInitialSize(1);
				dataSource.setLoginTimeout(3);

				if (handler != null)
					handler.accept(dataSource);

				dataSourceCache.put(key, dataSource);

			}
		}
		return dataSource;
	}

	public static String getDriver() {
		return ResourceLoader.getInstance().get(ResourceLoader.BASE_KEY_JDBC_DRIVER);
	}

	/**
	 * 로그인 타임아웃 시간 설정 단위 초
	 *
	 * @return
	 */
	public static int getLoginTimeout() {
		return 5;
	}

	/**
	 * 쿼리 타임아웃 시간 설정 단위 초.
	 *
	 * @Date 2015. 10. 16.
	 * @return
	 * @User KYJ
	 */
	public static int getQueryTimeout() {
		return 10;
	}

	/**
	 * 설정에 저장된 사용자 ID반환
	 *
	 * @return
	 */
	public static String getUserId() {

		return ResourceLoader.getInstance().get(ResourceLoader.BASE_KEY_JDBC_ID);
	}

	/**
	 * 설정에 저장된 패스워드 반환
	 *
	 * @return
	 */
	public static String getPassword() {
		try {
			String str = ResourceLoader.getInstance().get(ResourceLoader.BASE_KEY_JDBC_PASS);
			if (ValueUtil.isEmpty(str))
				return "";
			return EncrypUtil.decryp(str);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized static Connection getConnection() throws Exception {
		if (con == null) {
			String driver = getDriver();
			String url = getUrl();
			String userId = getUserId();
			String password = getPassword();
			/* DB 로그인 타임아웃 시간 설정 SEC */
			int loginTimeout = getLoginTimeout();

			Class.forName(driver);
			DriverManager.setLoginTimeout(loginTimeout);

			con = DriverManager.getConnection(url, userId, password);

			/* 무조건 한번만 등록됨 */
			if (!isRegistShutDownHook) {
				addShutDownHook();
				isRegistShutDownHook = true;
			}

		} else if (con.isClosed()) {
			// 2016.2.12 가끔씩 드라이버를 못찾는 현상이 있어, 드라이버도 찾는 로직 추가.
			String driver = getDriver();
			String url = getUrl();
			String userId = getUserId();
			String password = getPassword();

			Class.forName(driver);
			/* DB 로그인 타임아웃 시간 설정 SEC */
			int loginTimeout = getLoginTimeout();
			DriverManager.setLoginTimeout(loginTimeout);

			con = DriverManager.getConnection(url, userId, password);

		}

		// con.setNetworkTimeout(Executors.newFixedThreadPool(3), 3000);
		return con;
	}

	/**
	 * 접속 URL
	 *
	 * @Date 2015. 10. 16.
	 * @return
	 * @User KYJ
	 */
	private static String getUrl() {
		return ResourceLoader.getInstance().get(ResourceLoader.BASE_KEY_JDBC_URL);
	}

	/**
	 * 2014. 7. 12. KYJ
	 *
	 * @throws Exception
	 * @처리내용 : 커넥션 종료
	 */
	public static void close() throws Exception {
		close(con);
	}

	/**
	 * 커넥션 종료
	 *
	 * @Date 2015. 10. 16.
	 * @param con
	 * @throws Exception
	 * @User KYJ
	 */
	public static void close(Connection con) {
		if (con != null) {
			try {
				if (!con.isClosed()) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace();
						con.close();

						LOGGER.error(e.getMessage());
					}
					con = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 커넥션 종료
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 11. 17.
	 * @param con
	 * @throws Exception
	 */
	public static void close(DataSource dataSource) {
		if (dataSourceCache.containsValue(dataSource)) {
			Optional<Entry<String, DataSource>> findFirst = dataSourceCache.entrySet().stream().filter(ent -> ent.getValue() == dataSource)
					.findFirst();
			findFirst.ifPresent(v -> dataSourceCache.remove(v.getKey()));
		}

		try {
			dataSource.close();
			LOGGER.debug("Close Database Connection Request...");
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
			dataSource.close();
		}
	}

	/**
	 * 커넥션 종료
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 11. 17.
	 * @param con
	 * @throws Exception
	 */
	public static void close(javax.sql.DataSource dataSource) {
		if (dataSource != null) {
			try {

				if (dataSourceCache.containsValue(dataSource)) {
					Optional<Entry<String, DataSource>> findFirst = dataSourceCache.entrySet().stream()
							.filter(ent -> ent.getValue() == dataSource).findFirst();
					findFirst.ifPresent(v -> dataSourceCache.remove(v.getKey()));
				}

				LOGGER.debug("Close Connection.");
				close(dataSource.getConnection());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
