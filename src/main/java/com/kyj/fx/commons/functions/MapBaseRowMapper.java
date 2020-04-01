/********************************
 *	프로젝트 : spring_batch_tutorial
 *	패키지   : com.mkyong.mapper
 *	작성일   : 2016. 2. 25.
 *	프로젝트 : BATCH 프로젝트
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.functions;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author KYJ
 *
 */
public class MapBaseRowMapper implements RowMapper<Map<String, Object>> {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
	 * int)
	 */
	@Override
	public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {

		HashMap<String, Object> map = new HashMap<String, Object>();

		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			String columnName = metaData.getColumnName(i);
			Object value = rs.getObject(i);
			map.put(columnName, value);
		}

		return map;

	}

}
