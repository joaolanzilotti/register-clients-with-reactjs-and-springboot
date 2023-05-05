package com.corporation.apiclient.services;

import com.corporation.apiclient.config.FileStorageConfig;
import com.corporation.apiclient.exceptions.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;    //Caminho aonde os arquivos serão salvos, é pego pela classe Config e colocado no Path

    @Autowired
    public FileStorageService (FileStorageConfig fileStorageConfig){
        Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();  //Capturando o caminho e colocando no Path
        this.fileStorageLocation = path;
        try{
            Files.createDirectories(fileStorageLocation);  // Adicionando o Path no Files
        }catch (Exception e){
            throw  new FileStorageException("Could not create the directory where the uploades files will be stored!", e);
        }
    };

    public String storeFile(MultipartFile file){
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try{
            if(filename.contains("..")){  //Se Contiver dois .. será um erro de nome
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + filename);
            }
            //Para Salvar no Banco de Dados vai precisar substituir essas duas linhas
            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);   // Aqui ele pega o arquivo e vai transferindo aos poucos
            return filename;
        }catch (Exception e){
            throw new FileStorageException("Could not store file " + filename + ". Please try again!", e);
        }
    }

}
