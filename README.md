# Sicredi API Tests (QE Challenge)

Projeto de automação de testes de API utilizando **Java 17**, **Maven**, **TestNG** e **RestAssured**.

## Stack
- Java 17
- Maven
- TestNG
- RestAssured
- Jackson (serialização JSON)
- Allure (relatório)
- Logback/SLF4J (logs)

## Arquitetura (camadas)
- `config/`  
  Centraliza configuração e setup (baseUrl, basePath, RequestSpecification, filters).
- `client/`  
  Clients responsáveis por chamar endpoints (sem asserts).
- `model/`  
  Modelos/POJOs para payloads (tipado, fácil de evoluir).
- `tests/`  
  Casos de teste (asserts e validações).

## Pré-requisitos
- Java 17 instalado e configurado (`JAVA_HOME`)
- Maven instalado e disponível no PATH


## Executando os testes

### Rodar todos os testes
mvn clean test

### Rodar com credenciais (Auth)
mvn clean test -Dusername=emilys -Dpassword=emilyspass

### Gerar relatório Allure
mvn clean test
allure serve target/allure-results

## Configuração
Arquivo padrão: `src/main/resources/config.properties`

Exemplo:
```properties
baseUrl=https://dummyjson.com
apiBasePath=
timeoutMs=15000

## Comportamento identificado na API

Durante a execução dos testes foi identificado que o endpoint:

POST /products/add

aceita alguns dados considerados inválidos, como por exemplo:

- título vazio
- título nulo
- preço negativo

Mesmo nesses cenários, a API retorna **201 Created** e cria o produto normalmente.

Por esse motivo, os testes automatizados foram ajustados para **documentar o comportamento atual da API**, ao invés de esperar erros de validação.

Esse comportamento pode indicar uma possível oportunidade de melhoria na validação de dados da API.


## Cobertura de testes
- Produtos: listagem com paginação, busca por id, criação
- Auth: login e acesso a endpoint protegido, cenário sem token
- Smoke: endpoint /test


## CI/CD
O projeto possui pipeline no GitHub Actions para execução automática dos testes a cada push na branch main.