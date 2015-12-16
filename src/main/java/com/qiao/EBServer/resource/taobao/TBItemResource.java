package com.qiao.EBServer.resource.taobao;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.Sku;
import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.request.ItemDeleteRequest;
import com.taobao.api.request.ItemSkuGetRequest;
import com.taobao.api.request.ItemSkusGetRequest;
import com.taobao.api.request.ItemsInventoryGetRequest;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.response.ItemDeleteResponse;
import com.taobao.api.response.ItemSkuGetResponse;
import com.taobao.api.response.ItemSkusGetResponse;
import com.taobao.api.response.ItemsInventoryGetResponse;
import com.taobao.api.response.ItemsOnsaleGetResponse;

/**
 * <p>Title: TBWareResource</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2015年12月1日
 */
@Path("taobao/item")
public class TBItemResource {
	private static final Logger LOGGER = Logger.getLogger(TBItemResource.class);
	
	private final String serverUrl = "http://120.26.192.210/router/rest";
	//e百丽
	private String appKey="12038771";
	private String appSecret="1a5476b788487f2cb21a313bfb84e2f8";
	private String sessionKey="6100a1971c819dcd29aeb0c234b10dd41f6d795ade7afab98563612";
	
	private TaobaoClient getClient(String appKey, String appSecret){
		TaobaoClient client=null; 
		
		if(appKey==null||appKey.equals(""))
			appKey = this.appKey;
		if(appSecret==null||appSecret.equals(""))
			appSecret = this.appSecret;
		client=new DefaultTaobaoClient(serverUrl,appKey,appSecret); 
		return client;
	}
	
