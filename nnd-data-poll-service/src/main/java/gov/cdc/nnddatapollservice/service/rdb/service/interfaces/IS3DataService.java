package gov.cdc.nnddatapollservice.service.rdb.service.interfaces;

import java.util.List;

public interface IS3DataService {
    void persistToS3(List<String> records, String fileName);
}
