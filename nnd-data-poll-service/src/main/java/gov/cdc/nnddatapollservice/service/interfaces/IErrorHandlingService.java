package gov.cdc.nnddatapollservice.service.interfaces;

import java.util.List;

public interface IErrorHandlingService {
    <T> void dumpBatchToFile(List<T> batch, String fileName, String fileLocation) ;
}
