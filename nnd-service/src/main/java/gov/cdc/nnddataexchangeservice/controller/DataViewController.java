package gov.cdc.nnddataexchangeservice.controller;

import gov.cdc.nnddataexchangeservice.repository.rdb.model.DataViewConfig;
import gov.cdc.nnddataexchangeservice.security.GrantedAuthorityFinder;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeGenericService;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataViewService;
import gov.cdc.nnddataexchangeservice.service.model.dto.DataViewConfigDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static gov.cdc.nnddataexchangeservice.shared.ErrorResponseBuilder.buildErrorResponse;

@RestController
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Data View", description = "Data View API")
public class DataViewController {
    private final IDataViewService dataViewService;
    private final IDataExchangeGenericService dataExchangeGenericService;
    private final GrantedAuthorityFinder grantedAuthorityFinder;


    private static Logger logger = LoggerFactory.getLogger(DataViewController.class);

    public DataViewController(IDataViewService dataViewService,
                              IDataExchangeGenericService dataExchangeGenericService, GrantedAuthorityFinder grantedAuthorityFinder) {
        this.dataViewService = dataViewService;
        this.dataExchangeGenericService = dataExchangeGenericService;
        this.grantedAuthorityFinder = grantedAuthorityFinder;
    }

    @Operation(
            summary = "Get Specific ODSE data",
            description = "This endpoint is deprecated. Please use /api/data-view-v2 instead.",
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
                            name = "timestamp",
                            description = "Optional timestamp to filter counts",
                            required = false,
                            schema = @Schema(type = "string"))
            }
    )
    @GetMapping(path = "/api/data-view-old/{tableName}")
    @Deprecated
    public ResponseEntity<?> dataSyncSpecialODSETable(@PathVariable String tableName,
                                                           @RequestParam(name = "timestamp", required = false) String timestamp,
                                                           HttpServletRequest request
    )   {
        try {
            var res = dataExchangeGenericService.getDataForDataRetrieval(tableName, timestamp);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }

    }

    @Operation(
            summary = "Get Specific Data",
            description = """
                Fetches specific data based on a named query. 
                - If query support custom where, the param query string will be ignored even if it has value - vice versa with when custom query is deactivated

                - `param`: Used to supply values for predefined queries. Multiple values can be passed, separated by `~`. 
                  Example: `test~test1~test3` (this passes 3 values to placeholders in sequence).

                - `where`: Used only for queries flagged as accepting custom WHERE clauses. Supply the full WHERE clause string in the request body.
                  Example:
                  ```
                  o.record_status_cd = 'UNPROCESSED'
                  AND o.obs_domain_cd_st_1 = 'Order'
                  AND o.ctrl_cd_display_form = 'LabReport'
                  AND o.jurisdiction_cd IS NOT NULL
                  AND o.prog_area_cd IS NOT NULL
                  AND p.type_cd = 'AUT'
                  AND ar.type_cd = 'COMP'
                  AND o1.obs_domain_cd_st_1 = 'Result'
                  ```
                """,
            parameters = {
                    @Parameter(in = ParameterIn.HEADER,
                            name = "clientid",
                            description = "The Client ID for authentication",
                            required = true,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "clientsecret",
                            description = "The Client Secret for authentication",
                            required = true,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.HEADER,
                            name = "username",
                            description = "User's NBS system username",
                            required = true,
                            schema = @Schema(type = "string")),
                    @Parameter(in = ParameterIn.QUERY,
                            name = "param",
                            description = "Optional parameter values for the query. Use `~` to separate multiple values.",
                            required = false,
                            schema = @Schema(type = "string"))
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = false,
                    description = "Optional WHERE clause as raw SQL for queries that support custom conditions. Plain text only.",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(type = "string")
                    )
            )
    )
    @GetMapping(path = "/api/data-view/{queryName}",  consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> dataViewV2Table(@PathVariable String queryName,
                                             @RequestParam(name = "param", required = false) String param,
                                             @RequestParam(name = "username", required = true) String username,
                                             @RequestBody(required = false) String where,
                                             HttpServletRequest request
    )  {
        try {
            Set<GrantedAuthority> authorities = grantedAuthorityFinder.find(username);
            if (authorities.stream().noneMatch(auth ->
                    auth.getAuthority().equals("ADD-INVESTIGATION") ||
                    auth.getAuthority().equals("CREATE-NOTIFICATION") ||
                    auth.getAuthority().equals("ADD-PATIENT")
            )) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
            }
            var res = dataViewService.getDataForDataView(queryName, param, where);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }

    }


    @Operation(
            summary = "Add or Update Data View Config",
            description = """
        This endpoint is used to add or update a data view config record.
        - If config require CUSTOM PARAM, it must have WHERE_STATEMENT key in the where clause location
        - If an existing `queryName` is provided, the record will be **updated**.
        - If the `queryName` is new, the record will be **inserted**.
        
        The request body must match the following model:
        ```
        {
          "queryName": String (required),
          "sourceDb": String (required) - support (RDB, NBS_ODSE, NBS_MSGOUTE, SRTE, RDB_MODERN)
          "query": String (required),
          "metaData": String (optional),
          "customParamApplied": Boolean (optional) - value is true or false,
          "crossDbApplied": Boolean (optional) - value is true or false,
        }
        ```
        """,
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "clientid",
                            description = "The Client ID for authentication",
                            required = true,
                            schema = @Schema(type = "string")
                    ),
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "clientsecret",
                            description = "The Client Secret for authentication",
                            required = true,
                            schema = @Schema(type = "string")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "JSON representation of the DataViewConfigDto object",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DataViewConfigDto.class)
                    )
            )
    )
    @PostMapping("/api/data-view-config")
    public ResponseEntity<?> saveDataViewConfig(@RequestBody DataViewConfigDto dto,
                                                                  HttpServletRequest request)  {

        try {
            DataViewConfig savedEntity = dataViewService.saveConfig(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(DataViewConfigDto.fromEntity(savedEntity));
        } catch (Exception e) {
            return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    @Operation(
            summary = "Fetch Data View Config",
            description = """
        Retrieve Data View Config(s) from the system.

        - If `queryName` is provided, returns the specific config matching the name.
        - If not provided, returns **all available** data view configurations.
        """,
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "clientid",
                            description = "The Client ID for authentication",
                            required = true,
                            schema = @Schema(type = "string")
                    ),
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "clientsecret",
                            description = "The Client Secret for authentication",
                            required = true,
                            schema = @Schema(type = "string")
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "queryName",
                            description = "Optional filter for specific query name. If omitted, returns all configs.",
                            required = false,
                            schema = @Schema(type = "string")
                    )
            }
    )
    @GetMapping("/api/data-view-config")
    public ResponseEntity<?> getDataViewConfig(@RequestParam(name = "queryName", required = false) String queryName,
                                                  HttpServletRequest request)  {

        try {
            var configs = dataViewService.getConfigs(queryName);
            return ResponseEntity.status(HttpStatus.CREATED).body(configs);
        } catch (Exception e) {
            return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }
}
