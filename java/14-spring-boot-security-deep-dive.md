# Spring Boot Security - Complete Deep Dive Guide

**Target:** Senior Java developers — WITCH, FAANG, and beyond.
**Covers:** Every Java security type — Authentication, Authorization, JWT, OAuth2, OIDC, SAML, LDAP, API Key, mTLS, RBAC, ABAC, CORS, CSRF, Method Security, Cryptography, Audit Logging, Rate Limiting, and more.

---

## Table of Contents

1. [Spring Security Architecture](#architecture)
2. [Authentication vs Authorization](#authentication-vs-authorization)
3. [JWT (JSON Web Token) Implementation](#jwt-json-web-token-implementation)
4. [OAuth2 & OpenID Connect](#oauth2-openid-connect)
5. [CORS & CSRF](#cors-csrf)
6. [Method Security](#method-security)
7. [UserDetailsService & PasswordEncoder](#userdetailsservice-passwordencoder)
8. [Complex Technical Scenarios](#complex-technical-scenarios)
9. [Key Topics & Explanations](#key-topics-explanations)

---

## 1. Spring Security Architecture

Based on **Servlet Filters**. A chain of filters intercepts requests before they reach the DispatcherServlet.

### Core Components
1.  **SecurityContextHolder**: Stores `SecurityContext` (Current User details). Use `ThreadLocal`.
2.  **Authentication**: Interface representing the user (Principal, Credentials, Authorities).
3.  **AuthenticationManager**: Validates `Authentication` object.
4.  **ProviderManager**: Standard implementation of `AuthenticationManager`. It delegates to a list of `AuthenticationProvider`s.
5.  **UserDetailsService**: Loads user-specific data (from DB).
6.  **GrantedAuthority**: Represents a permission or role (e.g., `ROLE_ADMIN`).

### Filter Chain
Request -> `DelegatingFilterProxy` -> `FilterChainProxy` -> Security Filters (e.g., `UsernamePasswordAuthenticationFilter`, `BasicAuthenticationFilter`).

---

## 2. Authentication vs Authorization

*   **Authentication (Who are you?)**: Verifying identity (Login).
    *   Example: Username/Password check.
    *   Result: `Authentication` object created.
*   **Authorization (What can you do?)**: Verifying access rights (Permissions).
    *   Example: Does user have `ROLE_ADMIN` to access `/admin`?
    *   Result: Access Granted or `AccessDeniedException` (403).

---

## 3. JWT (JSON Web Token) Implementation

Stateless authentication mechanism.

### Structure
1.  **Header**: Algorithm & Token Type.
2.  **Payload**: Claims (Subject, Expiration, Roles).
3.  **Signature**: Verifies integrity.

### Workflow
1.  User POSTs credentials to `/login`.
2.  Server validates and generates JWT (signed with Secret Key).
3.  Server sends JWT to client.
4.  Client sends JWT in `Authorization: Bearer <token>` for future requests.
5.  **JwtAuthenticationFilter**: Intercepts request -> Parses Token -> Validates Signature -> Extracts User -> Sets `SecurityContext`.

```java
// Filter Example
String token = request.getHeader("Authorization");
if (token != null && jwtUtils.validate(token)) {
    UsernamePasswordAuthenticationToken auth = ...;
    SecurityContextHolder.getContext().setAuthentication(auth);
}
chain.doFilter(request, response);
```

---

## 4. OAuth2 & OpenID Connect

Standard protocol for authorization.

*   **Resource Owner**: User.
*   **Client**: Application (e.g., React App).
*   **Authorization Server**: Google/Facebook/Keycloak (Issues tokens).
*   **Resource Server**: Your API (Validates tokens).

### Spring Boot Resource Server
`spring-boot-starter-oauth2-resource-server` automatically validates JWTs issued by an Auth Server using JWK Set URI.

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://accounts.google.com
```

---

## 5. CORS & CSRF

### CORS (Cross-Origin Resource Sharing)
Browser security feature restricting cross-origin HTTP requests.
*   **Preflight Request (OPTIONS)**: Checks if actual request is safe.
*   **Configuration**:
    ```java
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000");
            }
        };
    }
    ```

### CSRF (Cross-Site Request Forgery)
Attack forcing user to execute unwanted actions.
*   **Protection**: Spring Security enables it by default (Synchronizer Token Pattern).
*   **Stateless APIs (JWT)**: Usually **disable CSRF** because there is no session to exploit. The JWT (in header) prevents CSRF naturally as attackers can't forge custom headers easily.

---

## 6. Method Security

Securing individual methods rather than URL patterns.

1.  Enable: `@EnableGlobalMethodSecurity(prePostEnabled = true)`
2.  Usage:
    ```java
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long id) { ... }
    
    @PostAuthorize("returnObject.owner == authentication.name")
    public Order getOrder(Long id) { ... }
    ```

---

## 7. UserDetailsService & PasswordEncoder

### UserDetailsService
Service interface to load user data.
```java
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) {
        // Fetch from DB repository
        User user = repo.findByUsername(username);
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}
```

### PasswordEncoder
Never store plain text passwords!
*   **BCryptPasswordEncoder**: Standard strong hashing.
*   `Start up`: `PasswordEncoder encoder = new BCryptPasswordEncoder();`

---

## 8. Complex Technical Scenarios

### Topic 1: How to handle Multiple Auth Providers?
**Answer:**
Configure `AuthenticationManagerBuilder`. Add multiple `AuthenticationProvider` beans (e.g., one for LDAD, one for DB). The Manager will try them in order.

### Topic 2: Stateless vs Stateful Security?
**Answer:**
*   **Stateful (Session)**: Server creates `HttpSession` (JSESSIONID). Good for traditional monoliths.
*   **Stateless (JWT)**: No session on server. Every request carries auth info. scalability. Good for Microservices.

### Topic 3: `@Secured` vs `@PreAuthorize`?
**Answer:**
*   `@Secured`: Older, standard Spring annotation. Only supports simple roles.
*   `@PreAuthorize`: Supports SpEL (Spring Expression Language). Can do complex logic (`hasRole('ADMIN') and #id < 10`).

---

## 9. Key Topics & Explanations

### Topic 1: What is the Security Filter Chain?
**Answer:**
A stack of filters that Spring Security inserts into the servlet container. It delegates specific tasks (login, logout, exception handling, header writing) to specialized filters.

### Topic 2: What is a Principal?
**Answer:**
The currently logged-in user. Usually an instance of `UserDetails`. Accessed via `SecurityContextHolder.getContext().getAuthentication().getPrincipal()`.

### Topic 3: How to implement "Remember Me"?
**Answer:**
Spring Security provides `RememberMeServices`. It generates a token (cookie) that persists after session expiration. When user returns, the cookie is validated to re-authenticate automatically.

```java
@Bean
public RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
    TokenBasedRememberMeServices services = new TokenBasedRememberMeServices("remember-me-key", userDetailsService);
    services.setTokenValiditySeconds(86400); // 24 hours
    return services;
}
```

### Topic 4: What is the difference between `ROLE_` and authorities?
**Answer:**
- **Roles**: Conventionally prefixed with `ROLE_` (e.g., `ROLE_ADMIN`). Used for high-level access control.
- **Authorities**: Fine-grained permissions (e.g., `READ_USERS`, `DELETE_USERS`). Can be used without `ROLE_` prefix.

### Topic 5: How to implement custom authentication providers?
**Answer:**
Create a class implementing `AuthenticationProvider` and register it as a bean.

```java
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) {
        // Custom auth logic (API call, external service, etc.)
        return new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
```

---

## 10. Advanced Security Patterns

### Multi-Tenant Security
Implement tenant-specific authentication and authorization.

```java
@Component
public class TenantAwareUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String tenantId = TenantContext.getCurrentTenant();
        // Load user from tenant-specific database/schema
        return userRepository.findByUsernameAndTenant(username, tenantId);
    }
}
```

### API Key Authentication
For service-to-service communication.

```java
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) {
        String apiKey = request.getHeader("X-API-Key");
        if (apiKey != null && apiKeyService.isValid(apiKey)) {
            Authentication auth = new ApiKeyAuthenticationToken(apiKey);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
```

### Rate Limiting
Protect against brute force attacks.

```java
@Bean
public FilterRegistrationBean<RateLimitFilter> rateLimitFilter() {
    FilterRegistrationBean<RateLimitFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new RateLimitFilter());
    registration.addUrlPatterns("/api/auth/*");
    return registration;
class SecurityTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testAccessDenied() throws Exception {
        mockMvc.perform(get("/admin"))
               .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testAccessGranted() throws Exception {
        mockMvc.perform(get("/admin"))
               .andExpect(status().isOk());
    }
}
```

### Integration Testing with Security
```java
@Test
void testJwtAuthentication() throws Exception {
    String token = jwtUtils.generateToken(userDetails);
    
    mockMvc.perform(get("/api/protected")
           .header("Authorization", "Bearer " + token))
           .andExpect(status().isOk());
}
```

---

## 12. Common Security Pitfalls & Solutions

### Pitfall 1: Hardcoded Secrets
**Problem:** API keys, passwords, or JWT secrets in code.
**Solution:** Use environment variables or secret management tools.

```yaml
# application.yml
jwt:
  secret: ${JWT_SECRET:default-secret}
```

### Pitfall 2: Insufficient Password Validation
**Problem:** Weak password policies.
**Solution:** Implement strong password validation.

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
}

@Component
public class PasswordValidator {
    public boolean isValid(String password) {
        return password.length() >= 12 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*[a-z].*") &&
               password.matches(".*[0-9].*") &&
               password.matches(".*[!@#$%^&*].*");
    }
}
```

### Pitfall 3: Missing HTTPS
**Problem:** Sensitive data transmitted over HTTP.
**Solution:** Enforce HTTPS in production.

```java
@Bean
public ServletWebServerFactory servletContainer() {
    TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
        @Override
        protected void postProcessContext(Context context) {
            SecurityConstraint securityConstraint = new SecurityConstraint();
            securityConstraint.setUserConstraint("CONFIDENTIAL");
            SecurityCollection collection = new SecurityCollection();
            collection.addPattern("/*");
            securityConstraint.addCollection(collection);
            context.addConstraint(securityConstraint);
        }
    };
    tomcat.addAdditionalTomcatConnectors(redirectConnector());
    return tomcat;
}
```

---

## 13. Performance & Monitoring

### Security Performance Tips
1. **Caching**: Cache user details and authorities.
2. **Lazy Loading**: Load authorities only when needed.
3. **Connection Pooling**: For database authentication.
4. **JWT Validation**: Use efficient signature verification.

```java
@Service
@CacheConfig(cacheNames = "users")
public class CachingUserDetailsService implements UserDetailsService {
    
    @Cacheable(key = "#username")
    @Override
    public UserDetails loadUserByUsername(String username) {
        // Database lookup
    }
}
```

### Security Monitoring
```java
@Component
@Slf4j
public class SecurityAuditListener {
    @EventListener
    public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
        log.info("User {} logged in successfully", 
                event.getAuthentication().getName());
    }
    
    @EventListener
    public void handleAuthenticationFailure(AuthenticationFailureEvent event) {
        log.warn("Failed login attempt for user {}", 
                event.getAuthentication().getName());
    }
}
```

---

## 14. Production Security Checklist

### Authentication
- [ ] Strong password policies implemented
- [ ] Multi-factor authentication (MFA) for sensitive operations
- [ ] JWT tokens have reasonable expiration times
- [ ] Refresh tokens are properly secured
- [ ] Account lockout after failed attempts

### Authorization
- [ ] Principle of least privilege applied
- [ ] Role-based access control (RBAC) implemented
- [ ] Method-level security for critical operations
- [ ] Regular security audits of permissions

### Infrastructure
- [ ] HTTPS enforced everywhere
- [ ] Security headers configured (HSTS, CSP, etc.)
- [ ] CORS properly configured
- [ ] CSRF protection enabled (for stateful apps)
- [ ] Rate limiting implemented

### Data Protection
- [ ] Sensitive data encrypted at rest
- [ ] PII properly masked in logs
- [ ] Database connections use SSL
- [ ] Backup data encrypted

### Monitoring & Auditing
- [ ] Security events logged
- [ ] Failed login attempts monitored
- [ ] Access logs reviewed regularly
- [ ] Intrusion detection systems in place

---

## 15. Code Examples Summary

### Complete Security Configuration
```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private JwtAuthenticationFilter jwtFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService)
               .passwordEncoder(passwordEncoder());
        return builder.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

