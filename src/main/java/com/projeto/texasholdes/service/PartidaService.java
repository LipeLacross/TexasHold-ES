package com.projeto.texasholdes.service;

import com.projeto.texasholdes.dto.response.ApuracaoResultadoResponse;
import com.projeto.texasholdes.dto.response.OperacaoPartidaResponse;
import com.projeto.texasholdes.dto.response.PartidaAbertaResponse;
import com.projeto.texasholdes.dto.response.PontuacaoJogadorResponse;
import com.projeto.texasholdes.entity.Carta;
import com.projeto.texasholdes.entity.Jogador;
import com.projeto.texasholdes.entity.Partida;
import com.projeto.texasholdes.entity.StatusPartida;
import com.projeto.texasholdes.exception.BusinessException;
import com.projeto.texasholdes.exception.ResourceNotFoundException;
import com.projeto.texasholdes.repository.JogadorRepository;
import com.projeto.texasholdes.repository.PartidaRepository;
import com.projeto.texasholdes.strategy.AvaliadorMaoStrategy;
import com.projeto.texasholdes.strategy.RegraPokerStrategy;
import com.projeto.texasholdes.strategy.ResultadoAvaliacaoMao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final JogadorRepository jogadorRepository;
    private final BaralhoService baralhoService;
    private final RegraPokerStrategy regraPokerStrategyPadrao;

    @Transactional
    public Long criarPartida(Long idCriador, Integer maxJogadores) {
        Jogador criador = buscarJogadorOuFalhar(idCriador);
        int max = (maxJogadores != null && maxJogadores >= 2) ? maxJogadores : 8;

        Partida partida = Partida.builder()
                .criador(criador)
                .maxJogadores(max)
                .status(StatusPartida.AGUARDANDO_JOGADORES)
                .jogadores(new java.util.ArrayList<>(List.of(criador)))
                .build();

        return partidaRepository.save(partida).getId();
    }

    @Transactional(readOnly = true)
    public List<PartidaAbertaResponse> listarPartidasAbertas() {
        return partidaRepository.findAllByStatus(StatusPartida.AGUARDANDO_JOGADORES).stream()
                .map(partida -> PartidaAbertaResponse.builder()
                        .idPartida(partida.getId())
                        .quantidadeJogadores(partida.getQuantidadeJogadores())
                        .maxJogadores(partida.getMaxJogadores())
                        .status(partida.getStatus().name())
                        .build())
                .toList();
    }

    @Transactional
    public OperacaoPartidaResponse entrarNaPartida(Long idPartida, Long idJogador) {
        Partida partida = buscarPartidaOuFalhar(idPartida);
        Jogador jogador = buscarJogadorOuFalhar(idJogador);

        if (partida.getStatus() != StatusPartida.AGUARDANDO_JOGADORES) {
            throw new BusinessException("A partida não está aberta para novos participantes.");
        }

        if (partida.getQuantidadeJogadores() >= partida.getMaxJogadores()) {
            throw new BusinessException("A mesa está cheia.");
        }

        boolean jaPresente = partida.getJogadores().stream()
                .anyMatch(j -> j.getId().equals(jogador.getId()));

        if (jaPresente) {
            throw new BusinessException("O jogador já está nesta partida.");
        }

        partida.getJogadores().add(jogador);
        partidaRepository.save(partida);

        return OperacaoPartidaResponse.builder()
                .sucesso(true)
                .mensagem("Jogador entrou na partida com sucesso.")
                .build();
    }

    @Transactional
    public OperacaoPartidaResponse iniciarPartida(Long idPartida, Long idJogadorResponsavel) {
        Partida partida = buscarPartidaOuFalhar(idPartida);

        if (idJogadorResponsavel != null && !partida.getCriador().getId().equals(idJogadorResponsavel)) {
            throw new BusinessException("Apenas o jogador responsável pela criação da mesa pode iniciá-la.");
        }

        if (partida.getStatus() != StatusPartida.AGUARDANDO_JOGADORES) {
            throw new BusinessException("A partida já foi iniciada ou finalizada.");
        }

        if (partida.getQuantidadeJogadores() < 2) {
            throw new BusinessException("Não há jogadores suficientes para iniciar o jogo. Mínimo de 2 participantes.");
        }

        distribuirCartasAosJogadores(partida);

        partida.setStatus(StatusPartida.EM_ANDAMENTO);
        partidaRepository.save(partida);

        return OperacaoPartidaResponse.builder()
                .sucesso(true)
                .mensagem("Partida iniciada e cartas distribuídas com sucesso.")
                .build();
    }

    /**
     * Contabiliza os pontos finais da rodada e informa quem ganhou via Strategy.
     * Permite injetar/selecionar dinamicamente a Strategy para testar cada uma das possibilidades de jogo.
     */
    @Transactional
    public ApuracaoResultadoResponse apurarResultado(Long idPartida, AvaliadorMaoStrategy strategyCustomizada) {
        Partida partida = buscarPartidaOuFalhar(idPartida);

        if (partida.getStatus() == StatusPartida.AGUARDANDO_JOGADORES) {
            throw new BusinessException("A partida ainda não foi iniciada.");
        }

        AvaliadorMaoStrategy strategy = (strategyCustomizada != null) ? strategyCustomizada : regraPokerStrategyPadrao;
        Map<Long, String> cartasJogadores = partida.getCartasJogadores();

        List<PontuacaoJogadorResponse> pontuacoes = new ArrayList<>();

        for (Jogador jogador : partida.getJogadores()) {
            String cartasStr = cartasJogadores.getOrDefault(jogador.getId(), "");
            List<Carta> cartas = Arrays.stream(cartasStr.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Carta::parse)
                    .toList();

            ResultadoAvaliacaoMao avaliacao = strategy.avaliar(cartas);

            pontuacoes.add(PontuacaoJogadorResponse.builder()
                    .idJogador(jogador.getId())
                    .nomeJogador(jogador.getNome())
                    .combinacao(avaliacao.getCombinacao().getDescricao())
                    .pontuacao(avaliacao.getPontuacaoTotal())
                    .cartas(cartasStr)
                    .descricao(avaliacao.getDescricao())
                    .build());
        }

        PontuacaoJogadorResponse vencedor = pontuacoes.stream()
                .max(Comparator.comparingInt(PontuacaoJogadorResponse::getPontuacao))
                .orElseThrow(() -> new BusinessException("Não há participantes com mãos avaliáveis."));

        partida.setStatus(StatusPartida.FINALIZADA);
        partidaRepository.save(partida);

        return ApuracaoResultadoResponse.builder()
                .idPartida(partida.getId())
                .idVencedor(vencedor.getIdJogador())
                .nomeVencedor(vencedor.getNomeJogador())
                .melhorCombinacao(vencedor.getCombinacao())
                .pontuacaoVencedora(vencedor.getPontuacao())
                .estrategiaUtilizada(strategy.getClass().getSimpleName())
                .pontuacoes(pontuacoes)
                .build();
    }

    private void distribuirCartasAosJogadores(Partida partida) {
        List<Carta> baralho = baralhoService.criarBaralhoEmbaralhado();
        Map<Long, String> cartasJogadores = new HashMap<>();

        int indiceCarta = 0;
        for (Jogador participante : partida.getJogadores()) {
            Carta carta1 = baralho.get(indiceCarta++);
            Carta carta2 = baralho.get(indiceCarta++);
            cartasJogadores.put(participante.getId(), carta1 + ", " + carta2);
        }

        partida.setCartasJogadores(cartasJogadores);
    }

    private Partida buscarPartidaOuFalhar(Long idPartida) {
        return partidaRepository.findById(idPartida)
                .orElseThrow(() -> new ResourceNotFoundException("Partida não encontrada com ID: " + idPartida));
    }

    private Jogador buscarJogadorOuFalhar(Long idJogador) {
        return jogadorRepository.findById(idJogador)
                .orElseThrow(() -> new ResourceNotFoundException("Jogador não encontrado com ID: " + idJogador));
    }
}
