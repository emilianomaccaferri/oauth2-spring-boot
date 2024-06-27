# oauth2-spring-boot
An implementation of OAuth2 authentication using Spring Boot

# start everything
* `docker compose up --build` and wait for everything to come online
* navigate to `http://localhost:7777` and create two clients, one for the aggregator and another for normal users
* run database migrations 
* start the aggregator with the credentials you created
* enjoy everything at `http://localhost:8089/grades and /students`
