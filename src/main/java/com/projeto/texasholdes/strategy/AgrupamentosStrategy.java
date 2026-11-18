package com.projeto.texasholdes.strategy;

import com.projeto.texasholdes.entity.Carta;
import com.projeto.texasholdes.entity.TipoCombinacao;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Strategy específica para testar e pontuar Trinca / Full House / Quadra.
 */
@Component
public class AgrupamentosStrategy implements AvaliadorMaoStrategy {
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

        Map<Integer, Long> contagem = cartas.stream()
                .collect(Collectors.groupingBy(Carta::getValorNumerico, Collectors.counting()));

        List<Map.Entry<Integer, Long>> grupos = contagem.entrySet().stream()
                .sorted((e1, e2) -> {
                    int cmp = Long.compare(e2.getValue(), e1.getValue());
                    return cmp != 0 ? cmp : Integer.compare(e2.getKey(), e1.getKey());
                })
                .toList();

        if (grupos.get(0).getValue() == 4) {
            int val = grupos.get(0).getKey();
            return ResultadoAvaliacaoMao.builder()
                    .combinacao(TipoCombinacao.QUADRA)
                    .pontuacaoTotal(800000 + val * 100)
                    .cartasRelevantes(cartas)
                    .descricao("Quadra de " + val)
                    .build();
        }

        if (grupos.size() >= 2 && grupos.get(0).getValue() == 3 && grupos.get(1).getValue() >= 2) {
            int trinca = grupos.get(0).getKey();
            int par = grupos.get(1).getKey();
            return ResultadoAvaliacaoMao.builder()
                    .combinacao(TipoCombinacao.FULL_HOUSE)
                    .pontuacaoTotal(700000 + trinca * 100 + par)
                    .cartasRelevantes(cartas)
                    .descricao("Full House (" + trinca + " e " + par + ")")
                    .build();
        }

        if (grupos.get(0).getValue() == 3) {
            int val = grupos.get(0).getKey();
            return ResultadoAvaliacaoMao.builder()
                    .combinacao(TipoCombinacao.TRINCA)
                    .pontuacaoTotal(400000 + val * 100)
                    .cartasRelevantes(cartas)
                    .descricao("Trinca de " + val)
                    .build();
        }

        Carta maisAlta = cartas.stream().max(Comparator.comparingInt(Carta::getValorNumerico)).orElse(cartas.get(0));
        return ResultadoAvaliacaoMao.builder()
                .combinacao(TipoCombinacao.CARTA_ALTA)
                .pontuacaoTotal(100000 + maisAlta.getValorNumerico())
                .cartasRelevantes(List.of(maisAlta))
                .descricao("Sem agrupamento triplo - Carta Alta: " + maisAlta.getValor())
                .build();
    }
}
