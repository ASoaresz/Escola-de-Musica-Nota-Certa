package com.escola_musica.nota_certa.service;

import com.escola_musica.nota_certa.entity.Aula;
import com.escola_musica.nota_certa.repository.AulaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class AulaService {
    private final AulaRepository aulaRepository;

    public AulaService(AulaRepository aulaRepository) {this.aulaRepository = aulaRepository;}

    public List<Aula> listarTodos() {
        return aulaRepository.findAll();
    }

    public Optional<Aula> buscarPorId(Long id) {
        return aulaRepository.findById(id);
    }

    public Aula salvar(Aula aula) {
        return aulaRepository.save(aula);
    }
}
