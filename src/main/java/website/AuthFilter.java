package website;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthFilter implements Filter {
    public static boolean disabled = false;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException
    {
        if (disabled) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }


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
        if (!Database.connected())
            disabled = true;

        if (disabled)
            System.out.println("AuthFilter is disabled");
    }

    @Override
    public void destroy() {

    }
}
