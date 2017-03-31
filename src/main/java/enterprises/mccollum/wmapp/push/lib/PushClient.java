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
	
	@Enumerated(EnumType.STRING)
	PushClientType type;
	
	String registrationId;
	
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
}
