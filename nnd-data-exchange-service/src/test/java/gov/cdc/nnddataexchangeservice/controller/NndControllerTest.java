package gov.cdc.nnddataexchangeservice.controller;

import com.google.gson.Gson;
import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeGenericService;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeService;
import gov.cdc.nnddataexchangeservice.service.model.DataExchangeModel;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class NndControllerTest {
    @Mock
    private IDataExchangeService dataExchangeService;

    @Mock
    private IDataExchangeGenericService dataExchangeGenericService;

    @InjectMocks
    private NndController dataSyncController;


    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = mock(HttpServletRequest.class);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void exchangingData_WithValidParams_ReturnsDataExchangeModel() throws DataExchangeException, IOException {
        String cnStatusTime = "2024-07-11";
        String transportStatusTime = "2024-07-12";
        String netssTime = "2024-07-13";
        String statusCd = "COMPLETE";
        String limit = "10";

        DataExchangeModel dataExchangeModel = new DataExchangeModel();
        Gson gson = new Gson();
        String data = gson.toJson(dataExchangeModel);
        when(dataExchangeService.getDataForOnPremExchanging(anyString(), anyString(), anyString(), anyString(), anyInt(), anyBoolean()))
                .thenReturn(data);

        ResponseEntity<String> response = dataSyncController.exchangingData(cnStatusTime, transportStatusTime, netssTime, statusCd, limit, "false");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void exchangingData_WithValidParams_ReturnsDataExchangeModel_True() throws DataExchangeException, IOException {
        String cnStatusTime = "2024-07-11";
        String transportStatusTime = "2024-07-12";
        String netssTime = "2024-07-13";
        String statusCd = "COMPLETE";
        String limit = "10";

        DataExchangeModel dataExchangeModel = new DataExchangeModel();
        Gson gson = new Gson();
        String data = gson.toJson(dataExchangeModel);
        when(dataExchangeService.getDataForOnPremExchanging(anyString(), anyString(), anyString(), anyString(), anyInt(), anyBoolean()))
                .thenReturn(data);

        ResponseEntity<String> response = dataSyncController.exchangingData(cnStatusTime, transportStatusTime, netssTime, statusCd, limit, "true");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void exchangingData_WithMissingStatusCd_ThrowsDataExchangeException() {
        String cnStatusTime = "2024-07-11";
        String transportStatusTime = "2024-07-12";
        String netssTime = "2024-07-13";
        String statusCd = "";
        String limit = "10";

        assertThrows(DataExchangeException.class, () ->
                dataSyncController.exchangingData(cnStatusTime, transportStatusTime, netssTime, statusCd, limit, "false"));
    }

    @Test
    void decodeAndDecompress_WithValidTableName_ReturnsDecodedData() throws DataExchangeException {
        String tableName = "test_table";
        String decodedData = "mockDecodedData";

        when(dataExchangeGenericService.decodeAndDecompress(anyString()))
                .thenReturn(decodedData);

        ResponseEntity<String> response = dataSyncController.decodeAndDecompress(tableName);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(decodedData, response.getBody());
    }


}
