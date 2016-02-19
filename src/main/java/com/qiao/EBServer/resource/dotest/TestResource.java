package com.qiao.EBServer.resource.dotest;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.qiao.EBServer.pojo.User;
import com.qiao.EBServer.service.TestService;



/**
 * <p>Title: JDResource</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2015年11月23日
 */
@Path("test")
public class TestResource {
	
	private static final Logger LOGGER = Logger.getLogger(TestResource.class);
	@Autowired
	private TestService testService;

	@Path("/one")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String one(final User user){
//		LOGGER.info("u:"+user);
		String msg = "a="+user.getA()+"&b="+user.getB();
		LOGGER.info(msg);
		return msg;
	}
	
	@Path("/two")
	@POST
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public User two(){
		User u = new User();
		u.setA("aaa");
		u.setB("bbb");
		return u;
	}
	
	@Path("/three")
	@GET
	public void three(){
		testService.service1();
	}
}
