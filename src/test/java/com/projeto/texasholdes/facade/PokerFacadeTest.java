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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokerFacadeTest {

    @Mock
    private JogadorService jogadorService;

    @Mock
    private PartidaService partidaService;

    @Mock
    private RegraPokerStrategy regraPokerStrategy;

    @Mock
    private CartaAltaStrategy cartaAltaStrategy;

    @Mock
    private ParesStrategy paresStrategy;

    @Mock
    private AgrupamentosStrategy agrupamentosStrategy;

    @Mock
    private SequenciaEFlushStrategy sequenciaEFlushStrategy;

    @InjectMocks
    private PokerFacadeImpl pokerFacade;

    @Test
    @DisplayName("Fachada: deve delegar cadastro de jogador para JogadorService")
    void deveDelegarCadastro() {
        CadastroJogadorRequest req = new CadastroJogadorRequest("Felipe", "felipe@teste.com", "senha123");
        when(jogadorService.cadastrar(req)).thenReturn(new CadastroJogadorResponse(1L));

        CadastroJogadorResponse resp = pokerFacade.cadastrarJogador(req);

        assertNotNull(resp);
        assertEquals(1L, resp.getId());
        verify(jogadorService).cadastrar(req);
    }

    @Test
    @DisplayName("Fachada: deve delegar criação e listagem de partidas para PartidaService")
    void deveDelegarPartidas() {
        when(partidaService.criarPartida(1L, 6)).thenReturn(10L);
        when(partidaService.listarPartidasAbertas()).thenReturn(List.of(
                PartidaAbertaResponse.builder().idPartida(10L).build()
        ));

        Long idPartida = pokerFacade.criarPartida(1L, 6);
        List<PartidaAbertaResponse> abertas = pokerFacade.listarPartidasAbertas();

        assertEquals(10L, idPartida);
        assertEquals(1, abertas.size());
        verify(partidaService).criarPartida(1L, 6);
        verify(partidaService).listarPartidasAbertas();
    }

    @Test
    @DisplayName("Fachada: deve delegar apuração resolvendo Strategy correspondente")
    void deveDelegarApuracaoComStrategyCorreta() {
        when(partidaService.apurarResultado(eq(10L), eq(paresStrategy)))
                .thenReturn(ApuracaoResultadoResponse.builder().idPartida(10L).idVencedor(1L).build());

        ApuracaoResultadoResponse resp = pokerFacade.apurarResultado(10L, "PARES");

        assertNotNull(resp);
        assertEquals(1L, resp.getIdVencedor());
        verify(partidaService).apurarResultado(eq(10L), eq(paresStrategy));
    }
}
