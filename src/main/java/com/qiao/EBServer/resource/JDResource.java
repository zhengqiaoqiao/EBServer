package com.qiao.EBServer.resource;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.ware.Sku;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.jd.open.api.sdk.request.ware.SkuCustomGetRequest;
import com.jd.open.api.sdk.request.ware.WareInfoByInfoRequest;
import com.jd.open.api.sdk.request.ware.WareSkuDeleteRequest;
import com.jd.open.api.sdk.request.ware.WareSkuGetRequest;
import com.jd.open.api.sdk.request.ware.WareSkuPriceUpdateRequest;
import com.jd.open.api.sdk.request.ware.WareSkuStockUpdateRequest;
import com.jd.open.api.sdk.response.ware.SkuCustomGetResponse;
import com.jd.open.api.sdk.response.ware.WareInfoByInfoSearchResponse;
import com.jd.open.api.sdk.response.ware.WareSkuDeleteResponse;
import com.jd.open.api.sdk.response.ware.WareSkuGetResponse;
import com.jd.open.api.sdk.response.ware.WareSkuPriceUpdateResponse;
import com.jd.open.api.sdk.response.ware.WareSkuStockUpdateResponse;

/**
 * <p>Title: JDResource</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2015年11月23日
 */
@Path("jd")
public class JDResource {
	
	private static final Logger LOGGER = Logger.getLogger(JDResource.class);
	
	private final String serverUrl = "http://gw.api.360buy.com/routerjson";
	private String accessToken="661f08b6-cbf0-416c-aeeb-211c04fc3edd";
	private String appKey="646070804302357494BC7B7B2EC32095";
	private String appSecret="b393064516524b91baab90c36eaebd6f";
	
	private JdClient getClient(String accessToken, String appKey, String appSecret){
		JdClient client=null; 
		if(accessToken==null||accessToken.equals(""))
			accessToken = this.accessToken;
		if(appKey==null||appKey.equals(""))
			appKey = this.appKey;
		if(appSecret==null||appSecret.equals(""))
			appSecret = this.appSecret;
		client=new DefaultJdClient(serverUrl,accessToken,appKey,appSecret); 
		return client;
	}
	
	//检索商品
	@Path("/ware/list")
	@GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Ware> searchWare(@QueryParam("accessToken") final String token, 
			@QueryParam("appKey") final String key, 
			@QueryParam("appSecret") final String secret, 
			@QueryParam("page") final String page,
			@QueryParam("pagesize") final String pagesize,
			@QueryParam("st") final String st,
			@QueryParam("et") final String et){
		List<Ware> list = null;
		WareInfoByInfoRequest request=new WareInfoByInfoRequest();
		request.setPage(page);
		request.setPageSize(pagesize);
		if(st!=null)
			request.setStartTime(st);
		if(et!=null)
			request.setEndTime(et);

		WareInfoByInfoSearchResponse response = null;
		try {
			JdClient client = getClient(token, key, secret);
			response = client.execute(request);
		} catch (JdException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getErrMsg());
			
		}

		if(response!=null&&response.getCode().equals("0")){
			list = response.getWareInfos();
			LOGGER.info("商品总数：" + response.getTotal());
		}else{
			LOGGER.warn(response.getMsg());
		}
		return list;
	}

	//根据skuid/outid获取sku信息
	@Path("/sku/get")
	@GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Sku getSkuByOutid(@QueryParam("accessToken") final String token, 
			@QueryParam("appKey") final String key, 
			@QueryParam("appSecret") final String secret, 
			@QueryParam("skuid") final String skuid,
			@QueryParam("outid") final String outid){
		Sku sku = null;
		if(skuid!=null&&!skuid.equals("")){
			WareSkuGetRequest request=new WareSkuGetRequest();
			request.setSkuId(skuid);
			WareSkuGetResponse response = null;
			try {
				JdClient client = getClient(token, key, secret);
				response = client.execute(request);
			} catch (JdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(response!=null&&response.getCode().equals("0")){
				sku = response.getSku();
			}else{
				System.out.println(response.getMsg());
			}
		}else if(outid!=null&&!outid.equals("")){
			SkuCustomGetRequest request=new SkuCustomGetRequest();
			request.setOuterId(outid);
			SkuCustomGetResponse response = null;
			try {
				JdClient client = getClient(token, key, secret);
				response = client.execute(request);
			} catch (JdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(response!=null&&response.getCode().equals("0")){
				sku = response.getSku();
			}else{
				System.out.println(response.getMsg());
			}
		}
		
		return sku;
	}
	
	//根据skuid删除sku信息
	@Path("/sku/delete")
	@DELETE
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public boolean deleteSku(@QueryParam("accessToken") final String token, 
			@QueryParam("appKey") final String key, 
			@QueryParam("appSecret") final String secret, 
			@QueryParam("skuid") final String skuid){
		boolean isSuccess = false;
		WareSkuDeleteRequest request=new WareSkuDeleteRequest();
		request.setSkuId(skuid);
		WareSkuDeleteResponse response = null;
		try {
			JdClient client = getClient(token, key, secret);
			response = client.execute(request);
		} catch (JdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(response!=null&&response.getCode().equals("0")){
			isSuccess = true;
		}else{
			isSuccess = false;
			System.out.println(response.getMsg());
		}
		return isSuccess;
	}
	
	//根据skuid/outid更新sku的价格
	@Path("/sku/update-price")
	@PUT
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public boolean updateSkuPriceBySkyidOrOutid(@QueryParam("accessToken") final String token, 
			@QueryParam("appKey") final String key, 
			@QueryParam("appSecret") final String secret, 
			@QueryParam("skuid") final String skuid, 
			@QueryParam("outid") final String outid,
			@QueryParam("price") final String price){
		boolean isSuccess = false;
		
		WareSkuPriceUpdateRequest request=new WareSkuPriceUpdateRequest();

		request.setSkuId(skuid);
		request.setOuterId(outid);
		request.setJdPrice(price);

		WareSkuPriceUpdateResponse response = null;
		try {
			JdClient client = getClient(token, key, secret);
			response = client.execute(request);
		} catch (JdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(response!=null&&response.getCode().equals("0")){
			isSuccess = true;
		}else{
			isSuccess = false;
			System.out.println(response.getMsg());
		}
		return isSuccess;
	}
	
	//根据skuid/outid更新sku的库存
	@Path("/sku/update-stock")
	@GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public boolean updateSkuStockBySkyidOrOutid(@QueryParam("accessToken") final String token, 
			@QueryParam("appKey") final String key, 
			@QueryParam("appSecret") final String secret, 
			@QueryParam("skuid") final String skuid, 
			@QueryParam("outid") final String outid,
			@QueryParam("stock") final String num){
		boolean isSuccess = false;
		
		WareSkuStockUpdateRequest request=new WareSkuStockUpdateRequest();

		request.setSkuId(skuid);
		request.setOuterId(outid);
		request.setQuantity(num);

		WareSkuStockUpdateResponse response = null;
		try {
			JdClient client = getClient(token, key, secret);
			response = client.execute(request);
		} catch (JdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(response!=null&&response.getCode().equals("0")){
			isSuccess = true;
		}else{
			isSuccess = false;
			System.out.println(response.getMsg());
		}
		return isSuccess;
	}
	
}
