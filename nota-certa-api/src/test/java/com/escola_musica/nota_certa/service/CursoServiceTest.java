
package com.escola_musica.nota_certa.service;

import com.escola_musica.nota_certa.api.dto.filter.CursoFilterDTO;
import com.escola_musica.nota_certa.api.dto.request.CursoRequestDTO;
import com.escola_musica.nota_certa.api.dto.response.CursoResponseDTO;
import com.escola_musica.nota_certa.api.exception.CodigoCursoJaCadastradoException;
import com.escola_musica.nota_certa.api.exception.RecursoNaoEncontradoException;
import com.escola_musica.nota_certa.entity.Curso;
import com.escola_musica.nota_certa.enums.Instrumento;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;


    @Test
    void deveSalvarCursoComSucesso() {

        CursoRequestDTO dto = new CursoRequestDTO(
                "CUR001",
                "Violão",
                Instrumento.VIOLAO,
                new BigDecimal("150.00"),
                true
        );

        when(cursoRepository.existsByCodigo("CUR001"))
                .thenReturn(false);

        when(cursoRepository.save(any(Curso.class)))
                .thenAnswer(invocation -> {
                    Curso curso = invocation.getArgument(0);
                    curso.setId(1L);
                    return curso;
                });

        CursoResponseDTO resposta = cursoService.salvar(dto);

        assertNotNull(resposta);
        assertEquals(1L, resposta.id());
        assertEquals("CUR001", resposta.codigo());
        assertEquals("Violão", resposta.nome());
        assertEquals(Instrumento.VIOLAO, resposta.instrumento());
        assertEquals(new BigDecimal("150.00"), resposta.precoPadrao());
        assertTrue(resposta.ativo());

        verify(cursoRepository).existsByCodigo("CUR001");
        verify(cursoRepository).save(any(Curso.class));
    }


    @Test
    void deveLancarExcecaoQuandoCodigoJaEstiverCadastrado() {

        CursoRequestDTO dto = new CursoRequestDTO(
                "CUR001",
                "Violão",
                Instrumento.VIOLAO,
                new BigDecimal("150.00"),
                true
        );

        when(cursoRepository.existsByCodigo("CUR001"))
                .thenReturn(true);

        assertThrows(
                CodigoCursoJaCadastradoException.class,
                () -> cursoService.salvar(dto)
        );

        verify(cursoRepository).existsByCodigo("CUR001");
        verify(cursoRepository, never()).save(any(Curso.class));
    }


    @Test
    void deveBuscarCursoPorIdComSucesso() {

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setCodigo("CUR001");
        curso.setNome("Violão");
        curso.setInstrumento(Instrumento.VIOLAO);
        curso.setPrecoPadrao(new BigDecimal("150.00"));
        curso.setAtivo(true);

        when(cursoRepository.findById(1L))
                .thenReturn(java.util.Optional.of(curso));

        CursoResponseDTO resposta = cursoService.buscarPorId(1L);

        assertNotNull(resposta);
        assertEquals(1L, resposta.id());
        assertEquals("CUR001", resposta.codigo());
        assertEquals("Violão", resposta.nome());
        assertEquals(Instrumento.VIOLAO, resposta.instrumento());
        assertEquals(new BigDecimal("150.00"), resposta.precoPadrao());
        assertTrue(resposta.ativo());

        verify(cursoRepository).findById(1L);
    }


    @Test
    void deveLancarExcecaoQuandoCursoNaoForEncontrado() {

        when(cursoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> cursoService.buscarPorId(99L)
        );

        verify(cursoRepository).findById(99L);
    }


    @Test
    void deveAtualizarCursoComSucesso() {

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setCodigo("CUR001");
        curso.setNome("Violão");
        curso.setInstrumento(Instrumento.VIOLAO);
        curso.setPrecoPadrao(new BigDecimal("150.00"));
        curso.setAtivo(true);

        CursoRequestDTO dto = new CursoRequestDTO(
                "CUR002",
                "Piano",
                Instrumento.PIANO,
                new BigDecimal("250.00"),
                false
        );

        when(cursoRepository.findById(1L))
                .thenReturn(Optional.of(curso));

        when(cursoRepository.existsByCodigo("CUR002"))
                .thenReturn(false);

        when(cursoRepository.save(any(Curso.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CursoResponseDTO resposta = cursoService.atualizar(1L, dto);

        assertNotNull(resposta);
        assertEquals(1L, resposta.id());
        assertEquals("CUR002", resposta.codigo());
        assertEquals("Piano", resposta.nome());
        assertEquals(Instrumento.PIANO, resposta.instrumento());
        assertEquals(new BigDecimal("250.00"), resposta.precoPadrao());
        assertFalse(resposta.ativo());

        verify(cursoRepository).findById(1L);
        verify(cursoRepository).existsByCodigo("CUR002");
        verify(cursoRepository).save(any(Curso.class));
    }


    @Test
    void deveLancarExcecaoAoAtualizarCursoComCodigoJaCadastrado() {

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setCodigo("CUR001");
        curso.setNome("Violão");
        curso.setInstrumento(Instrumento.VIOLAO);
        curso.setPrecoPadrao(new BigDecimal("150.00"));
        curso.setAtivo(true);

        CursoRequestDTO dto = new CursoRequestDTO(
                "CUR002",
                "Piano",
                Instrumento.PIANO,
                new BigDecimal("250.00"),
                true
        );

        when(cursoRepository.findById(1L))
                .thenReturn(Optional.of(curso));

        when(cursoRepository.existsByCodigo("CUR002"))
                .thenReturn(true);

        assertThrows(
                CodigoCursoJaCadastradoException.class,
                () -> cursoService.atualizar(1L, dto)
        );

        verify(cursoRepository).findById(1L);
        verify(cursoRepository).existsByCodigo("CUR002");
        verify(cursoRepository, never()).save(any(Curso.class));
    }


    @Test
    void devePermitirAtualizacaoMantendoMesmoCodigo() {

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setCodigo("CUR001");
        curso.setNome("Violão");
        curso.setInstrumento(Instrumento.VIOLAO);
        curso.setPrecoPadrao(new BigDecimal("150.00"));
        curso.setAtivo(true);

        CursoRequestDTO dto = new CursoRequestDTO(
                "CUR001",
                "Violão Avançado",
                Instrumento.VIOLAO,
                new BigDecimal("200.00"),
                true
        );

        when(cursoRepository.findById(1L))
                .thenReturn(Optional.of(curso));

        when(cursoRepository.save(any(Curso.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CursoResponseDTO resposta = cursoService.atualizar(1L, dto);

        assertNotNull(resposta);
        assertEquals("CUR001", resposta.codigo());
        assertEquals("Violão Avançado", resposta.nome());
        assertEquals(new BigDecimal("200.00"), resposta.precoPadrao());

        verify(cursoRepository).findById(1L);
        verify(cursoRepository, never()).existsByCodigo(anyString());
        verify(cursoRepository).save(any(Curso.class));
    }


    @Test
    void deveLancarExcecaoAoAtualizarCursoNaoEncontrado() {

        CursoRequestDTO dto = new CursoRequestDTO(
                "CUR002",
                "Piano",
                Instrumento.PIANO,
                new BigDecimal("250.00"),
                true
        );

        when(cursoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> cursoService.atualizar(99L, dto)
        );

        verify(cursoRepository).findById(99L);
        verify(cursoRepository, never()).save(any(Curso.class));
    }


    @Test
    void deveListarCursosSemFiltros() {

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setCodigo("CUR001");
        curso.setNome("Violão");
        curso.setInstrumento(Instrumento.VIOLAO);
        curso.setPrecoPadrao(new BigDecimal("150.00"));
        curso.setAtivo(true);

        Page<Curso> pagina = new PageImpl<>(List.of(curso));

        CursoFilterDTO filtro = new CursoFilterDTO(null, null);
        Pageable pageable = PageRequest.of(0, 10);

        when(cursoRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pagina);

        Page<CursoResponseDTO> resposta =
                cursoService.listarComFiltros(filtro, pageable);

        assertNotNull(resposta);
        assertEquals(1, resposta.getTotalElements());
        assertEquals("CUR001", resposta.getContent().get(0).codigo());
        assertEquals("Violão", resposta.getContent().get(0).nome());

        verify(cursoRepository).findAll(any(Specification.class), eq(pageable));
    }


    @Test
    void deveListarCursosFiltrandoPorNome() {

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setCodigo("CUR001");
        curso.setNome("Violão");
        curso.setInstrumento(Instrumento.VIOLAO);
        curso.setPrecoPadrao(new BigDecimal("150.00"));
        curso.setAtivo(true);

        Page<Curso> pagina = new PageImpl<>(List.of(curso));

        CursoFilterDTO filtro = new CursoFilterDTO("Violão", null);
        Pageable pageable = PageRequest.of(0, 10);

        when(cursoRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pagina);

        Page<CursoResponseDTO> resposta =
                cursoService.listarComFiltros(filtro, pageable);

        assertNotNull(resposta);
        assertEquals(1, resposta.getTotalElements());
        assertEquals("Violão", resposta.getContent().get(0).nome());

        verify(cursoRepository).findAll(any(Specification.class), eq(pageable));
    }


    @Test
    void deveListarCursosFiltrandoPorInstrumento() {

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setCodigo("CUR001");
        curso.setNome("Violão");
        curso.setInstrumento(Instrumento.VIOLAO);
        curso.setPrecoPadrao(new BigDecimal("150.00"));
        curso.setAtivo(true);

        Page<Curso> pagina = new PageImpl<>(List.of(curso));

        CursoFilterDTO filtro = new CursoFilterDTO(null, Instrumento.VIOLAO);
        Pageable pageable = PageRequest.of(0, 10);

        when(cursoRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pagina);

        Page<CursoResponseDTO> resposta =
                cursoService.listarComFiltros(filtro, pageable);

        assertNotNull(resposta);
        assertEquals(1, resposta.getTotalElements());
        assertEquals(Instrumento.VIOLAO, resposta.getContent().get(0).instrumento());

        verify(cursoRepository).findAll(any(Specification.class), eq(pageable));
    }


    @Test
    void deveListarCursosFiltrandoPorNomeEInstrumento() {

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setCodigo("CUR001");
        curso.setNome("Violão");
        curso.setInstrumento(Instrumento.VIOLAO);
        curso.setPrecoPadrao(new BigDecimal("150.00"));
        curso.setAtivo(true);

        Page<Curso> pagina = new PageImpl<>(List.of(curso));

        CursoFilterDTO filtro = new CursoFilterDTO("Violão", Instrumento.VIOLAO);
        Pageable pageable = PageRequest.of(0, 10);

        when(cursoRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pagina);

        Page<CursoResponseDTO> resposta =
                cursoService.listarComFiltros(filtro, pageable);

        assertNotNull(resposta);
        assertEquals(1, resposta.getTotalElements());
        assertEquals("Violão", resposta.getContent().get(0).nome());
        assertEquals(Instrumento.VIOLAO, resposta.getContent().get(0).instrumento());

        verify(cursoRepository).findAll(any(Specification.class), eq(pageable));
    }

}
