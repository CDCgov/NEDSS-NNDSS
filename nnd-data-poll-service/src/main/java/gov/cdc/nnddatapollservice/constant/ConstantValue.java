package gov.cdc.nnddatapollservice.constant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConstantValue {
    public static final String LOG_SUCCESS = "SUCCESS";
    public static final String RDB_MODERN = "RDB_MODERN";
    public static final String ODSE_OBS = "ODSE_OBS";
    public static final String COVID_DATAMART = "COVID_DATAMART";
    public static final String NBS_ODSE_EDX = "ODSE_EDX";
    public static final String RDB = "RDB";
    public static final String SRTE = "SRTE";
    public static final String SQL_LOG = "SQL Log: ";
    public static final String S3_LOG = "S3 Log: ";
    public static final String LOCAL_DIR_LOG = "Local Directory Log: " ;
    public static final String API_LEVEL = "APIs Level Log: " ;
    public static final String CRITICAL_NULL_LOG = "Critical Null Log: ";
    public static final String CRITICAL_COUNT_LOG = "Critical Count Log: ";

    public static final String CRITICAL_NON_NULL_LOG = "Critical Non Null Log: ";
    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";
    public static final String WARNING = "WARNING";

    public static final Set<String> SPECIAL_TABLES = new HashSet<>(Arrays.asList(
            "D_INV_HIV",
            "D_INV_ADMINISTRATIVE",
            "D_INV_EPIDEMIOLOGY",
            "D_INV_LAB_FINDING",
            "D_INV_MEDICAL_HISTORY",
            "D_INV_RISK_FACTOR",
            "D_INV_TREATMENT",
            "D_INV_VACCINATION"
    ));

    public static final Set<String> SPECIAL_ODSE_OBS_TABLES = new HashSet<>(Arrays.asList(
            "PERSON",
            "PARTICIPATION"
    ));


    public static final Set<String> ODSE_OBS_TABLES = new HashSet<>(Arrays.asList(
            "ENTITY",
            "ROLE",
            "PERSON",
            "ACT",
            "OBSERVATION",
            "PARTICIPATION",
            "ACT_RELATIONSHIP"
    ));


}
