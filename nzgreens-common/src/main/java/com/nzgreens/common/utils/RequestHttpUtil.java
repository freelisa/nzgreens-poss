package com.nzgreens.common.utils;

import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yu on 15/10/12.
 */
public class RequestHttpUtil {
	private static final Logger log = LoggerFactory.getLogger(RequestHttpUtil.class);

	public static int TIME_OUT = 15000;

	public static CloseableHttpClient getCloseableHttpClient() {
		return HttpClients.createDefault();
	}

	public static RequestConfig getRequestConfig() {
		return RequestConfig.custom()
				.setSocketTimeout(TIME_OUT)
				.setConnectTimeout(TIME_OUT)
				.setConnectionRequestTimeout(TIME_OUT)
				.setCookieSpec(CookieSpecs.STANDARD_STRICT)
				.build();

	}

	public static RequestConfig getRequestConfig(int timeOut) {
		return RequestConfig.custom()
				.setSocketTimeout(timeOut)
				.setConnectTimeout(timeOut)
				.setConnectionRequestTimeout(timeOut)
				.setCookieSpec(CookieSpecs.STANDARD_STRICT)
				.build();

	}

	public static String getContent(String urlPath) {
		return getContent(urlPath, "UTF-8");

	}

	public static String getContent(String urlPath, int timeOut) {
		return getContent(urlPath, null, "UTF-8", timeOut);

	}

	public static String getContent(String urlPath, Header[] headers) {
		return getContent(urlPath, headers, "UTF-8", TIME_OUT);
	}


	public static String getContent(String urlPath, String charsetName) {
		return getContent(urlPath, null, charsetName, TIME_OUT);
	}

	public static String getContent(String urlPath, Header[] headers, String charsetName, int timeOut) {
		CloseableHttpClient client = RequestHttpUtil.getCloseableHttpClient();
		HttpGet httpGet = RequestHttpUtil.getHttpGet(urlPath, timeOut);
		setRequestHeader(httpGet);
		httpGet.setHeaders(headers);
		String result = null;
		try {
			HttpResponse response = client.execute(httpGet);
			//返回状态 不等于 200
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				log.warn(urlPath + ": response http status code " + response.getStatusLine().getStatusCode());
				return null;
			} else {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, charsetName);
				EntityUtils.consume(entity);
			}

		} catch (IOException e) {
			log.error(urlPath + " get content was error!", e);
		} finally {
			if (httpGet != null) {
				httpGet.releaseConnection();
			}
			try {
				client.close();
			} catch (IOException e) {

			}
		}
		return result;
	}


	public static HttpPost getHttpPost(String httpAddress) {
		HttpPost post = new HttpPost(httpAddress);
		post.setConfig(getRequestConfig());
		return post;
	}


	public static HttpPost getHttpPost(String httpAddress, int timeOut) {
		HttpPost post = new HttpPost(httpAddress);
		post.setConfig(getRequestConfig(timeOut));
		return post;
	}

	public static String requestPost(String url, Map<String, String> params, int timeOut) throws IOException {
		List<NameValuePair> pairs = new ArrayList<>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return requestPost(url, pairs, null, timeOut);
	}

	public static String requestPost(String url, List<NameValuePair> params, int timeOut) throws IOException {

		return requestPost(url, params, null, timeOut);
	}


	public static String requestPost(String url, List<NameValuePair> params, Header[] headers, int timeOut) throws
			IOException {

		return requestPost(url, params, headers, "UTF-8", timeOut);
	}


	public static String requestPost(String url, List<NameValuePair> params, Header[] headers, String charsetName, int
			timeOut) throws IOException {

		HttpPost method = getHttpPost(url, timeOut);

		if (headers != null) {
			method.setHeaders(headers);
		}

		method.setEntity(new UrlEncodedFormEntity(params, charsetName));

		CloseableHttpClient client = RequestHttpUtil.getCloseableHttpClient();

		String result = null;

		try {

			CloseableHttpResponse response = client.execute(method);

			HttpEntity entity = response.getEntity();

			result = EntityUtils.toString(entity, charsetName);

			EntityUtils.consume(entity);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
			try {
				client.close();
			} catch (IOException e) {

			}
		}
		return result;
	}


	public static String requestPost(String url, List<NameValuePair> params, Header[] headers, String charsetName) throws IOException {

		HttpPost method = getHttpPost(url);

		if (headers != null) {
			method.setHeaders(headers);
		}

		method.setEntity(new UrlEncodedFormEntity(params, charsetName));

		CloseableHttpClient client = RequestHttpUtil.getCloseableHttpClient();

		String result = null;

		try {

			CloseableHttpResponse response = client.execute(method);

			HttpEntity entity = response.getEntity();

			result = EntityUtils.toString(entity, charsetName);

			EntityUtils.consume(entity);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
			try {
				client.close();
			} catch (IOException e) {

			}
		}
		return result;
	}

	public static String requestPost(String url, List<NameValuePair> params, Header[] headers) throws IOException {

		return requestPost(url, params, headers, "UTF-8");
	}

	public static String requestPost(String url, List<NameValuePair> params) throws IOException {

		return requestPost(url, params, null);
	}


	public static HttpGet getHttpGet(String httpAddress) {
		HttpGet get = new HttpGet(httpAddress);
		get.setConfig(getRequestConfig());
		return get;
	}

	public static HttpGet getHttpGet(String httpAddress, int timeOut) {
		HttpGet get = new HttpGet(httpAddress);
		get.setConfig(getRequestConfig(timeOut));
		return get;
	}


	public static void setRequestHeader(HttpRequestBase request) {
		if (request != null) {
			request.setHeader("Connection", "keep-alive");
			request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			request.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
			request.setHeader("Cache-Control", "max-age=0");
			request.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
		}
	}


}
