package com.finnoapp.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.finnoapp.model.User;
import com.finnoapp.repository.UserRepository;

@Component
public class ChildUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		System.out.println("inside loaduserbyusername");

		User user = repo.findByUserName(username);

		System.out.println(" inside find by username");

		Optional<User> user1 = Optional.ofNullable(user);

		return user1.map(ChildUserDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("user not found" + user));

	}

}
