package com.horta.service;

import com.horta.dto.PlantaDTO;
import com.horta.model.Planta;
import com.horta.repository.PlantaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para operações de negócio com plantas
 */
@Service
@Transactional
public class PlantaService {

    @Autowired
    private PlantaRepository plantaRepository;

    /**
     * Salva uma nova planta
     */
    public PlantaDTO salvarPlanta(PlantaDTO plantaDTO) {
        validarPlanta(plantaDTO);
        
        Planta planta = convertToEntity(plantaDTO);
        Planta plantaSalva = plantaRepository.save(planta);
        
        return convertToDTO(plantaSalva);
    }

    /**
     * Atualiza uma planta existente
     */
    public PlantaDTO atualizarPlanta(Long id, PlantaDTO plantaDTO) {
        Planta plantaExistente = plantaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Planta não encontrada com ID: " + id));

        validarPlanta(plantaDTO);
        
        // Atualiza os campos
        plantaExistente.setNome(plantaDTO.getNome());
        plantaExistente.setTipo(plantaDTO.getTipo());
        plantaExistente.setDataPlantio(plantaDTO.getDataPlantio());
        plantaExistente.setCicloDias(plantaDTO.getCicloDias());
        plantaExistente.setRegiao(plantaDTO.getRegiao());
        plantaExistente.setDescricao(plantaDTO.getDescricao());
        plantaExistente.setDiasEntreRegas(plantaDTO.getDiasEntreRegas());
        plantaExistente.setDiasEntrePodas(plantaDTO.getDiasEntrePodas());

        Planta plantaAtualizada = plantaRepository.save(plantaExistente);
        return convertToDTO(plantaAtualizada);
    }

    /**
     * Busca planta por ID
     */
    @Transactional(readOnly = true)
    public Optional<PlantaDTO> buscarPorId(Long id) {
        return plantaRepository.findById(id)
                .map(this::convertToDTO);
    }

    /**
     * Lista todas as plantas
     */
    @Transactional(readOnly = true)
    public List<PlantaDTO> listarTodas() {
        return plantaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca plantas por nome
     */
    @Transactional(readOnly = true)
    public List<PlantaDTO> buscarPorNome(String nome) {
        return plantaRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca plantas por tipo
     */
    @Transactional(readOnly = true)
    public List<PlantaDTO> buscarPorTipo(String tipo) {
        return plantaRepository.findByTipoIgnoreCase(tipo).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca plantas por região
     */
    @Transactional(readOnly = true)
    public List<PlantaDTO> buscarPorRegiao(String regiao) {
        return plantaRepository.findByRegiaoIgnoreCase(regiao).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca plantas prontas para colheita
     */
    @Transactional(readOnly = true)
    public List<PlantaDTO> buscarPlantasProntasParaColheita() {
        return plantaRepository.findPlantasProntasParaColheita(LocalDate.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca plantas que necessitam rega
     */
    @Transactional(readOnly = true)
    public List<PlantaDTO> buscarPlantasQueNecessitamRega() {
        return plantaRepository.findPlantasQueNecessitamRega(LocalDate.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca plantas que necessitam poda
     */
    @Transactional(readOnly = true)
    public List<PlantaDTO> buscarPlantasQueNecessitamPoda() {
        return plantaRepository.findPlantasQueNecessitamPoda(LocalDate.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Sugere plantas para uma região
     */
    @Transactional(readOnly = true)
    public List<PlantaDTO> sugerirPlantasParaRegiao(String regiao) {
        // Busca plantas que já foram plantadas com sucesso na região
        return plantaRepository.findByRegiaoIgnoreCase(regiao).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Remove uma planta
     */
    public void removerPlanta(Long id) {
        if (!plantaRepository.existsById(id)) {
            throw new RuntimeException("Planta não encontrada com ID: " + id);
        }
        plantaRepository.deleteById(id);
    }

    /**
     * Busca plantas plantadas recentemente
     */
    @Transactional(readOnly = true)
    public List<PlantaDTO> buscarPlantasRecentes(int dias) {
        LocalDate dataLimite = LocalDate.now().minusDays(dias);
        return plantaRepository.findPlantasRecentementePlantadas(dataLimite).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtém estatísticas de plantas por tipo
     */
    @Transactional(readOnly = true)
    public List<Object[]> obterEstatisticasPorTipo() {
        return plantaRepository.countPlantasPorTipo();
    }

    // Métodos auxiliares

    private void validarPlanta(PlantaDTO plantaDTO) {
        if (plantaDTO.getNome() == null || plantaDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da planta é obrigatório");
        }
        
        if (plantaDTO.getTipo() == null || plantaDTO.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo da planta é obrigatório");
        }
        
        if (plantaDTO.getDataPlantio() == null) {
            throw new IllegalArgumentException("Data de plantio é obrigatória");
        }
        
        if (plantaDTO.getDataPlantio().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de plantio não pode ser no futuro");
        }
        
        if (plantaDTO.getCicloDias() != null && plantaDTO.getCicloDias() <= 0) {
            throw new IllegalArgumentException("Ciclo em dias deve ser positivo");
        }
    }

    private PlantaDTO convertToDTO(Planta planta) {
        PlantaDTO dto = new PlantaDTO();
        dto.setId(planta.getId());
        dto.setNome(planta.getNome());
        dto.setTipo(planta.getTipo());
        dto.setDataPlantio(planta.getDataPlantio());
        dto.setCicloDias(planta.getCicloDias());
        dto.setRegiao(planta.getRegiao());
        dto.setDescricao(planta.getDescricao());
        dto.setDiasEntreRegas(planta.getDiasEntreRegas());
        dto.setDiasEntrePodas(planta.getDiasEntrePodas());
        
        // Campos calculados
        dto.setDataColheita(planta.calcularDataColheita());
        dto.setProximaRega(planta.calcularProximaRega());
        dto.setProximaPoda(planta.calcularProximaPoda());
        dto.setTempoColheita(planta.isTempoColheita());
        dto.setTotalCuidados(planta.getCuidados().size());
        
        return dto;
    }

    private Planta convertToEntity(PlantaDTO dto) {
        Planta planta = new Planta();
        planta.setNome(dto.getNome());
        planta.setTipo(dto.getTipo());
        planta.setDataPlantio(dto.getDataPlantio());
        planta.setCicloDias(dto.getCicloDias());
        planta.setRegiao(dto.getRegiao());
        planta.setDescricao(dto.getDescricao());
        planta.setDiasEntreRegas(dto.getDiasEntreRegas() != null ? dto.getDiasEntreRegas() : 3);
        planta.setDiasEntrePodas(dto.getDiasEntrePodas() != null ? dto.getDiasEntrePodas() : 30);
        
        return planta;
    }
}

