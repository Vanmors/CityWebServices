FROM ubuntu:20.04

ENV DEBIAN_FRONTEND=noninteractive
RUN ln -fs /usr/share/zoneinfo/Europe/Moscow /etc/localtime && \
    echo "Europe/Moscow" > /etc/timezone && \
    apt-get update && apt-get install -y \
    openjdk-17-jdk \
    bash \
    curl \
    unzip \
    tzdata \
    && rm -rf /var/lib/apt/lists/*

# Копируем и распаковываем Mule

#todo мб нужен друой jar

COPY mule-ee-distribution-standalone-4.9.1.zip /opt/


RUN unzip /opt/mule-ee-distribution-standalone-4.9.1.zip -d /opt/ && \
    mv /opt/mule-enterprise-standalone-4.9.1 /opt/mule && \
    rm /opt/mule-ee-distribution-standalone-4.9.1.zip

# Копируем ваше приложение
#COPY ./good-city-web-services-1.0.0-SNAPSHOT-mule-application.jar /opt/mule/apps/

COPY ./web-service-1.0.0-SNAPSHOT-mule-application.jar /opt/mule/apps/
COPY ./log4j2.xml /opt/mule/conf/log4j2.xml


#city-web-service - новейшая версия (мой автосгенеренный)

# Делаем Mule исполняемым и запускаем
RUN chmod +x /opt/mule/bin/mule

#todo мб запустить без "start"

#CMD ["/opt/mule/bin/mule", "start"]

CMD ["/opt/mule/bin/mule"]

# для дебага
#CMD tail -f /dev/null
#docker exec -it esb bash