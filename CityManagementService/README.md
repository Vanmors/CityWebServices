# City REST API with JAX-RS

Для запуска докер образа с Tomcat

[//]: # (```bash)

[//]: # (mvn clean install)

[//]: # (```)

[//]: # ()
[//]: # (```bash)

[//]: # (docker build -t cities .)

[//]: # (docker run -d -p 8080:8080 --name cities-container cities)

[//]: # ()
[//]: # (docker run -p 8080:8080 -p 4848:4848 -v ~/IdeaProjects/CityWebServices/CityManagementService/target/payaradocker:/opt/payara/deployments payara/server-full )

[//]: # (```)


Предварительно нужно закинуть файл city-management-1.0-SNAPSHOT.war в директорию payaradocker, которая лежит в target 
```bash
mvn clean install

docker run -p 8080:8080 -p 4848:4848 -v ~/IdeaProjects/CityWebServices/CityManagementService/target/payaradocker:/opt/payara/deployments payara/server-full

openssl req -x509 -newkey rsa:4096 -keyout city-management.key -out city-management.crt -days 365
openssl pkcs12 -export -in city-management.crt -inkey city-management.key -out city-management.p12 -name city-management
#pass-phrase и пароли - changeit  

docker exec -it citymanagementservice-payara-1 /bin/bash
```
Сервер доступен по урлу: http://localhost:8080/city-management-1.0-SNAPSHOT/api/