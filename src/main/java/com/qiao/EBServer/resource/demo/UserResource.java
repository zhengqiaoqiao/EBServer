package com.qiao.EBServer.resource.demo;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.request.ware.WareSkuStockUpdateRequest;
import com.jd.open.api.sdk.response.ware.WareSkuStockUpdateResponse;
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
	
	@Path("/list-by-name")
	@GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<User> findUsersByName(@QueryParam("name") final String name){
		List<User> list = userService.findUsersByName(name);
		return list;
	}
}
