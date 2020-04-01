/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.functions
 *	작성일   : 2018. 5. 13.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.functions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.ValueUtil;

/**
 * @author KYJ
 *
 */
public class GargoyleHostNameVertifier implements X509HostnameVerifier {

	private static Logger LOGGER = LoggerFactory.getLogger(GargoyleHostNameVertifier.class);

	private List<String> vanList = new ArrayList<>();

	public static final String FILE_NAME = "gargoyle.hostname.vertifier.properties";

	public void setup() {
		load();
		LOGGER.debug(getClass().getName() + "  initialize.");
		// vanList.addAll(new HostVanDQM().listVan());
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return GargoyleHostNameVertifier.this.verify(arg0, arg1);
			}
		});
	}

	private void load() {
		Properties prop = new Properties();
		File file = new File(FILE_NAME);
		if (file.exists()) {
			try (FileInputStream in = new FileInputStream(file)) {
				prop.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String strVanList = prop.getProperty("van");
		if (ValueUtil.isNotEmpty(strVanList)) {
			String[] split = strVanList.split(",");
			List<String> collect = Stream.of(split).filter(f -> ValueUtil.isNotEmpty(f)).collect(Collectors.toList());
			this.vanList.addAll(collect);
		}

	}

	private static GargoyleHostNameVertifier instnace = null;

	public static GargoyleHostNameVertifier defaultVertifier() {
		if (instnace == null)
			instnace = new GargoyleHostNameVertifier();
		return instnace;
	}

	@Override
	public boolean verify(String hostname, SSLSession session) {
		load();
		boolean flag = !vanList.contains(hostname);
		LOGGER.debug("SSLSession {} - isArrow? {} ", hostname, flag);
		return flag;
	}

	@Override
	public void verify(String host, SSLSocket ssl) throws IOException {
		LOGGER.debug("SSLSocket {}", host);
	}

	@Override
	public void verify(String host, X509Certificate cert) throws SSLException {
		LOGGER.debug("X509Certificate {}", host);

	}

	@Override
	public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
		LOGGER.debug("{}", host);

	}

}