	//检索在售商品
	@Path("/onsale")
	@GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Item> getOnsaleItem(@QueryParam("appKey") final String key, 
			@QueryParam("appSecret") final String secret, 
			@QueryParam("sessionKey") final String sessionKey, 
			@QueryParam("page") final long page,
			@QueryParam("pagesize") long pagesize,
			@QueryParam("st") final String st,
			@QueryParam("et") final String et){
		List<Item> list = null;

		ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();
		req.setFields("approve_status,num_iid,title,nick,type,cid,pic_url,num,props,valid_thru,list_time,price,has_discount,has_invoice,has_warranty,has_showcase,modified,delist_time,postage_id,seller_cids,outer_id,sold_quantity");
		req.setPageNo(page);
		req.setOrderBy("list_time:desc");//排序方式。格式为column:asc/desc ，column可选值:list_time(上架时间),delist_time(下架时间),num(商品数量)，modified(最近修改时间)，sold_quantity（商品销量）,;默认上架时间降序(即最新上架排在前面)。如按照上架时间降序排序方式为list_time:desc
		req.setPageSize(pagesize);
		if(!StringUtils.isEmpty(st)){
			req.setStartModified(StringUtils.parseDateTime(st));
		}
		if(!StringUtils.isEmpty(et)){
			req.setEndModified(StringUtils.parseDateTime(et));
		}

		ItemsOnsaleGetResponse rsp;
		try {
			TaobaoClient client = this.getClient(key, secret);
			rsp = client.execute(req, sessionKey);
			if(rsp.isSuccess()){
				list = rsp.getItems();
				LOGGER.info(rsp.getTotalResults());
			}else{
				LOGGER.warn(""+rsp.getErrorCode()+":"+rsp.getMsg());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	//检索未在售商品
	@Path("/nosale")
	@GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Item> getNosaleItem(@QueryParam("appKey") final String key, 
			@QueryParam("appSecret") final String secret, 
			@QueryParam("sessionKey") final String sessionKey, 
			@QueryParam("page") final long page,
			@QueryParam("pagesize") long pagesize,
			@QueryParam("banner") String banner,
			@QueryParam("st") final String st,
			@QueryParam("et") final String et){
		List<Item> list = null;

		ItemsInventoryGetRequest req = new ItemsInventoryGetRequest();
		req.setFields("approve_status,num_iid,title,nick,type,cid,pic_url,num,props,valid_thru,list_time,price,has_discount,has_invoice,has_warranty,has_showcase,modified,delist_time,postage_id,seller_cids,outer_id,sold_quantity");
		req.setPageNo(page);
		req.setOrderBy("list_time:desc");//排序方式。格式为column:asc/desc ，column可选值:list_time(上架时间),delist_time(下架时间),num(商品数量)，modified(最近修改时间)，sold_quantity（商品销量）,;默认上架时间降序(即最新上架排在前面)。如按照上架时间降序排序方式为list_time:desc
		req.setPageSize(pagesize);
		
//		regular_shelved(定时上架)
//		never_on_shelf(从未上架)
//		off_shelf(我下架的)
//		for_shelved(等待所有上架)
//		sold_out(全部卖完)
//		violation_off_shelf(违规下架的)
//		默认查询for_shelved(等待所有上架)这个状态的商品
//		注：for_shelved(等待所有上架)=regular_shelved(定时上架)+never_on_shelf(从未上架)+off_shelf(我下架的)
		if(!StringUtils.isEmpty(banner)){
			req.setBanner(banner);

		}
		if(!StringUtils.isEmpty(st)){
			req.setStartModified(StringUtils.parseDateTime(st));
		}
		if(!StringUtils.isEmpty(et)){
			req.setEndModified(StringUtils.parseDateTime(et));
		}

		ItemsInventoryGetResponse rsp;
		try {
			TaobaoClient client = this.getClient(key, secret);
			rsp = client.execute(req, sessionKey);
			if(rsp.isSuccess()){
				list = rsp.getItems();
				LOGGER.info(rsp.getTotalResults());
			}else{
				LOGGER.warn(""+rsp.getErrorCode()+":"+rsp.getMsg());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	//删除商品
	@Path("/delete")
	@GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public boolean deleteItem(@QueryParam("appKey") final String key, 
			@QueryParam("appSecret") final String secret, 
			@QueryParam("sessionKey") final String sessionKey, 
			@QueryParam("itemid") final String itemid){
		boolean flag = false;
		ItemDeleteRequest req = new ItemDeleteRequest();
		req.setNumIid(23242L);
		ItemDeleteResponse rsp;
		try {
			TaobaoClient client = this.getClient(key, secret);
			rsp = client.execute(req, sessionKey);
			if(rsp.isSuccess()){
				flag = true;
			}else{
				LOGGER.warn(""+rsp.getErrorCode()+":"+rsp.getMsg());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	
	
	
	//根据商品获取skus
	@Path("/skus/get")
	@GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Sku> getSkuByItemId(@QueryParam("appKey") final String key, 
			@QueryParam("appSecret") final String secret, 
			@QueryParam("sessionKey") final String sessionKey, 
			@QueryParam("itemid") final String itemid){
		List<Sku> list = null;
		LOGGER.info("AAAAAAA");
		LOGGER.warn("BBBBBBBB");
		ItemSkusGetRequest req = new ItemSkusGetRequest();
		req.setFields("sku_id,num_iid,quantity,price,created,modified,status,outer_id,barcode");
		req.setNumIids(itemid);
		ItemSkusGetResponse rsp;
		try {
			TaobaoClient client = this.getClient(key, secret);
			rsp = client.execute(req, sessionKey);
			if(rsp.isSuccess()){
				list = rsp.getSkus();
			}else{
				LOGGER.warn(""+rsp.getErrorCode()+":"+rsp.getMsg());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	//根据商品获取指定的sku
	@Path("/sku/get")
	@GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Sku getSkuByItemId(@QueryParam("appKey") final String key, 
			@QueryParam("appSecret") final String secret, 
			@QueryParam("sessionKey") final String sessionKey, 
			@QueryParam("itemid") final Long itemid,
			@QueryParam("skuid") final Long skuid){
		Sku sku = null;

		ItemSkuGetRequest  req = new ItemSkuGetRequest ();
		req.setFields("sku_id,num_iid,quantity,price,created,modified,status,outer_id,barcode");
		req.setNumIid(itemid);
		req.setSkuId(skuid);
		ItemSkuGetResponse rsp;
		try {
			TaobaoClient client = this.getClient(key, secret);
			rsp = client.execute(req, sessionKey);
			if(rsp.isSuccess()){
				sku = rsp.getSku();
			}else{
				LOGGER.warn(""+rsp.getErrorCode()+":"+rsp.getMsg());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sku;
	}
}
