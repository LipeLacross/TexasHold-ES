package com.projeto.texasholdes.facade;

import com.projeto.texasholdes.dto.request.CadastroJogadorRequest;
import com.projeto.texasholdes.dto.response.ApuracaoResultadoResponse;
import com.projeto.texasholdes.dto.response.CadastroJogadorResponse;
import com.projeto.texasholdes.dto.response.OperacaoPartidaResponse;
import com.projeto.texasholdes.dto.response.PartidaAbertaResponse;
import com.projeto.texasholdes.service.JogadorService;
import com.projeto.texasholdes.service.PartidaService;
import com.projeto.texasholdes.strategy.AgrupamentosStrategy;
import com.projeto.texasholdes.strategy.AvaliadorMaoStrategy;
import com.projeto.texasholdes.strategy.CartaAltaStrategy;
import com.projeto.texasholdes.strategy.ParesStrategy;
import com.projeto.texasholdes.strategy.RegraPokerStrategy;
import com.projeto.texasholdes.strategy.SequenciaEFlushStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementação da Fachada do Sistema de Pôquer.
 * Unifica a orquestração do JogadorService, PartidaService e das Estratégias de Avaliação,
 * desacoplando a camada de apresentação (Controllers) da complexidade interna do domínio.
 */
@Component
@RequiredArgsConstructor
public class PokerFacadeImpl implements PokerFacade {

    private final JogadorService jogadorService;
    private final PartidaService partidaService;

    // Strategies de Avaliação disponíveis
    private final RegraPokerStrategy regraPokerStrategy;
    private final CartaAltaStrategy cartaAltaStrategy;
    private final ParesStrategy paresStrategy;
    private final AgrupamentosStrategy agrupamentosStrategy;
    private final SequenciaEFlushStrategy sequenciaEFlushStrategy;

    @Override
    public CadastroJogadorResponse cadastrarJogador(CadastroJogadorRequest request) {
        return jogadorService.cadastrar(request);
    }

    @Override
    public Long criarPartida(Long idCriador, Integer maxJogadores) {
        return partidaService.criarPartida(idCriador, maxJogadores);
    }

    @Override
    public List<PartidaAbertaResponse> listarPartidasAbertas() {
        return partidaService.listarPartidasAbertas();
    }

    @Override
    public OperacaoPartidaResponse entrarNaPartida(Long idPartida, Long idJogador) {
        return partidaService.entrarNaPartida(idPartida, idJogador);
    }

    @Override
    public OperacaoPartidaResponse iniciarPartida(Long idPartida, Long idJogadorResponsavel) {
        return partidaService.iniciarPartida(idPartida, idJogadorResponsavel);
    }

    @Override
    public ApuracaoResultadoResponse apurarResultado(Long idPartida, String tipoEstrategia) {
        AvaliadorMaoStrategy strategy = resolverStrategy(tipoEstrategia);
        return partidaService.apurarResultado(idPartida, strategy);
    }

    private AvaliadorMaoStrategy resolverStrategy(String tipoEstrategia) {
        if (tipoEstrategia == null || tipoEstrategia.isBlank()) {
            return regraPokerStrategy;
        }

        return switch (tipoEstrategia.trim().toUpperCase()) {
            case "CARTA_ALTA", "ALTA" -> cartaAltaStrategy;
            case "PARES", "PAR", "DOIS_PARES" -> paresStrategy;
            case "AGRUPAMENTOS", "TRINCA", "QUADRA", "FULL_HOUSE" -> agrupamentosStrategy;
            case "SEQUENCIA", "FLUSH", "STRAIGHT" -> sequenciaEFlushStrategy;
            default -> regraPokerStrategy;
        };
    }
}
