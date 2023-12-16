package com.finnoapp.service;

import java.util.List;

import com.finnoapp.model.UserRole;

public interface UserRoleService {

    public List<UserRole> getRole(Long userId);

    public UserRole updateRole(UserRole userRole);

}
