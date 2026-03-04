# Sicredi API Tests (QE Challenge)

Projeto de automação de testes de API desenvolvido utilizando **Java 17**, **Maven**, **TestNG** e **RestAssured**, seguindo boas práticas de **Clean Code**, **arquitetura em camadas** e **reutilização de componentes**.

O objetivo deste projeto é validar e documentar o comportamento da API pública **DummyJSON**, aplicando princípios utilizados em frameworks profissionais de automação de testes.

---

# Tecnologias Utilizadas

- Java 17
- Maven
- TestNG
- RestAssured
- Jackson (serialização/deserialização JSON)
- Allure Report (relatórios de execução)
- SLF4J / Logback (logs)

---

# Arquitetura do Projeto

O projeto foi estruturado em camadas para garantir **separação de responsabilidades**, **manutenibilidade** e **escalabilidade**.

src
├── client
│ Classes responsáveis por chamadas HTTP para a API
│
├── config
│ Configuração global dos testes
│ BaseTest
│ TestConfig
│
├── model
│ Modelos (POJOs) usados como payloads das requisições
│
├── assertions
│ Camada responsável por centralizar validações reutilizáveis
│
├── utils
│ Utilidades auxiliares
│ RandomUtils
│ ProductFactory
│ TestDataProvider
│
└── tests
Casos de teste automatizados


---

# Padrões Aplicados

O projeto utiliza alguns padrões comuns em frameworks de automação:

## Client Layer
Classes responsáveis apenas por comunicação com a API.

Exemplo:
ProductsClient
AuthClient

Essas classes **não possuem asserts**, apenas executam requisições.

---

## Test Layer

As classes de teste realizam:

- validação de respostas
- verificação de contrato mínimo da API
- documentação de comportamentos inesperados

---

## Assertion Layer

Centraliza validações reutilizáveis.

Exemplo:
ApiAssertions.shouldBeOk(response)
ApiAssertions.shouldBeCreated(response)
ApiAssertions.shouldBeUnauthorized(response)


Isso reduz duplicação e melhora a legibilidade dos testes.

---

# Estrutura de Testes

Os testes estão organizados por domínio da API.

## ProductsTests

Valida funcionalidades relacionadas aos produtos:

- listagem com paginação
- busca por ID
- criação de produtos
- cenários de dados inválidos
- contrato mínimo da API

---

## AuthProductsTests

Valida autenticação e acesso a endpoints protegidos:

- login com credenciais válidas
- login com credenciais inválidas
- acesso com token válido
- acesso com token inválido
- acesso sem Authorization header

---

# Pré-requisitos

Antes de executar o projeto é necessário possuir:

- Java 17
- Maven
- Variável `JAVA_HOME` configurada

Verificação rápida:
java -version
mvn -version

---

# Executando os Testes

## Executar todos os testes
mvn clean test

---

## Executar testes com autenticação
mvn clean test -Dusername=emilys -Dpassword=emilyspass

Caso os parâmetros não sejam informados, os testes utilizam valores padrão.

---

# Relatório de Execução

O projeto utiliza **Allure Report** para visualização dos resultados.

### Executar testes
mvn clean test


### Gerar relatório
allure serve target/allure-results

---

# Configuração

Arquivo de configuração:
src/main/resources/config.properties


Exemplo:
baseUrl=https://dummyjson.com

apiBasePath=
timeoutMs=15000

Essas configurações podem ser sobrescritas via parâmetros do Maven.

---

# Comportamentos Identificados na API

Durante a execução dos testes foram identificados alguns comportamentos relevantes da API DummyJSON.

### Endpoint
POST /products/add

Foi observado que a API aceita alguns dados considerados inválidos:

- título vazio
- título nulo
- preço negativo

Mesmo nesses cenários a API retorna:
201 Created

Criando o produto normalmente.

Por esse motivo, os testes foram ajustados para **documentar o comportamento atual da API**, ao invés de esperar validações que não são implementadas.

Isso pode indicar uma possível **oportunidade de melhoria na validação de dados da API**.

---

# Cobertura de Testes

Atualmente o projeto cobre:

## Produtos

- listagem com paginação
- busca por produto por ID
- criação de produto
- validação de contrato mínimo
- cenários de dados inválidos
- cenários de paginação inválida

## Autenticação

- login válido
- login inválido
- acesso com token válido
- acesso com token inválido
- acesso sem token

## Smoke

- validação de disponibilidade da API

---

# CI/CD

O projeto possui pipeline configurado no **GitHub Actions** para execução automática dos testes a cada:

- push
- pull request

Fluxo do pipeline:
Checkout
Build
Execução dos testes
Geração de relatório

---

# Melhorias Futuras

Possíveis evoluções para o framework:

- integração com Docker
- execução paralela de testes
- geração automática de dados de teste
- validação de schema JSON
- integração com ferramentas de observabilidade

---

# Autor

Gabriel Paciullo Gomes  
Senior QA Engineer / Test Automation Engineer