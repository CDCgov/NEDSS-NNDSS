@echo off
set NETSS_FILE_LOCATION=/Users/DucNguyen/Desktop/LOG
set NETSS_PRIOR=true
set NETSS_DATE=12/30/2023
set OP_DBPASSWORD=fake.fake.fake.1234
set OP_DBSERVER=localhost:1433
set OP_DBUSER=sa
set OP_DBNAME=MSGOUTE
java -jar netss-message-processor.jar
