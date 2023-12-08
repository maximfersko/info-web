package com.fersko.info.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fersko.info.dto.TaskDto;
import com.fersko.info.repository.impl.TaskRepositoryImpl;
import com.fersko.info.service.TaskService;
import com.fersko.info.service.impl.TaskServiceImpl;
import com.fersko.info.servlets.utilities.ResponseHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@WebServlet(name = "taskServlet", urlPatterns = "/data/tasks/*")
public class TaskServlet extends HttpServlet {

    private TaskService taskService;

    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        taskService = new TaskServiceImpl(TaskRepositoryImpl.getTaskRepository());
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<TaskDto> checks = taskService.findByAll();
            ResponseHandler.sendJsonResponse(resp, checks, objectMapper);
        } else {
            ResponseHandler.handleGetRequest(req, resp, taskService, objectMapper);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            ResponseHandler.handleDeleteRequest(req, resp, taskService);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestBody = ResponseHandler.concatString(req);
        TaskDto newTask = objectMapper.readValue(requestBody, TaskDto.class);
        TaskDto savedTask = taskService.save(newTask);
        ResponseHandler.sendJsonResponse(resp, savedTask, objectMapper);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestBody = ResponseHandler.concatString(req);
        TaskDto updatedTask = objectMapper.readValue(requestBody, TaskDto.class);
        taskService.update(updatedTask);
        ResponseHandler.sendJsonResponse(resp, updatedTask, objectMapper);
    }


}
