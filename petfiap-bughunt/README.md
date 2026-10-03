# Checkpoint 5 — Bug Hunt PetFiap

## Identificação

| Integrante         | RM        | Turma   |
|--------------------|-----------|---------|
| Erick Gimenez      | RM 564748 | 2CCPY   |
| Sergio Mirabelo    | RM 562161 | 2CCPY   |
| Henrique Boscoli   | RM 563651 | 2CCPY  |
| João Queiroz       | RM 563578 | 2CCPY  |
| Tomazzo Canterucci | RM 565566 | 2CCPY  |


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
| bug01 | `AgendaServiceTest.deveRecusarAgendamentoComHorarioJaOcupado` vermelho: esperava `HorarioOcupadoException` mas veio `NullPointerException` (`salvo` era null, pois o conflito não foi detectado e o código seguiu para o `save` do mock). | `AgendaService.agendar` (~linha 25): comparava `petNome` e `dataHora` com `==`, que compara referências e não valores. | Troquei por `a.getPetNome().equals(...)` e `a.getDataHora().equals(...)`. | Comparação de objetos: `==` x `equals()` |
| bug02 | `AgendaServiceTest.deveLancarExcecaoQuandoAtendimentoNaoExiste` vermelho: a exceção nunca chegava a quem chamou; o método devolvia `null`. | `AgendaService.buscarPorId` (~linha 40): `catch (Exception e) { return null; }` engolia a `AtendimentoNaoEncontradoException` lançada pelo `orElseThrow`. | Removi o `try/catch`; o `orElseThrow` sozinho lança a exceção. | Tratamento de exceções (Aula 11) |
| bug03 | Não aparecia nos testes entregues. Achei lendo o código: era possível cancelar um atendimento já CONCLUIDO ou já CANCELADO. | `Atendimento.cancelar()` (~linha 60): atribuía `CANCELADO` sem validar o status, diferente do `concluir()`. | Adicionei a validação: só cancela se o status for AGENDADO; senão lança `StatusInvalidoException`. | Encapsulamento das regras de negócio no model e exceções customizadas |
| bug04 | `GeradorProtocoloTest.deveManterUmaUnicaInstancia` e `deveGerarProtocolosSequenciais` vermelhos: cada chamada devolvia um gerador novo, com o contador zerado. | `GeradorProtocolo.getInstancia` (~linha 15): fazia `return new GeradorProtocolo()` sem guardar o objeto no atributo estático `instancia`. | Passei a atribuir `instancia = new GeradorProtocolo()` e a retornar `instancia`. | Padrão Singleton (Aula 14) |
| bug05 | `AtendimentoFactoryTest.deveCriarTosaQuandoTipoForTosa` vermelho: pedir uma TOSA devolvia um objeto `Banho`. | `AtendimentoFactory.criar` (~linha 15): o `case "TOSA"` fazia `new Banho(...)` (erro de copiar e colar). | Troquei por `new Tosa(...)`. | Padrão Factory (Aula 14) e polimorfismo |
| bug06 | `AtendimentoBuilderTest.deveMontarAtendimentoCompleto` vermelho: `expected: <Rex> but was: <null>`. | `AtendimentoBuilder.comPet` (~linha 22): `petNome = petNome;` sem o `this`, atribuía o parâmetro a ele mesmo e o atributo ficava null. | Troquei por `this.petNome = petNome;`. | Escopo de variáveis e palavra-chave `this`; Builder (Aula 14) |
| bug07 | `AtendimentoBuilderTest.deveRecusarMontagemSemNomeDoPet` e `deveRecusarMontagemSemPorte` vermelhos: o Builder construía objetos inválidos. | `AtendimentoBuilder.construir` (~linha 40): não validava nada e deixava a validação "para o controller", contra o contrato (o objeto só nasce válido). | Validei `petNome` e `petPorte` (nulo ou em branco) no início do `construir`, lançando `IllegalArgumentException` com mensagem clara. | Padrão Builder, validação e fail fast |
| bug08 | `AtendimentoFactoryTest.devePreencherOsDadosDoPetNaConsulta` vermelho: `expected: <Mimi> but was: <null>`. | `ConsultaVeterinaria` (construtor com 5 parâmetros, ~linha 14): chamava `super()` sem argumentos e descartava todos os dados (inclusive o status AGENDADO). | Passei a chamar `super(protocolo, petNome, petPorte, tutorNome, dataHora)`. | Herança e construtores (`super`) |
| bug09 | Achei ao escrever o teste01: agendar para uma data no passado não era recusado (vermelho com `NullPointerException`, em vez de `IllegalArgumentException`). | `AgendaService.agendar`: não havia nenhuma validação de data. | Adicionei no início do método: se `dataHora` for anterior a `LocalDateTime.now()`, lança `IllegalArgumentException` antes de consultar o banco. | Validação de regras de negócio e exceções |
| bug10 | Achei ao escrever o teste03: uma Tosa devolvia 30 minutos em vez de 60 (`expected: <60> but was: <30>`), sem nenhum erro de compilação. | `Tosa` (~linha 40): declarava `getDuracaoMinutos(String porte)`, uma sobrecarga, e não a sobrescrita de `getDuracaoMinutos()` da classe pai. | Removi o parâmetro e adicionei `@Override` em `getDuracaoMinutos()`. | Sobrescrita (override) x sobrecarga (overload) e polimorfismo |
| bug11 | Achei ao escrever o teste04: o banho de porte PEQUENO custava R$ 100 e o de porte GRANDE R$ 60 (`expected: <60.0> but was: <100.0>`). O `BanhoTest` entregue passava porque não conferia o preço. | `Banho.calcularPreco` (~linha 25): os valores de PEQUENO e GRANDE estavam trocados em relação ao contrato. | Corrigi para PEQUENO = 60, MEDIO = 80 e GRANDE = 100. | Polimorfismo e regras de negócio no model |
| bug12 | Achei em code review (os testes unitários usam mock e não passam por aqui): a entidade não tem geração automática de id, então o `save` no banco falharia, pois o controller nunca informa um id. | `Atendimento` (~linha 18): o campo `id` tinha `@Id` mas faltava `@GeneratedValue`. | Adicionei `@GeneratedValue(strategy = GenerationType.IDENTITY)`. | Persistência com Spring Data JPA (Aula 13) |