---

## Conclusion

Spring Boot Security provides a comprehensive framework for securing applications. Key takeaways:

1. **Defense in Depth**: Use multiple layers of security (authentication, authorization, encryption, monitoring).
2. **Stateless vs Stateful**: Choose based on your architecture (microservices vs monolith).
3. **Principle of Least Privilege**: Grant minimum necessary permissions.
4. **Regular Updates**: Keep dependencies updated for security patches.
5. **Testing**: Comprehensive security testing is essential.

**Further Learning:**
- OWASP Top 10 vulnerabilities
- Spring Security reference documentation
- OAuth2 and OpenID Connect specifications
- Cryptography fundamentals
- Security design patterns

---

## 18. JWT, SSO & Advanced Authentication

### 1. JWT (JSON Web Token) Deep Dive

#### JWT Structure
```json
// Header
{
  "alg": "HS256",
  "typ": "JWT"
}

// Payload
{
  "sub": "1234567890",
  "name": "John Doe",
  "roles": ["USER", "ADMIN"],
  "iat": 1516239022,
  "exp": 1516242622
}

// Signature
HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
```

#### JWT Implementation
```java
@Component
public class JwtUtils {
    private final String jwtSecret;
    private final int jwtExpirationMs;
    
    public String generateJwtToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .claim("roles", userPrincipal.getAuthorities())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
    
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }
}
```

