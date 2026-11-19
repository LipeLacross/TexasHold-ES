package com.projeto.texasholdes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PontuacaoJogadorResponse {
    private Long idJogador;
    private String nomeJogador;
    private String combinacao;
    private int pontuacao;
    private String cartas;
    private String descricao;
}
