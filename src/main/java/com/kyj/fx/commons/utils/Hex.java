/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 5. 1.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

/**
 * @author KYJ
 *
 */
public class Hex {

	private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static char[] encode(byte[] bytes) {
		final int nBytes = bytes.length;
		char[] result = new char[2 * nBytes];

		int j = 0;
		for (int i = 0; i < nBytes; i++) {
			// Char for top 4 bits
			result[j++] = HEX[(0xF0 & bytes[i]) >>> 4];
			// Bottom 4
			result[j++] = HEX[(0x0F & bytes[i])];
		}

		return result;
	}

	public static byte[] decode(CharSequence s) {
		int nChars = s.length();

		if (nChars % 2 != 0) {
			throw new IllegalArgumentException("Hex-encoded string must have an even number of characters");
		}

		byte[] result = new byte[nChars / 2];

		for (int i = 0; i < nChars; i += 2) {
			int msb = Character.digit(s.charAt(i), 16);
			int lsb = Character.digit(s.charAt(i + 1), 16);

			if (msb < 0 || lsb < 0) {
				throw new IllegalArgumentException("Non-hex character in input: " + s + "\nmsb : " + msb + " lsb : " + lsb + "\n [" + s.charAt(i) +  " ," + s.charAt(i+1) + "]" );
			}
			result[i / 2] = (byte) ((msb << 4) | lsb);
		}
		return result;
	}

}