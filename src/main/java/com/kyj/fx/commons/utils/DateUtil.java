/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.kyj.fx.commons.models.GagoyleDate;

/**
 * 날짜처리와 관련된 유틸리티 클래스
 *
 * @author KYJ
 *
 */
public class DateUtil {

	public static final String SYSTEM_DATEFORMAT_YYYYMMDDHHMMSSS = "yyyyMMddHHmmssS";
	public static final String SYSTEM_DATEFORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	public static final String SYSTEM_DATEFORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String SYSTEM_DATEFORMAT_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd hh:mm:ss SSS";
	public static final String SYSTEM_DATEFORMAT_YYYY_MM_DDTHH_MM_SS_SSS = "yyyy-MM-dd'T'hh:mm:ss.SSS";
	public static final String SYSTEM_DATEFORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String SYSTEM_DATEFORMAT_YY_MM_DD_HH_MM_SS = "yy-MM-dd HH:mm:ss";

	public static final String SYSTEM_DATEFORMAT_YYYYMMDDHH = "yyyyMMddHH";
	public static final String SYSTEM_DATEFORMAT_YYYYMMDD = "yyyyMMdd";
	public static final String SYSTEM_DATEFORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String SYSTEM_DATEFORMAT_YYYY_MM_DD_EEE = "yyyy-MM-dd EEE";

	public static final String SYSTEM_DATEFORMAT_YYYY_MM = "yyyyMM";

	public static final String SYSTEM_DATEFORMAT_SSS = "SSS";
	public static final String SYSTEM_DATEFORMAT_YYYY = "yyyy";
	public static final String SYSTEM_DATEFORMAT_EEE = "EEE";/* 요일패턴. */
	public static final String SYSTEM_DATEFORMAT_HHMMSS = "HHMmmss";

	public static final String ORACLE_DATEFORMAT_YYYYMMDDHH24MISS = "YYYYMMDDHH24MISS";

	private static final SimpleDateFormat DEFAULT_SYSTEM_DATE_FORMAT = new SimpleDateFormat(SYSTEM_DATEFORMAT_YYYY_MM_DD_HH_MM_SS_SSS);

	public static enum DATE_TYPE {
		YEAR, MONTH, WEEK
	}

	public synchronized static String getCurrentDateString() {
		SimpleDateFormat format = new SimpleDateFormat(SYSTEM_DATEFORMAT_YYYYMMDDHHMMSSS);
		Date time = Calendar.getInstance().getTime(); //GregorianCalendar.getInstance().getTime();
		return format.format(time);
	}

	public static String getDateString(long dateTime, String format) {
		Calendar instance = GregorianCalendar.getInstance();
		synchronized (instance) {
			Date time = new Date(dateTime);

			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(time);
		}
	}

	/**
	 * year의 month에 해당하는 일자 목록을 리턴받는다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 2. 19.
	 * @param year
	 * @param month
	 * @return
	 */
	public static List<GagoyleDate> getPeriodDays(int year, int month) {
		GregorianCalendar today = new GregorianCalendar(year, month, 0);
		int maxday = today.getActualMaximum((GregorianCalendar.DAY_OF_MONTH));
		TimeZone timeZone = today.getTimeZone();
		List<GagoyleDate> dateList = new ArrayList<>();
		for (int i = 1; i <= maxday; i++)
			dateList.add(new GagoyleDate(timeZone, year, month, i));
		return dateList;
	}

	/**
	 * 현재 주차에 해당하는 일자수 리턴.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 14.
	 * @return
	 */
	public static List<GagoyleDate> getPeriodDaysByWeek() {
		Calendar instance = Calendar.getInstance();
		int year = instance.get(Calendar.YEAR);
		int week = instance.get(Calendar.WEEK_OF_YEAR);
		return getPeriodDaysByWeek(year, week);
	}

	/**
	 * 현재 주에서 gap주차를 뺀값부터 나열.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 18.
	 * @param gap
	 * @return
	 */
	public static List<GagoyleDate> getPeriodDaysByWeek(int gap) {
		Calendar current = Calendar.getInstance();
		int currentYear = current.get(Calendar.YEAR);
		int currentWeek = current.get(Calendar.WEEK_OF_YEAR);

		Calendar past = Calendar.getInstance();
		past.set(Calendar.WEEK_OF_MONTH, (-gap));
		int pastYear = past.get(Calendar.YEAR);
		int pastWeek = past.get(Calendar.WEEK_OF_YEAR);

		return getPeriodDaysByWeek(pastYear, pastWeek, currentYear, currentWeek);
	}

