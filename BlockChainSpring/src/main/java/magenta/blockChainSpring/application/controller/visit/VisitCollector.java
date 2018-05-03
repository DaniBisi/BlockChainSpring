package magenta.blockChainSpring.application.controller.visit;

public class VisitCollector {

	private String userName;
	private String agency;
	private String fileName;
	private String date;
	private String time;
	private String idVisit;

	public String getIdVisit() {
		return idVisit;
	}

	public void setIdVisit(String idVisit) {
		this.idVisit = idVisit;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
