# Checkpoint 5 — Bug Hunt PetFiap

> **Professor:** Ygor Moraes Martins dos Anjos
---

## Identificação

**Grupo:** debugthebug

| Integrante | RM | Turma |
|---|---|---|
| Felipe Rodrigues Ribeiro | RM565274 | 2CCPW |
| Guilherme Ferraz de Medeiros | RM564743 | 2CCPW |
| Manoela Oliveira Bello | RM563952 | 2CCPW |
| Roberto Marques Moreira | RM564935 | 2CCPW |

| Campo | |
|---|---|
| **Total de bugs corrigidos** | 12 / 12 |
| **Total de ajustes de Clean Code** | 6 / 6 |
| **Total de testes novos escritos** | 6 / 6 |
| **Suíte final (Run As → JUnit Test)** | 26 testes, 0 falhas |

---

## Parte 1 — Bugs encontrados

| # | Sintoma observado (o que fiz/vi) | Causa raiz (arquivo e linha aproximada) | Correção aplicada | Conceito da disciplina |
|---|---|---|---|---|
| bug01 | `deveMontarAtendimentoCompleto` falhava com `expected: <Rex> but was: <null>` | `AtendimentoBuilder.java` (~26): atribuição `petNome = petNome;` gerava *variable shadowing*. | Adicionado `this.petNome = petNome;` para referenciar o atributo de instância. | Escopo de variáveis e uso da palavra-chave `this`. |
| bug02 | Builder aceitava criação de atendimento sem nome do pet ou porte (nenhuma exceção lançada). | `AtendimentoBuilder.java` (~43): método `construir()` não continha validações prévias. | Inseridas validações de nulidade e texto em branco lançando `IllegalArgumentException`. | *Fail Fast* e garantia de invariantes no Padrão Builder. |
| bug03 | `deveCriarTosaQuandoTipoForTosa` falhava retornando instância de `Banho`. | `AtendimentoFactory.java` (~20): no `case "TOSA"`, o estagiário deu `new Banho(...)`. | Corrigida a instanciação para retornar `new Tosa(...)`. | Polimorfismo e Padrão de Projeto Factory Method. |
| bug04 | `devePreencherOsDadosDoPetNaConsulta` falhava com dados cadastrais nulos (`<null>`). | `ConsultaVeterinaria.java` (~19): construtor chamava `super();` vazio descartando argumentos. | Atualizado para `super(protocolo, petNome, petPorte, tutorNome, dataHora);`. | Herança e inicialização de superclasses com `super(...)`. |
| bug05 | Singleton criava um objeto novo a cada chamada de `getInstancia()` e quebrava contagem sequencial. | `GeradorProtocolo.java` (~22): `if (instancia == null)` retornava direto sem atribuir à variável estática. | Atribuído à variável estática `instancia = new GeradorProtocolo();` com controle sincronizado. | Padrão Singleton e gerenciamento de estado estático único. |
| bug06 | Agendamento duplicava mesmo com mesmo pet, dia e hora; `HorarioOcupadoException` não era lançada. | `AgendaService.java` (~20): comparação de objetos feita com operador de referência `==`. | Substituído por `a.getPetNome().equals(...)` e `a.getDataHora().isEqual(...)`. | Comparação de igualdade (`.equals()`) vs identidade de ponteiro (`==`). |
| bug07 | `deveLancarExcecaoQuandoAtendimentoNaoExiste` falhava pois o método retornava `null`. | `AgendaService.java` (~34): bloco `try-catch` engolia a exceção e forçava `return null;`. | Removido o bloco `try-catch` indevido para permitir a propagação da exceção original. | Tratamento de exceções e proibição do antipadrão *Catch and Swallow*. |
| bug08 | Preço do banho invertido: cobrava R$ 100 para PEQUENO e R$ 60 para GRANDE. | `Banho.java` (~29): retornos literais de preço estavam com as faixas trocadas. | Ajustado para retornar R$ 60 (Pequeno), R$ 80 (Médio) e R$ 100 (Grande) via constantes. | Implementação de regras de negócio em modelos polimórficos. |
| bug09 | `Tosa.getDuracaoMinutos()` retornava 30 min (valor padrão da mãe) em vez de 60 min. | `Tosa.java` (~39): método declarado como `getDuracaoMinutos(String porte)` com parâmetro inútil. | Removido o parâmetro indevido e aplicada a sobrescrita `@Override public int getDuracaoMinutos()`. | Sobrescrita (*Override*) vs Sobrecarga (*Overload*). |
| bug10 | Sistema permitia agendamento em datas e horários passados sem qualquer bloqueio. | `AgendaService.java` (~17): ausência de validação temporal antes de consultar o banco. | Inserida validação *Fail Fast* com `isBefore(LocalDateTime.now())` lançando `IllegalArgumentException`. | Validação de domínio e consistência temporal de dados. |
| bug11 | Atendimento já concluído ou cancelado podia ser cancelado novamente sem restrições. | `Atendimento.java` (~52): método `cancelar()` apenas atribuía `status = "CANCELADO"` cegamente. | Adicionada verificação de status exigindo `AGENDADO`, lançando `StatusInvalidoException` caso contrário. | Modelagem de máquina de estados finitos em POO. |
| bug12 | Persistência JPA com chave primária nula e falha de mapeamento polimórfico de tabela única. | `Atendimento.java` (~14): falta de anotação de geração de ID (`@GeneratedValue`) e estratégia de herança. | Adicionados `@GeneratedValue(strategy = GenerationType.IDENTITY)`, `@Inheritance` e `@DiscriminatorColumn`. | Mapeamento Objeto-Relacional (JPA/Hibernate) com herança *Single Table*. |

