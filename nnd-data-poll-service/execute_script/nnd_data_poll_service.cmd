@echo off
set NBS_NND_CRON=0 */1 * * * *
set NBS_NND_CRON_TIME_ZONE=UTC
set NND_FULL_LOAD=false
set NND_PULL_LIMIT=0
set NND_INSERT_LIMIT=1000
set NND_DE_CLIENT_ID=nnd-keycloak-client
set NND_DE_SECRET=bpVTppDam4sxXt4hfgm5hZ6Rteponjb9
@REM set NND_DE_TOKEN=/api/auth/token
@REM set NND_DE_DE=/api/nnd/data-exchange
@REM set NND_DE_GENERIC=/api/data-exchange-generic
set NND_DE_URL=http://localhost:8081
set NND_FILE_LOCATION=C:\Users\DucNguyen\Desktop\LOG\Poll
set NND_POLL_ENABLED=true
set RDB_POLL_ENABLED=true
set OP_DBPASSWORD=fake.fake.fake.1234
set OP_DBSERVER=localhost:1433
set OP_DBUSER=sa
set OP_DBNAME=MSGOUTE
set RDB_DBNAME=RDB
set SRTE_DBNAME=SRTE
java -jar nnd-data-poll-service.jar