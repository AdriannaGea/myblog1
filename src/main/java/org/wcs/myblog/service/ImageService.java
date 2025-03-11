package org.wcs.myblog.service;

import org.springframework.stereotype.Service;
import org.wcs.myblog.dto.ImageDTO;
import org.wcs.myblog.mapper.ImageMapper;
import org.wcs.myblog.model.Image;
import org.wcs.myblog.repository.ImageRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public ImageService(ImageRepository imageRepository, ImageMapper imageMapper) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    public List<ImageDTO> getAllImages() {
        List<Image> images = imageRepository.findAll();
        return images.stream().map(imageMapper::convertToDTO).collect(Collectors.toList());
    }

    public ImageDTO getImageById(Long id) {
        Optional<Image> image = imageRepository.findById(id);
        return image.map(imageMapper::convertToDTO).orElse(null);
    }

    public ImageDTO createImage(Image image) {
        Image savedImage = imageRepository.save(image);
        return imageMapper.convertToDTO(savedImage);
    }

    public ImageDTO updateImage(Long id, Image imageDetails) {
        return imageRepository.findById(id)
                .map(existingImage -> {
                    existingImage.setUrl(imageDetails.getUrl());
                    Image updatedImage = imageRepository.save(existingImage);
                    return imageMapper.convertToDTO(updatedImage);
                })
                .orElse(null);
    }

    public boolean deleteImage(Long id) {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            imageRepository.delete(image.get());
            return true;
        }
        return false;
    }
}

