# UniqueDataRestFullClient Framework 

> **by UniqueData Innovation company from Brazil**

---

> **Simplifique suas requisições HTTP com autenticação automática e interceptores poderosos.**

O **UniqueDataRestFullClient** é um framework inspirado no Feign, criado para agilizar o desenvolvimento de integrações HTTP em projetos Java. Ele oferece autenticação automática via tokens Bearer, interceptores configuráveis e opções avançadas de manipulação de requisição. Além disso, é totalmente orientado a objetos, permitindo que parâmetros e form-data sejam passados como objetos, sem necessidade de mapear manualmente cada campo.

---

## Sumário

1. [Principais Recursos](#principais-recursos)
2. [Instalação](#instalação)
   - [Dependência Maven](#dependência-maven)
   - [Requisitos de Sistema](#requisitos-de-sistema)
3. [Uso com Spring Boot](#uso-com-spring-boot)
   - [Exemplo de Configuração](#exemplo-de-configuração)
4. [Uso Sem Spring Boot](#uso-sem-spring-boot)
   - [Exemplo de Uso](#exemplo-de-uso)
5. [Autenticação Automática](#autenticação-automática)
   - [Bearer Token](#bearer-token)
   - [Bearer com Form-data](#bearer-com-form-data)
   - [Sem Autenticação](#sem-autenticação)
6. [Interceptors](#interceptors)
   - [Exemplo Avançado](#exemplo-avançado)
7. [Requisições HTTP](#requisições-http)
   - [Exemplo de Get/Post/Delete](#exemplo-de-getpostdelete)
8. [Passando Objetos como Parâmetros](#passando-objetos-como-parâmetros)
9. [Personalização de Credenciais](#personalização-de-credenciais)
10. [Contribuições](#contribuições)
11. [Licença](#licença)

---

## Principais Recursos

- **Autenticação Automática**: Utilize anotações para configurar tokens Bearer e gerenciar expiração.
- **Interceptors Avançados**: Injete cabeçalhos adicionais e gerencie o envio de tokens sem duplicação de código.
- **Auto Recover**: Opção de armazenamento em disco do token, permitindo recuperação de sessão mesmo após reinício da aplicação.
- **Multiplicidade de Formatos**: Suporte para JSON, form-data, x-www-form-urlencoded e mais.
- **Anotações Simples e Poderosas**: Defina rapidamente endpoints com `@UniqueDataRestFullGet`, `@UniqueDataRestFullPost`, etc.
- **Orientado a Objetos**: Os parâmetros das requisições podem ser passados como objetos, simplificando ainda mais a integração.

---

## Instalação

### Dependência Maven

```xml
<dependency>
  <groupId>br.com.uniquedata-restfull-sdk</groupId>
  <artifactId>uniquedata-restfull-sdk</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### Requisitos de Sistema

- Java 17 ou superior.

---

## Uso com Spring Boot

### Exemplo de Configuração

Se o seu projeto utiliza Spring Boot, basta fazer o *scan* do framework em sua classe principal, para que as interfaces anotadas sejam detectadas:

```java
@SpringBootApplication
public class Application {
    public static void main(final String[] args) {
        // Scaneia as classes anotadas e gera as implementações automaticamente
        UniqueDataRestFull.scan(Application.class);

        // Inicia a aplicação Spring Boot
        SpringApplication.run(Application.class, args);
    }
}
```

Depois, você pode injetar a interface diretamente via `@Autowired` em um componente Spring:

```java
@Component
public class MyComponent implements CommandLineRunner {

    @Autowired
    private ExampleApi exampleApi;

    @Override
    public void run(String... args) {
        // Agora você pode chamar diretamente os métodos do seu client
        TestResponseBodyDto response = exampleApi.save(new TestRequestBodyDto("Olá", 123));
        System.out.println(response);
    }
}
```

---

## Uso Sem Spring Boot

### Exemplo de Uso

Caso não utilize Spring Boot, simplesmente recupere a instância do client chamando:

```java
public class Main {
    public static void main(String[] args) {
        // Retorna uma implementação da interface ExampleApi
        ExampleApi exampleApi = UniqueDataRestFull.getApi(ExampleApi.class);

        // Uso direto
        TestResponseBodyDto response = exampleApi.save(new TestRequestBodyDto("Olá", 123));
        System.out.println(response);
    }
}
```

---

## Autenticação Automática

### Bearer Token

A anotação `@AutoAuthentication` permite configurar como o token será obtido e reutilizado. Você pode controlar:

- **autoRecover**: Se `true`, o token é armazenado em disco.
- **type**: Tipo de autenticação (ex.: `AuthType.BEARER_TOKEN`).
- **authenticate**: Detalhes de como o token é obtido (URL, classe de credencial, etc.).
- **interception**: Configurações do interceptor para injetar cabeçalhos e gerenciar expirações.

Exemplo completo:

```java
@AutoAuthentication(
    // Se ativado, o token será persistido em disco e recuperado automaticamente
    autoRecover = true,
    // Tipo de autenticação, no caso Bearer
    type = AuthType.BEARER_TOKEN,
    // Configuração da autenticação
    authenticate = @Authentication(
        // Habilita ou não a autenticação
        enabled = true,
        // Endereço completo para obter o token
        fullUrlAuth = "https://localhost:9095/autorizy",
        // Variável de ambiente que contém as credenciais em formato JSON
        credentialJsonEnvironmentVariable = "MY_CREDENTIAL_ENV",
        // Classe onde as credenciais serão mapeadas
        typeClassCredential = CredencialDto.class,
        // Cabeçalhos adicionais para a requisição de autenticação
        additionalHeaders = {
            @AdditionalHeader(headerName = "teste", headerValue = "auth")
        }
    ),
    // Configura o interceptor
    interception = @Interception(
        // Ativa o interceptor para injetar automaticamente o Bearer token nas requisições
        enabled = true,
        // Tempo de expiração do token (em ms). Se "@ExpireDate" não estiver na classe de credencial
        expireInMilliseconds = 60_000
    )
)
public interface AutoAuthencatedTestApi {
    // Seus métodos de API protegidos por autenticação...
}
```

### Bearer com Form-data

```java
@AutoAuthentication(
    autoRecover = false,
    type = AuthType.BEARER_TOKEN,
    authenticate = @Authentication(
        enabled = true,
        fullUrlAuth = "https://api.testing.com/oauth/token",
        credentialJsonEnvironmentVariable = "MY_CREDENTIAL_ENV",
        typeClassCredential = CredencialDto.class,
        typeClassAuthorize = ReponseAuthorizeDto.class,
        additionalHeaders = {
            @AdditionalHeader(headerName = "Content-Type", headerValue = "application/x-www-form-urlencoded"),
            @AdditionalHeader(headerName = "User-Agent", headerValue = "Mozilla/5.0 Chrome/127.0.0.0 Safari/537.36")
        }
    ),
    interception = @Interception(
        enabled = true,
        expireInMilliseconds = 60_000,
        additionalHeaders = {
            @AdditionalHeader(headerName = "User-Agent", headerValue = "Mozilla/5.0 Chrome/127.0.0.0 Safari/537.36")
        }
    )
)
public interface AutoAuthFormDataApi {}
```

### Sem Autenticação

```java
@AutoAuthentication(
    autoRecover = true,
    type = AuthType.BEARER_TOKEN,
    interception = @Interception(
        enabled = true,
        expireInMilliseconds = 60_000,
        additionalHeaders = {
            @AdditionalHeader(headerName = "User-Agent", headerValue = "Mozilla/5.0 Chrome/127.0.0.0 Safari/537.36"),
            @AdditionalHeader(headerName = "Authorization", headerValue = "Bearer SEU_TOKEN_FIXO_AQUI")
        }
    )
)
public interface AutoAuthFixedTokenApi {}
```

---

## Interceptors

### Exemplo Avançado

Os interceptors (configurados via `@Interception`) atuam de forma automática injetando cabeçalhos e gerenciando a expiração do token. Algumas vantagens:

- **Adicionar cabeçalhos customizados**: `@AdditionalHeader` permite adicionar qualquer informação no header de cada requisição.
- **Gerenciar expiração**: Se sua credencial não tiver a anotação `@ExpireDate`, use `expireInMilliseconds` para forçar a renovação após determinado período.
- **Desativar facilmente**: Defina `enabled = false` para não usar o interceptor.

```java
@Interception(
    enabled = true,
    expireInMilliseconds = 120_000,
    additionalHeaders = {
        @AdditionalHeader(headerName = "User-Agent", headerValue = "CustomAgent/1.0"),
        @AdditionalHeader(headerName = "Accept-Language", headerValue = "pt-BR")
    }
)
```

---

## Requisições HTTP

### Exemplo de Get/Post/Delete

Para fazer requisições, basta anotar métodos dentro de uma interface marcada com `@UniqueDataRestFullClient`. Você pode utilizar:

- `@UniqueDataRestFullGet`
- `@UniqueDataRestFullPost`
- `@UniqueDataRestFullPut`
- `@UniqueDataRestFullPatch`
- `@UniqueDataRestFullDelete`
- `@UniqueDataRestFull` (caso precise de métodos customizados ou verbos diferentes)

**Exemplo**:

```java
@UniqueDataRestFullClient(
    baseUrl = "https://backend.api.com", // Base URL da API
    autoAuthEndpointMonitor = AutoAuthFixedTokenApi.class // Monitor de autenticação
)
public interface ExampleApi {

    @UniqueDataRestFullGet(value = "/api/public/v1/products")
    UniqueDataRestFullResponse<List<TestResponseBodyDto>> getProductsGamivo(
        @RestFullParam("offset") int offset,
        @RestFullParam("limit") int limit
    );

    // Exemplo GET com Path Variable
    @UniqueDataRestFullGet(value = "/v3/jobs/{uid}")
    TesteDataDto checkJobId2(@RestFullPathVar("uid") String uid);

    // Exemplo POST com Body
    @UniqueDataRestFullPost("/")
    TestResponseBodyDto save(@RestFullBody TestRequestBodyDto requestBody);

    // Exemplo DELETE
    @UniqueDataRestFullDelete("/{id}")
    TestResponseBodyDto deleteById(@RestFullPathVar("id") String id);
}
```

---

## Passando Objetos como Parâmetros

O framework também permite mapear parâmetros de URL, form-data ou x-www-form-urlencoded a partir de **objetos**. Por exemplo, se você tiver um DTO assim:

```java
public class Teste {
    private String nome;
    private Integer idade;

    // getters e setters
}
```

Basta anotar o método com `@RestFullParamToObject` ou `@RestFullFormDataToObject` (dependendo do seu caso de uso), e o framework montará a requisição. Exemplo:

```java
@UniqueDataRestFullGet("/filters")
TestResponseBodyDto filtersByObject(@RestFullParamToObject Teste filtros);
```

Isto resultará em uma URL como `...?nome=valorDoNome&idade=valorDaIdade`. Já em casos de form-data, bastaria utilizar:

```java
@UniqueDataRestFull(
    method = RestFullMethod.POST,
    endpoint = "/save",
    accept = RestFullMediaType.APPLICATION_X_WWW_FORM_URLENCODED
)
TestResponseBodyDto saveByUrlEncoded(@RestFullFormDataToObject Teste requestBody);
```

---

## Personalização de Credenciais

- **@Bearer**: Para o campo que representa o token na classe de credencial (por exemplo, `private String token;`).
- **@ExpireDate**: Para o campo que representa a data/hora de expiração do token (por exemplo, `private Instant expireTime;`). Se este campo não for anotado, utilize `expireInMilliseconds` no `@Interception` para forçar nova autenticação.
- **autoRecover**: Se `true`, o token será salvo em disco. Caso a aplicação seja reiniciada, o framework lerá a última credencial salva.

Exemplo de classe de credencial:

```java
public class CredencialDto {
    @Bearer
    private String accessToken;

    @ExpireDate
    private LocalDateTime expiresAt;

    // Getters e Setters
}
```

---

## Licença

Este projeto está licenciado sob a Licença MIT. Consulte o arquivo [LICENSE](LICENSE) para detalhes.

