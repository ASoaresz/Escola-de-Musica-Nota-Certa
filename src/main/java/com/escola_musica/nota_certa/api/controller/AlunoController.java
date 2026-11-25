package com.escola_musica.nota_certa.api.controller;

import com.escola_musica.nota_certa.api.dto.filter.AlunoFilterDTO;
import com.escola_musica.nota_certa.api.dto.request.AlunoRequestDTO;
import com.escola_musica.nota_certa.api.dto.response.AlunoResponseDTO;
import com.escola_musica.nota_certa.api.validation.AtualizarAluno;
import com.escola_musica.nota_certa.service.AlunoService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping
    public Page<AlunoResponseDTO> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cidade,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        AlunoFilterDTO filtro = new AlunoFilterDTO(nome, cidade);

        return alunoService.listarComFiltros(filtro, pageable);
    }

    @GetMapping("/{id}")
    public AlunoResponseDTO buscarPorId(@PathVariable Long id) {
        return alunoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlunoResponseDTO salvar(
            @Valid @RequestBody AlunoRequestDTO dto
    ) {
        return alunoService.salvar(dto);
    }

    @PutMapping("/{id}")
    public AlunoResponseDTO atualizar(
            @PathVariable Long id,
            @Validated({Default.class, AtualizarAluno.class})
            @RequestBody AlunoRequestDTO dto
    ) {
        return alunoService.atualizar(id, dto);
    }
}
