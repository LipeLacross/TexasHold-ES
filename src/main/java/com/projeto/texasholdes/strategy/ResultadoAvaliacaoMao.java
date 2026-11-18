package com.projeto.texasholdes.strategy;

import com.projeto.texasholdes.entity.Carta;
import com.projeto.texasholdes.entity.TipoCombinacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ResultadoAvaliacaoMao implements Comparable<ResultadoAvaliacaoMao> {
    private final TipoCombinacao combinacao;
    private final int pontuacaoTotal;
    private final List<Carta> cartasRelevantes;
    private final String descricao;

    @Override
    public int compareTo(ResultadoAvaliacaoMao outro) {
        return Integer.compare(this.pontuacaoTotal, outro.pontuacaoTotal);
    }
}
