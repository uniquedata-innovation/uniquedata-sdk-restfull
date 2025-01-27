Below is the **English version** of your README with the same structure and code snippets.

---

# UniqueDataRestFullClient Framework

> **by UniqueData Innovation company from Brazil**

---

> **Simplify your HTTP requests with automatic authentication and powerful interceptors.**

**UniqueDataRestFullClient** is a Java framework inspired by Feign, designed to speed up the development of HTTP integrations in Java projects. It provides automatic Bearer token authentication, configurable interceptors, and advanced request handling features. In addition, it is fully object-oriented, allowing you to pass parameters and form-data as objects, without the need to manually map each field.

---

## Table of Contents

1. [Key Features](#key-features)  
2. [Installation](#installation)  
   - [Maven Dependency](#maven-dependency)  
   - [System Requirements](#system-requirements)  
3. [Using with Spring Boot](#using-with-spring-boot)  
   - [Configuration Example](#configuration-example)  
4. [Using Without Spring Boot](#using-without-spring-boot)  
   - [Usage Example](#usage-example)  
5. [Automatic Authentication](#automatic-authentication)  
   - [Bearer Token](#bearer-token)  
   - [Bearer with Form-Data](#bearer-with-form-data)  
   - [No Authentication](#no

Below is the **English version** of your README with the same structure and code snippets.

---

# UniqueDataRestFullClient Framework

> **by UniqueData Innovation company from Brazil**

---

> **Simplify your HTTP requests with automatic authentication and powerful interceptors.**

**UniqueDataRestFullClient** is a Java framework inspired by Feign, designed to speed up the development of HTTP integrations in Java projects. It provides automatic Bearer token authentication, configurable interceptors, and advanced request handling features. In addition, it is fully object-oriented, allowing you to pass parameters and form-data as objects, without the need to manually map each field.

---

## Table of Contents

1. [Key Features](#key-features)  
2. [Installation](#installation)  
   - [Maven Dependency](#maven-dependency)  
   - [System Requirements](#system-requirements)  
3. [Using with Spring Boot](#using-with-spring-boot)  
   - [Configuration Example](#configuration-example)  
4. [Using Without Spring Boot](#using-without-spring-boot)  
   - [Usage Example](#usage-example)  
5. [Automatic Authentication](#automatic-authentication)  
   - [Bearer Token](#bearer-token)  
   - [Bearer with Form-data](#bearer-with-form-data)  
   - [No Authentication](#no-authentication)  
6. [Interceptors](#interceptors)  
   - [Advanced Example](#advanced-example)  
7. [HTTP Requests](#http-requests)  
   - [Example of Get/Post/Delete](#example-of-getpostdelete)  
8. [Passing Objects as Parameters](#passing-objects-as-parameters)  
9. [Credentials Customization](#credentials-customization)  
10. [Contributions](#contributions)  
11. [License](#license)

---

## Key Features

- **Automatic Authentication**: Use annotations to configure Bearer tokens and manage expiration.  
- **Advanced Interceptors**: Inject additional headers and manage token flow without duplicating code.  
- **Auto Recover**: Option to store the token on disk, allowing session recovery even after application restarts.  
- **Multiple Formats**: Supports JSON, form-data, x-www-form-urlencoded, and more.  
- **Simple Yet Powerful Annotations**: Quickly define endpoints with `@UniqueDataRestFullGet`, `@UniqueDataRestFullPost`, etc.  
- **Object-Oriented**: Request parameters can be passed as objects, further simplifying integration.

---

## Installation

### Maven Dependency

```xml
<dependency>
  <groupId>br.com.uniquedata-restfull-sdk</groupId>
  <artifactId>uniquedata-restfull-sdk</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### System Requirements
- Java 17 or higher.

---

## Using with Spring Boot

### Configuration Example

If your project uses Spring Boot, simply **scan** the framework in your main application class so that annotated interfaces will be detected:

```java
@SpringBootApplication
public class Application {
    public static void main(final String[] args) {
        // Scans the annotated classes and generates implementations automatically
        UniqueDataRestFull.scan(Application.class);

        // Starts the Spring Boot application
        SpringApplication.run(Application.class, args);
    }
}
```

Then, you can inject the interface via `@Autowired` in a Spring component:

```java
@Component
public class MyComponent implements CommandLineRunner {

    @Autowired
    private ExampleApi exampleApi;

    @Override
    public void run(String... args) {
        // Now you can directly call the methods of your client
        TestResponseBodyDto response = exampleApi.save(new TestRequestBodyDto("Olá", 123));
        System.out.println(response);
    }
}
```

---

## Using Without Spring Boot

### Usage Example

If you do not use Spring Boot, simply retrieve the client instance by calling:

```java
public class Main {
    public static void main(String[] args) {
        // Returns an implementation of the ExampleApi interface
        ExampleApi exampleApi = UniqueDataRestFull.getApi(ExampleApi.class);

        // Direct usage
        TestResponseBodyDto response = exampleApi.save(new TestRequestBodyDto("Olá", 123));
        System.out.println(response);
    }
}
```

---

## Automatic Authentication

### Bearer Token

The `@AutoAuthentication` annotation lets you configure how the token is obtained and reused. You can control:

- **autoRecover**: If `true`, the token is stored on disk.  
- **type**: Authentication type (e.g., `AuthType.BEARER_TOKEN`).  
- **authenticate**: Details on how to obtain the token (URL, credential class, etc.).  
- **interception**: Configurations for interceptors to inject headers and manage expiration.

Complete example:

```java
@AutoAuthentication(
    // If enabled, the token is persisted on disk and automatically recovered
    autoRecover = true,
    // Authentication type, in this case Bearer
    type = AuthType.BEARER_TOKEN,
    // Authentication configuration
    authenticate = @Authentication(
        // Enable or disable authentication
        enabled = true,
        // Full URL to retrieve the token
        fullUrlAuth = "https://localhost:9095/autorizy",
        // Environment variable containing the credentials in JSON format
        credentialJsonEnvironmentVariable = "MY_CREDENTIAL_ENV",
        // Class where credentials will be mapped
        typeClassCredential = CredencialDto.class,
        // Additional headers for the authentication request
        additionalHeaders = {
            @AdditionalHeader(headerName = "teste", headerValue = "auth")
        }
    ),
    // Interceptor configuration
    interception = @Interception(
        // Enables the interceptor to automatically inject the Bearer token in requests
        enabled = true,
        // Token expiration time in ms. If `@ExpireDate` is not used in the credential class
        expireInMilliseconds = 60_000
    )
)
public interface AutoAuthencatedTestApi {
    // Your protected API methods...
}
```

### Bearer with Form-data

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

### No Authentication

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

### Advanced Example

Interceptors (configured via `@Interception`) automatically inject headers and manage token expiration. Some advantages:

- **Add Custom Headers**: `@AdditionalHeader` lets you add any information to the header of each request.  
- **Manage Expiration**: If your credential class does not have the `@ExpireDate` annotation, use `expireInMilliseconds` to force token renewal after a certain period.  
- **Easily Disable**: Set `enabled = false` if you want to disable the interceptor.

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

## HTTP Requests

### Example of Get/Post/Delete

To make HTTP requests, annotate methods inside an interface marked with `@UniqueDataRestFullClient`. You can use:

- `@UniqueDataRestFullGet`
- `@UniqueDataRestFullPost`
- `@UniqueDataRestFullPut`
- `@UniqueDataRestFullPatch`
- `@UniqueDataRestFullDelete`
- `@UniqueDataRestFull` (for custom methods or different HTTP verbs)

**Example**:

```java
@UniqueDataRestFullClient(
    baseUrl = "https://backend.api.com", // Base URL of the API
    autoAuthEndpointMonitor = AutoAuthFixedTokenApi.class // Authentication monitor
)
public interface ExampleApi {

    @UniqueDataRestFullGet(value = "/api/public/v1/products")
    UniqueDataRestFullResponse<List<TestResponseBodyDto>> getProductsGamivo(
        @RestFullParam("offset") int offset,
        @RestFullParam("limit") int limit
    );

    // Example GET with Path Variable
    @UniqueDataRestFullGet(value = "/v3/jobs/{uid}")
    TesteDataDto checkJobId2(@RestFullPathVar("uid") String uid);

    // Example POST with Body
    @UniqueDataRestFullPost("/")
    TestResponseBodyDto save(@RestFullBody TestRequestBodyDto requestBody);

    // Example DELETE
    @UniqueDataRestFullDelete("/{id}")
    TestResponseBodyDto deleteById(@RestFullPathVar("id") String id);
}
```

---

## Passing Objects as Parameters

The framework also allows you to map URL parameters, form-data, or x-www-form-urlencoded from **objects**. For example, if you have a DTO like this:

```java
public class Teste {
    private String nome;
    private Integer idade;

    // getters and setters
}
```

Simply annotate the method with `@RestFullParamToObject` or `@RestFullFormDataToObject` (depending on your use case), and the framework will assemble the request. For example:

```java
@UniqueDataRestFullGet("/filters")
TestResponseBodyDto filtersByObject(@RestFullParamToObject Teste filtros);
```

This will result in a URL like `...?nome=nomeValue&idade=idadeValue`. For form-data cases, just use:

```java
@UniqueDataRestFull(
    method = RestFullMethod.POST,
    endpoint = "/save",
    accept = RestFullMediaType.APPLICATION_X_WWW_FORM_URLENCODED
)
TestResponseBodyDto saveByUrlEncoded(@RestFullFormDataToObject Teste requestBody);
```

---

## Credentials Customization

- **@Bearer**: For the field that represents the token in the credential class (e.g., `private String token;`).  
- **@ExpireDate**: For the field that represents the token’s expiration date/time (e.g., `private Instant expireTime;`). If this field is not annotated, use `expireInMilliseconds` in `@Interception` to force re-authentication.  
- **autoRecover**: If `true`, the token is saved to disk. If the application restarts, the framework will read the last saved credential.

Example of a credential class:

```java
public class CredencialDto {
    @Bearer
    private String accessToken;

    @ExpireDate
    private LocalDateTime expiresAt;

    // Getters and Setters
}
```

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.