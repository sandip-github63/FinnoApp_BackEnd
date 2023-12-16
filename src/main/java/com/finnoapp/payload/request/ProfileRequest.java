package com.finnoapp.payload.request;

import org.springframework.web.multipart.MultipartFile;

public class ProfileRequest {

    private Long userId;

    private MultipartFile profileImage;

    public MultipartFile getProfileImage() {
	return profileImage;
    }

    public void setProfileImage(MultipartFile profileImage) {
	this.profileImage = profileImage;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

}