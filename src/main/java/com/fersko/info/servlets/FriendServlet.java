package com.fersko.info.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fersko.info.dto.FriendDto;
import com.fersko.info.service.FriendService;
import com.fersko.info.service.impl.FriendServiceImpl;
import com.fersko.info.servlets.utilities.ResponseHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "friendServlet", urlPatterns = "/data/friends/*")
public class FriendServlet extends HttpServlet {
	@Setter
	private FriendService friendService;
	private ObjectMapper objectMapper;

	@Override
	public void init() throws ServletException {
		friendService = new FriendServiceImpl();
		objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.equals("id")) {
			ResponseHandler.handleGetRequest(req, resp, friendService, objectMapper);
		} else {
			List<FriendDto> friends = friendService.findByAll();
			ResponseHandler.sendJsonResponse(resp, friends, objectMapper);
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.equals("id")) {
			ResponseHandler.handleDeleteRequest(req, resp, friendService);
		} else {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestBody = ResponseHandler.concatString(req);
		FriendDto friend = objectMapper.readValue(requestBody, FriendDto.class);
		FriendDto friendSaved = friendService.save(friend);
		ResponseHandler.sendJsonResponse(resp, friendSaved, objectMapper);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestBody = ResponseHandler.concatString(req);
		FriendDto updatedFriend = objectMapper.readValue(requestBody, FriendDto.class);
		friendService.update(updatedFriend);
		ResponseHandler.sendJsonResponse(resp, updatedFriend, objectMapper);
	}


}
