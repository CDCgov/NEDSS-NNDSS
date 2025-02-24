
## Environment variable
- Data Poll Service ENV:
  - Share Process:
    - SINGLE_TIME_POLL: (optional) default is false, can be set to true. Forcing the application to exist the process after it executed
    - TIME_ZONE: (optional) Time zone for CRON process, default is UTC
    - NBS_NND_CRON: (optional) NND CRON scheduler, default is 0 */1 * * * * -> every one minutes
    - NBS_DATA_CRON: same as above but for RDB CRON
  - NND Process
    - NND_POLL_ENABLED: (optional) default is false, must set to true to enable the NND POLL  
    - NND_FILE_LOCATION: Log location for NND process (ex: /Users/UserName/Desktop/LOG)
    - NND_FULL_LOAD: (optional) nnd full loading check, set to true if needed. Default is false 
    - NND_PULL_LIMIT: (optional) nnd api fetch capacity, default is 0, meaning its pulling any data available 
    - NND_INSERT_LIMIT: (optional) nnd poll insertion capacity, default is 1000, meaning the poll service will try to insert 1000 records per transaction into DB
  - API
    - NND_DE_CLIENT_ID: Data Sync API Client Id
    - NND_DE_SECRET: Data Sync API Secret
    - NND_DE_URL: Data Sync API Url
    - NND_DE_TOKEN/NND_DE_DE/NND_DE_GENERIC/NND_DE_GENERIC_TOTAL_RECORD: Data Sync Endpoints (Optional; already hardcoded in the app config)
  - DATA SYNC Process
    - RDB_POLL_ENABLED: (optional) default is false, set to true to enable RDB POLL
    - DATASYNC_STORE_SQL: (optional) default is false, set to true to enable Polling into SQL DB
    - DATASYNC_STORE_S3: (optional) default is false, set to true to enable Polling into S3
    - DATASYNC_STORE_LOCAL: (optional) default is false, set to true to enable Polling into Local System Directory
    - DATASYNC_LOCAL_FILE_PATH: Support for DATASYNC_STORE_LOCAL, value is path to local system directory (ex: /Users/UserName/Desktop/File)
    - DATASYNC_SQL_ERROR_PATH: Error path for Datasync, value is path to local system directory (ex: /Users/UserName/Desktop/File)
    - DATASYNC_BATCH_LIMIT: Data sync batch pull limiter, default is 1000; meaning each request it call out to APIs will pull maximum of 1000 records
    - DATASYNC_DELETE: Default is false, set this to true to allow data poll to clean up local table before proceed
  - AWS
    - AWS_KEY_ID: (optional) default is NA
    - AWS_ACCESS_KEY: (optional) default is NA
`  `- AWS_TOKEN: (optional) default is NA
    - AWS_PROFILE:  (optional) default is NA, this is a path to default profile on your machine. If choosing to authenticate aws this way then we dont need to set value for AWS ID/KEY/TOKEN
    - S3_BUCKET_NAME: (optional) default is NA, must a valid bucket name 
    - S3_REGION: (optional) default is NA, region associate with the aws account
  - Client Database
    - OP_DBSERVER: server url
    - OP_DBNAME: NND DB name
    - DATASYNC_DBNAME: RDB DB name
    - OP_DBUSER: DB Username
    - OP_DBPASSWORD: DB Password

## CRON Guide
- Ex: Setting up the scheduler time.
 - "*/1 * * * * ?" => For 1 second
 - "0 */1 * * * ?" => For 1 minute
 - "0 0 */1 * * ?" => For 1 hour
 - "0 0 0 */1 * ?" => For every day
 - "0 0 0 1 1 *" => It scheduled as 1 date of 1st month(january) every year