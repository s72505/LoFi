package com.lofi.filter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Set;

/**
 * Forces login before a protected page loads.
 * Ensures only authenticated users can access protected resources.
 */
public class AuthFilter implements Filter {

    /** 
     * URLs that do NOT require login. 
     * Includes login/register pages, static files, and servlets related to authentication.
     */
    private static final Set<String> PUBLIC_PATHS = Set.of(
        "/login.jsp",
        "/register.jsp",
        "/results.jsp",
        "/index.html",
        "/Login",          // servlet actions
        "/Register",
        "/img/",           // static resources
        "/css/",
        "/js/"
    );

    @Override
    public void doFilter(ServletRequest sreq, ServletResponse sres, FilterChain chain)
            throws IOException, ServletException {

        // Cast to HTTP-specific request and response
        HttpServletRequest  req = (HttpServletRequest)  sreq;
        HttpServletResponse res = (HttpServletResponse) sres;

        // Get the requested path (excluding context root)
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Check if user is logged in (valid session and userId exists)
        boolean loggedIn  = req.getSession(false) != null &&
                            req.getSession(false).getAttribute("userId") != null;

        // Check if request path is in the public allowlist
        boolean isPublic  = PUBLIC_PATHS.stream().anyMatch(path::startsWith);

        // Allow if user is logged in or accessing a public resource
        if (loggedIn || isPublic) {
            chain.doFilter(sreq, sres);         // let request continue
        } else {
            // Redirect to login page with error message if not authenticated
            res.sendRedirect(req.getContextPath() + "/login.jsp?err=loginRequired");
        }
    }

    // init() and destroy() methods are unused, but must be present in Filter interface

    @Override
    public void init(FilterConfig fc) {
        // No filter-specific initialization needed
    }

    @Override
    public void destroy() {
        // No cleanup required for this filter
    }
}
