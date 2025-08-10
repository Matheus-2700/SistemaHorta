package com.horta.dto;

import com.horta.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * DTO para transferência de dados da entidade Usuario
 */
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    private String email;

    // Senha não é incluída no DTO por segurança
    // Para alteração de senha, usar DTO específico

    private String regiao;
    private LocalDateTime dataCriacao;
    private LocalDateTime ultimoAcesso;
    private Boolean ativo;
    private Usuario.TipoUsuario tipo;

    // Campos adicionais para exibição
    private String tipoDescricao;
    private Boolean acessoRecente;
    private Long diasSemAcesso;

    // Construtores
    public UsuarioDTO() {}

    public UsuarioDTO(String nome, String email, String regiao) {
        this.nome = nome;
        this.email = email;
        this.regiao = regiao;
    }

    public UsuarioDTO(String nome, String email, String regiao, Usuario.TipoUsuario tipo) {
        this(nome, email, regiao);
        this.tipo = tipo;
        this.tipoDescricao = tipo != null ? tipo.getDescricao() : null;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(LocalDateTime ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Usuario.TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(Usuario.TipoUsuario tipo) {
        this.tipo = tipo;
        this.tipoDescricao = tipo != null ? tipo.getDescricao() : null;
    }

    public String getTipoDescricao() {
        return tipoDescricao;
    }

    public void setTipoDescricao(String tipoDescricao) {
        this.tipoDescricao = tipoDescricao;
    }

    public Boolean getAcessoRecente() {
        return acessoRecente;
    }

    public void setAcessoRecente(Boolean acessoRecente) {
        this.acessoRecente = acessoRecente;
    }

    public Long getDiasSemAcesso() {
        return diasSemAcesso;
    }

    public void setDiasSemAcesso(Long diasSemAcesso) {
        this.diasSemAcesso = diasSemAcesso;
    }

    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", regiao='" + regiao + '\'' +
                ", ativo=" + ativo +
                ", tipo=" + tipo +
                '}';
    }
}

