package com.escola_musica.nota_certa.api.controller;

import com.escola_musica.nota_certa.api.dto.filter.CursoFilterDTO;
import com.escola_musica.nota_certa.api.dto.request.CursoRequestDTO;
import com.escola_musica.nota_certa.api.dto.response.CursoResponseDTO;
import com.escola_musica.nota_certa.api.validation.AtualizarCurso;
import com.escola_musica.nota_certa.enums.Instrumento;
import com.escola_musica.nota_certa.service.CursoService;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public Page<CursoResponseDTO> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Instrumento instrumento,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        CursoFilterDTO filtro = new CursoFilterDTO(nome, instrumento);

        return cursoService.listarComFiltros(filtro, pageable);
    }

    @GetMapping("/{id}")
    public CursoResponseDTO buscarPorId(@PathVariable Long id) {
        return cursoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CursoResponseDTO salvar(
            @Valid @RequestBody CursoRequestDTO dto
    ) {
        return cursoService.salvar(dto);
    }

    @PutMapping("/{id}")
    public CursoResponseDTO atualizar(
            @PathVariable Long id,
            @Validated({Default.class, AtualizarCurso.class})
            @RequestBody CursoRequestDTO dto
    ) {
        return cursoService.atualizar(id, dto);
    }
}
