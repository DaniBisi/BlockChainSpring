package magenta.BlockChainSpring;

import java.util.LinkedHashSet;
import java.util.Set;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

public class AppUser implements User {

	private String username;
	private String organization;
	private String mspId;
	private Enrollment enrollment;
	private LinkedHashSet<String> role;
	public AppUser(String username, String organization, String affiliation) {
		this.username = username;
		this.organization = organization;
		this.mspId = affiliation;
		this.role = new LinkedHashSet<String>();
	}

	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
	}
	public String getAccount() {
		
		return null;
	}

	public String getAffiliation() {
		
		return mspId;
	}

	public Enrollment getEnrollment() {
		
		return enrollment;
	}

	public String getMspId() {
		return mspId;
	}

	public String getName() {
		
		return username;
	}

	public Set<String> getRoles() {
		return role;
	}

	public void addRoles(String role) {
		
		this.role.add(role);
	}

	public String getOrganization() {
		return organization;
	}
	
	

}
