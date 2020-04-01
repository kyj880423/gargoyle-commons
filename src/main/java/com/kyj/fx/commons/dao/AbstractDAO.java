/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.dao
 *	작성일   : 2018. 4. 13.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.dao;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import com.kyj.fx.commons.utils.DbUtil;
import com.kyj.fx.commons.utils.ValueUtil;

/**
 * @author KYJ
 *
 */
public class AbstractDAO {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AbstractDAO.class);

	// JdbcTemplate and data source
	private NamedParameterJdbcTemplate template;
	private DataSource dataSource;

	public AbstractDAO() {

	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public NamedParameterJdbcTemplate getNamedJdbcTemplate() {

		if (dataSource == null) {
			try {
				dataSource = DbUtil.getDataSource();
			} catch (Exception e) {
				throw new RuntimeException("can't load database ");
			}
		}

		if (template == null) {
			template = new NamedParameterJdbcTemplate(dataSource);
		}

		return template;
	}

	public void setNamedJdbcTemplate(NamedParameterJdbcTemplate namedJdbcTemplate) {
		this.template = namedJdbcTemplate;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 14. 
	 * @param query
	 * @param paramMap
	 * @param mapper
	 * @return
	 */
	public <T> T queryScala(String query, Map<String, Object> paramMap, ResultSetExtractor<T> mapper) {
		String _query = replaceVelocityText(query, paramMap);

		/**/
		StringBuilder sb = new StringBuilder();
		sb.append(query);
		sb.append("\n");
		sb.append(paramMap);
		DbUtil.noticeQuery(sb.toString());
		/**/
		return getNamedJdbcTemplate().query(_query, paramMap, mapper);
	}

	public <T> List<T> query(String query, Map<String, Object> paramMap, RowMapper<T> mapper) {
		String _query = replaceVelocityText(query, paramMap);

		/**/
		StringBuilder sb = new StringBuilder();
		sb.append(query);
		sb.append("\n");
		sb.append(paramMap);
		DbUtil.noticeQuery(sb.toString());
		/**/

		return getNamedJdbcTemplate().query(_query, paramMap, mapper);
	}

	protected String replaceVelocityText(String query, Map<String, Object> paramMap) {
		return ValueUtil.getVelocityToText(query, paramMap);
	}

	protected boolean isVelocity(String sql) {
		return ((sql.indexOf("#if") > -1 || sql.indexOf("#foreach") > -1) && sql.indexOf("#end") > -1);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 21.
	 * @param sql
	 * @param velocityParam
	 * @param paramMaps
	 * @return
	 * @throws DataAccessException
	 */
	public int[] batchUpdate(String sql, SqlParameterSource[] paramMaps) throws DataAccessException {
		return batchUpdate(sql, Collections.emptyMap(), paramMaps);
	}

	/*
	 * @ update batch 추가.
	 * 
	 */
	public int[] batchUpdate(String sql, Map<String, ?>[] paramMaps) throws DataAccessException {
		if (paramMaps != null && isVelocity(sql)) {
			StringWriter writer = new StringWriter();
			Velocity.evaluate(new VelocityContext(paramMaps[0]), writer, "AbstractIduDAO", sql.toString());
			sql = writer.toString();
		}
		DbUtil.noticeQuery(sql);
		return template.batchUpdate(sql, paramMaps);
	}

	/*
	 * @update 추가.
	 * 
	 */
	public int update(String sql, Map<String, ?> paramMap) throws DataAccessException {
		if (isVelocity(sql)) {
			StringWriter writer = new StringWriter();
			Velocity.evaluate(new VelocityContext(paramMap), writer, "AbstractIduDAO", sql.toString());

			sql = writer.toString();
		}
		DbUtil.noticeQuery(sql);
		return template.update(sql, paramMap);
	}

	/**
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 21.
	 * @param sql
	 * @param paramMaps
	 * @return
	 * @throws DataAccessException
	 */
	public int[] batchUpdate(String sql, Map<String, Object> velocityParam, SqlParameterSource[] paramMaps) throws DataAccessException {
		if (paramMaps != null && isVelocity(sql)) {
			StringWriter writer = new StringWriter();
			Velocity.evaluate(new VelocityContext(velocityParam), writer, "AbstractIduDAO", sql.toString());
			sql = writer.toString();
		}
		DbUtil.noticeQuery(sql);
		return template.batchUpdate(sql, paramMaps);
	}

	public <T> List<T> queryForList(String sql, Map<String, Object> paramMap, Class<T> clazz) {
		if (isVelocity(sql)) {
			StringWriter writer = new StringWriter();
			Velocity.evaluate(new VelocityContext(paramMap), writer, "AbstractDAO", sql.toString());
			sql = writer.toString();
		}
		DbUtil.noticeQuery(sql);
		return template.queryForList(sql, paramMap, clazz);
	}

	/**
	 * 프로시저 호출 <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 24.
	 * @param procedureName
	 * @param paramMap
	 * @param clazz
	 * @return
	 */
	public Map<String, Object> callProcedure(String procedureName, Map<String, Object> paramMap) {

		JdbcTemplate template = (JdbcTemplate) this.template.getJdbcOperations();

		SimpleJdbcCall call = new SimpleJdbcCall(template.getDataSource()).withProcedureName(procedureName);
		LOGGER.debug("procedure : {} ", procedureName);
		DbUtil.noticeQuery(procedureName);
		Map<String, Object> execute = call.execute(paramMap);
		return execute;
	}

}
