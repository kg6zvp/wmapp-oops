package enterprises.mccollum.wmapp.push;

import javax.ejb.Local;
import javax.ejb.Stateless;

import enterprises.mccollum.wmapp.push.lib.PushClient;
import enterprises.mccollum.wmapp.push.lib.PushClientType;
import enterprises.mccollum.wmapp.push.push_clients.LibFirebase;

@Local
@Stateless
public class PushUtils {

	public void send(PushClient dest, String msg) {
		PushClientType type = dest.getType();
		if(PushClientType.FIREBASE == type){
			LibFirebase.NotificationMessage objMsg = new LibFirebase.NotificationMessage(dest.getRegistrationId(), msg);
			LibFirebase.FCM.sendMessage(objMsg);
		}else if(PushClientType.MCCOLLUM_ENTERPRISES == type){
			//TODO: do it
		}
	}
}