---

## Parte 2 — Ajustes de Clean Code

| # | Onde estava | Qual princípio/boas práticas era violado | O que eu mudei |
|---|---|---|---|
| clean01 | `AtendimentoFactory.java` (método `criar`) | Nomes Significativos para Argumentos: uso de letras soltas indecifráveis (`p, t, n, po, tu, d`). | Renomeados os parâmetros para nomes semânticos (`protocolo, tipo, petNome, petPorte, tutorNome, dataHora`). |
| clean02 | `AtendimentoController.java` | Código Morto e Falta de Coesão: método privado `calcularDescontoFidelidade` nunca invocado. | Removido o método morto da controller, limpando o código de dívida técnica inútil. |
| clean03 | `Atendimento.java` | Setters Cegos / Encapsulamento: métodos `setProtocolo`, `setPetNome`, `setDataHora`, etc., quebrando a imutabilidade. | Removidos os setters desnecessários para atributos que não devem sofrer alteração após o registro. |
| clean04 | `Tosa.java` | Falta da anotação `@Override` explícita e quebra de contrato polimórfico na duração. | Adicionada a anotação `@Override` e normalizada a assinatura polimórfica herdada de `Atendimento`. |
| clean05 | `AgendaService.java` e `GeradorProtocolo.java` | Poluição de Saída Padrão: uso de `System.out.println` em código de produção e regras de negócio. | Removidas todas as chamadas de impressão manual no console do terminal. |
| clean06 | `Banho.java`, `Tosa.java`, `ConsultaVeterinaria.java`, `Atendimento.java` | Números Mágicos (*Magic Numbers*): literais de valores, durações e pontos soltos no meio dos métodos. | Extraídas constantes privadas bem nomeadas (`PRECO_PORTE_PEQUENO`, `DURACAO_MINUTOS`, `PONTOS_FIDELIDADE`, etc.). |

---

## Parte 3 — Testes novos (regras que estavam sem cobertura)

| # | Teste escrito (classe.método) | Regra coberta | Resultado ao escrever (vermelho/verde) |
|---|---|---|---|
| teste01 | `BanhoTest.deveCalcularPrecoCorretamenteParaCadaPorte` | Validação da tabela de preços escalonados do Banho (Pequeno: 60, Médio: 80, Grande: 100). | Vermelho (revelou o `bug08` de preços invertidos). |
| teste02 | `TosaTest.deveDurar60Minutos` | Validação da duração contratual de 60 minutos para tosa. | Vermelho (revelou o `bug09` onde retornava 30 da mãe). |
| teste03 | `AgendaServiceTest.deveRecusarAgendamentoComDataHoraNoPassado` | Bloqueio de agendamento retroativo lançando `IllegalArgumentException` sem acionar persistência. | Vermelho (revelou o `bug10` de ausência de validação). |
| teste04 | `AgendaServiceTest.deveRecusarCancelamentoDeAtendimentoJaConcluido` | Bloqueio de cancelamento para serviços concluídos lançando `StatusInvalidoException`. | Vermelho (revelou o `bug11` de transição cega de estado). |
| teste05 | `AgendaServiceTest.deveCancelarAtendimentoAgendadoComSucesso` | Garantia de transição legal de status de `AGENDADO` para `CANCELADO`. | Verde de cara (fluxo correto de cancelamento válido). |
| teste06 | `ConsultaVeterinariaTest.deveCobrarPrecoFixoIndependenteDoPorte` | Garantia de cobrança do valor fixo de R$ 150,00 da Consulta em qualquer circunstância. | Verde de cara (implementação no modelo já cumpria a regra). |

---

## Parte 4 — Perguntas de reflexão

### 1. A suíte como contrato (Aula 15)
A suíte entregue pelo professor atuou como o contrato executável de especificação de requisitos. Em vez de adivinhar o comportamento, as falhas nos indicavam a divergência exata: no `AtendimentoBuilderTest`, a mensagem `expected: <Rex> but was: <null>` apontou imediatamente que o atributo não estava sendo setado na construção da instância. Testar manualmente via requisições HTTP com `curl` seria inviável, lento e sujeito a falhas humanas: exige subir a aplicação inteira, configurar banco Oracle externo, disparar requisições manuais e interpretar retornos visuais. A suíte unitária roda isolada na JVM em menos de 2 segundos, garante cobertura de regressão instantânea e prova matematicamente que uma alteração não quebrou outras funcionalidades.

