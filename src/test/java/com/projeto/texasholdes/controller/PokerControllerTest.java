package com.projeto.texasholdes.controller;

import com.projeto.texasholdes.dto.request.CadastroJogadorRequest;
import com.projeto.texasholdes.dto.response.ApuracaoResultadoResponse;
import com.projeto.texasholdes.dto.response.CadastroJogadorResponse;
import com.projeto.texasholdes.dto.response.OperacaoPartidaResponse;
import com.projeto.texasholdes.dto.response.PartidaAbertaResponse;
import com.projeto.texasholdes.facade.PokerFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokerControllerTest {

    @Mock
    private PokerFacade pokerFacade;

    @InjectMocks
    private PokerController pokerController;

    @Test
    @DisplayName("US01: POST /poker/cadastro - deve responder HTTP 201 com ID via Fachada")
    void deveCadastrarJogador() {
        CadastroJogadorRequest request = new CadastroJogadorRequest("Felipe", "felipe@teste.com", "senha123");
        when(pokerFacade.cadastrarJogador(any(CadastroJogadorRequest.class)))
                .thenReturn(new CadastroJogadorResponse(1L));

        ResponseEntity<CadastroJogadorResponse> response = pokerController.cadastrar(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(pokerFacade).cadastrarJogador(request);
    }

    @Test
    @DisplayName("POST /poker/criarPartida - deve responder HTTP 201 via Fachada")
    void deveCriarPartida() {
        when(pokerFacade.criarPartida(1L, 8)).thenReturn(10L);

        ResponseEntity<Map<String, Object>> response = pokerController.criarPartida(1L, 8);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10L, response.getBody().get("idPartida"));
        verify(pokerFacade).criarPartida(1L, 8);
    }

    @Test
    @DisplayName("US09: GET /poker/partidasAbertas - deve responder HTTP 200 com partidas abertas via Fachada")
    void deveListarPartidasAbertas() {
        when(pokerFacade.listarPartidasAbertas()).thenReturn(List.of(
                PartidaAbertaResponse.builder()
                        .idPartida(10L)
                        .quantidadeJogadores(3)
                        .maxJogadores(8)
                        .status("AGUARDANDO_JOGADORES")
                        .build()
        ));

        ResponseEntity<List<PartidaAbertaResponse>> response = pokerController.listarPartidasAbertas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(10L, response.getBody().get(0).getIdPartida());
        verify(pokerFacade).listarPartidasAbertas();
    }

    @Test
    @DisplayName("US10: POST /poker/{idPartida}/entrar - deve responder HTTP 200 ao entrar via Fachada")
    void deveEntrarNaPartida() {
        when(pokerFacade.entrarNaPartida(10L, 5L)).thenReturn(
                OperacaoPartidaResponse.builder().sucesso(true).mensagem("Entrou").build()
        );

        ResponseEntity<OperacaoPartidaResponse> response = pokerController.entrarNaPartida(10L, 5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSucesso());
        verify(pokerFacade).entrarNaPartida(10L, 5L);
    }

    @Test
    @DisplayName("US12 e US13: POST /poker/{idPartida}/iniciar - deve responder HTTP 200 ao iniciar via Fachada")
    void deveIniciarPartida() {
        when(pokerFacade.iniciarPartida(10L, 1L)).thenReturn(
                OperacaoPartidaResponse.builder().sucesso(true).mensagem("Iniciada").build()
        );

        ResponseEntity<OperacaoPartidaResponse> response = pokerController.iniciarPartida(10L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSucesso());
        verify(pokerFacade).iniciarPartida(10L, 1L);
    }

    @Test
    @DisplayName("Strategy: POST /poker/{idPartida}/apurar - deve responder HTTP 200 com vencedor via Fachada")
    void deveApurarResultado() {
        when(pokerFacade.apurarResultado(10L, "PARES")).thenReturn(
                ApuracaoResultadoResponse.builder()
                        .idPartida(10L)
                        .idVencedor(1L)
                        .nomeVencedor("Felipe")
                        .melhorCombinacao("Um Par")
                        .pontuacaoVencedora(201400)
                        .estrategiaUtilizada("ParesStrategy")
                        .pontuacoes(List.of())
                        .build()
        );

        ResponseEntity<ApuracaoResultadoResponse> response = pokerController.apurarResultado(10L, "PARES");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getIdVencedor());
        verify(pokerFacade).apurarResultado(10L, "PARES");
    }
}
