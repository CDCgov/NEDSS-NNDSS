#!/bin/bash
export NBS_NND_CRON="* * * * * *"
export NBS_NND_CRON_TIME_ZONE="UTC"
export NND_FULL_LOAD="false"
export NND_PULL_LIMIT="0"
export NND_INSERT_LIMIT="1000"
export NND_DE_CLIENT_ID="nnd-keycloak-client"
export NND_DE_SECRET="bpVTppDam4sxXt4hfgm5hZ6Rteponjb9"
export NND_DE_URL="http://localhost:8081"
export NND_FILE_LOCATION="/Users/DucNguyen/Desktop/LOG/Poll"
export NND_POLL_ENABLED="true"
export RDB_POLL_ENABLED="true"
export OP_DBPASSWORD="fake.fake.fake.1234"
export OP_DBSERVER="localhost:1433"
export OP_DBUSER="sa"
export OP_DBNAME="MSGOUTE"
export RDB_DBNAME="RDB"
export SRTE_DBNAME=SRTE
java -jar nnd-data-poll-service.jar
