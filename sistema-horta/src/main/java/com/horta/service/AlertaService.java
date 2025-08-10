package com.horta.service;

import com.horta.dto.PlantaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service para gerenciamento de alertas automáticos
 */
@Service
public class AlertaService {

    private static final Logger logger = LoggerFactory.getLogger(AlertaService.class);

    @Autowired
    private PlantaService plantaService;

    /**
     * Verifica plantas que precisam de rega (executa diariamente às 8h)
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void verificarPlantasQueNecessitamRega() {
        logger.info("Iniciando verificação de plantas que necessitam rega...");
        
        try {
            List<PlantaDTO> plantas = plantaService.buscarPlantasQueNecessitamRega();
            
            if (!plantas.isEmpty()) {
                logger.warn("🚰 ALERTA: {} plantas precisam ser regadas hoje!", plantas.size());
                
                for (PlantaDTO planta : plantas) {
                    logger.warn("- {} (ID: {}) - Plantada em: {}", 
                            planta.getNome(), 
                            planta.getId(), 
                            planta.getDataPlantio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
                
                // Aqui você pode implementar envio de email, notificação push, etc.
                enviarNotificacaoRega(plantas);
            } else {
                logger.info("✅ Nenhuma planta precisa ser regada hoje.");
            }
        } catch (Exception e) {
            logger.error("Erro ao verificar plantas que necessitam rega: {}", e.getMessage(), e);
        }
    }

    /**
     * Verifica plantas que precisam de poda (executa semanalmente às segundas-feiras às 9h)
     */
    @Scheduled(cron = "0 0 9 * * MON")
    public void verificarPlantasQueNecessitamPoda() {
        logger.info("Iniciando verificação de plantas que necessitam poda...");
        
        try {
            List<PlantaDTO> plantas = plantaService.buscarPlantasQueNecessitamPoda();
            
            if (!plantas.isEmpty()) {
                logger.warn("✂️ ALERTA: {} plantas precisam ser podadas esta semana!", plantas.size());
                
                for (PlantaDTO planta : plantas) {
                    logger.warn("- {} (ID: {}) - Plantada em: {}", 
                            planta.getNome(), 
                            planta.getId(), 
                            planta.getDataPlantio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
                
                enviarNotificacaoPoda(plantas);
            } else {
                logger.info("✅ Nenhuma planta precisa ser podada esta semana.");
            }
        } catch (Exception e) {
            logger.error("Erro ao verificar plantas que necessitam poda: {}", e.getMessage(), e);
        }
    }

    /**
     * Verifica plantas prontas para colheita (executa diariamente às 18h)
     */
    @Scheduled(cron = "0 0 18 * * *")
    public void verificarPlantasProntasParaColheita() {
        logger.info("Iniciando verificação de plantas prontas para colheita...");
        
        try {
            List<PlantaDTO> plantas = plantaService.buscarPlantasProntasParaColheita();
            
            if (!plantas.isEmpty()) {
                logger.info("🌾 ÓTIMA NOTÍCIA: {} plantas estão prontas para colheita!", plantas.size());
                
                for (PlantaDTO planta : plantas) {
                    logger.info("- {} (ID: {}) - Plantada em: {} - Ciclo: {} dias", 
                            planta.getNome(), 
                            planta.getId(), 
                            planta.getDataPlantio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            planta.getCicloDias());
                }
                
                enviarNotificacaoColheita(plantas);
            } else {
                logger.info("📅 Nenhuma planta está pronta para colheita hoje.");
            }
        } catch (Exception e) {
            logger.error("Erro ao verificar plantas prontas para colheita: {}", e.getMessage(), e);
        }
    }

    /**
     * Relatório semanal (executa aos domingos às 20h)
     */
    @Scheduled(cron = "0 0 20 * * SUN")
    public void gerarRelatorioSemanal() {
        logger.info("Gerando relatório semanal da horta...");
        
        try {
            List<PlantaDTO> todasPlantas = plantaService.listarTodas();
            List<PlantaDTO> plantasRecentes = plantaService.buscarPlantasRecentes(7);
            
            logger.info("📊 RELATÓRIO SEMANAL DA HORTA");
            logger.info("═══════════════════════════════");
            logger.info("🌱 Total de plantas: {}", todasPlantas.size());
            logger.info("🆕 Plantas plantadas esta semana: {}", plantasRecentes.size());
            
            // Estatísticas por tipo
            var estatisticas = plantaService.obterEstatisticasPorTipo();
            logger.info("📈 Plantas por tipo:");
            for (Object[] stat : estatisticas) {
                logger.info("   - {}: {} plantas", stat[0], stat[1]);
            }
            
            logger.info("═══════════════════════════════");
            
        } catch (Exception e) {
            logger.error("Erro ao gerar relatório semanal: {}", e.getMessage(), e);
        }
    }

    /**
     * Limpeza de dados antigos (executa mensalmente no dia 1 às 2h)
     */
    @Scheduled(cron = "0 0 2 1 * *")
    public void limpezaDadosAntigos() {
        logger.info("Iniciando limpeza de dados antigos...");
        
        try {
            // Aqui você pode implementar lógica para arquivar ou remover dados muito antigos
            logger.info("🧹 Limpeza de dados concluída.");
        } catch (Exception e) {
            logger.error("Erro na limpeza de dados antigos: {}", e.getMessage(), e);
        }
    }

    // Métodos auxiliares para envio de notificações

    private void enviarNotificacaoRega(List<PlantaDTO> plantas) {
        // Implementar envio de email, SMS, push notification, etc.
        logger.info("📧 Enviando notificação de rega para {} plantas", plantas.size());
        
        // Exemplo de implementação futura:
        // emailService.enviarAlertaRega(plantas);
        // smsService.enviarAlertaRega(plantas);
        // pushNotificationService.enviarAlertaRega(plantas);
    }

    private void enviarNotificacaoPoda(List<PlantaDTO> plantas) {
        logger.info("📧 Enviando notificação de poda para {} plantas", plantas.size());
    }

    private void enviarNotificacaoColheita(List<PlantaDTO> plantas) {
        logger.info("📧 Enviando notificação de colheita para {} plantas", plantas.size());
    }

    /**
     * Método para testar alertas manualmente
     */
    public void testarAlertas() {
        logger.info("🧪 Executando teste manual de alertas...");
        verificarPlantasQueNecessitamRega();
        verificarPlantasQueNecessitamPoda();
        verificarPlantasProntasParaColheita();
        logger.info("✅ Teste de alertas concluído.");
    }
}

