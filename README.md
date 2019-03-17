# Fontys JEA application (SongWiki)

## :hammer_and_pick: Tools
The following tools are used for the implementation of SongWiki:

- Arquillian (integration testing framework)
- Payara (application server)
- Maven shrinkwrap resolver (only including necessary code during integration tests)
- Jersey (RESTful Web Services framework with JAX-RS API implementation)
- Hibernate (ORM tool)
- Java EE
- GSON (easy JSON serialization/deserialization)
- Maven Cargo (maven deployments of application server)

## :wrench: Configuration application server
This project uses Payara as application server.  
As Payara is based on Glassfish a lot of configuration is interchangeable.

### :zap: Application
The current setup for running the application looks like the following:

Resources -> JDBC -> JDBC Resources:
* JNDI name: jdbc/JEAPostgreSQLPool

Resources -> JDBC -> JDBC Connection Pools:
- General:
    * Pool name: JEAPostgreSQLPool
    * Resource type: javax.sql.ConnectionPoolDataSource
    * Datasource Classname: org.postgresql.ds.PGConnectionPoolDataSource
    * Additional default settings
    
- Additional Properties:
    * password: your_database_password_here
    * serverName: localhost
    * user: postgres
    * databaseName: postgres

### :heavy_check_mark: Testing
This application is setup to have a managed Payara Server running when tests are executed.
A production environment is mimicked as much as possible.

Arquillian is used for the integration tests on the managed server as it's the most widely used and battle-tested 
integration testing framework. Its configuration can be found in the `test/resources/` directory.

The Payara server is set up to use a different connection pool, connection resource and database for testing.  
Doing this makes sure the tests won't interfere with the production environment and can be tweaked when necessary.  
These are the settings I use:

Resources -> JDBC -> JDBC Resources:
* JNDI name: jdbc/JEAPostgreSQLTestPool

Resources -> JDBC -> JDBC Connection Pools:
- General:
    * Pool name: JEAPostgreSQLTestPool
    * Resource type: javax.sql.ConnectionPoolDataSource
    * Datasource Classname: org.postgresql.ds.PGConnectionPoolDataSource
    * Additional default settings
    
- Additional Properties:
    * password: your_database_password_here
    * serverName: localhost
    * user: postgres
    * databaseName: postgres_test

:warning: take note of the arquillian.xml file in `test/resources/`.  
The `glassFishHome` property should be set to the local copy of Payara to run the tests with!  
Currenty, the Payara server is expected to live one directory above the source code (denoted by the `../`).  
If this is not set correctly the application won't find the server to be deployed to and therefor won't run!

### :shield: Security  
#### Application  
The main application is secured using JAAS (Java Authentication and Authorization Service).  
This maps the username and password inserted in the login form to the account and role tables in the database.

The following files play a role in the security aspect (location starting from src/main/webapp):
* `META-INF/glassfish-web.xml` for mapping of the application roles to the database roles
* `META-INF/web.xml`Realm configuration specified in the application server and directory/file constraints
* `login.xhtml` the login page to redirect to if the principal doesn't have the required permissions
* `loginError.xhtml` the page to redirect to when the form details don't match up

To make JAAS work there are a few necessary Payara settings.  
A new realm with a name of 'SongWikiRealm' has to be created at Configurations -> server-config -> Security -> Realms.  
This realm is configured with the following values:

| Property          | Value                                        |
|-------------------|----------------------------------------------|
| JAAS Context      | jdbcRealm                                    |
| JNDI              | jdbc/JEAPostgreSQLPool                       |
| User Table        | account                                      |
| User Name Column  | username                                     |
| Password Column   | password                                     |
| Group Table       | account (the role is saved as an enum value) |
| Group Name Column | role                                         |

The default password hashing expected by Payara is SHA-265.  
This is provided by Google's Guava dependency by default.

#### REST endpoints  
The REST endpoints are secured using JWT ([JSON Web Tokens](https://jwt.io/)).

## :round_pushpin: Endpoint must-knowns
Endpoint classes need to manually throw exceptions when an error occurs.  
If not thrown, Jersey returns status codes in the 200 range.  
For example, 204 is returned when an object is null instead of the expected 404.  
[Reference](https://stackoverflow.com/a/22869076)

## References
These are several crucial resources I came to depend on during the creation of this application:

- [Hibernate cascade types with examples](https://vladmihalcea.com/a-beginners-guide-to-jpa-and-hibernate-cascade-types/)
- [Hibernate one to many relationships](https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/)
- [JAAS configuration video](https://www.youtube.com/watch?v=1xsU6juUZd0)