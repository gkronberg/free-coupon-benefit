# FREE-COUPON-BENEFIT

This service processes payment transactions and communication with external gateways.

1. [Built With](#built-with)
2. [How To Build](#how-to-build)
3. [Management](#anagement)

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
./gradlew build
```

(docker compose takes care of creating all the dependencies needed to run tests)

And to run Jacoco Test Reports:

```
./gradlew jacocoTestReport
```
Reports will be saved on ${project_home}/build/jacocoHtml/index.html


For turning down the Docker env:
```
docker-compose down
```

## Management
* http://localhost:9090/free-coupon-benefit/health
* http://localhost:9090/free-coupon-benefit/metrics
* http://localhost:9090/free-coupon-benefit/actuator/info
