FROM amazoncorretto:11
MAINTAINER @Germán Kronberg

RUN mkdir /code
COPY build/libs /code

ENTRYPOINT [ "sh", "-c", "java -jar -Duser.timezone=$TIMEZONE -Dnetworkaddress.cache.ttl=60 -Dnetworkaddress.cache.negative.ttl=10 /code/*.jar" ]