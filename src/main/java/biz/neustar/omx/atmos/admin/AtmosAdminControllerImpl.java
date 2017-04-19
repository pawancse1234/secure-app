package biz.neustar.omx.atmos.admin;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.neustar.omx.atmos.AllowedRoles;

/**
 * The Class AtmosAdminControllerImpl.
 */
@Path(value = "/routes")
public class AtmosAdminControllerImpl {

	private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(AtmosAdminControllerImpl.class);

	@GET
	@Path(value = "/logoutSucess")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response logoutSucess() {
		LOG.info("Entered into logout Sucess method.");
		// SecurityContextHolder.
		return Response.ok().type(MediaType.APPLICATION_JSON).entity("You have been logged out sucessfully!!!!")
				.build();
	}

	@GET
	@Path(value = "/getAllRoutes")
	@Produces({ MediaType.APPLICATION_JSON })
	@RolesAllowed({ AllowedRoles.ADMIN, AllowedRoles.APPS, AllowedRoles.ENGG, AllowedRoles.REFLOW })
	public Response getAllRoutes(@QueryParam("DNS_NAME") final String dnsName) {

		// Returns successful response.
		return Response.ok().header(ACCESS_CONTROL_ALLOW_ORIGIN, "*").type(MediaType.APPLICATION_JSON)
				.entity("getAllRoutes").build();
	}

	@GET
	@Path(value = "/reflowInitialData")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ AllowedRoles.ADMIN, AllowedRoles.APPS, AllowedRoles.REFLOW, AllowedRoles.ENGG })
	public Response getReflowInitialData() {
		LOG.info("Inside getReflowInitialData()");

		return Response.ok().header(ACCESS_CONTROL_ALLOW_ORIGIN, "*").type(MediaType.APPLICATION_JSON)
				.entity("getReflowInitialData").build();
	}

	@POST
	@Path(value = "/getRouteDefinition")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML })
	@RolesAllowed({ AllowedRoles.ADMIN, AllowedRoles.APPS, AllowedRoles.ENGG, AllowedRoles.REFLOW })
	public Response getRouteDefinition() {
		// Returns successful response back.
		return Response.ok().header(ACCESS_CONTROL_ALLOW_ORIGIN, "*").type(MediaType.APPLICATION_XML)
				.entity("getRouteDefinition").build();
	}

}
