# Project Architecture

## 📁 Project Structure

The project follows a layered architecture (Controller -> Facade -> Service -> Repository -> Entity) powered by Spring Boot and Java 21, adhering to Clean Code, SOLID, DRY, and GoF Design Patterns (**Facade** and **Strategy**).

```
TexasHold-ES/
├── .mvn/
│   └── wrapper/
│       ├── maven-wrapper.jar
│       └── maven-wrapper.properties
├── src/
│   ├── main/
│   │   ├── java/com/projeto/texasholdes/
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   └── PokerController.java
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   │   └── CadastroJogadorRequest.java
│   │   │   │   └── response/
│   │   │   │       ├── ApuracaoResultadoResponse.java
│   │   │   │       ├── CadastroJogadorResponse.java
│   │   │   │       ├── OperacaoPartidaResponse.java
│   │   │   │       ├── PartidaAbertaResponse.java
│   │   │   │       └── PontuacaoJogadorResponse.java
│   │   │   ├── entity/
│   │   │   │   ├── Carta.java
│   │   │   │   ├── Jogador.java
│   │   │   │   ├── Partida.java
│   │   │   │   ├── StatusPartida.java
│   │   │   │   └── TipoCombinacao.java
│   │   │   ├── exception/
│   │   │   │   ├── BusinessException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── facade/
│   │   │   │   ├── PokerFacade.java
│   │   │   │   └── PokerFacadeImpl.java
│   │   │   ├── repository/
│   │   │   │   ├── JogadorRepository.java
│   │   │   │   └── PartidaRepository.java
│   │   │   ├── service/
│   │   │   │   ├── BaralhoService.java
│   │   │   │   ├── JogadorService.java
│   │   │   │   └── PartidaService.java
│   │   │   ├── strategy/
│   │   │   │   ├── AgrupamentosStrategy.java
│   │   │   │   ├── AvaliadorMaoStrategy.java
│   │   │   │   ├── CartaAltaStrategy.java
│   │   │   │   ├── ParesStrategy.java
│   │   │   │   ├── RegraPokerStrategy.java
│   │   │   │   ├── ResultadoAvaliacaoMao.java
│   │   │   │   └── SequenciaEFlushStrategy.java
│   │   │   └── TexasholdesApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/com/projeto/texasholdes/
│       │   ├── controller/
│       │   │   └── PokerControllerTest.java
│       │   ├── facade/
│       │   │   └── PokerFacadeTest.java
│       │   ├── service/
│       │   │   ├── BaralhoServiceTest.java
│       │   │   ├── JogadorServiceTest.java
│       │   │   └── PartidaServiceTest.java
│       │   ├── strategy/
│       │   │   └── StrategyPokerTest.java
│       │   └── TexasholdesApplicationTests.java
│       └── resources/
│           └── application.properties
├── DATABASE.md
├── pom.xml
├── README.md
└── README_EN.md
```

### Directory Roles:
- **`controller/`**: Thin REST presentation layer delegating requests exclusively through the **PokerFacade**.
- **`facade/`**: **Facade (GoF)** design pattern interface and implementation encapsulating the complexities of players, matches, decks, and evaluation subsystems.
- **`strategy/`**: **Strategy (GoF)** design pattern implementation for hand ranking, card evaluation, and dynamic winner determination.
- **`dto/`**: Data Transfer Objects (Requests & Responses) decoupling internal persistence models from API contracts.
- **`service/`**: Core domain logic, validation orchestrations, and match lifecycle management.
- **`entity/`**: JPA persistence entities and domain value objects (`Jogador`, `Partida`, `Carta`, `StatusPartida`, `TipoCombinacao`).
- **`repository/`**: Spring Data JPA abstraction layer for database persistence.
- **`exception/`**: Centralized exception handling returning standardized error responses.
- **`config/`**: Security configuration, bean definitions, and password hashing (`BCryptPasswordEncoder`).
- **`test/`**: Unit and integration test suites covering happy and edge cases with H2 in-memory isolation and Mockito.

---

## 🎯 Architecture Principles & Design Patterns

1. **Padrão Fachada (Facade - GoF):**
   - Centraliza o ponto de entrada para o subsistema de domínio (`PokerFacade`). O `PokerController` interage unicamente com a Fachada, desacoplando completamente a camada web da orquestração interna entre `JogadorService`, `PartidaService`, `BaralhoService` e avaliadores.
2. **Padrão Strategy (Strategy - GoF):**
   - A contabilização dos pontos finais da rodada e a determinação do jogador vencedor é parametrizável via a interface `AvaliadorMaoStrategy`.
   - Permite testar cada possibilidade de jogo de forma modular e extensível:
     - `RegraPokerStrategy` (Avaliação padrão de todas as regras oficiais de Poker)
     - `CartaAltaStrategy` (Estratégia focada em Carta Alta)
     - `ParesStrategy` (Estratégia para Um Par e Dois Pares)
     - `AgrupamentosStrategy` (Estratégia para Trinca, Full House e Quadra)
     - `SequenciaEFlushStrategy` (Estratégia para Straight, Flush, Straight Flush e Royal Flush)
3. **Single Responsibility Principle (SRP):** Cada classe possui uma única razão para mudar.
4. **Resilient Security:** Hashing irreversível de senhas com BCrypt antes da escrita no banco.
5. **Environment Isolation:** Testes unitários e de integração 100% isolados com H2 em memória, sem depender da conectividade de rede externa.

---

## 📝 Conventions

- **File & Class Naming:**
  - Classes and Interfaces: `PascalCase` (e.g., `PokerFacade`, `AvaliadorMaoStrategy`).
  - Methods and Variables: `camelCase` (e.g., `apurarResultado`, `idPartida`).
  - Packages: lowercase, single words (e.g., `com.projeto.texasholdes.strategy`).
- **RESTful Endpoints:**
  - `POST /poker/cadastro` (US01)
  - `POST /poker/criarPartida`
  - `GET /poker/partidasAbertas` (US09)
  - `POST /poker/{idPartida}/entrar` (US10)
  - `POST /poker/{idPartida}/iniciar` (US12 e US13)
  - `POST /poker/{idPartida}/apurar` (Strategy de contabilização e apuração de vencedor)

---

## 🛠️ Maintenance and Expansion

### Maintenance:
- **Continuous Quality:** Keep all unit and integration tests green by executing `./mvnw test` before every commit.
- **Adding New Evaluation Strategies:** Implement the `AvaliadorMaoStrategy` interface and register it inside `PokerFacadeImpl`.

---

**Última atualização**: 18/11/2026  
**Versão do projeto**: 0.0.1-SNAPSHOT  
**Manutenedor**: Felipe Moreira
