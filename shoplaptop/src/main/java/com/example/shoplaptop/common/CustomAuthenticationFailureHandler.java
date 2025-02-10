package com.example.shoplaptop.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        request.getSession().setAttribute("ERROR_MESSAGE", "Sai tài khoản hoặc mật khẩu!");

        System.out.println("ERROR_MESSAGE trước khi redirect: " + request.getSession().getAttribute("ERROR_MESSAGE"));
        super.setDefaultFailureUrl("/login");
        super.onAuthenticationFailure(request, response, exception);
    }
}
