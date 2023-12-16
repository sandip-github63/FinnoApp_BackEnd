package com.finnoapp.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.finnoapp.service.ResetPasswordService;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public String generateOTP() {

	return String.valueOf((int) (Math.random() * 900000) + 100000);

    }

    @Override
    public void sendOTPByEmail(String email, String otp) {

	SimpleMailMessage message = new SimpleMailMessage();
	message.setTo(email);
	message.setSubject("Password Reset Otp");
	message.setText("Your OTP for password Rest is " + otp);

	javaMailSender.send(message);

    }

}
