package gov.cdc.nnddataexchangeservice.controller;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeGenericService;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeService;
import gov.cdc.nnddataexchangeservice.service.model.DataExchangeModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        when(dataExchangeService.getDataForOnPremExchanging(anyString(), anyString(), anyString(), anyString(), anyInt(), anyBoolean()))
                .thenReturn(dataExchangeModel);

        ResponseEntity<DataExchangeModel> response = dataExchangeController.exchangingData(cnStatusTime, transportStatusTime, netssTime, statusCd, limit, "false");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dataExchangeModel, response.getBody());
    }


    @Test
    void exchangingData_WithValidParams_ReturnsDataExchangeModel_True() throws DataExchangeException, IOException {
        String cnStatusTime = "2024-07-11";
        String transportStatusTime = "2024-07-12";
        String netssTime = "2024-07-13";
        String statusCd = "COMPLETE";
        String limit = "10";

        DataExchangeModel dataExchangeModel = new DataExchangeModel();
        when(dataExchangeService.getDataForOnPremExchanging(anyString(), anyString(), anyString(), anyString(), anyInt(), anyBoolean()))
                .thenReturn(dataExchangeModel);

        ResponseEntity<DataExchangeModel> response = dataExchangeController.exchangingData(cnStatusTime, transportStatusTime, netssTime, statusCd, limit, "true");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dataExchangeModel, response.getBody());
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

        when(dataExchangeGenericService.getGenericDataExchange(anyString(), anyString(), anyInt()))
                .thenReturn(base64CompressedData);

        ResponseEntity<String> response = dataExchangeController.exchangingData(tableName, timestamp, limit);

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

}
