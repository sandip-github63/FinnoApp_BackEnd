package com.finnoapp.config;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.finnoapp.model.User;

public class ChildUserDetails implements UserDetails {

    private String userName;

    private String password;

    private String email;

    private List<GrantedAuthority> authorities;

    public ChildUserDetails(User user) {

	System.out.println("inside childuserdetails");

	this.userName = user.getUserName();
	this.password = user.getPassword();
	this.email = user.getEmail();

	this.authorities = user.getuRole().stream()
		.map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getRoleName()))
		.collect(Collectors.toList());

	System.out.println("authorities  :" + authorities);

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
	return this.authorities;
    }

    @Override
    public String getPassword() {
	return this.password;
    }

    @Override
    public String getUsername() {
	return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
	return true;
    }

    @Override
    public boolean isAccountNonLocked() {
	return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
	return true;
    }

    @Override
    public boolean isEnabled() {
	return true;
    }

}
