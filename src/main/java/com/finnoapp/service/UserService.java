package com.finnoapp.service;

import java.util.List;
import java.util.Set;

import com.finnoapp.model.User;
import com.finnoapp.model.UserRole;

public interface UserService {

	public User createUser(User user, Set<UserRole> userRoles);

	public User getUserByUserName(String userName);

	public boolean deleteUser(Long userId);

	public List<User> getUsers();

	public boolean updatePasswordByEmail(String email, String password);

	public User getUserByUserId(Long userId);

	public User updateUser(User user);

	public User getUserById(Long userId);

}
