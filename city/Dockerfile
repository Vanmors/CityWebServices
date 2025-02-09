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
    bash \
    curl \
    unzip \
    tzdata \
    && rm -rf /var/lib/apt/lists/*


RUN curl -L https://github.com/payara/Payara/archive/refs/tags/payara-server-6.2025.1.tar.gz -o payara.tar.gz \
    && mkdir -p /opt/payara \
    && tar -xzf payara.tar.gz -C /opt/payara \
    && rm -f payara.tar.gz \
    && ls -R /opt/payara



RUN ls -R /opt/payara

# Устанавливаем права на выполнение
RUN chmod +x /opt/payara/payara5/bin/asadmin

# Копируем .war и .jar файлы
COPY CityManagementService/target/city-management-1.0-SNAPSHOT.jar /opt/payara/payara5/deployments/
COPY CityWebService/target/CityWebService-1.0-SNAPSHOT.war /opt/payara/payara5/deployments/

# Копируем скрипт запуска
COPY start_services.sh /start_services.sh
RUN chmod +x /start_services.sh

# Экспонируем порты
EXPOSE 5432 8080 4848

# Выполняем скрипт старта
CMD ["/start_services.sh"]
