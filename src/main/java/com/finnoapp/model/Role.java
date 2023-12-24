package com.finnoapp.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
