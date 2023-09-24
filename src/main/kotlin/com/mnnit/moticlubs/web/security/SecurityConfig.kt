package com.mnnit.moticlubs.web.security

import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.EMAIL_REGEX
import com.mnnit.moticlubs.utils.ServiceLogger
import com.mnnit.moticlubs.utils.UnauthorizedException
import com.mnnit.moticlubs.utils.putReqId
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
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
            "/login",
            "/logout",
        )
    }

    init {
        LOGGER.info("context-path: $contextPath")
    }

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        keyProvider: KeyProvider,
    ): SecurityWebFilterChain = http
        .csrf { it.disable() }
        .addFilterAt(
            // Change context path of service
            { exchange, chain ->
                chain.filter(
                    exchange.mutate()
                        .request(exchange.request.mutate().contextPath(contextPath).build())
                        .build(),
                )
            },
            SecurityWebFiltersOrder.FIRST,
        )
        .addFilterAt(
            // Firebase auth for APIs
            firebaseAuthTokenFilter(keyProvider),
            SecurityWebFiltersOrder.AUTHENTICATION,
        )
        .addFilterAt(
            // Store requestId for logging and tracing
            { exchange, chain ->
                putReqId(exchange.request.id)
                chain.filter(exchange)
            },
            SecurityWebFiltersOrder.AUTHORIZATION,
        )
        .authorizeExchange { spec ->
            spec.pathMatchers(
                "/actuator/**",
                "/login/**",
            ).permitAll()
                .anyExchange().access { authentication, _ ->
                    authentication.map { auth ->
                        when (val principal = auth.principal) {
                            is AuthenticationToken -> AuthorizationDecision(true)
                            is DefaultOidcUser -> AuthorizationDecision(principal.email matches EMAIL_REGEX)
                            else -> AuthorizationDecision(false)
                        }
                    }
                }
        }
        .oauth2Login { }
        .oauth2Client { }
        .oauth2ResourceServer { it.jwt { } }
        .logout { it.logoutUrl("/logout") }
        .build()

    private fun firebaseAuthTokenFilter(keyProvider: KeyProvider): AuthenticationWebFilter = AuthenticationWebFilter(
        ReactiveAuthenticationManager { auth ->
            val token = auth.principal as AuthenticationToken
            LOGGER.info(
                "authentication: emailVerified: ${token.isEmailVerified}; claims: ${token.userId}",
            )
            Mono.just(auth.apply { isAuthenticated = true })
        },
    ).apply {
        setServerAuthenticationConverter { exchange ->
            putReqId(exchange.request.id)

            val reqPath = exchange.request.path.value()

            LOGGER.info("attempt path: ${exchange.request.method.name()} ${exchange.request.path.value()}")
            val authHeader = exchange.request.headers[HttpHeaders.AUTHORIZATION]
                ?.first()
                ?.replace("Bearer", "")
                ?.trim()

            exchange.session.flatMap { session ->
                val validSession = session.isStarted && !session.isExpired

                authHeader ?: return@flatMap when {
                    AUTH_WHITELIST_PATH
                        .map { "$contextPath$it" }
                        .any { reqPath.startsWith(it) } -> Mono.empty()

                    reqPath.startsWith("$contextPath/$BASE_PATH") -> if (validSession) {
                        Mono.empty()
                    } else {
                        Mono.error(UnauthorizedException("Missing firebase auth token"))
                    }

                    else -> Mono.error(UnauthorizedException("Path not whitelisted"))
                }

                try {
                    val token = keyProvider.verifyJwt(authHeader)
                    Mono.just(FirebaseAuthentication(token))
                } catch (e: Exception) {
                    LOGGER.warn("Invalid auth token: ${e.localizedMessage}")
                    Mono.error(UnauthorizedException(e.localizedMessage))
                }
            }
        }
    }
}
