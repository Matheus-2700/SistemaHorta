package com.horta.controller;

import com.horta.dto.CuidadoDTO;
import com.horta.dto.PlantaDTO;
import com.horta.model.Cuidado;
import com.horta.service.CuidadoService;
import com.horta.service.PlantaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller para páginas web com Thymeleaf
 */
@Controller
@RequestMapping("/")
public class WebController {

    @Autowired
    private PlantaService plantaService;

    @Autowired
    private CuidadoService cuidadoService;

    @GetMapping
    public String index(Model model) {
        try {
            List<PlantaDTO> plantas = plantaService.listarTodas();
            List<PlantaDTO> plantasRega = plantaService.buscarPlantasQueNecessitamRega();
            List<PlantaDTO> plantasColheita = plantaService.buscarPlantasProntasParaColheita();
            List<CuidadoDTO> cuidadosRecentes = cuidadoService.buscarCuidadosRecentes();

            model.addAttribute("totalPlantas", plantas.size());
            model.addAttribute("plantasRega", plantasRega.size());
            model.addAttribute("plantasColheita", plantasColheita.size());
            model.addAttribute("cuidadosRecentes", cuidadosRecentes.size());
            model.addAttribute("plantas", plantas.subList(0, Math.min(5, plantas.size())));
            model.addAttribute("cuidados", cuidadosRecentes.subList(0, Math.min(5, cuidadosRecentes.size())));

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar dados: " + e.getMessage());
        }
        return "index";
    }

    @GetMapping("/plantas")
    public String listarPlantas(Model model) {
        try {
            List<PlantaDTO> plantas = plantaService.listarTodas();
            model.addAttribute("plantas", plantas);
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar plantas: " + e.getMessage());
        }
        return "plantas/lista";
    }

    @GetMapping("/plantas/nova")
    public String novaPlanta(Model model) {
        model.addAttribute("planta", new PlantaDTO());
        return "plantas/form";
    }

    @PostMapping("/plantas/salvar")
    public String salvarPlanta(@ModelAttribute PlantaDTO planta, RedirectAttributes redirectAttributes) {
        try {
            if (planta.getId() != null) {
                plantaService.atualizarPlanta(planta.getId(), planta);
                redirectAttributes.addFlashAttribute("sucesso", "Planta atualizada com sucesso!");
            } else {
                plantaService.salvarPlanta(planta);
                redirectAttributes.addFlashAttribute("sucesso", "Planta cadastrada com sucesso!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao salvar planta: " + e.getMessage());
        }
        return "redirect:/plantas";
    }

    @GetMapping("/plantas/{id}/editar")
    public String editarPlanta(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<PlantaDTO> planta = plantaService.buscarPorId(id);
            if (planta.isPresent()) {
                model.addAttribute("planta", planta.get());
                return "plantas/form";
            } else {
                redirectAttributes.addFlashAttribute("erro", "Planta não encontrada!");
                return "redirect:/plantas";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao carregar planta: " + e.getMessage());
            return "redirect:/plantas";
        }
    }

    @GetMapping("/plantas/{id}/remover")
    public String removerPlanta(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            plantaService.removerPlanta(id);
            redirectAttributes.addFlashAttribute("sucesso", "Planta removida com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao remover planta: " + e.getMessage());
        }
        return "redirect:/plantas";
    }

    @GetMapping("/plantas/{id}")
    public String detalhesPlanta(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<PlantaDTO> planta = plantaService.buscarPorId(id);
            if (planta.isPresent()) {
                List<CuidadoDTO> cuidados = cuidadoService.buscarCuidadosDaPlanta(id);
                model.addAttribute("planta", planta.get());
                model.addAttribute("cuidados", cuidados);
                model.addAttribute("novoCuidado", new CuidadoDTO());
                return "plantas/detalhes";
            } else {
                redirectAttributes.addFlashAttribute("erro", "Planta não encontrada!");
                return "redirect:/plantas";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao carregar detalhes: " + e.getMessage());
            return "redirect:/plantas";
        }
    }

    @GetMapping("/cuidados")
    public String listarCuidados(Model model) {
        try {
            List<CuidadoDTO> cuidados = cuidadoService.listarTodos();
            model.addAttribute("cuidados", cuidados);
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar cuidados: " + e.getMessage());
        }
        return "cuidados/lista";
    }

    @PostMapping("/cuidados/salvar")
    public String salvarCuidado(@ModelAttribute CuidadoDTO cuidado, RedirectAttributes redirectAttributes) {
        try {
            if (cuidado.getData() == null) {
                cuidado.setData(LocalDate.now());
            }
            cuidadoService.registrarCuidado(cuidado);
            redirectAttributes.addFlashAttribute("sucesso", "Cuidado registrado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao registrar cuidado: " + e.getMessage());
        }
        return "redirect:/plantas/" + cuidado.getPlantaId();
    }

    @GetMapping("/alertas")
    public String alertas(Model model) {
        try {
            List<PlantaDTO> plantasRega = plantaService.buscarPlantasQueNecessitamRega();
            List<PlantaDTO> plantasPoda = plantaService.buscarPlantasQueNecessitamPoda();
            List<PlantaDTO> plantasColheita = plantaService.buscarPlantasProntasParaColheita();

            model.addAttribute("plantasRega", plantasRega);
            model.addAttribute("plantasPoda", plantasPoda);
            model.addAttribute("plantasColheita", plantasColheita);
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar alertas: " + e.getMessage());
        }
        return "alertas";
    }

    @GetMapping("/relatorios")
    public String relatorios(Model model) {
        try {
            List<PlantaDTO> todasPlantas = plantaService.listarTodas();
            List<PlantaDTO> plantasRecentes = plantaService.buscarPlantasRecentes(30);
            List<Object[]> estatisticasPlantas = plantaService.obterEstatisticasPorTipo();
            List<Object[]> estatisticasCuidados = cuidadoService.obterEstatisticasPorTipo();

            model.addAttribute("totalPlantas", todasPlantas.size());
            model.addAttribute("plantasRecentes", plantasRecentes.size());
            model.addAttribute("estatisticasPlantas", estatisticasPlantas);
            model.addAttribute("estatisticasCuidados", estatisticasCuidados);
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar relatórios: " + e.getMessage());
        }
        return "relatorios";
    }
}