### 2. Mock e injeção de dependência (Aulas 13 a 15)
Em ambiente de produção, o Spring Boot gerencia o ciclo de vida dos componentes com o `@Autowired`, injetando a implementação concreta de `AtendimentoRepository` criada dinamicamente pelo Spring Data JPA conectada à base de dados. Já no `AgendaServiceTest`, o JUnit 5 executa em conjunto com a extensão do Mockito (`@ExtendWith(MockitoExtension.class)`): aqui é o Mockito quem cria um objeto simulado (dublê) com `@Mock` e o injeta via reflexão no serviço com `@InjectMocks`. O Spring sequer é inicializado. Isso permite programar o comportamento do repositório em memória via `when(...).thenReturn(...)` e comprovar chamadas com `verify()`, tornando o teste ultra-rápido, determinístico e totalmente independente de rede ou banco de dados externo.

### 3. `==` vs `.equals()` (Aula 7)
No Java, o operador `==` compara a identidade de referências na memória (se os ponteiros apontam para o mesmo endereço físico), enquanto o método `.equals()` compara a igualdade semântica dos dados. Literais de String estáticos como `"Rex"` compartilham o mesmo espaço no *String Pool* da JVM, fazendo com que comparações com `==` coincidam por mera coincidência em testes ingênuos. No entanto, quando objetos reais são instanciados em requisições distintas ou gerados dinamicamente via `LocalDateTime.parse()`, eles ocupam endereços de memória completamente diferentes. Ao usar `==`, o `AgendaService` avaliava `dataHora` e `petNome` como distintos mesmo com dados idênticos, permitindo agendamentos duplicados. A substituição por `.equals()` e `.isEqual()` corrigiu essa falha lógica.

### 4. Sobrescrita vs sobrecarga (Aula 7)
A sobrescrita (*override*) ocorre quando uma subclasse redefine um método herdado mantendo rigorosamente a mesma assinatura (mesmo nome, tipos de retorno e lista de parâmetros). Já a sobrecarga (*overload*) ocorre quando um método possui o mesmo nome, porém com parâmetros diferentes, configurando um método totalmente novo. Em `Tosa.java`, o estagiário definiu `public int getDuracaoMinutos(String porte)`. Como o método original em `Atendimento` não recebia argumentos, o Java interpretou como uma simples sobrecarga; assim, ao invocar `atendimento.getDuracaoMinutos()`, a JVM executava o método da superclasse que retornava 30. O uso da anotação `@Override` teria feito o compilador rejeitar o código imediatamente, acusando a inexistência daquele método na superclasse.

### 5. Singleton manual vs bean do Spring (Aula 14)
O padrão Singleton garante a existência de uma única instância de uma classe compartilhada globalmente em toda a aplicação. No `GeradorProtocolo`, o código manual continha um bug crítico: a verificação `if (instancia == null)` criava um `new GeradorProtocolo()` mas esquecia de atribuí-lo à variável de classe, instanciando um novo objeto a cada requisição e quebrando a sequência numérica. Em contrapartida, beans do Spring anotados com `@Service` ou `@Component` possuem o escopo Singleton gerenciado pelo próprio contêiner de Inversão de Controle (*IoC*). O Spring instancia o objeto uma única vez na inicialização e o injeta onde for requisitado, eliminando a necessidade de código manual de verificação, garantindo *thread-safety* e desacoplando a criação de instâncias das classes consumidoras.

### 6. Cobertura de testes: onde parar? (Aula 15)
Manter os testes que já nasceram verdes (como o do preço da consulta e do cancelamento normal) é indispensável. Embora não tenham revelado bugs no momento de sua criação, eles funcionam como uma rede de proteção viva contra regressões: se um desenvolvedor alterar a classe `ConsultaVeterinaria` futuramente, o teste impedirá que a regra seja violada sem aviso. Em um projeto real sob pressão de prazo, a prioridade absoluta deve ser: 1º os testes de caminhos felizes essenciais (fluxo principal gerador de valor); 2º os testes defensivos e caminhos de erro críticos (*Fail Fast*, limites, validações de segurança e concorrência). A busca obstinada por 100% de cobertura muitas vezes gera testes triviais (como testar getters); o foco sênior reside em garantir 100% de cobertura sobre as regras de negócio críticas.

---

## Parte 5 — Espaço livre (opcional)

Durante o processo de revisão e aplicação antecipada das regras de Clean Code (especificamente durante a substituição de números mágicos por constantes nomeadas e a padronização do polimorfismo com anotações `@Override`), **os bugs de cálculo de preço da classe `Banho` e de duração da classe `Tosa` foram visualizados e neutralizados no código-fonte antes da execução dos testes correspondentes da Parte B**. Optamos por manter os registros formais dos bugs e a escrita dos respectivos testes unitários, evidenciando transparência no fluxo de desenvolvimento, respeito ao contrato de testes e comprovação matemática da qualidade final do software.