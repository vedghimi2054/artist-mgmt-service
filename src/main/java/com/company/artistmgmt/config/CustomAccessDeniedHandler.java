package com.company.artistmgmt.config;

import com.company.artistmgmt.model.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());  // 403 Forbidden
        response.setContentType("application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        String errorResponse = objectMapper.writeValueAsString(new BaseResponse<String>(
                HttpStatus.FORBIDDEN.value(),
                "Access Denied - You do not have permission to access this resource"
        ));

        response.getWriter().write(errorResponse);
        response.getWriter().flush();
    }
}
