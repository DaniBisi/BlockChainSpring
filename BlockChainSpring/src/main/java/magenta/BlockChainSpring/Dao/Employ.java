package magenta.BlockChainSpring.Dao;

import java.util.LinkedList;

public class Employ implements Items {
	private LinkedList<String> valName;
	private LinkedList<String> valList;
	private String idHash;
	private String agency;
	private String date;
	private String name;
	private String iD;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Employ(String ID, String IdHash, String agency, String name, String date) {
		setiD(ID);
		idHash = IdHash;
		this.agency = agency;
		this.name = name;
		this.date = date;
		this.valList = new LinkedList<String>();
		this.valName = new LinkedList<String>();
		
	}

	public String getiD() {
		return iD;
	}

	public void setiD(String iD) {
		this.iD = iD;
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
		valList.add(this.iD);
		valList.add(this.idHash);
		valList.add(this.agency);
		valList.add(this.date);
		valList.add(this.name);
		return valList;
	}

	@Override
	public LinkedList<String> getValName() {
		valName.add("ID");
		valName.add("IdHash");
		valName.add("agency");
		valName.add("date");
		valName.add("name");
		return valName;
	}
}
