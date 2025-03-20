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

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

class DataExchangeControllerTest {

    @Mock
    private IDataExchangeService dataExchangeService;

    @Mock
    private IDataExchangeGenericService dataExchangeGenericService;

    @InjectMocks
    private DataExchangeController dataExchangeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        ResponseEntity<String> response = dataExchangeController.exchangingData(cnStatusTime, transportStatusTime, netssTime, statusCd, limit, "false");

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

        ResponseEntity<String> response = dataExchangeController.exchangingData(cnStatusTime, transportStatusTime, netssTime, statusCd, limit, "true");

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
                dataExchangeController.exchangingData(cnStatusTime, transportStatusTime, netssTime, statusCd, limit, "false"));
    }

    @Test
    void exchangingGenericData_WithValidParams_ReturnsBase64CompressedData() throws Exception {
        String tableName = "test_table";
        String timestamp = "2024-07-11";
        String limit = "10";
        String base64CompressedData = "mockBase64Data";
        String load = "true";

        when(dataExchangeGenericService.getDataForDataSync(anyString(), anyString(),anyString(),
                anyString(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean()))
                .thenReturn(base64CompressedData);

        ResponseEntity<String> response = dataExchangeController.dataSync(
                tableName,
                timestamp,
                "0",
                "1",
                limit,
                load,
                "1",
                "false",
                null,
                null,
                null);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(base64CompressedData, response.getBody());
    }



    @Test
    void decodeAndDecompress_WithValidTableName_ReturnsDecodedData() throws DataExchangeException {
        String tableName = "test_table";
        String decodedData = "mockDecodedData";

        when(dataExchangeGenericService.decodeAndDecompress(anyString()))
                .thenReturn(decodedData);

        ResponseEntity<String> response = dataExchangeController.decodeAndDecompress(tableName);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(decodedData, response.getBody());
    }


    @Test
    void dataSyncTotalRecords_Test() throws Exception {
        String tableName = "test_table";
        String timestamp = "2024-07-11";

        when(dataExchangeGenericService.getTotalRecord(anyString(), anyBoolean(),anyString(), anyBoolean()))
                .thenReturn(10);

        ResponseEntity<Integer> response = dataExchangeController.dataSyncTotalRecords(
                tableName,
                timestamp,
                "true",
                "1",
                null,
                null);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


}
