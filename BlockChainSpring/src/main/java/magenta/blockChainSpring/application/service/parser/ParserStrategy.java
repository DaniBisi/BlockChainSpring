package magenta.blockChainSpring.application.service.parser;

import org.springframework.stereotype.Service;

import com.google.common.collect.ObjectArrays;

@Service
public class ParserStrategy {

	public String[] getFormattedQuery(String query, String args) {

		String[] formattedQ = { query };
		if (args != null && !args.isEmpty()) {
			String[] arguments = args.split(" ");
			formattedQ = ObjectArrays.concat(formattedQ, arguments, String.class);
		}
		return formattedQ;
	}

	public ParseAnsware getCommandParser(String query) {

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