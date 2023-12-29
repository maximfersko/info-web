package com.fersko.info.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fersko.info.dto.PeerDto;
import com.fersko.info.service.PeerService;
import com.fersko.info.service.impl.PeerServiceImpl;
import com.fersko.info.servlets.utilities.ResponseHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "peerServlet", urlPatterns = "/data/peers/*")
public class PeerServlet extends HttpServlet {
	@Setter
	private PeerService peerService;

	private ObjectMapper objectMapper;

	@Override
	public void init() throws ServletException {
		peerService = new PeerServiceImpl();
		objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.equals("id")) {
			ResponseHandler.handleGetRequest(req, resp, peerService, objectMapper);
		} else {
			List<PeerDto> peers = peerService.findByAll();
			ResponseHandler.sendJsonResponse(resp, peers, objectMapper);
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestBody = ResponseHandler.concatString(req);
		PeerDto updatedPeer = objectMapper.readValue(requestBody, PeerDto.class);
		peerService.update(updatedPeer);
		ResponseHandler.sendJsonResponse(resp, updatedPeer, objectMapper);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.equals("id")) {
			ResponseHandler.handleDeleteRequest(req, resp, peerService);
		} else {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestBody = ResponseHandler.concatString(req);
		PeerDto newPeer = objectMapper.readValue(requestBody, PeerDto.class);
		PeerDto savedCheck = peerService.save(newPeer);
		ResponseHandler.sendJsonResponse(resp, savedCheck, objectMapper);
	}


}
