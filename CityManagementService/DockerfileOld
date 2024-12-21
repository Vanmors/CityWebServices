# # Используем официальный образ Tomcat
# FROM tomcat:9-jdk11-openjdk-slim
#
# # Удаляем дефолтный веб-приложение (например, ROOT) если нужно
# RUN rm -rf /usr/local/tomcat/webapps/ROOT
#
# # Копируем ваш WAR файл в папку webapps
# COPY target2/CityManagementService-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/CityManagementService.war


# Base image with Payara
FROM payara/server-full:6.2023.4

# Copy WAR file to Payara deployment directory
#COPY target/CityManagementService-1.0-SNAPSHOT.war /opt/payara/deployments/
COPY target2/booking_service.war /opt/payara/deployments/

# Expose HTTP port
EXPOSE 8080

# Start the server
CMD ["asadmin", "start-domain", "--verbose"]
