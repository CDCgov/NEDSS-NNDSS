package gov.cdc.nndmessageprocessor.service;

import gov.cdc.nndmessageprocessor.exception.DataProcessorException;
import gov.cdc.nndmessageprocessor.service.interfaces.INETSSTransportQOutService;
import gov.cdc.nndmessageprocessor.service.model.dto.NETSSTransportQOutDto;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NetssCaseService {
    private final INETSSTransportQOutService netssTransportQOutService;

    public NetssCaseService(INETSSTransportQOutService netssTransportQOutService) {
        this.netssTransportQOutService = netssTransportQOutService;
    }

    public void getNetssCases(Short mmwrYear, Short mmwrWeek, Boolean includePriorYear) throws DataProcessorException {

        if (mmwrYear > 2000) {
            mmwrYear = (short) (mmwrYear - 2000); //Netss uses 2 digit mmwr year
        }

        var coll = getNETSSTransportQOutDTCollectionForYear(mmwrYear, mmwrWeek, includePriorYear);

        if (coll.isEmpty()) {
            throw  new DataProcessorException("No Data Found");
        }

        var dedupedCollection = removeDupsFromNetssCollection(coll);
    }


    protected List<NETSSTransportQOutDto> removeDupsFromNetssCollection(List<NETSSTransportQOutDto> netssTransportQOutDtoList) {
        HashMap<String, NETSSTransportQOutDto> netssMsgMap = new HashMap<>();
        for (NETSSTransportQOutDto netssTransportQOutDto : netssTransportQOutDtoList) {
            String payload = netssTransportQOutDto.getPayload();
            if (payload != null && payload.length() >= 12) {
                String caseReportId = payload.substring(6, 12); // CASE REPORT ID
                netssMsgMap.put(caseReportId, netssTransportQOutDto);
            }
        }
        return new ArrayList<>(netssMsgMap.values());
    }


    protected List<NETSSTransportQOutDto> getNETSSTransportQOutDTCollectionForYear(Short mmwrYear, Short mmwrWeek, Boolean includePriorYear) {
        List<NETSSTransportQOutDto> coll = null;
        if (includePriorYear) {
            coll = netssTransportQOutService.getNetssCaseDataYtdAndPriorYear(mmwrYear, mmwrWeek, mmwrYear - 1);
        } else {
            coll = netssTransportQOutService.getNetssCaseDataYtd(mmwrYear, mmwrWeek);
        }
        return coll;
    }

    @Value("${io.finalLocation}")
    private String fileLocation;
    private static boolean processAndWriteNETSSOutputFile(Short mmwrYear, List<NETSSTransportQOutDto> coll) {
        //Get the directory from the property file
        if (propertyUtil.getNETSSOutputFileLocation() == null) {
            System.out.println("NETSS_OUTPUT_FILE_LOCATION not found in NEDSS.properties file. Processing can not continue...");
            return false;
        }
        String filePath = propertyUtil.getNETSSOutputFileLocation();


        File dirToWriteTo = new File(filePath);
        if (!dirToWriteTo.exists())
            dirToWriteTo.mkdirs(); // make the directory if it does not exist

        if (!dirToWriteTo.isDirectory()) {
            logger.error("NETSS_OUTPUT_FILE_LOCATION in NEDSS.properties file is not a directory?");
            System.out.println("NETSS_OUTPUT_FILE_LOCATION in NEDSS.properties file is not a directory?");
            return false;
        }
        File netssFile = new File(dirToWriteTo, NETSSFileName);

        return writeNETSSOutputFile(netssFile, coll, mmwrYear);
    }
}
