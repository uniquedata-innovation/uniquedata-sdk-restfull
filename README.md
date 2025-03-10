# UniqueDataRestFullClient Framework

> **Simplify your HTTP requests with automatic authentication and powerful interceptors.**

**UniqueDataRestFullClient** is a Java framework inspired by Feign, designed to speed up the development of HTTP integrations in Java projects. It provides automatic Bearer token authentication, configurable interceptors, and advanced request handling features. In addition, it is fully object-oriented, allowing you to pass parameters and form-data as objects, without the need to manually map each field. 
**By UniqueData Innovation company from Brazil**

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
  <groupId>br.com.uniquedata.sdk.restfull.restfull</groupId>
  <artifactId>uniquedata-sdk-restfull</artifactId>
  <version>0.0.1-BETA.1</version>
</dependency>
```

### System Requirements
- Java 17 or higher.

---

## Getting Started

### Using simple mode example

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

### Using with Spring Boot example

If your project uses Spring Boot, simply **scan** the framework in your main application class so that annotated interfaces will be detected:

```java
@SpringBootApplication
@EnableUniqueDataRestFullClient
public class Application {
    public static void main(final String[] args) {
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

## Automatic Authentication

The `@AutoAuthentication` annotation performs automatic authentication, allowing you to configure how the token is obtained and reused. You can control the following settings:


- **autoRecover**: If `true`, the token is stored on disk. If the application restarts, the framework will read the last saved token.  
- **type**: Authentication type (e.g., `AuthType.BEARER_TOKEN`).  
- **authenticate**: Details on how to obtain the token (URL, credential class, etc.).  
- **interception**: Configurations for interceptors to inject headers and manage expiration.

Full example of a default Bearer token JSON

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
        //export MY_CREDENTIAL_ENV="{\"clientId\": \"124\", \"secret\": \"123\",\"grantType\":\"client_credentials\"}"
        credentialJsonEnvironmentVariable = "MY_CREDENTIAL_ENV", 
        
        //Fixed input credentials, but only for local development. Not secure! For testing purposes only!
        //credentialJsonForTest = "{"client_id": "124", "secret": "123","grant_type":"client_credentials"}", 

        // Class where credentials will be mapped
        typeClassCredential = CredencialDto.class,

        // Class response with authorized
        typeClassAuthorize = ReponseAuthorizeDto.class,

        // Additional headers for the authentication request
        additionalHeaders = {
            @AdditionalHeader(headerName = "teste", headerValue = "auth")
        }
    ),
    // Interceptor configuration
    interception = @Interception(
        // Enables the interceptor to automatically inject the Bearer token in requests
        enabled = true,
        // Token expiration time in ms. If `@ExpireDate` is not used in the `typeClassAuthorize`
        //expireInMilliseconds = 60_000
    )
)
public interface AutoAuthencatedTestApi {
    // Your protected API methods...
}
```

#### Type Class Request Credential

The Credential class is a data model that mirrors the user credentials provided in the `@AutoAuthentication` annotation. This class serves as a structured representation of authentication details, which are used to obtain an access token from an authentication service.

Annotations and Fields

- **@RestFullField**: For the field with a customized name. Used by `typeClassCredential` and `typeClassAuthorize`.
- **client_id** `@RestFullField("client_id")`: Represents the client identifier required for authentication.
- **secret**: Holds the client secret, which is typically used alongside the client ID for secure authentication.
- **grantType** `@RestFullField("grant_type")`: Defines the type of grant being used in the authentication process (e.g., password, client_credentials).

This class is primarily used when interacting with authentication endpoints that require structured credential data. The framework will automatically serialize these fields into a request format compatible with the authentication API.

```java
public class CredentialDto {

    @RestFullField("client_id")
    private String client_id;

    private String secret;
   
    @RestFullField("grant_type")
    private String grantType;
   
