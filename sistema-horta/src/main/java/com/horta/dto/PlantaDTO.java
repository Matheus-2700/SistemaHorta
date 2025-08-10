package com.horta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * DTO para transferência de dados da entidade Planta
 */
public class PlantaDTO {

    private Long id;

    @NotBlank(message = "Nome da planta é obrigatório")
    private String nome;

    @NotBlank(message = "Tipo da planta é obrigatório")
    private String tipo;

    @NotNull(message = "Data de plantio é obrigatória")
    private LocalDate dataPlantio;

    @Positive(message = "Ciclo em dias deve ser positivo")
    private Integer cicloDias;

    private String regiao;
    private String descricao;
    private Integer diasEntreRegas;
    private Integer diasEntrePodas;

    // Campos calculados
    private LocalDate dataColheita;
    private LocalDate proximaRega;
    private LocalDate proximaPoda;
    private Boolean tempoColheita;
    private Integer totalCuidados;

    // Construtores
    public PlantaDTO() {}

    public PlantaDTO(String nome, String tipo, LocalDate dataPlantio, Integer cicloDias, String regiao) {
        this.nome = nome;
        this.tipo = tipo;
        this.dataPlantio = dataPlantio;
        this.cicloDias = cicloDias;
        this.regiao = regiao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getDataPlantio() {
        return dataPlantio;
    }

    public void setDataPlantio(LocalDate dataPlantio) {
        this.dataPlantio = dataPlantio;
    }

    public Integer getCicloDias() {
        return cicloDias;
    }

    public void setCicloDias(Integer cicloDias) {
        this.cicloDias = cicloDias;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getDiasEntreRegas() {
        return diasEntreRegas;
    }

    public void setDiasEntreRegas(Integer diasEntreRegas) {
        this.diasEntreRegas = diasEntreRegas;
    }

    public Integer getDiasEntrePodas() {
        return diasEntrePodas;
    }

    public void setDiasEntrePodas(Integer diasEntrePodas) {
        this.diasEntrePodas = diasEntrePodas;
    }

    public LocalDate getDataColheita() {
        return dataColheita;
    }

    public void setDataColheita(LocalDate dataColheita) {
        this.dataColheita = dataColheita;
    }

    public LocalDate getProximaRega() {
        return proximaRega;
    }

    public void setProximaRega(LocalDate proximaRega) {
        this.proximaRega = proximaRega;
    }

    public LocalDate getProximaPoda() {
        return proximaPoda;
    }

    public void setProximaPoda(LocalDate proximaPoda) {
        this.proximaPoda = proximaPoda;
    }

    public Boolean getTempoColheita() {
        return tempoColheita;
    }

    public void setTempoColheita(Boolean tempoColheita) {
        this.tempoColheita = tempoColheita;
    }

    public Integer getTotalCuidados() {
        return totalCuidados;
    }

    public void setTotalCuidados(Integer totalCuidados) {
        this.totalCuidados = totalCuidados;
    }

    @Override
    public String toString() {
        return "PlantaDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", tipo='" + tipo + '\'' +
                ", dataPlantio=" + dataPlantio +
                ", cicloDias=" + cicloDias +
                ", regiao='" + regiao + '\'' +
                '}';
    }
}

