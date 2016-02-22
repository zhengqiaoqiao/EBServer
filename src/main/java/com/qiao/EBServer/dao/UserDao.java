package com.qiao.EBServer.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.qiao.EBServer.domain.User;


@Repository
public class UserDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private HibernateTemplate hibernateTemplate;

	public List<User> list(User user){
		List<User> list = (List<User>) hibernateTemplate.findByExample(user);
		return list;
	}
	
}
