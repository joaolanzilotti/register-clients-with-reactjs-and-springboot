package com.corporation.apiclient.integrationtests.dto.wrappers;

import com.corporation.apiclient.integrationtests.dto.ClientDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientEmbeddedDTO implements Serializable {

    @JsonProperty("clientDTOList")
    private List<ClientDTO> clients;

}
