FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Lion Li

RUN mkdir -p /aurora/monitor
RUN mkdir -p /aurora/monitor/logs

WORKDIR /aurora/monitor

EXPOSE 9090

ADD ./target/aurora-monitor-admin.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
