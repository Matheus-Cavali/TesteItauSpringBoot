# TesteItauSpringBoot

Testando meus conhecimentos com o desafio de programação do Itaú Unibanco com Spring Boot.

API REST que recebe transações financeiras e retorna estatísticas (contagem, soma, média, mínimo e máximo) calculadas em cima das transações ocorridas nos últimos 60 segundos.

## Tecnologias utilizadas

- Java 17+
- Spring Boot
- Spring Web
- Spring Boot Starter Validation (Bean Validation)
- Maven

## Como rodar o projeto

### Pré-requisitos
- JDK 17 ou superior
- Maven (ou usar o `mvnw` incluso no projeto)

### Passos

1. Clone o repositório:
```bash
git clone https://github.com/Matheus-Cavali/TesteItauSpringBoot.git
```

2. Entre na pasta do projeto:
```bash
cd TesteItauSpringBoot
```

3. Rode a aplicação:
```bash
./mvnw spring-boot:run
```

Ou importe o projeto na sua IDE (IntelliJ, Eclipse, etc.) e rode a classe `ItauTesteApplication`.

A aplicação sobe por padrão em `http://localhost:8080`.

## Endpoints disponíveis

| Método | Path | Descrição |
|--------|------|-----------|
| POST | `/transacao` | Registra uma nova transação |
| DELETE | `/transacao` | Remove todas as transações armazenadas |
| GET | `/estatistica` | Retorna estatísticas das transações dos últimos 60 segundos |

### POST /transacao

Recebe uma transação e a valida antes de armazenar.

**Corpo da requisição:**
```json
{
    "valor": 123.45,
    "dataHora": "2026-07-26T14:30:00.000-03:00"
}
```

**Regras de validação:**
- `valor` e `dataHora` são obrigatórios
- `valor` não pode ser negativo (zero é aceito)
- `dataHora` não pode estar no futuro

**Respostas possíveis:**
- `201 Created` — transação aceita e registrada
- `422 Unprocessable Entity` — transação rejeitada por violar alguma regra de negócio (valor negativo ou data futura)
- `400 Bad Request` — requisição malformada (campo ausente ou tipo inválido)

**Exemplo:**
```bash
curl -i -X POST http://localhost:8080/transacao \
  -H "Content-Type: application/json" \
  -d "{\"valor\": 123.45, \"dataHora\": \"2026-07-26T14:30:00.000-03:00\"}"
```

### DELETE /transacao

Remove todas as transações armazenadas em memória.

**Resposta:**
- `200 OK` — sem corpo

**Exemplo:**
```bash
curl -i -X DELETE http://localhost:8080/transacao
```

### GET /estatistica

Retorna as estatísticas das transações ocorridas nos últimos 60 segundos.

**Resposta:**
```json
{
    "count": 10,
    "sum": 1234.56,
    "avg": 123.456,
    "min": 12.34,
    "max": 123.56
}
```

Se não houver transações nos últimos 60 segundos, todos os valores retornam zerados.

**Exemplo:**
```bash
curl -i http://localhost:8080/estatistica
```

## Estrutura do projeto

```
src/main/java/org/example/itauteste/
├── Control/
│   ├── TransacaoControl.java     -> endpoints POST e DELETE /transacao
│   └── EstatisticaControl.java   -> endpoint GET /estatistica
├── Service/
│   └── TransacaoService.java     -> regras de negócio e armazenamento em memória
├── Model/
│   ├── Transacao.java            -> representação de uma transação
│   └── Estatistica.java          -> representação da resposta de estatísticas
├── DTO/
└── ItauTesteApplication.java
```

A separação em camadas segue o padrão Controller → Service → Model:
- **Control**: responsável apenas por traduzir requisições HTTP em chamadas ao Service e montar o `ResponseEntity` com o status code correto.
- **Service**: concentra toda a regra de negócio (validação de transações, armazenamento em memória, cálculo de estatísticas).
- **Model**: classes que representam os dados manipulados pela aplicação.

## Decisões técnicas

**Armazenamento em memória com `CopyOnWriteArrayList`**
O desafio exige que os dados fiquem em memória, sem uso de banco de dados. Como o Spring Boot atende requisições em múltiplas threads simultaneamente (via pool de threads do Tomcat embutido), uma `ArrayList` comum não seria segura para esse cenário — duas requisições concorrentes (por exemplo, um POST e um GET ao mesmo tempo) poderiam causar inconsistência ou `ConcurrentModificationException`. Optei por `CopyOnWriteArrayList`, que é adequada para cenários com leitura frequente (cálculo de estatísticas) e escrita esporádica (novas transações), garantindo thread-safety sem necessidade de sincronização manual.

**Validação em duas camadas**
- Bean Validation (`@NotNull`) cuida da validação estrutural do JSON recebido (campos obrigatórios ausentes), retornando `400 Bad Request` automaticamente quando falha.
- A validação de regra de negócio (valor negativo, data no futuro) é feita manualmente no `TransacaoService`, permitindo retornar `422 Unprocessable Entity` de forma explícita, conforme exigido pelo desafio.

**Cálculo de estatísticas**
Utiliza `DoubleSummaryStatistics` para calcular soma, média, mínimo e máximo das transações filtradas dentro da janela de 60 segundos. Foi necessário tratar separadamente o caso de lista vazia, já que `DoubleSummaryStatistics` retorna `Infinity`/`-Infinity` para mínimo e máximo quando não há elementos — comportamento diferente do exigido pelo desafio, que pede todos os valores zerados nesse caso.

**Separação Controller/Service**
Os controllers não acessam diretamente a estrutura de dados em memória nem executam lógica de validação — toda essa responsabilidade fica no Service, mantendo os controllers focados apenas em tradução HTTP (request → chamada de negócio → response).

## Autor

Matheus Cavali
