package magenta.blockChainSpring.application.model;

import java.util.LinkedList;

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

	public Visit(String ID, String IdHash, String agency, String name, String date, String time) {
		setIdVisit(ID);
		idHash = IdHash;
		this.agency = agency;
		this.userName = name;
		this.date = date;
		this.time = time;
		this.valList = new LinkedList<String>();
		this.valName = new LinkedList<String>();
		
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
	public LinkedList<String> getValList() {
		valList.add(this.idVisit);
		valList.add(this.idHash);
		valList.add(this.agency);
		valList.add(this.date);
		valList.add(this.time);
		valList.add(this.userName);
		return valList;
	}

	@Override
	public LinkedList<String> getValName() {
		valName.add("ID");
		valName.add("IdHash");
		valName.add("agency");
		valName.add("date");
		valName.add("time");
		valName.add("name");
		return valName;
	}
}
