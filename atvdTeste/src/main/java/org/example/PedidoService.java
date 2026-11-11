package org.example;

public class PedidoService {
    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;

    public PedidoService(ProdutoRepository produtoRepository, PedidoRepository pedidoRepository) {
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public PedidoModel criarPedido(Long produtoId, int quantidade) throws ProdutoNaoEncontradoException {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        ProdutoModel produto = produtoRepository.buscarPorId(produtoId);
        if (produto == null) {
            throw new ProdutoNaoEncontradoException("Produto não encontrado: " + produtoId);
        }

        if (quantidade > produto.getQuantidade()) {
            throw new EstoqueInsuficienteException("Estoque insuficiente");
        }

        double total = produto.getPreco() * quantidade;
        PedidoModel pedido = new PedidoModel(produto, quantidade, total);

        try {
            pedidoRepository.salvar(pedido);
        } catch (RuntimeException e) {
            throw new ErroAoSalvarPedidoException("Erro ao persistir pedido", e);
        }

        return pedido;
    }
}