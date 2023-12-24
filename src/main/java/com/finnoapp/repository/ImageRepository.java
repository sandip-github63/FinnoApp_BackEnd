package com.finnoapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finnoapp.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

	List<Image> findByImageName(String imageName);

}
