/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.suppliers
 *	작성일    : 2016. 6. 10.
  *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.http.HttpHost;

import com.kyj.fx.commons.memory.ConfigResourceLoader;
import com.kyj.fx.commons.memory.ResourceLoader;

/**
 * 
 * 네트워크 처리와 관련된 유틸리티 클래스 <br/>
 * 
 * @author KYJ
 *
 */
/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public final class NetworkUtil {

	private NetworkUtil() {
	}

	/**
	 * host address정보로 변환해주는 펑션인터페이스
	 *
	 * @최초생성일 2016. 6. 10.
	 */
	private static Function<InetAddress, String> hostAddressConverter = new Function<InetAddress, String>() {

		@Override
		public String apply(InetAddress t) {
			return t.getHostAddress();
		}
	};

	private static Predicate<String> CONTINUE = r -> true;

	/**
	 * InetAddress클래스로부터 조건에 일치하는 정보를 리턴. </br>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 10.
	 * @param convertValue
	 * @return
	 * @throws SocketException
	 */
	private static String getAddressInfoFindOne(Function<InetAddress, String> convertValue) throws SocketException {
		return getAddressInfoFindOne(convertValue, false);
	}
	
	private static String addrCache;
	private static String getAddressInfoFindOne(Function<InetAddress, String> convertValue, boolean cache) throws SocketException {
		if(cache)
		{
			if(addrCache!=null && !addrCache.isEmpty())
				return addrCache;
		}
		
		List<String> addressInfoFindAll = getAddressInfoFindAll(convertValue, v -> false);

		if (addressInfoFindAll.isEmpty())
			return "";

		return addrCache = addressInfoFindAll.get(0);
	}

	/**
	 * InetAddress클래스로부터 조건에 일치하는 정보를 리턴. </br>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 10.
	 * @param convertValue
	 * @return
	 * @throws SocketException
	 */
	private static List<String> getAddressInfoFindAll(Function<InetAddress, String> convertValue, Predicate<String> doBreak)
			throws SocketException {

		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		List<String> findAll = new ArrayList<>();

		END: while (networkInterfaces.hasMoreElements()) {
			NetworkInterface nextElement = networkInterfaces.nextElement();

			if (nextElement.isVirtual())
				continue;

			if (nextElement.isLoopback())
				continue;

			if (!nextElement.isUp())
				continue;

			Enumeration<InetAddress> inetAddresses = nextElement.getInetAddresses();
			while (inetAddresses.hasMoreElements()) {
				InetAddress netAddress = inetAddresses.nextElement();

				if (!netAddress.isLoopbackAddress() && !netAddress.isLinkLocalAddress() && netAddress.isSiteLocalAddress()) {
					String hostAddress = netAddress.getHostAddress();
					if (hostAddress.startsWith("192.168"))
						continue;

					String val = convertValue.apply(netAddress);
					findAll.add(val);
					if (doBreak.test(val))
						break END;
				}
			}
		}

		return findAll;
	}

	/**
	 * 장비 IP 주소리턴. </br>
	 * 127.0 으로 시작하거나 192.168로 시작하는 IP주소 대상아님.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 10.
	 * @return
	 * @throws SocketException
	 */
	public static String getRealAddress() throws SocketException {
		return getAddressInfoFindOne(hostAddressConverter);
	}
	
	/**
	 * 장비 IP 주소리턴. </br>
	 * 127.0 으로 시작하거나 192.168로 시작하는 IP주소 대상아님.
	 * 
	 * 한번 읽어온적이 있는경우 cache를 이용. <
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 11. 
	 * @param cache
	 * @return
	 * @throws SocketException
	 */
	public static String getRealAddress(boolean cache) throws SocketException {
		return getAddressInfoFindOne(hostAddressConverter, cache);
	}

	/**
	 * 장비 IP 주소리턴. </br>
	 * 127.0 으로 시작하거나 192.168로 시작하는 IP주소 대상아님.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 10.
	 * @return
	 * @throws SocketException
	 */
	public static List<String> getAllRealAddress() throws SocketException {
		return getAddressInfoFindAll(hostAddressConverter, CONTINUE);
	}

	/**
	 * Proxy port 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 17.
	 * @return
	 */
	public static final String getHttpProxyPort() {
		return ResourceLoader.getInstance().get(ResourceLoader.HTTP_PROXY_PORT);
	}

	/**
	 * Proxy Host 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 17.
	 * @return
	 */
	public static final String getHttpHost() {
		return ResourceLoader.getInstance().get(ResourceLoader.HTTP_PROXY_HOST);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 17.
	 * @return
	 */
	public static final HttpHost getProxyHost() {
		HttpHost proxy = new HttpHost(getHttpHost(), Integer.parseInt(getHttpProxyPort(), 10), "http");
		return proxy;
	}

	/**
	 * htpps 프록시 포트 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 17.
	 * @return
	 */
	public static final String getHttpsProxyPort() {
		return ResourceLoader.getInstance().get(ResourceLoader.HTTPS_PROXY_PORT);
	}

	/**
	 * https 프록시 호스트 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 17.
	 * @return
	 */
	public static final String getHttpsProxyHost() {
		return ResourceLoader.getInstance().get(ResourceLoader.HTTPS_PROXY_HOST);
	}

	/**
	 * 프록시 사용 여부를 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 17.
	 * @return
	 */
	public static final boolean isUseProxy() {
		return "Y".equals(ResourceLoader.getInstance().get(ConfigResourceLoader.USE_PROXY_YN));
		// return
		// "Y".equals(ConfigResourceLoader.getInstance().get(ConfigResourceLoader.USE_PROXY_YN));
	}

	// public static List<> getMac() {
	// try {
	// Enumeration<NetworkInterface> networkInterfaces =
	// NetworkInterface.getNetworkInterfaces();
	// while(networkInterfaces.hasMoreElements())
	// {
	// NetworkInterface nextElement = networkInterfaces.nextElement();
	// byte[] hardwareAddress = nextElement.getHardwareAddress();
	// String md5DigestAsHex = DigestUtils.md5DigestAsHex(hardwareAddress);
	// System.out.println(md5DigestAsHex);
	// }
	// } catch (SocketException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
}
