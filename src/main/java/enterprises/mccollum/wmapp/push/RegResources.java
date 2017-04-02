package enterprises.mccollum.wmapp.push;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
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
import enterprises.mccollum.wmapp.ssauthclient.WMPrincipal;

/**
 * @author smccollum
 */
@Path("reg")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegResources {
	@Inject
	PushClientBean pushClients;
	
	@Context
	SecurityContext seCtx;
	
	/**
	 * @api {post} api/reg/client
	 * @apiName PostClientRegistration
	 * @apiGroup Registration
	 * @apiDescription This call registers a device with the push notification server
	 * 
	 * @apiSuccess {200} OK The client was registered or updated successfully
	 * @apiSuccess {json} pushClient The new or updated instance of the PushClient
	 */
	@POST
	@Path("client")
	@EmployeeTypesOnly({"*", "test"})
	public Response registerClient(PushClient client){
		WMPrincipal principal = (WMPrincipal)seCtx.getUserPrincipal();
		if(client.getStudentId() == null)
			client.setStudentId(principal.getToken().getStudentID());
		if(client.getUsername() == null)
			client.setUsername(principal.getToken().getUsername());
		if(!client.getStudentId().equals(principal.getToken().getStudentID()) ||
				!client.getUsername().equals(principal.getToken().getUsername())){
			return Response.status(Status.FORBIDDEN).build();
		}
		if(client.getId() == null || (client.getId() != null && !pushClients.containsKey(client.getId()))){
			client.setId(null);
			client = pushClients.persist(client);
		}else{
			client = pushClients.save(client);
		}
		return Response.status(Status.OK).entity(client).build();
	}
}
