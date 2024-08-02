@echo off
set NBS_NND_CRON=* * * * * *
set NND_FILE_LOCATION=/Users/DucNguyen/Desktop/LOG
set NND_PRIOR=true
set NND_DATE=12/30/2023
set OP_DBPASSWORD=fake.fake.fake.1234
set OP_DBSERVER=localhost:1433
set OP_DBUSER=sa
java -jar nnd-message-processor.jar
