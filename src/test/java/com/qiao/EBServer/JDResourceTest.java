package com.qiao.EBServer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.jd.open.api.sdk.domain.ware.Sku;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.qiao.EBServer.resource.jingdong.JDWareResource;
import com.qiao.EBServer.util.DateUtil;




/**
 * <p>Title: JDResourceTest</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2015年11月23日
 */
public class JDResourceTest {
	private HttpServer server;
    private WebTarget target;
    private String accessToken;
    private String appKey;
    private String appSecret;
    
    private JDWareResource rs = new JDWareResource();

    @Before
    public void setUp() throws Exception {
        server = Main.startServer();
        final Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
        //京东运动闪购店的，
        accessToken="cfb9d504-0ee6-4f85-983f-64db0ae57e2d";
    	appKey="646070804302357494BC7B7B2EC32095";
    	appSecret="b393064516524b91baab90c36eaebd6f";
    }

    @After
    public void tearDown() throws Exception {
        server.shutdownNow();
    }
    
    /**
     * 京东下架商品检索测试
     */
//    @Test
    public void test1() {
		List<Ware> list = rs.searchDelistingWare(accessToken, appKey, appSecret, "1", "1", null, null);
		for(int i=0;i<list.size();i++){
			Ware w = list.get(i);
			String wareid = w.getWareId().toString();
			System.out.println("京东商品编码："+wareid);
			List<Sku> skus = rs.getSkuByWareids(accessToken, appKey, appSecret, wareid);
			if(skus==null)
				continue;
			for(Sku sku:skus){
				String skuid = sku.getSkuId().toString();
				String outid = sku.getOuterId();

				System.out.println("---------------------");
				System.out.println("京东SKUID："+skuid);
				System.out.println("---------------------");
				System.out.println("京东外部id："+outid);
			}
			System.out.println("========================");
			
		}
    }
    /**
     * 京东sku信息测试
     */
//    @Test
    public void test2() {
    	String skuid="10046988728";
		Sku sku = rs.getSkuById(accessToken, appKey, appSecret, skuid, "");
		String wareid = String.valueOf(sku.getWareId());
		Ware ware = rs.getWareById(accessToken, appKey, appSecret, wareid);
		String createtime = sku.getCreated();
		String modifytime = sku.getModified();
		System.out.println("ware的创建时间："+ware.getCreated());
		System.out.println("---------------------");
		System.out.println("ware的修改时间："+ware.getModified());
		System.out.println("---------------------");
		//adidas阿迪三叶草2015年新款女子护腕头带指环AB2777
		System.out.println("ware的Title："+ware.getTitle());
		System.out.println("---------------------");
		System.out.println("sku的创建时间："+createtime);
		System.out.println("---------------------");
		System.out.println("sku的修改时间："+modifytime);
    }
    
    /**
     * 京东商品修改测试
     */
//    @Test
    public void test3() {
		String wareid = "10008678551";
		Ware ware = rs.getWareById(accessToken, appKey, appSecret, wareid);
//		ware.setTitle("adidas阿迪三叶草2015年新款女子护腕头带指环AB2777");
		ware.setWareStatus("ON_SALE");
    	rs.updateWare(accessToken, appKey, appSecret, ware);
    }
    
    /**
     * 京东sku信息更新测试
     */
//    @Test
    public void test4() {
		String skuid = "10046988728";
		//229.00
		rs.updateSkuPriceBySkyidOrOutid(accessToken, appKey, appSecret, skuid, "", "229");
		
    }
    
    /**
     * 京东根据商品id获取sku信息测试
     */
//    @Test
    public void test5() throws ParseException{
    	String wareid = "10008678551";
		System.out.println("京东商品编码："+wareid);
		List<Sku> skus = rs.getSkuByWareids(accessToken, appKey, appSecret, wareid);
		
		for(Sku sku:skus){
			String skuid = sku.getSkuId().toString();
			String outid = sku.getOuterId();
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.DEFAULT_FORMAT);
			Date date = simpleDateFormat.parse(sku.getModified());
			String s = DateUtil.rollMinutes(date, -60*8);
			Date d = DateUtil.StringToDate(DateUtil.rollDays(date, -8/24), DateUtil.DEFAULT_FORMAT);

			System.out.println("---------------------");
			System.out.println("京东SKUID："+skuid);
			System.out.println("---------------------");
			System.out.println("京东外部id："+outid);
			System.out.println("---------------------");
			System.out.println("sku修改时间："+sku.getModified());
			System.out.println("---------------------");
			System.out.println("sku状态："+sku.getStatus());
			System.out.println("---------------------");
			System.out.println("riqi："+s);
		}
    }
    
    /**
     * 京东商品检索测试
     */
//    @Test
    public void test6() {
		List<Ware> list = rs.searchWare(accessToken, appKey, appSecret, "1", "100", "2015-12-01 09:50:48", "2015-12-01 09:50:48");
		for(int j=0;j<list.size();j++){
			Ware w = list.get(j);
			System.out.println(w.getWareId()+"---"+w.getCreated()+"---"+w.getModified());

		}

    }
}
