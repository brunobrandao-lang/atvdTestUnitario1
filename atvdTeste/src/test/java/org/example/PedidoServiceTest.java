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
class PedidoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService service;

    // PED01 e PED02 - Pedido Válido e Verificação de Interações
    @Test
    void deveCalcularValorTotalDoPedidoEInteragirComRepositorios() throws ProdutoNaoEncontradoException {
        ProdutoModel produto = new ProdutoModel("Mouse", 49.90, 10);
        when(produtoRepository.buscarPorId(1L)).thenReturn(produto);

        PedidoModel pedido = service.criarPedido(1L, 2);

        assertEquals(99.80, pedido.getValorTotal(), 0.001);
        verify(produtoRepository, times(1)).buscarPorId(1L);
        verify(pedidoRepository, times(1)).salvar(any(PedidoModel.class));
    }

    // PED03 - Quantidade igual a zero
    @Test
    void naoDeveCriarPedidoComQuantidadeZero() {
        assertThrows(IllegalArgumentException.class, () -> service.criarPedido(1L, 0));
        verify(pedidoRepository, never()).salvar(any());
    }

    // PED04 - Quantidade negativa (-3)
    @Test
    void naoDeveCriarPedidoComQuantidadeNegativa() {
        assertThrows(IllegalArgumentException.class, () -> service.criarPedido(1L, -3));
        verify(pedidoRepository, never()).salvar(any());
    }

    // PED05 - Produto inexistente (ID 99)
    @Test
    void naoDeveCriarPedidoSeProdutoNaoExistir() {
        when(produtoRepository.buscarPorId(99L)).thenReturn(null);

        assertThrows(ProdutoNaoEncontradoException.class, () -> service.criarPedido(99L, 2));
        verify(pedidoRepository, never()).salvar(any());
    }

    // PED06 - Quantidade maior que o estoque (11 com estoque 10)
    @Test
    void naoDeveCriarPedidoSeEstoqueForInsuficiente() {
        ProdutoModel produto = new ProdutoModel("Mouse", 49.90, 10);
        when(produtoRepository.buscarPorId(1L)).thenReturn(produto);

        assertThrows(EstoqueInsuficienteException.class, () -> service.criarPedido(1L, 11));
        verify(pedidoRepository, never()).salvar(any());
    }

    // PED07 - Quantidade igual ao estoque (10 com estoque 10 - valor de fronteira)
    @Test
    void deveCriarPedidoComQuantidadeIgualAoEstoque() throws ProdutoNaoEncontradoException {
        ProdutoModel produto = new ProdutoModel("Mouse", 49.90, 10);
        when(produtoRepository.buscarPorId(1L)).thenReturn(produto);

        PedidoModel pedido = service.criarPedido(1L, 10);

        assertEquals(499.00, pedido.getValorTotal(), 0.001);
        verify(pedidoRepository).salvar(any(PedidoModel.class));
    }

    // PED08 - Qualquer cenário de erro garante que o pedido não é salvo
    @Test
    void deveGarantirQuePedidoNaoESalvoEmQualquerCenarioDeErro() {
        when(produtoRepository.buscarPorId(99L)).thenReturn(null);

        assertThrows(ProdutoNaoEncontradoException.class, () -> service.criarPedido(99L, 2));

        verify(pedidoRepository, never()).salvar(any(PedidoModel.class));
    }

    // ATIVIDADE 7 -- Cenário A & D (Banco fora do ar ao salvar pedido com tratamento)
    @Test
    void deveLancarErroAoSalvarPedidoQuandoRepositoryFalhar() {
        ProdutoModel produto = new ProdutoModel("Mouse", 49.90, 10);
        when(produtoRepository.buscarPorId(1L)).thenReturn(produto);
        doThrow(new RuntimeException("Falha na conexão"))
                .when(pedidoRepository).salvar(any(PedidoModel.class));

        assertThrows(ErroAoSalvarPedidoException.class, () -> service.criarPedido(1L, 2));
        verify(pedidoRepository, times(1)).salvar(any(PedidoModel.class));
    }

    // ATIVIDADE 7 -- Cenário C (Falha no ProdutoRepository garante que PedidoRepository não é chamado)
    @Test
    void naoDeveSalvarPedidoSeProdutoRepositoryLancarExcecao() {
        when(produtoRepository.buscarPorId(1L)).thenThrow(new RuntimeException("Banco fora do ar"));

        assertThrows(RuntimeException.class, () -> service.criarPedido(1L, 2));
        verify(pedidoRepository, never()).salvar(any());
    }
}