package com.projeto.texasholdes.facade;

import com.projeto.texasholdes.dto.request.CadastroJogadorRequest;
import com.projeto.texasholdes.dto.response.ApuracaoResultadoResponse;
import com.projeto.texasholdes.dto.response.CadastroJogadorResponse;
import com.projeto.texasholdes.dto.response.OperacaoPartidaResponse;
import com.projeto.texasholdes.dto.response.PartidaAbertaResponse;
import com.projeto.texasholdes.strategy.AvaliadorMaoStrategy;

import java.util.List;

/**
 * Padrão de Projeto: FACHADA (Facade)
 * Interface unificada para acesso simplificado a todos os serviços e operações
 * do subsistema de Pôquer Texas Hold'em (Jogadores, Partidas, Baralho e Apuração).
 */
public interface PokerFacade {

    // Operações de Jogador (US01)
    CadastroJogadorResponse cadastrarJogador(CadastroJogadorRequest request);

    // Operações de Partida e Mesa (US09, US10, US12, US13)
    Long criarPartida(Long idCriador, Integer maxJogadores);
    List<PartidaAbertaResponse> listarPartidasAbertas();
    OperacaoPartidaResponse entrarNaPartida(Long idPartida, Long idJogador);
    OperacaoPartidaResponse iniciarPartida(Long idPartida, Long idJogadorResponsavel);

    // Contabilização e Apuração dos Pontos Finais com Strategy
    ApuracaoResultadoResponse apurarResultado(Long idPartida, String tipoEstrategia);
}
