/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2015. 10. 16.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.kyj.fx.commons.exceptions.GargoyleException;
import com.kyj.fx.commons.exceptions.NotSupportException;
import com.kyj.fx.commons.functions.BiTransactionScope;
import com.kyj.fx.commons.functions.DbExecListener;
import com.kyj.fx.commons.functions.FourThFunction;
import com.kyj.fx.commons.functions.MapBaseRowMapper;
import com.kyj.fx.commons.functions.ResultSetToMapConverter;
import com.kyj.fx.commons.memory.ConfigResourceLoader;
import com.kyj.fx.commons.memory.ResourceLoader;
import com.kyj.fx.commons.threads.ExecutorDemons;
import com.kyj.utils.EncrypUtil;

import javafx.util.Callback;

/**
 * jdbc
 *
 * @author KYJ
 *
 */
public class DbUtil extends ConnectionManager {
	/**
	 * 
	 * @최초생성일 2016. 8. 4.
	 */
	public static final int DEFAULT_FETCH_SIZE = 100;

	/**
	 * @최초생성일 2016. 9. 1.
	 */
	public static final int DEFAULT_LIMIT_ROW_COUNT = 1000;

	private static final Logger LOGGER = LoggerFactory.getLogger(DbUtil.class);

	/**
	 * 쿼리이벤트 수신 클래스 목록
	 *
	 * @최초생성일 2016. 2. 12.
	 */
	private static ConcurrentHashMap<String, DbExecListener> listeners = new ConcurrentHashMap<>();

	public static String[] dmlkeyword;// = { "insert", "update", "delete",
										// "create", "drop", "alter" };

	static {
		String item = ConfigResourceLoader.getInstance().get(ConfigResourceLoader.DML_KEYWORD);
		dmlkeyword = item.split(",");
	}

	static BiFunction<Connection, String, PreparedStatement> DEFAULT_PREPAREDSTATEMENT_CONVERTER = (c, sql) -> {
		try {
			return c.prepareStatement(sql);
		} catch (Exception e) {
			throw new RuntimeException(e);
			// return null;
		}
	};

	static BiFunction<Connection, String, PreparedStatement> READ_ONLY_CURSOR_PREPAREDSTATEMENT_CONVERTER = (c, sql) -> {
		try {
			return c.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
			return null;
		}
	};

	/**
	 * 쿼리가 수행되면 메세지를 보낸다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 2. 4.
	 * @param query
	 */
	public static void noticeQuery(String query) {

		// 성능 차이로 인한 극복이 필요한경우에는 아래 주석을 해제하고 사용할것.
		// listeners.values().parallelStream().forEach(listener ->
		// listener.onQuertying(query));

		listeners.values().stream().forEach(listener -> listener.onQuerying(query));
	}

	/**
	 * 쿼리 수행 이벤트 수신을 등록한다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 2. 12.
	 * @param listener
	 */
	public static void registQuertyListener(DbExecListener listener) {
		String canonicalName = listener.getClass().getCanonicalName();
		if (!listeners.containsKey(canonicalName))
			listeners.put(canonicalName, listener);
	}

	public static List<Map<String, Object>> select(final String sql) throws Exception {
		try (Connection con = getConnection()) {
			return select(con, sql);
		}
	}

	public static <T> List<Map<String, T>> select(final String sql, int fetchCount, int limitSize,
			BiFunction<ResultSetMetaData, ResultSet, List<Map<String, T>>> convert) throws Exception {
		try (Connection con = getConnection()) {
			return select(con, sql, fetchCount, limitSize, convert);
		}
	}

	public static List<Map<String, Object>> select(final String sql,
			BiFunction<ResultSetMetaData, ResultSet, List<Map<String, Object>>> convert) throws Exception {
		try (Connection con = getConnection()) {
			return select(con, sql, convert);
		}

	}

	public static List<Map<String, Object>> select(Connection con, final String sql) throws Exception {
		return select(con, sql, 10);
	}

	public static List<Map<String, Object>> select(final Connection con, final String sql,
			BiFunction<ResultSetMetaData, ResultSet, List<Map<String, Object>>> convert) throws Exception {
		return select(con, sql, 10, -1, convert);
	}

	public static List<Map<String, Object>> selectLimit(final String sql) throws Exception {
		try (Connection con = getConnection()) {
			return selectLimit(con, sql, 10, DEFAULT_FETCH_SIZE);
		}

	}

	public static List<Map<String, Object>> selectLimit(final String sql, int limitSize) throws Exception {
		try (Connection con = getConnection()) {
			return selectLimit(con, sql, 10, limitSize);
		}

	}

	public static List<Map<String, Object>> selectLimit(Connection con, final String sql, int fetchCount, int limitSize) throws Exception {
		return select(con, sql, fetchCount, limitSize, new ResultSetToMapConverter());
	}

	/**
	 * SQL을 실행하고 결과를 반환
	 *
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> select(final Connection con, final String sql, int fetchCount) throws Exception {
		return select(con, sql, fetchCount, -1, new ResultSetToMapConverter());
	}

	public static Map<String, Object> findOne(final Connection con, final String sql) throws Exception {
		List<Map<String, Object>> select = select(con, sql, 1, 1, new ResultSetToMapConverter());
		return select.isEmpty() ? Collections.emptyMap() : select.get(0);
	}

	public static <T> T findOne(Connection con, String sql, Class<T> clazz) throws Exception {
		List<T> select = selectBeans(con, sql, 1, 1, createBeanRowMapper(clazz));
		return select.isEmpty() ? null : select.get(0);
	}

	public static List<Map<String, Object>> selectCursor(final Connection con, final String sql, int startRow) throws Exception {
		return selectCursor(con, sql, startRow, -1);
	}

	public static List<Map<String, Object>> selectCursor(final Connection con, final String sql, int startRow, int limitRow)
			throws Exception {
		Properties properties = new Properties();
		properties.put(ResultSetToMapConverter.START_ROW, --startRow);
		return select(con, sql, DEFAULT_FETCH_SIZE, limitRow, READ_ONLY_CURSOR_PREPAREDSTATEMENT_CONVERTER,
				new ResultSetToMapConverter(properties));
	}

	public static <T> List<Map<String, T>> select(final Connection con, final String sql, int fetchCount,
			BiFunction<ResultSetMetaData, ResultSet, List<Map<String, T>>> convert) throws Exception {
		return select(con, sql, fetchCount, -1, DEFAULT_PREPAREDSTATEMENT_CONVERTER, convert);
	}

	public static <T> List<Map<String, T>> select(final Connection con, final String sql, int fetchCount, int limitedSize,
			BiFunction<ResultSetMetaData, ResultSet, List<Map<String, T>>> convert) throws Exception {
		return select(con, sql, fetchCount, limitedSize, DEFAULT_PREPAREDSTATEMENT_CONVERTER, convert);
	}

	public static <T> List<Map<String, T>> select(final Connection con, final String sql, int fetchCount, int limitedSize,
			BiFunction<Connection, String, PreparedStatement> prestatementConvert,
			BiFunction<ResultSetMetaData, ResultSet, List<Map<String, T>>> convert) throws Exception {
		List<Map<String, T>> arrayList = Collections.emptyList();

		try {

			noticeQuery(sql);

			PreparedStatement prepareStatement = null;
			ResultSet executeQuery = null;

			/* 쿼리 타임아웃 시간 설정 SEC */
			// int queryTimeout = getQueryTimeout();