#### JWT Filter
```java
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) {
        String jwt = getJwtFromRequest(request);
        
        if (StringUtils.hasText(jwt) && jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUsernameFromJwtToken(jwt);
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```

### 2. SSO (Single Sign-On) Implementation

#### SAML 2.0 Integration
```java
@Configuration
public class SamlConfig {
    
    @Bean
    public RelyingPartyRegistrationRepository relyingPartyRegistrationRepository() {
        RelyingPartyRegistration registration = RelyingPartyRegistration
            .withRegistrationId("saml")
            .metadataUri("https://idp.example.com/metadata")
            .credentials(c -> c.add(credential()))
            .singleSignOnService(singleSignOnService())
            .build();
        
        return new InMemoryRelyingPartyRegistrationRepository(registration);
    }
    
    @Bean
    public Saml2WebSsoAuthenticationFilter saml2WebSsoAuthenticationFilter() {
        return new Saml2WebSsoAuthenticationFilter(
            new AntPathRequestMatcher("/saml2/sso/**"),
            authenticationManager()
        );
    }
}
```

#### OAuth2 SSO with Keycloak
```java
@Configuration
@EnableOAuth2Sso
public class OAuth2SsoConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/login**").permitAll()
                .anyRequest().authenticated()
            .and()
            .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                    .userService(customOAuth2UserService);
    }
}

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauth2User = delegate.loadUser(userRequest);
        
        // Map OAuth2 user to application user
        return new CustomOAuth2User(oauth2User, userService);
    }
}
```

