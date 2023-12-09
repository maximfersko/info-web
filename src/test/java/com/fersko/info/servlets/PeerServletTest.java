package com.fersko.info.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fersko.info.dto.PeerDto;
import com.fersko.info.service.PeerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class PeerServletTest {

    @Mock
    private PeerService peerService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private PeerServlet peerServlet;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        peerServlet = new PeerServlet();
        peerServlet.init();
        peerServlet.setPeerService(peerService);
    }

    @Test
    void testDoPost() throws Exception {

        String requestBody = "{\"id\": \"1\", \"birthday\": \"2023-01-01\"}";
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        when(request.getReader()).thenReturn(reader);

        PeerDto newPeer = new PeerDto("1", LocalDate.parse("2023-01-01"));
        when(peerService.save(any())).thenReturn(newPeer);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        peerServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).getWriter();
    }

    @Test
    void testDoPut() throws Exception {
        String requestBody = "{\"id\": \"1\", \"birthday\": \"2023-01-01\"}";
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        when(request.getReader()).thenReturn(reader);

        PeerDto updatedPeer = new PeerDto("1", LocalDate.parse("2023-01-01"));
        doNothing().when(peerService).update(updatedPeer);

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        peerServlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}
