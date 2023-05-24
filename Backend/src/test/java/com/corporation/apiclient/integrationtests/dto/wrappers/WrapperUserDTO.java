package com.corporation.apiclient.integrationtests.dto.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WrapperUserDTO implements Serializable {

    @JsonProperty("_embedded")
    private UserEmbeddedDTO embedded;



}
