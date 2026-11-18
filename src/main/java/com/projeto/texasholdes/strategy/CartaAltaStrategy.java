package com.projeto.texasholdes.strategy;

import com.projeto.texasholdes.entity.Carta;
import com.projeto.texasholdes.entity.TipoCombinacao;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * Strategy específica para testar e pontuar a combinação Carta Alta.
 */
@Component
public class CartaAltaStrategy implements AvaliadorMaoStrategy {
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
        Carta maisAlta = cartas.stream()
                .max(Comparator.comparingInt(Carta::getValorNumerico))
                .orElse(cartas.get(0));

        return ResultadoAvaliacaoMao.builder()
                .combinacao(TipoCombinacao.CARTA_ALTA)
                .pontuacaoTotal(100000 + maisAlta.getValorNumerico())
                .cartasRelevantes(List.of(maisAlta))
                .descricao("Carta Alta: " + maisAlta.getValor())
                .build();
    }
}
