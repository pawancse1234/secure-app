package biz.neustar.omx.atmos.admin.security;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.security.access.AccessDeniedException;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {

	@Override
	public Response toResponse(AccessDeniedException e) {
		return Response.status(Status.UNAUTHORIZED).entity("User Not Authorised to perform the activity").build();
	}

}