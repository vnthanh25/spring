package com.bim.server.service;

import java.util.List;

import com.bim.server.model.User;


public interface UserService {

	void saveUser(User user);

	void updateUser(User user);

	void deleteUserById(Long id);

	void deleteAllUsers();

	boolean isUserExist(User user);

	List<User> findAllUsers();
	
	User findById(Long id);

	User findByName(String name);

}
