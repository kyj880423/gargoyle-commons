/********************************
 *	프로젝트 : sos-client
 *	패키지   : com.samsung.sds.sos.client.util
 *	작성일   : 2016. 9. 6.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.excel;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.ValueUtil;

/**
 *  ExcelData부 핸들러.
 * 
 * @author KYJ
 *
 */
public class DefautExcelDataSetHandler implements IExcelDataSetHandler<Sheet, LinkedHashMap<ExcelColumnExpression, List<Object>>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefautExcelDataSetHandler.class);

	/* (non-Javadoc)
	 * @see com.samsung.sds.sos.client.util.ExcelDataSetConverter#accept(org.apache.poi.ss.usermodel.Sheet, java.util.List)
	 */
	@Override
	public void accept(final int startRow, Sheet sheet, LinkedHashMap<ExcelColumnExpression, List<Object>> map) {
		Iterator<ExcelColumnExpression> iterator = map.keySet().iterator();
		int i = -1;

		while (iterator.hasNext()) {
			ExcelColumnExpression next = iterator.next();

			LOGGER.debug("{}", next);

			int columnIdx = next.index + START_COLUMN_INDEX;

			List<Object> values = map.get(next);
			i = startRow;
			for (Object value : values) {
				try {
					createCell(sheet, value, ++i, columnIdx);
				} catch (Exception e) {
					LOGGER.error(ValueUtil.toString(e));
				}
			}

		}

	}

	/**
	 * 사용자 화면 정보를 담는 스크린 핸들러
	 * @최초생성일 2016. 9. 8.
	 */
	private IExcelScreenHandler screenHandler;

	@Override
	public void setExcelScreenHandler(IExcelScreenHandler screenHandler) {
		this.screenHandler = screenHandler;
	}

	@Override
	public IExcelScreenHandler getExcelScreenHandler() {
		return this.screenHandler;
	}

}
