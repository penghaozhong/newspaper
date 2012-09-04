
package com.kids21.app.newpaper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.Properties;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Configuration {
	private static Properties defaultProperty;

	static {
		init();
	}

	/* package */static void init() {
		defaultProperty = new Properties();
		//defaultProperty.setProperty("kids21droid.debug", "false");
		defaultProperty.setProperty("kids21droid.debug", "true");
		defaultProperty.setProperty("kids21droid.source", "kids21droid");
		// defaultProperty.setProperty("kids21droid.clientVersion","");
		defaultProperty.setProperty("kids21droid.clientURL",
				"http://sandin.tk/kids21droid.xml");
		defaultProperty.setProperty("kids21droid.http.userAgent",
				"kids21droid 1.0");
		// defaultProperty.setProperty("kids21droid.user","");
		// defaultProperty.setProperty("kids21droid.password","");
		defaultProperty.setProperty("kids21droid.http.useSSL", "false");
		// defaultProperty.setProperty("kids21droid.http.proxyHost","");
		defaultProperty.setProperty("kids21droid.http.proxyHost.fallback",
				"http.proxyHost");
		// defaultProperty.setProperty("kids21droid.http.proxyUser","");
		// defaultProperty.setProperty("kids21droid.http.proxyPassword","");
		// defaultProperty.setProperty("kids21droid.http.proxyPort","");
		defaultProperty.setProperty("kids21droid.http.proxyPort.fallback",
				"http.proxyPort");
		defaultProperty.setProperty("kids21droid.http.connectionTimeout",
				"20000");
		defaultProperty.setProperty("kids21droid.http.readTimeout", "120000");
		defaultProperty.setProperty("kids21droid.http.retryCount", "3");
		defaultProperty.setProperty("kids21droid.http.retryIntervalSecs", "10");
		// defaultProperty.setProperty("kids21droid.oauth.consumerKey","");
		// defaultProperty.setProperty("kids21droid.oauth.consumerSecret","");
		defaultProperty.setProperty("kids21droid.async.numThreads", "1");
		defaultProperty.setProperty("kids21droid.clientVersion", "1.0");
		try {
			// Android platform should have dalvik.system.VMRuntime in the
			// classpath.
			// @see
			// http://developer.android.com/reference/dalvik/system/VMRuntime.html
			Class.forName("dalvik.system.VMRuntime");
			defaultProperty.setProperty("kids21droid.dalvik", "true");
		} catch (ClassNotFoundException cnfe) {
			defaultProperty.setProperty("kids21droid.dalvik", "false");
		}
		DALVIK = getBoolean("kids21droid.dalvik");
		String t4jProps = "kids21droid.properties";
		boolean loaded = loadProperties(defaultProperty, "."
				+ File.separatorChar + t4jProps)
				|| loadProperties(
						defaultProperty,
						Configuration.class.getResourceAsStream("/WEB-INF/"
								+ t4jProps))
				|| loadProperties(defaultProperty,
						Configuration.class.getResourceAsStream("/" + t4jProps));
	}

	private static boolean loadProperties(Properties props, String path) {
		try {
			File file = new File(path);
			if (file.exists() && file.isFile()) {
				props.load(new FileInputStream(file));
				return true;
			}
		} catch (Exception ignore) {
		}
		return false;
	}

	private static boolean loadProperties(Properties props, InputStream is) {
		try {
			props.load(is);
			return true;
		} catch (Exception ignore) {
		}
		return false;
	}

	private static boolean DALVIK;

	public static boolean isDalvik() {
		return DALVIK;
	}

	public static boolean useSSL() {
		return getBoolean("kids21droid.http.useSSL");
	}

	public static String getScheme() {
		return useSSL() ? "https://" : "http://";
	}

	public static String getCilentVersion() {
		return getProperty("kids21droid.clientVersion");
	}

	public static String getCilentVersion(String clientVersion) {
		return getProperty("kids21droid.clientVersion", clientVersion);
	}

	public static String getSource() {
		return getProperty("kids21droid.source");
	}

	public static String getSource(String source) {
		return getProperty("kids21droid.source", source);
	}

	public static String getProxyHost() {
		return getProperty("kids21droid.http.proxyHost");
	}

	public static String getProxyHost(String proxyHost) {
		return getProperty("kids21droid.http.proxyHost", proxyHost);
	}

	public static String getProxyUser() {
		return getProperty("kids21droid.http.proxyUser");
	}

	public static String getProxyUser(String user) {
		return getProperty("kids21droid.http.proxyUser", user);
	}

	public static String getClientURL() {
		return getProperty("kids21droid.clientURL");
	}

	public static String getClientURL(String clientURL) {
		return getProperty("kids21droid.clientURL", clientURL);
	}

	public static String getProxyPassword() {
		return getProperty("kids21droid.http.proxyPassword");
	}

	public static String getProxyPassword(String password) {
		return getProperty("kids21droid.http.proxyPassword", password);
	}

	public static int getProxyPort() {
		return getIntProperty("kids21droid.http.proxyPort");
	}

	public static int getProxyPort(int port) {
		return getIntProperty("kids21droid.http.proxyPort", port);
	}

	public static int getConnectionTimeout() {
		return getIntProperty("kids21droid.http.connectionTimeout");
	}

	public static int getConnectionTimeout(int connectionTimeout) {
		return getIntProperty("kids21droid.http.connectionTimeout",
				connectionTimeout);
	}

	public static int getReadTimeout() {
		return getIntProperty("kids21droid.http.readTimeout");
	}

	public static int getReadTimeout(int readTimeout) {
		return getIntProperty("kids21droid.http.readTimeout", readTimeout);
	}

	public static int getRetryCount() {
		return getIntProperty("kids21droid.http.retryCount");
	}

	public static int getRetryCount(int retryCount) {
		return getIntProperty("kids21droid.http.retryCount", retryCount);
	}

	public static int getRetryIntervalSecs() {
		return getIntProperty("kids21droid.http.retryIntervalSecs");
	}

	public static int getRetryIntervalSecs(int retryIntervalSecs) {
		return getIntProperty("kids21droid.http.retryIntervalSecs",
				retryIntervalSecs);
	}

	public static String getUser() {
		return getProperty("kids21droid.user");
	}

	public static String getUser(String userId) {
		return getProperty("kids21droid.user", userId);
	}

	public static String getPassword() {
		return getProperty("kids21droid.password");
	}

	public static String getPassword(String password) {
		return getProperty("kids21droid.password", password);
	}

	public static String getUserAgent() {
		return getProperty("kids21droid.http.userAgent");
	}

	public static String getUserAgent(String userAgent) {
		return getProperty("kids21droid.http.userAgent", userAgent);
	}

	public static String getOAuthConsumerKey() {
		return getProperty("kids21droid.oauth.consumerKey");
	}

	public static String getOAuthConsumerKey(String consumerKey) {
		return getProperty("kids21droid.oauth.consumerKey", consumerKey);
	}

	public static String getOAuthConsumerSecret() {
		return getProperty("kids21droid.oauth.consumerSecret");
	}

	public static String getOAuthConsumerSecret(String consumerSecret) {
		return getProperty("kids21droid.oauth.consumerSecret", consumerSecret);
	}

	public static boolean getBoolean(String name) {
		String value = getProperty(name);
		return Boolean.valueOf(value);
	}

	public static int getIntProperty(String name) {
		String value = getProperty(name);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public static int getIntProperty(String name, int fallbackValue) {
		String value = getProperty(name, String.valueOf(fallbackValue));
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public static long getLongProperty(String name) {
		String value = getProperty(name);
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public static String getProperty(String name) {
		return getProperty(name, null);
	}

	public static String getProperty(String name, String fallbackValue) {
		String value;
		try {
			value = System.getProperty(name, fallbackValue);
			if (null == value) {
				value = defaultProperty.getProperty(name);
			}
			if (null == value) {
				String fallback = defaultProperty.getProperty(name
						+ ".fallback");
				if (null != fallback) {
					value = System.getProperty(fallback);
				}
			}
		} catch (AccessControlException ace) {
			// Unsigned applet cannot access System properties
			value = fallbackValue;
		}
		return replace(value);
	}

	private static String replace(String value) {
		if (null == value) {
			return value;
		}
		String newValue = value;
		int openBrace = 0;
		if (-1 != (openBrace = value.indexOf("{", openBrace))) {
			int closeBrace = value.indexOf("}", openBrace);
			if (closeBrace > (openBrace + 1)) {
				String name = value.substring(openBrace + 1, closeBrace);
				if (name.length() > 0) {
					newValue = value.substring(0, openBrace)
							+ getProperty(name)
							+ value.substring(closeBrace + 1);

				}
			}
		}
		if (newValue.equals(value)) {
			return value;
		} else {
			return replace(newValue);
		}
	}

	public static int getNumberOfAsyncThreads() {
		return getIntProperty("kids21droid.async.numThreads");
	}

	public static boolean getDebug() {
		return getBoolean("kids21droid.debug");

	}
}
