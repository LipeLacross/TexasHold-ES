package com.projeto.texasholdes.strategy;

import com.projeto.texasholdes.entity.Carta;
import java.util.List;

/**
 * Interface Strategy para avaliação de mãos de Poker.
 * Cada implementação avalia uma possibilidade específica de combinação
 * (Royal Flush, Straight Flush, Quadra, Full House, Flush, Straight, Trinca, Dois Pares, Um Par, Carta Alta).
 */
public interface AvaliadorMaoStrategy {
    ResultadoAvaliacaoMao avaliar(List<Carta> cartas);
}
