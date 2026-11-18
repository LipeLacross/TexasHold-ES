package com.projeto.texasholdes.strategy;

import com.projeto.texasholdes.entity.Carta;
import com.projeto.texasholdes.entity.TipoCombinacao;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RegraPokerStrategy implements AvaliadorMaoStrategy {

    @Override
    public ResultadoAvaliacaoMao avaliar(List<Carta> cartas) {
        if (cartas == null || cartas.isEmpty()) {
            return ResultadoAvaliacaoMao.builder()
                    .combinacao(TipoCombinacao.CARTA_ALTA)
                    .pontuacaoTotal(0)
                    .cartasRelevantes(List.of())
                    .descricao("Sem cartas")
                    .build();
        }

        List<Carta> ordenadas = cartas.stream()
                .sorted(Comparator.comparingInt(Carta::getValorNumerico).reversed())
                .toList();

        boolean mesmoNaipe = cartas.stream().map(Carta::getNaipe).distinct().count() == 1 && cartas.size() >= 5;
        boolean sequencia = ehSequencia(ordenadas);

        // 1. Royal Flush
        if (mesmoNaipe && sequencia && ordenadas.get(0).getValorNumerico() == 14) {
            return buildResultado(TipoCombinacao.ROYAL_FLUSH, 1000000, ordenadas, "Royal Flush");
        }

        // 2. Straight Flush
        if (mesmoNaipe && sequencia) {
            int score = 900000 + ordenadas.get(0).getValorNumerico();
            return buildResultado(TipoCombinacao.STRAIGHT_FLUSH, score, ordenadas, "Straight Flush ao " + ordenadas.get(0).getValor());
        }

        Map<Integer, Long> contagemValores = ordenadas.stream()
                .collect(Collectors.groupingBy(Carta::getValorNumerico, Collectors.counting()));

        List<Map.Entry<Integer, Long>> grupos = contagemValores.entrySet().stream()
                .sorted((e1, e2) -> {
                    int cmp = Long.compare(e2.getValue(), e1.getValue());
                    return cmp != 0 ? cmp : Integer.compare(e2.getKey(), e1.getKey());
                })
                .toList();

        // 3. Quadra
        if (grupos.get(0).getValue() == 4) {
            int valQuadra = grupos.get(0).getKey();
            int score = 800000 + valQuadra * 100;
            return buildResultado(TipoCombinacao.QUADRA, score, ordenadas, "Quadra de " + valQuadra);
        }

        // 4. Full House
        if (grupos.size() >= 2 && grupos.get(0).getValue() == 3 && grupos.get(1).getValue() >= 2) {
            int valTrinca = grupos.get(0).getKey();
            int valPar = grupos.get(1).getKey();
            int score = 700000 + valTrinca * 100 + valPar;
            return buildResultado(TipoCombinacao.FULL_HOUSE, score, ordenadas, "Full House de " + valTrinca + " com " + valPar);
        }

        // 5. Flush
        if (mesmoNaipe) {
            int score = 600000 + ordenadas.get(0).getValorNumerico();
            return buildResultado(TipoCombinacao.FLUSH, score, ordenadas, "Flush de " + ordenadas.get(0).getNaipe());
        }

        // 6. Straight
        if (sequencia) {
            int score = 500000 + ordenadas.get(0).getValorNumerico();
            return buildResultado(TipoCombinacao.STRAIGHT, score, ordenadas, "Straight com carta mais alta " + ordenadas.get(0).getValor());
        }

        // 7. Trinca
        if (grupos.get(0).getValue() == 3) {
            int valTrinca = grupos.get(0).getKey();
            int score = 400000 + valTrinca * 100;
            return buildResultado(TipoCombinacao.TRINCA, score, ordenadas, "Trinca de " + valTrinca);
        }

        // 8. Dois Pares
        if (grupos.size() >= 2 && grupos.get(0).getValue() == 2 && grupos.get(1).getValue() == 2) {
            int p1 = grupos.get(0).getKey();
            int p2 = grupos.get(1).getKey();
            int score = 300000 + p1 * 100 + p2;
            return buildResultado(TipoCombinacao.DOIS_PARES, score, ordenadas, "Dois Pares de " + p1 + " e " + p2);
        }

        // 9. Um Par
        if (grupos.get(0).getValue() == 2) {
            int valPar = grupos.get(0).getKey();
            int score = 200000 + valPar * 100;
            return buildResultado(TipoCombinacao.UM_PAR, score, ordenadas, "Um Par de " + valPar);
        }

        // 10. Carta Alta
        int valAlta = ordenadas.get(0).getValorNumerico();
        int score = 100000 + valAlta;
        return buildResultado(TipoCombinacao.CARTA_ALTA, score, ordenadas, "Carta Alta: " + ordenadas.get(0).getValor());
    }

    private boolean ehSequencia(List<Carta> ordenadas) {
        if (ordenadas.size() < 5) {
            return false;
        }
        for (int i = 0; i < ordenadas.size() - 1; i++) {
            if (ordenadas.get(i).getValorNumerico() - ordenadas.get(i + 1).getValorNumerico() != 1) {
                return false;
            }
        }
        return true;
    }

    private ResultadoAvaliacaoMao buildResultado(TipoCombinacao tipo, int score, List<Carta> cartas, String desc) {
        return ResultadoAvaliacaoMao.builder()
                .combinacao(tipo)
                .pontuacaoTotal(score)
                .cartasRelevantes(cartas)
                .descricao(desc)
                .build();
    }
}
