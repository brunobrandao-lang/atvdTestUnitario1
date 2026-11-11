package org.example;

public class ProdutoModel {
        private String nome;
        private double preco;
        private int quantidade;

        public ProdutoModel(String nome, double preco, int quantidade) {
            this.nome = nome;
            this.preco = preco;
            this.quantidade = quantidade;
        }

        public String getNome() {
            return nome;
        }

        public double getPreco() {
            return preco;
        }

        public int getQuantidade() {
            return quantidade;
        }
}
