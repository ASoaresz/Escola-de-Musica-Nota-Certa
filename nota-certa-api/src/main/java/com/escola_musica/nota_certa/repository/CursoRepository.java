package com.escola_musica.nota_certa.repository;

import com.escola_musica.nota_certa.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository
        extends JpaRepository<Aluno, Long> {

}
