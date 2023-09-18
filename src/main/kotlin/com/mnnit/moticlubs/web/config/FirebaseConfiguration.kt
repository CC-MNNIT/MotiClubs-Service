package com.mnnit.moticlubs.web.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
class FirebaseConfiguration {

    @ConfigurationProperties(prefix = "firebase")
    class Properties {
        lateinit var issuer: String
        lateinit var audience: String
        lateinit var publicKeyUrl: String
    }

    @Bean
    fun googleCredentials(): GoogleCredentials = GoogleCredentials
        .fromStream(FileInputStream("src/main/resources/firebase_private_key.json"))

    @Bean
    fun firebaseApp(googleCredentials: GoogleCredentials): FirebaseApp = FirebaseApp
        .initializeApp(
            FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build(),
        )

    @Bean
    fun firebaseAuth(firebaseApp: FirebaseApp): FirebaseAuth = FirebaseAuth.getInstance(firebaseApp)

    @Bean
    fun firebaseMessaging(firebaseApp: FirebaseApp): FirebaseMessaging = FirebaseMessaging.getInstance(firebaseApp)
}
