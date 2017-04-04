package enterprises.mccollum.wmapp.push;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;

import enterprises.mccollum.wmapp.push.lib.PushClient;
import enterprises.mccollum.wmapp.push.lib.PushClientBean;
import enterprises.mccollum.wmapp.push.lib.PushClientType;
import enterprises.mccollum.wmapp.push.push_clients.LibFirebase;

@Local
@Stateless
public class PushUtils {
	@Inject
	PushClientBean pushClientBean;

	/**
	 * Send a single message to a specific device
	 * @param dest
	 * @param msg
	 */
	public void sendToDevice(PushClient dest, JsonObject msg) {
		PushClientType type = dest.getType();
		if(PushClientType.FIREBASE == type){
			LibFirebase.NotificationMessage objMsg = new LibFirebase.NotificationMessage(dest.getRegistrationId(), msg);
			LibFirebase.FCM.sendMessage(objMsg);
		}else if(PushClientType.MCCOLLUM_ENTERPRISES == type){
			//TODO: do it
		}
		dest.updateLastPush(); //update last push to current time
		pushClientBean.save(dest); //save the last push time ot the database
	}
	
	/**
	 * Send a message to all of a users devices
	 * @param studentId
	 * @param msg
	 */
	public Boolean sendToUser(Long studentId, JsonObject msg){
		List<PushClient> devices = pushClientBean.getUserDevices(studentId);
		if(devices.size() < 1)
			return false;
		sendToUser(devices, msg);
		return true;
	}
	
	/**
	 * Send a message to all of a users devices
	 * @param username
	 * @param msg
	 */
	public Boolean sendToUser(String username, JsonObject msg){
		List<PushClient> devices = pushClientBean.getUserDevices(username);
		if(devices.size() < 1)
			return false;
		sendToUser(devices, msg);
		return true;
	}
	
	/**
	 * Send a message to all of a users devices
	 * @param devices
	 * @param msg
	 */
	private void sendToUser(List<PushClient> devices, JsonObject msg){
		for(PushClient client : devices){
			sendToDevice(client, msg);
		}
	}
}
