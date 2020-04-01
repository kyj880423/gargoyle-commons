/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.memory
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.memory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.kyj.fx.commons.utils.DbUtil;
import com.kyj.fx.commons.utils.ValueUtil;

/**
 * 2016-12-09 File기반 환경설정에서 Database 기반 환경설정으로 변경 by kyj.
 *
 * @author KYJ
 *
 */
public class ResourceLoaderDbProperties extends Properties {

	/**
	 * @최초생성일 2016. 12. 9.
	 */
	private static final long serialVersionUID = -8896422372811421885L;

	public static final String GARGOYLE_DB_URL;

	public static final String DRIVER = "org.sqlite.JDBC";

	static {
		GARGOYLE_DB_URL = "jdbc:sqlite:" + ValueUtil.getBaseDir() + File.separator + "gargoyle.db";
	}

	private Supplier<Connection> supplier;

	private ResourceLoaderDbProperties() {

		supplier = new Supplier<Connection>() {

			@Override
			public Connection get() {
				try {
					Connection connection = DbUtil.getConnection(DRIVER, GARGOYLE_DB_URL, new Properties());
					connection.setAutoCommit(true);
					return connection;
				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}
		};
		//

		try (Connection con = supplier.get()) {
			String createTable = "create table if not exists tbm_sys_env ( key text , value text , PRIMARY KEY (key)  )";
			PreparedStatement prepareStatement = con.prepareStatement(createTable);
			prepareStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		/* [start] Migration new column */
		try (Connection con = supplier.get()) {
			String createTable = "alter table tbm_sys_env add column use_yn varchar(1) default 'Y' ";
			PreparedStatement prepareStatement = con.prepareStatement(createTable);
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			// Nothing
		}

		try (Connection con = supplier.get()) {
			String createTable = "alter table tbm_sys_env add column del_yn varchar(1) default 'N' ";
			PreparedStatement prepareStatement = con.prepareStatement(createTable);
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			// Nothing
		}
		/* [end] Migration new column */

		load();

	}

	private static ResourceLoaderDbProperties INSTANCE = new ResourceLoaderDbProperties();

	public static synchronized ResourceLoaderDbProperties getInstance() {

		// if (INSTANCE == null) {
		//
		// INSTANCE = new ResourceLoaderDbProperties();
		//
		// }
		return INSTANCE;
	}

	// private boolean isEmpty(Object key) {
	//// try (Connection con = supplier.get()) {
	////
	////
	//// Map<String, Object> findOne = DbUtil.findOne(con,
	//// String.format("select 1 as v from tbm_sys_env where key ='%s'",
	// key.toString()));
	////
	//// if (findOne.isEmpty())
	//// return true;
	////
	//// if (findOne.get("v") == null)
	//// return true;
	//// return false;
	//// } catch (Exception e) {
	////
	//// }
	// return super.isEmpty();
	// }

	private int update(Object key, Object value) {
		try (Connection con = supplier.get()) {

			return DbUtil.update(con,
					String.format("update tbm_sys_env set value='%s' where key = '%s' ", value.toString(), key.toString()));
		} catch (Exception e) {
		}
		return -1;
	}

	private int insert(Object key, Object value) {
		try (Connection con = supplier.get()) {
			return DbUtil.update(con, String.format("insert into tbm_sys_env values ('%s','%s') ", key.toString(), value.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public Object findOne(Object key) {
		Object v = null;
		try (Connection con = supplier.get()) {

			PreparedStatement stat = con.prepareStatement(
					String.format("select value from tbm_sys_env where key = '%s' and del_yn='N' and use_yn='Y' limit 1", key));
			ResultSet rs = stat.executeQuery();
			if (rs.next()) {
				v = rs.getObject("value");
			}

		} catch (Exception e) {
		}
		return v;
	}

	@Override
	public synchronized void load(Reader reader) throws IOException {
		load();
	}

	@Override
	public synchronized void load(InputStream inStream) throws IOException {
		load();
	}

	@Override
	public synchronized void loadFromXML(InputStream in) throws IOException, InvalidPropertiesFormatException {
		load();
	}

	@Override
	public void store(Writer writer, String comments) throws IOException {
		saveAll();
		// load();
	}

	@Override
	public void store(OutputStream out, String comments) throws IOException {
		saveAll();
		// load();
	}

	private void load() {

		try (Connection con = supplier.get()) {

			PreparedStatement stat = con
					.prepareStatement("select key, value from tbm_sys_env where 1=1 and del_yn = 'N' and use_yn = 'Y' ");
			ResultSet rs = stat.executeQuery();

			Map<Object, Object> m = new HashMap<>();
			while (rs.next()) {
				m.put(rs.getObject("key"), rs.getObject("value"));
			}

			putAll(m);

		} catch (Exception e) {
		}
	}

	private void saveAll() {

		Set<java.util.Map.Entry<Object, Object>> entrys = entrySet();
		try {
			Connection con = null;
			String update = "update tbm_sys_env set value = '%s' where key = '%s'";
			String insert = " insert into tbm_sys_env (key, value) values ('%s', '%s') ";

			try {
				con = supplier.get();
				con.setAutoCommit(false);
				Iterator<java.util.Map.Entry<Object, Object>> iterator = entrys.iterator();
				Statement stat = con.createStatement();

				while (iterator.hasNext()) {
					java.util.Map.Entry<Object, Object> next = iterator.next();
					Object key = next.getKey();
					Object value = next.getValue();

					try {

						// Map<String, Object> findOne = DbUtil.findOne(con,
						// String.format("select 1 as v from tbm_sys_env where
						// key ='%s'", key));

						Map<Object, Object> hashMap = new HashMap<>();
						hashMap.put(key, value);

						if (findOne(key) != null) {
							stat.addBatch(String.format(update, value.toString(), key.toString()));
						} else {
							stat.addBatch(String.format(insert, key.toString(), value.toString()));
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				int[] executeBatch = stat.executeBatch();
				int sum = IntStream.of(executeBatch).sum();

				con.commit();
			} catch (SQLException e1) {
				con.rollback();
				e1.printStackTrace();
			} finally {
				if (con != null) {
					con.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void clearKeys(List<String> keys) {

		try {
			Connection con = null;
			String update = "delete from tbm_sys_env where key = '%s'";

			try {
				con = supplier.get();
				con.setAutoCommit(false);
				Iterator<String> iterator = keys.iterator();
				Statement stat = con.createStatement();

				while (iterator.hasNext()) {
					String key = iterator.next();

					try {

						// Map<String, Object> findOne = DbUtil.findOne(con,
						// String.format("select 1 as v from tbm_sys_env where
						// key ='%s'", key));

						Map<Object, Object> hashMap = new HashMap<>();
						hashMap.put(key, key);

						stat.addBatch(String.format(update, key.toString()));

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				stat.executeBatch();
				// int sum = IntStream.of(executeBatch).sum();

				con.commit();
			} catch (SQLException e1) {
				con.rollback();
				e1.printStackTrace();
			} finally {
				if (con != null) {
					con.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
