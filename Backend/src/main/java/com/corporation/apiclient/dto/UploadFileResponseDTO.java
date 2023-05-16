package com.corporation.apiclient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileResponseDTO implements Serializable {

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private Long size;

}
