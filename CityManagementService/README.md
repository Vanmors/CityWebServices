# City REST API with JAX-RS

Для запуска докер образа с Tomcat

```bash
mvn clean install
```

```bash
docker build -t cities .
docker run -d -p 8080:8080 --name cities-container cities
```