### 3. LDAP Authentication

#### LDAP Configuration
```java
@Configuration
@EnableWebSecurity
public class LdapSecurityConfig {
    
    @Bean
    public AuthenticationManager ldapAuthenticationManager(BaseLdapPathContextSource contextSource) {
        LdapBindAuthenticationManagerFactory factory = 
            new LdapBindAuthenticationManagerFactory(contextSource);
        factory.setUserDnPatterns("uid={0},ou=people");
        factory.setUserSearchBase("ou=people");
        factory.setUserSearchFilter("(uid={0})");
        return factory.createAuthenticationManager();
    }
    
    @Bean
    public ContextSource contextSource() {
        return new DefaultSpringSecurityContextSource(
            "ldap://localhost:389/dc=springframework,dc=org");
    }
}
```

### 4. Multi-Factor Authentication (MFA)

#### TOTP Implementation
```java
@Service
public class TotpService {
    
    public String generateSecretKey() {
        return GoogleAuthenticator.generateRandomSecret();
    }
    
    public String generateQrCodeUrl(String secret, String email, String issuer) {
        return GoogleAuthenticator.getQRBarcodeURL(
            issuer, email, secret);
    }
    
    public boolean verifyCode(String secret, String code) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(secret, Integer.parseInt(code));
    }
}

@RestController
@RequestMapping("/api/mfa")
public class MfaController {
    
    @PostMapping("/enable")
    public ResponseEntity<?> enableMfa(@RequestBody MfaRequest request) {
        String secret = totpService.generateSecretKey();
        String qrUrl = totpService.generateQrCodeUrl(secret, request.getEmail(), "MyApp");
        
        // Save secret for user
        userService.saveMfaSecret(request.getEmail(), secret);
        
        return ResponseEntity.ok(new MfaResponse(qrUrl, secret));
    }
    
    @PostMapping("/verify")
    public ResponseEntity<?> verifyMfa(@RequestBody MfaVerificationRequest request) {
        boolean isValid = totpService.verifyCode(request.getSecret(), request.getCode());
        
        if (isValid) {
            userService.enableMfa(request.getEmail());
            return ResponseEntity.ok("MFA enabled successfully");
        }
        
        return ResponseEntity.badRequest().body("Invalid verification code");
    }
}
```

### 5. Token Refresh & Revocation

#### Refresh Token Implementation
```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        
        if (!refreshTokenService.validateRefreshToken(refreshToken)) {
            return ResponseEntity.badRequest().body("Invalid refresh token");
        }
        
        String username = refreshTokenService.getUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        String newAccessToken = jwtUtils.generateTokenFromUsername(username);
        String newRefreshToken = refreshTokenService.generateRefreshToken(username);
        
        return ResponseEntity.ok(new JwtResponse(newAccessToken, newRefreshToken));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest request) {
        String refreshToken = request.getRefreshToken();
        refreshTokenService.revokeRefreshToken(refreshToken);
        
        // Invalidate JWT token (add to blacklist)
        jwtUtils.blacklistToken(request.getAccessToken());
        
        return ResponseEntity.ok("Logged out successfully");
    }
}
```

#### Token Blacklist
```java
@Service
public class TokenBlacklistService {
    
    private final RedisTemplate<String, String> redisTemplate;
    
    public void blacklistToken(String token) {
        long expiration = jwtUtils.getExpirationFromToken(token);
        redisTemplate.opsForValue().set("blacklist:" + token, "true", 
            expiration - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
    
    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token));
    }
}
```

---

This guide covers the essential Spring Security concepts needed for senior-level interviews and production applications.
