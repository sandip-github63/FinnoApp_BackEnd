package com.finnoapp.pojos;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.finnoapp.exception.CustomException;

public class ImageUpload {

	private static final Logger log = LoggerFactory.getLogger(ImageUpload.class);

	@Value("${imageUpload.directory}")
	private String uploadDirectory;

	@Value("${imageUpload.maxSize}")
	private String maxSize;

	public Map<String, String> saveImage(MultipartFile image) {
		Map<String, String> map = new HashMap<>();

		if (image == null || image.isEmpty()) {
			map.put("imageNotFound", "true");
			return map;
		}

		try {
			long fileSizeInBytes = image.getSize();
			// Validate image size for images only
			long maxsize = Long.parseLong(maxSize);

			System.out.println("max size: " + maxsize);

			long maxImageSizeInBytes = maxsize * 1024 * 1024; // 1MB limit for images

			if (fileSizeInBytes > maxImageSizeInBytes) {
				throw new CustomException("Image size exceeds the allowed limit (" + maxsize + "MB).");
			}

			String fileName = generateUniqueFilename(image.getOriginalFilename());

			Path filePath = Paths.get(uploadDirectory, fileName);

			Files.createDirectories(filePath.getParent()); // Create directories if not exist

			try (InputStream inputStream = image.getInputStream()) {
				Files.copy(inputStream, filePath);
			}

			map.put("imageName", fileName);
			map.put("imagePath", filePath.toString());// storing relative path
		} catch (IOException e) {
			log.error("Error saving image", e);
			map.put("serverError", "yes");
		}

		return map;
	}

	private String generateUniqueFilename(String originalFilename) {
		return UUID.randomUUID().toString() + "_" + originalFilename;
	}
}
