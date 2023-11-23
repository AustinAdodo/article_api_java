## Important Notes
#### Using Dialects for sql lite with the below configurations in your application.properties may not be suitable for mocking and testing,
#### this is because such in memory databases DO NOT support retrieval of AUTO-INCREMENTED and AUTO-GENERATED ids persisted to sqlite databases.
####   
##### spring.datasource.url=jdbc:sqlite:classpath:datastore.db
##### spring.datasource.driverClassName=org.sqlite.JDBC
##### spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
##### spring.datasource.initialization-mode=always
##### spring.jpa.hibernate.ddl-auto=update
