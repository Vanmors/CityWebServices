#FROM payara/server-full
#
#COPY CityManagementService/target/city-management-1.0-SNAPSHOT.jar /opt/payara/deployments
#COPY CityWebService/target/CityWebService-1.0-SNAPSHOT.war /opt/payara/deployments
#
##COPY tickets-ejb/build/libs/tickets-ejb-1.jar /opt/jboss/wildfly/standalone/deployments/tickets-ejb.jar
##COPY tickets-web-service/build/libs/tickets-web-service-1.war /opt/jboss/wildfly/standalone/deployments/tickets-service.war


# Используем базовый образ Ubuntu
FROM ubuntu:20.04

# Обновляем пакеты и устанавливаем необходимые
# Отключаем интерактивный режим и задаем таймзону
ENV DEBIAN_FRONTEND=noninteractive
RUN ln -fs /usr/share/zoneinfo/Europe/Moscow /etc/localtime && \
    echo "Europe/Moscow" > /etc/timezone && \
    apt-get update && apt-get install -y \
    postgresql \
    postgresql-client \
    postgresql-contrib \
    openjdk-11-jdk \
    bash \
    curl \
    unzip \
    tzdata \
    && rm -rf /var/lib/apt/lists/*







# Устанавливаем Consul
COPY deploy_sources/consul_1.14.4_linux_amd64.zip /tmp/consul.zip
# Устанавливаем Consul
RUN unzip /tmp/consul.zip -d /usr/local/bin/ \
    && rm -f /tmp/consul.zip \
    && chmod +x /usr/local/bin/consul






COPY deploy_sources/payara6 /opt/payara


# Устанавливаем права на выполнение
RUN chmod +x /opt/payara/bin/asadmin

RUN mkdir -p /opt/payara/deployments

# Копируем .war и .jar файлы
COPY CityManagementService/target/city-management-1.0-SNAPSHOT.jar /opt/payara/deployments/
COPY CityWebService/target/CityWebService-1.0-SNAPSHOT.war /opt/payara/deployments/

# Копируем скрипт запуска
COPY start_services.sh /start_services.sh
RUN chmod +x /start_services.sh

# Экспонируем порты
EXPOSE 5432 8080 4848

#CMD tail -f /dev/null

# Выполняем скрипт старта
CMD ["/start_services.sh"]
