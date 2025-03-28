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
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static gov.cdc.nnddataexchangeservice.shared.ErrorResponseBuilder.buildErrorResponse;
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
                            required = false),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "version",
                            description = "Version check Flag",
                            schema = @Schema(type = "string", defaultValue = "1"),
                            required = false),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "noPagination",
                            description = "No Pagination check Flag",
                            schema = @Schema(type = "string", defaultValue = "false"),
                            required = false),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "useKeyPagination",
                            description = "Use Key Pagination check Flag",
                            schema = @Schema(type = "string", defaultValue = "false"),
                            required = false),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "lastKey",
                            description = "Last use Key indicator",
                            schema = @Schema(type = "string", defaultValue = ""),
                            required = false)
            }
    )
    @SuppressWarnings("java:S1452")
    @GetMapping(path = "/api/datasync/{tableName}")
    public ResponseEntity<?> dataSync(@PathVariable String tableName, @RequestParam String timestamp,
                                           @RequestHeader(name = "startRow", defaultValue = "0", required = false) String startRow,
                                           @RequestHeader(name = "endRow", defaultValue = "0", required = false) String endRow,
                                           @RequestHeader(name = "initialLoad", defaultValue = "false", required = false) String initialLoadApplied,
                                           @RequestHeader(name = "allowNull", defaultValue = "false", required = false) String allowNull,
                                           @RequestHeader(name = "version", defaultValue = "") String version,
                                           @RequestHeader(name = "noPagination", defaultValue = "false") String noPagination,
                                           @RequestHeader(name = "useKeyPagination", defaultValue = "false") String useKeyPagination,
                                           @RequestHeader(name = "lastKey", defaultValue = "") String lastKey,
                                           HttpServletRequest request) {
        try {
            if (version == null || version.isEmpty()) {
                throw new DataExchangeException("Version is Missing");
            }

            String param = "";
            if (Boolean.parseBoolean(useKeyPagination)) {
                param = lastKey;
            }
            else {
                param = convertTimestampFromString(timestamp);
            }
            var base64CompressedData = dataExchangeGenericService.getDataForDataSync(tableName, param, startRow, endRow, Boolean.parseBoolean(initialLoadApplied),
                    Boolean.parseBoolean(allowNull), Boolean.parseBoolean(noPagination), Boolean.parseBoolean(useKeyPagination));
            return new ResponseEntity<>(base64CompressedData, HttpStatus.OK);
        } catch (Exception e) {
            return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
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
                            required = false),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "version",
                            description = "Version check Flag",
                            schema = @Schema(type = "string", defaultValue = "1"),
                            required = false),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "useKeyPagination",
                            description = "Use Key Pagination check Flag",
                            schema = @Schema(type = "string", defaultValue = "false"),
                            required = false),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "lastKey",
                            description = "Last use Key indicator",
                            schema = @Schema(type = "string", defaultValue = ""),
                            required = false)
            }
    )
    @SuppressWarnings("java:S1452")
    @GetMapping(path = "/api/datasync/count/{tableName}")
    public ResponseEntity<?> dataSyncTotalRecords(@PathVariable String tableName,
                                                       @RequestParam String timestamp,
                                                        @RequestHeader(name = "initialLoad", defaultValue = "false", required = false) String initialLoadApplied,
                                                        @RequestHeader(name = "version", defaultValue = "") String version,
                                                        @RequestHeader(name = "useKeyPagination", defaultValue = "false") String useKeyPagination,
                                                        @RequestHeader(name = "lastKey", defaultValue = "") String lastKey,
                                                        HttpServletRequest request
                                                        ) {
        try {
            if (version == null || version.isEmpty()) {
                throw new DataExchangeException("Version is Missing");
            }

            logger.info("Fetching Data Count for Data Availability, Table {}", tableName);
            String param = "";
            if (Boolean.parseBoolean(useKeyPagination)) {
                param = lastKey;
            }
            else {
                param = convertTimestampFromString(timestamp);
            }

            var res = dataExchangeGenericService.getTotalRecord(tableName, Boolean.parseBoolean(initialLoadApplied), param, Boolean.parseBoolean(useKeyPagination));
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }

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

    @Operation(
            summary = "Get count for all tables",
            description = "Fetches the record count for all tables, optionally filtered by source database, table name, and timestamp. Requires client authentication headers.",
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
                            name = "sourceDbName",
                            description = "Optional source database name to filter counts",
                            required = false,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.QUERY,
                            name = "tableName",
                            description = "Optional table name to filter counts",
                            required = false,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.QUERY,
                            name = "timestamp",
                            description = "Optional timestamp to filter counts",
                            required = false,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "initialLoad",
                            description = "Flag indicating whether this is an initial data load",
                            schema = @Schema(type = "string", defaultValue = "false"),
                            required = false)
            }
    )
    @GetMapping(path = "/api/datasync/all-tables-count")
    public ResponseEntity<?> getAllTablesCount(@RequestParam(value = "sourceDbName", required = false) String sourceDbName,
                                               @RequestParam(value = "tableName", required = false) String tableName,
                                               @RequestParam(value = "timestamp", required = false) String timestamp,
                                               @RequestParam(value = "initialLoad", required = false) boolean initialLoad,
                                               HttpServletRequest request) {
        try {
            logger.info("Fetching counts for all tables, optional filters - sourceDbName: {}, tableName: {}, timestamp: {}", sourceDbName, tableName, timestamp);
            List<Map<String, Object>> tableCounts = dataExchangeGenericService.getAllTablesCount(sourceDbName, tableName, timestamp, initialLoad);
            if(tableCounts.isEmpty()) {
                return new ResponseEntity<>("No results found for the given input(s).", HttpStatus.OK);
            }
            return new ResponseEntity<>(tableCounts, HttpStatus.OK);
        } catch (Exception e) {
            return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

}
