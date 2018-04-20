package magenta.BlockChainSpring.Application;

import static org.hamcrest.CoreMatchers.endsWith;

import java.util.LinkedList;

import com.google.common.collect.ObjectArrays;

import magenta.BlockChainSpring.ParseAll;
import magenta.BlockChainSpring.ParseAllVisits;
import magenta.BlockChainSpring.ParseAnsware;
import magenta.BlockChainSpring.ParseCar;
import magenta.BlockChainSpring.ParseMessage;
import magenta.BlockChainSpring.ParseVisit;

public class commandFactory {

	private String query;
	private String args;

	public commandFactory(String query, String args) {
		this.query = query;
		this.args = args;

	}

	public String[] getFormattedQuery() {

		String[] formattedQ = { query };
		if (args!= null && !args.isEmpty()) {
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
