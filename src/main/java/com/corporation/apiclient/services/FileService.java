package com.corporation.apiclient.services;

import com.corporation.apiclient.entities.File;
import com.corporation.apiclient.exceptions.FileNotFoundException;
import com.corporation.apiclient.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public File store(MultipartFile file) throws IOException{
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        File fileEntity = new File(filename, file.getContentType(), file.getSize(), file.getBytes());
        return fileRepository.save(fileEntity);
    }

    public File getFile(Long id) throws FileNotFoundException {
        return fileRepository.findById(id).orElseThrow(() -> new FileNotFoundException("File Not Found!"));
    }

    public Stream<File> getAllFiles(){
        return fileRepository.findAll().stream();
    }

}
