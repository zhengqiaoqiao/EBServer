package com.qiao.EBServer.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
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
	
	/**
	 * HTTP通过get请求
	 * @param url  
	 * 				http://www.baidu.com    
	 * 				http://www.baidu.com?id=1&name=a……
	 * @param params
	 * 				无参数设为null
	 * @param charset
	 * 				默认编码为UTF-8
	 * @return
	 */
	public static String get(String url, Map<String, String> params, String charset){
		String msg = null;
		HttpClient httpclient = getHttpClient();  
		try {
			HttpGet get = buildHttpGet(url, params, charset);
			msg = invoke(httpclient, get);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			httpclient.getConnectionManager().shutdown();  
		}
		return msg;
	}
	
	
	/**
	 * HTTP通过POST请求
	 * @param url
	 * @param params
	 * @param charset
	 * 			默认编码为UTF-8
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String post(String url, Map<String, String> params, String charset) throws UnsupportedEncodingException {  
		HttpClient httpclient = getHttpClient();  
        String msg = null;  
          
        HttpPost post = null;
		try {
			post = buildHttpPost(url, params, charset);
			msg = invoke(httpclient, post);  
	       
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			 httpclient.getConnectionManager().shutdown();  
		}
        return msg;   
	} 
	
	/**
	 * 执行http请求
	 * @param httpclient
	 * @param hur
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * 
	 *
	 */
	private static String invoke(HttpClient httpclient,  
            HttpUriRequest hur) throws ClientProtocolException, IOException {
		HttpResponse response = null; 
		String msg = null;
        response = httpclient.execute(hur);  
		HttpEntity entity = response.getEntity();    
        String charset = EntityUtils.getContentCharSet(entity);  
        msg = EntityUtils.toString(entity, charset);  
  
        return msg;  
    }  
	
	/**
	 * 构造POST请求对象
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
    private static HttpPost buildHttpPost(String url, Map<String, String> params, String charset) throws UnsupportedEncodingException{  
        HttpPost httpost = new HttpPost(url); 
        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
        if(params!=null){
            Set<String> keySet = params.keySet();  
            for(String key : keySet) {  
                nvps.add(new BasicNameValuePair(key, params.get(key)));  
            }  
        }
        httpost.setEntity(new UrlEncodedFormEntity(nvps, charset));  
        return httpost;  
    }  
    
    /**
     * 构造GET请求对象
     * @param params
     * @param charset
     * @return
     * @throws IOException
     */
    private static HttpGet buildHttpGet(String strUrl, Map<String, String> params, String charset) throws IOException {
    	HttpGet get = null; 
    	if(StringUtil.isEmpty(charset)){
    		charset = "UTF-8";
    	}
    	URL url = new URL(strUrl);
    	StringBuffer sb = new StringBuffer();
		if (StringUtil.isEmpty(url.getQuery())) {
			if (strUrl.endsWith("?")) {
				sb.append(strUrl);
			} else {
				sb.append(strUrl);
				sb.append("?");
			}
		} else {
			if (strUrl.endsWith("&")) {
				sb.append(strUrl);
			} else {
				sb.append(strUrl);
				sb.append("&");
			}
		}
    	if (params != null && params.isEmpty()) {
			Set<Entry<String, String>> entries = params.entrySet();
			boolean hasParam = false;
			for (Entry<String, String> entry : entries) {
				String name = entry.getKey();
				String value = entry.getValue();
				// 忽略参数名或参数值为空的参数
				if (!StringUtil.isEmpty(name)&&!StringUtil.isEmpty(value)) {
					if (hasParam) {
						sb.append("&");
					} else {
						hasParam = true;
					}
					sb.append(name).append("=").append(URLEncoder.encode(value, charset));
				}
			}
		}
    	String furl = sb.toString();
		if(furl.endsWith("&")){
			furl = furl.substring(0, furl.length()-1);
		}
		get = new HttpGet(furl);  
		System.out.println(furl);
		return get;
	}
    
    
    
    public static void main (String[] args){
    	String url = "http://www.baidu.com?i=1";
    	Map<String, String> params = null;
    	try {
			buildHttpGet(url,params,null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
