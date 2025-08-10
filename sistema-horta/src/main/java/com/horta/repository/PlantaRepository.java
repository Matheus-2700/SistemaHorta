package com.horta.repository;

import com.horta.model.Planta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações com a entidade Planta
 */
@Repository
public interface PlantaRepository extends JpaRepository<Planta, Long> {

    /**
     * Busca plantas por nome (case insensitive)
     */
    List<Planta> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca plantas por tipo
     */
    List<Planta> findByTipoIgnoreCase(String tipo);

    /**
     * Busca plantas por região
     */
    List<Planta> findByRegiaoIgnoreCase(String regiao);

    /**
     * Busca plantas plantadas em um período específico
     */
    List<Planta> findByDataPlantioBetween(LocalDate dataInicio, LocalDate dataFim);

    /**
     * Busca plantas que estão prontas para colheita
     */
    @Query("SELECT p FROM Planta p WHERE p.dataPlantio IS NOT NULL AND p.cicloDias IS NOT NULL " +
           "AND FUNCTION('DATE_ADD', p.dataPlantio, INTERVAL p.cicloDias DAY) <= :dataAtual")
    List<Planta> findPlantasProntasParaColheita(@Param("dataAtual") LocalDate dataAtual);

    /**
     * Busca plantas que precisam de rega
     */
    @Query("SELECT p FROM Planta p WHERE p.dataPlantio IS NOT NULL AND p.diasEntreRegas IS NOT NULL " +
           "AND FUNCTION('DATE_ADD', p.dataPlantio, INTERVAL p.diasEntreRegas DAY) <= :dataAtual")
    List<Planta> findPlantasQueNecessitamRega(@Param("dataAtual") LocalDate dataAtual);

    /**
     * Busca plantas que precisam de poda
     */
    @Query("SELECT p FROM Planta p WHERE p.dataPlantio IS NOT NULL AND p.diasEntrePodas IS NOT NULL " +
           "AND FUNCTION('DATE_ADD', p.dataPlantio, INTERVAL p.diasEntrePodas DAY) <= :dataAtual")
    List<Planta> findPlantasQueNecessitamPoda(@Param("dataAtual") LocalDate dataAtual);

    /**
     * Busca plantas por tipo e região
     */
    List<Planta> findByTipoIgnoreCaseAndRegiaoIgnoreCase(String tipo, String regiao);

    /**
     * Conta plantas por tipo
     */
    @Query("SELECT p.tipo, COUNT(p) FROM Planta p GROUP BY p.tipo")
    List<Object[]> countPlantasPorTipo();

    /**
     * Busca plantas plantadas recentemente (últimos 30 dias)
     */
    @Query("SELECT p FROM Planta p WHERE p.dataPlantio >= :dataLimite ORDER BY p.dataPlantio DESC")
    List<Planta> findPlantasRecentementePlantadas(@Param("dataLimite") LocalDate dataLimite);;

    /**
     * Busca plantas com ciclo específico
     */
    List<Planta> findByCicloDiasBetween(Integer cicloMin, Integer cicloMax);

    /**
     * Verifica se existe planta com nome específico
     */
    boolean existsByNomeIgnoreCase(String nome);

    /**
     * Busca plantas ordenadas por data de plantio
     */
    List<Planta> findAllByOrderByDataPlantioDesc();

    /**
     * Busca plantas com descrição contendo texto específico
     */
    List<Planta> findByDescricaoContainingIgnoreCase(String texto);
}

