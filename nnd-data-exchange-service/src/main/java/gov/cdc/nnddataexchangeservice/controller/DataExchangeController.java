package gov.cdc.nnddataexchangeservice.controller;


import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeGenericService;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeService;
import gov.cdc.nnddataexchangeservice.service.model.DataExchangeModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@SecurityRequirement(name = "bearer-key")
public class DataExchangeController {

    private final IDataExchangeService dataExchangeService;
    private final IDataExchangeGenericService dataExchangeGenericService;

    public DataExchangeController(IDataExchangeService dataExchangeService,
                                  IDataExchangeGenericService dataExchangeGenericService) {
        this.dataExchangeService = dataExchangeService;
        this.dataExchangeGenericService = dataExchangeGenericService;
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
                                                            @RequestParam("statusCd") String statusCd,
                                                            @RequestHeader(name = "limit", defaultValue = "0") String limit) throws DataExchangeException {
        if (statusCd.isEmpty()) {
            throw new DataExchangeException("Status Code is Missing");
        }

        int intLimit = Integer.parseInt(limit);
        return ResponseEntity.ok(dataExchangeService.getDataForOnPremExchanging(cnStatusTime, transportStatusTime,netssTime, statusCd, intLimit));
    }

    @Operation(
            summary = "Getting generic data from NND",
            description = "Getting generic data from NND",
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
    @GetMapping(path = "/api/data-exchange-generic/{tableName}")
    public ResponseEntity<String> exchangingData(@PathVariable String tableName, @RequestParam(required = false) String timestamp) {
        try {
            var base64CompressedData = dataExchangeGenericService.getGenericDataExchange(tableName, timestamp);
            return new ResponseEntity<>(base64CompressedData, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
    }

    @PostMapping(path = "/api/data-exchange-generic")
    public ResponseEntity<String> decodeAndDecompress(@RequestBody String tableName) {
        try {
            var val = dataExchangeGenericService.decodeAndDecompress(tableName);
            return new ResponseEntity<>(val, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
