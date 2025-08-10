package com.horta.controller;

import com.horta.service.AlertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para gerenciamento de alertas
 */
@RestController
@RequestMapping("/alertas")
@Tag(name = "Alertas", description = "API para gerenciamento de alertas automáticos")
@CrossOrigin(origins = "*")
public class AlertaController {

    @Autowired
    private AlertaService alertaService;

    @Operation(summary = "Testar alertas", description = "Executa manualmente todos os tipos de alertas para teste")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alertas testados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/testar")
    public ResponseEntity<String> testarAlertas() {
        try {
            alertaService.testarAlertas();
            return ResponseEntity.ok("Alertas testados com sucesso! Verifique os logs para detalhes.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao testar alertas: " + e.getMessage());
        }
    }

    @Operation(summary = "Verificar plantas que necessitam rega", description = "Executa manualmente a verificação de plantas que precisam ser regadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificação executada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/rega")
    public ResponseEntity<String> verificarRega() {
        try {
            alertaService.verificarPlantasQueNecessitamRega();
            return ResponseEntity.ok("Verificação de rega executada com sucesso! Verifique os logs para detalhes.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao verificar rega: " + e.getMessage());
        }
    }

    @Operation(summary = "Verificar plantas que necessitam poda", description = "Executa manualmente a verificação de plantas que precisam ser podadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificação executada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/poda")
    public ResponseEntity<String> verificarPoda() {
        try {
            alertaService.verificarPlantasQueNecessitamPoda();
            return ResponseEntity.ok("Verificação de poda executada com sucesso! Verifique os logs para detalhes.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao verificar poda: " + e.getMessage());
        }
    }

    @Operation(summary = "Verificar plantas prontas para colheita", description = "Executa manualmente a verificação de plantas prontas para colheita")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificação executada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/colheita")
    public ResponseEntity<String> verificarColheita() {
        try {
            alertaService.verificarPlantasProntasParaColheita();
            return ResponseEntity.ok("Verificação de colheita executada com sucesso! Verifique os logs para detalhes.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao verificar colheita: " + e.getMessage());
        }
    }

    @Operation(summary = "Gerar relatório semanal", description = "Executa manualmente a geração do relatório semanal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/relatorio")
    public ResponseEntity<String> gerarRelatorio() {
        try {
            alertaService.gerarRelatorioSemanal();
            return ResponseEntity.ok("Relatório semanal gerado com sucesso! Verifique os logs para detalhes.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    @Operation(summary = "Status dos alertas", description = "Retorna informações sobre o sistema de alertas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status retornado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/status")
    public ResponseEntity<String> statusAlertas() {
        try {
            String status = """
                    📊 SISTEMA DE ALERTAS AUTOMÁTICOS
                    ═══════════════════════════════════
                    
                    🚰 Verificação de Rega: Diariamente às 08:00
                    ✂️ Verificação de Poda: Segundas-feiras às 09:00
                    🌾 Verificação de Colheita: Diariamente às 18:00
                    📈 Relatório Semanal: Domingos às 20:00
                    🧹 Limpeza de Dados: Dia 1 de cada mês às 02:00
                    
                    ✅ Sistema ativo e funcionando!
                    
                    💡 Use os endpoints POST para testar manualmente:
                    - POST /alertas/testar - Testa todos os alertas
                    - POST /alertas/rega - Verifica plantas que precisam de rega
                    - POST /alertas/poda - Verifica plantas que precisam de poda
                    - POST /alertas/colheita - Verifica plantas prontas para colheita
                    - POST /alertas/relatorio - Gera relatório semanal
                    """;
            
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao obter status: " + e.getMessage());
        }
    }
}

