package com.qiao.EBServer.resource.yintai;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.TreeMap;

import javax.ws.rs.Path;

import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import com.qiao.EBServer.util.Base64Util;
import com.qiao.EBServer.util.DateUtil;
import com.qiao.EBServer.util.HttpUtil;
import com.qiao.EBServer.util.SecurityUtil;
import com.qiao.EBServer.util.StringUtil;


/**
 * <p>Title: YinTaiItemResource</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2015年12月1日
 */
@Path("yintai/item")
public class YinTaiItemResource {
	private static final Logger LOGGER = Logger.getLogger(YinTaiItemResource.class);
	
	private final String serverUrl = "http://api.yintai.com/openapi/service";
	private final String clientId = "101234";
	private final String clientName = "优购";
	//银泰
	private String appKey="101234";
	private String appSecret="0F8A72C2-EEA6-459B-BAAF-1A1426354A1C";
	
	/**
	 * 生成签名sign
	 * @param method
	 * @param secret
	 * @param apiPath
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private String makeSign(TreeMap<String, String> params) throws Exception {
     	Object[] keys = params.keySet().toArray();
		Arrays.sort(keys);
		StringBuilder buffer = new StringBuilder(128);
		for (int i = 0; i < keys.length; i++) {
			if(!StringUtil.isEmpty(params.get(keys[i]))){
			    buffer.append(keys[i].toString().toUpperCase()).append("=").append(getValue(encodeUrl(params.get(keys[i]))));
			}else{
				continue;
			}
			if (i != keys.length - 1) {
				buffer.append("&");
			}
		}
		 byte[] signByte=SecurityUtil.encodeHmacSHA256(buffer.toString().getBytes("UTF-8"),params.get("secrectkey"));
		 String encoded =Base64Util.encode(signByte); 
		return encoded;
		
	}
	
	/*
	 * URL编码 (符合FRC1738规范)
	 * @param input 待编码的字符串
	 * @return 编码后的字符串
	 * @throws OpenApiException 不支持指定编码时抛出异常。
	 */
	private String encodeUrl(String input) throws Exception {
		return URLEncoder.encode(input, "utf-8").replace("%2B", "%2b").replace("%2A", "%2a");
	}
	
	private String getValue(Object value) {
		if (value.getClass().isArray()) {
			String valueStr = "";
			String[] items = (String[]) value;
			Arrays.sort(items);

			for (String item : items) {
				valueStr += item;
			}
			return valueStr;
		} else {
			return value.toString();
		}
	}
	
	/**
	 * 银泰提交公共方法
	 * @param seller
	 * @param params(只需要传业务参数)
	 * @param method 方法名称
	 * @param sendMethod post/get
	 * @return
	 * @throws Exception
	 */
	public String sendYtUtil(TreeMap<String, String> params,String method,String sendMethod) {
		String resultPost = "";
		try {
			//获取验证			
			params.put("appkey", appKey);//"100007"
			params.put("secrectkey",appSecret);//"12345678"
			params.put("Timereq", DateUtil.getCurrentDate("yyyyMMddHHmmss"));//日期标记（参与签名验证格式：yyyyMMddHHmmss）
			String signs=makeSign(params);
			params.remove("appkey");
			params.remove("secrectkey");
			params.put("Content_type", "json");//参数格式（json、xml）
			params.put("sip_http_method", sendMethod);//提交方式
			params.put("Language", "Chinese");//语言
			params.put("ClientId",clientId);
			params.put("ClientName",clientName);
			params.put("signtype", "1");//0 普通排序方式	1  sort排序方式
			params.put("signMethod", "1");//0  md5 1  sha256
			params.put("Date", DateUtil.getCurrentDate("yyyyMMddHHmmss"));//提交日期（格式：yyyyMMddHHmmss））
			params.put("method", method);//方法名称
			params.put("ver", "1.0");//接口版本
			
			params.put("sign", signs);		
			if ("post".equals(sendMethod)) {
				resultPost = HttpUtil.post(serverUrl, params, HTTP.UTF_8);
			} else {
				resultPost = HttpUtil.get(serverUrl, params, HTTP.UTF_8);
			}
		} catch (Exception e) {
			
		}
		return resultPost;
	}
	
	public static void main(String[] args){
		String barcode = "4055344871997";
		YinTaiItemResource r = new YinTaiItemResource();
		TreeMap<String, String> params = new TreeMap<String, String>();
		params.put("vendorID", "101234");
		params.put("vendorItemCode", barcode);

		String msg = r.sendYtUtil(params, "Yintai.OpenApi.Vendor.GetGoodsInfoByItemCode", "post");
		System.out.println(msg);
	}
	
}
