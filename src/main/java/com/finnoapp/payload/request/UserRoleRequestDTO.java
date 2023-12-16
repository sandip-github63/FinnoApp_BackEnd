package com.finnoapp.payload.request;

public class UserRoleRequestDTO {

	private Long userRoleId;

	private Long userId;

	private String roleName;

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return "UserRoleRequestDTO [userRoleId=" + userRoleId + ", userId=" + userId + ", roleName=" + roleName + "]";
	}

}
