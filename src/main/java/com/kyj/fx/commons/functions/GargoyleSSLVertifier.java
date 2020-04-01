/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.functions
 *	작성일   : 2018. 5. 13.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.functions;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.stream.Stream;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KYJ
 *
 */
public class GargoyleSSLVertifier {

	private static Logger LOGGER = LoggerFactory.getLogger(GargoyleSSLVertifier.class);

	private SSLContext ctx;

	public void setup() {
		try {
			ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
			SSLContext.setDefault(ctx);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static GargoyleSSLVertifier gargoyleSSLVertifier = new GargoyleSSLVertifier();
	static {
		gargoyleSSLVertifier.setup();
	}

	public static final SSLContext defaultContext() {
		return gargoyleSSLVertifier.getSSLContext();
	}

	public SSLContext getSSLContext() {
		return ctx;
	}

	/**
	 *
	 * SSL 통신 인증
	 *
	 * @author KYJ
	 *
	 */
	private static class DefaultTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			// LOGGER.debug("######################");
			// LOGGER.debug("checkClientTrusted");
			LOGGER.debug(arg1);
			// LOGGER.debug("######################");
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			// LOGGER.debug("########################################################################################");
			// LOGGER.debug("checkServerTrusted");
			// LOGGER.debug(arg1);

			boolean present = Stream.of(arg0).filter(v -> {

				switch (v.getSigAlgName()) {
				case "SHA256withRSA":
					return true;
				case "SHA384withECDSA":
					return true;
				case "SHA384withRSA":
					return true;
				case "SHA1withRSA":
					return true;
				}

				return false;
			}).findFirst().isPresent();

			if (!present) {
				LOGGER.debug("Can't not found Truested Algorisms ");
				Stream.of(arg0).forEach(v -> LOGGER.warn(v.getSigAlgName()));
				throw new CertificateException();
			}

			// LOGGER.debug("########################################################################################");
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

}
