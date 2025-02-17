### **Manuale della Sicurezza in Spring Boot**

---

## **Introduzione**
Questo manuale spiega il **flusso di sicurezza in Spring Boot** con autenticazione basata su **JWT (JSON Web Token)**. Il sistema gestisce:
- **Autenticazione degli utenti**
- **Protezione delle API**
- **Gestione dei ruoli e permessi**

Tutte le classi servono a **configurare** e **personalizzare** i comportamenti di sicurezza di Spring, oppure **integrare** quelli già esistenti.

---

## **1. Giro del Fumo: Il Flusso della Sicurezza**
Il flusso della sicurezza coinvolge sia il **Frontend (FE)** che il **Backend (BE)**:

1. **Registrazione**
    - Il FE invia una richiesta `POST /api/auth/register` con **username e password**.
    - Il BE riceve la richiesta tramite **AuthController.java** e la passa a **AppUserService.java**.
    - L’utente viene salvato nel database con un ruolo predefinito (`ROLE_USER`).

2. **Login**
    - Il FE invia una richiesta `POST /api/auth/login` con credenziali.
    - Il BE autentica l’utente usando **AppUserService.java**.
    - Se le credenziali sono corrette, **JwtTokenUtil.java** genera un token JWT.
    - Il token viene restituito al FE dentro **AuthResponse.java**.

3. **Accesso alle API**
    - Il FE usa il token ricevuto nelle richieste API, inserendolo nell’**header Authorization** (`Bearer <TOKEN>`).
    - **JwtRequestFilter.java** intercetta ogni richiesta, valida il token e imposta l’utente nel contesto di sicurezza.
    - **SecurityConfig.java** decide se l’utente può accedere alla risorsa richiesta.

---

## **2. Classi Coinvolte**
### **1. Modello Utente**
- **AppUser.java**
    - Modello dell’utente che implementa `UserDetails`.
    - Contiene **username, password e ruoli**.

  ```java
  @Entity
  @Table(name = "users")
  public class AppUser implements UserDetails {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
  
      @Column(nullable = false, unique = true)
      private String username;
  
      @Column(nullable = false)
      private String password;
  
      @ElementCollection(fetch = FetchType.EAGER)
      @Enumerated(EnumType.STRING)
      private Set<Role> roles;
  
      @Override
      public Collection<GrantedAuthority> getAuthorities() {
          return roles.stream()
              .map(role -> new SimpleGrantedAuthority(role.name()))
              .collect(Collectors.toList());
      }
  }
  ```

### **2. Repository Utente**
- **AppUserRepository.java**
    - Interfaccia JPA per gestire gli utenti nel database.
  ```java
  public interface AppUserRepository extends JpaRepository<AppUser, Long> {
      Optional<AppUser> findByUsername(String username);
      boolean existsByUsername(String username);
  }
  ```

### **3. Servizio di Autenticazione**
- **AppUserService.java**
    - Gestisce registrazione e autenticazione utenti.
    - Utilizza **PasswordEncoder** per salvare password sicure.
    - Se il login è valido, chiama **JwtTokenUtil.java** per generare il token.

  ```java
  public String authenticateUser(String username, String password) {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(username, password)
      );
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      return jwtTokenUtil.generateToken(userDetails);
  }
  ```

### **4. Controller di Autenticazione**
- **AuthController.java**
    - Espone API di login e registrazione.
  ```java
  @RestController
  @RequestMapping("/api/auth")
  public class AuthController {
  
      private final AppUserService appUserService;
  
      @PostMapping("/register")
      public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
          appUserService.registerUser(
                  registerRequest.getUsername(),
                  registerRequest.getPassword(),
                  Set.of(Role.ROLE_USER) // Ruolo predefinito
          );
          return ResponseEntity.ok("Registrazione completata");
      }
  
      @PostMapping("/login")
      public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
          String token = appUserService.authenticateUser(
                  loginRequest.getUsername(),
                  loginRequest.getPassword()
          );
          return ResponseEntity.ok(new AuthResponse(token));
      }
  }
  ```

### **5. Gestione del Token JWT**
- **JwtTokenUtil.java**
    - Genera e valida i token JWT.
  ```java
  public String generateToken(UserDetails userDetails) {
      List<String> roles = userDetails.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)
              .collect(Collectors.toList());

      return Jwts.builder()
              .setSubject(userDetails.getUsername())
              .claim("roles", roles)
              .setIssuedAt(new Date())
              .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
              .signWith(SignatureAlgorithm.HS256, secret)
              .compact();
  }
  ```

### **6. Filtro JWT**
- **JwtRequestFilter.java**
    - Intercetta ogni richiesta HTTP e verifica il token.
    - Se il token è valido, imposta l’utente autenticato nel contesto di Spring Security.

  ```java
  @Component
  public class JwtRequestFilter extends OncePerRequestFilter {
  
      @Autowired
      private CustomUserDetailsService customUserDetailsService;
  
      @Autowired
      private JwtTokenUtil jwtTokenUtil;
  
      @Override
      protected void doFilterInternal(HttpServletRequest request,
                                      HttpServletResponse response,
                                      FilterChain chain) throws ServletException, IOException {
          final String requestTokenHeader = request.getHeader("Authorization");

          if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
              String jwtToken = requestTokenHeader.substring(7);
              String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

              if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                  UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                  if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                      UsernamePasswordAuthenticationToken authenticationToken =
                              new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                  }
              }
          }
          chain.doFilter(request, response);
      }
  }
  ```

