package com.horta.dto;

import com.horta.model.Cuidado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para transferência de dados da entidade Cuidado
 */
public class CuidadoDTO {

    private Long id;

    @NotNull(message = "ID da planta é obrigatório")
    private Long plantaId;

    private String plantaNome; // Para exibição

    @NotNull(message = "Data do cuidado é obrigatória")
    private LocalDate data;

    @NotNull(message = "Tipo de cuidado é obrigatório")
    private Cuidado.TipoCuidado tipo;

    private String observacoes;
    private LocalDateTime dataCriacao;
    private String usuarioResponsavel;

    // Campos adicionais para exibição
    private String tipoDescricao;
    private Boolean cuidadoRecente;

    // Construtores
    public CuidadoDTO() {}

    public CuidadoDTO(Long plantaId, LocalDate data, Cuidado.TipoCuidado tipo, String observacoes) {
        this.plantaId = plantaId;
        this.data = data;
        this.tipo = tipo;
        this.observacoes = observacoes;
    }

    public CuidadoDTO(Long plantaId, String plantaNome, LocalDate data, Cuidado.TipoCuidado tipo, String observacoes) {
        this(plantaId, data, tipo, observacoes);
        this.plantaNome = plantaNome;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlantaId() {
        return plantaId;
    }

    public void setPlantaId(Long plantaId) {
        this.plantaId = plantaId;
    }

    public String getPlantaNome() {
        return plantaNome;
    }

    public void setPlantaNome(String plantaNome) {
        this.plantaNome = plantaNome;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Cuidado.TipoCuidado getTipo() {
        return tipo;
    }

    public void setTipo(Cuidado.TipoCuidado tipo) {
        this.tipo = tipo;
        this.tipoDescricao = tipo != null ? tipo.getDescricao() : null;
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

    public String getTipoDescricao() {
        return tipoDescricao;
    }

    public void setTipoDescricao(String tipoDescricao) {
        this.tipoDescricao = tipoDescricao;
    }

    public Boolean getCuidadoRecente() {
        return cuidadoRecente;
    }

    public void setCuidadoRecente(Boolean cuidadoRecente) {
        this.cuidadoRecente = cuidadoRecente;
    }

    @Override
    public String toString() {
        return "CuidadoDTO{" +
                "id=" + id +
                ", plantaId=" + plantaId +
                ", plantaNome='" + plantaNome + '\'' +
                ", data=" + data +
                ", tipo=" + tipo +
                ", observacoes='" + observacoes + '\'' +
                ", usuarioResponsavel='" + usuarioResponsavel + '\'' +
                '}';
    }
}

