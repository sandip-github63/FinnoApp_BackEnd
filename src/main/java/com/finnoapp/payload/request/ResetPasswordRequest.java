package com.finnoapp.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ResetPasswordRequest {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "OTP cannot be blank")
    private String otp;

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getOtp() {
	return otp;
    }

    public void setOtp(String otp) {
	this.otp = otp;
    }

}
