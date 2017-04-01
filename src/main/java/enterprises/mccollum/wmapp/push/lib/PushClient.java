package enterprises.mccollum.wmapp.push.lib;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class PushClient {
	public static final String PUSH_DEST_HEADER = "PushDest";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id;
	
	Long studentId;
	
	String username;
	
	@Enumerated(EnumType.STRING)
	PushClientType type;
	
	String registrationId;
	
	Long lastPush;
	
	public PushClient(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public PushClientType getType() {
		return type;
	}

	public void setType(PushClientType type) {
		this.type = type;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returs the epoch time of the last push notification
	 * @return
	 */
	public Long getLastPush() {
		return lastPush;
	}

	/**
	 * Sets the epoch time of the last push notification
	 * @param lastPush
	 */
	public void setLastPush(Long lastPush) {
		this.lastPush = lastPush;
	}
	
	/**
	 * Updates the time of the last push notification to the current time
	 */
	public void updateLastPush(){
		setLastPush(System.currentTimeMillis());
	}
}
