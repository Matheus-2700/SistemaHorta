package com.horta.service;

import com.horta.dto.CuidadoDTO;
import com.horta.model.Cuidado;
import com.horta.model.Planta;
import com.horta.repository.CuidadoRepository;
import com.horta.repository.PlantaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para operações de negócio com cuidados
 */
@Service
@Transactional
public class CuidadoService {

    @Autowired
    private CuidadoRepository cuidadoRepository;

    @Autowired
    private PlantaRepository plantaRepository;

    /**
     * Registra um novo cuidado
     */
    public CuidadoDTO registrarCuidado(CuidadoDTO cuidadoDTO) {
        validarCuidado(cuidadoDTO);
        
        Planta planta = plantaRepository.findById(cuidadoDTO.getPlantaId())
                .orElseThrow(() -> new RuntimeException("Planta não encontrada com ID: " + cuidadoDTO.getPlantaId()));

        Cuidado cuidado = convertToEntity(cuidadoDTO, planta);
        Cuidado cuidadoSalvo = cuidadoRepository.save(cuidado);
        
        return convertToDTO(cuidadoSalvo);
    }

    /**
     * Atualiza um cuidado existente
     */
    public CuidadoDTO atualizarCuidado(Long id, CuidadoDTO cuidadoDTO) {
        Cuidado cuidadoExistente = cuidadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuidado não encontrado com ID: " + id));

        validarCuidado(cuidadoDTO);

        // Atualiza os campos
        cuidadoExistente.setData(cuidadoDTO.getData());
        cuidadoExistente.setTipo(cuidadoDTO.getTipo());
        cuidadoExistente.setObservacoes(cuidadoDTO.getObservacoes());
        cuidadoExistente.setUsuarioResponsavel(cuidadoDTO.getUsuarioResponsavel());

        Cuidado cuidadoAtualizado = cuidadoRepository.save(cuidadoExistente);
        return convertToDTO(cuidadoAtualizado);
    }

    /**
     * Busca cuidado por ID
     */
    @Transactional(readOnly = true)
    public Optional<CuidadoDTO> buscarPorId(Long id) {
        return cuidadoRepository.findById(id)
                .map(this::convertToDTO);
    }

