package enterprises.mccollum.wmapp.push;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
	
	/**
	 * @api {post} api/push/device Push a notification to a specific device
	 * @apiName PostToDevice
	 * @apiGroup Push
	 * @apiDescription This call will send a notification to a device based on a PushClient ID passed in the header
	 *
	 * @apiHeader {Long} PushDest The Westmont Push Client ID number
	 * @apiParam {String} msg The message to send. This is the request body
	 *
	 * @apiSuccess {200} OK The push notification was sent
	 * 
	 */
	@POST
	@Path("device")
	@EmployeeTypesOnly({"*"})
	public Response push(@HeaderParam(PushClient.PUSH_DEST_HEADER)Long id, JsonObject msg){
		if(!pushClients.containsKey(id))
			return Response.status(Status.PRECONDITION_FAILED).build();
		pushUtils.sendToDevice(pushClients.get(id), msg);
		return Response.status(Status.OK).entity(msg).build();
	}
	
	/**
	 * @api {post} api/push/user/id/:student_id Push a notification to a specific user
	 * @apiName PostToUserId
	 * @apiGroup Push
	 * @apiDescription This call will send a notification to all of a user's devices based on a student ID
	 *
	 * @apiParam {Long} student_id The student ID of the person to send the message to
	 * @apiParam {String} msg The message to send. This is the request body
	 *
	 * @apiSuccess {200} OK The push notification was sent
	 * 
	 */
	@POST
	@Path("user/id/{studentId}")
	@EmployeeTypesOnly({"*"})
	public Response pushToUserId(@PathParam("studentId")Long studentId, JsonObject msg){
		if(!pushUtils.sendToUser(studentId, msg))
			return Response.status(Status.PRECONDITION_FAILED).build();
		return Response.status(Status.OK).entity(msg).build();
	}
	
	/**
	 * @api {post} api/push/user/username/:username Push a notification to a specific user
	 * @apiName PostToUsername
	 * @apiGroup Push
	 * @apiDescription This call will send a notification to all of a user's devices based on username
	 *
	 * @apiParam {String} username The username of the person to send the message to
	 * @apiParam {String} msg The message to send. This is the request body
	 *
	 * @apiSuccess {200} OK The push notification was sent
	 * 
	 */
	@POST
	@Path("user/username/{username}")
	@EmployeeTypesOnly({"*"})
	public Response pushToUserName(@PathParam("username")String username, JsonObject msg){
		if(!pushUtils.sendToUser(username, msg))
			return Response.status(Status.PRECONDITION_FAILED).build();
		return Response.status(Status.OK).entity(msg).build();
	}
	
	@POST
	@Path("multicastDevices")
	@EmployeeTypesOnly("*")
	public Response pushToUsers(@HeaderParam(PushClient.PUSH_DEST_HEADER)String users, String msg){
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}
	
	@POST
	@Path("multicastUsers")
	@EmployeeTypesOnly("*")
	public Response multicastUsers(@HeaderParam(PushClient.PUSH_DEST_HEADER)String users, String msg){
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}
}
