package com.finnoapp.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finnoapp.model.Role;
import com.finnoapp.repository.RoleRepository;
import com.finnoapp.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository repository;

    @Override
    public Role getRole(String roleName) {
	System.out.println("inside Role Service Impl method......");

	return this.repository.findByRoleName(roleName);
    }

}
