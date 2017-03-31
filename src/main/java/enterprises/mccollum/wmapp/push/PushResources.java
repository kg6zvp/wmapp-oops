package enterprises.mccollum.wmapp.push;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import enterprises.mccollum.wmapp.push.lib.PushClient;
import enterprises.mccollum.wmapp.push.lib.PushClientBean;
import enterprises.mccollum.wmapp.ssauthclient.EmployeeTypesOnly;

@Path("push")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PushResources {
	@Inject
	PushUtils pushUtils;
	
	@Context
	SecurityContext seCtx;
	
	@Inject
	PushClientBean pushClients;
	
	@PUT
	@EmployeeTypesOnly("*")
	public Response push(@HeaderParam(PushClient.PUSH_DEST_HEADER)Long id, String msg){
		pushUtils.send(pushClients.get(id), msg);
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}
	
	@POST
	@Path("registerClient")
	@EmployeeTypesOnly("*")
	public Response registerClient(PushClient client){
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}
}
