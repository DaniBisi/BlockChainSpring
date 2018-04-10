package magenta.BlockChainSpring;

public class Employ implements Items{
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

	public Employ(String ID,String IdHash, String agency, String name,String date) {
		setiD(ID);
		idHash = IdHash;
		this.agency = agency;
		this.name = name;
		this.date = date;
	}

	public String getiD() {
		return iD;
	}

	public void setiD(String iD) {
		this.iD = iD;
	}
}
