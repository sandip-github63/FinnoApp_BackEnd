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
			log.warn("No image provided for saving.");
			return map;
		}

		try {
			long fileSizeInBytes = image.getSize();
			long maxsize = Long.parseLong(maxSize);

			log.debug("Max size configured: {} MB", maxsize);

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
			log.info("Image '{}' saved successfully at '{}'.", fileName, filePath);
		} catch (IOException e) {
			log.error("Error saving image", e);
			map.put("serverError", "yes");
			log.error("Failed to save image due to an IOException.", e);
		} catch (CustomException e) {
			log.error("Error saving image: {}", e.getMessage());
			map.put("imageSizeError", e.getMessage());
		}

		return map;
	}

	private String generateUniqueFilename(String originalFilename) {
		return UUID.randomUUID().toString() + "_" + originalFilename;
	}

	public boolean deleteImageFromServerByImageName(String imageName) {
		try {
			Path imagePath = Paths.get(uploadDirectory, imageName);

			// Check if the file exists before attempting to delete
			if (Files.exists(imagePath)) {
				Files.delete(imagePath);
				log.info("Image '{}' deleted successfully.", imageName);
				return true; // Image deleted successfully
			} else {
				log.warn("Image not found for deletion: {}", imageName);
				return false; // Image not found
			}
		} catch (IOException e) {
			log.error("Error deleting image", e);
			return false; // Error occurred while deleting image
		}
	}
}