    // getters and setters
}
```
---

#### Type Class Response Authorize

The ResponseAuthorize class represents the response structure for authentication requests. This class is responsible for handling authorization tokens and their expiration details after a successful authentication request.

Annotations and Fields

- **@Bearer**: For the field that represents the token in the authorize class (e.g., `private String token;`).  
- **@ExpireDate**: For the field that represents the token’s expiration date/time (e.g., `private Instant expireTime;`). If this field is not annotated, use `expireInMilliseconds` in `@Interception` to force re-authentication.  
- **@RestFullField**: For the field with a customized name. Used by `typeClassCredential` and `typeClassAuthorize`.

Example of a ReponseAuthorizeDto class:

```java
public class ReponseAuthorizeDto {

    @Bearer
    private String accessToken;

    @ExpireDate
    private LocalDateTime expiresAt; // OR private Date expiresAt
   
    // getters and setters
}
```

---

### Full example of a Bearer with Form-data

Form-Data Authentication: When using form-data for authentication, `@AdditionalHeader` is required. If you have custom fields, use `@RestFullField`.

```java
@AutoAuthentication(
    autoRecover = false,
    type = AuthType.BEARER_TOKEN,
    authenticate = @Authentication(
        enabled = true,
        fullUrlAuth = "https://api.testing.com/oauth/token",
        
        //Fixed input credentials, but only for local development. Not secure! For testing purposes only!
        credentialJsonForTest = "{"client_id": "124", "secret": "123","grant_type":"client_credentials"}", 
        
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

---

### No Authentication (Fixed Token)

The following example demonstrates how to configure `@AutoAuthentication` with a fixed Bearer token. This approach is useful when using a static authentication token instead of dynamically retrieving one.


```java
@AutoAuthentication(
    autoRecover = true,
    type = AuthType.BEARER_TOKEN,
    interception = @Interception(
        enabled = true,
        
        //If no expiry date field is present, you can manually define the expiration time.”
        expireInMilliseconds = 60_000,
        
        additionalHeaders = {
            @AdditionalHeader(headerName = "User-Agent", headerValue = "Mozilla/5.0 Chrome/127.0.0.0 Safari/537.36"),
            @AdditionalHeader(headerName = "Authorization", headerValue = "Bearer YOYR_TOKEN_FIXED_HERE")
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
- **Manage Expiration**: If your `typeClassAuthorize` class does not have the `@ExpireDate` annotation, use `expireInMilliseconds` to force token renewal after a certain period.  
- **Easily Disable**: Set `enabled = false` if you want to disable the interceptor.

```java
@Interception(
    enabled = true,
    expireInMilliseconds = 120_000, //optional
    additionalHeaders = {
        @AdditionalHeader(headerName = "User-Agent", headerValue = "CustomAgent/1.0"),
        @AdditionalHeader(headerName = "Accept-Language", headerValue = "pt-BR")
    }
)
```

---

## HTTP Requests


### Example of GET/POST/DELETE

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

    // Example GET with Path Variable
    @UniqueDataRestFullGet(value = "/v3/jobs/{uid}")
    public TesteDataDto checkJobId2(@RestFullPathVar("uid") String uid);

    // Example POST with Body
    @UniqueDataRestFullPost("/")
    public TestResponseBodyDto save(@RestFullBody TestRequestBodyDto requestBody);

    // Example DELETE
    @UniqueDataRestFullDelete("/{id}")
    public TestResponseBodyDto deleteById(@RestFullPathVar("id") String id);
    
    @UniqueDataRestFullGet(value = "/api/public/v1/products")
    public List<TestResponseBodyDto> getProducts(@RestFullParam("offset") int offset, @RestFullParam("limit") int limit);
    
    @UniqueDataRestFullGet(value = "/api/public/v1/products")
    public UniqueDataRestFullResponse<List<TestResponseBodyDto>> getProducts(
        @RestFullParam("offset") int offset,
        @RestFullParam("limit") int limit
    );
 
}
```

```java
    public static void main(final String[] args) {
        final ExampleApi api = UniqueDataRestFull.getApi(ExampleApi.class);
        final UniqueDataRestFullResponse<List<TestResponseBodyDto>> response = api.getProducts(0,1);
        
        final List<TestResponseBodyDto> list = response.getResponseBody();
        final ResponseHttpStatus httpStatus = response.getHttpStatus();
        final Map<String, String> responseHeaders = response.getResponseHeaders();
    }
 ```

---

## Passing Objects as Parameters

The framework also allows you to map URL parameters, form-data, or x-www-form-urlencoded directly from objects. It automatically converts DTOs (Data Transfer Objects), POJOs (Plain Old Java Objects), or other structured objects into request parameters.

```java
public class Teste {
    private String nome;
    
    private Integer idade;

	 @RestFullField("complex_name")
	 private String complexName;	

    // getters and setters
}
```

Simply annotate the method with `@RestFullParamToObject` or `@RestFullFormDataToObject` (depending on your use case), and the framework will assemble the request. For example:

```java
@UniqueDataRestFullGet("/filters")
TestResponseBodyDto filtersByObject(@RestFullParamToObject Teste filtros);
```

This will result in a URL like `...?nome=nomeValue&idade=idadeValue`.

For form-data cases, just use:

```java
@UniqueDataRestFullPost(
    endpoint = "/save",
    method = RestFullMethod.POST,
    accept = RestFullMediaType.APPLICATION_X_WWW_FORM_URLENCODED
)
TestResponseBodyDto saveByUrlEncoded(@RestFullFormDataToObject Teste requestBody);
```

---

## More Annotation Usage

 **@RestFullBody**: Example body **JSON** 

```java
@UniqueDataRestFullPost(endpoint = "/save", accept = RestFullMediaType.APPLICATION_X_WWW_FORM_URLENCODED)
public TestResponseBodyDto save(@RestFullFormData("name") final String name, @RestFullFormData("year") final int year);
```

---

 **@RestFullFormData**: Example 

```java
@UniqueDataRestFullPost(endpoint = "/save", accept = RestFullMediaType.APPLICATION_X_WWW_FORM_URLENCODED)
public TestResponseBodyDto save(@RestFullFormData("name") final String name, @RestFullFormData("year") final int year);
```

---

 **@RestFullMapToFormData**: Example 

```java
Map<String, Object> atributeFormData = ...;
atributeFormData.put("fist","JADER");
atributeFormData.put("last","DEV");

@UniqueDataRestPost(endpoint = "/", accept = RestFullMediaType.APPLICATION_X_WWW_FORM_URLENCODED)
public TestResponseBodyDto save(@RestFullMapToFormData final Map<String, Object> formaDataMa);
```

---

 **@RestFullParam**: Example 

```java
URL http://domain.com.br/endpoint?id=22

@UniqueDataRestFullGet("/")
public TestResponseBodyDto getById(@RestFullParam("id") final String id);
```

---

 **@RestFullMapToParam**: Example

```java
Map<String, Object> atributeParameters = ...;
atributeFormData.put("first","JADER");
atributeFormData.put("last","DEV");

@UniqueDataRestGet(endpoint = "/parameters",accept)
public TestResponseBodyDto get(@RestFullMapToParam final Map<String, Object> parameters);

build to > /paramters?first=JADER&last=DEV
```

---

 **@RestFullObjectToParam**: Example of URL https://domain.com.br/endpoint/filters?name=jader&text=dev&complex_name=jb

```java 
public class TestRequestObjectToParamDto {
  private String name;
  private String text;
  @RestFullField("complex_name")
  private String complexName;
  
  // getters and setters
}

@UniqueDataRestGet("/filters")
public TestResponseBodyDto filters(@RestFullObjectToParam final TestRequestObjectToParamDto object);
```

---

**@RestFullPathVar**: Example URL http://domain.com.br/endpoint/200

```java
@UniqueDataRestFullGet("/{id}")
public TestResponseBodyDto getById(@RestFullPathVar("id") final String id);
 
@UniqueDataRestFullDelete("/{id}")
public TestResponseBodyDto deleteById(@RestFullPathVar("id") final String id);
```

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.


