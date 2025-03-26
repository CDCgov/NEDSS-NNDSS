package gov.cdc.nnddataexchangeservice.shared;


import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ErrorResponseBuilderTest {

    @Test
    void testBuildErrorResponse() {
        // Arrange
        Exception exception = new RuntimeException("Something went wrong");
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/test");

        // Act
        ResponseEntity<Map<String, Object>> response = ErrorResponseBuilder.buildErrorResponse(
                exception, HttpStatus.INTERNAL_SERVER_ERROR, request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.get("status"));
        assertEquals("Internal Server Error", body.get("error"));
        assertEquals("Something went wrong", body.get("message"));
        assertEquals("/api/test", body.get("path"));

        assertTrue(body.get("timestamp").toString().matches("\\d{4}-\\d{2}-\\d{2}T.*"), "Timestamp format incorrect");
        assertTrue(((String) body.get("stackTrace")).contains("ErrorResponseBuilderTest"), "Stack trace should contain test class");
    }
}