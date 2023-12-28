package com.finnoapp.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/serve")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Value("${imageUpload.directory}")
    private String uploadedImagesDirectory;

    @GetMapping("/images/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get(uploadedImagesDirectory).resolve(imageName);
            logger.info("Requested imagePath: {}", imagePath);

            // Check if the file exists
            if (Files.exists(imagePath)) {
                Resource resource = new UrlResource(imagePath.toUri());

                // Determine content type dynamically
                String contentType = Files.probeContentType(imagePath);
                if (contentType == null) {
                    contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
                }

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType(contentType));

                logger.info("Image found and served successfully.");
                return ResponseEntity.ok().headers(headers).body(resource);
            } else {
                logger.warn("Image not found for imageName: {}", imageName);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error while serving image for imageName: {}", imageName, e);
            return ResponseEntity.notFound().build();
        }
    }
}
