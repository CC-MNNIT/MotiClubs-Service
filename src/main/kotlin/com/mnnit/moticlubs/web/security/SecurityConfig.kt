package com.mnnit.moticlubs.web.security

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import com.mnnit.moticlubs.utils.ServiceLogger
import com.mnnit.moticlubs.utils.putReqId
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig(
    @Value("\${app.context-path}") private val contextPath: String,
) {

    companion object {
        private val LOGGER = ServiceLogger.getLogger(SecurityConfig::class.java)
        private val AUTH_WHITELIST_PATH = arrayOf(
            "/actuator",
            "/swagger",
            "/webjars/swagger-ui",
            "/v3/api-docs",
        )
    }

    init {
        LOGGER.info("context-path: $contextPath")
    }

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        firebaseAuth: FirebaseAuth,
    ): SecurityWebFilterChain = http
        .csrf { it.disable() }

        // Change context path of service
        .addFilterAt({ exchange, chain ->
            chain.filter(
                exchange.mutate()
                    .request(exchange.request.mutate().contextPath(contextPath).build())
                    .build()
            )
        }, SecurityWebFiltersOrder.FIRST)

        // Firebase auth for APIs
        .addFilterAt(firebaseAuthTokenFilter(firebaseAuth), SecurityWebFiltersOrder.AUTHENTICATION)

        // Store requestId for logging and tracing
        .addFilterAt({ exchange, chain ->
            putReqId(exchange.request.id)
            chain.filter(exchange)
        }, SecurityWebFiltersOrder.AUTHORIZATION)
        .authorizeExchange { spec ->
            spec.pathMatchers(
                "/actuator/**",
                "/swagger/**",
                "/webjars/swagger-ui/**",
                "/v3/api-docs/**",
            ).permitAll()
                .anyExchange().authenticated()
        }
        .build()

    private fun firebaseAuthTokenFilter(firebaseAuth: FirebaseAuth): AuthenticationWebFilter = AuthenticationWebFilter(
        ReactiveAuthenticationManager { auth ->
            val token = auth.principal as FirebaseToken
            LOGGER.info(
                "authentication: emailVerified: ${token.isEmailVerified}; claims: ${
                    firebaseAuth.getUser(token.uid).customClaims
                }"
            )
            Mono.just(auth.apply { isAuthenticated = true })
        }
    ).apply {
        setServerAuthenticationConverter { exchange ->
            putReqId(exchange.request.id)

            val reqPath = exchange.request.path.value()
            if (AUTH_WHITELIST_PATH
                    .map { "$contextPath$it" }
                    .any { reqPath.startsWith(it) }
            ) {
                return@setServerAuthenticationConverter Mono.empty()
            }

            LOGGER.info("attempt path: ${exchange.request.method.name()} ${exchange.request.path.value()}")
            val authHeader = exchange.request.headers[HttpHeaders.AUTHORIZATION]
                ?.first()
                ?.replace("Bearer", "")
                ?.trim()
                ?: return@setServerAuthenticationConverter Mono.error(
                    ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing firebase auth token")
                )

            try {
                val token = firebaseAuth.verifyIdToken(authHeader)
                Mono.just(FirebaseAuthentication(token))
            } catch (e: Exception) {
                LOGGER.warn("Invalid auth token: ${e.localizedMessage}")
                Mono.error(ResponseStatusException(HttpStatus.UNAUTHORIZED, e.localizedMessage))
            }
        }
    }
}
