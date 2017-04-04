package enterprises.mccollum.wmapp.push.lib;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import enterprises.mccollum.utils.genericentityejb.GenericPersistenceManager;
import enterprises.mccollum.wmapp.authobjects.UserToken;

@Local
@Stateless
public class PushClientBean extends GenericPersistenceManager<PushClient, Long> {
	public PushClientBean(){
		super(PushClient.class);
	}

	public List<PushClient> getUserDevices(Long studentId) {
		PushClient key = new PushClient();
		key.setStudentId(studentId);
		return getMatching(key);
	}
	
	public List<PushClient> getUserDevices(String username){
		PushClient key = new PushClient();
		key.setUsername(username);
		return getMatching(key);
	}

	public List<PushClient> getThisClient(UserToken token) {
		PushClient key = new PushClient();
		key.setStudentId(token.getStudentID());
		key.setTokenId(token.getTokenId());
		return getMatching(key);
	}
}
