package com.finnoapp.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finnoapp.model.Role;
import com.finnoapp.model.User;
import com.finnoapp.model.UserRole;
import com.finnoapp.repository.RoleRepository;
import com.finnoapp.repository.UserRoleRepository;
import com.finnoapp.service.UserRoleService;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleRepository userRolerepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<UserRole> getRole(Long userId) {

	User u = new User();
	u.setUserId(userId);

	return this.userRolerepository.findByUser(u);
    }

    @Override
    public UserRole updateRole(UserRole userRole) {

	Role role = this.roleRepository.findByRoleName(userRole.getRole().getRoleName());

	userRole.setRole(role);

	return this.userRolerepository.save(userRole);
    }

}
