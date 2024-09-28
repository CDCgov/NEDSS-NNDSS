@echo off
set SINGLE_TIME_POLL = false
set NBS_NND_CRON=0 */1 * * * *
set NBS_NND_CRON_TIME_ZONE=UTC

set NND_POLL_ENABLED =false
set NND_FILE_LOCATION=C:\Users\DucNguyen\Desktop\LOG\Poll
set NND_FULL_LOAD=false
set NND_PULL_LIMIT=1000
set NND_INSERT_LIMIT=1000

set NND_DE_CLIENT_ID=nnd-keycloak-client
set NND_DE_SECRET=bpVTppDam4sxXt4hfgm5hZ6Rteponjb9
set NND_DE_URL=http://localhost:8081

set RDB_POLL_ENABLED=true
set RDB_MODERN_POLL_ENABLED=false
set SRTE_POLL_ENABLED=false
set DATASYNC_STORE_SQL=true
set DATASYNC_STORE_S3=false
set DATASYNC_STORE_LOCAL=false
set DATASYNC_LOCAL_FILE_PATH=/Users/SelvarasuSathiah/Desktop/OUTBOUND_POLL_DATA

set AWS_KEY_ID=NA
set AWS_ACCESS_KEY=NA
set AWS_TOKEN=NA
set S3_BUCKET_NAME=Bucket
set S3_REGION=us-1


set OP_DBSERVER=localhost:1433
set OP_DBNAME=MSGOUTE
set RDB_DBNAME=RDB
set RDB_MODERN_DBNAME=RDB_MODERN
set SRTE_DBNAME=SRTE

set OP_DBPASSWORD=fake.fake.fake.1234
set OP_DBUSER=sa

java -jar nnd-data-poll-service.jar