package com.projeto.texasholdes.strategy;

import com.projeto.texasholdes.entity.Carta;
import com.projeto.texasholdes.entity.TipoCombinacao;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Strategy específica para testar e pontuar Par / Dois Pares.
 */
@Component
public class ParesStrategy implements AvaliadorMaoStrategy {
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

        Map<Integer, List<Carta>> agrupados = cartas.stream()
                .collect(Collectors.groupingBy(Carta::getValorNumerico));

        List<Map.Entry<Integer, List<Carta>>> pares = agrupados.entrySet().stream()
                .filter(e -> e.getValue().size() >= 2)
                .sorted((e1, e2) -> Integer.compare(e2.getKey(), e1.getKey()))
                .toList();

        if (pares.size() >= 2) {
            int p1 = pares.get(0).getKey();
            int p2 = pares.get(1).getKey();
            return ResultadoAvaliacaoMao.builder()
                    .combinacao(TipoCombinacao.DOIS_PARES)
                    .pontuacaoTotal(300000 + p1 * 100 + p2)
                    .cartasRelevantes(cartas)
                    .descricao("Dois Pares (" + p1 + " e " + p2 + ")")
                    .build();
        } else if (pares.size() == 1) {
            int p1 = pares.get(0).getKey();
            return ResultadoAvaliacaoMao.builder()
                    .combinacao(TipoCombinacao.UM_PAR)
                    .pontuacaoTotal(200000 + p1 * 100)
                    .cartasRelevantes(cartas)
                    .descricao("Um Par (" + p1 + ")")
                    .build();
        }

        Carta maisAlta = cartas.stream().max(Comparator.comparingInt(Carta::getValorNumerico)).orElse(cartas.get(0));
        return ResultadoAvaliacaoMao.builder()
                .combinacao(TipoCombinacao.CARTA_ALTA)
                .pontuacaoTotal(100000 + maisAlta.getValorNumerico())
                .cartasRelevantes(List.of(maisAlta))
                .descricao("Sem par - Carta Alta: " + maisAlta.getValor())
                .build();
    }
}
