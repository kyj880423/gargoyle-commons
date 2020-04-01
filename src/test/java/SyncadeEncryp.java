
/********************************
 *	프로젝트 : SAAudit
 *	패키지   : com.kyj.fx.sa.audit.util
 *	작성일   : 2018. 5. 11.
 *	작성자   : KYJ
 *******************************/

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author KYJ
 *
 */
public class SyncadeEncryp {

	public static void main(String[] args) throws Exception {
		String syncadeDecryptString = encryptString("sa", "zeppelin");
		System.out.println(syncadeDecryptString);
		syncadeDecryptString = decryptString(syncadeDecryptString, "zeppelin");
		System.out.println(syncadeDecryptString);
	}

	/**
	 * DMI.Core.GUI
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 27.
	 * @param DecryptStr
	 * @param EncryptKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptString(String Expression, String Key) throws Exception {
		String str = "";
		// int length = Key.length();
		int int32_1 = Expression.length();
		NumberFormat nf3 = new DecimalFormat("#000");
		for (int startIndex = 0; startIndex < int32_1; ++startIndex) {
			int int32_2 = Key.substring(startIndex, startIndex + 1).toCharArray()[0];
			// int int32_2 = Convert.ToInt32(Key.Substring(startIndex,
			// 1).ToCharArray(0, 1)[0]);
			char ch = Expression.toCharArray()[startIndex];
			str += nf3.format((int) ch ^ int32_2);
			// str += Convert.ToInt32((int) ch ^ int32_2).ToString("000");
		}
		return str;
	}

	public static String decryptString(String Expression, String Key) throws Exception {
		int num = 1;
		String str1 = "";
		String str2 = "";
		while (num < Expression.length()) {

			int int32 = Integer.parseInt(Expression.substring(num - 1, (num - 1 + 3)), 10);
			str1 += (char) int32;
			num += 3;
		}
		Expression = str1;
		// int length = Key.length();
		int int32_1 = Expression.length();
		for (int index = 0; index < int32_1; ++index) {
			int temp = index % Key.length();
			int int32_2 = Key.substring(temp, temp + 1).toCharArray()[0];
			char ch = Expression.toCharArray()[index];
			str2 += (char) (ch ^ int32_2);
		}
		return str2;
	}

}
