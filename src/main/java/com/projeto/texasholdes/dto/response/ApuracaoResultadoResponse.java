package com.projeto.texasholdes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApuracaoResultadoResponse {
    private Long idPartida;
    private Long idVencedor;
    private String nomeVencedor;
    private String melhorCombinacao;
    private int pontuacaoVencedora;
    private String estrategiaUtilizada;
    private List<PontuacaoJogadorResponse> pontuacoes;
}
