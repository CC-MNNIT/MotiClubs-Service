package com.mnnit.moticlubs.web.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
class FirebaseConfiguration {

    @Bean
    fun firebaseApp(): FirebaseApp = FirebaseApp
        .initializeApp(
            FirebaseOptions.builder()
                .setCredentials(
                    GoogleCredentials.fromStream(FileInputStream("src/main/resources/firebase_private_key.json"))
                )
                .build()
        )

    @Bean
    fun firebaseAuth(firebaseApp: FirebaseApp): FirebaseAuth = FirebaseAuth.getInstance(firebaseApp)

    @Bean
    fun firebaseMessaging(firebaseApp: FirebaseApp): FirebaseMessaging = FirebaseMessaging.getInstance(firebaseApp)
}
