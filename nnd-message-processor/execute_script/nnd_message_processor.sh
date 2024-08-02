#!/bin/bash
export NBS_NND_CRON="* * * * * *"
export NND_FILE_LOCATION="/Users/DucNguyen/Desktop/LOG"
export NND_PRIOR="true"
export NND_DATE="12/30/2023"
export OP_DBPASSWORD="fake.fake.fake.1234"
export OP_DBSERVER="localhost:1433"
export OP_DBUSER="sa"
java -jar /Users/DucNguyen/Desktop/CDC_Repos/NEDSS-NNDSS/nnd-message-processor/build/libs/nnd-message-processor.jar
