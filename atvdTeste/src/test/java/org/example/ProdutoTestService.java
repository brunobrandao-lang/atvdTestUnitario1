package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoTestService {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

    // CT01 - Produto válido (RF01)
    @Test
    void deveSalvarProdutoComSucesso() {
        ProdutoModel produto = new ProdutoModel("Mouse", 49.90, 10);
        when(repository.salvar(produto)).thenReturn(produto);

        ProdutoModel resultado = service.salvar(produto);

        assertEquals("Mouse", resultado.getNome());
        verify(repository, times(1)).salvar(produto);
    }

    // CT02 - Preço negativo (-10) (RF02)
    @Test
    void naoDeveSalvarProdutoComPrecoNegativo() {
        ProdutoModel produto = new ProdutoModel("Mouse", -10.0, 5);

        assertThrows(IllegalArgumentException.class, () -> service.salvar(produto));
        verify(repository, never()).salvar(any());
    }

    // CT03 - Preço igual a zero (RF02)
    @Test
    void naoDeveSalvarProdutoComPrecoZero() {
        ProdutoModel produto = new ProdutoModel("Mouse", 0.0, 5);

        assertThrows(IllegalArgumentException.class, () -> service.salvar(produto));
        verify(repository, never()).salvar(any());
    }

    // CT04 - Nome vazio ("") (RF02)
    @Test
    void naoDeveSalvarProdutoComNomeVazio() {
        ProdutoModel produto = new ProdutoModel("", 49.90, 5);

        assertThrows(IllegalArgumentException.class, () -> service.salvar(produto));
        verify(repository, never()).salvar(any());
    }

    // CT05 - Nome nulo (RF02)
    @Test
    void naoDeveSalvarProdutoComNomeNulo() {
        ProdutoModel produto = new ProdutoModel(null, 49.90, 5);

        assertThrows(IllegalArgumentException.class, () -> service.salvar(produto));
        verify(repository, never()).salvar(any());
    }

    // CT06 - Quantidade negativa (-1) (RF02)
    @Test
    void naoDeveSalvarProdutoComQuantidadeNegativa() {
        ProdutoModel produto = new ProdutoModel("Mouse", 49.90, -1);

        assertThrows(IllegalArgumentException.class, () -> service.salvar(produto));
        verify(repository, never()).salvar(any());
    }

    // CT07 - Quantidade igual a zero (RF02)
    @Test
    void deveSalvarProdutoComQuantidadeZero() {
        ProdutoModel produto = new ProdutoModel("Mouse", 49.90, 0);
        when(repository.salvar(produto)).thenReturn(produto);

        ProdutoModel resultado = service.salvar(produto);

        assertEquals(0, resultado.getQuantidade());
        verify(repository, times(1)).salvar(produto);
    }

    // CT08 - Buscar produto existente (ID 1) (RF03)
    @Test
    void deveBuscarProdutoPorId() throws ProdutoNaoEncontradoException {
        ProdutoModel produto = new ProdutoModel("Mouse", 49.90, 10);
        when(repository.buscarPorId(1L)).thenReturn(produto);

        ProdutoModel resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Mouse", resultado.getNome());
        verify(repository, times(1)).buscarPorId(1L);
    }

    // CT09 - Buscar produto inexistente (ID 99) (RF03)
    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        when(repository.buscarPorId(99L)).thenReturn(null);

        assertThrows(ProdutoNaoEncontradoException.class, () -> service.buscarPorId(99L));
        verify(repository, times(1)).buscarPorId(99L);
    }

    // CT10 - Falha no repositório ao salvar (RF04)
    @Test
    void deveLancarErroQuandoRepositoryFalharAoSalvar() {
        ProdutoModel produto = new ProdutoModel("Teclado", 129.90, 5);
        doThrow(new RuntimeException("Falha na conexão"))
                .when(repository).salvar(any(ProdutoModel.class));

        assertThrows(ErroAoSalvarProdutoException.class, () -> service.salvar(produto));
        verify(repository, times(1)).salvar(any(ProdutoModel.class));
    }
}