package com.qiao.EBServer.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;






/**
 * <p>Title: HttpConnectionManager</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2015年12月15日
 */
public class HttpConnectionManager { 

	private static HttpParams httpParams;
	private static ClientConnectionManager connectionManager;

	/**
	 * 最大连接数
	 */
	public final static int MAX_TOTAL_CONNECTIONS = 800;
	/**
	 * 获取连接的最大等待时间
	 */
	public final static int WAIT_TIMEOUT = 12000000;
	/**
	 * 每个路由最大连接数
	 */
	public final static int MAX_ROUTE_CONNECTIONS = 400;
	/**
	 * 连接超时时间
	 */
	public final static int CONNECT_TIMEOUT = 2400000;
	/**
	 * 读取超时时间
	 */
	public final static int READ_TIMEOUT = 2400000;
	private static final HttpConnectionManager HttpConnManager = null;

	// 创建公用的连接池 update by zhengqiao
	static {
		httpParams = new BasicHttpParams();
		// 设置最大连接数
		ConnManagerParams.setMaxTotalConnections(httpParams,
				MAX_TOTAL_CONNECTIONS);
		// 设置获取连接的最大等待时间
		ConnManagerParams.setTimeout(httpParams, WAIT_TIMEOUT);
		// 设置每个路由最大连接数
		ConnPerRouteBean connPerRoute = new ConnPerRouteBean(
				MAX_ROUTE_CONNECTIONS);
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);
		// 设置连接超时时间
		HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
		// 设置读取超时时间
		HttpConnectionParams.setSoTimeout(httpParams, READ_TIMEOUT);
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));

		connectionManager = new ThreadSafeClientConnManager(httpParams,
				registry);
	}

	public static HttpClient getHttpClient() {
		return new DefaultHttpClient(connectionManager, httpParams);
	}
	
	public static String getHttpConn(String connUrl) {
		String msg = "";
		InputStream ins = null;
		HttpGet httpget = new HttpGet(connUrl);

		try {
			HttpClient httpClient = HttpConnManager.getHttpClient();
			HttpResponse response = httpClient.execute(httpget);
			if (response.getStatusLine().getStatusCode() != 200) {
				httpget.abort();
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				ins = entity.getContent();
				msg = ComTools.recvStringFromStream(ins);

			}
			return msg;
		} catch (Exception ex) {
			System.out.println(ex.toString());
			httpget.abort();
			ex.printStackTrace();
			return null;
		}
		 finally {
			 if (ins != null) {
				 try {
					 ins.close();
				 } catch (IOException e) {
			 // TODO Auto-generated catch block
					 e.printStackTrace();
				 }
			 }
		 }

	}

}
