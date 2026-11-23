package com.escola_musica.nota_certa.service;

import com.escola_musica.nota_certa.entity.Aula;
import com.escola_musica.nota_certa.repository.AulaRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service

public class AulaService {
    private final AulaRepository aulaRepository;

    public AulaService(AulaRepository aulaRepository) {this.aulaRepository = aulaRepository;}

    public Page<Aula> listarTodos(Pageable pageable) {
        return aulaRepository.findAll(pageable);
    }

    public Optional<Aula> buscarPorId(Long id) {
        return aulaRepository.findById(id);
    }

    public Aula salvar(Aula aula) {
        return aulaRepository.save(aula);
    }
}
