package com.finnoapp.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finnoapp.exception.CustomException;
import com.finnoapp.model.User;
import com.finnoapp.model.UserRole;
import com.finnoapp.repository.RoleRepository;
import com.finnoapp.repository.UserRepository;
import com.finnoapp.service.UserService;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Override
	public User createUser(User user, Set<UserRole> userRoles) {

		User u1 = null;

		System.out.println("inside createUser method...");

		User u = this.userRepo.findByUserName(user.getUserName());

		try {

			if (u != null) {
				System.out.println("user already exit...");

				throw new CustomException("user is already exits...");

			} else {
				for (UserRole uRole : userRoles) {

					roleRepo.save(uRole.getRole());

					System.out.println("user Roles save succssfully..." + uRole.getRole().getRoleId());

				}

				user.getuRole().addAll(userRoles);

				user.setPassword(passwordEncoder.encode(user.getPassword()));

				u1 = userRepo.save(user);

				System.out.println("user data save successfully..." + u1.getFirstName());

			}

		} catch (CustomException ex) {
			System.out.println(ex.getMessage());
		}

		return u1;
	}

	@Override
	public User getUserByUserName(String userName) {
		User user = userRepo.findByUserName(userName);

		return user;
	}

	@Override
	public boolean deleteUser(Long userId) {

		boolean result = false;

		Optional<User> user = userRepo.findById(userId);

		if (user.isPresent()) {

			userRepo.deleteById(userId);
			return result = true;
		}

		return result;

	}

	@Override
	public List<User> getUsers() {

		return this.userRepo.findAll();
	}

	@Override
	@Transactional
	public boolean updatePasswordByEmail(String email, String password) {
		try {
			if (StringUtils.isBlank(email)) {
				return false; // or throw an IllegalArgumentException
			}

			Optional<User> userOptional = this.userRepo.findByEmail(email);
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				user.setPassword(this.passwordEncoder.encode(password));
				this.userRepo.save(user); // Update only the password in the repository
				return true; // Return true since the user was present and the password was updated
			}

			return false; // Return false if the user was not present
		} catch (Exception e) {
			// Log the exception or handle it appropriately
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public User getUserByUserId(Long userId) {
		Optional<User> user = this.userRepo.findById(userId);
		if (user.isPresent()) {
			return user.get();

		}
		return null;
	}

	@Override
	public User updateUser(User user) {
		User u = this.userRepo.save(user);
		return u;
	}

	@Override
	public User getUserById(Long userId) {

		return userRepo.findById(userId).orElse(null);
	}

}
