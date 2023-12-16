package com.finnoapp.payload.request;

public class UpdatePasswordRequest {

    private String email;

    private String newPassword;

    private String confirmPassword;

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getNewPassword() {
	return newPassword;
    }

    public void setNewPassword(String newPassword) {
	this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
	return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
	this.confirmPassword = confirmPassword;
    }

    public UpdatePasswordRequest(String email, String newPassword, String confirmPassword) {
	super();
	this.email = email;
	this.newPassword = newPassword;
	this.confirmPassword = confirmPassword;
    }

    public UpdatePasswordRequest() {
	super();
	// TODO Auto-generated constructor stub
    }

}
