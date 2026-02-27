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

## Configuração
Arquivo padrão: `src/test/resources/config.properties`

Exemplo:
```properties
baseUrl=https://dummyjson.com
apiBasePath=

## Executando os testes
mvn clean test
