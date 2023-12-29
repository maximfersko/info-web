package com.fersko.info.servlets;

import com.fersko.info.dto.CheckDto;
import com.fersko.info.service.CheckService;
import jakarta.servlet.ServletException;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class CheckServletTest {

	@Mock
	private CheckService checkService;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@InjectMocks
	private CheckServlet checkServlet;

	@BeforeEach
	void setUp() throws ServletException {
		MockitoAnnotations.openMocks(this);
		checkServlet = new CheckServlet();
		checkServlet.init();
		checkServlet.setCheckService(checkService);
	}

	@Test
	void testDoDeleteWithInvalidPathInfo() throws Exception {
		when(request.getPathInfo()).thenReturn("/invalid");

		checkServlet.doDelete(request, response);

		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}


	@Test
	void testDoPost() throws Exception {
		String requestBody = "{\"id\": 1, \"peerDto\": {\"id\": 2}, \"taskDto\": {\"id\": 3}, \"date\": \"2023-01-01\"}";
		BufferedReader reader = new BufferedReader(new StringReader(requestBody));
		when(request.getReader()).thenReturn(reader);

		CheckDto newCheck = new CheckDto(1L, null, null, null);
		when(checkService.save(any())).thenReturn(newCheck);

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);

		checkServlet.doPost(request, response);

		verify(response).setStatus(HttpServletResponse.SC_OK);
		verify(response).getWriter();
	}

	@Test
	void testDoPut() throws Exception {

		String requestBody = "{\"id\": 1, \"peerDto\": {\"id\": 2}, \"taskDto\": {\"id\": 3}, \"date\": \"2023-01-01\"}";
		BufferedReader reader = new BufferedReader(new StringReader(requestBody));
		when(request.getReader()).thenReturn(reader);

		CheckDto updatedCheck = new CheckDto(1L, null, null, null);
		doReturn(null).when(checkService).update(updatedCheck);

		PrintWriter writer = mock(PrintWriter.class);
		when(response.getWriter()).thenReturn(writer);

		checkServlet.doPut(request, response);

		verify(response).setStatus(HttpServletResponse.SC_OK);
	}


	@Test
	void testDoGetWithValidPathInfo() throws Exception {
		when(request.getPathInfo()).thenReturn("/1");

		CheckDto checkDto = new CheckDto(1L, null, null, null);
		when(checkService.findById(anyLong())).thenReturn(Optional.of(checkDto));

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);

		checkServlet.doGet(request, response);

		verify(response).setStatus(HttpServletResponse.SC_OK);
		verify(response).getWriter();
	}


}
