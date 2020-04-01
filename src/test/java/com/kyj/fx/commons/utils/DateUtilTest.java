/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2019. 11. 22.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.utils;

import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class DateUtilTest {

	@Test
	public void rangeDayTest() {

		String startDate = "2019-10-15";
		String endDate = "2019-11-20";
		System.out.println(String.format("%s ~ %s", startDate, endDate));

		List<Calendar> rangeDay = DateUtil.rangeDay(startDate, endDate, DateUtil.SYSTEM_DATEFORMAT_YYYY_MM_DD, a -> a);
		rangeDay.forEach(System.out::println);

		Calendar s = DateUtil.toCalendar(DateUtil.toDate(startDate, DateUtil.SYSTEM_DATEFORMAT_YYYY_MM_DD));
		Calendar e = DateUtil.toCalendar(DateUtil.toDate(endDate, DateUtil.SYSTEM_DATEFORMAT_YYYY_MM_DD));
		{
			int expected = rangeDay.get(0).get(Calendar.DAY_OF_MONTH);
			int actual = s.get(Calendar.DAY_OF_MONTH);
			System.out.println(expected + " == " + actual);
			Assert.assertEquals(expected, actual);
		}

		int len = rangeDay.size() - 1;
		{

			int expected = rangeDay.get(len).get(Calendar.DAY_OF_MONTH);
			int actual = e.get(Calendar.DAY_OF_MONTH);
			System.out.println(expected + " == " + actual);
			Assert.assertEquals(expected, actual);
		}
	}

}
