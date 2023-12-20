package com.finnoapp.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_id")
	private Long roleId;

	@Column(name = "role_name")
	private String roleName;

	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonBackReference
	Set<UserRole> uRole = new HashSet<>();

	public Long getRoleId() {
		return roleId;
	}

	public Set<UserRole> getuRole() {
		return uRole;
	}

	public void setuRole(Set<UserRole> uRole) {
		this.uRole = uRole;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Role() {
		super();
		System.out.println("inside Role default constructor..");
	}

	public Role(Long roleId, String roleName, Set<UserRole> uRole) {
		super();
		System.out.println("inside Role parametrized constructor..");
		this.roleId = roleId;
		this.roleName = roleName;
		this.uRole = uRole;
	}

}
