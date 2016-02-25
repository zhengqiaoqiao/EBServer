package com.qiao.EBServer.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;


/**
 * 用户表
 * 
 * @author zheng.qq
 * CacheConcurrencyStrategy有五种缓存方式：
 
　　 CacheConcurrencyStrategy.NONE，不适用，默认
　　 CacheConcurrencyStrategy.READ_ONLY ，只读模式，在此模式下，如果对数据进行更新操作，会有异常；
　　 CacheConcurrencyStrategy.READ_WRITE ，读写模式在更新缓存的时候会把缓存里面的数据换成一个锁，其它事务如果去取相应的缓存数据，发现被锁了，直接就去数据库查询；
　　 CacheConcurrencyStrategy.NONSTRICT_READ_WRITE ，不严格的读写模式则不会的缓存数据加锁；
　　 CacheConcurrencyStrategy.TRANSACTIONAL ，事务模式指缓存支持事务，当事务回滚时，缓存也能回滚，只支持 JTA 环境。
 */
@Entity
@Table(name = "user")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@XmlRootElement 
public class User implements java.io.Serializable {

	private static final long serialVersionUID = 3862680753139413201L;

	/**
	 * id
	 */
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, insertable = true, updatable = true, length = 32)
	private String id;

	/**
	 * 姓名
	 */
	@Column(name = "name")
	@QueryParam("name")
	private String name;

	/**
	 * 年龄
	 */
	@Column(name = "age")
	@QueryParam("name")
	private String age;

	/**
	 * 地址
	 */
	@Column(name = "address")
	private String address;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
