package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.service.interfaces.IDataHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.IDataPullService;
import gov.cdc.nnddatapollservice.service.interfaces.IRdbDataHandlingService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataPullService implements IDataPullService {
    private static Logger logger = LoggerFactory.getLogger(DataPullService.class);

    private final IDataHandlingService dataHandlingService;
    private final IRdbDataHandlingService rdbDataHandlingService;
    @Value("${scheduler.cron}")
    private String cron;


    @Value("${scheduler.zone}")
    private String zone;

    public DataPullService(IDataHandlingService dataHandlingService,
                           IRdbDataHandlingService rdbDataHandlingService) {
        this.dataHandlingService = dataHandlingService;
        this.rdbDataHandlingService = rdbDataHandlingService;
    }


    @Scheduled(cron = "${scheduler.cron}", zone = "${scheduler.zone}")
    public void scheduleDataFetch() throws DataPollException {
        logger.info("CRON STARTED");
        logger.info(cron);
        logger.info(zone);
        //dataHandlingService.handlingExchangedData();
    }

    @PostConstruct
    public void syncRdbData() throws DataPollException {
        //Foreign Key tables
//        rdbDataHandlingService.handlingExchangedData("D_ORGANIZATION");//tested
//        rdbDataHandlingService.handlingExchangedData("D_PROVIDER");//tested
//        rdbDataHandlingService.handlingExchangedData("D_CASE_MANAGEMENT");
//        rdbDataHandlingService.handlingExchangedData("D_INTERVIEW");//no record
//        rdbDataHandlingService.handlingExchangedData("D_INV_ADMINISTRATIVE");//tested
//        rdbDataHandlingService.handlingExchangedData("D_INV_EPIDEMIOLOGY");//tested
//        rdbDataHandlingService.handlingExchangedData("D_INV_HIV");//tested
//        rdbDataHandlingService.handlingExchangedData("D_INV_LAB_FINDING");//tested
//        rdbDataHandlingService.handlingExchangedData("D_INV_MEDICAL_HISTORY");//tested
//        rdbDataHandlingService.handlingExchangedData("D_INV_RISK_FACTOR");//tested
//        rdbDataHandlingService.handlingExchangedData("D_INV_TREATMENT");//no record
//        rdbDataHandlingService.handlingExchangedData("D_INV_VACCINATION");//tested
//        rdbDataHandlingService.handlingExchangedData("D_PATIENT");//tested
//        rdbDataHandlingService.handlingExchangedData("F_INTERVIEW_CASE");//no record
//        rdbDataHandlingService.handlingExchangedData("F_PAGE_CASE");//tested
//        rdbDataHandlingService.handlingExchangedData("F_STD_PAGE_CASE");//tested
//        rdbDataHandlingService.handlingExchangedData("F_VAR_PAM");//no record
//        rdbDataHandlingService.handlingExchangedData("CONDITION");//tested
//        rdbDataHandlingService.handlingExchangedData("INVESTIGATION");//tested
//        rdbDataHandlingService.handlingExchangedData("RDB_DATE");//tested
//        rdbDataHandlingService.handlingExchangedData("CONFIRMATION_METHOD");//tested 1
//        rdbDataHandlingService.handlingExchangedData("LDF_GROUP");//not ready.
//        rdbDataHandlingService.handlingExchangedData("HEP_MULTI_VALUE_FIELD_GROUP");//not ready.
//        rdbDataHandlingService.handlingExchangedData("NOTIFICATION");//tested
//        rdbDataHandlingService.handlingExchangedData("PERTUSSIS_SUSPECTED_SOURCE_GRP");//not ready.
//        rdbDataHandlingService.handlingExchangedData("PERTUSSIS_TREATMENT_GROUP");//not ready.

//        rdbDataHandlingService.handlingExchangedData("BMIRD_CASE");//no record
        rdbDataHandlingService.handlingExchangedData("CASE_COUNT");//Error "FK__CASE_COUN__INVES__71D87064". The conflict occurred in database "RDB", table "dbo.D_PROVIDER", column 'PROVIDER_KEY'-foreign key dependency
//        rdbDataHandlingService.handlingExchangedData("CONFIRMATION_METHOD_GROUP");//tested
//        rdbDataHandlingService.handlingExchangedData("GENERIC_CASE");//foreign key dependency LDF_GROUP
//        rdbDataHandlingService.handlingExchangedData("HEPATITIS_CASE");//foreign key HEP_MULTI_VALUE_FIELD_GROUP
//        rdbDataHandlingService.handlingExchangedData("HEPATITIS_DATAMART");//tested
//        rdbDataHandlingService.handlingExchangedData("LDF_DATA");//tested
//        rdbDataHandlingService.handlingExchangedData("LDF_FOODBORNE");//no record
//        rdbDataHandlingService.handlingExchangedData("MEASLES_CASE");//LDF_GROUP dependency
//        rdbDataHandlingService.handlingExchangedData("NOTIFICATION_EVENT");//foreign key NOTIFICATION
//        rdbDataHandlingService.handlingExchangedData("PERTUSSIS_CASE");//foreign key LDF_GROUP, PERTUSSIS_SUSPECTED_SOURCE_GRP, PERTUSSIS_TREATMENT_GROUP
//        rdbDataHandlingService.handlingExchangedData("RUBELLA_CASE");//foreign key LDF_GROUP
//        rdbDataHandlingService.handlingExchangedData("TREATMENT");//no record
//        rdbDataHandlingService.handlingExchangedData("TREATMENT_EVENT");//no record
//        rdbDataHandlingService.handlingExchangedData("VAR_PAM_LDF");//no record
//        List<PollDataSyncConfig> tableList= rdbDataHandlingService.getTableListFromConfig();
//        System.out.println("in data pull service tableList: " + tableList);
//        int i=0;
//        for (PollDataSyncConfig pollDataSyncConfig : tableList) {
//            System.out.println("pollDataSyncConfig: order:"+pollDataSyncConfig.getTableOrder() +"  Table:"+ pollDataSyncConfig.getTableName());
//            if(!pollDataSyncConfig.getTableName().equals("LDF_GROUP")
//                && !pollDataSyncConfig.getTableName().equals("HEP_MULTI_VALUE_FIELD_GROUP")
//                && !pollDataSyncConfig.getTableName().equals("PERTUSSIS_SUSPECTED_SOURCE_GRP")
//                && !pollDataSyncConfig.getTableName().equals("PERTUSSIS_TREATMENT_GROUP")
//                && !pollDataSyncConfig.getTableName().equals("CASE_COUNT")
//                && !pollDataSyncConfig.getTableName().equals("GENERIC_CASE")
//                && !pollDataSyncConfig.getTableName().equals("HEPATITIS_CASE")
//                && !pollDataSyncConfig.getTableName().equals("MEASLES_CASE")
//                && !pollDataSyncConfig.getTableName().equals("NOTIFICATION_EVENT")
//                && !pollDataSyncConfig.getTableName().equals("PERTUSSIS_CASE")
//                && !pollDataSyncConfig.getTableName().equals("RUBELLA_CASE")) {
//
//                rdbDataHandlingService.handlingExchangedData(pollDataSyncConfig.getTableName());
//            }
//        }
    }
}