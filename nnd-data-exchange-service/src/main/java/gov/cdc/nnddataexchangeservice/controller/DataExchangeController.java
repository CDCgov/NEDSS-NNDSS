package gov.cdc.nnddataexchangeservice.controller;


import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeService;
import gov.cdc.nnddataexchangeservice.service.model.DataExchangeModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "bearer-key")
public class DataExchangeController {

    private final IDataExchangeService dataExchangeService;

    public DataExchangeController(IDataExchangeService dataExchangeService) {
        this.dataExchangeService = dataExchangeService;
    }

    @Operation(
            summary = "Getting data from NND",
            description = "Getting data from NND",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER,
                            name = "clientid",
                            description = "The Client Id",
                            required = true,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "clientsecret",
                            description = "The Client Secret",
                            required = true,
                            schema = @Schema(type = "string"))}
    )
    @GetMapping(path = "/api/nnd/data-exchange")
    public ResponseEntity<DataExchangeModel> exchangingData(@RequestParam("cnStatusTime") String cnStatusTime,
                                                            @RequestParam("transportStatusTime") String transportStatusTime,
                                                            @RequestParam("netssTime") String netssTime,
                                                            @RequestParam("statusCd") String statusCd) throws DataExchangeException {
        if (statusCd.isEmpty()) {
            throw new DataExchangeException("Status Code is Missing");
        }
        return ResponseEntity.ok(dataExchangeService.getDataForOnPremExchanging(cnStatusTime, transportStatusTime,netssTime, statusCd));
    }
}
