package com.corporation.apiclient.controller;

import com.corporation.apiclient.entities.File;
import com.corporation.apiclient.exceptions.FileNotFoundException;
import com.corporation.apiclient.response.FileResponse;
import com.corporation.apiclient.response.MessageResponse;
import com.corporation.apiclient.services.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/filedb")
@Tag(name = "File DataBase", description = "Endpoints for Files in DataBase")
public class FileControllerDb {

    private static final ZoneId BRAZIL_TIMEZONE = ZoneId.of("America/Sao_Paulo");

    @Autowired
    private FileService fileService;


    @PostMapping("/upload")
    @Operation(summary = "Upload a File to DataBase", description = "Upload a File to DataBase", tags = {"File DataBase"})
    public ResponseEntity<MessageResponse> uploadFile(@RequestParam("file") MultipartFile file)
            throws IOException {

        fileService.store(file);
        String message = "Uploaded the file successfully: " + file.getOriginalFilename();

        final MessageResponse messageResponse =
                new MessageResponse(LocalDateTime.now(BRAZIL_TIMEZONE).toString(),
                        HttpStatus.CREATED.value(), message, file.getOriginalFilename(), file.getContentType());

        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

    @GetMapping("/files")
    @Operation(summary = "Find All Files In DataBase", description = "Find All Files In DataBase", tags = {"File DataBase"})
    public ResponseEntity<List<FileResponse>> getListFiles() {

        List<FileResponse> files = fileService.getAllFiles().map(fileFromDatabase -> {

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
                    .path(String.valueOf(fileFromDatabase.getId())).toUriString();

            return new FileResponse(fileFromDatabase.getName(), fileDownloadUri,
                    fileFromDatabase.getContentType(), (long) fileFromDatabase.getData().length);

        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/{id}")
    @Operation(summary = "Get File in DataBase", description = "Get File in DataBase", tags = {"File DataBase"})
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) throws FileNotFoundException {

        File fileFromDatabase = fileService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileFromDatabase.getName() + "\"")
                .body(fileFromDatabase.getData());
    }


}
