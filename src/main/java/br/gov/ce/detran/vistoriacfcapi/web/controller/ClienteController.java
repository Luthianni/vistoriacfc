package br.gov.ce.detran.vistoriacfcapi.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.ce.detran.vistoriacfcapi.entity.Cliente;
import br.gov.ce.detran.vistoriacfcapi.jwt.JwtUserDetails;
import br.gov.ce.detran.vistoriacfcapi.service.ClienteService;
import br.gov.ce.detran.vistoriacfcapi.service.UsuarioService;
import br.gov.ce.detran.vistoriacfcapi.web.dto.ClienteCreateDto;
import br.gov.ce.detran.vistoriacfcapi.web.dto.ClienteResponseDto;
import br.gov.ce.detran.vistoriacfcapi.web.dto.mapper.ClienteMapper;
import br.gov.ce.detran.vistoriacfcapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Clientes", description = "Contém todas as operações relativas ao recurso de um cliente")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {
  
    private final ClienteService clienteService;    
    private final UsuarioService usuarioService;

    @Operation(summary = "Criar um novo cliente", description = "Recurso para criar um novo cliente vinculado a um usuário cadastrado. " +
            "Requisição exige uso de um bearer token. Acesso restrito a Role='CLIENTE'",
			responses = {
				@ApiResponse(responseCode = "201", description = "Recurso criado com sucesso", 
				content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),
				@ApiResponse(responseCode = "409", description = "Cliente CPF já possui cadastrado no sistema",
						content = @Content(mediaType = "Application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
				@ApiResponse(responseCode = "422", description = "Recurso não processados por falta de dados ou dados invalidos",
						content = @Content(mediaType = "Application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                @ApiResponse(responseCode = "403", description = "Recurso não permiti ao perfil ADMIN",
                        content = @Content(mediaType = "Application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
			}
	)

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SERVIDOR')")
    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto dto,
                                                    @AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = ClienteMapper.toCliente(dto);
        cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
        clienteService.salvar(cliente);
        return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));
    }
    
    
}
