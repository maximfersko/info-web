package com.fersko.info.servlets.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fersko.info.dto.BaseDto;
import com.fersko.info.service.BaseService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class ResponseHandler {

    private ResponseHandler() {

    }

    public static <D extends BaseDto<I>, S extends BaseService<D, ?>, I> void handleGetRequest(HttpServletRequest req, HttpServletResponse resp, S service, ObjectMapper objectMapper) throws IOException {
        String[] pathParts = req.getPathInfo().split("/");
        try {
            String entityIdString = pathParts[1];
            Optional<D> entity = service.findById(convertToId(entityIdString));
            if (entity.isPresent()) {
                sendJsonResponse(resp, entity.get(), objectMapper);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public static <D extends BaseDto<I>, S extends BaseService<D, ?>, I> void handleDeleteRequest(HttpServletRequest req, HttpServletResponse resp, S service) throws IOException {
        String[] pathParts = req.getPathInfo().split("/");
        try {
            String entityIdString = pathParts[1];
            if (service.delete(convertToId(entityIdString))) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


    private static <I> I convertToId(String entityIdString) {
        try {
            return (I) Long.valueOf(entityIdString);
        } catch (NumberFormatException e) {
            return (I) entityIdString;
        }
    }

    public static void sendJsonResponse(HttpServletResponse response, Object data, ObjectMapper objectMapper) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter writer = response.getWriter()) {
            objectMapper.writeValue(writer, data);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(response.getWriter(), "Internal server error");
        }
    }

    public static String concatString(HttpServletRequest request) throws IOException {
        return request.getReader()
                .lines()
                .reduce("", String::concat);
    }
}
