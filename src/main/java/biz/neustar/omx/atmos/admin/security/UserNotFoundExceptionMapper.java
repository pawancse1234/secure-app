package biz.neustar.omx.atmos.admin.security;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.security.authentication.BadCredentialsException;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class UserNotFoundExceptionMapper implements ExceptionMapper<BadCredentialsException> {

	@Override
	public Response toResponse(BadCredentialsException e) {
		return Response.status(Status.UNAUTHORIZED).entity(new String("Username/password incorrect!!!!")).build();
	}

}