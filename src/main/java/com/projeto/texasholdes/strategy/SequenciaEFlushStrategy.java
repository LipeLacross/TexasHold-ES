package com.projeto.texasholdes.strategy;

import com.projeto.texasholdes.entity.Carta;
import com.projeto.texasholdes.entity.TipoCombinacao;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * Strategy específica para testar e pontuar Flush / Straight / Straight Flush / Royal Flush.
 */
@Component
public class SequenciaEFlushStrategy implements AvaliadorMaoStrategy {
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

        if (mesmoNaipe && sequencia && ordenadas.get(0).getValorNumerico() == 14) {
            return ResultadoAvaliacaoMao.builder()
                    .combinacao(TipoCombinacao.ROYAL_FLUSH)
                    .pontuacaoTotal(1000000)
                    .cartasRelevantes(ordenadas)
                    .descricao("Royal Flush")
                    .build();
        }

        if (mesmoNaipe && sequencia) {
            return ResultadoAvaliacaoMao.builder()
                    .combinacao(TipoCombinacao.STRAIGHT_FLUSH)
                    .pontuacaoTotal(900000 + ordenadas.get(0).getValorNumerico())
                    .cartasRelevantes(ordenadas)
                    .descricao("Straight Flush")
                    .build();
        }

        if (mesmoNaipe) {
            return ResultadoAvaliacaoMao.builder()
                    .combinacao(TipoCombinacao.FLUSH)
                    .pontuacaoTotal(600000 + ordenadas.get(0).getValorNumerico())
                    .cartasRelevantes(ordenadas)
                    .descricao("Flush de " + ordenadas.get(0).getNaipe())
                    .build();
        }

        if (sequencia) {
            return ResultadoAvaliacaoMao.builder()
                    .combinacao(TipoCombinacao.STRAIGHT)
                    .pontuacaoTotal(500000 + ordenadas.get(0).getValorNumerico())
                    .cartasRelevantes(ordenadas)
                    .descricao("Straight")
                    .build();
        }

        Carta maisAlta = ordenadas.get(0);
        return ResultadoAvaliacaoMao.builder()
                .combinacao(TipoCombinacao.CARTA_ALTA)
                .pontuacaoTotal(100000 + maisAlta.getValorNumerico())
                .cartasRelevantes(List.of(maisAlta))
                .descricao("Sem sequência/flush - Carta Alta: " + maisAlta.getValor())
                .build();
    }

    private boolean ehSequencia(List<Carta> ordenadas) {
        if (ordenadas.size() < 5) return false;
        for (int i = 0; i < ordenadas.size() - 1; i++) {
            if (ordenadas.get(i).getValorNumerico() - ordenadas.get(i + 1).getValorNumerico() != 1) {
                return false;
            }
        }
        return true;
    }
}
