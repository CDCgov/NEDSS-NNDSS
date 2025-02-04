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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static gov.cdc.nnddataexchangeservice.shared.TimestampHandler.convertTimestampFromString;

@RestController
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Data Exchange", description = "Data Exchange API")
public class DataExchangeController {

    private final IDataExchangeService dataExchangeService;
    private final IDataExchangeGenericService dataExchangeGenericService;
    private static Logger logger = LoggerFactory.getLogger(DataExchangeController.class);

    public DataExchangeController(IDataExchangeService dataExchangeService,
                                  IDataExchangeGenericService dataExchangeGenericService) {
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
                            required = false)
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
            summary = "Get data from multiple tables related to Datasync process",
            description = "Fetches data from the specified table based on the timestamp for data synchronization.",
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
                    @Parameter(in = ParameterIn.PATH,
                            name = "tableName",
                            description = "The name of the table from which data is to be synced",
                            required = true,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.QUERY,
                            name = "timestamp",
                            description = "Timestamp parameter used to filter data",
                            required = true,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "startRow",
                            description = "The starting row for pagination",
                            schema = @Schema(type = "string", defaultValue = "0"),
                            required = false),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "endRow",
                            description = "The ending row for pagination",
                            schema = @Schema(type = "string", defaultValue = "0"),
                            required = false),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "initialLoad",
                            description = "Flag indicating whether this is an initial data load",
                            schema = @Schema(type = "string", defaultValue = "false"),
                            required = false),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "allowNull",
                            description = "Flag indicating whether null timestamps are allowed",
                            schema = @Schema(type = "string", defaultValue = "false"),
                            required = false)
            }
    )
    @GetMapping(path = "/api/datasync/{tableName}")
    public ResponseEntity<String> dataSync(@PathVariable String tableName, @RequestParam String timestamp,
                                                 @RequestHeader(name = "startRow", defaultValue = "0", required = false) String startRow,
                                                 @RequestHeader(name = "endRow", defaultValue = "0", required = false) String endRow,
                                                 @RequestHeader(name = "initialLoad", defaultValue = "false", required = false) String initialLoadApplied,
                                                 @RequestHeader(name = "allowNull", defaultValue = "false", required = false) String allowNull) throws DataExchangeException {
        logger.info("Fetching Data for Data Availability, Table {}", tableName);
        var ts = convertTimestampFromString(timestamp);
        var base64CompressedData = dataExchangeGenericService.getDataForDataSync(tableName, ts, startRow, endRow, Boolean.parseBoolean(initialLoadApplied),
                Boolean.parseBoolean(allowNull));
        return new ResponseEntity<>(base64CompressedData, HttpStatus.OK);
    }

    @Operation(
            summary = "Get total record count for data sync from NND",
            description = "Fetches the total number of records from the specified table based on the timestamp for data synchronization. Requires client authentication headers.",
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
                    @Parameter(in = ParameterIn.PATH,
                            name = "tableName",
                            description = "The name of the table to retrieve the record count from",
                            required = true,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.QUERY,
                            name = "timestamp",
                            description = "Timestamp parameter used to filter records",
                            required = true,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "initialLoad",
                            description = "Flag indicating whether this is an initial data load",
                            schema = @Schema(type = "string", defaultValue = "false"),
                            required = false)
            }
    )
    @GetMapping(path = "/api/datasync/count/{tableName}")
    public ResponseEntity<Integer> dataSyncTotalRecords(@PathVariable String tableName,
                                                       @RequestParam String timestamp,
                                           @RequestHeader(name = "initialLoad", defaultValue = "false", required = false) String initialLoadApplied) throws DataExchangeException {
        logger.info("Fetching Data Count for Data Availability, Table {}", tableName);
        var ts = convertTimestampFromString(timestamp);
        var res = dataExchangeGenericService.getTotalRecord(tableName, Boolean.parseBoolean(initialLoadApplied), ts);
        return new ResponseEntity<>(res, HttpStatus.OK);
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
