package com.horta.repository;

import com.horta.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações com a entidade Usuario
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca usuário por email
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca usuário por email (case insensitive)
     */
    Optional<Usuario> findByEmailIgnoreCase(String email);

    /**
     * Busca usuários por nome (case insensitive)
     */
    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca usuários por região
     */
    List<Usuario> findByRegiaoIgnoreCase(String regiao);

    /**
     * Busca usuários ativos
     */
    List<Usuario> findByAtivoTrue();

    /**
     * Busca usuários inativos
     */
    List<Usuario> findByAtivoFalse();

    /**
     * Busca usuários por tipo
     */
    List<Usuario> findByTipo(Usuario.TipoUsuario tipo);

    /**
     * Busca usuários administradores
     */
    @Query("SELECT u FROM Usuario u WHERE u.tipo = 'ADMIN' AND u.ativo = true")
    List<Usuario> findAdministradoresAtivos();

    /**
     * Verifica se existe usuário com email específico
     */
    boolean existsByEmail(String email);

    /**
     * Verifica se existe usuário com email específico (case insensitive)
     */
    boolean existsByEmailIgnoreCase(String email);

    /**
     * Busca usuários criados em um período específico
     */
    List<Usuario> findByDataCriacaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    /**
     * Busca usuários que fizeram login recentemente
     */
    @Query("SELECT u FROM Usuario u WHERE u.ultimoAcesso >= :dataLimite ORDER BY u.ultimoAcesso DESC")
    List<Usuario> findUsuariosComAcessoRecente(@Param("dataLimite") LocalDateTime dataLimite);

    /**
     * Busca usuários que nunca fizeram login
     */
    @Query("SELECT u FROM Usuario u WHERE u.ultimoAcesso IS NULL")
    List<Usuario> findUsuariosSemLogin();

    /**
     * Conta usuários por região
     */
    @Query("SELECT u.regiao, COUNT(u) FROM Usuario u WHERE u.regiao IS NOT NULL GROUP BY u.regiao")
    List<Object[]> countUsuariosPorRegiao();

    /**
     * Conta usuários por tipo
     */
    @Query("SELECT u.tipo, COUNT(u) FROM Usuario u GROUP BY u.tipo")
    List<Object[]> countUsuariosPorTipo();

    /**
     * Busca usuários ordenados por data de criação
     */
    List<Usuario> findAllByOrderByDataCriacaoDesc();

    /**
     * Busca usuários ordenados por último acesso
     */
    List<Usuario> findAllByOrderByUltimoAcessoDesc();

    /**
     * Atualiza último acesso do usuário
     */
    @Query("UPDATE Usuario u SET u.ultimoAcesso = :agora WHERE u.id = :usuarioId")
    void atualizarUltimoAcesso(@Param("usuarioId") Long usuarioId, @Param("agora") LocalDateTime agora);

    /**
     * Busca usuários inativos há muito tempo
     */
    @Query("SELECT u FROM Usuario u WHERE u.ultimoAcesso < :dataLimite OR u.ultimoAcesso IS NULL")
    List<Usuario> findUsuariosInativos(@Param("dataLimite") LocalDateTime dataLimite);

    /**
     * Conta total de usuários ativos
     */
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.ativo = true")
    Long countUsuariosAtivos();

    /**
     * Busca usuários por nome e região
     */
    List<Usuario> findByNomeContainingIgnoreCaseAndRegiaoIgnoreCase(String nome, String regiao);
}