## Parte 2 — Ajustes de Clean Code

| # | Onde estava | Qual princípio/boas práticas era violado | O que eu mudei |
|---|---|---|---|
| clean01 | `AtendimentoFactory.criar(int p, String t, String n, String po, String tu, LocalDateTime d)` | Nomes que revelem a intenção: parâmetros de uma letra não dizem o que são. | Renomeei para `protocolo`, `tipo`, `petNome`, `petPorte`, `tutorNome` e `dataHora`. |
| clean02 | `AgendaService.agendar`: `System.out.println("Recibo: ...")` | Não usar `System.out` para registrar eventos da aplicação; usar um logger, que tem nível e pode ser configurado. | Troquei por `log.info(...)` com SLF4J (já incluso no Spring Boot, sem nova dependência). |
| clean03 | `GeradorProtocolo`: comentário dizia "thread-safe", mas não havia proteção; e havia `System.out.println` no construtor. | Comentário que mente e `println` de depuração esquecido. | Adicionei `synchronized` em `getInstancia()` e `proximo()` (o comentário passou a ser verdadeiro) e removi o `println`. |
| clean04 | `AtendimentoController`: método privado `calcularDescontoFidelidade`, nunca chamado, com comentário de "futuro". | Código morto (YAGNI): confunde o leitor, e a lógica nem batia com o comentário. | Removi o método e o comentário. |
| clean05 | Textos `"AGENDADO"`, `"CONCLUIDO"` e `"CANCELADO"` espalhados em `Atendimento` e `AgendaService`. | Magic strings: um erro de digitação não é detectado pelo compilador. | Criei as constantes `STATUS_AGENDADO`, `STATUS_CONCLUIDO` e `STATUS_CANCELADO` em `Atendimento` e passei a usá-las. |
| clean06 | `@Autowired` direto no campo `repository` (`AgendaService`) e `service` (`AtendimentoController`). | Injeção em campo esconde as dependências e impede campos `final`. | Troquei por injeção pelo construtor, com campos `final`. |

## Parte 3 — Testes novos (regras que estavam sem cobertura)

| # | Teste escrito (classe.método) | Regra coberta | Resultado ao escrever (vermelho/verde) |
|---|---|---|---|
| teste01 | `AgendaServiceRegrasNovasTest.deveRecusarAgendamentoQuandoDataHoraNoPassado` | Agendar com data/hora no passado lança `IllegalArgumentException` e o repository nem é consultado. | Vermelho: revelou o bug09. |
| teste02 | `AtendimentoStatusTest.deveRecusarCancelamentoQuandoAtendimentoJaConcluido` | `cancelar()` em atendimento CONCLUIDO é recusado com `StatusInvalidoException` e o status não muda. | Verde: o bug03 já tinha sido corrigido por leitura do código (sem a correção, teria ficado vermelho). |
| teste03 | `DuracaoAtendimentoTest.deveDurar60MinutosQuandoAtendimentoForTosa` | A tosa dura 60 minutos. | Vermelho: revelou o bug10. |
| teste04 | `PrecoBanhoTest.deveCobrarPrecoDoPorteQuandoBanho` | Preço do banho por porte (PEQUENO R$ 60 e GRANDE R$ 100). | Vermelho: revelou o bug11. |
| teste05 | `PrecoTosaTest.deveCobrarPrecoDoPorteQuandoTosaForMediaOuGrande` | Preço da tosa por porte (MEDIO R$ 90 e GRANDE R$ 120). | Verde: a regra já estava correta. |
| teste06 | `PrecoConsultaTest.deveCobrar150QualquerQueSejaOPorteQuandoConsulta` | A consulta custa R$ 150 fixos, independente do porte. | Verde: a regra já estava correta. |

---

## Parte 4 — Perguntas de reflexão

