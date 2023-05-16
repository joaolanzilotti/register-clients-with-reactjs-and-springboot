package com.corporation.apiclient.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {

    private String timestamp;
    private Integer status;
    private String message;
    private String path;
    private String method;

}
