package com.qiao.EBServer.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: Domain</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2015年12月17日
 */
public class Domain {
	private int s1 = 1;
	private String s2 = "Hello World";
	private boolean s3 = false;
	private List<Domain> s4 = new ArrayList<Domain>();
	private String[] s5 = {"1","2","3"};
	private Gender s6 = Gender.MAN;
	
	/**
	 * @return the s1
	 */
	public int getS1() {
		return s1;
	}
	/**
	 * @param s1 the s1 to set
	 */
	public void setS1(int s1) {
		this.s1 = s1;
	}
	/**
	 * @return the s2
	 */
	public String getS2() {
		return s2;
	}
	/**
	 * @param s2 the s2 to set
	 */
	public void setS2(String s2) {
		this.s2 = s2;
	}
	/**
	 * @return the s3
	 */
	public boolean isS3() {
		return s3;
	}
	/**
	 * @param s3 the s3 to set
	 */
	public void setS3(boolean s3) {
		this.s3 = s3;
	}
	/**
	 * @return the s4
	 */
	public List<Domain> getS4() {
		return s4;
	}
	/**
	 * @param s4 the s4 to set
	 */
	public void setS4(List<Domain> s4) {
		this.s4 = s4;
	}
	/**
	 * @return the s5
	 */
	public String[] getS5() {
		return s5;
	}
	/**
	 * @param s5 the s5 to set
	 */
	public void setS5(String[] s5) {
		this.s5 = s5;
	}
	/**
	 * @return the s6
	 */
	public Gender getS6() {
		return s6;
	}
	/**
	 * @param s6 the s6 to set
	 */
	public void setS6(Gender s6) {
		this.s6 = s6;
	}
}
