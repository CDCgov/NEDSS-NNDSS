package gov.cdc.nnddataexchangeservice.controller;


import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeGenericService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.cdc.nnddataexchangeservice.shared.ErrorResponseBuilder.buildErrorResponse;
import static gov.cdc.nnddataexchangeservice.shared.TimestampHandler.convertTimestampFromString;

@RestController
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Data Sync", description = "Data Sync API")
public class DataSyncController {

    private final IDataExchangeGenericService dataExchangeGenericService;
    private static Logger logger = LoggerFactory.getLogger(DataSyncController.class);

    public DataSyncController(
                              IDataExchangeGenericService dataExchangeGenericService) {
        this.dataExchangeGenericService = dataExchangeGenericService;
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
                            required = false,
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
            String base64CompressedData = null;
            try {
                base64CompressedData = dataExchangeGenericService.getDataForDataSync(tableName, param, startRow, endRow, Boolean.parseBoolean(initialLoadApplied),
                        Boolean.parseBoolean(allowNull), Boolean.parseBoolean(noPagination), Boolean.parseBoolean(useKeyPagination));
            } catch (DataExchangeException e) {
                e.printStackTrace();
                throw new RuntimeException(e); //NOSONAR
            }
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
                            required = false,
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
                                                        ){

        try {
            if (version == null || version.isEmpty()) {
                try {
                    throw new DataExchangeException("Version is Missing");
                } catch (DataExchangeException e) {
                    throw new RuntimeException(e); //NOSONAR
                }
            }

            logger.info("Fetching Data Count for Data Availability, Table {}", tableName);
            String param = "";
            if (Boolean.parseBoolean(useKeyPagination)) {
                param = lastKey;
            }
            else {
                param = convertTimestampFromString(timestamp);
            }

            Integer res ;
            try {
                res = dataExchangeGenericService.getTotalRecord(tableName, Boolean.parseBoolean(initialLoadApplied), param, Boolean.parseBoolean(useKeyPagination));
            } catch (DataExchangeException e) {
                throw new RuntimeException(e); //NOSONAR
            }
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    @Operation(
            summary = "Get Meta data for specific table",
            description = "This endpoint will return data detail info such as field information like name, and data type",
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
                            name = "sourcedb",
                            description = "Source DB",
                            required = true,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "version",
                            description = "Version check Flag",
                            schema = @Schema(type = "string", defaultValue = "1"),
                            required = false)

            }
    )
    @GetMapping(path = "/api/datasync/metadata/{tableName}")
    @SuppressWarnings("java:S1452")
    public ResponseEntity<?> dataSyncMetaData(@PathVariable String tableName,
                                              @RequestHeader(name = "version", defaultValue = "") String version,
                                              HttpServletRequest request) {
            try {
                if (version == null || version.isEmpty()) {
                    throw new DataExchangeException("Version is Missing");
                }

                var res = dataExchangeGenericService.getTableMetaData(tableName);
                return new ResponseEntity<>(res, HttpStatus.OK);
            } catch (Exception e) {
                return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
            }
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
                    @Parameter(in = ParameterIn.QUERY,
                            name = "nullTimestampAllow",
                            description = "Optional null ts value allow flag to filter counts - value should be either true, false or empty",
                            required = false,
                            schema = @Schema(type = "string"))
            }
    )
    @GetMapping(path = "/api/datasync/all-tables-count")
    public ResponseEntity<Map<String, Object>> getAllTablesCount(@RequestParam(value = "sourceDbName", required = false) String sourceDbName,
                                               @RequestParam(value = "tableName", required = false) String tableName,
                                               @RequestParam(value = "timestamp", required = false) String timestamp,
                                               @RequestParam(value = "nullTimestampAllow", required = false,
                                                       defaultValue = "false") String nullTimestampAllow,
                                               HttpServletRequest request) {
        try {
            boolean nullTsAlow = false;
            if (!nullTimestampAllow.isEmpty()) {
                nullTsAlow = Boolean.parseBoolean(nullTimestampAllow);
            }
            List<Map<String, Object>> tableCounts = dataExchangeGenericService.getAllTablesCount(sourceDbName, tableName, timestamp, nullTsAlow);
            Map<String, Object> response = new HashMap<>();
            if (tableCounts.isEmpty()) {
                response.put("message", "No results found for the given input(s).");
                response.put("data", new ArrayList<>());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            response.put("message", "Success");
            response.put("data", tableCounts);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }



}
