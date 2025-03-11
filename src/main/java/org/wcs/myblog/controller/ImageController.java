package org.wcs.myblog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wcs.myblog.dto.ImageDTO;
import org.wcs.myblog.model.Image;
import org.wcs.myblog.model.Article;
import org.wcs.myblog.repository.ImageRepository;
import org.wcs.myblog.repository.ArticleRepository;
import org.wcs.myblog.service.ImageService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<List<ImageDTO>> getAllImages() {
        List<ImageDTO> images = imageService.getAllImages();
        if (images.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImageById(@PathVariable Long id) {
        ImageDTO image = imageService.getImageById(id);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(image);
    }

    @PostMapping
    public ResponseEntity<ImageDTO> createImage(@RequestBody Image image) {
        ImageDTO savedImage = imageService.createImage(image);
        return ResponseEntity.status(201).body(savedImage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImageDTO> updateImage(@PathVariable Long id, @RequestBody Image imageDetails) {
        ImageDTO updatedImage = imageService.updateImage(id, imageDetails);
        if (updatedImage == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedImage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        if (imageService.deleteImage(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