			prepareStatement = prestatementConvert.apply(con, sql); // con.prepareStatement(sql);
			// postgre-sql can't
			// prepareStatement.setQueryTimeout(queryTimeout);
			if (prepareStatement != null) {
				if (!(limitedSize <= 0)) {
					prepareStatement.setMaxRows(limitedSize);
				}

				if (fetchCount > 0) {
					prepareStatement.setFetchSize(fetchCount);
				}
				executeQuery = prepareStatement.executeQuery();

				ResultSetMetaData metaData = executeQuery.getMetaData();

				arrayList = convert.apply(metaData, executeQuery);
			}

		} catch (Throwable e) {
			throw e;
		}
		// finally {
		// close();
		// }

		return arrayList;
	}

	public static <T> List<T> selectBeans(final Connection con, String sql, final Class<T> bean) throws Exception {
		RowMapper<T> createBeanRowMapper = createBeanRowMapper(bean);
		return selectBeans(con, sql, 30, -1, createBeanRowMapper);
	}

	public static <T> List<T> selectBeans(final Connection con, String sql, int fetchCount, final Class<T> bean) throws Exception {
		RowMapper<T> createBeanRowMapper = createBeanRowMapper(bean);
		return selectBeans(con, sql, fetchCount, -1, createBeanRowMapper);
	}

	public static <T> List<T> selectBeans(final Connection con, final String sql, int fetchCount, RowMapper<T> mapper) throws Exception {
		return selectBeans(con, sql, fetchCount, -1, mapper);
	}

	public static <T> List<T> selectBeans(final Connection con, final String sql, int fetchCount, int limitedSize, RowMapper<T> mapper)
			throws Exception {
		List<T> resultList = Collections.emptyList();

		try {
			noticeQuery(sql);

			PreparedStatement prepareStatement = null;
			ResultSet executeQuery = null;

			/* 쿼리 타임아웃 시간 설정 SEC */
			// int queryTimeout = getQueryTimeout();

			prepareStatement = con.prepareStatement(sql);
			LOGGER.debug(sql);
			// postgre-sql can't
			// prepareStatement.setQueryTimeout(queryTimeout);
			if (!(limitedSize <= 0)) {
				prepareStatement.setMaxRows(limitedSize);
			}

			if (fetchCount > 0) {
				prepareStatement.setFetchSize(fetchCount);
			}
			executeQuery = prepareStatement.executeQuery();

			RowMapperResultSetExtractor<T> rowMapperResultSetExtractor = new RowMapperResultSetExtractor<T>(mapper);
			resultList = rowMapperResultSetExtractor.extractData(executeQuery);
		} catch (Throwable e) {
			throw e;
		} finally {
			close(con);
		}
		return resultList;
	}

	/**
	 * SQL을 실행하고 결과를 반환
	 *
	 * @param sql
	 * @return
	 * @throws Exception
	 */

	public static <T> List<T> select(final String sql, Map<String, Object> paramMap, RowMapper<T> rowMapper) throws Exception {
		return select(getDataSource(), sql, paramMap, rowMapper);
	}

	/**
	 * SQL을 실행하고 결과를 반환
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 21.
	 * @param sql
	 * @param mapSqlParameterSource
	 * @param rowMapper
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> selectLimit(String sql, MapSqlParameterSource mapSqlParameterSource,
			RowMapper<Map<String, Object>> rowMapper, int limit) throws Exception {
		return selectLimit(getDataSource(), sql, mapSqlParameterSource, rowMapper, limit);
	}

	public static <T> List<T> select(DataSource dataSource, final String sql, Map<String, Object> paramMap, RowMapper<T> rowMapper)
			throws Exception {
		return select(dataSource, sql, new MapSqlParameterSource(paramMap), rowMapper);
	}

	/**
	 * SQL을 실행하고 결과를 반환
	 *
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> select(final String sql, MapSqlParameterSource paramMap, RowMapper<T> rowMapper) throws Exception {
		return select(getDataSource(), sql, paramMap, rowMapper);
	}

	/**
	 * SQL을 실행하고 결과를 반환
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 21.
	 * @param dataSource
	 * @param sql
	 * @param paramMap
	 * @param rowMapper
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> select(DataSource dataSource, final String sql, MapSqlParameterSource paramMap, RowMapper<T> rowMapper)
			throws Exception {
		return selectLimit(dataSource, sql, paramMap, rowMapper, -1);
	}

	public static String selectScala(DataSource dataSource, final String sql, Map<String, Object> paramMap) {
		return selectScala(dataSource, sql, new MapSqlParameterSource(paramMap));
	}

	/**
	 * 첫번쨰로우의 첫번쨰 컬럼값 리턴. <br/>
	 * 값이 없는경우 NULL <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 27.
	 * @param dataSource
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public static String selectScala(DataSource dataSource, final String sql, MapSqlParameterSource paramMap) {
		String r = null;
		try {
			noticeQuery(sql);
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
			//
			// jdbcTemplate.getJdbcOperations().execute(new
			// PreparedStatementCreator() {
			//
			// @Override
			// public PreparedStatement createPreparedStatement(Connection con)
			// throws SQLException {
			// // TODO Auto-generated method stub
			// return null;
			// }
			// }, new PreparedStatementCallback<String>() {
			//
			// @Override
			// public String doInPreparedStatement(PreparedStatement ps) throws
			// SQLException, DataAccessException {
			// // TODO Auto-generated method stub
			// return null;
			// }
			// });
			ResultSetExtractor<String> extr = new ResultSetExtractor<String>() {

				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						return rs.getString(1);
					}
					return null;
				}
			};
			r = jdbcTemplate.query(sql, paramMap, extr);

		} catch (Exception e) {
			throw e;
		}
		// finally {
		// close(dataSource);
		// }
		return r;
	}

	public static String selectScala(Connection dataSource, final String sql) throws Exception {
		String r = null;
		try {
			noticeQuery(sql);

			PreparedStatement prepareStatement = dataSource.prepareStatement(sql);
			ResultSet executeQuery = prepareStatement.executeQuery();

			if (executeQuery.next()) {
				return executeQuery.getString(1);
			}

		} catch (Exception e) {
			throw e;
		}
		// finally {
		// close(dataSource);
		// }
		return r;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 21.
	 * @param dataSource
	 * @param sql
	 * @param paramMap
	 * @param rowMapper
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> selectLimit(DataSource dataSource, final String sql, MapSqlParameterSource paramMap, RowMapper<T> rowMapper,
			final int limit) throws Exception {
		List<T> query = null;
		try {
			noticeQuery(sql);
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

			// String _sql = ValueUtil.getVelocityToText(sql, paramMap);
			// LOGGER.debug(_sql);

			ResultSetExtractor<List<T>> extr = new ResultSetExtractor<List<T>>() {

				@Override
				public List<T> extractData(ResultSet rs) throws SQLException, DataAccessException {

					ArrayList<T> arrayList = new ArrayList<T>();
					int rownum = 1;

					while (rs.next()) {
						T mapRow = rowMapper.mapRow(rs, rownum);
						arrayList.add(mapRow);
						rownum++;

						if ((limit != -1) && (limit <= rownum)) {
							break;
						}

					}
					return arrayList;
				}
			};

			query = jdbcTemplate.query(sql, paramMap, extr);
		} catch (Exception e) {
			throw e;
		}
		// finally {
		//// cleanDataSource();
		//// close(dataSource);
		// }
		return query;
	}

	/**
	 * SQL을 실행하고 결과를 반환
	 *
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> select(final String sql, Map<String, Object> paramMap) throws Exception {

		List<Map<String, Object>> arrayList = null;
		Connection connection = getConnection();
		try {

			noticeQuery(sql);

			PreparedStatement prepareStatement = null;
			ResultSet executeQuery = null;

			/* 쿼리 타임아웃 시간 설정 SEC */
			int queryTimeout = getQueryTimeout();

			prepareStatement = connection.prepareStatement(sql);
			prepareStatement.setQueryTimeout(queryTimeout);

			executeQuery = prepareStatement.executeQuery();
			/*
			 * if (fetchCount > 0) { executeQuery.setFetchSize(fetchCount); }
			 */

			ResultSetMetaData metaData = executeQuery.getMetaData();
			int columnCount = metaData.getColumnCount();
			arrayList = new ArrayList<Map<String, Object>>();

			while (executeQuery.next()) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				for (int c = 1; c <= columnCount; c++) {
					map.put(metaData.getColumnLabel(c), executeQuery.getString(c));
				}
				arrayList.add(map);
			}

		} catch (Exception e) {
			LOGGER.debug(ValueUtil.toString(e));
			throw e;
		} finally {
			close(connection);
		}

		return arrayList;
	}

	public static int update(Connection con, String query) throws Exception {
		try {
			noticeQuery(query);

			PreparedStatement prepareStatement = con.prepareStatement(query);
			return prepareStatement.executeUpdate();
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
			throw e;
		}
	}

	public static int update(String query) throws Exception {
		Connection con = null;

		try {
			noticeQuery(query);

			con = getConnection();
			PreparedStatement prepareStatement = con.prepareStatement(query);
			return prepareStatement.executeUpdate();
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
			throw e;
		} finally {
			close(con);
		}
	}

	public static <T> int update(String query, Map<String, Object> map) throws Exception {

		return getTransactionedScope(null, new BiTransactionScope<T, NamedParameterJdbcTemplate>() {

			@Override
			public int scope(T t, NamedParameterJdbcTemplate u) throws Exception {

				return u.update(query, map);
			}
		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 27.
	 * @param dataSource
	 * @param query
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static <T> int update(DataSource dataSource, String query, Map<String, Object> map) throws Exception {
		return getTransactionedScope(dataSource, null, new BiTransactionScope<T, NamedParameterJdbcTemplate>() {

			@Override
			public int scope(T t, NamedParameterJdbcTemplate u) throws Exception {

				return u.update(query, map);
			}
		}, true, ExceptionHandler.getDefaultHandler());
	}

	/**
	 * API 수정.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 6.
	 * @param query
	 * @param maps
	 * @return
	 * @throws Exception
	 */
	public static <T> int updateList(String query, List<Map<String, Object>> maps) throws Exception {

		return getTransactionedScope(null, new BiTransactionScope<T, NamedParameterJdbcTemplate>() {

			@Override
			public int scope(T t, NamedParameterJdbcTemplate u) throws Exception {

				@SuppressWarnings("unchecked")
				Map<String, ?>[] array = maps.toArray(new HashMap[maps.size()]);
				noticeQuery(query);
				int[] batchUpdate = u.batchUpdate(query, array);
				return IntStream.of(batchUpdate).sum();
			}
		});

		// DataSource dataSource = null;
		// Connection connection = null;
		// try {
		//
		// noticeQuery(query);
		//
		// connection = getDataSource().getConnection();
		// connection.setAutoCommit(false);
		// NamedParameterJdbcTemplate jdbcTemplate = new
		// NamedParameterJdbcTemplate(dataSource);
		// Map<String, ?>[] array = maps.toArray(new HashMap[maps.size()]);
		// jdbcTemplate.batchUpdate(query, array);
		// connection.commit();
		// } catch (Exception e) {
		// connection.rollback();
		// e.printStackTrace();
		// } finally {
		// close(connection);
		// }

	}

	/**
	 * 트랜잭션으로 감싸진 영역을 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 29.
	 * @param consumer
	 * @throws Exception
	 */
	public static <T> int getTransactionedScope(T userObj, BiTransactionScope<T, NamedParameterJdbcTemplate> consumer) {
		return getTransactionedScope(userObj, consumer, null);
	}

	public static <T> int getTransactionedScope(T userObj, BiTransactionScope<T, NamedParameterJdbcTemplate> consumer,
			ExceptionHandler exceptionHandler) {

		DataSource defaultDataSource = null;
		try {
			defaultDataSource = getDataSource();
		} catch (Exception e) {
			if (exceptionHandler != null) {
				exceptionHandler.handle(e);
				return -1;
			} else
				LOGGER.error(ValueUtil.toString(e));
		}
		if (defaultDataSource == null) {
			RuntimeException ex = new RuntimeException("Default DataSource is null.");
			if (exceptionHandler != null) {
				exceptionHandler.handle(ex);
				return -1;
			} else
				throw ex;

		}

		return getTransactionedScope(defaultDataSource, userObj, consumer, exceptionHandler);
	}

	public static <T> int getTransactionedScope(DataSource dataSource, T userObj,
			BiTransactionScope<T, NamedParameterJdbcTemplate> consumer, ExceptionHandler exceptionHandler) {
		return getTransactionedScope(dataSource, userObj, consumer, true, exceptionHandler);
	}

	public static <T> int getTransactionedScope(DataSource dataSource, T userObj,
			BiTransactionScope<T, NamedParameterJdbcTemplate> consumer, boolean autoClose, ExceptionHandler exceptionHandler) {
		// DataSource dataSource = null;
		try {
			// dataSource = getDataSource();
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

			TransactionTemplate template = new TransactionTemplate();
			DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
			template.setTransactionManager(transactionManager);

			return template.execute(status -> {
				int result = -1;
				try {
					result = consumer.scope(userObj, namedParameterJdbcTemplate);
				} catch (Exception e) {
					status.setRollbackOnly();
					LOGGER.error(ValueUtil.toString(e));
					if (exceptionHandler != null)
						exceptionHandler.handle(e);
					result = -1;
				}
				return result;
			});

		} catch (Exception e) {
			if (exceptionHandler != null)
				exceptionHandler.handle(e);
			else
				LOGGER.error(ValueUtil.toString(e));
		} finally {
			if (autoClose) {
				try {
					close(dataSource);
				} catch (Exception e) {
					LOGGER.error(ValueUtil.toString(e));
				}
			}
		}
		return -1;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 9. 18.
	 * @param con
	 * @param userObj
	 * @param sqlConverter
	 * @return
	 * @throws Exception
	 */
	public static <T> int getTransactionedScope(Connection con, T userObj, Function<T, List<String>> sqlConverter) throws Exception {
		return getTransactionedScope(con, userObj, sqlConverter, err -> {
			throw new RuntimeException(err);
		});

	}

	public static <T> int getTransactionedScope(Connection con, T userObj, Function<T, List<String>> sqlConverter,
			Consumer<Exception> exceptionHandler) throws Exception {
		int result = -1;
		try {
			LOGGER.debug("is AutoCommit ? : {}", con.getAutoCommit());
			con.setAutoCommit(false);
			List<String> apply = sqlConverter.apply(userObj);
			Statement createStatement = con.createStatement();
			for (String sql : apply) {

				/*
				 * sqlite에서 공백이 포함된 sql은 add한경우 에러. 확인해보니 isEmpty함수에 이상이 있는듯하여
				 * 수정.
				 */
				if (ValueUtil.isEmpty(sql))
					continue;

				LOGGER.debug(sql);
				createStatement.addBatch(sql);
			}

			int[] executeBatch = createStatement.executeBatch();

			con.commit();
			result = (int) IntStream.of(executeBatch).filter(v -> v == 0).count();
		} catch (Exception e) {
			con.rollback();
			exceptionHandler.accept(e);
			result = -1;
		} finally {
			con.commit();
			close(con);
		}
		return result;
	}

	/**
	 * 쿼리가 데이터 조작 SQL문인지를 체크한다.
	 *
	 * @param query
	 * @return
	 */
	public static boolean isDml(final String query) {
		if (query != null && !query.trim().isEmpty()) {
			final String sql = getSubLowerCase(query);
			return Arrays.stream(dmlkeyword).anyMatch(keyword -> {
				return sql.startsWith(keyword);
			});
		}
		return false;
	}

	public static boolean isNotDml(final String query) {
		return !isDml(query);
	}

	/**
	 * isDml 함수에서 문자열이 너무 긴경우는 substring으로 앞부분만 잘라 확인함.
	 *
	 * @param sql
	 * @return
	 */
	private static String getSubLowerCase(String sql) {
		String lowerCase = null;
		if (sql.length() > 15)
			lowerCase = sql.trim().substring(0, 15).toLowerCase();
		else
			lowerCase = sql.toLowerCase();
		return lowerCase;
	}

	/**
	 * 커넥션의 Driver명을 리턴
	 *
	 * @param connection
	 * @return
	 * @throws SQLException
	 * @throws NotSupportException
	 */
	public static String getDriverNameByConnection(Connection connection) throws SQLException, NotSupportException {
		if (connection == null)
			throw new NullPointerException("Connection is null");

		if (connection != null) {
			Driver dbmsDriver = DriverManager.getDriver(connection.getMetaData().getURL());
			if (dbmsDriver != null) {
				Class<? extends Driver> class1 = dbmsDriver.getClass();
				return class1.getName();
			}
		}
		throw new NotSupportException("could not  found DBMS Driver! ");
	}

	/**
	 * Map에 존재하는 값들을 ''가 붙은 형태로 새로 만들어서 리턴.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 2. 15.
	 * @param param
	 * @return
	 */
	public static Map<String, Object> replaceDotMapValue(Map<String, Object> param) {
		Iterator<Entry<String, Object>> iterator = param.entrySet().iterator();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		while (iterator.hasNext()) {
			Entry<String, Object> next = iterator.next();
			Object value = null;
			if (next.getValue() != null)
				value = "'".concat(next.getValue().toString()).concat("'");
			map.put(next.getKey(), value);
		}
		return map;
	}

	/**
	 * 데이터베이스 접속 여부 확인 기본 대기시간 : 2초
	 *
	 * @param conSupplier
	 *            커넥션정보를 제공
	 * @param onSuccess
	 *            접속성공시 처리
	 * @param exHandler
	 *            에러발생시 처리
	 * @return 접속 성공여부
	 */
	public static void ping(Supplier<PoolProperties> conSupplier, Consumer<Boolean> onSuccess, Consumer<Throwable> exHandler) {
		ping(conSupplier, onSuccess, exHandler, 1, false);
	}

	public static void pingAsync(Supplier<PoolProperties> conSupplier, Consumer<Boolean> onSuccess, Consumer<Throwable> exHandler) {
		ping(conSupplier, onSuccess, exHandler, 1, true);
	}

	/**
	 * 데이터베이스 접속 여부 확인
	 *
	 * @param conSupplier
	 *            커넥션정보를 제공
	 * @param onSuccess
	 *            접속성공시 처리
	 * @param exHandler
	 *            에러발생시 처리
	 * @param timeoutSec
	 *            핑테스트 대기시간 (Secound)
	 * @return 접속 성공여부
	 */
	public static void ping(Supplier<PoolProperties> conSupplier, Consumer<Boolean> onSuccess, Consumer<Throwable> exHandler,
			int timeoutSec, boolean async) {

		// boolean result = false;
		PoolProperties prop = conSupplier.get();

		String driverClassName = prop.getDriverClassName();
		String url = prop.getUrl();
		String userName = prop.getUsername();
		String password = prop.getPassword();

		if (async) {
			ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(() -> {
				ping(onSuccess, exHandler, timeoutSec, driverClassName, url, userName, password);
			});
		} else {
			ping(onSuccess, exHandler, timeoutSec, driverClassName, url, userName, password);
		}

	}

	private static void ping(Consumer<Boolean> onSuccess, Consumer<Throwable> exHandler, int timeoutSec, String driverClassName, String url,
			String userName, String password) {
		boolean result;
		try (Connection connection = getConnection(driverClassName, url, userName, password, timeoutSec)) {

			String pingSQL = ConfigResourceLoader.getInstance().get(ConfigResourceLoader.SQL_PING, driverClassName);

			// 리스너들에게 공지
			noticeQuery(pingSQL);

			if (connection != null) {
				// connection.setAutoCommit(false);
				// try {
				// // postgresql was not supported...
				// Oracle에서 관련 에러 발생.. Connection객체를 얻어오는 과정과
				// executeQuery처리결과만으로 판단
				// connection.setNetworkTimeout(Executors.newSingleThreadExecutor(),
				// 5000);
				// } catch (Exception e) {
				//
				// }
				ResultSet executeQuery = connection.createStatement().executeQuery(pingSQL);
				result = executeQuery.next();
				onSuccess.accept(result);
			} else
				exHandler.accept(new Exception("Connection create fail."));
		} catch (Throwable e) {
			exHandler.accept(e);
		}
	}

	/********************************
	 * 작성일 : 2016. 8. 11. 작성자 : KYJ
	 *
	 *
	 * @return
	 * @throws Exception
	 ********************************/
	public static List<String> tables(String tableNamePattern) throws Exception {
		return tables(tableNamePattern, rs -> {
			try {
				return rs.getString(3);
			} catch (SQLException e) {
				LOGGER.error(ValueUtil.toString(e));
			}
			return "";
		});
	}

	/********************************
	 * 작성일 : 2016. 8. 11. 작성자 : KYJ
	 *
	 * 2016-11-10 모든 테이블탐색후 대소문자무시 검색으로 수정 </br>
	 * </br>
	 * 1.TABLE_CAT String => table catalog (may be null) </br>
	 * 2.TABLE_SCHEM String => table schema (may be null) </br>
	 * 3.TABLE_NAME String => table name </br>
	 * 4.TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW",
	 * "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS",
	 * "SYNONYM". </br>
	 * 5.REMARKS String => explanatory comment on the table </br>
	 * 6.TYPE_CAT String => the types catalog (may be null) </br>
	 * 7.TYPE_SCHEM String => the types schema (may be null) </br>
	 * 8.TYPE_NAME String => type name (may be null) </br>
	 * 9.SELF_REFERENCING_COL_NAME String => name of the designated "identifier"
	 * column of a typed table (may be null) </br>
	 * 10.REF_GENERATION String => specifies how values in
	 * SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER",
	 * "DERIVED". (may be null) </br>
	 * 
	 * @param converter
	 * @return
	 * @throws Exception
	 ********************************/
	public static <T> List<T> tables(String tableNamePattern, Function<ResultSet, T> converter) throws Exception {
		try (Connection connection = getConnection()) {
			return tables(connection, tableNamePattern, converter);
		}
	}

	/********************************
	 * 작성일 : 2016. 8. 11. 작성자 : KYJ
	 *
	 * 2016-11-10 모든 테이블탐색후 대소문자무시 검색으로 수정 2017-07-12 Connection을 파라미터로 넣어 동적으로
	 * 찾을 수 있게 수정 </br>
	 * </br>
	 * 1.TABLE_CAT String => table catalog (may be null) </br>
	 * 2.TABLE_SCHEM String => table schema (may be null) </br>
	 * 3.TABLE_NAME String => table name </br>
	 * 4.TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW",
	 * "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS",
	 * "SYNONYM". </br>
	 * 5.REMARKS String => explanatory comment on the table </br>
	 * 6.TYPE_CAT String => the types catalog (may be null) </br>
	 * 7.TYPE_SCHEM String => the types schema (may be null) </br>
	 * 8.TYPE_NAME String => type name (may be null) </br>
	 * 9.SELF_REFERENCING_COL_NAME String => name of the designated "identifier"
	 * column of a typed table (may be null) </br>
	 * 10.REF_GENERATION String => specifies how values in
	 * SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER",
	 * "DERIVED". (may be null) </br>
	 * 
	 * @param connection
	 * @param converter
	 * @return
	 * @throws Exception
	 ********************************/
	public static <T> List<T> tables(Connection connection, String tableNamePattern, Function<ResultSet, T> converter) throws Exception {
		if (converter == null)
			throw new GargoyleException(GargoyleException.ERROR_CODE.PARAMETER_EMPTY, "converter is null ");

		List<T> tables = new ArrayList<>();

		DatabaseMetaData metaData = connection.getMetaData();

		/* 17.9.19 catalog를 추가하여 조회할 수 있게 코드 수정 */

		ResultSet catalogs = metaData.getCatalogs();
		boolean existsCatalog = false;
		boolean existsSchema = false;

		while (catalogs.next()) {
			existsCatalog = true;
			String catal = catalogs.getString(1);
			ResultSet rs = metaData.getTables(catal, null,
					"%"/* + tableNamePattern + "%" */, new String[] { "TABLE" });
			String tableNamePatternUpperCase = tableNamePattern.toUpperCase();
			while (rs.next()) {

				// 2016-08-18 특정데이터베이스(sqlite)에서는 인덱스 트리거정보도 동시에 출력된다.
				String tableType = rs.getString(4);
				if ("TABLE".equals(tableType)) {

					String tableName = rs.getString(3);
					if (tableName.toUpperCase().indexOf(tableNamePatternUpperCase) != -1) {
						T apply = converter.apply(rs);
						if (apply != null)
							tables.add(apply);
					}

				}

			}
		}

		if (!existsCatalog) {
			ResultSet schemas = metaData.getSchemas();
			while (schemas.next()) {
				existsSchema = true;
				ResultSet rs = metaData.getTables(null, schemas.getString(0),
						"%"/* + tableNamePattern + "%" */, new String[] { "TABLE" });
				String tableNamePatternUpperCase = tableNamePattern.toUpperCase();
				while (rs.next()) {

					// 2016-08-18 특정데이터베이스(sqlite)에서는 인덱스 트리거정보도 동시에 출력된다.
					String tableType = rs.getString(4);
					if ("TABLE".equals(tableType)) {

						String tableName = rs.getString(3);
						if (tableName.toUpperCase().indexOf(tableNamePatternUpperCase) != -1) {
							T apply = converter.apply(rs);
							if (apply != null)
								tables.add(apply);
						}

					}
				}
			}
		}

		if (!existsCatalog && !existsSchema) {
			ResultSet rs = metaData.getTables(null, null,
					"%"/* + tableNamePattern + "%" */, new String[] { "TABLE" });
			String tableNamePatternUpperCase = tableNamePattern.toUpperCase();
			while (rs.next()) {

				// 2016-08-18 특정데이터베이스(sqlite)에서는 인덱스 트리거정보도 동시에 출력된다.
				String tableType = rs.getString(4);
				if ("TABLE".equals(tableType)) {

					String tableName = rs.getString(3);
					if (tableName.toUpperCase().indexOf(tableNamePatternUpperCase) != -1) {
						T apply = converter.apply(rs);
						if (apply != null)
							tables.add(apply);
					}

				}
			}
		}
		return tables;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 23.
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public static List<String> catalogs(Connection connection) throws Exception {
		Function<ResultSet, String> converter = rs -> {
			try {
				return rs.getString("TABLE_CAT");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		};
		return catalogs(connection, converter);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 23.
	 * @param connection
	 * @param converter
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> catalogs(Connection connection, Function<ResultSet, T> converter) throws Exception {
		if (converter == null)
			throw new GargoyleException(GargoyleException.ERROR_CODE.PARAMETER_EMPTY, "converter is null ");
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet catalogs = metaData.getCatalogs();
		List<T> arr = new ArrayList<>();
		while (catalogs.next()) {
			arr.add(converter.apply(catalogs));
		}
		return arr;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 27.
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public static List<String> functions(Connection connection) throws Exception {
		return functions(connection, "%", "%", "%");
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 27.
	 * @param connection
	 * @param catalog
	 * @param schemaPattern
	 * @param functionNamePattern
	 * @return
	 * @throws Exception
	 */
	public static List<String> functions(Connection connection, String catalog, String schemaPattern, String functionNamePattern)
			throws Exception {
		Function<ResultSet, String> converter = rs -> {
			try {
				return rs.getString("FUNCTION_NAME");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		};
		return functions(connection, catalog, schemaPattern, functionNamePattern, converter);
	}

	/**
	 * 
	 * 1.FUNCTION_CAT String => function catalog (may be null) 2.FUNCTION_SCHEM
	 * String => function schema (may be null) 3.FUNCTION_NAME String =>
	 * function name. This is the name used to invoke the function 4.REMARKS
	 * String => explanatory comment on the function 5.FUNCTION_TYPE short =>
	 * kind of function: ◦functionResultUnknown - Cannot determine if a return
	 * value or table will be returned ◦ functionNoTable- Does not return a
	 * table ◦ functionReturnsTable - Returns a table
	 * 
	 * 6.SPECIFIC_NAME String => the name which uniquely identifies this
	 * function within its schema. This is a user specified, or DBMS generated,
	 * name that may be different then the FUNCTION_NAME for example with
	 * overload functions
	 * 
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 23.
	 * @param connection
	 * @param converter
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> functions(Connection connection, String catalog, String schemaPattern, String functionNamePattern,
			Function<ResultSet, T> converter) throws Exception {
		if (converter == null)
			throw new GargoyleException(GargoyleException.ERROR_CODE.PARAMETER_EMPTY, "converter is null ");
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet catalogs = metaData.getFunctions(catalog, schemaPattern, functionNamePattern);
		List<T> arr = new ArrayList<>();
		while (catalogs.next()) {
			arr.add(converter.apply(catalogs));
		}
		return arr;
	}

	public static List<String> columns(String tableNamePattern) throws Exception {
		try (Connection con = getConnection()) {
			return columns(con, tableNamePattern, t -> {
				try {
					return t.getString(4);
				} catch (SQLException e) {
					LOGGER.error(ValueUtil.toString(e));
				}
				return "";
			});
		}
	}

	/**
	 *
	 * 1. TABLE_CAT String => table catalog (may be null) </br>
	 * 2.TABLE_SCHEM String => table schema (may be null) </br>
	 * 3.TABLE_NAME String => table name </br>
	 * 4.COLUMN_NAME String => column name </br>
	 * 5.DATA_TYPE int => SQL type from java.sql.Types </br>
	 * 6.TYPE_NAME String => Data source dependent type name, for a UDT the type
	 * name is fully qualified </br>
	 * 7.COLUMN_SIZE int => column size. </br>
	 * 8.BUFFER_LENGTH is not used. </br>
	 * 9.DECIMAL_DIGITS int => the number of fractional digits. Null is returned
	 * for data types where DECIMAL_DIGITS is not applicable. </br>
	 * 10.NUM_PREC_RADIX int => Radix (typically either 10 or 2) </br>
	 * 11.NULLABLE int => is NULL allowed. ◦ columnNoNulls - might not allow
	 * NULL values </br>
	 * ◦ columnNullable - definitely allows NULL values </br>
	 * ◦ columnNullableUnknown - nullability unknown </br>
	 * </br>
	 * 12.REMARKS String => comment describing column (may be null) </br>
	 * 13.COLUMN_DEF String => default value for the column, which should be
	 * interpreted as a string when the value is enclosed in single quotes (may
	 * be null) </br>
	 * 14.SQL_DATA_TYPE int => unused </br>
	 * 15.SQL_DATETIME_SUB int => unused </br>
	 * 16.CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in
	 * the column </br>
	 * 17.ORDINAL_POSITION int => index of column in table (starting at 1) </br>
	 * 18.IS_NULLABLE String => ISO rules are used to determine the nullability
	 * for a column. ◦ YES --- if the column can include NULLs </br>
	 * ◦ NO --- if the column cannot include NULLs </br>
	 * ◦ empty string --- if the nullability for the column is unknown </br>
	 * </br>
	 * 19.SCOPE_CATALOG String => catalog of table that is the scope of a
	 * reference attribute (null if DATA_TYPE isn't REF) </br>
	 * 20.SCOPE_SCHEMA String => schema of table that is the scope of a
	 * reference attribute (null if the DATA_TYPE isn't REF) </br>
	 * 21.SCOPE_TABLE String => table name that this the scope of a reference
	 * attribute (null if the DATA_TYPE isn't REF) </br>
	 * 22.SOURCE_DATA_TYPE short => source type of a distinct type or
	 * user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE
	 * isn't DISTINCT or user-generated REF) </br>
	 * 23.IS_AUTOINCREMENT String => Indicates whether this column is auto
	 * incremented ◦ YES --- if the column is auto incremented </br>
	 * ◦ NO --- if the column is not auto incremented </br>
	 * ◦ empty string --- if it cannot be determined whether the column is auto
	 * incremented </br>
	 * </br>
	 * 24.IS_GENERATEDCOLUMN String => Indicates whether this is a generated
	 * column ◦ YES --- if this a generated column </br>
	 * ◦ NO --- if this not a generated column </br>
	 * ◦ empty string --- if it cannot be determined whether this is a generated
	 * column </br>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 10.
	 * @param tableNamePattern
	 * @param converter
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> columns(String tableNamePattern, Function<ResultSet, T> converter) throws Exception {

		try (Connection con = getConnection()) {
			return columns(con, tableNamePattern, converter);
		}
	}

	/**
	 * 1. TABLE_CAT String => table catalog (may be null) </br>
	 * 2.TABLE_SCHEM String => table schema (may be null) </br>
	 * 3.TABLE_NAME String => table name </br>
	 * 4.COLUMN_NAME String => column name </br>
	 * 5.DATA_TYPE int => SQL type from java.sql.Types </br>
	 * 6.TYPE_NAME String => Data source dependent type name, for a UDT the type
	 * name is fully qualified </br>
	 * 7.COLUMN_SIZE int => column size. </br>
	 * 8.BUFFER_LENGTH is not used. </br>
	 * 9.DECIMAL_DIGITS int => the number of fractional digits. Null is returned
	 * for data types where DECIMAL_DIGITS is not applicable. </br>
	 * 10.NUM_PREC_RADIX int => Radix (typically either 10 or 2) </br>
	 * 11.NULLABLE int => is NULL allowed. ◦ columnNoNulls - might not allow
	 * NULL values </br>
	 * ◦ columnNullable - definitely allows NULL values </br>
	 * ◦ columnNullableUnknown - nullability unknown </br>
	 * </br>
	 * 12.REMARKS String => comment describing column (may be null) </br>
	 * 13.COLUMN_DEF String => default value for the column, which should be
	 * interpreted as a string when the value is enclosed in single quotes (may
	 * be null) </br>
	 * 14.SQL_DATA_TYPE int => unused </br>
	 * 15.SQL_DATETIME_SUB int => unused </br>
	 * 16.CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in
	 * the column </br>
	 * 17.ORDINAL_POSITION int => index of column in table (starting at 1) </br>
	 * 18.IS_NULLABLE String => ISO rules are used to determine the nullability
	 * for a column. ◦ YES --- if the column can include NULLs </br>
	 * ◦ NO --- if the column cannot include NULLs </br>
	 * ◦ empty string --- if the nullability for the column is unknown </br>
	 * </br>
	 * 19.SCOPE_CATALOG String => catalog of table that is the scope of a
	 * reference attribute (null if DATA_TYPE isn't REF) </br>
	 * 20.SCOPE_SCHEMA String => schema of table that is the scope of a
	 * reference attribute (null if the DATA_TYPE isn't REF) </br>
	 * 21.SCOPE_TABLE String => table name that this the scope of a reference
	 * attribute (null if the DATA_TYPE isn't REF) </br>
	 * 22.SOURCE_DATA_TYPE short => source type of a distinct type or
	 * user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE
	 * isn't DISTINCT or user-generated REF) </br>
	 * 23.IS_AUTOINCREMENT String => Indicates whether this column is auto
	 * incremented ◦ YES --- if the column is auto incremented </br>
	 * ◦ NO --- if the column is not auto incremented </br>
	 * ◦ empty string --- if it cannot be determined whether the column is auto
	 * incremented </br>
	 * </br>
	 * 24.IS_GENERATEDCOLUMN String => Indicates whether this is a generated
	 * column ◦ YES --- if this a generated column </br>
	 * ◦ NO --- if this not a generated column </br>
	 * ◦ empty string --- if it cannot be determined whether this is a generated
	 * column </br>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 10.
	 * @param con
	 * @param tableNamePattern
	 * @return
	 * @throws Exception
	 * @Deprecated catalog, schema 파라미터가 존재하는 API 사용할것.
	 */
	@Deprecated
	public static List<String> columns(Connection con, String tableNamePattern) throws Exception {
		return columns(con, tableNamePattern, t -> {
			try {
				return t.getString(4);
			} catch (SQLException e) {
				LOGGER.error(ValueUtil.toString(e));
			}
			return "";
		});
	}

	/**
	 * 테이블 컬럼 조회
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 24.
	 * @param con
	 * @param catalog
	 * @param schema
	 * @param tableNamePattern
	 * @return
	 * @throws Exception
	 */
	public static List<String> columns(Connection con, String catalog, String schema, String tableNamePattern) throws Exception {
		return columns(con, catalog, schema, tableNamePattern, t -> {
			try {
				return t.getString(4);
			} catch (SQLException e) {
				LOGGER.error(ValueUtil.toString(e));
			}
			return "";
		});
	}

	/**
	 * 1. TABLE_CAT String => table catalog (may be null) </br>
	 * 2.TABLE_SCHEM String => table schema (may be null) </br>
	 * 3.TABLE_NAME String => table name </br>
	 * 4.COLUMN_NAME String => column name </br>
	 * 5.DATA_TYPE int => SQL type from java.sql.Types </br>
	 * 6.TYPE_NAME String => Data source dependent type name, for a UDT the type
	 * name is fully qualified </br>
	 * 7.COLUMN_SIZE int => column size. </br>
	 * 8.BUFFER_LENGTH is not used. </br>
	 * 9.DECIMAL_DIGITS int => the number of fractional digits. Null is returned
	 * for data types where DECIMAL_DIGITS is not applicable. </br>
	 * 10.NUM_PREC_RADIX int => Radix (typically either 10 or 2) </br>
	 * 11.NULLABLE int => is NULL allowed. ◦ columnNoNulls - might not allow
	 * NULL values </br>
	 * ◦ columnNullable - definitely allows NULL values </br>
	 * ◦ columnNullableUnknown - nullability unknown </br>
	 * </br>
	 * 12.REMARKS String => comment describing column (may be null) </br>
	 * 13.COLUMN_DEF String => default value for the column, which should be
	 * interpreted as a string when the value is enclosed in single quotes (may
	 * be null) </br>
	 * 14.SQL_DATA_TYPE int => unused </br>
	 * 15.SQL_DATETIME_SUB int => unused </br>
	 * 16.CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in
	 * the column </br>
	 * 17.ORDINAL_POSITION int => index of column in table (starting at 1) </br>
	 * 18.IS_NULLABLE String => ISO rules are used to determine the nullability
	 * for a column. ◦ YES --- if the column can include NULLs </br>
	 * ◦ NO --- if the column cannot include NULLs </br>
	 * ◦ empty string --- if the nullability for the column is unknown </br>
	 * </br>
	 * 19.SCOPE_CATALOG String => catalog of table that is the scope of a
	 * reference attribute (null if DATA_TYPE isn't REF) </br>
	 * 20.SCOPE_SCHEMA String => schema of table that is the scope of a
	 * reference attribute (null if the DATA_TYPE isn't REF) </br>
	 * 21.SCOPE_TABLE String => table name that this the scope of a reference
	 * attribute (null if the DATA_TYPE isn't REF) </br>
	 * 22.SOURCE_DATA_TYPE short => source type of a distinct type or
	 * user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE
	 * isn't DISTINCT or user-generated REF) </br>
	 * 23.IS_AUTOINCREMENT String => Indicates whether this column is auto
	 * incremented ◦ YES --- if the column is auto incremented </br>
	 * ◦ NO --- if the column is not auto incremented </br>
	 * ◦ empty string --- if it cannot be determined whether the column is auto
	 * incremented </br>
	 * </br>
	 * 24.IS_GENERATEDCOLUMN String => Indicates whether this is a generated
	 * column ◦ YES --- if this a generated column </br>
	 * ◦ NO --- if this not a generated column </br>
	 * ◦ empty string --- if it cannot be determined whether this is a generated
	 * column </br>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 10.
	 * @param connection
	 * @param tableNamePattern
	 * @param converter
	 * @return
	 * @throws Exception
	 * @Deprecated catalog, schema 파라미터가 존재하는 API 사용할것.
	 */
	@Deprecated
	public static <T> List<T> columns(Connection connection, String tableNamePattern, Function<ResultSet, T> converter) throws Exception {
		return columns(connection, null, null, tableNamePattern, COLUMN_CONVERTER, converter);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 9. 18.
	 * @param connection
	 * @param catalog
	 * @param tableNamePattern
	 * @param converter
	 * @return
	 * @throws Exception
	 * 
	 */
	public static <T> List<T> columns(Connection connection, String catalog, String schema, String tableNamePattern,
			Function<ResultSet, T> converter) throws Exception {
		return columns(connection, catalog, schema, tableNamePattern, COLUMN_CONVERTER, converter);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 9. 15.
	 * @param connection
	 * @param tableNamePattern
	 * @param columnNameConverter
	 * @param converter
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> columns(Connection connection, String catalog, String schema, String tableNamePattern,
			FourThFunction<String, String, String, DatabaseMetaData, ResultSet> columnNameConverter, Function<ResultSet, T> converter)
			throws Exception {
		if (converter == null)
			throw new GargoyleException(GargoyleException.ERROR_CODE.PARAMETER_EMPTY, "converter is null ");

		List<T> tables = new ArrayList<>();
		// try (Connection connection = getConnection()) {

		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet rs = columnNameConverter.apply(catalog, schema, tableNamePattern, metaData); // metaData.getColumns(null,
		// null,
		// tableNamePattern,
		// null);

		while (rs.next()) {
			tables.add(converter.apply(rs));
		}
		// }

		return tables;
	}

	public static <K, T> Map<K, T> columnsToMap(Connection connection, String tableNamePattern, Function<ResultSet, K> keyMapper,
			Function<ResultSet, T> valueMapper) throws Exception {
		if (keyMapper == null || valueMapper == null)
			throw new GargoyleException(GargoyleException.ERROR_CODE.PARAMETER_EMPTY, "converter is null ");

		Map<K, T> tables = new LinkedHashMap<>();
		// try (Connection connection = getConnection()) {

		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet rs = COLUMN_CONVERTER.apply(null, null, tableNamePattern, metaData);

		while (rs.next()) {
			K k = keyMapper.apply(rs);
			if (k == null)
				continue;
			T t = valueMapper.apply(rs);
			tables.put(k, t);
		}
		return tables;
	}

	public static final FourThFunction<String, String, String, DatabaseMetaData, ResultSet> COLUMN_CONVERTER = (catalog, schema,
			tableNamePattern, metaData) -> {

		try {
			return metaData.getColumns(catalog, schema, tableNamePattern, null);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
		return null;
	};

	public static final FourThFunction<String, String, String, DatabaseMetaData, ResultSet> PRIMARY_CONVERTER = (catalog, schema,
			tableNamePattern, metaData) -> {
		try {

			return metaData.getPrimaryKeys(catalog, schema, tableNamePattern);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
		return null;
	};

	public static List<String> pks(String tableNamePattern) throws Exception {
		return pks(getConnection(), null, null, tableNamePattern, t -> {
			try {
				return t.getString(4);
			} catch (SQLException e) {
				LOGGER.error(ValueUtil.toString(e));
			}
			return "";
		});
	}

	public static List<String> pks(Connection con, String tableNamePattern) throws Exception {
		return pks(con, null, null, tableNamePattern, t -> {
			try {
				return t.getString(4);
			} catch (SQLException e) {
				LOGGER.error(ValueUtil.toString(e));
			}
			return "";
		});
	}

	public static <T> List<T> pks(String tableNamePattern, Function<ResultSet, T> converter) throws Exception {
		List<T> tables = Collections.emptyList();
		try (Connection connection = getConnection()) {
			tables = pks(connection, null, null, tableNamePattern, converter);
		}
		return tables;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 11. 5.
	 * @param connection
	 * @param catalog
	 * @param schema
	 * @param tableNamePattern
	 * @return
	 * @throws Exception
	 */
	public static List<String> pks(Connection connection, String catalog, String schema, String tableNamePattern) throws Exception {
		return pks(connection, catalog, schema, tableNamePattern, t -> {
			try {
				return t.getString(4);
			} catch (SQLException e) {
				LOGGER.error(ValueUtil.toString(e));
			}
			return "";
		});
	}

	public static <T> List<T> pks(Connection connection, String catalog, String schema, String tableNamePattern,
			Function<ResultSet, T> converter) throws Exception {
		if (converter == null)
			throw new GargoyleException(GargoyleException.ERROR_CODE.PARAMETER_EMPTY, "converter is null ");

		List<T> tables = new ArrayList<>();

		DatabaseMetaData metaData = connection.getMetaData();

		ResultSet rs = PRIMARY_CONVERTER.apply(catalog, schema, tableNamePattern, metaData); // metaData.getPrimaryKeys(null,
		// null,
		// tableNamePattern);

		if (rs != null) {
			while (rs.next()) {
				tables.add(converter.apply(rs));
			}
		}

		return tables;
	}

	/**
	 *
	 * // 16.09.01 >> 쿼리로 부터 테이블을 찾아옴 퍼옴 by Hong
	 *
	 * @param sql
	 * @return
	 */
	public static String getTableNames(String sql) {
		// 2016-11-10 문자열 관련된 처리므로 ValueUtil로 이동.
		return ValueUtil.getTableNames(sql);
	}

	/********************************
	 * 작성일 : 2016. 9. 3. 작성자 : KYJ
	 *
	 * 스키마라는 개념이 존재하는 데이터베이스인지 유무
	 *
	 * @return
	 ********************************/
	public static boolean isExistsSchemaDatabase() {

		String driver = DbUtil.getDriver().trim();
		if (driver == null || driver.isEmpty())
			return true;
		return isExistsSchemaDatabase(driver);
	}

	/********************************
	 * 작성일 : 2016. 9. 3. 작성자 : KYJ
	 *
	 * 스키마라는 개념이 존재하는 데이터베이스인지 유무
	 *
	 * @param con
	 * @return
	 ********************************/
	public static boolean isExistsSchemaDatabase(Connection con) {
		try {
			String driverName = con.getMetaData().getDriverName();
			return isExistsSchemaDatabase(driverName);
		} catch (SQLException e) {
			// Nothing.
		}
		return false;
	}

	/********************************
	 * 작성일 : 2016. 9. 3. 작성자 : KYJ
	 *
	 * 스키마라는 개념이 존재하는 데이터베이스인지 유무
	 *
	 * @param driver
	 * @return
	 ********************************/
	public static boolean isExistsSchemaDatabase(String driver) {

		String drivers = ConfigResourceLoader.getInstance().get(ConfigResourceLoader.NOT_EXISTS_SCHEMA_DRIVER_NAMES);
		if (drivers != null && !driver.isEmpty()) {
			drivers = drivers.trim();
			Optional<String> findFirst = Stream.of(drivers.split(",")).filter(v -> v.equals(driver)).findFirst();
			return !findFirst.isPresent();
		}
		return true;
	}

	public static List<String> getSqlFunctions(final Connection con, boolean autoClose) throws SQLException {
		try {
			DatabaseMetaData metaData = con.getMetaData();

			String stringFunctions = metaData.getStringFunctions();
			String numericFunctions = metaData.getNumericFunctions();
			String timeDateFunctions = metaData.getTimeDateFunctions();

			return Stream.of(stringFunctions.split(","), numericFunctions.split(","), timeDateFunctions.split(",")).flatMap(v -> {
				return Stream.of(v);
			}).collect(Collectors.toList());

		} catch (Exception e) {

		} finally {

			if (autoClose)
				con.close();
		}
		return Collections.emptyList();
	}

	public static void getCurrentSchema(Connection con) {

	}

	/**
	 * 빈프로퍼티 로우 매퍼클래스를 생성후 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 4. 4.
	 * @param clazz
	 * @return
	 */
	public static <T> RowMapper<T> createBeanRowMapper(Class<T> clazz) {
		return new BeanPropertyRowMapper(clazz);
		// return ParameterizedBeanPropertyRowMapper.newInstance(clazz);
	}

	/**
	 * find procedure.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 9. 12.
	 * @param connection
	 * @param cat
	 * @param schem
	 * @param procedureNamePattern
	 * @return
	 * @throws SQLException
	 */
	public static List<String> findProcedure(Connection connection, String cat, String schem, String procedureNamePattern)
			throws SQLException {
		List<String> items = new ArrayList<String>();
		try {

			ResultSet rs = connection.getMetaData().getProcedures(cat, schem, "%" + procedureNamePattern + "%");

			while (rs.next()) {
				items.add(String.format("%s.%s.%s", rs.getString(1), rs.getString(2), rs.getString(3)));
			}

		} catch (SQLException e) {
			throw e;
		}

		return items;

	}

	public static List<String> findProcedure(Connection connection, String procedureNamePattern) throws SQLException {
		List<String> items = new ArrayList<String>();

		ResultSet catalogs = connection.getMetaData().getCatalogs();
		while (catalogs.next()) {

			List<String> findProcedure = findProcedure(connection, catalogs.getString(1), null, procedureNamePattern);
			items.addAll(findProcedure);
		}
		return items;
	}

	public static String decryp(Object str) {
		if (ValueUtil.isEmpty(str))
			return "";

		try {
			return EncrypUtil.decryp(str.toString());
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * 처리 가능한 데이터베이스 접속 목록 정보를 로드한다. <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 31.
	 * @return
	 */
	public static List<Map<String, Object>> getAvailableConnections() {
		return getAvailableConnections(m -> true);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 2.
	 * @param filter
	 *            필터
	 * @return
	 */
	public static List<Map<String, Object>> getAvailableConnections(Predicate<Map<String, Object>> filter) {

		ResourceLoader instance = ResourceLoader.getInstance();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Enumeration<Object> keySet = instance.keySet();
		while (keySet.hasMoreElements()) {
			Object _key = keySet.nextElement();
			if (_key == null)
				continue;
			String key = (String) _key;
			if (!key.startsWith("database.info."))
				continue;

			String value = instance.get(key);
			if ("jdbc.pass".equals(key))
				value = decryp(value);
			JSONObject json = ValueUtil.toJSONObject(value);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = new HashMap<String, Object>(json);
			map.put("seqNum", key);

			if (filter != null && filter.test(map))
				list.add(0, map);
		}
		return list;
	}

	public static Connection getConnection(Map<String, Object> map) throws Exception {
		String jdbcDriver = ConfigResourceLoader.getInstance().get("dbms." + map.get(ResourceLoader.DBMS.toString()));
		String password = map.get(ResourceLoader.BASE_KEY_JDBC_PASS) == null ? "" : map.get(ResourceLoader.BASE_KEY_JDBC_PASS).toString();

		if (!"N".equals(map.get(ResourceLoader.BASE_KEY_IS_ENCRYP_JDBC_PASS)))
			password = EncrypUtil.decryp(password);

		return getConnection(jdbcDriver, map.get(ResourceLoader.BASE_KEY_JDBC_URL).toString(),
				map.get(ResourceLoader.BASE_KEY_JDBC_ID) == null ? "" : map.get(ResourceLoader.BASE_KEY_JDBC_ID).toString(), password);

	}

	/**
	 * 프로시저 컬럼 정보를 찾는다.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 17.
	 * @param con
	 * @param catalog
	 * @param schemaPattern
	 * @param procedureNamePattern
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> getProcedureColumns(Connection con, String catalog, String schemaPattern,
			String procedureNamePattern) throws SQLException {
		return getProcedureColumns(con, catalog, schemaPattern, procedureNamePattern, null);
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 19. 
	 * @param con
	 * @param catalog
	 * @param schemaPattern
	 * @param functionNamePattern
	 * @return
	 * @throws SQLException 
	 */
	public static List<Map<String, Object>> getFunctionColumns(Connection con, String catalog, String schemaPattern,
			String functionNamePattern) throws SQLException {
		return getFunctionColumns(con, catalog, schemaPattern, functionNamePattern, null);
	}

	

	/**
	 * 프로시저 컬럼 정보를 찾는다.
	 * 
	 * <OL>
	 * <LI><B>PROCEDURE_CAT</B> String {@code =>} procedure catalog (may be
	 * <code>null</code>)
	 * <LI><B>PROCEDURE_SCHEM</B> String {@code =>} procedure schema (may be
	 * <code>null</code>)
	 * <LI><B>PROCEDURE_NAME</B> String {@code =>} procedure name
	 * <LI><B>COLUMN_NAME</B> String {@code =>} column/parameter name
	 * <LI><B>COLUMN_TYPE</B> Short {@code =>} kind of column/parameter:
	 * <UL>
	 * <LI>procedureColumnUnknown - nobody knows
	 * <LI>procedureColumnIn - IN parameter
	 * <LI>procedureColumnInOut - INOUT parameter
	 * <LI>procedureColumnOut - OUT parameter
	 * <LI>procedureColumnReturn - procedure return value
	 * <LI>procedureColumnResult - result column in <code>ResultSet</code>
	 * </UL>
	 * <LI><B>DATA_TYPE</B> int {@code =>} SQL type from java.sql.Types
	 * <LI><B>TYPE_NAME</B> String {@code =>} SQL type name, for a UDT type the
	 * type name is fully qualified
	 * <LI><B>PRECISION</B> int {@code =>} precision
	 * <LI><B>LENGTH</B> int {@code =>} length in bytes of data
	 * <LI><B>SCALE</B> short {@code =>} scale - null is returned for data types
	 * where SCALE is not applicable.
	 * <LI><B>RADIX</B> short {@code =>} radix
	 * <LI><B>NULLABLE</B> short {@code =>} can it contain NULL.
	 * <UL>
	 * <LI>procedureNoNulls - does not allow NULL values
	 * <LI>procedureNullable - allows NULL values
	 * <LI>procedureNullableUnknown - nullability unknown
	 * </UL>
	 * <LI><B>REMARKS</B> String {@code =>} comment describing parameter/column
	 * <LI><B>COLUMN_DEF</B> String {@code =>} default value for the column,
	 * which should be interpreted as a string when the value is enclosed in
	 * single quotes (may be <code>null</code>)
	 * <UL>
	 * <LI>The string NULL (not enclosed in quotes) - if NULL was specified as
	 * the default value
	 * <LI>TRUNCATE (not enclosed in quotes) - if the specified default value
	 * cannot be represented without truncation
	 * <LI>NULL - if a default value was not specified
	 * </UL>
	 * <LI><B>SQL_DATA_TYPE</B> int {@code =>} reserved for future use
	 * <LI><B>SQL_DATETIME_SUB</B> int {@code =>} reserved for future use
	 * <LI><B>CHAR_OCTET_LENGTH</B> int {@code =>} the maximum length of binary
	 * and character based columns. For any other datatype the returned value is
	 * a NULL
	 * <LI><B>ORDINAL_POSITION</B> int {@code =>} the ordinal position, starting
	 * from 1, for the input and output parameters for a procedure. A value of 0
	 * is returned if this row describes the procedure's return value. For
	 * result set columns, it is the ordinal position of the column in the
	 * result set starting from 1. If there are multiple result sets, the column
	 * ordinal positions are implementation defined.
	 * <LI><B>IS_NULLABLE</B> String {@code =>} ISO rules are used to determine
	 * the nullability for a column.
	 * <UL>
	 * <LI>YES --- if the column can include NULLs
	 * <LI>NO --- if the column cannot include NULLs
	 * <LI>empty string --- if the nullability for the column is unknown
	 * </UL>
	 * <LI><B>SPECIFIC_NAME</B> String {@code =>} the name which uniquely
	 * identifies this procedure within its schema.
	 * </OL>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 17.
	 * @param con
	 * @param catalog
	 * @param schemaPattern
	 * @param procedureNamePattern
	 * @param columnNamePattern
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> getProcedureColumns(Connection con, String catalog, String schemaPattern,
			String procedureNamePattern, String columnNamePattern) throws SQLException {

		// List<Map<String, Object>> r = Collections.emptyList();
		// try {
		// ResultSet resultSet = con.getMetaData().getProcedureColumns(catalog,
		// schemaPattern, procedureNamePattern, columnNamePattern);

		//
		// r = c.apply(resultSet.getMetaData(), resultSet);
		// } catch (Exception e) {
		// LOGGER.error(ValueUtil.toString(e));
		// throw e;
		// }

		ResultSetToMapConverter c = new ResultSetToMapConverter();
		return getProcedureColumns(con, catalog, schemaPattern, procedureNamePattern, columnNamePattern, rs -> {
			try {
				return c.apply(rs.getMetaData(), rs);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});

		// return r;
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 19. 
	 * @param con
	 * @param catalog
	 * @param schemaPattern
	 * @param functionNamePattern
	 * @param columnNamePattern
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> getFunctionColumns(Connection con, String catalog, String schemaPattern,
			String functionNamePattern, String columnNamePattern) throws SQLException {

		ResultSetToMapConverter c = new ResultSetToMapConverter();
		return getFunctionColumns(con, catalog, schemaPattern, functionNamePattern, columnNamePattern, rs -> {
			try {
				return c.apply(rs.getMetaData(), rs);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
	}
	
	

	/**
	 * <OL>
	 * <LI><B>PROCEDURE_CAT</B> String {@code =>} procedure catalog (may be
	 * <code>null</code>)
	 * <LI><B>PROCEDURE_SCHEM</B> String {@code =>} procedure schema (may be
	 * <code>null</code>)
	 * <LI><B>PROCEDURE_NAME</B> String {@code =>} procedure name
	 * <LI><B>COLUMN_NAME</B> String {@code =>} column/parameter name
	 * <LI><B>COLUMN_TYPE</B> Short {@code =>} kind of column/parameter:
	 * <UL>
	 * <LI>procedureColumnUnknown - nobody knows
	 * <LI>procedureColumnIn - IN parameter
	 * <LI>procedureColumnInOut - INOUT parameter
	 * <LI>procedureColumnOut - OUT parameter
	 * <LI>procedureColumnReturn - procedure return value
	 * <LI>procedureColumnResult - result column in <code>ResultSet</code>
	 * </UL>
	 * <LI><B>DATA_TYPE</B> int {@code =>} SQL type from java.sql.Types
	 * <LI><B>TYPE_NAME</B> String {@code =>} SQL type name, for a UDT type the
	 * type name is fully qualified
	 * <LI><B>PRECISION</B> int {@code =>} precision
	 * <LI><B>LENGTH</B> int {@code =>} length in bytes of data
	 * <LI><B>SCALE</B> short {@code =>} scale - null is returned for data types
	 * where SCALE is not applicable.
	 * <LI><B>RADIX</B> short {@code =>} radix
	 * <LI><B>NULLABLE</B> short {@code =>} can it contain NULL.
	 * <UL>
	 * <LI>procedureNoNulls - does not allow NULL values
	 * <LI>procedureNullable - allows NULL values
	 * <LI>procedureNullableUnknown - nullability unknown
	 * </UL>
	 * <LI><B>REMARKS</B> String {@code =>} comment describing parameter/column
	 * <LI><B>COLUMN_DEF</B> String {@code =>} default value for the column,
	 * which should be interpreted as a string when the value is enclosed in
	 * single quotes (may be <code>null</code>)
	 * <UL>
	 * <LI>The string NULL (not enclosed in quotes) - if NULL was specified as
	 * the default value
	 * <LI>TRUNCATE (not enclosed in quotes) - if the specified default value
	 * cannot be represented without truncation
	 * <LI>NULL - if a default value was not specified
	 * </UL>
	 * <LI><B>SQL_DATA_TYPE</B> int {@code =>} reserved for future use
	 * <LI><B>SQL_DATETIME_SUB</B> int {@code =>} reserved for future use
	 * <LI><B>CHAR_OCTET_LENGTH</B> int {@code =>} the maximum length of binary
	 * and character based columns. For any other datatype the returned value is
	 * a NULL
	 * <LI><B>ORDINAL_POSITION</B> int {@code =>} the ordinal position, starting
	 * from 1, for the input and output parameters for a procedure. A value of 0
	 * is returned if this row describes the procedure's return value. For
	 * result set columns, it is the ordinal position of the column in the
	 * result set starting from 1. If there are multiple result sets, the column
	 * ordinal positions are implementation defined.
	 * <LI><B>IS_NULLABLE</B> String {@code =>} ISO rules are used to determine
	 * the nullability for a column.
	 * <UL>
	 * <LI>YES --- if the column can include NULLs
	 * <LI>NO --- if the column cannot include NULLs
	 * <LI>empty string --- if the nullability for the column is unknown
	 * </UL>
	 * <LI><B>SPECIFIC_NAME</B> String {@code =>} the name which uniquely
	 * identifies this procedure within its schema.
	 * </OL>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 29.
	 * @param con
	 * @param catalog
	 * @param schemaPattern
	 * @param procedureNamePattern
	 * @param columnNamePattern
	 * @param callback
	 * @return
	 * @throws SQLException
	 */
	public static <T> T getProcedureColumns(Connection con, String catalog, String schemaPattern, String procedureNamePattern,
			String columnNamePattern, Callback<ResultSet, T> callback) throws SQLException {
		try {
			ResultSet resultSet = con.getMetaData().getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern);
			return callback.call(resultSet);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
			throw e;
		}
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 19. 
	 * @param <T>
	 * @param con
	 * @param catalog
	 * @param schemaPattern
	 * @param procedureNamePattern
	 * @param columnNamePattern
	 * @param callback
	 * @return
	 * @throws SQLException
	 */
	public static <T> T getFunctionColumns(Connection con, String catalog, String schemaPattern, String procedureNamePattern,
			String columnNamePattern, Callback<ResultSet, T> callback) throws SQLException {
		try {
			ResultSet resultSet = con.getMetaData().getFunctionColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern);
			return callback.call(resultSet);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
			throw e;
		}
	}

	/**
	 * 프로시저에 대한 정보를 리턴 <br/>
	 *
	 * <ol>
	 * <li>PROCEDURE_CAT String => procedure catalog (may be null)</li>
	 * <li>PROCEDURE_SCHEM String => procedure schema (may be null)</li>
	 * <li>PROCEDURE_NAME String => procedure name</li>
	 * </ol>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 17.
	 * @param con
	 * @param catalog
	 * @param schemaPattern
	 * @param procedureNamePattern
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> getProcedures(Connection con, String catalog, String schemaPattern, String procedureNamePattern)
			throws SQLException {

		List<Map<String, Object>> r = Collections.emptyList();
		try {
			DatabaseMetaData metaData = con.getMetaData();
			ResultSet resultSet = metaData.getProcedures(catalog, schemaPattern, procedureNamePattern);
			ResultSetToMapConverter c = new ResultSetToMapConverter();

			r = c.apply(resultSet.getMetaData(), resultSet);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
			throw e;
		}
		return r;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 23.
	 * @param con
	 * @param catalog
	 * @param schemaPattern
	 * @param procedureNamePattern
	 * @param callback
	 * @return
	 * @throws SQLException
	 */
	public static <T> List<T> getProcedures(Connection con, String catalog, String schemaPattern, String procedureNamePattern,
			Function<ResultSet, T> callback) throws SQLException {

		List<T> r = new ArrayList<>();
		try {
			DatabaseMetaData metaData = con.getMetaData();
			ResultSet resultSet = metaData.getProcedures(catalog, schemaPattern, procedureNamePattern);

			while (resultSet.next()) {
				T t = callback.apply(resultSet);
				r.add(t);
			}

		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
			throw e;
		}
		return r;
	}

	public static Optional<ResultSet> executeProcedure(Connection con, String _sql, TreeMap<String, Object> param,
			ExceptionHandler handler) {

		Optional<ResultSet> op = Optional.empty();
		if (ValueUtil.isEmpty(_sql))
			return null;

		// if the _sql is dynamic sql , convert param by velocity context.
		String sql = ValueUtil.getVelocityToText(_sql, param);
		if (ValueUtil.isNotEmpty(param)) {
			sql = ValueUtil.getVelocityToText(_sql, param);
		}
		try {

			CallableStatement prepareCall = con.prepareCall(sql);

			Set<String> keySet = param.keySet();

			int seq = 1;
			for (String key : keySet) {
				Object val = param.get(key);
				prepareCall.setObject(seq, val);
				seq++;
			}

			ResultSet rs = prepareCall.executeQuery();

			op = Optional.of(rs);
		} catch (Exception e) {
			if (handler != null)
				handler.handle(e);
			else
				throw new RuntimeException(e);
		}

		return op;
	}

	/**
	 * alias 이름으로 Connection 정보를 찾아 리턴한다. <br/>
	 * 여러건인경우 발견한 첫번째 행만 리턴 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 2.
	 * @param alias
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnectionByAliasName(String alias) throws Exception {
		List<Map<String, Object>> availableConnections = getAvailableConnections(map -> {
			return alias.equals(map.get("alias"));
		});

		if (availableConnections == null || availableConnections.isEmpty())
			return null;
		return getConnection(availableConnections.get(0));
	}

	/**
	 * return the env value from gargoyle database table - tbm_sys_env <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 27.
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("key", key);
		try {
			// String sql = ValueUtil.getVelocityToText();

			List<Map<String, Object>> select = DbUtil.select(DbUtil.getDataSource(),
					"select value from tbm_sys_env where 1=1 and use_yn='Y' and del_yn='N' and key=:key", hashMap,
					new MapBaseRowMapper()
					/*new RowMapper<HashMap<String, Object>>() {

						@Override
						public HashMap<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
							rs.getString("value");
							HashMap<String, Object> hashMap = new HashMap<String, Object>();

							int columnCount = rs.getMetaData().getColumnCount();
							while (rs.next()) {
								for (int c = 1; c < columnCount; c++) {
									hashMap.put(rs.getMetaData().getColumnName(c), rs.getString(c));
								}
							}
							return hashMap;
						}
					}
					*/
					);
			if (!select.isEmpty())
				return select.get(0).get("value").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}
