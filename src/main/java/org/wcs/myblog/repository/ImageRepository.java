package org.wcs.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wcs.myblog.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
