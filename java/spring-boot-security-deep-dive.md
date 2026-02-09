# Spring Boot Security - Deep Dive Guide

**Target:** Skill enhancement for WITCH companies and beyond (Senior Roles).  
**Covers:** Authentication, Authorization, OAuth2, JWT, Filters, Method Security, and CSRF/CORS.

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
