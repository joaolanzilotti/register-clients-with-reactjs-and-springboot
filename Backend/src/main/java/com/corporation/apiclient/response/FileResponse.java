package com.corporation.apiclient.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {

    private String name;
    private String url;
    private String type;
    private Long size;

}
