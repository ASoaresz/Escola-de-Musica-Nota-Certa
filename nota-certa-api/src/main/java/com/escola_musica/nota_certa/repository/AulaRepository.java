package com.escola_musica.nota_certa.repository;

import com.escola_musica.nota_certa.entity.Aula;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AulaRepository
        extends JpaRepository<Aula, Long> {
}
