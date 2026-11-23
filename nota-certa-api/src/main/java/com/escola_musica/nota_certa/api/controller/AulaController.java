package com.escola_musica.nota_certa.api.controller;

import com.escola_musica.nota_certa.service.AulaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.escola_musica.nota_certa.entity.Aula;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/aulas")

public class AulaController {
    private final AulaService aulaService;

    public AulaController(AulaService aulaService) {
        this.aulaService = aulaService;
    }

    @GetMapping
    public Page<Aula> listarTodos(Pageable pageable) {
        return aulaService.listarTodos(pageable);
    }

    @GetMapping("/{id}")
    public Aula buscarPorId(@PathVariable Long id) {
        return aulaService.buscarPorId(id)
                .orElseThrow();
    }

    @PostMapping
    public Aula salvar(@RequestBody Aula aula) {
        return aulaService.salvar(aula);
    }


}
