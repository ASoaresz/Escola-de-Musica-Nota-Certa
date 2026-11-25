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
import com.escola_musica.nota_certa.enums.Modalidade;
import com.escola_musica.nota_certa.enums.StatusAula;
import com.escola_musica.nota_certa.repository.AulaRepository;
import com.escola_musica.nota_certa.repository.AlunoRepository;
import com.escola_musica.nota_certa.repository.CursoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AulaServiceTest {

    @Mock
    private AulaRepository aulaRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private AulaService aulaService;


    @Test
    void deveSalvarAulaComSucesso() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setAtivo(true);

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Violão");
        curso.setAtivo(true);

        LocalDateTime dataHora = LocalDateTime.of(2026, 12, 10, 14, 0);

        AulaRequestDTO dto = new AulaRequestDTO(
                1L,
                1L,
                dataHora,
                Modalidade.PRESENCIAL,
                new BigDecimal("150.00")
        );

        when(alunoRepository.findById(1L))
                .thenReturn(Optional.of(aluno));

        when(cursoRepository.findById(1L))
                .thenReturn(Optional.of(curso));

        when(aulaRepository.existsByAlunoIdAndDataHoraAndStatus(
                1L, dataHora, StatusAula.AGENDADA
        )).thenReturn(false);

        when(aulaRepository.save(any(Aula.class)))
                .thenAnswer(invocation -> {
                    Aula aula = invocation.getArgument(0);
                    aula.setId(1L);
                    aula.setStatus(StatusAula.AGENDADA);
                    return aula;
                });

        AulaResponseDTO resposta = aulaService.salvar(dto);

        assertNotNull(resposta);
        assertEquals(1L, resposta.id());
        assertEquals(1L, resposta.alunoId());
        assertEquals("João Silva", resposta.nomeAluno());
        assertEquals(1L, resposta.cursoId());
        assertEquals("Violão", resposta.nomeCurso());
        assertEquals(dataHora, resposta.dataHora());
        assertEquals(Modalidade.PRESENCIAL, resposta.modalidade());
        assertEquals(new BigDecimal("150.00"), resposta.valor());
        assertEquals(StatusAula.AGENDADA, resposta.status());

        verify(alunoRepository).findById(1L);
        verify(cursoRepository).findById(1L);
        verify(aulaRepository).existsByAlunoIdAndDataHoraAndStatus(
                1L, dataHora, StatusAula.AGENDADA
        );
        verify(aulaRepository).save(any(Aula.class));
    }


    @Test
    void deveLancarExcecaoQuandoAlunoNaoForEncontrado() {

        LocalDateTime dataHora = LocalDateTime.of(2026, 12, 10, 14, 0);

        AulaRequestDTO dto = new AulaRequestDTO(
                1L,
                1L,
                dataHora,
                Modalidade.PRESENCIAL,
                new BigDecimal("150.00")
        );

        when(alunoRepository.findById(1L))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException excecao = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> aulaService.salvar(dto)
        );

        assertEquals("Aluno não encontrado com ID: 1", excecao.getMessage());

        verify(alunoRepository).findById(1L);
        verify(cursoRepository, never()).findById(any());
        verify(aulaRepository, never()).save(any());
    }


    @Test
    void deveLancarExcecaoQuandoCursoNaoForEncontrado() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setAtivo(true);

        LocalDateTime dataHora = LocalDateTime.of(2026, 12, 10, 14, 0);

        AulaRequestDTO dto = new AulaRequestDTO(
                1L,
                1L,
                dataHora,
                Modalidade.PRESENCIAL,
                new BigDecimal("150.00")
        );

        when(alunoRepository.findById(1L))
                .thenReturn(Optional.of(aluno));

        when(cursoRepository.findById(1L))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException excecao = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> aulaService.salvar(dto)
        );

        assertEquals("Curso não encontrado com ID: 1", excecao.getMessage());

        verify(alunoRepository).findById(1L);
        verify(cursoRepository).findById(1L);
        verify(aulaRepository, never()).save(any());
    }


    @Test
    void deveLancarExcecaoQuandoAlunoEstiverInativo() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setAtivo(false);

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Violão");
        curso.setAtivo(true);

        LocalDateTime dataHora = LocalDateTime.of(2026, 12, 10, 14, 0);

        AulaRequestDTO dto = new AulaRequestDTO(
                1L,
                1L,
                dataHora,
                Modalidade.PRESENCIAL,
                new BigDecimal("150.00")
        );

        when(alunoRepository.findById(1L))
                .thenReturn(Optional.of(aluno));

        when(cursoRepository.findById(1L))
                .thenReturn(Optional.of(curso));

        RecursoInativoException excecao = assertThrows(
                RecursoInativoException.class,
                () -> aulaService.salvar(dto)
        );

        assertEquals(
                "Não é possível agendar aula para um aluno inativo",
                excecao.getMessage()
        );

        verify(alunoRepository).findById(1L);
        verify(cursoRepository).findById(1L);
        verify(aulaRepository, never()).existsByAlunoIdAndDataHoraAndStatus(
                any(), any(), any()
        );
        verify(aulaRepository, never()).save(any());
    }


    @Test
    void deveLancarExcecaoQuandoCursoEstiverInativo() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setAtivo(true);

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Violão");
        curso.setAtivo(false);

        LocalDateTime dataHora = LocalDateTime.of(2026, 12, 10, 14, 0);

        AulaRequestDTO dto = new AulaRequestDTO(
                1L,
                1L,
                dataHora,
                Modalidade.PRESENCIAL,
                new BigDecimal("150.00")
        );

        when(alunoRepository.findById(1L))
                .thenReturn(Optional.of(aluno));

        when(cursoRepository.findById(1L))
                .thenReturn(Optional.of(curso));

        RecursoInativoException excecao = assertThrows(
                RecursoInativoException.class,
                () -> aulaService.salvar(dto)
        );

        assertEquals(
                "Não é possível agendar aula para um curso inativo",
                excecao.getMessage()
        );

        verify(alunoRepository).findById(1L);
        verify(cursoRepository).findById(1L);
        verify(aulaRepository, never()).existsByAlunoIdAndDataHoraAndStatus(
                any(), any(), any()
        );
        verify(aulaRepository, never()).save(any());
    }


    @Test
    void deveLancarExcecaoQuandoHouverConflitoDeHorario() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setAtivo(true);

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Violão");
        curso.setAtivo(true);

        LocalDateTime dataHora = LocalDateTime.of(2026, 12, 10, 14, 0);

        AulaRequestDTO dto = new AulaRequestDTO(
                1L,
                1L,
                dataHora,
                Modalidade.PRESENCIAL,
                new BigDecimal("150.00")
        );

        when(alunoRepository.findById(1L))
                .thenReturn(Optional.of(aluno));

        when(cursoRepository.findById(1L))
                .thenReturn(Optional.of(curso));

        when(aulaRepository.existsByAlunoIdAndDataHoraAndStatus(
                1L, dataHora, StatusAula.AGENDADA
        )).thenReturn(true);

        AulaConflitoException excecao = assertThrows(
                AulaConflitoException.class,
                () -> aulaService.salvar(dto)
        );

        assertEquals(
                "O aluno já possui uma aula agendada nesse horário",
                excecao.getMessage()
        );

        verify(alunoRepository).findById(1L);
        verify(cursoRepository).findById(1L);
        verify(aulaRepository).existsByAlunoIdAndDataHoraAndStatus(
                1L, dataHora, StatusAula.AGENDADA
        );
        verify(aulaRepository, never()).save(any());
    }


    @Test
    void deveBuscarAulaPorIdComSucesso() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Violão");

        LocalDateTime dataHora = LocalDateTime.of(2026, 12, 10, 14, 0);

        Aula aula = new Aula();
        aula.setId(1L);
        aula.setAluno(aluno);
        aula.setCurso(curso);
        aula.setDataHora(dataHora);
        aula.setModalidade(Modalidade.PRESENCIAL);
        aula.setValor(new BigDecimal("150.00"));
        aula.setStatus(StatusAula.AGENDADA);

        when(aulaRepository.findById(1L))
                .thenReturn(Optional.of(aula));

        AulaResponseDTO resposta = aulaService.buscarPorId(1L);

        assertNotNull(resposta);
        assertEquals(1L, resposta.id());
        assertEquals(1L, resposta.alunoId());
        assertEquals("João Silva", resposta.nomeAluno());
        assertEquals(1L, resposta.cursoId());
        assertEquals("Violão", resposta.nomeCurso());
        assertEquals(dataHora, resposta.dataHora());
        assertEquals(Modalidade.PRESENCIAL, resposta.modalidade());
        assertEquals(new BigDecimal("150.00"), resposta.valor());
        assertEquals(StatusAula.AGENDADA, resposta.status());

        verify(aulaRepository).findById(1L);
    }


    @Test
    void deveLancarExcecaoQuandoAulaNaoForEncontrada() {

        when(aulaRepository.findById(99L))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException excecao = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> aulaService.buscarPorId(99L)
        );

        assertEquals(
                "Aula não encontrada com ID: 99",
                excecao.getMessage()
        );

        verify(aulaRepository).findById(99L);
    }


    @Test
    void deveAtualizarStatusParaRealizadaComSucesso() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Violão");

        LocalDateTime dataHora = LocalDateTime.of(2026, 12, 10, 14, 0);

        Aula aula = new Aula();
        aula.setId(1L);
        aula.setAluno(aluno);
        aula.setCurso(curso);
        aula.setDataHora(dataHora);
        aula.setModalidade(Modalidade.PRESENCIAL);
        aula.setValor(new BigDecimal("150.00"));
        aula.setStatus(StatusAula.AGENDADA);

        when(aulaRepository.findById(1L))
                .thenReturn(Optional.of(aula));

        when(aulaRepository.save(any(Aula.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AulaResponseDTO resposta = aulaService.atualizarStatus(
                1L, StatusAula.REALIZADA
        );

        assertNotNull(resposta);
        assertEquals(StatusAula.REALIZADA, resposta.status());
        assertEquals(1L, resposta.id());

        verify(aulaRepository).findById(1L);
        verify(aulaRepository).save(aula);
    }


    @Test
    void deveAtualizarStatusParaCanceladaComSucesso() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Violão");

        LocalDateTime dataHora = LocalDateTime.of(2026, 12, 10, 14, 0);

        Aula aula = new Aula();
        aula.setId(1L);
        aula.setAluno(aluno);
        aula.setCurso(curso);
        aula.setDataHora(dataHora);
        aula.setModalidade(Modalidade.PRESENCIAL);
        aula.setValor(new BigDecimal("150.00"));
        aula.setStatus(StatusAula.AGENDADA);

        when(aulaRepository.findById(1L))
                .thenReturn(Optional.of(aula));

        when(aulaRepository.save(any(Aula.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AulaResponseDTO resposta = aulaService.atualizarStatus(
                1L, StatusAula.CANCELADA
        );

        assertNotNull(resposta);
        assertEquals(StatusAula.CANCELADA, resposta.status());
        assertEquals(1L, resposta.id());

        verify(aulaRepository).findById(1L);
        verify(aulaRepository).save(aula);
    }


    @Test
    void deveLancarExcecaoQuandoAulaNaoForEncontradaAoAtualizarStatus() {

        when(aulaRepository.findById(99L))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException excecao = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> aulaService.atualizarStatus(99L, StatusAula.REALIZADA)
        );

        assertEquals(
                "Aula não encontrada com ID: 99",
                excecao.getMessage()
        );

        verify(aulaRepository).findById(99L);
        verify(aulaRepository, never()).save(any());
    }


    @Test
    void deveLancarExcecaoQuandoAulaJaEstiverRealizada() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Violão");

        Aula aula = new Aula();
        aula.setId(1L);
        aula.setAluno(aluno);
        aula.setCurso(curso);
        aula.setStatus(StatusAula.REALIZADA);

        when(aulaRepository.findById(1L))
                .thenReturn(Optional.of(aula));

        StatusAulaInvalidoException excecao = assertThrows(
                StatusAulaInvalidoException.class,
                () -> aulaService.atualizarStatus(1L, StatusAula.CANCELADA)
        );

        assertEquals(
                "Não é possível alterar uma aula realizada",
                excecao.getMessage()
        );

        verify(aulaRepository).findById(1L);
        verify(aulaRepository, never()).save(any());
    }


    @Test
    void deveLancarExcecaoQuandoAulaJaEstiverCancelada() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Violão");

        Aula aula = new Aula();
        aula.setId(1L);
        aula.setAluno(aluno);
        aula.setCurso(curso);
        aula.setStatus(StatusAula.CANCELADA);

        when(aulaRepository.findById(1L))
                .thenReturn(Optional.of(aula));

        StatusAulaInvalidoException excecao = assertThrows(
                StatusAulaInvalidoException.class,
                () -> aulaService.atualizarStatus(1L, StatusAula.REALIZADA)
        );

        assertEquals(
                "Não é possível alterar uma aula cancelada",
                excecao.getMessage()
        );

        verify(aulaRepository).findById(1L);
        verify(aulaRepository, never()).save(any());
    }


    @Test
    void deveLancarExcecaoQuandoTentarAgendarNovamenteUmaAula() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Violão");

        Aula aula = new Aula();
        aula.setId(1L);
        aula.setAluno(aluno);
        aula.setCurso(curso);
        aula.setStatus(StatusAula.AGENDADA);

        when(aulaRepository.findById(1L))
                .thenReturn(Optional.of(aula));

        StatusAulaInvalidoException excecao = assertThrows(
                StatusAulaInvalidoException.class,
                () -> aulaService.atualizarStatus(1L, StatusAula.AGENDADA)
        );

        assertEquals(
                "Não é possível agendar novamente uma aula já agendada",
                excecao.getMessage()
        );

        verify(aulaRepository).findById(1L);
        verify(aulaRepository, never()).save(any());
    }


    @Test
    void deveLancarExcecaoQuandoNovoStatusForInvalido() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Violão");

        Aula aula = new Aula();
        aula.setId(1L);
        aula.setAluno(aluno);
        aula.setCurso(curso);
        aula.setStatus(StatusAula.AGENDADA);

        when(aulaRepository.findById(1L))
                .thenReturn(Optional.of(aula));

        StatusAulaInvalidoException excecao = assertThrows(
                StatusAulaInvalidoException.class,
                () -> aulaService.atualizarStatus(1L, null)
        );

        assertEquals(
                "O novo status deve ser REALIZADA ou CANCELADA",
                excecao.getMessage()
        );

        verify(aulaRepository).findById(1L);
        verify(aulaRepository, never()).save(any());
    }


    @Test
    void deveListarAulasSemFiltros() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Violão");

        Aula aula = new Aula();
        aula.setId(1L);
        aula.setAluno(aluno);
        aula.setCurso(curso);
        aula.setDataHora(LocalDateTime.of(2026, 12, 10, 14, 0));
        aula.setModalidade(Modalidade.PRESENCIAL);
        aula.setValor(new BigDecimal("150.00"));
        aula.setStatus(StatusAula.AGENDADA);

        Pageable pageable = PageRequest.of(0, 10);
        AulaFilterDTO filtro = new AulaFilterDTO(
                null, null, null, null, null, null
        );

        Page<Aula> pagina = new PageImpl<>(List.of(aula), pageable, 1);

        when(aulaRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pagina);

        Page<AulaResponseDTO> resposta = aulaService.listarComFiltros(
                filtro, pageable
        );

        assertNotNull(resposta);
        assertEquals(1, resposta.getTotalElements());
        assertEquals(1, resposta.getContent().size());
        assertEquals(1L, resposta.getContent().get(0).id());
        assertEquals("João Silva", resposta.getContent().get(0).nomeAluno());
        assertEquals("Violão", resposta.getContent().get(0).nomeCurso());

        verify(aulaRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void deveListarAulasFiltrandoPorStatus() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Violão");

        Aula aula = new Aula();
        aula.setId(1L);
        aula.setAluno(aluno);
        aula.setCurso(curso);
        aula.setDataHora(LocalDateTime.of(2026, 12, 10, 14, 0));
        aula.setModalidade(Modalidade.PRESENCIAL);
        aula.setValor(new BigDecimal("150.00"));
        aula.setStatus(StatusAula.AGENDADA);

        Pageable pageable = PageRequest.of(0, 10);
        AulaFilterDTO filtro = new AulaFilterDTO(
                StatusAula.AGENDADA, null, null, null, null, null
        );

        Page<Aula> pagina = new PageImpl<>(List.of(aula), pageable, 1);

        when(aulaRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pagina);

        Page<AulaResponseDTO> resposta = aulaService.listarComFiltros(
                filtro, pageable
        );

        assertNotNull(resposta);
        assertEquals(1, resposta.getTotalElements());
        assertEquals(StatusAula.AGENDADA, resposta.getContent().get(0).status());

        verify(aulaRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void deveListarAulasFiltrandoPorModalidade() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Violão");

        Aula aula = new Aula();
        aula.setId(1L);
        aula.setAluno(aluno);
        aula.setCurso(curso);
        aula.setDataHora(LocalDateTime.of(2026, 12, 10, 14, 0));
        aula.setModalidade(Modalidade.PRESENCIAL);
        aula.setValor(new BigDecimal("150.00"));
        aula.setStatus(StatusAula.AGENDADA);

        Pageable pageable = PageRequest.of(0, 10);
        AulaFilterDTO filtro = new AulaFilterDTO(
                null, Modalidade.PRESENCIAL, null, null, null, null
        );

        Page<Aula> pagina = new PageImpl<>(List.of(aula), pageable, 1);

        when(aulaRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pagina);

        Page<AulaResponseDTO> resposta = aulaService.listarComFiltros(
                filtro, pageable
        );

        assertNotNull(resposta);
        assertEquals(1, resposta.getTotalElements());
        assertEquals(Modalidade.PRESENCIAL, resposta.getContent().get(0).modalidade());

        verify(aulaRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void deveListarAulasFiltrandoPorAlunoId() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Violão");

        Aula aula = new Aula();
        aula.setId(1L);
        aula.setAluno(aluno);
        aula.setCurso(curso);
        aula.setDataHora(LocalDateTime.of(2026, 12, 10, 14, 0));
        aula.setModalidade(Modalidade.PRESENCIAL);
        aula.setValor(new BigDecimal("150.00"));
        aula.setStatus(StatusAula.AGENDADA);

        Pageable pageable = PageRequest.of(0, 10);
        AulaFilterDTO filtro = new AulaFilterDTO(
                null, null, 1L, null, null, null
        );

        Page<Aula> pagina = new PageImpl<>(List.of(aula), pageable, 1);

        when(aulaRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pagina);

        Page<AulaResponseDTO> resposta = aulaService.listarComFiltros(
                filtro, pageable
        );

        assertNotNull(resposta);
        assertEquals(1, resposta.getTotalElements());
        assertEquals(1L, resposta.getContent().get(0).alunoId());
        assertEquals("João Silva", resposta.getContent().get(0).nomeAluno());

        verify(aulaRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void deveListarAulasFiltrandoPorCursoId() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Violão");

        Aula aula = new Aula();
        aula.setId(1L);
        aula.setAluno(aluno);
        aula.setCurso(curso);
        aula.setDataHora(LocalDateTime.of(2026, 12, 10, 14, 0));
        aula.setModalidade(Modalidade.PRESENCIAL);
        aula.setValor(new BigDecimal("150.00"));
        aula.setStatus(StatusAula.AGENDADA);

        Pageable pageable = PageRequest.of(0, 10);
        AulaFilterDTO filtro = new AulaFilterDTO(
                null, null, null, 1L, null, null
        );

        Page<Aula> pagina = new PageImpl<>(List.of(aula), pageable, 1);

        when(aulaRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pagina);

        Page<AulaResponseDTO> resposta = aulaService.listarComFiltros(
                filtro, pageable
        );

        assertNotNull(resposta);
        assertEquals(1, resposta.getTotalElements());
        assertEquals(1L, resposta.getContent().get(0).cursoId());
        assertEquals("Violão", resposta.getContent().get(0).nomeCurso());

        verify(aulaRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void deveListarAulasFiltrandoPorPeriodoDeDatas() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Violão");

        LocalDateTime dataHora = LocalDateTime.of(2026, 12, 10, 14, 0);
        LocalDateTime dataInicio = LocalDateTime.of(2026, 12, 1, 0, 0);
        LocalDateTime dataFim = LocalDateTime.of(2026, 12, 31, 23, 59);

        Aula aula = new Aula();
        aula.setId(1L);
        aula.setAluno(aluno);
        aula.setCurso(curso);
        aula.setDataHora(dataHora);
        aula.setModalidade(Modalidade.PRESENCIAL);
        aula.setValor(new BigDecimal("150.00"));
        aula.setStatus(StatusAula.AGENDADA);

        Pageable pageable = PageRequest.of(0, 10);
        AulaFilterDTO filtro = new AulaFilterDTO(
                null, null, null, null, dataInicio, dataFim
        );

        Page<Aula> pagina = new PageImpl<>(List.of(aula), pageable, 1);

        when(aulaRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pagina);

        Page<AulaResponseDTO> resposta = aulaService.listarComFiltros(
                filtro, pageable
        );

        assertNotNull(resposta);
        assertEquals(1, resposta.getTotalElements());
        assertEquals(dataHora, resposta.getContent().get(0).dataHora());

        verify(aulaRepository).findAll(any(Specification.class), eq(pageable));
    }


}
