package com.fersko.info.servlets;

import com.fersko.info.dto.TaskDto;
import com.fersko.info.service.TaskService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class TaskServletTest {

    @Mock
    private TaskService taskService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private TaskServlet taskServlet;


    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        taskServlet = new TaskServlet();
        taskServlet.init();
        taskServlet.setTaskService(taskService);
    }


    @Test
    void testDoPost() throws Exception {
        String requestBody = "{\"id\": \"1\", \"parentTask\": null, \"maxXp\": 100}";
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        when(request.getReader()).thenReturn(reader);

        TaskDto newTask = new TaskDto(1L, "1", null, 100);
        when(taskService.save(any())).thenReturn(newTask);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        taskServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).getWriter();
    }

    @Test
    void testDoPut() throws Exception {
        String requestBody = "{\"id\": \"1\", \"parentTask\": null, \"maxXp\": 150}";
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        when(request.getReader()).thenReturn(reader);

        TaskDto updatedTask = new TaskDto(2L, "1", null, 150);
        doReturn(null).when(taskService).update(updatedTask);

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        taskServlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}
