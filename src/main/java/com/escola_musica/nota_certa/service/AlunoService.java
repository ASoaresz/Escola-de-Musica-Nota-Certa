package com.escola_musica.nota_certa.service;

import com.escola_musica.nota_certa.api.dto.filter.AlunoFilterDTO;
import com.escola_musica.nota_certa.api.dto.request.AlunoRequestDTO;
import com.escola_musica.nota_certa.api.dto.response.AlunoResponseDTO;
import com.escola_musica.nota_certa.api.exception.RecursoNaoEncontradoException;
import com.escola_musica.nota_certa.entity.Aluno;
import com.escola_musica.nota_certa.repository.AlunoRepository;
import com.escola_musica.nota_certa.repository.specification.AlunoSpecification;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.escola_musica.nota_certa.api.exception.EmailJaCadastradoException;

import java.time.LocalDateTime;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Page<AlunoResponseDTO> listarComFiltros(
            AlunoFilterDTO filtro,
            Pageable pageable
    ) {
        Specification<Aluno> spec = (root, query, cb) -> cb.conjunction();

        if (filtro.nome() != null && !filtro.nome().isBlank()) {
            spec = spec.and(AlunoSpecification.nomeContem(filtro.nome()));
        }

        if (filtro.cidade() != null && !filtro.cidade().isBlank()) {
            spec = spec.and(AlunoSpecification.cidadeIgual(filtro.cidade()));
        }

        return alunoRepository.findAll(spec, pageable)
                .map(this::paraResponse);
    }

    public AlunoResponseDTO buscarPorId(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Aluno não encontrado com ID: " + id
                        )
                );

        return paraResponse(aluno);
    }

    public AlunoResponseDTO salvar(AlunoRequestDTO dto) {

        if (alunoRepository.existsByEmail(dto.email())) {
            throw new EmailJaCadastradoException(
                    "Já existe um aluno cadastrado com o e-mail: " + dto.email()
            );
        }

        Aluno aluno = new Aluno();

        aluno.setNome(dto.nome());
        aluno.setEmail(dto.email());
        aluno.setTelefone(dto.telefone());
        aluno.setCidade(dto.cidade());
        aluno.setDataCadastro(LocalDateTime.now());
        aluno.setAtivo(true);

        Aluno alunoSalvo = alunoRepository.save(aluno);

        return paraResponse(alunoSalvo);
    }

    public AlunoResponseDTO atualizar(Long id, AlunoRequestDTO dto) {

        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Aluno não encontrado com ID: " + id
                        )
                );

        if (!aluno.getEmail().equalsIgnoreCase(dto.email())
                && alunoRepository.existsByEmail(dto.email())) {

            throw new EmailJaCadastradoException(
                    "Já existe um aluno cadastrado com o e-mail: " + dto.email()
            );
        }

        aluno.setNome(dto.nome());
        aluno.setEmail(dto.email());
        aluno.setTelefone(dto.telefone());
        aluno.setCidade(dto.cidade());
        aluno.setAtivo(dto.ativo());

        Aluno alunoAtualizado = alunoRepository.save(aluno);

        return paraResponse(alunoAtualizado);
    }

    private AlunoResponseDTO paraResponse(Aluno aluno) {
        return new AlunoResponseDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getTelefone(),
                aluno.getCidade(),
                aluno.getDataCadastro(),
                aluno.getAtivo()
        );
    }
}
