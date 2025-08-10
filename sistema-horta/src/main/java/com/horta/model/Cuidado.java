package com.horta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade que representa um cuidado realizado com uma planta
 */
@Entity
@Table(name = "cuidados")
public class Cuidado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planta_id", nullable = false)
    @NotNull(message = "Planta é obrigatória")
    private Planta planta;

    @NotNull(message = "Data do cuidado é obrigatória")
    @Column(nullable = false)
    private LocalDate data;

    @NotBlank(message = "Tipo de cuidado é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoCuidado tipo;

    @Column(length = 1000)
    private String observacoes;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "usuario_responsavel", length = 100)
    private String usuarioResponsavel;

    // Enum para tipos de cuidado
    public enum TipoCuidado {
        REGA("Rega"),
        PODA("Poda"),
        COLHEITA("Colheita"),
        FERTILIZACAO("Fertilização"),
        TRANSPLANTE("Transplante"),
        TRATAMENTO("Tratamento"),
        OUTROS("Outros");

        private final String descricao;

        TipoCuidado(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    // Construtores
    public Cuidado() {
        this.dataCriacao = LocalDateTime.now();
    }

    public Cuidado(Planta planta, LocalDate data, TipoCuidado tipo, String observacoes) {
        this();
        this.planta = planta;
        this.data = data;
        this.tipo = tipo;
        this.observacoes = observacoes;
    }

    // Métodos de negócio
    @PrePersist
    protected void onCreate() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
    }

    public boolean isCuidadoRecente() {
        return data != null && data.isAfter(LocalDate.now().minusDays(7));
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public TipoCuidado getTipo() {
        return tipo;
    }

    public void setTipo(TipoCuidado tipo) {
        this.tipo = tipo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

    public void setUsuarioResponsavel(String usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }

    @Override
    public String toString() {
        return "Cuidado{" +
                "id=" + id +
                ", data=" + data +
                ", tipo=" + tipo +
                ", observacoes='" + observacoes + '\'' +
                ", usuarioResponsavel='" + usuarioResponsavel + '\'' +
                '}';
    }
}

