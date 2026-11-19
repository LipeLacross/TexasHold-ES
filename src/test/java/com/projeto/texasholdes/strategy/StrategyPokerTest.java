package com.projeto.texasholdes.strategy;

import com.projeto.texasholdes.entity.Carta;
import com.projeto.texasholdes.entity.TipoCombinacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StrategyPokerTest {

    private final RegraPokerStrategy regraPokerStrategy = new RegraPokerStrategy();
    private final CartaAltaStrategy cartaAltaStrategy = new CartaAltaStrategy();
    private final ParesStrategy paresStrategy = new ParesStrategy();
    private final AgrupamentosStrategy agrupamentosStrategy = new AgrupamentosStrategy();
    private final SequenciaEFlushStrategy sequenciaEFlushStrategy = new SequenciaEFlushStrategy();

    @Test
    @DisplayName("Strategy RegraPoker: deve identificar Royal Flush")
    void deveAvaliarRoyalFlush() {
        List<Carta> cartas = List.of(
                new Carta("10", "Copas"),
                new Carta("Valete", "Copas"),
                new Carta("Dama", "Copas"),
                new Carta("Rei", "Copas"),
                new Carta("Ás", "Copas")
        );

        ResultadoAvaliacaoMao res = regraPokerStrategy.avaliar(cartas);

        assertEquals(TipoCombinacao.ROYAL_FLUSH, res.getCombinacao());
        assertEquals(1000000, res.getPontuacaoTotal());
    }

    @Test
    @DisplayName("Strategy RegraPoker: deve identificar Straight Flush")
    void deveAvaliarStraightFlush() {
        List<Carta> cartas = List.of(
                new Carta("9", "Espadas"),
                new Carta("8", "Espadas"),
                new Carta("7", "Espadas"),
                new Carta("6", "Espadas"),
                new Carta("5", "Espadas")
        );

        ResultadoAvaliacaoMao res = regraPokerStrategy.avaliar(cartas);

        assertEquals(TipoCombinacao.STRAIGHT_FLUSH, res.getCombinacao());
        assertTrue(res.getPontuacaoTotal() > 900000);
    }

    @Test
    @DisplayName("Strategy Agrupamentos: deve avaliar Quadra")
    void deveAvaliarQuadra() {
        List<Carta> cartas = List.of(
                new Carta("Rei", "Copas"),
                new Carta("Rei", "Espadas"),
                new Carta("Rei", "Ouros"),
                new Carta("Rei", "Paus"),
                new Carta("2", "Copas")
        );

        ResultadoAvaliacaoMao res = agrupamentosStrategy.avaliar(cartas);

        assertEquals(TipoCombinacao.QUADRA, res.getCombinacao());
        assertTrue(res.getPontuacaoTotal() > 800000);
    }

    @Test
    @DisplayName("Strategy Agrupamentos: deve avaliar Full House")
    void deveAvaliarFullHouse() {
        List<Carta> cartas = List.of(
                new Carta("10", "Copas"),
                new Carta("10", "Espadas"),
                new Carta("10", "Ouros"),
                new Carta("4", "Paus"),
                new Carta("4", "Copas")
        );

        ResultadoAvaliacaoMao res = agrupamentosStrategy.avaliar(cartas);

        assertEquals(TipoCombinacao.FULL_HOUSE, res.getCombinacao());
        assertTrue(res.getPontuacaoTotal() >= 700000);
    }

    @Test
    @DisplayName("Strategy SequenciaEFlush: deve avaliar Flush")
    void deveAvaliarFlush() {
        List<Carta> cartas = List.of(
                new Carta("2", "Ouros"),
                new Carta("5", "Ouros"),
                new Carta("8", "Ouros"),
                new Carta("Valete", "Ouros"),
                new Carta("Rei", "Ouros")
        );

        ResultadoAvaliacaoMao res = sequenciaEFlushStrategy.avaliar(cartas);

        assertEquals(TipoCombinacao.FLUSH, res.getCombinacao());
        assertTrue(res.getPontuacaoTotal() >= 600000);
    }

    @Test
    @DisplayName("Strategy SequenciaEFlush: deve avaliar Straight")
    void deveAvaliarStraight() {
        List<Carta> cartas = List.of(
                new Carta("9", "Copas"),
                new Carta("8", "Espadas"),
                new Carta("7", "Ouros"),
                new Carta("6", "Paus"),
                new Carta("5", "Copas")
        );

        ResultadoAvaliacaoMao res = sequenciaEFlushStrategy.avaliar(cartas);

        assertEquals(TipoCombinacao.STRAIGHT, res.getCombinacao());
        assertTrue(res.getPontuacaoTotal() >= 500000);
    }

    @Test
    @DisplayName("Strategy Agrupamentos: deve avaliar Trinca")
    void deveAvaliarTrinca() {
        List<Carta> cartas = List.of(
                new Carta("7", "Copas"),
                new Carta("7", "Espadas"),
                new Carta("7", "Ouros"),
                new Carta("2", "Paus"),
                new Carta("3", "Copas")
        );

        ResultadoAvaliacaoMao res = agrupamentosStrategy.avaliar(cartas);

        assertEquals(TipoCombinacao.TRINCA, res.getCombinacao());
        assertTrue(res.getPontuacaoTotal() >= 400000);
    }

    @Test
    @DisplayName("Strategy Pares: deve avaliar Dois Pares")
    void deveAvaliarDoisPares() {
        List<Carta> cartas = List.of(
                new Carta("Valete", "Copas"),
                new Carta("Valete", "Espadas"),
                new Carta("4", "Ouros"),
                new Carta("4", "Paus"),
                new Carta("2", "Copas")
        );

        ResultadoAvaliacaoMao res = paresStrategy.avaliar(cartas);

        assertEquals(TipoCombinacao.DOIS_PARES, res.getCombinacao());
        assertTrue(res.getPontuacaoTotal() >= 300000);
    }

    @Test
    @DisplayName("Strategy Pares: deve avaliar Um Par")
    void deveAvaliarUmPar() {
        List<Carta> cartas = List.of(
                new Carta("8", "Copas"),
                new Carta("8", "Espadas"),
                new Carta("2", "Ouros"),
                new Carta("3", "Paus"),
                new Carta("5", "Copas")
        );

        ResultadoAvaliacaoMao res = paresStrategy.avaliar(cartas);

        assertEquals(TipoCombinacao.UM_PAR, res.getCombinacao());
        assertTrue(res.getPontuacaoTotal() >= 200000);
    }

    @Test
    @DisplayName("Strategy CartaAlta: deve avaliar Carta Alta")
    void deveAvaliarCartaAlta() {
        List<Carta> cartas = List.of(
                new Carta("Ás", "Copas"),
                new Carta("4", "Espadas"),
                new Carta("7", "Ouros")
        );

        ResultadoAvaliacaoMao res = cartaAltaStrategy.avaliar(cartas);

        assertEquals(TipoCombinacao.CARTA_ALTA, res.getCombinacao());
        assertEquals(100014, res.getPontuacaoTotal());
        assertEquals("Carta Alta: Ás", res.getDescricao());
    }
}
