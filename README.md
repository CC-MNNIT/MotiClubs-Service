# MotiClubs - Service

[![Spring Maven Build](https://github.com/CC-MNNIT/MotiClubs-Service/actions/workflows/maven.yml/badge.svg)](https://github.com/CC-MNNIT/MotiClubs-Service/actions/workflows/maven.yml)

This service is backend of the [MotiClubs App](https://github.com/CC-MNNIT/MotiClubs).

## Swagger URLs

| Environment | URL                                       |
|-------------|-------------------------------------------|
| dev         | http://localhost:8002/swagger             |
| prod        | https://sac.mnnit.ac.in/moticlubs/swagger |

## Testing

This service additionally requires the following files for running locally:

- `secrets.yml`
- `firebase_private_key.json`

### Run Locally

Execute the service

```shell
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

Packaging jar file

```shell
mvn clean package
```

### Run prod

```shell
mvn clean package
java -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector -XX:+UseSerialGC -XX:MaxRAMPercentage=75.0 -jar MotiClubs-Service.jar --spring.profiles.active=prod
```
