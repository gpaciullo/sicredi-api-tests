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
mvn clean test

## Configuração
Arquivo padrão: `src/test/resources/config.properties`

Exemplo:
```properties
baseUrl=https://dummyjson.com
apiBasePath=

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