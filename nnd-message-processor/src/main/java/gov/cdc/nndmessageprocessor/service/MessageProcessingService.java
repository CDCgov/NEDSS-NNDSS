package gov.cdc.nndmessageprocessor.service;

import gov.cdc.nndmessageprocessor.exception.DataProcessorException;
import gov.cdc.nndmessageprocessor.service.interfaces.INetssCaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MessageProcessingService {
    private static Logger logger = LoggerFactory.getLogger(MessageProcessingService.class);

    private final INetssCaseService netssCaseService;
    @Value("${scheduler.cron}")
    private String cron;

    @Value("${scheduler.zone:#{null}}")
    private String zone;

    @Value("${functional.year}")
    private short year;

    @Value("${functional.week}")
    private short week;

    @Value("${functional.prior}")
    private boolean prior;


    public MessageProcessingService(INetssCaseService netssCaseService) {
        this.netssCaseService = netssCaseService;
    }

    @Scheduled(cron = "${scheduler.cron}", zone = "${scheduler.zone:#{T(java.util.TimeZone).getDefault().getID()}}")
    public void scheduleDataFetch() throws DataProcessorException {
        if (zone == null || zone.isEmpty()) {
            zone = java.util.TimeZone.getDefault().getID();
        }
        logger.info("CRON STARTED");
        logger.info("Cron expression: {}", cron);
        logger.info("Time zone: {}", zone);
        netssCaseService.getNetssCases(year, week, prior);
    }


}
