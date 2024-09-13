@echo off
set NBS_NND_CRON=0 */1 * * * *
set NBS_NND_CRON_TIME_ZONE=UTC
set NND_FULL_LOAD=false
set NND_PULL_LIMIT=0
set NND_INSERT_LIMIT=1000
set NND_DE_CLIENT_ID=nnd-keycloak-client
set NND_DE_SECRET=bpVTppDam4sxXt4hfgm5hZ6Rteponjb9
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
set DATASYNC_STORE_LOCAL=true
set DATASYNC_STORE_S3=false
set DATASYNC_LOCAL_FILE_PATH=/Users/SelvarasuSathiah/Desktop/OUTBOUND_POLL_DATA
java -jar nnd-data-poll-service.jar