package com.nzgreens.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

public class HttpClientUtils
{

	private static PoolingHttpClientConnectionManager connectionManager = null;
	private static HttpClientBuilder httpBulder = null;
	private static RequestConfig requestConfig = null;

	private static int MAXCONNECTION = 200;

	private static int DEFAULTMAXCONNECTION = 5;

	private static int TIME_OUT = 20000;

	static {
		//设置http的状态参数
		requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT).setConnectTimeout(TIME_OUT).setConnectionRequestTimeout(TIME_OUT).build();

		connectionManager = new PoolingHttpClientConnectionManager();
		//设置最大链接数
		connectionManager.setMaxTotal(MAXCONNECTION);
		//设置路由链接数
		connectionManager.setDefaultMaxPerRoute(DEFAULTMAXCONNECTION);

		httpBulder = HttpClients.custom();

		httpBulder.setConnectionManager(connectionManager);

	}

	public static CloseableHttpClient getConnection() {
		CloseableHttpClient httpClient = httpBulder.build();
		httpClient = httpBulder.build();
		return httpClient;
	}


	public static HttpUriRequest getRequestMethod(Map<String, String> map, String url, String method) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		Set<Map.Entry<String, String>> entrySet = map.entrySet();
		for (Map.Entry<String, String> e : entrySet) {
			String name = e.getKey();
			String value = e.getValue();
			NameValuePair pair = new BasicNameValuePair(name, value);
			params.add(pair);
		}
		HttpUriRequest reqMethod = null;
		if ("post".equals(method)) {
			reqMethod = RequestBuilder.post().setUri(url)
					.addParameters(params.toArray(new BasicNameValuePair[params.size()]))
					.setConfig(requestConfig).build();
		} else if ("get".equals(method)) {
			reqMethod = RequestBuilder.get().setUri(url)
					.addParameters(params.toArray(new BasicNameValuePair[params.size()]))
					.setConfig(requestConfig).build();
		}
		return reqMethod;
	}


	public static Map<String, String> execute(Map<String, String> map, String url, String method) {
		HttpClient client = null;
		HttpUriRequest post = null;
		HttpResponse response = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("connectionStatus", "F");
		try {
			client = HttpClientUtils.getConnection();
			post = getRequestMethod(map, url, method);
			response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity resEntity = response.getEntity();
				String message = EntityUtils.toString(resEntity, "utf-8");
				returnMap.put("connectionStatus", "T");
				returnMap.put("message", message);
			} else {
				returnMap.put("connectionStatus", "F");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


		return returnMap;
	}

	public static int getExecuteStatus(Map<String, String> map, String url, String method) {
		HttpClient client = null;
		HttpUriRequest post = null;
		HttpResponse response = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("connectionStatus", "F");
		try {
			client = HttpClientUtils.getConnection();
			post = getRequestMethod(map, url, method);
			response = client.execute(post);
			return response.getStatusLine().getStatusCode();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


		return 400;
	}

	 
    public static String executeS(Map<String, String> map, String url,String method){
    	HttpClient client = null;
        HttpUriRequest post = null;
        HttpResponse response=null;
        String result=null;
		 try {
			client = HttpClientUtils.getConnection();
			post = getRequestMethod(map, url, method);
			response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
	             HttpEntity resEntity = response.getEntity();
	             result = EntityUtils.toString(resEntity, "utf-8");
	             
	         }
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			 if(response!=null){
	         	try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					e.printStackTrace();
				}
	         }
		}
		 return result;
   }
    
	
	public static Map<String, String> executePost(Map<String, String> map, String url) {
		return execute(map, url, "post");
	}

	public static Map<String, String> executeGet(Map<String, String> map, String url) {
		return execute(map, url, "get");
	}

	public static int getStutusExecuteGet(Map<String, String> map, String url) {
		return getExecuteStatus(map, url, "get");
	}


	public static void main(String args[]) throws IOException {
		Map<String, String> map = new HashMap<>();
		map.put("userId", "1");
		map.put("password", "t7AwJCr21ELXOodTt8yWKw==");
		map.put("auth", "universal_auth");
		//for (int i = 0; i < 200; i++) {
			map.put("account", "087655");
			Map<String, String> result = HttpClientUtils.executePost(map, "https://futures-m.8caopan.com/app/future/exchange/user/login");
			if ("T".equals(result.get("connectionStatus"))) {
				JSONObject json = JSON.parseObject(result.get("message"));
				if (json.getBoolean("success")) {
					System.out.println(map.get("account"));
				}
			}

		//}

	}


	public static void reg() throws ClientProtocolException, IOException {
		String url = "http://112.74.212.112:8090/app-api/api/appAddUser/addUserRedirect.do";
		Map<String, String> map = new HashMap<String, String>();
		String p = "{\"me\":\"18016037111\",\"ps\":\"111111\",\"ck\":\"http://t.m.8caopan.com/gg/user/register/callback\",\"mid\":\"606\",\"agentMobile\":\"\"}";
		map.put("p", p);
		System.out.println(executePost(map, url));
	}

	public static void login() throws ClientProtocolException, IOException {
		String url = "http://112.74.212.112:8090/app-api/api/appLogin/appLoginRedirect.do";
		Map<String, String> map = new HashMap<String, String>();
		map.put("p", "{\"me\":\"18611112225\",\"ps\":\"111111\",\"ck\":\"http://t.m.8caopan.com/gg/user/register/callback\",\"rt\":\"http://localhost:8080/web/user/trade/hall\",\"mid\":\"606\"}");
		System.out.println(executePost(map, url));
	}

	public static void sendSms() throws ClientProtocolException, IOException {
		String url = "http://112.74.212.112:8090/app-api/api/code/getCodeNew.do";
		Map<String, String> map = new HashMap<String, String>();
		map.put("p", "{\"me\":\"18621236633\",\"mid\":\"606\",\"type\":\"UP\"}");
		System.out.println(executePost(map, url));
	}

	public static void updataPassWord() throws ClientProtocolException, IOException {
		String url = "http://112.74.212.112:8090/app-api/api/appUser/updateCustomerPwd.do";
		String p = "{\"me\":\"18621236633\",\"newPwd\":\"111111\",\"checkPwd\":\"111111\",\"code\":\"8216\",\"mid\":\"606\"}";
		Map<String, String> map = new HashMap<String, String>();
		map.put("p", p);
		System.out.println(executePost(map, url));
	}
}