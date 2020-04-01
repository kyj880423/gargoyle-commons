/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.memory
 *	작성일   : 2018. 12. 15.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.memory;

import java.io.File;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.kyj.fx.commons.dao.AbstractDAO;
import com.kyj.fx.commons.utils.ValueUtil;

/**
 * 
 * Gargoyle 데이터베이스와 연동을 위한 객체. <br/>
 * 
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class GargoyleDb extends AbstractDAO {

	public static final String GARGOYLE_DB_URL;
	public static final String DRIVER = "org.sqlite.JDBC";
	private static final DataSource source = new DataSource();
	static {
		/* 설치 위치마다 Location이 다르기때문에 db 위치정보를 static 초기화 블록에서 수행한다. */
		GARGOYLE_DB_URL = "jdbc:sqlite:" + ValueUtil.getBaseDir() + File.separator + "gargoyle.db";
		source.setDriverClassName(DRIVER);
		source.setUrl(GARGOYLE_DB_URL);

		source.setInitSQL("select 1");
		source.setTestOnConnect(true);
		source.setTestOnBorrow(true);
//		source.setValidationQuery("select 1");
	}

	/**
	 * DataSource를 초기화한다 <br/>
	 */
	public GargoyleDb() {
		super();
		this.setDataSource(source);
	}

}
