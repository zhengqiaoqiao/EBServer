package com.qiao.EBServer.filter;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import org.apache.log4j.Logger;

/**
 * <p>Title: MyClientRequestFilter</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2016年2月16日
 */
public class CorsFilter implements ClientRequestFilter, ClientResponseFilter,
	ContainerRequestFilter, ContainerResponseFilter {

	private static final Logger LOGGER = Logger.getLogger(CorsFilter.class);
	//客户端请求过滤
	@Override
	public void filter(ClientRequestContext arg0) throws IOException {
		// TODO Auto-generated method stub
		LOGGER.info("客户端请求过滤");
	}
	

	//服务端请求过滤
	@Override
	public void filter(ContainerRequestContext requestCtx) throws IOException {
		// TODO Auto-generated method stub
		LOGGER.info("服务端请求过滤");
	}
	
	//服务端响应过滤
	@Override
	public void filter(ContainerRequestContext requestCtx,
			ContainerResponseContext responseCtx) throws IOException {
		// TODO Auto-generated method stub
		LOGGER.info("服务端响应过滤");
		// The following was required to permit testing outside the Application Server Container
		responseCtx.getHeaders().add("Access-Control-Allow-Origin", requestCtx.getHeaderString("Origin"));
	    responseCtx.getHeaders().add("Access-Control-Allow-Credentials", "true");
	    responseCtx.getHeaders().add("Access-Control-Allow-Methods", "OPTIONS, GET, POST, DELETE, PUT");
	    responseCtx.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Access-Control-Allow-Methods, Access-Control-Allow-Headers, Authorization, X-Requested-With");
	}
	//客户端响应过滤
	@Override
	public void filter(ClientRequestContext arg0, ClientResponseContext arg1)
			throws IOException {
		// TODO Auto-generated method stub
		LOGGER.info("客户端响应过滤");
	}

}
