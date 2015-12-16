package com.qiao.EBServer;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.qiao.EBServer.resource.taobao.TBItemResource;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.Sku;

/**
 * <p>Title: TBResourceTest</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2015年12月2日
 */
public class TBResourceTest {
	private HttpServer server;
    private WebTarget target;
    //e百丽
  	private String appKey="12038771";
  	private String appSecret="1a5476b788487f2cb21a313bfb84e2f8";
  	private String sessionKey="6100a1971c819dcd29aeb0c234b10dd41f6d795ade7afab98563612";
   
    
    private TBItemResource rs = new TBItemResource();

    @Before
    public void setUp() throws Exception {
        server = Main.startServer();
        final Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
        //e百丽
        appKey="12038771";
      	appSecret="1a5476b788487f2cb21a313bfb84e2f8";
      	sessionKey="6100a1971c819dcd29aeb0c234b10dd41f6d795ade7afab98563612";
    }

    @After
    public void tearDown() throws Exception {
        server.shutdownNow();
    }
    
    /**
     * 淘宝在售商品检索测试
     */
    @Test
    public void test1() {
		List<Item> list = rs.getOnsaleItem(appKey, appSecret,sessionKey, 1, 40, "", "");
		if(list==null)
			return;
			
		for(int i=0;i<list.size();i++){
			Item item = list.get(i);
			List<Sku> skus = rs.getSkuByItemId(appKey, appSecret,sessionKey, String.valueOf(item.getNumIid()));
			if(skus==null)
				continue;
			System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^");
			for(Sku sku:skus){
				System.out.println("=========================");
				System.out.println("outid:"+sku.getOuterId());
				System.out.println("-------------------------");
				System.out.println("skuid:"+sku.getSkuId());
				System.out.println("-------------------------");
				System.out.println("status:"+sku.getStatus());
				System.out.println("-------------------------");
				System.out.println("price:"+sku.getPrice());
				System.out.println("-------------------------");
				System.out.println("quantity:"+sku.getQuantity());
				System.out.println("-------------------------");
				System.out.println("modified:"+sku.getModified());
				System.out.println("-------------------------");
				System.out.println("create:"+sku.getCreated());
			}
			
		}
    }
}
