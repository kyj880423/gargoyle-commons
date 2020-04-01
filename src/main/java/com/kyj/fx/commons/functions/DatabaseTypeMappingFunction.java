/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.functions
 *	작성일   : 2015. 10. 16.
 *	프로젝트 : VisualFxVoEditor
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.functions;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kyj.Fx.dao.wizard.memory.DatabaseTypeMappingResourceLoader;

/**
 * 데이터베이스 타입을 공통 표준 타입으로 변환
 * 
 * @author KYJ
 *
 */
public class DatabaseTypeMappingFunction implements Function<String, String> {

	private static Logger LOGGER = LoggerFactory.getLogger(DatabaseTypeMappingFunction.class);

	/**
	 * 
	 * 변환
	 * 
	 * @param key
	 *            데이터베이스에서 조회된 타입이름
	 * @return 표준타입으로 변환된 이름
	 */
	@Override
	public String apply(String key) {
		String to = "";

		if (key != null)
			to = DatabaseTypeMappingResourceLoader.getInstance().get(key);
		LOGGER.debug("from: " + key + " to : " + to);
		return to;
	}
}
