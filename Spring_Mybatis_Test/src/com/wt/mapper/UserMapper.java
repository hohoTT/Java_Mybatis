package com.wt.mapper;

import java.util.List;

import com.wt.entities.User;

public interface UserMapper {

	void save(User user);

	void update(User user);

	void delete(int id);

	User findById(int id);

	List<User> findAll();

}
