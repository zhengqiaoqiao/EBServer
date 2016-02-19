package com.qiao.EBServer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiao.EBServer.dao.TestDao;

/**
 * <p>Title: TestService</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2016年2月19日
 */
@Service
public class TestService {
	@Autowired
	private TestDao testDao;
	public void service1(){
		System.out.println("service is service1.");
		testDao.dao1();
	}
}
