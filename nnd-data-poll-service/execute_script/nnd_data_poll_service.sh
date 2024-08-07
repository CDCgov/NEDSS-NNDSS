#!/bin/bash
export NBS_NND_CRON="* * * * * *"
export NBS_NND_CRON_TIME_ZONE="UTC"
export NND_DE_CLIENT_ID="nnd-keycloak-client"
export NND_DE_DE="/api/nnd/data-exchange"
export NND_DE_SECRET="bpVTppDam4sxXt4hfgm5hZ6Rteponjb9"
export NND_DE_TOKEN="/api/auth/token"
export NND_DE_URL="http://localhost:8081"
export NND_FILE_LOCATION="/Users/DucNguyen/Desktop/LOG/Poll"
export OP_DBPASSWORD="fake.fake.fake.1234"
export OP_DBSERVER="localhost:1433"
export OP_DBUSER="sa"
export OP_DBNAME="MSGOUTE"
java -jar nnd-data-poll-service.jar
