package com.horta.repository;

import com.horta.model.Cuidado;
import com.horta.model.Planta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório para operações com a entidade Cuidado
 */
@Repository
public interface CuidadoRepository extends JpaRepository<Cuidado, Long> {

    /**
     * Busca todos os cuidados de uma planta específica
     */
    List<Cuidado> findByPlantaOrderByDataDesc(Planta planta);

    /**
     * Busca cuidados de uma planta por ID
     */
    List<Cuidado> findByPlantaIdOrderByDataDesc(Long plantaId);

    /**
     * Busca cuidados por tipo
     */
    List<Cuidado> findByTipo(Cuidado.TipoCuidado tipo);

    /**
     * Busca cuidados por tipo e planta
     */
    List<Cuidado> findByTipoAndPlantaOrderByDataDesc(Cuidado.TipoCuidado tipo, Planta planta);

    /**
     * Busca cuidados realizados em um período específico
     */
    List<Cuidado> findByDataBetweenOrderByDataDesc(LocalDate dataInicio, LocalDate dataFim);

    /**
     * Busca cuidados de uma planta em um período específico
     */
    List<Cuidado> findByPlantaAndDataBetweenOrderByDataDesc(Planta planta, LocalDate dataInicio, LocalDate dataFim);

    /**
     * Busca último cuidado de um tipo específico para uma planta
     */
    @Query("SELECT c FROM Cuidado c WHERE c.planta = :planta AND c.tipo = :tipo ORDER BY c.data DESC LIMIT 1")
    Cuidado findUltimoCuidadoPorTipo(@Param("planta") Planta planta, @Param("tipo") Cuidado.TipoCuidado tipo);

    /**
     * Busca cuidados realizados por um usuário específico
     */
    List<Cuidado> findByUsuarioResponsavelOrderByDataDesc(String usuarioResponsavel);

    /**
     * Busca cuidados recentes (últimos 7 dias)
     */
    @Query("SELECT c FROM Cuidado c WHERE c.data >= :dataLimite ORDER BY c.data DESC")
    List<Cuidado> findCuidadosRecentes(@Param("dataLimite") LocalDate dataLimite);

    /**
     * Conta cuidados por tipo
     */
    @Query("SELECT c.tipo, COUNT(c) FROM Cuidado c GROUP BY c.tipo")
    List<Object[]> countCuidadosPorTipo();

    /**
     * Conta cuidados por planta
     */
    @Query("SELECT c.planta.nome, COUNT(c) FROM Cuidado c GROUP BY c.planta.nome ORDER BY COUNT(c) DESC")
    List<Object[]> countCuidadosPorPlanta();

    /**
     * Busca cuidados com observações contendo texto específico
     */
    List<Cuidado> findByObservacoesContainingIgnoreCase(String texto);

    /**
     * Verifica se existe cuidado de um tipo específico para uma planta em uma data
     */
    boolean existsByPlantaAndTipoAndData(Planta planta, Cuidado.TipoCuidado tipo, LocalDate data);

    /**
     * Busca cuidados criados em um período específico
     */
    List<Cuidado> findByDataCriacaoBetweenOrderByDataCriacaoDesc(LocalDateTime dataInicio, LocalDateTime dataFim);

    /**
     * Busca todos os cuidados ordenados por data de criação
     */
    List<Cuidado> findAllByOrderByDataCriacaoDesc();

    /**
     * Busca cuidados de hoje
     */
    @Query("SELECT c FROM Cuidado c WHERE c.data = :hoje ORDER BY c.dataCriacao DESC")
    List<Cuidado> findCuidadosDeHoje(@Param("hoje") LocalDate hoje);

    /**
     * Busca estatísticas de cuidados por mês
     */
    @Query("SELECT YEAR(c.data), MONTH(c.data), COUNT(c) FROM Cuidado c " +
           "GROUP BY YEAR(c.data), MONTH(c.data) ORDER BY YEAR(c.data) DESC, MONTH(c.data) DESC")
    List<Object[]> findEstatisticasCuidadosPorMes();

    /**
     * Remove cuidados antigos (mais de 1 ano)
     */
    @Query("DELETE FROM Cuidado c WHERE c.data < :dataLimite")
    void deleteCuidadosAntigos(@Param("dataLimite") LocalDate dataLimite);
}

