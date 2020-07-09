# prm-api
Process Reliability Monitor (PRM) - API
-
This application  will provide a common API for the PRM application to collect and 
report historical and performance data. A postgresSQL database (prm) will hold the 
configuration and historical data. 
It is written with the Spring Framework which utilizes Spring boot and Spring Data JDBC. 
It applies MVC principles in Java using the Spring Framework (JDBC) (run on a Tomcat Server). 

#Installation
Create a local PostgresSQL database "prm", username=postgres load in the proper tables using /database/prmDBLoad.sql

