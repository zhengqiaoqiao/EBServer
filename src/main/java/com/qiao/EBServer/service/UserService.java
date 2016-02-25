package com.qiao.EBServer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jd.open.api.sdk.internal.util.StringUtil;
import com.qiao.EBServer.dao.UserDao;
import com.qiao.EBServer.domain.User;

@Service
@Scope("prototype")
public class UserService {
	@Autowired
	private UserDao userDao;
	
	public List<User> findUsersByName(String name) {
		if(StringUtil.isEmpty(name)){
			return null;
		}
		User user = new User();
		user.setName(name);
		List<User> list = userDao.list(user);
		return list;
	}
	
	public List<User> findUsers(User user) {
		List<User> list = userDao.list(user);
		return list;
	}
}
