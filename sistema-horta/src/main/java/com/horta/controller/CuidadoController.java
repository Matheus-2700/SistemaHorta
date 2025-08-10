package com.horta.controller;

import com.horta.dto.CuidadoDTO;
import com.horta.model.Cuidado;
import com.horta.service.CuidadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller REST para operações com cuidados
 */
@RestController
@RequestMapping("/cuidados")
@Tag(name = "Cuidados", description = "API para gerenciamento de cuidados com plantas")
@CrossOrigin(origins = "*")
public class CuidadoController {

    @Autowired
    private CuidadoService cuidadoService;

    @Operation(summary = "Lista todos os cuidados", description = "Retorna uma lista com todos os cuidados registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cuidados retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<CuidadoDTO>> listarTodos() {
        try {
            List<CuidadoDTO> cuidados = cuidadoService.listarTodos();
            return ResponseEntity.ok(cuidados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Registra novo cuidado", description = "Registra um novo cuidado para uma planta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuidado registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<CuidadoDTO> registrarCuidado(@Valid @RequestBody CuidadoDTO cuidadoDTO) {
        try {
            CuidadoDTO cuidadoSalvo = cuidadoService.registrarCuidado(cuidadoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(cuidadoSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Busca cuidado por ID", description = "Retorna os detalhes de um cuidado específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuidado encontrado"),
            @ApiResponse(responseCode = "404", description = "Cuidado não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CuidadoDTO> buscarPorId(
            @Parameter(description = "ID do cuidado") @PathVariable Long id) {
        try {
            Optional<CuidadoDTO> cuidado = cuidadoService.buscarPorId(id);
            return cuidado.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Atualiza cuidado", description = "Atualiza os dados de um cuidado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuidado atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuidado não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CuidadoDTO> atualizarCuidado(
            @Parameter(description = "ID do cuidado") @PathVariable Long id,
            @Valid @RequestBody CuidadoDTO cuidadoDTO) {
        try {
            CuidadoDTO cuidadoAtualizado = cuidadoService.atualizarCuidado(id, cuidadoDTO);
            return ResponseEntity.ok(cuidadoAtualizado);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Remove cuidado", description = "Remove um cuidado do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cuidado removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cuidado não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerCuidado(
            @Parameter(description = "ID do cuidado") @PathVariable Long id) {
        try {
            cuidadoService.removerCuidado(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Histórico de cuidados da planta", description = "Retorna o histórico de cuidados de uma planta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/planta/{plantaId}")
    public ResponseEntity<List<CuidadoDTO>> buscarCuidadosDaPlanta(
            @Parameter(description = "ID da planta") @PathVariable Long plantaId) {
        try {
            List<CuidadoDTO> cuidados = cuidadoService.buscarCuidadosDaPlanta(plantaId);
            return ResponseEntity.ok(cuidados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Busca cuidados por tipo", description = "Busca cuidados de um tipo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<CuidadoDTO>> buscarPorTipo(
            @Parameter(description = "Tipo de cuidado") @PathVariable Cuidado.TipoCuidado tipo) {
        try {
            List<CuidadoDTO> cuidados = cuidadoService.buscarPorTipo(tipo);
            return ResponseEntity.ok(cuidados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Cuidados em período", description = "Busca cuidados realizados em um período específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/periodo")
    public ResponseEntity<List<CuidadoDTO>> buscarCuidadosNoPeriodo(
            @Parameter(description = "Data de início") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data de fim") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            if (dataInicio.isAfter(dataFim)) {
                return ResponseEntity.badRequest().build();
            }
            List<CuidadoDTO> cuidados = cuidadoService.buscarCuidadosNoPeriodo(dataInicio, dataFim);
            return ResponseEntity.ok(cuidados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Cuidados recentes", description = "Retorna cuidados realizados nos últimos 7 dias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/recentes")
    public ResponseEntity<List<CuidadoDTO>> buscarCuidadosRecentes() {
        try {
            List<CuidadoDTO> cuidados = cuidadoService.buscarCuidadosRecentes();
            return ResponseEntity.ok(cuidados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Cuidados de hoje", description = "Retorna cuidados realizados hoje")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/hoje")
    public ResponseEntity<List<CuidadoDTO>> buscarCuidadosDeHoje() {
        try {
            List<CuidadoDTO> cuidados = cuidadoService.buscarCuidadosDeHoje();
            return ResponseEntity.ok(cuidados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Registrar rega", description = "Registra uma rega para uma planta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rega registrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/rega/{plantaId}")
    public ResponseEntity<CuidadoDTO> registrarRega(
            @Parameter(description = "ID da planta") @PathVariable Long plantaId,
            @Parameter(description = "Observações") @RequestParam(required = false) String observacoes,
            @Parameter(description = "Usuário responsável") @RequestParam(required = false) String usuarioResponsavel) {
        try {
            CuidadoDTO cuidado = cuidadoService.registrarRega(plantaId, observacoes, usuarioResponsavel);
            return ResponseEntity.status(HttpStatus.CREATED).body(cuidado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Registrar poda", description = "Registra uma poda para uma planta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Poda registrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/poda/{plantaId}")
    public ResponseEntity<CuidadoDTO> registrarPoda(
            @Parameter(description = "ID da planta") @PathVariable Long plantaId,
            @Parameter(description = "Observações") @RequestParam(required = false) String observacoes,
            @Parameter(description = "Usuário responsável") @RequestParam(required = false) String usuarioResponsavel) {
        try {
            CuidadoDTO cuidado = cuidadoService.registrarPoda(plantaId, observacoes, usuarioResponsavel);
            return ResponseEntity.status(HttpStatus.CREATED).body(cuidado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Registrar colheita", description = "Registra uma colheita para uma planta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Colheita registrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/colheita/{plantaId}")
    public ResponseEntity<CuidadoDTO> registrarColheita(
            @Parameter(description = "ID da planta") @PathVariable Long plantaId,
            @Parameter(description = "Observações") @RequestParam(required = false) String observacoes,
            @Parameter(description = "Usuário responsável") @RequestParam(required = false) String usuarioResponsavel) {
        try {
            CuidadoDTO cuidado = cuidadoService.registrarColheita(plantaId, observacoes, usuarioResponsavel);
            return ResponseEntity.status(HttpStatus.CREATED).body(cuidado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Estatísticas por tipo", description = "Retorna estatísticas de cuidados por tipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/estatisticas/tipo")
    public ResponseEntity<List<Object[]>> estatisticasPorTipo() {
        try {
            List<Object[]> estatisticas = cuidadoService.obterEstatisticasPorTipo();
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Estatísticas por planta", description = "Retorna estatísticas de cuidados por planta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/estatisticas/planta")
    public ResponseEntity<List<Object[]>> estatisticasPorPlanta() {
        try {
            List<Object[]> estatisticas = cuidadoService.obterEstatisticasPorPlanta();
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

