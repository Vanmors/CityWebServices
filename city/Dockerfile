#FROM payara/server-full
#
#COPY CityManagementService/target/city-management-1.0-SNAPSHOT.jar /opt/payara/deployments
#COPY CityWebService/target/CityWebService-1.0-SNAPSHOT.war /opt/payara/deployments
#
##COPY tickets-ejb/build/libs/tickets-ejb-1.jar /opt/jboss/wildfly/standalone/deployments/tickets-ejb.jar
##COPY tickets-web-service/build/libs/tickets-web-service-1.war /opt/jboss/wildfly/standalone/deployments/tickets-service.war


# Используем базовый образ с Ubuntu
FROM ubuntu:20.04

# Обновляем пакеты и устанавливаем необходимые
RUN apt-get update && apt-get install -y \
    postgresql \
    payara-server \
    postgresql-client \
    bash \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Копируем файлы .war и .jar в соответствующие директории Payara
COPY CityManagementService/target/city-management-1.0-SNAPSHOT.jar /opt/payara/deployments/
COPY CityWebService/target/CityWebService-1.0-SNAPSHOT.war /opt/payara/deployments/

# Копируем скрипты для старта сервисов
COPY start_services.sh /start_services.sh
RUN chmod +x /start_services.sh

# Экспонируем порты
EXPOSE 5432 8080 4848

# Выполнение скрипта старта при запуске контейнера
CMD ["/start_services.sh"]
