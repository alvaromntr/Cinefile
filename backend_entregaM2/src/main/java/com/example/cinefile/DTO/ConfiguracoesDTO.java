package com.example.cinefile.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfiguracoesDTO {
    private Boolean receberNotificacoesEmail;
    private String temaInterface;
    private String idiomaPreferido;
}