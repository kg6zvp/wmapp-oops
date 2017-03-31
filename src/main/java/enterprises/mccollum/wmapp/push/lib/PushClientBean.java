package enterprises.mccollum.wmapp.push.lib;

import javax.ejb.Local;
import javax.ejb.Stateless;

import enterprises.mccollum.utils.genericentityejb.GenericPersistenceManager;

@Local
@Stateless
public class PushClientBean extends GenericPersistenceManager<PushClient, Long> {
	public PushClientBean(){
		super(PushClient.class);
	}
}
