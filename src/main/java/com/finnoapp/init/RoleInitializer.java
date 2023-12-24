package com.finnoapp.init;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.finnoapp.model.Role;
import com.finnoapp.repository.RoleRepository;

@Component
public class RoleInitializer {

	@Autowired
	private RoleRepository roleRepository;

	@PostConstruct
	@Transactional
	public void initializeRoles() {
		createRoleIfNotExists("ROLE_ADMIN");
		createRoleIfNotExists("ROLE_USER");
	}

	private void createRoleIfNotExists(String roleName) {
		Role role = this.roleRepository.findByRoleName(roleName);
		if (role == null) {
			role = new Role();
			role.setRoleName(roleName);
			roleRepository.save(role);
		}
	}

}
