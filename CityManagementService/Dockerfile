# Используем официальный образ Tomcat
FROM tomcat:9-jdk11-openjdk-slim

# Удаляем дефолтный веб-приложение (например, ROOT) если нужно
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Копируем ваш WAR файл в папку webapps
COPY target/CityManagementService-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/CityManagementService.war
