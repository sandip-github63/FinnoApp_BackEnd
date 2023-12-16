package com.finnoapp.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finnoapp.model.User;
import com.finnoapp.payload.request.ResetPasswordRequest;
import com.finnoapp.payload.request.UpdatePasswordRequest;
import com.finnoapp.payload.response.GenericMessage;
import com.finnoapp.payload.response.ResetPasswordResponse;
import com.finnoapp.repository.UserRepository;
import com.finnoapp.service.ResetPasswordService;
import com.finnoapp.service.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/reset-password")
public class ResetPasswordController {

	@Autowired
	private ResetPasswordService resetPasswordService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@PostMapping("/generate-otp")
	public ResponseEntity<ResetPasswordResponse> generateOTP(@RequestBody @Valid ResetPasswordRequest request,
			BindingResult bindingResult) {
		// Validate request
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(buildErrorResponse(bindingResult));
		}

		String email = request.getEmail();

		// Check if the email exists in the database

		Optional<User> user = this.userRepository.findByEmail(email);

		// If the email exists, generate OTP and send it via email
		if (user.isPresent()) {

			try {
				String otp = resetPasswordService.generateOTP();
				resetPasswordService.sendOTPByEmail(email, otp);

				ResetPasswordResponse response = new ResetPasswordResponse();
				response.setMessage("OTP sent successfully");

				// Save OTP in the database
				user.ifPresent(u -> {
					u.setOtp(otp);
					u.setOtpExpirationTime(LocalDateTime.now().plusMinutes(5));
					this.userRepository.save(u);
				});

				return ResponseEntity.ok(response);
			} catch (MailException e) {
				ResetPasswordResponse response = new ResetPasswordResponse();
				response.setMessage("Failed to send OTP. Please try again.");
				e.printStackTrace();

				return ResponseEntity.status(500).body(response);
			}
		}

		else {
			ResetPasswordResponse response = new ResetPasswordResponse();
			response.setMessage(
					"If the email exists in our system, we will send a password reset OTP to it. Please check your email.");
			return ResponseEntity.status(200).body(response);
		}
	}

	private ResetPasswordResponse buildErrorResponse(BindingResult bindingResult) {
		ResetPasswordResponse response = new ResetPasswordResponse();
		response.setMessage("Validation failed. Please check your request.");

		bindingResult.getFieldErrors()
				.forEach(error -> response.addValidationError(error.getField(), error.getDefaultMessage()));

		return response;
	}

	@PostMapping("/validate-otp")
	public ResponseEntity<ResetPasswordResponse> validateOTP(@RequestBody @Valid ResetPasswordRequest request,
			BindingResult bindingResult) {
		// Validate request
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(buildErrorResponse(bindingResult));
		}

		String email = request.getEmail();
		String enteredOTP = request.getOtp();

		// Check if the email exists in the database
		Optional<User> userOptional = this.userRepository.findByEmail(email);

		if (userOptional.isPresent()) {
			User user = userOptional.get();
			String storedOTP = user.getOtp(); // Assuming you have a field in the User entity to store OTP
			LocalDateTime expirationTime = user.getOtpExpirationTime();

			// Validate the entered OTP and check expiration time
			if (expirationTime.isAfter(LocalDateTime.now()) && enteredOTP.equals(storedOTP)) {
				// OTP is valid
				ResetPasswordResponse response = new ResetPasswordResponse();
				response.setMessage("OTP is valid");
				return ResponseEntity.ok(response);
			} else if (expirationTime.isBefore(LocalDateTime.now())) {
				// OTP has expired
				ResetPasswordResponse response = new ResetPasswordResponse();
				response.setMessage("OTP has expired. Please request a new one.");
				return ResponseEntity.status(400).body(response);
			} else {
				// Invalid OTP
				ResetPasswordResponse response = new ResetPasswordResponse();
				response.setMessage("Invalid OTP. Please try again.");
				return ResponseEntity.status(400).body(response);
			}
		} else {
			// User not found
			ResetPasswordResponse response = new ResetPasswordResponse();
			response.setMessage("User not found. Please check your email and try again.");
			return ResponseEntity.status(404).body(response);
		}
	}

	// update UserPasswordByEmailId
	@PutMapping("/update-password")
	public ResponseEntity<?> updateUserPasswordByEmail(@RequestBody UpdatePasswordRequest request) {
		GenericMessage<Void> response = new GenericMessage<>();
		if (isValidRequest(request)) {
			boolean result = userService.updatePasswordByEmail(request.getEmail(), request.getNewPassword());
			if (result) {

				response.setMessage("Password Updated..");
				return ResponseEntity.ok(response);
			} else {
				response.setMessage("Email Id not Found");
				return ResponseEntity.status(404).body(response);
			}
		} else {
			response.setMessage("Bad Request....");
			return ResponseEntity.status(400).body(response);
		}
	}

	private boolean isValidRequest(UpdatePasswordRequest request) {
		return request.getEmail() != null && request.getNewPassword() != null && request.getConfirmPassword() != null
				&& request.getNewPassword().equals(request.getConfirmPassword());
	}
}
