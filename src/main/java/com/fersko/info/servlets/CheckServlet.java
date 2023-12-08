package com.fersko.info.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fersko.info.dto.CheckDto;
import com.fersko.info.repository.impl.CheckRepositoryImpl;
import com.fersko.info.service.CheckService;
import com.fersko.info.service.impl.CheckServiceImpl;

import com.fersko.info.servlets.utilities.ResponseHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@WebServlet(name = "checkServlet", urlPatterns = "/data/checks/*")
public class CheckServlet extends HttpServlet {

    private CheckService checkServiceImpl;

    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        checkServiceImpl = new CheckServiceImpl(CheckRepositoryImpl.getChecksRepository());
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestBody = ResponseHandler.concatString(req);
        CheckDto newCheck = objectMapper.readValue(requestBody, CheckDto.class);
        CheckDto savedCheck = checkServiceImpl.save(newCheck);
        ResponseHandler.sendJsonResponse(resp, savedCheck, objectMapper);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestBody = ResponseHandler.concatString(req);
        CheckDto updatedCheck = objectMapper.readValue(requestBody, CheckDto.class);
        checkServiceImpl.update(updatedCheck);
        ResponseHandler.sendJsonResponse(resp, updatedCheck, objectMapper);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<CheckDto> checks = checkServiceImpl.findByAll();
            ResponseHandler.sendJsonResponse(resp, checks, objectMapper);
        } else {
            ResponseHandler.handleGetRequest(req, resp, checkServiceImpl, objectMapper);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            ResponseHandler.handleDeleteRequest(req, resp, checkServiceImpl);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }






}
