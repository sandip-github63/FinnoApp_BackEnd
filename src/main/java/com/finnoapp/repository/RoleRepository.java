package com.finnoapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finnoapp.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    public Role findByRoleName(String roleName);

}