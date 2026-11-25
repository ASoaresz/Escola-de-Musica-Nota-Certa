package com.escola_musica.nota_certa.service;

import com.escola_musica.nota_certa.api.dto.filter.AlunoFilterDTO;
import com.escola_musica.nota_certa.api.dto.request.AlunoRequestDTO;
import com.escola_musica.nota_certa.api.dto.response.AlunoResponseDTO;
import com.escola_musica.nota_certa.api.exception.RecursoNaoEncontradoException;
import com.escola_musica.nota_certa.entity.Aluno;
import com.escola_musica.nota_certa.repository.AlunoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.escola_musica.nota_certa.api.exception.EmailJaCadastradoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private AlunoService alunoService;

    @Test
    void deveSalvarAlunoComSucesso() {

        AlunoRequestDTO dto = new AlunoRequestDTO(
                "João Silva",
                "joao@email.com",
                "11987654321",
                "São Paulo",
                null
        );

        when(alunoRepository.existsByEmail(dto.email()))
                .thenReturn(false);

        when(alunoRepository.save(any(Aluno.class)))
                .thenAnswer(invocation -> {
                    Aluno aluno = invocation.getArgument(0);
                    aluno.setId(1L);
                    return aluno;
                });

        AlunoResponseDTO resposta = alunoService.salvar(dto);

        assertNotNull(resposta);
        assertEquals(1L, resposta.id());
        assertEquals("João Silva", resposta.nome());
        assertEquals("joao@email.com", resposta.email());
        assertEquals("11987654321", resposta.telefone());
        assertEquals("São Paulo", resposta.cidade());
        assertTrue(resposta.ativo());

        verify(alunoRepository).existsByEmail(dto.email());
        verify(alunoRepository).save(any(Aluno.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaEstiverCadastrado() {

        AlunoRequestDTO dto = new AlunoRequestDTO(
                "João Silva",
                "joao@email.com",
                "11987654321",
                "São Paulo",
                null
        );

        when(alunoRepository.existsByEmail(dto.email()))
                .thenReturn(true);

        assertThrows(
                EmailJaCadastradoException.class,
                () -> alunoService.salvar(dto)
        );

        verify(alunoRepository).existsByEmail(dto.email());

        verify(alunoRepository, never()).save(any(Aluno.class));
    }

    @Test
    void deveBuscarAlunoPorIdComSucesso() {

        LocalDateTime dataCadastro = LocalDateTime.now();

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setEmail("joao@email.com");
        aluno.setTelefone("11987654321");
        aluno.setCidade("São Paulo");
        aluno.setDataCadastro(dataCadastro);
        aluno.setAtivo(true);

        when(alunoRepository.findById(1L))
                .thenReturn(Optional.of(aluno));

        AlunoResponseDTO resposta = alunoService.buscarPorId(1L);

        assertNotNull(resposta);
        assertEquals(1L, resposta.id());
        assertEquals("João Silva", resposta.nome());
        assertEquals("joao@email.com", resposta.email());
        assertEquals("11987654321", resposta.telefone());
        assertEquals("São Paulo", resposta.cidade());
        assertEquals(dataCadastro, resposta.dataCadastro());
        assertTrue(resposta.ativo());

        verify(alunoRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoForEncontrado() {

        when(alunoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> alunoService.buscarPorId(99L)
        );

        verify(alunoRepository).findById(99L);
    }

    @Test
    void deveAtualizarAlunoComSucesso() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setEmail("joao@email.com");
        aluno.setTelefone("11987654321");
        aluno.setCidade("São Paulo");
        aluno.setDataCadastro(LocalDateTime.now());
        aluno.setAtivo(true);

        AlunoRequestDTO dto = new AlunoRequestDTO(
                "João Santos",
                "joao.santos@email.com",
                "11912345678",
                "Campinas",
                false
        );

        when(alunoRepository.findById(1L))
                .thenReturn(Optional.of(aluno));

        when(alunoRepository.existsByEmail(dto.email()))
                .thenReturn(false);

        when(alunoRepository.save(any(Aluno.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AlunoResponseDTO resposta = alunoService.atualizar(1L, dto);

        assertNotNull(resposta);
        assertEquals(1L, resposta.id());
        assertEquals("João Santos", resposta.nome());
        assertEquals("joao.santos@email.com", resposta.email());
        assertEquals("11912345678", resposta.telefone());
        assertEquals("Campinas", resposta.cidade());
        assertFalse(resposta.ativo());

        verify(alunoRepository).findById(1L);
        verify(alunoRepository).existsByEmail(dto.email());
        verify(alunoRepository).save(any(Aluno.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarAlunoComEmailJaCadastrado() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setEmail("joao@email.com");
        aluno.setTelefone("11987654321");
        aluno.setCidade("São Paulo");
        aluno.setDataCadastro(LocalDateTime.now());
        aluno.setAtivo(true);

        AlunoRequestDTO dto = new AlunoRequestDTO(
                "João Silva",
                "maria@email.com",
                "11987654321",
                "São Paulo",
                true
        );

        when(alunoRepository.findById(1L))
                .thenReturn(Optional.of(aluno));

        when(alunoRepository.existsByEmail(dto.email()))
                .thenReturn(true);

        assertThrows(
                EmailJaCadastradoException.class,
                () -> alunoService.atualizar(1L, dto)
        );

        verify(alunoRepository).findById(1L);
        verify(alunoRepository).existsByEmail(dto.email());
        verify(alunoRepository, never()).save(any(Aluno.class));
    }

    @Test
    void devePermitirAtualizacaoMantendoMesmoEmail() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setEmail("joao@email.com");
        aluno.setTelefone("11987654321");
        aluno.setCidade("São Paulo");
        aluno.setDataCadastro(LocalDateTime.now());
        aluno.setAtivo(true);

        AlunoRequestDTO dto = new AlunoRequestDTO(
                "João Santos",
                "joao@email.com",
                "11912345678",
                "Campinas",
                true
        );

        when(alunoRepository.findById(1L))
                .thenReturn(Optional.of(aluno));

        when(alunoRepository.save(any(Aluno.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AlunoResponseDTO resposta = alunoService.atualizar(1L, dto);

        assertNotNull(resposta);
        assertEquals("João Santos", resposta.nome());
        assertEquals("joao@email.com", resposta.email());
        assertEquals("11912345678", resposta.telefone());
        assertEquals("Campinas", resposta.cidade());

        verify(alunoRepository).findById(1L);
        verify(alunoRepository, never()).existsByEmail(anyString());
        verify(alunoRepository).save(any(Aluno.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarAlunoNaoEncontrado() {

        AlunoRequestDTO dto = new AlunoRequestDTO(
                "João Silva",
                "joao@email.com",
                "11987654321",
                "São Paulo",
                true
        );

        when(alunoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> alunoService.atualizar(99L, dto)
        );

        verify(alunoRepository).findById(99L);
        verify(alunoRepository, never()).save(any(Aluno.class));
    }

    @Test
    void deveListarAlunosSemFiltros() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setEmail("joao@email.com");
        aluno.setTelefone("11987654321");
        aluno.setCidade("São Paulo");
        aluno.setDataCadastro(LocalDateTime.now());
        aluno.setAtivo(true);

        List<Aluno> alunos = List.of(aluno);

        Page<Aluno> pagina = new PageImpl<>(alunos);

        AlunoFilterDTO filtro = new AlunoFilterDTO(null, null);

        Pageable pageable = PageRequest.of(0, 10);

        when(alunoRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pagina);

        Page<AlunoResponseDTO> resposta =
                alunoService.listarComFiltros(filtro, pageable);

        assertNotNull(resposta);
        assertEquals(1, resposta.getTotalElements());
        assertEquals("João Silva", resposta.getContent().get(0).nome());
        assertEquals("joao@email.com", resposta.getContent().get(0).email());

        verify(alunoRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void deveListarAlunosFiltrandoPorNome() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setEmail("joao@email.com");
        aluno.setTelefone("11987654321");
        aluno.setCidade("São Paulo");
        aluno.setDataCadastro(LocalDateTime.now());
        aluno.setAtivo(true);

        Page<Aluno> pagina = new PageImpl<>(List.of(aluno));

        AlunoFilterDTO filtro = new AlunoFilterDTO("João", null);

        Pageable pageable = PageRequest.of(0, 10);

        when(alunoRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pagina);

        Page<AlunoResponseDTO> resposta =
                alunoService.listarComFiltros(filtro, pageable);

        assertNotNull(resposta);
        assertEquals(1, resposta.getTotalElements());
        assertEquals("João Silva", resposta.getContent().get(0).nome());

        verify(alunoRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void deveListarAlunosFiltrandoPorCidade() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setEmail("joao@email.com");
        aluno.setTelefone("11987654321");
        aluno.setCidade("São Paulo");
        aluno.setDataCadastro(LocalDateTime.now());
        aluno.setAtivo(true);

        Page<Aluno> pagina = new PageImpl<>(List.of(aluno));

        AlunoFilterDTO filtro = new AlunoFilterDTO(null, "São Paulo");

        Pageable pageable = PageRequest.of(0, 10);

        when(alunoRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pagina);

        Page<AlunoResponseDTO> resposta =
                alunoService.listarComFiltros(filtro, pageable);

        assertNotNull(resposta);
        assertEquals(1, resposta.getTotalElements());
        assertEquals("São Paulo", resposta.getContent().get(0).cidade());

        verify(alunoRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void deveListarAlunosFiltrandoPorNomeECidade() {

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setEmail("joao@email.com");
        aluno.setTelefone("11987654321");
        aluno.setCidade("São Paulo");
        aluno.setDataCadastro(LocalDateTime.now());
        aluno.setAtivo(true);

        Page<Aluno> pagina = new PageImpl<>(List.of(aluno));

        AlunoFilterDTO filtro = new AlunoFilterDTO("João", "São Paulo");

        Pageable pageable = PageRequest.of(0, 10);

        when(alunoRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pagina);

        Page<AlunoResponseDTO> resposta =
                alunoService.listarComFiltros(filtro, pageable);

        assertNotNull(resposta);
        assertEquals(1, resposta.getTotalElements());
        assertEquals("João Silva", resposta.getContent().get(0).nome());
        assertEquals("São Paulo", resposta.getContent().get(0).cidade());

        verify(alunoRepository).findAll(any(Specification.class), eq(pageable));
    }
}