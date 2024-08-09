package gov.cdc.nndmessageprocessor.service.interfaces;

import gov.cdc.nndmessageprocessor.exception.DataProcessorException;

public interface INetssCaseService {
    void getNetssCases(Short mmwrYear, Short mmwrWeek, Boolean includePriorYear) throws DataProcessorException;
}
