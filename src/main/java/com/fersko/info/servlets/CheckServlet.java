package com.fersko.info.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fersko.info.dto.CheckDto;
import com.fersko.info.entity.Check;
import com.fersko.info.repository.impl.CheckRepositoryImpl;
import com.fersko.info.service.impl.CheckServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;


@WebServlet(name = "checkServlet", urlPatterns = "/data/check/*")
public class CheckServlet extends HttpServlet {

    private final CheckServiceImpl checkServiceImpl = new CheckServiceImpl(CheckRepositoryImpl.getChecksRepository());

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<CheckDto> checks = checkServiceImpl.findByAll();
            sendJsonResponse(resp, checks);
        } else {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                try {
                    long checkId = Long.parseLong(pathParts[1]);
                    Optional<CheckDto> check = checkServiceImpl.findById(checkId);
                    if (check.isPresent()) {
                        sendJsonResponse(resp, check.get());
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    }
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestBody = req.getReader()
                .lines()
                .reduce("", String::concat);
        CheckDto newCheck = objectMapper.readValue(requestBody, CheckDto.class);
        CheckDto savedCheck = checkServiceImpl.save(newCheck);

        sendJsonResponse(resp, savedCheck);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestBody = req.getReader()
                .lines()
                .reduce("", String::concat);
        CheckDto updatedCheck = objectMapper.readValue(requestBody, CheckDto.class);

        checkServiceImpl.update(updatedCheck);

        sendJsonResponse(resp, updatedCheck);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                try {
                    long checkId = Long.parseLong(pathParts[1]);
                    if (checkServiceImpl.delete(checkId)) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    }
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter writer = response.getWriter()) {
            objectMapper.writeValue(writer, data);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(response.getWriter(), "Internal server error");
        }
    }


}
