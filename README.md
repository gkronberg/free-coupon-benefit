# FREE-COUPON-BENEFIT

1. [Built With](#built-with)
2. [How To Build](#how-to-build)
3. [How To Run](#how-to-run)
4. [Management](#management)
5. [Swagger](#swagger)

## Built With
- Spring Framework & Spring Boot - Web Framework
- Gradle
- Docker - Container Framework

## How To Build

Install docker (and docker-compose if they are not bundled together).

Later, to build without running tests:
```
./gradlew -x test build
```

To run tests:
```
docker-compose -f docker-compose-local.yml up -d
./gradlew clean build --no-build-cache
```

(docker compose takes care of creating all the dependencies needed to run tests)

For turning down the Docker env:
```
docker-compose -f docker-compose-local.yml down
```

## How To Run

```
java -jar ./build/libs/free-coupon-benefit-0.0.1-SNAPSHOT.jar
```

## Management
* http://localhost:9091/free-coupon-benefit/actuator
* http://localhost:9091/free-coupon-benefit/actuator/health
* http://localhost:9091/free-coupon-benefit/actuator/metrics

## Swagger
* http://localhost:9091/swagger-ui.html
* http://localhost:9091/free-coupon-benefit/api-docs