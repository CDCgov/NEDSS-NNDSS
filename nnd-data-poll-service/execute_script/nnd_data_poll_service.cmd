@echo off
set NBS_NND_CRON=* * * * * *;
set NBS_NND_CRON_TIME_ZONE=UTC;
set NND_DE_CLIENT_ID=nnd-keycloak-client;
set NND_DE_DE=/api/nnd/data-exchange;
set NND_DE_SECRET=bpVTppDam4sxXt4hfgm5hZ6Rteponjb9;
set NND_DE_TOKEN=/api/auth/token;
set NND_DE_URL=http://localhost:8081;
set NND_FILE_LOCATION=/Users/DucNguyen/Desktop/LOG/Poll;
set OP_DBPASSWORD=fake.fake.fake.1234;
set OP_DBSERVER=localhost:1433;
set OP_DBUSER=sa
java -jar nnd-data-poll-service.jar
