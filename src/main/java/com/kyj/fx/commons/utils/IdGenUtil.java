/**
 *
 */
package com.kyj.fx.commons.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.util.Calendar;

/**
 * MAC(12)-JVM(6)-DATETIME(12)-SEQ(3) 형태의 Unique한 Id를 생성. DATETIME(1000분의1초까지의
 * 값), JVM, SEQ 값들은 36진수 값으로 표현.
 *
 * @name_ko 아이디 생성 유틸 클래스
 * @author WiseBell
 */
public class IdGenUtil {
	private static short counter = (short) 0;
	private static final int JVM = (int) (System.currentTimeMillis() >>> 8);
	private static String MACHINE = "";
	private static boolean initiaized = false;

	// static {
	// StringBuffer buf = new StringBuffer();
	// try {
	// InetAddress ia = InetAddress.getLocalHost();
	// NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
	// byte[] mac = ni.getHardwareAddress();
	//
	// for (int i = 0; i < 6; i++) {
	// buf.append(String.format("%02x", mac[i]));
	// }
	// } catch (Exception e) {
	// buf = new StringBuffer("000000000000");
	// }
	//
	// MACHINE = buf.toString();
	// }

	private static void initialize() {
		if (initiaized) {
			return;
		}
		synchronized (IdGenUtil.class) {
			StringBuffer buf = new StringBuffer();
			try {
				InetAddress ia = InetAddress.getLocalHost();
				NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
				byte[] mac = ni.getHardwareAddress();

				for (int i = 0; i < 6; i++) {
					buf.append(String.format("%02x", mac[i]));
				}
			} catch (Exception e) {
				buf = new StringBuffer("000000000000");
			}

			MACHINE = buf.toString();
			initiaized = true;
		}
	}

	/*
	 * private static final int IP; static { int ipadd; try { ipadd = toInt(
	 * InetAddress.getLocalHost( ).getAddress( ) ); } catch ( Exception e ) {
	 * ipadd = 0; } IP = ipadd; }
	 *
	 * private static final long MAC; static { long macadd; try { InetAddress ia
	 * = InetAddress.getLocalHost( ); NetworkInterface ni =
	 * NetworkInterface.getByInetAddress( ia ); macadd = toLong(
	 * ni.getHardwareAddress( ) ); } catch ( Exception e ) { macadd = 0; } MAC =
	 * macadd; }
	 *
	 * /* private static int getIP( ) { return IP; }
	 *
	 * private static int toInt( byte[ ] bytes ) { int result = 0; for ( int i =
	 * 0; i < 4; i++ ) { result = ( result << 8 ) - Byte.MIN_VALUE + (int)
	 * bytes[ i ]; } return result; }
	 *
	 * private static long toLong( byte[ ] bytes ) { long result = 0; for ( int
	 * i = 0; i < 6; i++ ) { result = ( result << 8 ) - Byte.MIN_VALUE + (int)
	 * bytes[ i ]; } return result; }
	 */

	/**
	 * Unique across JVMs on this machine (unless they load this class in the
	 * same quater second - very unlikely)
	 */
	private static int getJVM() {
		return JVM;
	}

	/**
	 * Unique in a millisecond for this JVM instance (unless there are >
	 * Short.MAX_VALUE instances created in a millisecond)
	 */
	public static short getCount() {
		synchronized (IdGenUtil.class) {
			if (counter < 0) {
				counter = 0;
			}

			return counter++;
		}
	}

	/**
	 * Unique in a network
	 */
	public static String getMachine() {
		initialize();
		return MACHINE;
	}

	public static String format00(int intValue) {
		String formatted = NumeralConverter.to36(intValue);
		StringBuffer buf = new StringBuffer("00");
		buf.replace(2 - formatted.length(), 2, formatted);
		return buf.toString();
	}

	public static String format000(int intValue) {
		String formatted = NumeralConverter.to36(intValue);
		StringBuffer buf = new StringBuffer("000");
		buf.replace(3 - formatted.length(), 3, formatted);
		return buf.toString();
	}

	public static String format(int intValue) {
		String formatted = NumeralConverter.to36(intValue);
		StringBuffer buf = new StringBuffer("000000");
		buf.replace(6 - formatted.length(), 6, formatted);
		return buf.toString();
	}

	public static String format(short shortValue) {
		String formatted = NumeralConverter.to36(shortValue);
		StringBuffer buf = new StringBuffer("000");
		buf.replace(3 - formatted.length(), 3, formatted);
		return buf.toString();
	}

	public static String getTimestamp17() {
		String formatted = DateUtil.getCurrentDateString(DateUtil.SYSTEM_DATEFORMAT_YYYYMMDDHHMMSSS);
		StringBuffer buf = new StringBuffer("00000000000000000");
		buf.replace(0, formatted.length(), formatted);
		return buf.toString();
	}

	public static String getTimestamp() {
		Calendar today = Calendar.getInstance();
		StringBuffer buf = new StringBuffer();
		buf.append(format000(today.get(Calendar.YEAR)));
		buf.append(NumeralConverter.to(today.get(Calendar.MONTH) + 1, 36));
		buf.append(NumeralConverter.to(today.get(Calendar.DAY_OF_MONTH), 36));
		buf.append(NumeralConverter.to(today.get(Calendar.HOUR_OF_DAY), 36));
		buf.append(format00(today.get(Calendar.MINUTE)));
		buf.append(format00(today.get(Calendar.SECOND)));
		buf.append(format00(today.get(Calendar.MILLISECOND)));
		return buf.toString();
	}

	public static String generate() {
		StringBuffer buf = new StringBuffer(33);
		buf.append(getMachine());
		buf.append(format(getJVM()));
		buf.append(getTimestamp());
		buf.append(format(getCount()));

		return buf.toString().toLowerCase();
	}

	public static String generate(char seperator) {
		StringBuffer buf = new StringBuffer(36);
		buf.append(getMachine()).append(seperator);
		buf.append(format(getJVM())).append(seperator);
		buf.append(getTimestamp()).append(seperator);
		buf.append(format(getCount()));

		return buf.toString().toLowerCase();
	}

	public static String generateRand36(int length) {
		StringBuffer buf = new StringBuffer();
		SecureRandom rand = new SecureRandom();
		for (int i = 0; i < length; i++) {
			buf.append(NumeralConverter.to36(rand.nextInt(36)));
		}
		return buf.toString();
	}
}
