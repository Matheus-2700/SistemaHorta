package com.horta.service;

import com.horta.dto.CuidadoDTO;
import com.horta.model.Cuidado;
import com.horta.model.Planta;
import com.horta.repository.CuidadoRepository;
import com.horta.repository.PlantaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para CuidadoService
 */
@ExtendWith(MockitoExtension.class)
class CuidadoServiceTest {

    @Mock
    private CuidadoRepository cuidadoRepository;

    @Mock
    private PlantaRepository plantaRepository;

    @InjectMocks
    private CuidadoService cuidadoService;

    private Planta planta;
    private Cuidado cuidado;
    private CuidadoDTO cuidadoDTO;

    @BeforeEach
    void setUp() {
        planta = new Planta();
        planta.setId(1L);
        planta.setNome("Tomate");
        planta.setTipo("Hortaliça");
        planta.setDataPlantio(LocalDate.now().minusDays(30));

        cuidado = new Cuidado();
        cuidado.setId(1L);
        cuidado.setPlanta(planta);
        cuidado.setData(LocalDate.now());
        cuidado.setTipo(Cuidado.TipoCuidado.REGA);
        cuidado.setObservacoes("Rega normal");
        cuidado.setDataCriacao(LocalDateTime.now());
        cuidado.setUsuarioResponsavel("João");

        cuidadoDTO = new CuidadoDTO();
        cuidadoDTO.setPlantaId(1L);
        cuidadoDTO.setData(LocalDate.now());
        cuidadoDTO.setTipo(Cuidado.TipoCuidado.REGA);
        cuidadoDTO.setObservacoes("Rega normal");
        cuidadoDTO.setUsuarioResponsavel("João");
    }

    @Test
    void deveRegistrarCuidadoComSucesso() {
        // Given
        when(plantaRepository.findById(1L)).thenReturn(Optional.of(planta));
        when(cuidadoRepository.save(any(Cuidado.class))).thenReturn(cuidado);

        // When
        CuidadoDTO resultado = cuidadoService.registrarCuidado(cuidadoDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(Cuidado.TipoCuidado.REGA, resultado.getTipo());
        assertEquals("Rega normal", resultado.getObservacoes());
        verify(plantaRepository, times(1)).findById(1L);
        verify(cuidadoRepository, times(1)).save(any(Cuidado.class));
    }

    @Test
    void deveLancarExcecaoQuandoPlantaNaoExistir() {
        // Given
        when(plantaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            cuidadoService.registrarCuidado(cuidadoDTO);
        });
    }

