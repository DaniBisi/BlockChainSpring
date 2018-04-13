package magenta.BlockChainSpring;

import static org.hamcrest.CoreMatchers.endsWith;

import java.util.LinkedList;

import com.google.common.collect.ObjectArrays;

public class commandFactory {

	private String query;
	private String args;

	public commandFactory(String query, String args) {
		this.query = query;
		this.args = args;

	}

	public String[] getFormattedQuery() {

		String[] formattedQ = { query };
		System.out.println("query before: " + query + args);
		if (!args.isEmpty()) {
			String[] arguments = args.split(" ");
			formattedQ = ObjectArrays.concat(formattedQ, arguments, String.class);
		}
		return formattedQ;
	}

	public ParseAnsware getCommandParser() {
		
		switch (query) {
		case "queryAllVisits":
			return new ParseAllVisits();
		case "queryAllCars":
			return new ParseAll();
		case "queryCar":
			return new ParseCar();
		case "queryVisit":
			return new ParseVisit();
		case "createVisit":
		return new ParseMessage();
		default:
			return new ParseAll();
		}
	}

}
