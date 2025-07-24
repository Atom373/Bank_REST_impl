# Bank REST API

This project is a RESTful API for basic bank functionality, supporting both admin and user roles.

## Features

### Admin capabilities

- Manage users (create, delete, view details)
- Manage bank cards (create, delete, view details, block/unblock)

### Regular user capabilities

- View owned cards
- Request card blocking
- Transfer funds between owned cards
- View sensitive card information

## Security

To run the application securely, set the `BC_ADMIN_PASSWORD` environment variable.  
If this variable is not set, the default admin password will be `12345`.

## How to run

To start the application locally using Docker:

```bash
git clone https://github.com/Atom373/Bank_REST_impl.git
cd Bank_REST_impl
docker-compose up
```

Now you can access the swagger via this [link](http://localhost:8090/swagger-ui/index.html)

## Technologies used

- Java 17
- Spring Boot, MVC, Data JPA, Security
- Liquibase
- Postgresql (for user data)
- Swagger/OpenAPI
- Docker