### 1. A suíte como contrato (Aula 15)
Eu rodei a suíte no IntelliJ e li a mensagem de cada teste vermelho antes de abrir o código. No `deveRecusarAgendamentoComHorarioJaOcupado`, a mensagem dizia `expected: HorarioOcupadoException but was: NullPointerException`, e o `Caused by` apontava para `AgendaService.java:30`, na variável `salvo`. Isso mostrou que o código tinha passado direto pela verificação de conflito e chegado ao `save`, e a causa estava no `if` com `==`. Em outro teste, `expected: <Mimi> but was: <null>` me levou ao construtor da `ConsultaVeterinaria`, que chamava `super()` sem argumentos. A suíte é melhor do que testar com curl porque roda em segundos, sem banco e sem subir a API, pode ser repetida depois de cada correção e avisa na hora quando uma correção quebra outra regra. Ela também documenta o contrato do sistema em forma de código.

### 2. Mock e injeção de dependência (Aulas 13 a 15)
Em produção, o Spring cria o `AtendimentoRepository` (uma implementação do Spring Data JPA ligada ao Oracle) e o injeta no `AgendaService`. No `AgendaServiceTest`, quem faz esse trabalho é o Mockito: o `@Mock` cria um repository falso e o `@InjectMocks` o coloca dentro do service, no lugar do que o Spring faria com o `@Autowired`. O `AgendaService` é uma classe Java comum, que só precisa receber um objeto com os métodos do repository. Por isso o teste roda sem banco e sem subir o Spring: o mock devolve exatamente o que o teste ensinou com `when(...)`, como a lista com o banho já agendado do Rex. Depois que passei a usar injeção pelo construtor (clean06), o Mockito passou a usar o próprio construtor para injetar o mock, e a suíte continuou verde.

### 3. `==` vs `.equals()` (Aula 7)
O `==` compara se duas variáveis apontam para o mesmo objeto na memória, e não se os valores são iguais. No teste, a data do segundo agendamento era criada com `LocalDateTime.parse(...)`, que gera um objeto novo com os mesmos valores. Para o `==`, eram objetos diferentes, então a verificação de conflito dizia que não havia choque de horário. Com literais como `"Rex"`, o `==` "funciona por sorte" porque o Java guarda literais iguais num pool e reaproveita o mesmo objeto, mas isso não vale para Strings que chegam de uma requisição HTTP, como o `petNome` do controller. Minha correção trocou o `==` por `.equals()` no `petNome` e na `dataHora`, que compara o conteúdo, e o conflito passou a ser detectado.

### 4. Sobrescrita vs sobrecarga (Aula 7)
A classe `Atendimento` define `getDuracaoMinutos()` sem parâmetros e devolve 30. A `Tosa` declarava `getDuracaoMinutos(String porte)` devolvendo 60. Como a assinatura era diferente, isso era uma sobrecarga (overload): um segundo método convivendo com o primeiro, e não uma sobrescrita (override). Quando o código chamava `getDuracaoMinutos()` num `Atendimento`, o Java usava o método da classe pai, e a tosa dava 30 minutos, sem nenhum erro de compilação. Se a `Tosa` tivesse a anotação `@Override`, o compilador acusaria na hora que o método não sobrescreve nada da superclasse. O `Banho` já fazia certo, com `@Override` e a mesma assinatura. Corrigi a `Tosa` removendo o parâmetro e adicionando `@Override`.

### 5. Singleton manual vs bean do Spring (Aula 14)
O Singleton garante que existe uma única instância de `GeradorProtocolo` e um ponto global de acesso, para que o contador dos protocolos seja único e sequencial. O bug era que o `getInstancia()` criava um `new GeradorProtocolo()` mas nunca guardava o objeto no atributo estático `instancia`, que ficava sempre `null`. Cada chamada devolvia um gerador novo com o contador zerado, e todo protocolo saía como 1. Depois também encontrei um comentário dizendo "thread-safe" sem nenhuma proteção, e adicionei `synchronized`. O `AgendaService` anotado com `@Service` não corre esse risco porque o Spring cria o bean uma vez (escopo singleton é o padrão) e injeta a mesma instância onde ela é pedida, então não depende de código estático escrito à mão. Além disso, ele só guarda o repository e não tem estado mutável próprio.

### 6. Cobertura de testes: onde parar? (Aula 15)
Vale manter os que ficaram verdes. Eles protegem regras do contrato, como o preço da tosa por porte e o preço fixo da consulta, contra regressões futuras, e custam pouco para rodar. Os testes que a suíte entregou deixavam passar bugs de preço e duração, porque o `BanhoTest` só conferia pontos e duração, e o preço do banho estava invertido sem que nenhum teste reclamasse. Em um projeto real com prazo, eu priorizaria o caminho feliz das funções principais (agendar, concluir, cancelar) e logo depois os caminhos de erro das regras de negócio que envolvem dinheiro, status e validação, como data no passado e cancelamento de atendimento já concluído, porque é onde os bugs mais custam. Perseguir 100% de cobertura não garante qualidade, pois um teste pode executar uma linha sem conferir nada, e o tempo costuma ser mais bem gasto nas regras de maior risco.

---

## Parte 5 — Espaço livre (opcional)

O bug12 foi encontrado lendo o código, pois os testes unitários usam mock e não passam pelo banco.
