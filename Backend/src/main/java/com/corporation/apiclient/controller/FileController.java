package com.corporation.apiclient.controller;

import com.corporation.apiclient.dto.UploadFileResponseDTO;
import com.corporation.apiclient.services.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Tag(name = "File Local", description = "Endpoints for Files in Local")
@RestController
@RequestMapping("/api/file")
public class FileController {

    private Logger logger = Logger.getLogger(FileController.class.getName());

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    @Operation(summary = "Upload a File to Local", description = "Upload a File to Local", tags = {"File Local"})
    public UploadFileResponseDTO uploadFile(@RequestParam("file")MultipartFile file){
        logger.info("Storing file to disk");

        String filename = fileStorageService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/file/downloadFile/").path(filename).toUriString();

        return new UploadFileResponseDTO(filename, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFile")
    @Operation(summary = "Upload Multiple Files to Local", description = "Upload Multiple Files to Local", tags = {"File Local"})
    public List<UploadFileResponseDTO> uploadMultipleFile(@RequestParam("files")MultipartFile[] files){
        logger.info("Storing file to disk");



        return Arrays.stream(files).map(this::uploadFile).collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{filename:.+}")
    @Operation(summary = "Download File", description = "Download File", tags = {"File Local"})
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename, HttpServletRequest request){
        logger.info("Downloading this File on Disk");

        Resource resource = fileStorageService.loadFileAsResource(filename);

        String contentType = "";
        try{
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch (Exception e){
            logger.info("Could not determine file type!");
        }

        if(contentType.isBlank()){
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
