package biz.neustar.omx.atmos.admin.security;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

public class MyRequestHeaderAuthenticationFilter extends RequestHeaderAuthenticationFilter {

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) {
		try {
			super.unsuccessfulAuthentication(request, response, failed);
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}

		// see comments in Servlet API around using sendError as an alternative
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}
}