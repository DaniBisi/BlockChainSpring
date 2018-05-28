package magenta.blockchainspring.application.model;

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
	private String affiliation;
	public AppUser(String username, String organization, String msp) {
		this.username = username;
		this.organization = organization;
		this.affiliation = organization;
		this.mspId = msp;
		this.role = new LinkedHashSet<>();
	}

	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
	}
	public String getAccount() {
		
		return null;
	}

	public String getAffiliation() {
		
		return affiliation;
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

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	
	

}
