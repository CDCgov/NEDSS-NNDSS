package gov.cdc.nnddataexchangeservice.controller;


import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeService;
import gov.cdc.nnddataexchangeservice.service.model.DataExchangeModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataExchangeController {

    private final IDataExchangeService dataExchangeService;

    public DataExchangeController(IDataExchangeService dataExchangeService) {
        this.dataExchangeService = dataExchangeService;
    }

    @GetMapping(path = "/api/nnd/data-exchange")
    public ResponseEntity<DataExchangeModel> exchangingData()  {
        return ResponseEntity.ok(dataExchangeService.getDataForOnPremExchanging());
    }
}
