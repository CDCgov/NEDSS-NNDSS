package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.interfaces.IDataHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.IDataPullService;
import gov.cdc.nnddatapollservice.service.interfaces.IRdbDataHandlingService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
    //@PostConstruct
    public void syncRdbData() throws DataPollException {
        //Foreign Key tables
        rdbDataHandlingService.handlingExchangedData("D_ORGANIZATION");
        rdbDataHandlingService.handlingExchangedData("D_PROVIDER");
        rdbDataHandlingService.handlingExchangedData("D_CASE_MANAGEMENT");
        rdbDataHandlingService.handlingExchangedData("D_INTERVIEW");
        rdbDataHandlingService.handlingExchangedData("D_INV_ADMINISTRATIVE");
        rdbDataHandlingService.handlingExchangedData("D_INV_EPIDEMIOLOGY");
        rdbDataHandlingService.handlingExchangedData("D_INV_HIV");
        rdbDataHandlingService.handlingExchangedData("D_INV_LAB_FINDING");
        rdbDataHandlingService.handlingExchangedData("D_INV_MEDICAL_HISTORY");
        rdbDataHandlingService.handlingExchangedData("D_INV_RISK_FACTOR");
        rdbDataHandlingService.handlingExchangedData("D_INV_TREATMENT");
        rdbDataHandlingService.handlingExchangedData("D_INV_VACCINATION");
        rdbDataHandlingService.handlingExchangedData("D_PATIENT");
        rdbDataHandlingService.handlingExchangedData("F_INTERVIEW_CASE");
        rdbDataHandlingService.handlingExchangedData("F_PAGE_CASE");
        rdbDataHandlingService.handlingExchangedData("F_STD_PAGE_CASE");
        rdbDataHandlingService.handlingExchangedData("F_VAR_PAM");
        rdbDataHandlingService.handlingExchangedData("CONDITION");
        rdbDataHandlingService.handlingExchangedData("INVESTIGATION");
        rdbDataHandlingService.handlingExchangedData("RDB_DATE");
        rdbDataHandlingService.handlingExchangedData("CONFIRMATION_METHOD");
        rdbDataHandlingService.handlingExchangedData("LDF_GROUP");
        rdbDataHandlingService.handlingExchangedData("HEP_MULTI_VALUE_FIELD_GROUP");
        rdbDataHandlingService.handlingExchangedData("NOTIFICATION");
        rdbDataHandlingService.handlingExchangedData("PERTUSSIS_SUSPECTED_SOURCE_GRP");
        rdbDataHandlingService.handlingExchangedData("PERTUSSIS_TREATMENT_GROUP");
        // Foreign keys ends
        rdbDataHandlingService.handlingExchangedData("BMIRD_CASE");
        rdbDataHandlingService.handlingExchangedData("CASE_COUNT");
        rdbDataHandlingService.handlingExchangedData("CONFIRMATION_METHOD_GROUP");
        rdbDataHandlingService.handlingExchangedData("GENERIC_CASE");
        rdbDataHandlingService.handlingExchangedData("HEPATITIS_CASE");
        rdbDataHandlingService.handlingExchangedData("HEPATITIS_DATAMART");
        rdbDataHandlingService.handlingExchangedData("LDF_DATA");
        rdbDataHandlingService.handlingExchangedData("LDF_FOODBORNE");
        rdbDataHandlingService.handlingExchangedData("MEASLES_CASE");
        rdbDataHandlingService.handlingExchangedData("NOTIFICATION_EVENT");
        rdbDataHandlingService.handlingExchangedData("PERTUSSIS_CASE");
        rdbDataHandlingService.handlingExchangedData("RUBELLA_CASE");
        rdbDataHandlingService.handlingExchangedData("TREATMENT");
        rdbDataHandlingService.handlingExchangedData("TREATMENT_EVENT");
        rdbDataHandlingService.handlingExchangedData("VAR_PAM_LDF");
    }
}
