# Respostas para o Formulário - Engenharia de Software (UFRPE)

**Atividade:** Implementação com Padrões de Projeto (GoF)  
**Aluno:** Felipe Moreira Rios (`felipe.rios@ufrpe.br`)  
**Data:** 18/11/2026  

---

## 📋 Dados para o Google Forms

### 1. E-mail:
```text
felipe.rios@ufrpe.br
```

### 2. Equipe:
```text
TexasHold-ES
```
*(ou o nome específico da sua equipe/integrantes: Felipe Moreira, Isaac Adler, etc.)*

### 3. Link para o repositório no GitHub (Público):
```text
https://github.com/LipeLacross/TexasHold-ES
```
*(ou se for o fork upstream do Isaac: `https://github.com/isaacadler/TexasHold-ES`)*

---

### 4. História de Usuário:
```text
Contabilização de pontos finais da rodada e apuração do jogador vencedor (Showdown / Fim de Partida)
```
*(Nota: Também cobre US12/US13 na inicialização e distribuição via Fachada)*

---

### 5. Padrão de Projeto Escolhido:
```text
Strategy e Facade (Fachada)
```

**Justificativa Técnica (caso o forms peça texto explicativo):**
> 1. **Padrão Fachada (Facade):** O acesso a todo o subsistema de domínio (`JogadorService`, `PartidaService`, `BaralhoService` e avaliadores) foi centralizado na interface `PokerFacade`, desacoplando o controller web da complexidade interna do negócio.
> 2. **Padrão Strategy:** A contabilização dos pontos da rodada e a escolha do vencedor foram desacopladas através da interface `AvaliadorMaoStrategy`. Isso permite injetar dinamicamente diferentes estratégias de avaliação (`RegraPokerStrategy`, `CartaAltaStrategy`, `ParesStrategy`, `AgrupamentosStrategy`, `SequenciaEFlushStrategy`), possibilitando testar e pontuar de forma isolada e extensível cada uma das combinações possíveis de jogo sem violar o princípio Open/Closed (OCP).

---

### 6. Commit da Implementação do Projeto:

Você pode fornecer o hash do commit ou o link direto no GitHub:

- **Hash do Commit (Strategy - Apuração e Pontuação):**
  ```text
  1429868
  ```
  **Link do Commit no GitHub:**
  ```text
  https://github.com/LipeLacross/TexasHold-ES/commit/1429868
  ```

- **Hash do Commit (Facade - Fachada do Modelo):**
  ```text
  eaf9be7
  ```
  **Link do Commit no GitHub:**
  ```text
  https://github.com/LipeLacross/TexasHold-ES/commit/eaf9be7
  ```

- **Hash do Commit Consolidado (Docs & Padrões):**
  ```text
  65a568c
  ```
  **Link do Commit no GitHub:**
  ```text
  https://github.com/LipeLacross/TexasHold-ES/commit/65a568c
  ```

---

## 🧪 Resumo dos Testes Unitários Executados
- **Total de Testes:** 32 testes unitários e de integração
- **Falhas:** 0
- **Status:** `BUILD SUCCESS`
- **Classes de Testes:**
  - `StrategyPokerTest`: Teste unitário para cada possibilidade de combinação (Royal Flush, Straight Flush, Quadra, Full House, Flush, Straight, Trinca, Dois Pares, Um Par, Carta Alta).
  - `PokerFacadeTest`: Testes da Fachada delegando chamadas para o modelo.
  - `PokerControllerTest`: Testes dos endpoints HTTP integrados à Fachada.
  - `PartidaServiceTest`: Teste da apuração de pontos finais com Strategy.
  - `JogadorServiceTest`, `BaralhoServiceTest`, `TexasholdesApplicationTests`.
