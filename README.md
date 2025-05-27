
# Projeto Spring Boot com Testes Automatizados

Este repositório contém uma aplicação Java Spring Boot desenvolvida com foco em testes automatizados, validação, segurança, cobertura de código e testes de API. Também foram utilizados conceitos avançados como autenticação via OAuth2, JWT, mensageria com Apache Kafka e otimização de consultas JPA. Fiz esse projeto no decorrer do curso da DevSuperior, focado no desenvolvimento de práticas como TDD. Outros projetos menos também foram feitos como forma de fixação do conteúdo, esses são:

 - [Primeiro desafio do curso usando TDD](https://github.com/lucasasmuniz/TDD-event-city-challenge)
 - [Estudo da dependência Validation](https://github.com/lucasasmuniz/desafio-validacao-seguranca/tree/main)
 - [Sistema de cinema](https://github.com/lucasasmuniz/desafio-sistema-de-filmes) -> Aqui eu desenvolvi o sistema de filmes e fiz testes unitários e de integração com MockMvc, também ajustei o problema de n+1 consultas do Hibernate.
 - [Estudo de cobertura de testes usando JaCoCo e JUnit](https://github.com/lucasasmuniz/sistema-filmes-notas-testes)
 - [Rest Assured em sistema de cinema](https://github.com/lucasasmuniz/sistema-filmes-rest-assured)
 - [Primeiros estudos de Rest Assured](https://github.com/lucasasmuniz/estudos-rest-assured)

---

## Principais dependências estudadas no curso

- Spring Boot
- Spring Security (OAuth2)
- Spring Data JPA
- JaCoCo (Plugin)
- Mockito / JUnit
- RestAssured
- Bean Validation
- Java Mail Sender
- PostgreSQL
- H2 Database

### Dependências adicionadas após o curso

 Após terminar o curso, adicionei algumas dependências extras para enriquecer o projeto:

 - `Kafka` – Para o envio de e-mail para recuperação de senha
 - `OpenAPI` – Para documentação interativa da API
 - `Spring HATEOAS` – Para enriquecer as respostas da API com links e navegação.
 - `Spring Cache com Redis`

## Estrutura do Curso e Anotações

### Módulo 1 – CRUD e Estruturação do Projeto
- Implementação padrão de um CRUD com boas práticas de estruturação.
- Uso de camadas service, controller, DTO, entity e repository.

---

### Módulo 2 – Testes Automatizados

#### Padrão de Nome de Testes
- `<AÇÃO> should <EFEITO> [when <CENÁRIO>]`

#### AAA (Arrange, Act, Assert)
1. Arrange – preparar os dados e dependências.
2. Act – executar a lógica.
3. Assert – verificar o resultado.

#### Boas Práticas
- Testes simples, lineares e sem condicionais ou laços.
- O resultado do teste deve ser determinístico.

#### Tipos de Testes e Annotations

| Tipo                  | Annotations principais                       | Detalhes                                                  |
|-----------------------|----------------------------------------------|-----------------------------------------------------------|
| Teste de Unidade      | `@ExtendWith`, `@Mock`, `@InjectMocks`      | Não carrega contexto Spring. Usa Mockito.                 |
| Teste de Integração   | `@SpringBootTest`, `@Transactional`         | Carrega contexto e faz rollback automático.               |
| Teste Web Unitário    | `@WebMvcTest`                               | Carrega só a camada web.                                  |
| Teste Repository      | `@DataJpaTest`                              | Focado em repositórios, rollback automático.              |

#### Mockito

- `@Mock` – cria um objeto falso.
- `@Spy` – espiona um objeto real.
- `@InjectMocks` – injeta os mocks automaticamente.
- `when(...).thenReturn(...)` – para métodos que retornam valores.
- `doNothing().when(...)` ou `doThrow().when(...)` – para métodos void.

---

### Módulo 3 – Validação e Segurança

#### Validação
- Anotações básicas do **Bean Validation** (e.g., `@NotNull`, `@Size`).
- Tratativa da `MethodArgumentNotValidException`.
- Resposta customizada para erros de validação.
- Implementação de `ConstraintValidator` customizado.

#### Segurança
- Implementação de entidades `User` e `Role`.
- Seed de dados para perfis.
- Spring Security com **OAuth2** e **JWT**.
- Autorização por perfil com `@PreAuthorize`.
- Uso de `clientId` e `clientSecret` via variáveis de ambiente.
- Uso de **grant type** `password`, mas destacando que há opções melhores.

---

### Módulo 4 – Consultas JPA e Performance

#### Boas Práticas

Evitar problema N+1 usando JOIN FETCH:
 - **Paginação com JOIN FETCH:** necessário usar `countQuery`.
 - **@Query personalizada:** escrita com JPQL.
 - Consultas `@ManyToOne`: passar ID da entidade relacionada.
 - Consultas `@ManyToMany`: criar seed com IDs das duas entidades.
Exemplo com `@Query`
```java
@Query("SELECT obj FROM Product obj JOIN FETCH obj.categories")
List<Product> findAllWithCategories();
```
Paginação:
Quando usar `JOIN FETCH` com paginação, é necessário definir countQuery:
```java
@Query(value = "SELECT obj FROM Product obj JOIN FETCH obj.categories",
       countQuery = "SELECT COUNT(obj) FROM Product obj")
Page<Product> findPagedWithCategories(Pageable pageable);
```

Lista como Parâmetro:
- **Consultas SQL com listas:** usar cláusula `where (:lista IS NULL OR field IN :lista)`.

Outros:

- **Parâmetros opcionais** `@RequestParam(value = "var", defaultValue = "valor")`.
- `PageImpl` para criar objetos paginados.

---

### Módulo 5 – Cobertura de Testes com JaCoCo

#### Conceitos
- **Cobertura de código:** % de código-fonte executado.
- **Cobertura de testes:** % de requisitos/testes cobertos.


#### 📊 Tipos de Cobertura
| Tipo     | Descrição                                                                |
|----------|--------------------------------------------------------------------|
| Statement Coverage     | % de linhas executadas                                                |
| Branch Coverage      | % de caminhos de decisão executados                                                   |
| Function Coverage     | % de métodos executados ao menos uma vez                                        |

> A cobertura de código é uma métrica de apoio, mas não garante qualidade. Cobertura de testes (testar os requisitos e regras do sistema) é o que importa.

#### Observações
- Cobrir 100% de linhas não garante ausência de erros.
- `Getters`/`setters` e `equals`/`hashCode` nem sempre precisam ser testados.
- **JaCoCo** é a principal ferramenta gratuita para cobertura em Java.

#### 🧪 Tipos de Testes

| Tipo             | Acesso ao código | Exemplos                             |
|------------------|------------------|-------------------------------------|
| Caixa Branca     | Sim              | Unitários (Service, Repository)                    |
| Caixa Preta      | Não              | Integração, API(Controller)          |

---

### Módulo 6 – Testes de API com MockMvc e RestAssured

#### MockMvc
- Usa `@AutoConfigureMockMvc` e `@SpringBootTest`.
- Simula requisições sem subir o servidor.
- Permite `.andExpect(status().isOk())`, `.jsonPath(...)`, etc.
- Exemplo de requisição com `accept` e `contentType`:

```java
mockMvc.perform(get("/api")
    .accept(MediaType.APPLICATION_JSON))
    .andExpect(status().isOk());
    .andExpect(jsonPath("$.name").value("Produto"))
    .andDo(MockMvcResultHandlers.print()); // isso serve pra printar os trace
```

#### ⚠️ Atenção: endpoints OAuth2 exigem contentType e parâmetros especiais:

```java
MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
params.add("grant_type", "password");
params.add("client_id", clientId);
params.add("username", username);
params.add("password", password);

ResultActions result = mockMvc.perform(post("/oauth2/token")
    .params(params)
    .with(httpBasic(clientId, clientSecret))
    .accept("application/json;charset=UTF-8"))
    .andExpect(status().isOk());
```

#### RestAssured
- Testes diretos na API sem contexto do Spring.

```java
@BeforeEach
public void setUp() {
    baseURI = "http://localhost:8080";
}

given()
  .get("/products")
.then()
  .statusCode(200)
  .body("content.id[1]", is(6));
```

- Filtragem no body:
```java
.body("content.findAll { it.price > 2000 }.name", hasItems("PC Gamer Boo", "PC Gamer Foo"))
```

- Autenticação e obtenção de token:
```java
public static Response getAccessToken(String username, String password) {
    return given()
        .auth()
        .preemptive()
        .basic("myclientid", "myclientsecret")
        .contentType("application/x-www-form-urlencoded")
        .formParam("grant_type", "password")
        .formParam("username", username)
        .formParam("password", password")
    .when()
        .post("/oauth2/token");
}
```

- Suporte para autenticação OAuth2 com `auth().preemptive().basic(...)`.
- Permite testes com `.body(..., hasItems(...))`, filtros com `findAll {}` etc.

---

## 🎯 Commits Convencionais

| Tipo     | Uso                                                                |
|----------|--------------------------------------------------------------------|
| feat     | Nova funcionalidade                                                |
| fix      | Correção de bugs                                                   |
| docs     | Atualização de documentação                                        |
| style    | Formatação, sem alterações de comportamento                        |
| refactor | Refatorações sem alterar comportamento                            |
| chore    | Tarefas de manutenção (ex: atualização de dependências)           |
| test     | Adição ou modificação de testes                                   |

---

## 📌 Observações Finais

- Utilização de testes automatizados com foco em confiabilidade.
- Testes unitários para lógica de negócio e integração para validação ponta a ponta.
- Estratégias de melhoria de performance com consultas otimizadas JPA.
- Padrões de commits adotados ao longo do desenvolvimento.

---
