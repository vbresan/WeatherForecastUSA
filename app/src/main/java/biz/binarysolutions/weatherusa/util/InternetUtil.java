package biz.binarysolutions.weatherusa.util;

import android.text.TextUtils;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 *
 */
public class InternetUtil {
	
	private static final int TIMEOUT = 15000;


	/**
	 *
	 * @param url
	 * @param userAgent
	 * @return
	 */
	public static String getGetResponse(String url, String userAgent) {
		
		String             response   = "";
		HttpsURLConnection connection = null;
		
		try {
			connection = (HttpsURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.setReadTimeout(TIMEOUT);
			connection.setConnectTimeout(TIMEOUT);

			if (! TextUtils.isEmpty(userAgent)) {
				connection.setRequestProperty("User-Agent", userAgent);
			}

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpsURLConnection.HTTP_OK) {
				response = StringUtil.getString(connection.getInputStream());
			}
		} catch (IOException e) {
			// do nothing
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		
		return response;
	}
}