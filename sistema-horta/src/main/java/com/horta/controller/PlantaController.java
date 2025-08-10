package com.horta.controller;

import com.horta.dto.PlantaDTO;
import com.horta.service.PlantaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller REST para operações com plantas
 */
@RestController
@RequestMapping("/plantas")
@Tag(name = "Plantas", description = "API para gerenciamento de plantas")
@CrossOrigin(origins = "*")
public class PlantaController {

    @Autowired
    private PlantaService plantaService;

    @Operation(summary = "Lista todas as plantas", description = "Retorna uma lista com todas as plantas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de plantas retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<PlantaDTO>> listarTodas() {
        try {
            List<PlantaDTO> plantas = plantaService.listarTodas();
            return ResponseEntity.ok(plantas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Cadastra nova planta", description = "Cadastra uma nova planta no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Planta cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<PlantaDTO> cadastrarPlanta(@Valid @RequestBody PlantaDTO plantaDTO) {
        try {
            PlantaDTO plantaSalva = plantaService.salvarPlanta(plantaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(plantaSalva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Busca planta por ID", description = "Retorna os detalhes de uma planta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Planta encontrada"),
            @ApiResponse(responseCode = "404", description = "Planta não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PlantaDTO> buscarPorId(
            @Parameter(description = "ID da planta") @PathVariable Long id) {
        try {
            Optional<PlantaDTO> planta = plantaService.buscarPorId(id);
            return planta.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Atualiza planta", description = "Atualiza os dados de uma planta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Planta atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Planta não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PlantaDTO> atualizarPlanta(
            @Parameter(description = "ID da planta") @PathVariable Long id,
            @Valid @RequestBody PlantaDTO plantaDTO) {
        try {
            PlantaDTO plantaAtualizada = plantaService.atualizarPlanta(id, plantaDTO);
            return ResponseEntity.ok(plantaAtualizada);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrada")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Remove planta", description = "Remove uma planta do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Planta removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Planta não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPlanta(
            @Parameter(description = "ID da planta") @PathVariable Long id) {
        try {
            plantaService.removerPlanta(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Busca plantas por nome", description = "Busca plantas que contenham o nome especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/buscar/nome")
    public ResponseEntity<List<PlantaDTO>> buscarPorNome(
            @Parameter(description = "Nome da planta") @RequestParam String nome) {
        try {
            List<PlantaDTO> plantas = plantaService.buscarPorNome(nome);
            return ResponseEntity.ok(plantas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Busca plantas por tipo", description = "Busca plantas de um tipo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/buscar/tipo")
    public ResponseEntity<List<PlantaDTO>> buscarPorTipo(
            @Parameter(description = "Tipo da planta") @RequestParam String tipo) {
        try {
            List<PlantaDTO> plantas = plantaService.buscarPorTipo(tipo);
            return ResponseEntity.ok(plantas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Busca plantas por região", description = "Busca plantas de uma região específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/buscar/regiao")
    public ResponseEntity<List<PlantaDTO>> buscarPorRegiao(
            @Parameter(description = "Região") @RequestParam String regiao) {
        try {
            List<PlantaDTO> plantas = plantaService.buscarPorRegiao(regiao);
            return ResponseEntity.ok(plantas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Plantas prontas para colheita", description = "Retorna plantas que estão prontas para colheita")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/colheita")
    public ResponseEntity<List<PlantaDTO>> plantasProntasParaColheita() {
        try {
            List<PlantaDTO> plantas = plantaService.buscarPlantasProntasParaColheita();
            return ResponseEntity.ok(plantas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Plantas que necessitam rega", description = "Retorna plantas que precisam ser regadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/rega")
    public ResponseEntity<List<PlantaDTO>> plantasQueNecessitamRega() {
        try {
            List<PlantaDTO> plantas = plantaService.buscarPlantasQueNecessitamRega();
            return ResponseEntity.ok(plantas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Plantas que necessitam poda", description = "Retorna plantas que precisam ser podadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/poda")
    public ResponseEntity<List<PlantaDTO>> plantasQueNecessitamPoda() {
        try {
            List<PlantaDTO> plantas = plantaService.buscarPlantasQueNecessitamPoda();
            return ResponseEntity.ok(plantas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Sugestões de plantas", description = "Sugere plantas ideais para uma região")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sugestões retornadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/sugestoes")
    public ResponseEntity<List<PlantaDTO>> sugerirPlantasParaRegiao(
            @Parameter(description = "Região") @RequestParam String regiao) {
        try {
            List<PlantaDTO> plantas = plantaService.sugerirPlantasParaRegiao(regiao);
            return ResponseEntity.ok(plantas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Plantas recentes", description = "Retorna plantas plantadas recentemente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/recentes")
    public ResponseEntity<List<PlantaDTO>> plantasRecentes(
            @Parameter(description = "Número de dias") @RequestParam(defaultValue = "30") int dias) {
        try {
            List<PlantaDTO> plantas = plantaService.buscarPlantasRecentes(dias);
            return ResponseEntity.ok(plantas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Estatísticas por tipo", description = "Retorna estatísticas de plantas por tipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/estatisticas/tipo")
    public ResponseEntity<List<Object[]>> estatisticasPorTipo() {
        try {
            List<Object[]> estatisticas = plantaService.obterEstatisticasPorTipo();
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

