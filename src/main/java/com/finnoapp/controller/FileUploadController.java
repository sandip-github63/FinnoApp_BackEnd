package com.finnoapp.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.finnoapp.exception.CustomException;
import com.finnoapp.model.User;
import com.finnoapp.payload.request.ProfileRequest;
import com.finnoapp.payload.response.GenericMessage;
import com.finnoapp.service.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/file")
public class FileUploadController {

	@Value("${fileupload.directory}")
	private String uploadDirectory;

	@Value("${fileupload.maxFileSize}")
	private String maxFileSize;

	@Autowired
	private UserService userService;

	@PostMapping("/upload")
	public ResponseEntity<?> uploadProfile(@ModelAttribute ProfileRequest profileRequest) {
		try {
			MultipartFile profileImage = profileRequest.getProfileImage();

			// Check if the uploaded file is not empty
			if (profileImage != null && !profileImage.isEmpty() && profileRequest.getUserId() != null) {
				// Check the file size
				long fileSizeInBytes = profileImage.getSize();
				long maxFileSizeInBytes = parseFileSize(maxFileSize);

				if (fileSizeInBytes > maxFileSizeInBytes) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("File size exceeds the maximum allowed size (" + maxFileSize + ")");
				}

				String fileName = StringUtils.cleanPath(profileImage.getOriginalFilename());

				System.out.println("file Name :" + fileName);
				// Resolve the absolute path dynamically
				Path absolutePath = Paths.get(uploadDirectory).toAbsolutePath();
				System.out.println("absolute path :" + absolutePath);
				Path filePath = absolutePath.resolve(fileName);

				System.out.println("file path :" + absolutePath);

				// file name's store in database

				User user = this.userService.getUserByUserId(profileRequest.getUserId());

				if (user == null)
					throw new CustomException("invalid userId");

				user.setProfile(fileName);
				this.userService.updateUser(user);

				// Save the file to the server using the resolved absolute path
				Files.copy(profileImage.getInputStream(), filePath);

				GenericMessage<Void> g = new GenericMessage<>();
				g.setMessage("Profile image uploaded successfully!");

				return ResponseEntity.ok(g);

			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide a valid profile image");
			}
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile image");
		}
	}

	private long parseFileSize(String size) {
		size = size.toUpperCase();
		if (size.endsWith("KB")) {
			return Long.parseLong(size.substring(0, size.length() - 2)) * 1024;
		} else if (size.endsWith("MB")) {
			return Long.parseLong(size.substring(0, size.length() - 2)) * 1024 * 1024;
		} else if (size.endsWith("GB")) {
			return Long.parseLong(size.substring(0, size.length() - 2)) * 1024 * 1024 * 1024;
		} else {
			return Long.parseLong(size);
		}
	}

	@GetMapping("/profile/{userId}")
	public ResponseEntity<Resource> getProfileImage(@PathVariable Long userId) {
		try {
			// Retrieve user from the database by user ID
			User user = userService.getUserByUserId(userId);

			if (user == null || user.getProfile() == null) {
				return ResponseEntity.notFound().build();
			}

			// Resolve the absolute path dynamically
			Path absolutePath = Paths.get(uploadDirectory).toAbsolutePath();
			Path filePath = absolutePath.resolve(user.getProfile());

			// Load the file as a resource
			Resource resource = new UrlResource(filePath.toUri());

			// Set the Content-Disposition header to force download
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
