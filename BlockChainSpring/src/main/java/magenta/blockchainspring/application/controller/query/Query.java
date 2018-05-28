package magenta.blockchainspring.application.controller.query;

public class Query {
private String queryName;
private String args;
public String getArgs() {
	return args;
}
public void setArgs(String args) {
	this.args = args;
}
public String getQueryName() {
	return queryName;
}
public void setQueryName(String queryName) {
	this.queryName = queryName;
}

}
