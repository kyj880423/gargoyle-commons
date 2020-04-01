/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.util.Date;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

/**
 * 날짜 데이터가 기본적으로 입력되는 컨텍스트 플러그인
 *
 * 기본 날짜포멧 : YYYYMMDDHHMMSS
 *
 * @author KYJ
 *
 */

public class DateFormatVelocityContextExtension extends VelocityContext {

	private String dateFormat = DateUtil.SYSTEM_DATEFORMAT_YYYYMMDDHHMMSS;

	public DateFormatVelocityContextExtension() {
		super();
	}

	public DateFormatVelocityContextExtension(String dateFormat) {
		super();
		this.dateFormat = dateFormat;
	}

	public DateFormatVelocityContextExtension(Context innerContext) {
		super(innerContext);
	}

	public DateFormatVelocityContextExtension(@SuppressWarnings("rawtypes") Map context, Context innerContext) {
		super(context, innerContext);
	}

	/* (non-Javadoc)
	 * @see org.apache.velocity.VelocityContext#internalGet(java.lang.String)
	 */
	@Override
	public Object internalGet(String key) {

		if ("date".equals(key))
			return DateUtil.getDateAsStr(new Date(), this.dateFormat);

		return super.internalGet(key);
	}
}
