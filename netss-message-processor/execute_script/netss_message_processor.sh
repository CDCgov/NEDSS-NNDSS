#!/bin/bash
export NETSS_FILE_LOCATION="/Users/DucNguyen/Desktop/LOG"
export NETSS_PRIOR="true"
export NETSS_DATE="12/30/2023"
export OP_DBPASSWORD="fake.fake.fake.1234"
export OP_DBSERVER="localhost:1433"
export OP_DBUSER="sa"
export OP_DBNAME="MSGOUTE"
java -jar netss-message-processor.jar
