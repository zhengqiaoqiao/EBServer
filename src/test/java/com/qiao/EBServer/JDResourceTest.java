package com.qiao.EBServer;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jd.open.api.sdk.domain.ware.Ware;
import com.qiao.EBServer.resource.JDResource;




/**
 * <p>Title: JDResourceTest</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2015年11月23日
 */
public class JDResourceTest {
	private HttpServer server;
    private WebTarget target;
    
    private JDResource rs = new JDResource();

    @Before
    public void setUp() throws Exception {
        server = Main.startServer();
        final Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdownNow();
    }
    @Test
    public void test1() {

        final List list= target.path("jd/ware/list")
        		.queryParam("page", 1)
        		.queryParam("pagesize", 1)
        		.request().get(List.class);
        
        System.out.println(list.get(0).toString());
    }
    
    @Test
    public void test2() {
    	List<Ware> list = rs.searchWare(null, null, null, "1", "1", null, null);
       
        
        System.out.println(list.get(0).getJdPrice());
    }
}
