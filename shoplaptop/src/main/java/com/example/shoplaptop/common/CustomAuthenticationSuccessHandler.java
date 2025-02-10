package com.example.shoplaptop.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;


@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        request.getSession().setAttribute("SUCCESS_MESSAGE", "Đăng nhập thành công!");
//        RedirectAttributes redirectAttributes = (RedirectAttributes) request.getSession().getAttribute("redirectAttributes");
//        redirectAttributes.addFlashAttribute("SUCCESS_MESSAGE", "Đăng nhập thành công!");

        String redirectUrl = determineTargetUrl(request, authentication);

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
//        request.getSession().setAttribute("SUCCESS_MESSAGE", null);
    }

    private String determineTargetUrl(HttpServletRequest request, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean isCustomer = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CUSTOMER"));

        if (isAdmin) {
            return "/dashboard";
        } else if (isCustomer) {
            // Lấy URL từ session mà Customer truy cập trước đó
            String targetUrl = (String) request.getSession().getAttribute("REDIRECT_URL");
            request.getSession().removeAttribute("REDIRECT_URL");

            if (targetUrl == null || targetUrl.isEmpty() || targetUrl.contains("favicon.ico") || targetUrl.contains("error") || targetUrl.contains("dashboard")) {
                targetUrl = "/home";
            }
            return targetUrl;
        } else {
            return "/";
        }
    }
}
