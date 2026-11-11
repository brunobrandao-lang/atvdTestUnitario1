package org.example;

public interface ProdutoRepository {
    ProdutoModel salvar(ProdutoModel produto);
    ProdutoModel buscarPorId(Long id);
}