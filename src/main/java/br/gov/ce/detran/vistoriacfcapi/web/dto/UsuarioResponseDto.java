package br.gov.ce.detran.vistoriacfcapi.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class UsuarioResponseDto {

    private Long id;
    private String username;
    private String role;
    private String nome;
    private String cpf;
    private String matricula;
    private String email;
    private String telefone;
}
