package enterprises.mccollum.wmapp.push;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import enterprises.mccollum.wmapp.authobjects.TestUser;
import enterprises.mccollum.wmapp.push.lib.PushClient;
import enterprises.mccollum.wmapp.push.lib.PushClientBean;
import enterprises.mccollum.wmapp.push.lib.PushClientType;
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
	 * @api {get} api/reg/clientTypes
	 * @apiName GetSupportedClientTypes
	 * @apiGroup Registration
	 * @apiDescription Returns a list of client types supported by the server
	 * 
	 * @apiSuccess {200} OK
	 * @apiSuccess {json} supportedClientTypes The list of client type strings supported
	 * 
	 * @return
	 */
	@GET
	@Path("clientTypes")
	public Response getSupportedClientTypes(){
		List<String> pushClientTypes = new ArrayList<>(PushClientType.values().length);
		for(PushClientType t : PushClientType.values()){
			pushClientTypes.add(t.toString());
		}
		return Response.ok(pushClientTypes).build();
	}
	
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
	@EmployeeTypesOnly({"*", TestUser.EMPLOYEE_TYPE})
	public Response registerClient(PushClient client){
		WMPrincipal principal = (WMPrincipal)seCtx.getUserPrincipal();
		
		client.setStudentId(principal.getToken().getStudentID());
		client.setUsername(principal.getToken().getUsername());
		client.setTokenId(principal.getToken().getTokenId());
		
		if(client.getId() == null || (client.getId() != null && !pushClients.containsKey(client.getId()))){
			client.setId(null);
			client = pushClients.persist(client);
		}else{
			client = pushClients.save(client);
		}
		return Response.status(Status.OK).entity(client).build();
	}
	
	/**
	 * List registrations for this device
	 * 
	 * @api {get} api/reg/client
	 * @apiName GetClientRegistration
	 * @apiGroup Registration
	 * @apiDescription This call retrieves the information about the push client associated with this device
	 * 
	 * @apiSuccess {200} OK The client was registered or updated successfully
	 * @apiSuccess {json} pushClient The new or updated instance of the PushClient
	 * 
	 * @return
	 */
	@GET
	@Path("client")
	@EmployeeTypesOnly({"*", TestUser.EMPLOYEE_TYPE})
	public Response getPushClient(){
		WMPrincipal principal = (WMPrincipal)seCtx.getUserPrincipal();
		List<PushClient> matching = pushClients.getThisClient(principal.getToken());
		if(matching.size() < 1)
			return Response.status(Status.NO_CONTENT).build(); //tell the client it's not registered and return a status of no content
		return Response.ok(matching).build(); //Get the PushClient information to the client
	}
	
	/**
	 * List registered devices for this user
	 * @return
	 */
	@GET
	@Path("allClients")
	@EmployeeTypesOnly({"*", TestUser.EMPLOYEE_TYPE})
	public Response getAllPushClients(){
		WMPrincipal principal = (WMPrincipal)seCtx.getUserPrincipal();
		List<PushClient> matching = pushClients.getUserDevices(principal.getToken().getStudentID());
		if(matching.size() < 1)
			return Response.status(Status.NO_CONTENT).build(); //tell the user they have no devices registered
		return Response.ok(matching).build();
	}
}
