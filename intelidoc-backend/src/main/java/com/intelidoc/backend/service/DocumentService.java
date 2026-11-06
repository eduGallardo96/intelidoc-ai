package com.intelidoc.backend.service;

import com.intelidoc.backend.dto.UploadResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {

    UploadResponseDTO upload(MultipartFile file);
}
