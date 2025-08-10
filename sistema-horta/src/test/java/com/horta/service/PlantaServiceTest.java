package com.horta.service;

import com.horta.dto.PlantaDTO;
import com.horta.model.Planta;
import com.horta.repository.PlantaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para PlantaService
 */
@ExtendWith(MockitoExtension.class)
class PlantaServiceTest {

    @Mock
    private PlantaRepository plantaRepository;

    @InjectMocks
    private PlantaService plantaService;

    private Planta planta;
    private PlantaDTO plantaDTO;

    @BeforeEach
    void setUp() {
        planta = new Planta();
        planta.setId(1L);
        planta.setNome("Tomate");
        planta.setTipo("Hortaliça");
        planta.setDataPlantio(LocalDate.now().minusDays(30));
        planta.setCicloDias(90);
        planta.setRegiao("Sul");
        planta.setDescricao("Tomate cereja");
        planta.setDiasEntreRegas(3);
        planta.setDiasEntrePodas(30);

        plantaDTO = new PlantaDTO();
        plantaDTO.setNome("Tomate");
        plantaDTO.setTipo("Hortaliça");
        plantaDTO.setDataPlantio(LocalDate.now().minusDays(30));
        plantaDTO.setCicloDias(90);
        plantaDTO.setRegiao("Sul");
        plantaDTO.setDescricao("Tomate cereja");
        plantaDTO.setDiasEntreRegas(3);
        plantaDTO.setDiasEntrePodas(30);
    }

    @Test
    void deveSalvarPlantaComSucesso() {
        // Given
        when(plantaRepository.save(any(Planta.class))).thenReturn(planta);

        // When
        PlantaDTO resultado = plantaService.salvarPlanta(plantaDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Tomate", resultado.getNome());
        assertEquals("Hortaliça", resultado.getTipo());
        verify(plantaRepository, times(1)).save(any(Planta.class));
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNulo() {
        // Given
        plantaDTO.setNome(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            plantaService.salvarPlanta(plantaDTO);
        });
    }

    @Test
    void deveLancarExcecaoQuandoTipoForNulo() {
        // Given
        plantaDTO.setTipo(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            plantaService.salvarPlanta(plantaDTO);
        });
    }

    @Test
    void deveLancarExcecaoQuandoDataPlantioForNula() {
        // Given
        plantaDTO.setDataPlantio(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            plantaService.salvarPlanta(plantaDTO);
        });
    }

    @Test
    void deveLancarExcecaoQuandoDataPlantioForNoFuturo() {
        // Given
        plantaDTO.setDataPlantio(LocalDate.now().plusDays(1));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            plantaService.salvarPlanta(plantaDTO);
        });
    }

    @Test
    void deveBuscarPlantaPorIdComSucesso() {
        // Given
        when(plantaRepository.findById(1L)).thenReturn(Optional.of(planta));

        // When
        Optional<PlantaDTO> resultado = plantaService.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Tomate", resultado.get().getNome());
        verify(plantaRepository, times(1)).findById(1L);
    }

    @Test
    void deveRetornarVazioQuandoPlantaNaoExistir() {
        // Given
        when(plantaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        Optional<PlantaDTO> resultado = plantaService.buscarPorId(999L);

        // Then
        assertFalse(resultado.isPresent());
        verify(plantaRepository, times(1)).findById(999L);
    }

    @Test
    void deveListarTodasAsPlantas() {
        // Given
        List<Planta> plantas = Arrays.asList(planta);
        when(plantaRepository.findAll()).thenReturn(plantas);

        // When
        List<PlantaDTO> resultado = plantaService.listarTodas();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Tomate", resultado.get(0).getNome());
        verify(plantaRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarPlantasPorNome() {
        // Given
        List<Planta> plantas = Arrays.asList(planta);
        when(plantaRepository.findByNomeContainingIgnoreCase("Tomate")).thenReturn(plantas);

        // When
        List<PlantaDTO> resultado = plantaService.buscarPorNome("Tomate");

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Tomate", resultado.get(0).getNome());
        verify(plantaRepository, times(1)).findByNomeContainingIgnoreCase("Tomate");
    }

    @Test
    void deveBuscarPlantasPorTipo() {
        // Given
        List<Planta> plantas = Arrays.asList(planta);
        when(plantaRepository.findByTipoIgnoreCase("Hortaliça")).thenReturn(plantas);

        // When
        List<PlantaDTO> resultado = plantaService.buscarPorTipo("Hortaliça");

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Hortaliça", resultado.get(0).getTipo());
        verify(plantaRepository, times(1)).findByTipoIgnoreCase("Hortaliça");
    }

    @Test
    void deveBuscarPlantasPorRegiao() {
        // Given
        List<Planta> plantas = Arrays.asList(planta);
        when(plantaRepository.findByRegiaoIgnoreCase("Sul")).thenReturn(plantas);

        // When
        List<PlantaDTO> resultado = plantaService.buscarPorRegiao("Sul");

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Sul", resultado.get(0).getRegiao());
        verify(plantaRepository, times(1)).findByRegiaoIgnoreCase("Sul");
    }

    @Test
    void deveAtualizarPlantaComSucesso() {
        // Given
        when(plantaRepository.findById(1L)).thenReturn(Optional.of(planta));
        when(plantaRepository.save(any(Planta.class))).thenReturn(planta);

        plantaDTO.setNome("Tomate Atualizado");

        // When
        PlantaDTO resultado = plantaService.atualizarPlanta(1L, plantaDTO);

        // Then
        assertNotNull(resultado);
        verify(plantaRepository, times(1)).findById(1L);
        verify(plantaRepository, times(1)).save(any(Planta.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarPlantaInexistente() {
        // Given
        when(plantaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            plantaService.atualizarPlanta(999L, plantaDTO);
        });
    }

    @Test
    void deveRemoverPlantaComSucesso() {
        // Given
        when(plantaRepository.existsById(1L)).thenReturn(true);

        // When
        plantaService.removerPlanta(1L);

        // Then
        verify(plantaRepository, times(1)).existsById(1L);
        verify(plantaRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoRemoverPlantaInexistente() {
        // Given
        when(plantaRepository.existsById(anyLong())).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            plantaService.removerPlanta(999L);
        });
    }

    @Test
    void deveBuscarPlantasProntasParaColheita() {
        // Given
        List<Planta> plantas = Arrays.asList(planta);
        when(plantaRepository.findPlantasProntasParaColheita(any(LocalDate.class))).thenReturn(plantas);

        // When
        List<PlantaDTO> resultado = plantaService.buscarPlantasProntasParaColheita();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(plantaRepository, times(1)).findPlantasProntasParaColheita(any(LocalDate.class));
    }

    @Test
    void deveBuscarPlantasQueNecessitamRega() {
        // Given
        List<Planta> plantas = Arrays.asList(planta);
        when(plantaRepository.findPlantasQueNecessitamRega(any(LocalDate.class))).thenReturn(plantas);

        // When
        List<PlantaDTO> resultado = plantaService.buscarPlantasQueNecessitamRega();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(plantaRepository, times(1)).findPlantasQueNecessitamRega(any(LocalDate.class));
    }

    @Test
    void deveBuscarPlantasQueNecessitamPoda() {
        // Given
        List<Planta> plantas = Arrays.asList(planta);
        when(plantaRepository.findPlantasQueNecessitamPoda(any(LocalDate.class))).thenReturn(plantas);

        // When
        List<PlantaDTO> resultado = plantaService.buscarPlantasQueNecessitamPoda();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(plantaRepository, times(1)).findPlantasQueNecessitamPoda(any(LocalDate.class));
    }
}

