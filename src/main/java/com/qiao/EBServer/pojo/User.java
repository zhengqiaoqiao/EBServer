package com.qiao.EBServer.pojo;

import javax.xml.bind.annotation.*;

/**
 * <p>Title: User</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2016年2月16日
 */

@XmlRootElement 
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
	private String a;
	private String b;
	/**
	 * @return the a
	 */
	public String getA() {
		return a;
	}
	/**
	 * @param a the a to set
	 */
	public void setA(String a) {
		this.a = a;
	}
	/**
	 * @return the b
	 */
	public String getB() {
		return b;
	}
	/**
	 * @param b the b to set
	 */
	public void setB(String b) {
		this.b = b;
	}
}
