package enterprises.mccollum.wmapp.push;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import enterprises.mccollum.wmapp.push.lib.PushClientBean;

@Singleton
@Startup
public class ClientExpirationBean {
	@Inject
	PushClientBean pushClients;
	
	@PostConstruct
	public void init(){
		
	}
	
	@Schedule(hour = "3", minute = "0", second = "0", persistent = true)
	public void expire(){
		//TODO: Actually expire stuff
	}
}
