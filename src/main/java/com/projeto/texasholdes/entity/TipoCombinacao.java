package com.projeto.texasholdes.entity;

public enum TipoCombinacao {
    CARTA_ALTA(1, "Carta Alta"),
    UM_PAR(2, "Um Par"),
    DOIS_PARES(3, "Dois Pares"),
    TRINCA(4, "Trinca"),
    STRAIGHT(5, "Straight (Sequência)"),
    FLUSH(6, "Flush"),
    FULL_HOUSE(7, "Full House"),
    QUADRA(8, "Quadra"),
    STRAIGHT_FLUSH(9, "Straight Flush"),
    ROYAL_FLUSH(10, "Royal Flush");

    private final int pontuacaoBase;
    private final String descricao;

    TipoCombinacao(int pontuacaoBase, String descricao) {
        this.pontuacaoBase = pontuacaoBase;
        this.descricao = descricao;
    }

    public int getPontuacaoBase() {
        return pontuacaoBase;
    }

    public String getDescricao() {
        return descricao;
    }
}
