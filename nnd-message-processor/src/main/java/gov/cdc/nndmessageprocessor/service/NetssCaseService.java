package gov.cdc.nndmessageprocessor.service;

import gov.cdc.nndmessageprocessor.exception.DataProcessorException;
import gov.cdc.nndmessageprocessor.service.interfaces.INETSSTransportQOutService;
import gov.cdc.nndmessageprocessor.service.interfaces.INetssCaseService;
import gov.cdc.nndmessageprocessor.service.model.dto.NETSSTransportQOutDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NetssCaseService implements INetssCaseService {
    private static Logger logger = LoggerFactory.getLogger(NetssCaseService.class);

    private final INETSSTransportQOutService netssTransportQOutService;
    @Value("${io.fileLocation}")
    private String fileLocation;
    private static final String NETSSFileName = "STDNETSS.dat";  // output filename per requirements

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
        var written = processAndWriteNETSSOutputFile(mmwrYear, dedupedCollection);

        if (written) {
            logger.info("OK");
        } else {
            logger.info("FAILED");
        }
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


    protected List<NETSSTransportQOutDto> getNETSSTransportQOutDTCollectionForYear(Short mmwrYear, Short mmwrWeek, Boolean includePriorYear) throws DataProcessorException {
        List<NETSSTransportQOutDto> coll = null;
        if (includePriorYear) {
            coll = netssTransportQOutService.getNetssCaseDataYtdAndPriorYear(mmwrYear, mmwrWeek, mmwrYear - 1);
        } else {
            coll = netssTransportQOutService.getNetssCaseDataYtd(mmwrYear, mmwrWeek);
        }
        return coll;
    }


    protected boolean processAndWriteNETSSOutputFile(Short mmwrYear, List<NETSSTransportQOutDto> coll) {
        File dirToWriteTo = new File(fileLocation);
        if (!dirToWriteTo.exists() && !dirToWriteTo.mkdirs()) {
            System.out.println("Failed to create directory: " + fileLocation);
            return false;
        }

        if (!dirToWriteTo.isDirectory()) {
            System.out.println(fileLocation + " is not a directory.");
            return false;
        }

        File netssFile = new File(dirToWriteTo, NETSSFileName);
        return writeNETSSOutputFile(netssFile, coll, mmwrYear);
    }

    protected boolean writeNETSSOutputFile(File netssFile, List<NETSSTransportQOutDto> coll, Short mmwrYear) {
        int totalRecordsWritten = 0;
        int verificationRecordsWritten = 0;
        Map<String, Integer> countersMap = new HashMap<>();
        String formattedCount;
        String blanks44 = "                                            ";
        String stateCCD = null;

        try (PrintWriter netssWriter = new PrintWriter(new FileWriter(netssFile, false))) { // overwrite existing
            if (mmwrYear > 2000) {
                mmwrYear = (short) (mmwrYear - 2000);
            }

            for (NETSSTransportQOutDto netssTransportQOutDT : coll) {
                String thisConditionCd = netssTransportQOutDT.getEvent();
                if (thisConditionCd != null) {
                    if (stateCCD == null) {
                        stateCCD = netssTransportQOutDT.getPayload().substring(2, 4);
                    }
                    netssWriter.println(netssTransportQOutDT.getPayload());
                    totalRecordsWritten++;

                    countersMap.merge(thisConditionCd, 1, Integer::sum);
                }
            }

            for (Map.Entry<String, Integer> entry : countersMap.entrySet()) {
                formattedCount = String.format("%05d", entry.getValue());
                netssWriter.println("V" + stateCCD + entry.getKey() + formattedCount + mmwrYear + blanks44);
                verificationRecordsWritten++;
            }

        } catch (IOException e) {
            return false;
        }

        if (totalRecordsWritten > 0) {
            return true;
        } else {
            return false;
        }
    }
}
