package com.horta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa uma planta no sistema de gerenciamento de horta
 */
@Entity
@Table(name = "plantas")
public class Planta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome da planta é obrigatório")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "Tipo da planta é obrigatório")
    @Column(nullable = false, length = 50)
    private String tipo; // ex: hortaliça, fruta, erva

    @NotNull(message = "Data de plantio é obrigatória")
    @Column(name = "data_plantio", nullable = false)
    private LocalDate dataPlantio;

    @Positive(message = "Ciclo em dias deve ser positivo")
    @Column(name = "ciclo_dias")
    private Integer cicloDias; // tempo médio até colheita

    @Column(length = 100)
    private String regiao; // para sugestões

    @Column(length = 500)
    private String descricao;

    @Column(name = "dias_entre_regas")
    private Integer diasEntreRegas = 3; // padrão: regar a cada 3 dias

    @Column(name = "dias_entre_podas")
    private Integer diasEntrePodas = 30; // padrão: podar a cada 30 dias

    @OneToMany(mappedBy = "planta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cuidado> cuidados = new ArrayList<>();

    // Construtores
    public Planta() {}

    public Planta(String nome, String tipo, LocalDate dataPlantio, Integer cicloDias, String regiao) {
        this.nome = nome;
        this.tipo = tipo;
        this.dataPlantio = dataPlantio;
        this.cicloDias = cicloDias;
        this.regiao = regiao;
    }

    // Métodos de negócio
    public LocalDate calcularDataColheita() {
        if (cicloDias != null && dataPlantio != null) {
            return dataPlantio.plusDays(cicloDias);
        }
        return null;
    }

    public LocalDate calcularProximaRega() {
        if (dataPlantio != null) {
            return dataPlantio.plusDays(diasEntreRegas);
        }
        return null;
    }

    public LocalDate calcularProximaPoda() {
        if (dataPlantio != null) {
            return dataPlantio.plusDays(diasEntrePodas);
        }
        return null;
    }

    public boolean isTempoColheita() {
        LocalDate dataColheita = calcularDataColheita();
        return dataColheita != null && !LocalDate.now().isBefore(dataColheita);
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

    public List<Cuidado> getCuidados() {
        return cuidados;
    }

    public void setCuidados(List<Cuidado> cuidados) {
        this.cuidados = cuidados;
    }

    @Override
    public String toString() {
        return "Planta{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", tipo='" + tipo + '\'' +
                ", dataPlantio=" + dataPlantio +
                ", cicloDias=" + cicloDias +
                ", regiao='" + regiao + '\'' +
                '}';
    }
}