    @Test
    void deveLancarExcecaoQuandoPlantaIdForNulo() {
        // Given
        cuidadoDTO.setPlantaId(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            cuidadoService.registrarCuidado(cuidadoDTO);
        });
    }

    @Test
    void deveLancarExcecaoQuandoDataForNula() {
        // Given
        cuidadoDTO.setData(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            cuidadoService.registrarCuidado(cuidadoDTO);
        });
    }

    @Test
    void deveLancarExcecaoQuandoTipoForNulo() {
        // Given
        cuidadoDTO.setTipo(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            cuidadoService.registrarCuidado(cuidadoDTO);
        });
    }

    @Test
    void deveLancarExcecaoQuandoDataForNoFuturo() {
        // Given
        cuidadoDTO.setData(LocalDate.now().plusDays(1));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            cuidadoService.registrarCuidado(cuidadoDTO);
        });
    }

    @Test
    void deveBuscarCuidadoPorIdComSucesso() {
        // Given
        when(cuidadoRepository.findById(1L)).thenReturn(Optional.of(cuidado));

        // When
        Optional<CuidadoDTO> resultado = cuidadoService.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(Cuidado.TipoCuidado.REGA, resultado.get().getTipo());
        verify(cuidadoRepository, times(1)).findById(1L);
    }

    @Test
    void deveRetornarVazioQuandoCuidadoNaoExistir() {
        // Given
        when(cuidadoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        Optional<CuidadoDTO> resultado = cuidadoService.buscarPorId(999L);

        // Then
        assertFalse(resultado.isPresent());
        verify(cuidadoRepository, times(1)).findById(999L);
    }

    @Test
    void deveListarTodosOsCuidados() {
        // Given
        List<Cuidado> cuidados = Arrays.asList(cuidado);
        when(cuidadoRepository.findAll()).thenReturn(cuidados);

        // When
        List<CuidadoDTO> resultado = cuidadoService.listarTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(Cuidado.TipoCuidado.REGA, resultado.get(0).getTipo());
        verify(cuidadoRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarCuidadosDaPlanta() {
        // Given
        List<Cuidado> cuidados = Arrays.asList(cuidado);
        when(cuidadoRepository.findByPlantaIdOrderByDataDesc(1L)).thenReturn(cuidados);

        // When
        List<CuidadoDTO> resultado = cuidadoService.buscarCuidadosDaPlanta(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getPlantaId());
        verify(cuidadoRepository, times(1)).findByPlantaIdOrderByDataDesc(1L);
    }

    @Test
    void deveBuscarCuidadosPorTipo() {
        // Given
        List<Cuidado> cuidados = Arrays.asList(cuidado);
        when(cuidadoRepository.findByTipo(Cuidado.TipoCuidado.REGA)).thenReturn(cuidados);

        // When
        List<CuidadoDTO> resultado = cuidadoService.buscarPorTipo(Cuidado.TipoCuidado.REGA);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(Cuidado.TipoCuidado.REGA, resultado.get(0).getTipo());
        verify(cuidadoRepository, times(1)).findByTipo(Cuidado.TipoCuidado.REGA);
    }

    @Test
    void deveAtualizarCuidadoComSucesso() {
        // Given
        when(cuidadoRepository.findById(1L)).thenReturn(Optional.of(cuidado));
        when(cuidadoRepository.save(any(Cuidado.class))).thenReturn(cuidado);

        cuidadoDTO.setObservacoes("Observação atualizada");

        // When
        CuidadoDTO resultado = cuidadoService.atualizarCuidado(1L, cuidadoDTO);

        // Then
        assertNotNull(resultado);
        verify(cuidadoRepository, times(1)).findById(1L);
        verify(cuidadoRepository, times(1)).save(any(Cuidado.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarCuidadoInexistente() {
        // Given
        when(cuidadoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            cuidadoService.atualizarCuidado(999L, cuidadoDTO);
        });
    }

    @Test
    void deveRemoverCuidadoComSucesso() {
        // Given
        when(cuidadoRepository.existsById(1L)).thenReturn(true);

        // When
        cuidadoService.removerCuidado(1L);

        // Then
        verify(cuidadoRepository, times(1)).existsById(1L);
        verify(cuidadoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoRemoverCuidadoInexistente() {
        // Given
        when(cuidadoRepository.existsById(anyLong())).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            cuidadoService.removerCuidado(999L);
        });
    }

    @Test
    void deveRegistrarRegaComSucesso() {
        // Given
        when(plantaRepository.findById(1L)).thenReturn(Optional.of(planta));
        when(cuidadoRepository.save(any(Cuidado.class))).thenReturn(cuidado);

        // When
        CuidadoDTO resultado = cuidadoService.registrarRega(1L, "Rega teste", "João");

        // Then
        assertNotNull(resultado);
        assertEquals(Cuidado.TipoCuidado.REGA, resultado.getTipo());
        verify(cuidadoRepository, times(1)).save(any(Cuidado.class));
    }

    @Test
    void deveRegistrarPodaComSucesso() {
        // Given
        cuidado.setTipo(Cuidado.TipoCuidado.PODA);
        when(plantaRepository.findById(1L)).thenReturn(Optional.of(planta));
        when(cuidadoRepository.save(any(Cuidado.class))).thenReturn(cuidado);

        // When
        CuidadoDTO resultado = cuidadoService.registrarPoda(1L, "Poda teste", "João");

        // Then
        assertNotNull(resultado);
        assertEquals(Cuidado.TipoCuidado.PODA, resultado.getTipo());
        verify(cuidadoRepository, times(1)).save(any(Cuidado.class));
    }

    @Test
    void deveRegistrarColheitaComSucesso() {
        // Given
        cuidado.setTipo(Cuidado.TipoCuidado.COLHEITA);
        when(plantaRepository.findById(1L)).thenReturn(Optional.of(planta));
        when(cuidadoRepository.save(any(Cuidado.class))).thenReturn(cuidado);

        // When
        CuidadoDTO resultado = cuidadoService.registrarColheita(1L, "Colheita teste", "João");

        // Then
        assertNotNull(resultado);
        assertEquals(Cuidado.TipoCuidado.COLHEITA, resultado.getTipo());
        verify(cuidadoRepository, times(1)).save(any(Cuidado.class));
    }

    @Test
    void deveBuscarCuidadosRecentes() {
        // Given
        List<Cuidado> cuidados = Arrays.asList(cuidado);
        when(cuidadoRepository.findCuidadosRecentes(any(LocalDate.class))).thenReturn(cuidados);

        // When
        List<CuidadoDTO> resultado = cuidadoService.buscarCuidadosRecentes();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(cuidadoRepository, times(1)).findCuidadosRecentes(any(LocalDate.class));
    }

    @Test
    void deveBuscarCuidadosDeHoje() {
        // Given
        List<Cuidado> cuidados = Arrays.asList(cuidado);
        when(cuidadoRepository.findCuidadosDeHoje(any(LocalDate.class))).thenReturn(cuidados);

        // When
        List<CuidadoDTO> resultado = cuidadoService.buscarCuidadosDeHoje();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(cuidadoRepository, times(1)).findCuidadosDeHoje(any(LocalDate.class));
    }
}

