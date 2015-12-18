package com.qiao.EBServer.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * <p>Title: HttpConnectionManager</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2015年12月15日
 */
public class HttpUtil { 

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
	
	public static String get(String url) {  
        DefaultHttpClient httpclient = new DefaultHttpClient();  
        String body = null;  
          
        HttpGet get = new HttpGet(url);  
        body = invoke(httpclient, get);  
          
        httpclient.getConnectionManager().shutdown();  
          
        return body;  
    }  
	
	public static String post(String url, Map<String, String> params) {  
		DefaultHttpClient httpclient = new DefaultHttpClient();  
        String body = null;  
          
        HttpPost post = postForm(url, params, HTTP.UTF_8);  
          
        body = invoke(httpclient, post);  
          
        httpclient.getConnectionManager().shutdown();  
          
        return body;   
	} 
	
	private static String invoke(DefaultHttpClient httpclient,  
            HttpUriRequest httpost) {  
          
        HttpResponse response = sendRequest(httpclient, httpost);  
        String body = null;
		try {
			body = paseResponse(response);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
          
        return body;  
    }  
	
	private static String paseResponse(HttpResponse response) throws ParseException {   
        HttpEntity entity = response.getEntity();    
        String charset = EntityUtils.getContentCharSet(entity);  
        String body = null;  
        try {  
            body = EntityUtils.toString(entity, charset);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
          
        return body;  
    }  
  
    private static HttpResponse sendRequest(DefaultHttpClient httpclient,  
            HttpUriRequest hur) {  
        HttpResponse response = null;  
          
        try {  
            response = httpclient.execute(hur);  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return response;  
    }  
    
    private static HttpPost postForm(String url, Map<String, String> params, String charset){  
        
        HttpPost httpost = new HttpPost(url); 
        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
        if(params!=null){
            Set<String> keySet = params.keySet();  
            for(String key : keySet) {  
                nvps.add(new BasicNameValuePair(key, params.get(key)));  
            }  
        }
        try {  
            httpost.setEntity(new UrlEncodedFormEntity(nvps, charset));  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        } 

        return httpost;  
    }  
    
    public static void main (String[] args){
    	String s = HttpUtil.post("http://www.baidu.com",null);
    	System.out.println(s);
    }
}