    /**
     * Lista todos os cuidados
     */
    @Transactional(readOnly = true)
    public List<CuidadoDTO> listarTodos() {
        return cuidadoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca cuidados de uma planta específica
     */
    @Transactional(readOnly = true)
    public List<CuidadoDTO> buscarCuidadosDaPlanta(Long plantaId) {
        return cuidadoRepository.findByPlantaIdOrderByDataDesc(plantaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca cuidados por tipo
     */
    @Transactional(readOnly = true)
    public List<CuidadoDTO> buscarPorTipo(Cuidado.TipoCuidado tipo) {
        return cuidadoRepository.findByTipo(tipo).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca cuidados em um período específico
     */
    @Transactional(readOnly = true)
    public List<CuidadoDTO> buscarCuidadosNoPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return cuidadoRepository.findByDataBetweenOrderByDataDesc(dataInicio, dataFim).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca cuidados recentes (últimos 7 dias)
     */
    @Transactional(readOnly = true)
    public List<CuidadoDTO> buscarCuidadosRecentes() {
        LocalDate dataLimite = LocalDate.now().minusDays(7);
        return cuidadoRepository.findCuidadosRecentes(dataLimite).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca cuidados de hoje
     */
    @Transactional(readOnly = true)
    public List<CuidadoDTO> buscarCuidadosDeHoje() {
        return cuidadoRepository.findCuidadosDeHoje(LocalDate.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca último cuidado de um tipo específico para uma planta
     */
    @Transactional(readOnly = true)
    public Optional<CuidadoDTO> buscarUltimoCuidadoPorTipo(Long plantaId, Cuidado.TipoCuidado tipo) {
        Planta planta = plantaRepository.findById(plantaId)
                .orElseThrow(() -> new RuntimeException("Planta não encontrada com ID: " + plantaId));

        Cuidado ultimoCuidado = cuidadoRepository.findUltimoCuidadoPorTipo(planta, tipo);
        return ultimoCuidado != null ? Optional.of(convertToDTO(ultimoCuidado)) : Optional.empty();
    }

    /**
     * Busca cuidados por usuário responsável
     */
    @Transactional(readOnly = true)
    public List<CuidadoDTO> buscarCuidadosPorUsuario(String usuarioResponsavel) {
        return cuidadoRepository.findByUsuarioResponsavelOrderByDataDesc(usuarioResponsavel).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Remove um cuidado
     */
    public void removerCuidado(Long id) {
        if (!cuidadoRepository.existsById(id)) {
            throw new RuntimeException("Cuidado não encontrado com ID: " + id);
        }
        cuidadoRepository.deleteById(id);
    }

    /**
     * Obtém estatísticas de cuidados por tipo
     */
    @Transactional(readOnly = true)
    public List<Object[]> obterEstatisticasPorTipo() {
        return cuidadoRepository.countCuidadosPorTipo();
    }

    /**
     * Obtém estatísticas de cuidados por planta
     */
    @Transactional(readOnly = true)
    public List<Object[]> obterEstatisticasPorPlanta() {
        return cuidadoRepository.countCuidadosPorPlanta();
    }

    /**
     * Verifica se já existe cuidado de um tipo para uma planta em uma data
     */
    @Transactional(readOnly = true)
    public boolean existeCuidadoNaData(Long plantaId, Cuidado.TipoCuidado tipo, LocalDate data) {
        Planta planta = plantaRepository.findById(plantaId)
                .orElseThrow(() -> new RuntimeException("Planta não encontrada com ID: " + plantaId));
        
        return cuidadoRepository.existsByPlantaAndTipoAndData(planta, tipo, data);
    }

    /**
     * Registra rega para uma planta
     */
    public CuidadoDTO registrarRega(Long plantaId, String observacoes, String usuarioResponsavel) {
        CuidadoDTO cuidadoDTO = new CuidadoDTO();
        cuidadoDTO.setPlantaId(plantaId);
        cuidadoDTO.setData(LocalDate.now());
        cuidadoDTO.setTipo(Cuidado.TipoCuidado.REGA);
        cuidadoDTO.setObservacoes(observacoes);
        cuidadoDTO.setUsuarioResponsavel(usuarioResponsavel);
        
        return registrarCuidado(cuidadoDTO);
    }

    /**
     * Registra poda para uma planta
     */
    public CuidadoDTO registrarPoda(Long plantaId, String observacoes, String usuarioResponsavel) {
        CuidadoDTO cuidadoDTO = new CuidadoDTO();
        cuidadoDTO.setPlantaId(plantaId);
        cuidadoDTO.setData(LocalDate.now());
        cuidadoDTO.setTipo(Cuidado.TipoCuidado.PODA);
        cuidadoDTO.setObservacoes(observacoes);
        cuidadoDTO.setUsuarioResponsavel(usuarioResponsavel);
        
        return registrarCuidado(cuidadoDTO);
    }

    /**
     * Registra colheita para uma planta
     */
    public CuidadoDTO registrarColheita(Long plantaId, String observacoes, String usuarioResponsavel) {
        CuidadoDTO cuidadoDTO = new CuidadoDTO();
        cuidadoDTO.setPlantaId(plantaId);
        cuidadoDTO.setData(LocalDate.now());
        cuidadoDTO.setTipo(Cuidado.TipoCuidado.COLHEITA);
        cuidadoDTO.setObservacoes(observacoes);
        cuidadoDTO.setUsuarioResponsavel(usuarioResponsavel);
        
        return registrarCuidado(cuidadoDTO);
    }

    // Métodos auxiliares

    private void validarCuidado(CuidadoDTO cuidadoDTO) {
        if (cuidadoDTO.getPlantaId() == null) {
            throw new IllegalArgumentException("ID da planta é obrigatório");
        }
        
        if (cuidadoDTO.getData() == null) {
            throw new IllegalArgumentException("Data do cuidado é obrigatória");
        }
        
        if (cuidadoDTO.getTipo() == null) {
            throw new IllegalArgumentException("Tipo de cuidado é obrigatório");
        }
        
        if (cuidadoDTO.getData().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data do cuidado não pode ser no futuro");
        }
    }

    private CuidadoDTO convertToDTO(Cuidado cuidado) {
        CuidadoDTO dto = new CuidadoDTO();
        dto.setId(cuidado.getId());
        dto.setPlantaId(cuidado.getPlanta().getId());
        dto.setPlantaNome(cuidado.getPlanta().getNome());
        dto.setData(cuidado.getData());
        dto.setTipo(cuidado.getTipo());
        dto.setObservacoes(cuidado.getObservacoes());
        dto.setDataCriacao(cuidado.getDataCriacao());
        dto.setUsuarioResponsavel(cuidado.getUsuarioResponsavel());
        dto.setCuidadoRecente(cuidado.isCuidadoRecente());
        
        return dto;
    }

    private Cuidado convertToEntity(CuidadoDTO dto, Planta planta) {
        Cuidado cuidado = new Cuidado();
        cuidado.setPlanta(planta);
        cuidado.setData(dto.getData());
        cuidado.setTipo(dto.getTipo());
        cuidado.setObservacoes(dto.getObservacoes());
        cuidado.setUsuarioResponsavel(dto.getUsuarioResponsavel());
        
        return cuidado;
    }
}

