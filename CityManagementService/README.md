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
```
Сервер доступен по урлу: http://localhost:8080/city-management-1.0-SNAPSHOT/api/