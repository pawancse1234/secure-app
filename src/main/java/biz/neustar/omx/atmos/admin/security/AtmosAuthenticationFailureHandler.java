package biz.neustar.omx.atmos.admin.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * The Class AtmosAuthenticationFailureHandler.
 */
public class AtmosAuthenticationFailureHandler implements AuthenticationFailureHandler {
	Logger log = LoggerFactory.getLogger(AtmosAuthenticationFailureHandler.class);

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.info("Failed here because user not found");
	}

}
