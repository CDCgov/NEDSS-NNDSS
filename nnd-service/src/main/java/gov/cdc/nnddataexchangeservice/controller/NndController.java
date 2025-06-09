package gov.cdc.nnddataexchangeservice.controller;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeGenericService;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@SecurityRequirement(name = "bearer-key")
@Tag(name = "NND", description = "NND API")
public class NndController {
    private final IDataExchangeService dataExchangeService;
    private final IDataExchangeGenericService dataExchangeGenericService;
    private static Logger logger = LoggerFactory.getLogger(NndController.class);

    public NndController(IDataExchangeService dataExchangeService, IDataExchangeGenericService dataExchangeGenericService) {
        this.dataExchangeService = dataExchangeService;
        this.dataExchangeGenericService = dataExchangeGenericService;
    }

    @Operation(
            summary = "Get data from multiple tables related to NND process",
            description = "Fetches data based on various parameters related to the NND process.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER,
                            name = "clientid",
                            description = "The Client Id for authentication",
                            required = true,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "clientsecret",
                            description = "The Client Secret for authentication",
                            required = true,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.QUERY,
                            name = "cnStatusTime",
                            description = "CN Status time parameter",
                            required = true,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.QUERY,
                            name = "transportStatusTime",
                            description = "Transport Status time parameter",
                            required = true,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.QUERY,
                            name = "netssTime",
                            description = "NETSS time parameter",
                            required = true,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.QUERY,
                            name = "statusCd",
                            description = "Status code for filtering",
                            required = true,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "limit",
                            description = "Limit on the number of records returned",
                            schema = @Schema(type = "string", defaultValue = "0"),
                            required = false),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "compress",
                            description = "Boolean flag to compress the response",
                            schema = @Schema(type = "string", defaultValue = "false"),
                            required = false),

            }
    )
    @GetMapping(path = "/api/nnd")
    public ResponseEntity<String> exchangingData(@RequestParam("cnStatusTime") String cnStatusTime,
                                                 @RequestParam("transportStatusTime") String transportStatusTime,
                                                 @RequestParam("netssTime") String netssTime,
                                                 @RequestParam("statusCd") String statusCd,
                                                 @RequestHeader(name = "limit", defaultValue = "0", required = false) String limit,
                                                 @RequestHeader(name = "compress", defaultValue = "false", required = false) String compress) throws DataExchangeException, IOException {
        if (statusCd.isEmpty()) {
            throw new DataExchangeException("Status Code is Missing");
        }


        boolean compressCheck = false;
        if (compress.equalsIgnoreCase("true") ) {
            compressCheck = true;
        }

        int intLimit = Integer.parseInt(limit);
        return ResponseEntity.ok(dataExchangeService.getDataForOnPremExchanging(cnStatusTime, transportStatusTime,netssTime, statusCd, intLimit, compressCheck));
    }

    @Operation(
            summary = "Decoding data that return from data sync endpoint",
            description = "Data Sync return zipped and encoded data, this endpoint can be used to decode the data for inspection and integration",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER,
                            name = "clientid",
                            description = "The Client Id for authentication",
                            required = true,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "clientsecret",
                            description = "The Client Secret for authentication",
                            required = true,
                            schema = @Schema(type = "string"))
            }
    )
    @PostMapping(path = "/api/datasync/decode")
    public ResponseEntity<String> decodeAndDecompress(@RequestBody String tableName) throws DataExchangeException {
        var val = dataExchangeGenericService.decodeAndDecompress(tableName);
        return new ResponseEntity<>(val, HttpStatus.OK);
    }

}
