FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Lion Li

RUN mkdir -p /aurora/server
RUN mkdir -p /aurora/server/logs
RUN mkdir -p /aurora/server/temp

WORKDIR /aurora/server

EXPOSE 8080

ADD ./target/aurora-admin.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
