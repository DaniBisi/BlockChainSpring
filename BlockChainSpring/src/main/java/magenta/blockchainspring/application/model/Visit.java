package magenta.blockchainspring.application.model;

import java.util.LinkedList;
import java.util.List;

public class Visit implements Items {
	private LinkedList<String> valName;
	private LinkedList<String> valList;
	private String idHash;
	private String agency;
	private String date;
	private String time;
	private String userName;
	private String idVisit;
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String name) {
		this.userName = name;
	}

	public Visit(String iD, String idHash, String agency, String name, String date, String time) {
		setIdVisit(iD);
		this.idHash = idHash;
		this.agency = agency;
		this.userName = name;
		this.date = date;
		this.time = time;
		this.valList = new LinkedList<>();
		this.valName = new LinkedList<>();
		
	}

	public String getIdVisit() {
		return idVisit;
	}

	public void setIdVisit(String iD) {
		this.idVisit = iD;
	}

	public String getIdHash() {
		return idHash;
	}

	public void setIdHash(String idHash) {
		this.idHash = idHash;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public List<String> getValList() {
		valList.add(this.idVisit);
		valList.add(this.idHash);
		valList.add(this.agency);
		valList.add(this.date);
		valList.add(this.time);
		valList.add(this.userName);
		return valList;
	}

	@Override
	public List<String> getValName() {
		valName.add("ID");
		valName.add("IdHash");
		valName.add("agency");
		valName.add("date");
		valName.add("time");
		valName.add("name");
		return valName;
	}
}
