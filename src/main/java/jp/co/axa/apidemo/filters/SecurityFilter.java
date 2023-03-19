package jp.co.axa.apidemo.filters;

import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/api/v1/*")
public class SecurityFilter implements Filter {

    @Value("${api.security.apiKey}")
    private String apikey;

    private static final String API_KEY_PARAM = "X-Custom-ApiKey";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String reqApiKey = req.getHeader(API_KEY_PARAM);

        //Return 401 response if the API key is not matched
        if (!apikey.equals(reqApiKey)) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
