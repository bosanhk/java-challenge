### Prerequisites
- Java 8 (JDK 1.8)
- Maven

### How to use this spring-boot project

- Install packages with `mvn package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : https://localhost:8443/swagger-ui.html
- H2 UI : https://localhost:8443/h2-console 
  (JDBC URL: jdbc:h2:mem:testdb, login: sa, password: password)


### Bug Fixed
- Unable to update employee
- allow user to create an employee via query parameter

### Enhancements done
- package rearrange
- Add log rotation for each day  
- Use ResponseDto for standard response object.
- Add exception handler for all API to control the response message.
- Add 'username' into Employee for avoiding confusion when multiple persons have same first/last name, which the username must be unique.
- Add JPA validation for entities.
- Use DTO to increase flexibility on the handling of API interfaces and entities.
- Applied SSL with self-signed cert
- Applied JPA second level cache 
- Add Department entity and the corresponding repository, service and controller
- Add MVC tests for all APIs
- Applied API key for all API
- Update the Swagger documentation (title, summary, API header, responses), and remove the default responses.

### TODO
- Encrypt sensitive information before store into the database (e.g. salary)
- Investigate other alternative (e.g. Redis) for JPA second level cache if deploy the application to OpenShift 


## 
#### My experience in Java
- I have 7+ years of Java experience
- I have built API from scratch using both Spring and Spring Boot
- I can design and implement a Spring application with consideration of flexibility, scalability, performance and security. 


