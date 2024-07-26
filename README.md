## NND Overview
This repository houses two NND services; NND dataexchange and NND data poll.
These 2 services support the outbound processes and provide support for the STLT to pull transport data from CDC cloud hosted NBS database via HTTPs endpoint and The Poller serivce allow the STLT to periodically calls the HTTPs endpoint to pull the data on the configurable scheduler.
This architecture ensure the STLT can make a secure connection to CDC database without depending on third party tool such as Rhapsody.


## Services Overview
- Data Exchange API Service Requirement
    - Java 17
    - Keycloak for security
- Data Poller Service Requirement
    - Java 17

## Environment variable
- Data Exchange API Service ENV:
    - NBS_DBPASSWORD: DB info
    - NBS_DBSERVER: DB info
    - NBS_DBUSER: DB info
    - NND_AUTH_URI: Keycloak info
- Data Poller Service ENV:
    - NND_DE_CLIENT_ID: Keycloak client ID
    - NND_DE_SECRET: Keycloak client Secret
    - NND_DE_DE: Data Exchange Endpoint (ex: /api/nnd/data-exchange)
    - NND_DE_TOKEN: Data Exchange Endpoint (ex: /api/auth/token)
    - NND_DE_URL: Data Exchange Url (ex: localhost)
    - NND_FILE_LOCATION: Path to log directory on the setup machine (ex: /Users/UserName/Desktop/LOG)
    - OP_DBPASSWORD: On Prem DB info
    - OP_DBSERVER: On Prem DB info
    - OP_DBUSER: On Prem DB info
    - NBS_NND_CRON: Cron Scheduler (ex: * * * * * *)
    - NBS_NND_CRON_TIME_ZONE: Cron timezone (ex: UTC)

## Useful command to run Poller jar file
The two enviroment variable the end user will need to explicitly set when running the poller service is the CRON configuration (NBS_NND_CRON (schedule) and NBS_NND_CRON_TIME_ZONE (timezone))
- Mac
    - export ENV_VAR=value
    - java -jar build/libs/nnd-data-poll.jar
- Linux
    -  export ENV_VAR=value
    -  java -jar build/libs/nnd-data-poll.jar
- Windows
    - User can explicitly set the ENV in environment configuration or it also can be on terminal as follow
    - set ENV=value
    - java -jar build/libs/nnd-data-poll.jar
- Java command
    - java -Denv=value -jar build/libs/nnd-data-poll.jar