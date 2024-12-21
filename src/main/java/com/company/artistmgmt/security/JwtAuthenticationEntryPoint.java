package com.company.artistmgmt.security;

import com.company.artistmgmt.model.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        BaseResponse<String> errorResponse = new BaseResponse<>(
//                HttpStatus.UNAUTHORIZED.value(),
//                "UNAUTHORIZED"
//        );
//
//        OutputStream out = response.getOutputStream();
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule for LocalDateTime
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Ensure proper date-time formatting
//
//        mapper.writeValue(out, errorResponse);
//        out.flush();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        String message = "UNAUTHORIZED";  // Default message
        if (authException instanceof BadCredentialsException) {
            message = "Invalid credentials"; // For invalid credentials
        }

        BaseResponse<String> errorResponse = new BaseResponse<>(
                HttpStatus.UNAUTHORIZED.value(),
                message
        );

        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.writeValue(out, errorResponse);
        out.flush();
    }

}
