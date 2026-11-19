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
TexasHold-ES (Felipe Moreira Rios, Isaac Adler)
```

### 3. Link para o repositório no GitHub (Público):
```text
https://github.com/LipeLacross/TexasHold-ES
```

---

### 4. História de Usuário:
```text
Contabilização de pontos finais da rodada e apuração do jogador vencedor (Showdown / Fim de Partida)
```

---

### 5. Padrão de Projeto Escolhido:
```text
Strategy e Facade (Fachada)
```

**Justificativa Técnica:**
> 1. **Padrão Fachada (Facade):** O acesso a todo o subsistema de domínio (`JogadorService`, `PartidaService`, `BaralhoService` e avaliadores) foi centralizado na interface `PokerFacade`, desacoplando o controller web da complexidade interna do negócio.
> 2. **Padrão Strategy:** A contabilização dos pontos da rodada e a escolha do vencedor foram desacopladas através da interface `AvaliadorMaoStrategy`. Isso permite injetar dinamicamente diferentes estratégias de avaliação (`RegraPokerStrategy`, `CartaAltaStrategy`, `ParesStrategy`, `AgrupamentosStrategy`, `SequenciaEFlushStrategy`), possibilitando testar e pontuar de forma isolada e extensível cada uma das combinações possíveis de jogo sem violar o princípio Open/Closed (OCP).

---

### 6. Commit da Implementação do Projeto:

Hashes e referências diretas no GitHub:

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

---

## 🎮 Roteiro de Demonstração / Execução via API (cURL)

Caso queira demonstrar a execução do jogo puramente via chamadas HTTP (API REST) no terminal ou Postman:

### 1. Iniciar a aplicação Spring Boot
```powershell
.\mvnw spring-boot:run
```

### 2. Cadastrar 2 Jogadores (US01)
```powershell
# Jogador 1 (Felipe)
curl.exe -X POST "http://localhost:8080/poker/cadastro" `
  -H "Content-Type: application/json" `
  -d '{\"nome\":\"Felipe\",\"login\":\"felipe\",\"senha\":\"senha123\"}'

# Jogador 2 (Adler)
curl.exe -X POST "http://localhost:8080/poker/cadastro" `
  -H "Content-Type: application/json" `
  -d '{\"nome\":\"Adler\",\"login\":\"adler\",\"senha\":\"senha123\"}'
```
*Retorno HTTP 201 com os IDs gerados (ex: `id: 1` e `id: 2`).*

### 3. Criar uma Mesa / Partida
```powershell
curl.exe -X POST "http://localhost:8080/poker/criarPartida?idCriador=1&maxJogadores=8"
```
*Retorno HTTP 201: `{"idPartida": 1}`.*

### 4. Consultar Mesas Abertas (US12)
```powershell
curl.exe -X GET "http://localhost:8080/poker/partidasAbertas"
```
*Retorna a lista de partidas com status `CRIADA` e vagas disponíveis.*

### 5. Jogador 2 Entrar na Mesa (US09)
```powershell
curl.exe -X POST "http://localhost:8080/poker/1/entrar?idJogador=2"
```
*Debita o cacife inicial de 20 fichas e senta o jogador na mesa.*

### 6. Iniciar Partida e Distribuir Cartas (US10 & US13)
```powershell
curl.exe -X POST "http://localhost:8080/poker/1/iniciar?idJogadorResponsavel=1"
```
*Embaralha, distribui 2 cartas para cada jogador e 5 cartas na mesa.*

### 7. Apurar Vencedor e Contabilizar Pontos via Strategy!
```powershell
curl.exe -X POST "http://localhost:8080/poker/1/apurar"
```

> **Dica para o professor:** É possível testar cada possibilidade passando o parâmetro `?estrategia=` (ex: `PARES`, `FLUSH`, etc.):
> ```powershell
> curl.exe -X POST "http://localhost:8080/poker/1/apurar?estrategia=PARES"
> ```

