package com.escola_musica.nota_certa.service;

import com.escola_musica.nota_certa.api.dto.filter.AulaFilterDTO;
import com.escola_musica.nota_certa.api.dto.request.AulaRequestDTO;
import com.escola_musica.nota_certa.api.dto.response.AulaResponseDTO;
import com.escola_musica.nota_certa.api.exception.AulaConflitoException;
import com.escola_musica.nota_certa.api.exception.RecursoInativoException;
import com.escola_musica.nota_certa.api.exception.RecursoNaoEncontradoException;
import com.escola_musica.nota_certa.api.exception.StatusAulaInvalidoException;
import com.escola_musica.nota_certa.entity.Aluno;
import com.escola_musica.nota_certa.entity.Aula;
import com.escola_musica.nota_certa.entity.Curso;
import com.escola_musica.nota_certa.enums.StatusAula;
import com.escola_musica.nota_certa.repository.AulaRepository;
import com.escola_musica.nota_certa.repository.AlunoRepository;
import com.escola_musica.nota_certa.repository.CursoRepository;
import com.escola_musica.nota_certa.repository.specification.AulaSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class AulaService {

    private final AulaRepository aulaRepository;
    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;

    public AulaService(
            AulaRepository aulaRepository,
            AlunoRepository alunoRepository,
            CursoRepository cursoRepository
    ) {
        this.aulaRepository = aulaRepository;
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
    }

    private AulaResponseDTO paraResponse(Aula aula) {

        return new AulaResponseDTO(
                aula.getId(),
                aula.getAluno().getId(),
                aula.getAluno().getNome(),
                aula.getCurso().getId(),
                aula.getCurso().getNome(),
                aula.getDataHora(),
                aula.getModalidade(),
                aula.getValor(),
                aula.getStatus()
        );
    }

    public Page<AulaResponseDTO> listarComFiltros(
            AulaFilterDTO filtro,
            Pageable pageable
    ) {

        Specification<Aula> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();

        if (filtro.status() != null) {
            spec = spec.and(
                    AulaSpecification.statusIgual(filtro.status())
            );
        }

        if (filtro.modalidade() != null) {
            spec = spec.and(
                    AulaSpecification.modalidadeIgual(filtro.modalidade())
            );
        }

        if (filtro.alunoId() != null) {
            spec = spec.and(
                    AulaSpecification.alunoIgual(filtro.alunoId())
            );
        }

        if (filtro.cursoId() != null) {
            spec = spec.and(
                    AulaSpecification.cursoIgual(filtro.cursoId())
            );
        }

        if (filtro.dataInicio() != null) {
            spec = spec.and(
                    AulaSpecification.dataHoraMaiorOuIgual(filtro.dataInicio())
            );
        }

        if (filtro.dataFim() != null) {
            spec = spec.and(
                    AulaSpecification.dataHoraMenorOuIgual(filtro.dataFim())
            );
        }

        return aulaRepository.findAll(spec, pageable)
                .map(this::paraResponse);
    }

    public AulaResponseDTO buscarPorId(Long id) {

        Aula aula = aulaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Aula não encontrada com ID: " + id
                        )
                );

        return paraResponse(aula);
    }

    public AulaResponseDTO salvar(AulaRequestDTO dto) {

        Aluno aluno = alunoRepository.findById(dto.alunoId())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Aluno não encontrado com ID: " + dto.alunoId()
                        )
                );

        Curso curso = cursoRepository.findById(dto.cursoId())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Curso não encontrado com ID: " + dto.cursoId()
                        )
                );

        if (Boolean.FALSE.equals(aluno.getAtivo())) {
            throw new RecursoInativoException(
                    "Não é possível agendar aula para um aluno inativo"
            );
        }

        if (Boolean.FALSE.equals(curso.getAtivo())) {
            throw new RecursoInativoException(
                    "Não é possível agendar aula para um curso inativo"
            );
        }

        boolean conflito = aulaRepository
                .existsByAlunoIdAndDataHoraAndStatus(
                        dto.alunoId(),
                        dto.dataHora(),
                        StatusAula.AGENDADA
                );

        if (conflito) {
            throw new AulaConflitoException(
                    "O aluno já possui uma aula agendada nesse horário"
            );
        }

        Aula aula = new Aula();
        aula.setAluno(aluno);
        aula.setCurso(curso);
        aula.setDataHora(dto.dataHora());
        aula.setModalidade(dto.modalidade());
        aula.setValor(dto.valor());

        Aula aulaSalva = aulaRepository.save(aula);

        return paraResponse(aulaSalva);
    }


    public AulaResponseDTO atualizarStatus(Long id, StatusAula novoStatus) {

        Aula aula = aulaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Aula não encontrada com ID: " + id
                        )
                );

        if (aula.getStatus() == StatusAula.REALIZADA) {
            throw new StatusAulaInvalidoException(
                    "Não é possível alterar uma aula realizada"
            );
        }

        if (aula.getStatus() == StatusAula.CANCELADA) {
            throw new StatusAulaInvalidoException(
                    "Não é possível alterar uma aula cancelada"
            );
        }

        if (novoStatus == StatusAula.AGENDADA) {
            throw new StatusAulaInvalidoException(
                    "Não é possível agendar novamente uma aula já agendada"
            );
        }

        if (novoStatus != StatusAula.REALIZADA &&
                novoStatus != StatusAula.CANCELADA) {
            throw new StatusAulaInvalidoException(
                    "O novo status deve ser REALIZADA ou CANCELADA"
            );
        }

        aula.setStatus(novoStatus);

        Aula aulaAtualizada = aulaRepository.save(aula);

        return paraResponse(aulaAtualizada);
    }

}