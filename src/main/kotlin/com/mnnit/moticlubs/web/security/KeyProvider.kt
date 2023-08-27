package com.mnnit.moticlubs.web.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.impl.JWTParser
import com.mnnit.moticlubs.web.client.GoogleClient
import com.mnnit.moticlubs.web.config.FirebaseConfiguration
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.scheduler.Schedulers
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import java.security.InvalidAlgorithmParameterException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.interfaces.RSAPublicKey
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

@Service
class KeyProvider(
    private val googleClient: GoogleClient,
    private val properties: FirebaseConfiguration.Properties
) {

    private data class PublicCert(
        val maxAge: Long,
        val publicKeys: Map<String, JWTVerifier>
    )

    companion object {
        private const val TAG = "[FETCH_PUB]"
        private val LOGGER = LoggerFactory.getLogger(KeyProvider::class.java)
    }

    private val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(8)
    private var publicCert: PublicCert? = null
    private var seconds: Long = 0
    private var hasError: Boolean = true

    private val runnable = Runnable {
        googleClient
            .fetchPublicKeys()
            .map { pair -> PublicCert(pair.first, pair.second.mapValues { getJwtVerifier(it.value) }) }
            .publishOn(Schedulers.parallel())
            .subscribeOn(Schedulers.parallel())
            .onErrorMap {
                hasError = true
                LOGGER.warn("$TAG error: ${it.localizedMessage}")
                it
            }
            .subscribe { cert ->
                publicCert = cert
                seconds = cert.maxAge
                scheduleFetchCerts()
            }
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    fun pollScheduling() {
        if (!hasError) return

        hasError = false
        scheduleFetchCerts()
    }

    fun scheduleFetchCerts() {
        LOGGER.info("$TAG scheduled execution after: $seconds sec")
        executor.schedule(runnable, seconds, TimeUnit.SECONDS)
    }

    fun verifyJwt(jwt: String): AuthenticationToken {
        val cert = publicCert
        cert ?: throw ResponseStatusException(HttpStatus.TOO_EARLY, "Service unavailable")

        val header = JWTParser().parseHeader(
            Base64.getUrlDecoder()
                .decode(jwt.split(".")[0])
                .toString(StandardCharsets.UTF_8)
        )

        val verifier = cert.publicKeys[header.keyId]
            ?: throw InvalidAlgorithmParameterException("Key ID not found")

        return AuthenticationToken(verifier.verify(jwt))
    }

    private fun getJwtVerifier(publicKey: String): JWTVerifier = JWT
        .require(Algorithm.RSA256(getPublicRSA(publicKey), null))
        .withIssuer(properties.issuer)
        .withAudience(properties.audience)
        .build()

    private fun getPublicRSA(publicKey: String): RSAPublicKey {
        val decode = Base64.getDecoder().decode(
            publicKey
                .replace("-----BEGIN CERTIFICATE-----\n", "")
                .replace("-----END CERTIFICATE-----\n", "")
                .replace("\n", "")
        )
        val certificate = CertificateFactory.getInstance("X.509")
            .generateCertificate(ByteArrayInputStream(decode)) as X509Certificate
        return certificate.publicKey as RSAPublicKey
    }
}
