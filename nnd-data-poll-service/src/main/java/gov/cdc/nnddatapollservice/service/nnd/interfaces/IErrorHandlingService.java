package gov.cdc.nnddatapollservice.service.nnd.interfaces;

import java.util.List;

public interface IErrorHandlingService {
    <T> void dumpBatchToFile(List<T> batch, String fileName, String fileLocation) ;
}
