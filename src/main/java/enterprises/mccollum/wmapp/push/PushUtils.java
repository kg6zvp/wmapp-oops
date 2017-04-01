package enterprises.mccollum.wmapp.push;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

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
	public void sendToDevice(PushClient dest, String msg) {
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
	public void sendToUser(Long studentId, String msg){
		List<PushClient> devices = pushClientBean.getUserDevices(studentId);
		sendToUser(devices, msg);
	}
	
	/**
	 * Send a message to all of a users devices
	 * @param username
	 * @param msg
	 */
	public void sendToUser(String username, String msg){
		List<PushClient> devices = pushClientBean.getUserDevices(username);
		sendToUser(devices, msg);
	}
	
	/**
	 * Send a message to all of a users devices
	 * @param devices
	 * @param msg
	 */
	private void sendToUser(List<PushClient> devices, String msg){
		for(PushClient client : devices){
			sendToDevice(client, msg);
		}
	}
}
