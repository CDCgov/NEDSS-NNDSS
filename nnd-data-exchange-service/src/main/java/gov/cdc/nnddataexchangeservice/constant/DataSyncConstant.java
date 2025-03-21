package gov.cdc.nnddataexchangeservice.constant;

public class DataSyncConstant {
    public static final String DEFAULT_TIME_STAMP = "1753-01-01";
    public static final String GENERIC_PARAM = ":timestamp";    // THIS WILL ALWAYs is :timetamp (just naming thing)
    public static final String LIMIT_PARAM = ":limit";
    public static final String OPERATION = ":operator";
    public static final String START_ROW = ":startRow";
    public static final String END_ROW = ":endRow";
    public static final String GREATER_EQUAL = ">=";
    public static final String GREATER = ">";
    public static final String LESS = "<";
    public static final Integer BYTE_SIZE = 1024;
    public static final String DB_RDB = "RDB";
    public static final String DB_RDB_MODERN = "RDB_MODERN";

    public static final String DB_SRTE = "SRTE";
}