---

## **3. Aggiungere un Nuovo Filtro**
Per aggiungere un nuovo filtro, devi:
1. **Creare una classe che estende `OncePerRequestFilter`**
2. **Registrarlo in `SecurityConfig.java` con `addFilterBefore()`**

**Esempio:**
```java
@Component
public class CustomLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        System.out.println("Richiesta ricevuta: " + request.getRequestURI());
        chain.doFilter(request, response);
    }
}
```

**Aggiunta in `SecurityConfig.java`:**
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .addFilterBefore(new CustomLoggingFilter(), JwtRequestFilter.class)
        .build();
}
```

---

## **4. Configurazione Sicurezza**
- **SecurityConfig.java** configura l'accesso alle API e registra i filtri di sicurezza.
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
```
### **Integrazione: Configurazione di Spring e Flusso Temporale delle Classi**

---

## **1. Classi di Spring che vengono configurate e il loro ruolo nel ciclo di vita**
Nel **ciclo di avvio e gestione della sicurezza**, Spring Boot carica e utilizza le classi di configurazione personalizzate in momenti precisi. Ecco dove intervengono le classi che hai fornito:

### **A. Fase di Avvio dell’Applicazione**
1. **Spring carica le configurazioni di sicurezza e le dipendenze necessarie.**
    - **`SecurityConfig.java`**: Configura la sicurezza, i filtri e le autorizzazioni.
    - **`CorsConfig.java`**: Configura le regole CORS per permettere richieste cross-origin.

2. **Spring legge la configurazione e crea utenti predefiniti.**
    - **`AuthRunner.java`**: All’avvio dell’applicazione, controlla se gli utenti predefiniti (`admin`, `user`, `seller`) esistono. Se non esistono, li crea.

### **B. Fase di Autenticazione (Login)**
1. **L’utente invia le credenziali (`LoginRequest.java`).**
2. **Il controller riceve la richiesta e la inoltra al servizio di autenticazione.**
    - **`AuthController.java`** gestisce la richiesta di login.
    - **`AppUserService.java`** verifica le credenziali con `AuthenticationManager`.

3. **Se le credenziali sono corrette, Spring Security esegue l'autenticazione.**
    - **Spring utilizza `CustomUserDetailsService.java` per caricare l’utente dal database.**
    - **Se tutto è valido, `JwtTokenUtil.java` genera un token JWT.**
    - **Il token viene restituito nel DTO `AuthResponse.java`.**

### **C. Fase di Validazione delle Richieste API**
1. **L’utente autenticato invia una richiesta API con il token JWT.**
2. **`JwtRequestFilter.java` intercetta la richiesta e verifica il token.**
    - **Se il token manca, viene sollevata l’eccezione `JwtTokenMissingException.java`.**
    - **Se il token è valido, Spring Security imposta l’utente autenticato nel contesto.**

3. **Spring Security esegue le autorizzazioni prima di accettare la richiesta.**
    - **Se l’utente non ha i permessi corretti, `JwtAuthenticationEntryPoint.java` restituisce un errore 401.**

4. **L’API viene eseguita solo se l’utente è autorizzato.**

---

## **2. Schema Temporale: Quando ogni Classe Viene Richiamata**
| **Momento**                   | **Classe Coinvolta**                       | **Descrizione** |
|--------------------------------|-------------------------------------------|----------------|
| **Avvio Applicazione**        | `SecurityConfig.java`                     | Configura la sicurezza, carica i filtri JWT |
|                                | `CorsConfig.java`                         | Configura CORS per il supporto a richieste cross-origin |
|                                | `AuthRunner.java`                         | Crea utenti predefiniti (`admin`, `user`, `seller`) |
| **Registrazione Utente**      | `AuthController.java`                     | Riceve la richiesta di registrazione |
|                                | `AppUserService.java`                     | Crea un nuovo utente e lo salva nel database |
|                                | `RegisterRequest.java`                    | DTO contenente username e password per la registrazione |
| **Login Utente**              | `AuthController.java`                     | Riceve la richiesta di login |
|                                | `AppUserService.java`                     | Verifica le credenziali |
|                                | `CustomUserDetailsService.java`           | Recupera l’utente dal database |
|                                | `JwtTokenUtil.java`                       | Genera il token JWT per l’utente autenticato |
|                                | `AuthResponse.java`                       | DTO che restituisce il token JWT al frontend |
|                                | `LoginRequest.java`                       | DTO con username e password per il login |
| **Invio di una Richiesta API** | `JwtRequestFilter.java`                   | Intercetta la richiesta e legge il token JWT |
|                                | `JwtTokenUtil.java`                       | Valida il token e verifica username e ruoli |
|                                | `CustomUserDetailsService.java`           | Carica l’utente autenticato nel contesto di sicurezza |
|                                | `SecurityConfig.java`                     | Controlla se l’utente ha i permessi necessari |
| **Token Mancante o Invalido**  | `JwtTokenMissingException.java`           | Lancia un’eccezione se il token è assente |
|                                | `JwtAuthenticationEntryPoint.java`        | Restituisce un errore 401 se l’utente non è autorizzato |
| **Accesso alle API con Swagger** | `OpenApiConfig.java`                   | Configura Swagger per autenticazione tramite JWT |

---

Questa integrazione chiarisce **quando e dove Spring chiama ogni classe** nel flusso di autenticazione e gestione della sicurezza.