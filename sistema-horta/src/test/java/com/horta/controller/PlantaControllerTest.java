package com.horta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.horta.dto.PlantaDTO;
import com.horta.service.PlantaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para PlantaController
 */
@WebMvcTest(PlantaController.class)
class PlantaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlantaService plantaService;

    @Autowired
    private ObjectMapper objectMapper;

    private PlantaDTO plantaDTO;

    @BeforeEach
    void setUp() {
        plantaDTO = new PlantaDTO();
        plantaDTO.setId(1L);
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
    void deveListarTodasAsPlantas() throws Exception {
        // Given
        List<PlantaDTO> plantas = Arrays.asList(plantaDTO);
        when(plantaService.listarTodas()).thenReturn(plantas);

        // When & Then
        mockMvc.perform(get("/plantas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nome").value("Tomate"))
                .andExpect(jsonPath("$[0].tipo").value("Hortaliça"));

        verify(plantaService, times(1)).listarTodas();
    }

    @Test
    void deveCadastrarPlantaComSucesso() throws Exception {
        // Given
        PlantaDTO novaPlanta = new PlantaDTO();
        novaPlanta.setNome("Alface");
        novaPlanta.setTipo("Hortaliça");
        novaPlanta.setDataPlantio(LocalDate.now());
        novaPlanta.setCicloDias(60);
        novaPlanta.setRegiao("Sul");

        PlantaDTO plantaSalva = new PlantaDTO();
        plantaSalva.setId(2L);
        plantaSalva.setNome("Alface");
        plantaSalva.setTipo("Hortaliça");
        plantaSalva.setDataPlantio(LocalDate.now());
        plantaSalva.setCicloDias(60);
        plantaSalva.setRegiao("Sul");

        when(plantaService.salvarPlanta(any(PlantaDTO.class))).thenReturn(plantaSalva);

        // When & Then
        mockMvc.perform(post("/plantas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaPlanta)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nome").value("Alface"));

        verify(plantaService, times(1)).salvarPlanta(any(PlantaDTO.class));
    }

    @Test
    void deveRetornarBadRequestQuandoDadosInvalidos() throws Exception {
        // Given
        PlantaDTO plantaInvalida = new PlantaDTO();
        // Nome não definido (obrigatório)

        when(plantaService.salvarPlanta(any(PlantaDTO.class)))
                .thenThrow(new IllegalArgumentException("Nome é obrigatório"));

        // When & Then
        mockMvc.perform(post("/plantas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(plantaInvalida)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveBuscarPlantaPorId() throws Exception {
        // Given
        when(plantaService.buscarPorId(1L)).thenReturn(Optional.of(plantaDTO));

        // When & Then
        mockMvc.perform(get("/plantas/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Tomate"));

        verify(plantaService, times(1)).buscarPorId(1L);
    }

    @Test
    void deveRetornarNotFoundQuandoPlantaNaoExistir() throws Exception {
        // Given
        when(plantaService.buscarPorId(anyLong())).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/plantas/999"))
                .andExpect(status().isNotFound());

        verify(plantaService, times(1)).buscarPorId(999L);
    }

    @Test
    void deveAtualizarPlantaComSucesso() throws Exception {
        // Given
        PlantaDTO plantaAtualizada = new PlantaDTO();
        plantaAtualizada.setId(1L);
        plantaAtualizada.setNome("Tomate Atualizado");
        plantaAtualizada.setTipo("Hortaliça");
        plantaAtualizada.setDataPlantio(LocalDate.now().minusDays(30));
        plantaAtualizada.setCicloDias(90);
        plantaAtualizada.setRegiao("Sul");

        when(plantaService.atualizarPlanta(eq(1L), any(PlantaDTO.class))).thenReturn(plantaAtualizada);

        // When & Then
        mockMvc.perform(put("/plantas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(plantaAtualizada)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Tomate Atualizado"));

        verify(plantaService, times(1)).atualizarPlanta(eq(1L), any(PlantaDTO.class));
    }

    @Test
    void deveRemoverPlantaComSucesso() throws Exception {
        // Given
        doNothing().when(plantaService).removerPlanta(1L);

        // When & Then
        mockMvc.perform(delete("/plantas/1"))
                .andExpect(status().isNoContent());

        verify(plantaService, times(1)).removerPlanta(1L);
    }

    @Test
    void deveRetornarNotFoundAoRemoverPlantaInexistente() throws Exception {
        // Given
        doThrow(new RuntimeException("Planta não encontrada")).when(plantaService).removerPlanta(anyLong());

        // When & Then
        mockMvc.perform(delete("/plantas/999"))
                .andExpect(status().isNotFound());

        verify(plantaService, times(1)).removerPlanta(999L);
    }

    @Test
    void deveBuscarPlantasPorNome() throws Exception {
        // Given
        List<PlantaDTO> plantas = Arrays.asList(plantaDTO);
        when(plantaService.buscarPorNome("Tomate")).thenReturn(plantas);

        // When & Then
        mockMvc.perform(get("/plantas/buscar/nome")
                        .param("nome", "Tomate"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nome").value("Tomate"));

        verify(plantaService, times(1)).buscarPorNome("Tomate");
    }

    @Test
    void deveBuscarPlantasPorTipo() throws Exception {
        // Given
        List<PlantaDTO> plantas = Arrays.asList(plantaDTO);
        when(plantaService.buscarPorTipo("Hortaliça")).thenReturn(plantas);

        // When & Then
        mockMvc.perform(get("/plantas/buscar/tipo")
                        .param("tipo", "Hortaliça"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].tipo").value("Hortaliça"));

        verify(plantaService, times(1)).buscarPorTipo("Hortaliça");
    }

    @Test
    void deveBuscarPlantasPorRegiao() throws Exception {
        // Given
        List<PlantaDTO> plantas = Arrays.asList(plantaDTO);
        when(plantaService.buscarPorRegiao("Sul")).thenReturn(plantas);

        // When & Then
        mockMvc.perform(get("/plantas/buscar/regiao")
                        .param("regiao", "Sul"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].regiao").value("Sul"));

        verify(plantaService, times(1)).buscarPorRegiao("Sul");
    }

    @Test
    void deveBuscarPlantasProntasParaColheita() throws Exception {
        // Given
        List<PlantaDTO> plantas = Arrays.asList(plantaDTO);
        when(plantaService.buscarPlantasProntasParaColheita()).thenReturn(plantas);

        // When & Then
        mockMvc.perform(get("/plantas/colheita"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(plantaService, times(1)).buscarPlantasProntasParaColheita();
    }

    @Test
    void deveBuscarPlantasQueNecessitamRega() throws Exception {
        // Given
        List<PlantaDTO> plantas = Arrays.asList(plantaDTO);
        when(plantaService.buscarPlantasQueNecessitamRega()).thenReturn(plantas);

        // When & Then
        mockMvc.perform(get("/plantas/rega"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(plantaService, times(1)).buscarPlantasQueNecessitamRega();
    }

    @Test
    void deveSugerirPlantasParaRegiao() throws Exception {
        // Given
        List<PlantaDTO> plantas = Arrays.asList(plantaDTO);
        when(plantaService.sugerirPlantasParaRegiao("Sul")).thenReturn(plantas);

        // When & Then
        mockMvc.perform(get("/plantas/sugestoes")
                        .param("regiao", "Sul"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(plantaService, times(1)).sugerirPlantasParaRegiao("Sul");
    }
}

