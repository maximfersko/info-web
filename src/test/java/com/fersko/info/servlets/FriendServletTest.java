package com.fersko.info.servlets;

import com.fersko.info.dto.FriendDto;
import com.fersko.info.dto.PeerDto;
import com.fersko.info.service.FriendService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class FriendServletTest {

	@Mock
	private FriendService friendService;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@InjectMocks
	private FriendServlet friendServlet;


	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		friendServlet = new FriendServlet();
		friendServlet.init();
		friendServlet.setFriendService(friendService);
	}

	@Test
	void testDoGetWithValidPathInfo() throws Exception {
		when(request.getPathInfo()).thenReturn("/1");

		PeerDto peerDto = new PeerDto(2L, "1", LocalDate.now());
		FriendDto friendDto = new FriendDto(1L, peerDto, peerDto);
		when(friendService.findById(anyLong())).thenReturn(Optional.of(friendDto));

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);

		friendServlet.doGet(request, response);

		verify(response).setStatus(HttpServletResponse.SC_OK);
		verify(response).getWriter();
	}

	@Test
	void testDoDeleteWithInvalidPathInfo() throws Exception {
		when(request.getPathInfo()).thenReturn("/invalid");

		friendServlet.doDelete(request, response);

		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Test
	void testDoPost() throws Exception {

		String requestBody = "{\"id\": 1, \"firstPeer\": {\"id\": \"1\", \"birthday\": \"2023-01-01\"}, \"secondPeer\": {\"id\": \"2\", \"birthday\": \"2023-01-02\"}}";
		BufferedReader reader = new BufferedReader(new StringReader(requestBody));
		when(request.getReader()).thenReturn(reader);

		PeerDto firstPeer = new PeerDto(1L, "1", LocalDate.parse("2023-01-01"));
		PeerDto secondPeer = new PeerDto(2L, "2", LocalDate.parse("2023-01-02"));
		FriendDto newFriend = new FriendDto(1L, firstPeer, secondPeer);
		when(friendService.save(any())).thenReturn(newFriend);

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);

		friendServlet.doPost(request, response);

		verify(response).setStatus(HttpServletResponse.SC_OK);
		verify(response).getWriter();
	}

	@Test
	void testDoPut() throws Exception {
		String requestBody = "{\"id\": 1, \"firstPeer\": {\"id\": \"1\", \"birthday\": \"2023-01-01\"}, \"secondPeer\": {\"id\": \"2\", \"birthday\": \"2023-01-02\"}}";
		BufferedReader reader = new BufferedReader(new StringReader(requestBody));
		when(request.getReader()).thenReturn(reader);

		PeerDto firstPeer = new PeerDto(1L, "1", LocalDate.parse("2023-01-01"));
		PeerDto secondPeer = new PeerDto(2L, "2", LocalDate.parse("2023-01-02"));
		FriendDto updatedFriend = new FriendDto(1L, firstPeer, secondPeer);
		doReturn(null).when(friendService).update(updatedFriend);

		PrintWriter writer = mock(PrintWriter.class);
		when(response.getWriter()).thenReturn(writer);

		friendServlet.doPut(request, response);

		verify(response).setStatus(HttpServletResponse.SC_OK);
	}
}