	public static List<GagoyleDate> getPeriodDaysByWeek(int startYear, int startWeek, int endYear, int endWeek) {

		Calendar firstCalendar = GregorianCalendar.getInstance();
		firstCalendar.setWeekDate(startYear, startWeek, Calendar.SUNDAY);
		// Date firstDate = firstCalendar.getTime();

		Calendar lastCalendar = GregorianCalendar.getInstance();

		lastCalendar.setWeekDate(endYear, endWeek, /* Calendar.SUNDAY */ lastCalendar.get(Calendar.DAY_OF_WEEK));
		// Date lastDate = lastCalendar.getTime();

		ArrayList<GagoyleDate> arrayList = new ArrayList<>();
		while (firstCalendar.before(lastCalendar)) {
			firstCalendar.add(Calendar.DATE, 1);
			arrayList.add(new GagoyleDate(firstCalendar.getTime()));
		}

		// List<GagoyleDate> periodDaysByWeek1 = getPeriodDaysByWeek(startYear,
		// startWeek);
		// List<GagoyleDate> periodDaysByWeek2 = getPeriodDaysByWeek(endYear,
		// endWeek);
		//
		// ArrayList<GagoyleDate> arrayList = new
		// ArrayList<>(periodDaysByWeek1.size() + periodDaysByWeek2.size());
		// arrayList.addAll(periodDaysByWeek1);
		// arrayList.addAll(periodDaysByWeek2);

		return arrayList;
	}

	/**
	 * 주차에 해당하는 일자수 리턴.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 14.
	 * @param year
	 * @param week
	 * @return
	 */
	public static List<GagoyleDate> getPeriodDaysByWeek(int year, int week) {

		Calendar instance = GregorianCalendar.getInstance();

		instance.setWeekDate(year, week, Calendar.SUNDAY);

		// int minimum =
		// instance.getActualMinimum(GregorianCalendar.DAY_OF_WEEK);
		// int maximum =
		// instance.getActualMaximum(GregorianCalendar.DAY_OF_WEEK);

		TimeZone timeZone = instance.getTimeZone();
		List<GagoyleDate> dateList = new ArrayList<>();

		for (int i = 0 /* minimum */; i <= 6/* maximum */; i++)

			dateList.add(new GagoyleDate(timeZone, instance.get(GregorianCalendar.YEAR), instance.get(GregorianCalendar.MONTH),
					instance.get(GregorianCalendar.DAY_OF_MONTH) + i));

		return dateList;
	}

	/**
	 * 첫번째 주차 날짜정보 리턴.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 14.
	 * @param year
	 * @param week
	 * @return
	 */
	public static GagoyleDate getFirstDateByWeek(int year, int week) {
		Calendar instance = GregorianCalendar.getInstance();
		instance.setWeekDate(year, week, Calendar.SUNDAY);
		TimeZone timeZone = instance.getTimeZone();
		return new GagoyleDate(timeZone, instance.get(GregorianCalendar.YEAR), instance.get(GregorianCalendar.MONTH),
				instance.get(GregorianCalendar.DAY_OF_MONTH));
	}

	public static GagoyleDate getLastDateByWeek(int year, int week) {
		Calendar instance = GregorianCalendar.getInstance();
		instance.setWeekDate(year, week, Calendar.SUNDAY);
		TimeZone timeZone = instance.getTimeZone();
		return new GagoyleDate(timeZone, instance.get(GregorianCalendar.YEAR), instance.get(GregorianCalendar.MONTH),
				instance.get(GregorianCalendar.DAY_OF_MONTH) + 6);
	}

	/**
	 * 속하는 해당 월의 마지막 첫번째 일자를 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 4.
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfMonth(Date date) {
		Calendar instance = GregorianCalendar.getInstance();
		instance.setTime(date);
		int maximum = instance.getMinimum(Calendar.DAY_OF_MONTH);
		instance.set(Calendar.DAY_OF_MONTH, maximum);
		return instance.getTime();
	}

	/**
	 * 속하는 해당 월의 마지막 일자를 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 4.
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfMonth(Date date) {
		Calendar instance = GregorianCalendar.getInstance();
		instance.setTime(date);
		int maximum = instance.getMaximum(Calendar.DAY_OF_MONTH);
		instance.set(Calendar.DAY_OF_MONTH, maximum);
		return instance.getTime();
	}

	/**
	 * 두 날짜 사이의 차이를 calendarField 타입형태로 리턴한다.
	 * 
	 * 밀리초, 초, 분, 시간만 지원한다.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 3. 27.
	 * @param end
	 * @param start
	 * @param calendarField
	 *            Calendar
	 * @return
	 */
	public static long calcTime(Date end, Date start, int calendarField) {
		if (end == null || start == null)
			return 0;
		long diff = end.getTime() - start.getTime();

		switch (calendarField) {
		case Calendar.MILLISECOND:
			return diff;

		case Calendar.SECOND:
			return diff / 1000;

		case Calendar.MINUTE:
			return diff / (1000 * 60);

		case Calendar.HOUR:
			return diff / (1000 * 60 * 24);
		default:
			throw new RuntimeException("Not support.");
		}
	}

