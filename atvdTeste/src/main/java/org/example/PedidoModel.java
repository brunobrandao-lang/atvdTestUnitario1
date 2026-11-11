package org.example;

public class PedidoModel {
    private ProdutoModel produto;
    private int quantidade;
    private double valorTotal;

    public PedidoModel(ProdutoModel produto, int quantidade, double valorTotal) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
    }

    public ProdutoModel getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValorTotal() {
        return valorTotal;
    }
}