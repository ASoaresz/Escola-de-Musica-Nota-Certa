package com.escola_musica.nota_certa.service;

import com.escola_musica.nota_certa.api.dto.filter.CursoFilterDTO;
import com.escola_musica.nota_certa.api.dto.request.CursoRequestDTO;
import com.escola_musica.nota_certa.api.dto.response.CursoResponseDTO;
import com.escola_musica.nota_certa.api.exception.CodigoCursoJaCadastradoException;
import com.escola_musica.nota_certa.api.exception.RecursoNaoEncontradoException;
import com.escola_musica.nota_certa.entity.Curso;
import com.escola_musica.nota_certa.repository.CursoRepository;
import com.escola_musica.nota_certa.repository.specification.CursoSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    // Listar cursos com filtros e paginação
    public Page<CursoResponseDTO> listarComFiltros(
            CursoFilterDTO filtro,
            Pageable pageable
    ) {
        Specification<Curso> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();

        if (filtro.nome() != null && !filtro.nome().isBlank()) {
            spec = spec.and(
                    CursoSpecification.nomeContem(filtro.nome())
            );
        }

        if (filtro.instrumento() != null) {
            spec = spec.and(
                    CursoSpecification.instrumentoIgual(filtro.instrumento())
            );
        }

        return cursoRepository.findAll(spec, pageable)
                .map(this::paraResponse);
    }

    public CursoResponseDTO buscarPorId(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Curso não encontrado com ID: " + id
                        )
                );

        return paraResponse(curso);
    }

    public CursoResponseDTO salvar(CursoRequestDTO dto) {

        if (cursoRepository.existsByCodigo(dto.codigo())) {
            throw new CodigoCursoJaCadastradoException(
                    "Já existe um curso cadastrado com o código: " + dto.codigo()
            );
        }

        Curso curso = new Curso();

        curso.setCodigo(dto.codigo());
        curso.setNome(dto.nome());
        curso.setInstrumento(dto.instrumento());
        curso.setPrecoPadrao(dto.precoPadrao());

        if (dto.ativo() != null) {
            curso.setAtivo(dto.ativo());
        }

        Curso cursoSalvo = cursoRepository.save(curso);

        return paraResponse(cursoSalvo);
    }

    public CursoResponseDTO atualizar(Long id, CursoRequestDTO dto) {

        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Curso não encontrado com ID: " + id
                        )
                );

        if (!curso.getCodigo().equalsIgnoreCase(dto.codigo())
                && cursoRepository.existsByCodigo(dto.codigo())) {

            throw new CodigoCursoJaCadastradoException(
                    "Já existe um curso cadastrado com o código: " + dto.codigo()
            );
        }

        curso.setCodigo(dto.codigo());
        curso.setNome(dto.nome());
        curso.setInstrumento(dto.instrumento());
        curso.setPrecoPadrao(dto.precoPadrao());
        curso.setAtivo(dto.ativo());

        Curso cursoAtualizado = cursoRepository.save(curso);

        return paraResponse(cursoAtualizado);
    }

    // Converter entidade Curso em CursoResponseDTO
    private CursoResponseDTO paraResponse(Curso curso) {
        return new CursoResponseDTO(
                curso.getId(),
                curso.getCodigo(),
                curso.getNome(),
                curso.getInstrumento(),
                curso.getPrecoPadrao(),
                curso.getAtivo()
        );
    }
}
