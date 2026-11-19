package com.projeto.texasholdes.controller;

import com.projeto.texasholdes.dto.request.CadastroJogadorRequest;
import com.projeto.texasholdes.dto.response.ApuracaoResultadoResponse;
import com.projeto.texasholdes.dto.response.CadastroJogadorResponse;
import com.projeto.texasholdes.dto.response.OperacaoPartidaResponse;
import com.projeto.texasholdes.dto.response.PartidaAbertaResponse;
import com.projeto.texasholdes.facade.PokerFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Controller de Apresentação REST.
 * Conforme exigido no Padrão FACHADA (Facade), todo o subsistema de domínio
 * é acessado exclusivamente através da interface PokerFacade.
 */
@RestController
@RequestMapping("/poker")
@RequiredArgsConstructor
public class PokerController {

    private final PokerFacade pokerFacade;

    @PostMapping("/cadastro")
    public ResponseEntity<CadastroJogadorResponse> cadastrar(@Valid @RequestBody CadastroJogadorRequest request) {
        CadastroJogadorResponse response = pokerFacade.cadastrarJogador(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/criarPartida")
    public ResponseEntity<Map<String, Object>> criarPartida(
            @RequestParam Long idCriador,
            @RequestParam(required = false, defaultValue = "8") Integer maxJogadores
    ) {
        Long idPartida = pokerFacade.criarPartida(idCriador, maxJogadores);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("idPartida", idPartida));
    }

    @GetMapping("/partidasAbertas")
    public ResponseEntity<List<PartidaAbertaResponse>> listarPartidasAbertas() {
        List<PartidaAbertaResponse> response = pokerFacade.listarPartidasAbertas();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{idPartida}/entrar")
    public ResponseEntity<OperacaoPartidaResponse> entrarNaPartida(
            @PathVariable Long idPartida,
            @RequestParam Long idJogador
    ) {
        OperacaoPartidaResponse response = pokerFacade.entrarNaPartida(idPartida, idJogador);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{idPartida}/iniciar")
    public ResponseEntity<OperacaoPartidaResponse> iniciarPartida(
            @PathVariable Long idPartida,
            @RequestParam(required = false) Long idJogadorResponsavel
    ) {
        OperacaoPartidaResponse response = pokerFacade.iniciarPartida(idPartida, idJogadorResponsavel);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para contabilizar pontos finais da rodada e informar quem ganhou via Strategy.
     * Permite passar o parâmetro opcional 'estrategia' para testar cada uma das possibilidades de jogo.
     */
    @PostMapping("/{idPartida}/apurar")
    public ResponseEntity<ApuracaoResultadoResponse> apurarResultado(
            @PathVariable Long idPartida,
            @RequestParam(required = false) String estrategia
    ) {
        ApuracaoResultadoResponse response = pokerFacade.apurarResultado(idPartida, estrategia);
        return ResponseEntity.ok(response);
    }
}
