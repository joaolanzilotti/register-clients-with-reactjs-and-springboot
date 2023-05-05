package com.corporation.apiclient.controller;

import com.corporation.apiclient.dto.UploadFileResponseDTO;
import com.corporation.apiclient.services.ClientService;
import com.corporation.apiclient.services.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Tag(name = "File EndPoint")
@RestController
@RequestMapping("/api/file")
public class FileController {

    private Logger logger = Logger.getLogger(FileController.class.getName());

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public UploadFileResponseDTO uploadFile(@RequestParam("file")MultipartFile file){
        logger.info("Storing file to disk");

        String filename = fileStorageService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/file/downloadFile/").path(filename).toUriString();

        return new UploadFileResponseDTO(filename, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFile")
    public List<UploadFileResponseDTO> uploadMultipleFile(@RequestParam("files")MultipartFile[] files){
        logger.info("Storing file to disk");



        return Arrays.stream(files).map(this::uploadFile).collect(Collectors.toList());
    }

}