	/**
	 * date에 calendarField 속성에 맞는 amount값을 처리한 결과를 리턴함.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 4. 10.
	 * @param date
	 * @param totayTotalMin
	 * @param calendarField
	 * @return
	 */
	public static Date calc(Date date, int calendarField, int amount) {
		// date값을 복사
		Date newDate = new Date(date.getTime());
		Calendar instance = Calendar.getInstance();
		instance.setTime(newDate);
		instance.add(calendarField, amount);
		return instance.getTime();
	}

	public static final Calendar toNowCalendar() {
		return toCalendar(new Date());
	}

	public static final Calendar toCalendar(Date date) {
		if (date == null)
			throw new RuntimeException("date is null.");

		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	/**
	 * date 파라미터에 해당하는 주에 포함되는 eee(요일)을 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 3. 25.
	 * @param date
	 *            적용하려는 주의 date값
	 * @param eee
	 *            Calendar안에 속하는 요일값
	 * @return
	 */
	public static Date getFirstWeekOfEEE(Date date, int eee) {
		Calendar instance = GregorianCalendar.getInstance();
		instance.setTime(date);
		instance.set(Calendar.DAY_OF_WEEK, eee);
		return instance.getTime();
	}

	public static Date getCurrentDate(String format) throws ParseException {
		String str = getCurrentDateString(format);
		return new SimpleDateFormat(format).parse(str);
	}

	public static String getCurrentDateString(String format) {
		Date time = GregorianCalendar.getInstance().getTime();
		return new SimpleDateFormat(format).format(time);
	}

	public static String getDateString(Date date) {
		return DEFAULT_SYSTEM_DATE_FORMAT.format(date);
	}

	public static String getDateString(long dateTime) {
		return getDateString(new Date(dateTime));
	}

	public static String getDateAsStr(Date date, String format) {
		return getDateAsStr(date, format, Locale.ENGLISH);
	}

	public static String getDateAsStr(Date date, String format, Locale locale) {
		return new SimpleDateFormat(format, locale).format(date);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 2. 15.
	 * @param when
	 * @param systemDateformatYyyyMmDdHhMmSs
	 * @return
	 */
	public static String toString(Date when, String format) {
		return getDateAsStr(when, format);
	}

	/**
	 * Date클래스 toString
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 3. 31.
	 * @param when
	 * @param format
	 * @param locale
	 * @return
	 */
	public static String toString(Date when, String format, Locale locale) {
		return getDateAsStr(when, format, locale);
	}

	public static String getDateAsStr(String date, String fromFormat, String toFormat) {
		String returnDate = "";

		try {
			returnDate = new SimpleDateFormat(toFormat).format(new SimpleDateFormat(fromFormat).parse(date));
		} catch (ParseException e) {
			returnDate = date;
		}

		return returnDate;
	}

	public static String getSystemDateAsStr(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 11. 7.
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static Date toDate(String date) {

		if (date == null || ValueUtil.isEmpty(date))
			return null;

		Field[] declaredFields = DateUtil.class.getDeclaredFields();
		Date d = null;
		for (Field f : declaredFields) {
			if (!f.isAccessible())
				f.setAccessible(true);

			if (!Modifier.isStatic(f.getModifiers()))
				continue;

			if (!f.getName().startsWith("SYSTEM_DATEFORMAT")) {
				continue;
			}

			try {
				Object format = f.get(null);
				if (ValueUtil.isEmpty(format))
					continue;

				d = toDate(date, format.toString());
				if (d != null)
					break;
			} catch (Exception e) {
				// Nothing.
			}

		}

		if (d == null)
			throw new RuntimeException("Invalid Convert Date Format");

		return d;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 11. 14.
	 * @param date
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date toDate(String date, String format) {
		return toDate(date, Locale.getDefault(), format);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 11. 14.
	 * @param date
	 * @param locale
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date toDate(String date, Locale locale, String format) {
		try {
			return new SimpleDateFormat(format, locale).parse(date);
		} catch (ParseException e) {
			/**/
		}
		return null;
	}

	/**
	 * 요일 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 4. 10.
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		return getField(date, Locale.getDefault(), Calendar.DAY_OF_WEEK);
	}

	/**
	 * Date의 특정 필드값 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 4. 10.
	 * @param date
	 * @param calendarField
	 * @return
	 */
	public static int getField(Date date, int calendarField) {
		return getField(date, Locale.getDefault(), calendarField);
	}

	/**
	 * Date의 특정 필드값 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 4. 10.
	 * @param date
	 * @param locale
	 * @param calendarField
	 * @return
	 */
	public static int getField(Date date, Locale locale, int calendarField) {
		Calendar instance = Calendar.getInstance(locale);
		instance.setTime(date);
		return instance.get(calendarField);
	}

	/**
	 * 타임존에 대한 정보를 리턴함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 5. 9.
	 * @param tz
	 * @return
	 */
	public static String displayTimeZone(TimeZone tz) {
		long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
		long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset()) - TimeUnit.HOURS.toMinutes(hours);
		// avoid -4:-30 issue
		minutes = Math.abs(minutes);
		String result = "";
		if (hours > 0) {
			result = String.format("(GMT+%d:%02d) %s", hours, minutes, tz.getID());
		} else {
			result = String.format("(GMT%d:%02d) %s", hours, minutes, tz.getID());
		}
		return result;
	}

	/**
	 * 시스템 디폴트 타임존 리턴
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 5. 9.
	 * @return
	 */
	public static String displayGMT() {
		return displayHourGMT(Calendar.getInstance().getTimeZone());
	}

	/**
	 * 타임존에 해당하는 GMT 시간을 리턴
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 5. 9.
	 * @param tz
	 * @return
	 */
	public static String displayGMT(TimeZone tz) {
		return displayHourGMT(tz);
	}

	/**
	 * 타임존에 해당하는 GMT 시간을 리턴
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 5. 9.
	 * @param tz
	 * @return
	 */
	public static String displayHourGMT(TimeZone tz) {
		return displayGMT(tz, (a, b) -> "GMT+%d");
	}

	/**
	 * 타임존에 해당하는 시간과 분 정보를 리턴
	 *
	 * 디폴트 포멧 ::: GMT+%d:%02d
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 5. 9.
	 * @param tz
	 * @return
	 */
	public static String displayHourMinuteGMT(TimeZone tz) {
		return displayGMT(tz, (a, b) -> "GMT+%d:%02d");
	}

	/**
	 * GMT 시간정보를 리턴함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 5. 9.
	 * @param tz
	 * @param format
	 *            파라미터 시간,분 정보를 받고 포멧을 리턴함.
	 * @return
	 */
	public static String displayGMT(TimeZone tz, BiFunction<Long, Long, String> format) {

		long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
		long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset()) - TimeUnit.HOURS.toMinutes(hours);
		// avoid -4:-30 issue
		minutes = Math.abs(minutes);
		String result = "";
		if (hours > 0) {
			result = String.format(format.apply(hours, minutes), hours, minutes, tz.getID());
		} else {
			result = String.format(format.apply(hours, minutes), hours, minutes, tz.getID());
		}
		return result;

	}

	/**
	 * Javafx에서 사용되는 Date관련 유틸리티
	 * 
	 * @author KYJ
	 *
	 */
	public static class Fx {

		public static final String EEE = "EEE";
		public static final String uuuuMMdd = "uuuuMMdd";
		public static final String uuuuMMDDHHmmss = "uuuuMMddHmmss";
		public static final String uuuu_MM_DD__HH_mm_ss = "uuuu-MM-dd H:mm:ss";
		public static final String HHmm = "HHmm";

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2016. 12. 16.
		 * @param date
		 * @return
		 */
		public static String toString(LocalDate date) {
			return date.format(DateTimeFormatter.ofPattern(uuuuMMDDHHmmss));
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2016. 12. 16.
		 * @param date
		 * @param pattern
		 * @return
		 */
		public static String toString(LocalDate date, String pattern) {
			if (date == null)
				return "";
			return date.format(DateTimeFormatter.ofPattern(pattern));
		}

		public static String toString(LocalTime date, String pattern) {
			if (date == null)
				return "";
			return date.format(DateTimeFormatter.ofPattern(pattern));
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2016. 12. 16.
		 * @param eee2
		 * @param eee3
		 */
		public static LocalDate toDate(String eee, String pattern) {
			if (eee == null || pattern == null)
				return null;
			return LocalDate.parse(eee, DateTimeFormatter.ofPattern(pattern));
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2017. 03. 27.
		 * @param eee
		 * @param pattern
		 */
		public static LocalDateTime toDateTime(String date, String pattern) {
			return toDateTime(date, pattern, Locale.getDefault());
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2018. 9. 11.
		 * @param date
		 * @param pattern
		 * @param locale
		 * @return
		 */
		public static LocalDateTime toDateTime(String date, String pattern, Locale locale) {
			if (date == null || pattern == null)
				return null;
			return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern, locale));
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2017. 03. 27.
		 * @param eee
		 * @param pattern
		 */
		public static LocalTime toLocalTime(String date, String pattern) {
			if (date == null || pattern == null)
				return null;
			return LocalTime.parse(date, DateTimeFormatter.ofPattern(pattern));
		}
	}

	/**
	 * date 기준으로 h, m , s 를 더한 시간 정보를 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 4. 28.
	 * @param date
	 * @param h
	 * @param m
	 * @param s
	 * @return
	 */
	public static Date addDelta(Date date, int h, int m, int s) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.add(Calendar.HOUR_OF_DAY, h);
		instance.add(Calendar.MINUTE, m);
		instance.add(Calendar.SECOND, s);
		return instance.getTime();
	}

	/**
	 * calc date. <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 18.
	 * @param date
	 * @param field
	 * @param v
	 * @return
	 */
	public static Date add(Date date, int field, int v) {
		return add(date, field, v, c -> c.getTime());
	}

	/**
	 * calc date. <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 18.
	 * @param date
	 * @param field
	 * @param v
	 * @param convert
	 * @return
	 */
	public static <T> T add(Date date, int field, int v, Function<Calendar, T> convert) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.add(field, v);
		return convert.apply(instance);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 11. 14.
	 * @param date
	 * @return
	 */
	public static java.sql.Date toSqlDate(String date) {
		Date d = toDate(date);
		long time = -1;
		if (d != null) {
			time = d.getTime();
		}
		return new java.sql.Date(time);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 22.
	 * @param <T>
	 * @param startDate
	 * @param endDate
	 * @param dateFormat
	 * @return
	 */
	public static List<Calendar> rangeDay(String startDate, String endDate, String dateFormat) {
		return rangeDay(startDate, endDate, dateFormat, a -> a);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 22.
	 * @param <T>
	 * @param startDate
	 * @param endDate
	 * @param dateFormat
	 * @param convert
	 * @return
	 */
	public static <T> List<T> rangeDay(String startDate, String endDate, String dateFormat, Function<Calendar, T> convert) {
		Date dStart = toDate(startDate, dateFormat);
		Date dEnd = toDate(endDate, dateFormat);
		return rangeDay(dStart, dEnd, convert);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 5.
	 * @param <T>
	 * @param startDate
	 * @param endDate
	 * @param convert
	 * @return
	 */
	public static <T> List<T> rangeDay(Date startDate, Date endDate, Function<Calendar, T> convert) {
		Calendar cStart = toCalendar(startDate);
		Calendar cEnd = toCalendar(endDate);
		return rangeDay(cStart, cEnd, convert);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 5.
	 * @param <T>
	 * @param cStart
	 * @param cEnd
	 * @param convert
	 * @return
	 */
	public static <T> List<T> rangeDay(Calendar cStart, Calendar cEnd, Function<Calendar, T> convert) {
		List<T> arrayList = new ArrayList<T>();

		if (cStart.compareTo(cEnd) > 0)
			return Collections.emptyList();

		while (cStart.before(cEnd)) {
			arrayList.add(convert.apply((Calendar) cStart.clone()));
			cStart.add(Calendar.DAY_OF_MONTH, 1);
		}
		arrayList.add(convert.apply(cEnd));
		return arrayList;
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 5. 
	 * @param startDate
	 * @return
	 */
	public static List<Date> rangeDay(Date startDate) {
		Calendar cStart = toCalendar(startDate);
		Calendar cEnd = Calendar.getInstance();
		return rangeDay(cStart, cEnd, c-> c.getTime());
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 5. 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<Date> rangeDay(Date startDate, Date endDate) {
		Calendar cStart = toCalendar(startDate);
		Calendar cEnd = toCalendar(endDate);
		return rangeDay(cStart, cEnd, c-> c.getTime());
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 5. 
	 * @param <T>
	 * @param startDate
	 * @param convert
	 * @return
	 */
	public static <T> List<T> rangeDay(Date startDate, Function<Calendar, T> convert) {
		Calendar cStart = toCalendar(startDate);
		Calendar cEnd = Calendar.getInstance();
		return rangeDay(cStart, cEnd, convert);
	}
	
}
