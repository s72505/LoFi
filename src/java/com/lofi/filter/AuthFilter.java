package com.lofi.filter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Set;

/**
 * Forces login before a protected page loads.
 */
public class AuthFilter implements Filter {

    /** URLs that do NOT need login (login, register, img, css, JS, etc.) */
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

        HttpServletRequest  req = (HttpServletRequest)  sreq;
        HttpServletResponse res = (HttpServletResponse) sres;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        boolean loggedIn  = req.getSession(false) != null &&
                            req.getSession(false).getAttribute("userId") != null;

        boolean isPublic  = PUBLIC_PATHS.stream().anyMatch(path::startsWith);

        if (loggedIn || isPublic) {
            chain.doFilter(sreq, sres);         // let request continue
        } else {
            res.sendRedirect(req.getContextPath() + "/login.jsp?err=loginRequired");
        }
    }

    // init()/destroy() not needed for simple filter

    @Override
    public void init(FilterConfig fc) {
    }

    @Override
    public void destroy() {
    }
}
