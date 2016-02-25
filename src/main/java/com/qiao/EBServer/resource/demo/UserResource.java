package com.qiao.EBServer.resource.demo;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.qiao.EBServer.domain.User;
import com.qiao.EBServer.service.UserService;

/**
 * <p>Title: UserResource</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2016年2月19日
 */
@Path("user")
public class UserResource {
	private final Logger LOGGER = Logger.getLogger(UserResource.class);
	@Autowired
	private UserService userService;
	
	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;
	@Context
	private ServletConfig servletConfig;
	@Context
	private ServletContext servletContext;
	@Context
	private HttpHeaders header;
	@Context
	private UriInfo info;
	
	@Path("/list-by-name")
	@GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<User> findUsersByName(@QueryParam("name") final String name){
		List<User> list = userService.findUsersByName(name);
		return list;
	}
	
	@Path("/list")
	@POST
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<User> findUsers(User user){
		List<User> list = userService.findUsers(user);
		return list;
	}
}
