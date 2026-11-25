package com.escola_musica.nota_certa.api.controller;

import com.escola_musica.nota_certa.api.dto.filter.AulaFilterDTO;
import com.escola_musica.nota_certa.api.dto.request.AulaRequestDTO;
import com.escola_musica.nota_certa.api.dto.request.StatusAulaRequestDTO;
import com.escola_musica.nota_certa.api.dto.response.AulaResponseDTO;
import com.escola_musica.nota_certa.enums.Modalidade;
import com.escola_musica.nota_certa.enums.StatusAula;
import com.escola_musica.nota_certa.service.AulaService;
import jakarta.validation.Valid;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/aulas")

public class AulaController {
    private final AulaService aulaService;

    public AulaController(AulaService aulaService) {
        this.aulaService = aulaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AulaResponseDTO salvar(@Valid @RequestBody AulaRequestDTO dto) {
        return aulaService.salvar(dto);
    }

    @GetMapping
    public Page<AulaResponseDTO> listarComFiltros(
            @RequestParam(required = false) StatusAula status,
            @RequestParam(required = false) Modalidade modalidade,
            @RequestParam(required = false) Long alunoId,
            @RequestParam(required = false) Long cursoId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dataFim,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        AulaFilterDTO filtro = new AulaFilterDTO(
                status, modalidade, alunoId, cursoId, dataInicio, dataFim
        );

        return aulaService.listarComFiltros(filtro, pageable);
    }

    @GetMapping("/{id}")
    public AulaResponseDTO buscarPorId(@PathVariable Long id) {
        return aulaService.buscarPorId(id);
    }

    @PatchMapping("/{id}/status")
    public AulaResponseDTO atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusAulaRequestDTO dto
    ) {
        return aulaService.atualizarStatus(id, dto.status());
    }
}
