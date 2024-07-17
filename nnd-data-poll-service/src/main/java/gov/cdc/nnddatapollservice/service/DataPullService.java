package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.service.interfaces.IDataHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.IDataPullService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DataPullService implements IDataPullService {
    private final IDataHandlingService dataHandlingService;

    public DataPullService(IDataHandlingService dataHandlingService) {
        this.dataHandlingService = dataHandlingService;
    }


    // Use the cron expression to schedule the task with a specific time zone
    @Scheduled(cron = "${scheduler.cron}", zone = "${scheduler.zone}")
    public void scheduleDataFetch() {
        dataHandlingService.handlingExchangedData();
    }
}
