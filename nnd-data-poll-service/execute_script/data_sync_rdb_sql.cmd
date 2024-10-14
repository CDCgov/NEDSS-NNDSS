@echo off
set SINGLE_TIME_POLL = false
set NBS_RDB_CRON=0 */1 * * * *


set NND_DE_CLIENT_ID=nnd-keycloak-client
set NND_DE_SECRET=bpVTppDam4sxXt4hfgm5hZ6Rteponjb9
set NND_DE_URL=http://localhost:8081

set RDB_POLL_ENABLED=true
set DATASYNC_STORE_SQL=true
set DATASYNC_SQL_ERROR_PATH=/Users/UserName/Desktop/File
set DATASYNC_BATCH_LIMIT=1000
set DATASYNC_DELETE=false


set OP_DBSERVER=localhost:1433
set OP_DBNAME=MSGOUTE
set RDB_DBNAME=RDB
set OP_DBPASSWORD=fake.fake.fake.1234
set OP_DBUSER=sa

java -jar nnd-data-poll-service.jar