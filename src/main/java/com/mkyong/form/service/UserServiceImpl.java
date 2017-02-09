package com.mkyong.form.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mkyong.form.dao.UserDao;
import com.mkyong.form.model.User;

public class UserServiceImpl implements UserService {
	
	@Autowired
	public void setUserDao(UserDao userDao){
		this.userDao = userDao;
	}

	@Override
	public User findById(Integer id) {
		return userDao.findById(id);
	}

	@Override
	public List<User> findAll() {
		return userDao.findAll();
	}

	@Override
	public void saveOrUpdate(User user) {
		if(findById(user.getId() == null){
			userDao.save(user);
		}else {
			userDao.update(user);
		}
				)
	}

	@Override
	public void delete(int id) {
		userDao.delete(id);
	}

}
