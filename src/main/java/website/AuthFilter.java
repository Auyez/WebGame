package website;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // /login.html and /services/auth don't require authentication
        if (request.getRequestURI().endsWith("/login.html") ||
            request.getRequestURI().endsWith("/services/auth/login") ||
            request.getRequestURI().endsWith("/services/auth/register")) {

            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }


        // Servlet cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("authToken")) {
                    String token = cookie.getValue();
                    if (token != null && AuthTokens.getInstance().isValid(token)) {
                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    }
                }
            }
        }

        response.sendRedirect("login.html");
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {

    }
}
