package com.kyj.fx.commons.functions;

import java.sql.Connection;
import java.util.Map;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.memory.ResourceLoader;
import com.kyj.fx.commons.utils.DbUtil;
import com.kyj.fx.commons.utils.ExceptionHandler;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.utils.EncrypUtil;

/**
 * 데이터베이스 커넥션정보를 리턴해주는 객체 더불어 접속 관련 메타정보를 기록.
 * 
 * @author KYJ
 *
 */
public class ConnectionSupplier implements Supplier<Connection> {

	private static Logger LOGGER = LoggerFactory.getLogger(ConnectionSupplier.class);

	private String url;
	private String username;
	private String password;
	private String driver;

	// 17.11.03 추가하되는 필드긴한데 적용되는지는 확인해볼 사항.
	private String catalog;
	private String schema;

	private ExceptionHandler exHandler;

	public ConnectionSupplier() {

	}
	
	public static String BASE_KEY_JDBC_URL = ResourceLoader.BASE_KEY_JDBC_URL;
	public static String BASE_KEY_JDBC_ID = ResourceLoader.BASE_KEY_JDBC_ID;
	public static String BASE_KEY_JDBC_PASS = ResourceLoader.BASE_KEY_JDBC_PASS;
	public static String BASE_KEY_JDBC_DRIVER = "driver";
	
	public ConnectionSupplier(Map<String, Object> map) throws Exception {
		this.url = map.get(ResourceLoader.BASE_KEY_JDBC_URL).toString();
		this.username = map.get(ResourceLoader.BASE_KEY_JDBC_ID) == null ? "" : map.get(ResourceLoader.BASE_KEY_JDBC_ID).toString();
		this.password = map.get(ResourceLoader.BASE_KEY_JDBC_PASS) == null ? ""
				: EncrypUtil.decryp(map.get(ResourceLoader.BASE_KEY_JDBC_PASS).toString());
		this.driver = map.get(BASE_KEY_JDBC_DRIVER).toString();
	}

	@Override
	public Connection get() {

		Connection con = null;
		try {

			if (getUrl() == null || getUsername() == null || getPassword() == null) {
				con = DbUtil.getConnection();
			} else {
				con = DbUtil.getConnection(getDriver(), getUrl(), getUsername(), getPassword());
			}

			// 17.11.03 추가하되는 필드긴한데 적용되는지는 확인해볼 사항.
			if (ValueUtil.isNotEmpty(this.catalog)) {
				con.setCatalog(this.catalog);
			}
			
			// 17.11.03 추가하되는 필드긴한데 적용되는지는 확인해볼 사항.
			if (ValueUtil.isNotEmpty(this.schema)) {
				con.setSchema(this.schema);
			}


		} catch (Exception e) {
			if (exHandler != null)
				exHandler.handle(e);
			else
				LOGGER.error(ValueUtil.toString(e));
		}
		return con;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 3. 
	 * @param catalog
	 */
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 3. 
	 * @param schema
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	public ExceptionHandler getExHandler() {
		return exHandler;
	}

	public void setExHandler(ExceptionHandler exHandler) {
		this.exHandler = exHandler;
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getDriver() {
		return driver;
	}

}
