package biz.neustar.omx.atmos.admin;

import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.security.core.context.SecurityContextHolder;

import biz.neustar.omx.atmos.AllowedRoles;

@Path(value = "/user")
public class UserManagement {
	@Path(value = "/login")
	@GET
	@RolesAllowed({ AllowedRoles.ADMIN, AllowedRoles.APPS, AllowedRoles.ENGG, AllowedRoles.REFLOW })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response loginAndReturnRole() {
		return Response.ok().header("Access-Control-Allow-Origin", "*")
				.type(MediaType.APPLICATION_JSON).entity(SecurityContextHolder.getContext().getAuthentication()
						.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList()))
				.build();
	}

	@Path(value = "/loginFailure")
	@GET
	// @RolesAllowed({ AllowedRoles.ADMIN, AllowedRoles.APPS, AllowedRoles.ENGG,
	// AllowedRoles.REFLOW })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response loginFailures() {
		return Response.status(401).header("Access-Control-Allow-Origin", "*").type(MediaType.APPLICATION_JSON)
				.entity("Authentication is required").build();
	}

}
