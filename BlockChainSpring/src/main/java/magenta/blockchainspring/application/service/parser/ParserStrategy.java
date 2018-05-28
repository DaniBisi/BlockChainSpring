package magenta.blockchainspring.application.service.parser;

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
		case "queryVisit":
			return new ParseVisit();
		case "lastInsertedId":
			return new ParseAllVisits();
		case "createVisit":
			return new ParseMessage();
		default:
			return new ParseAllVisits();
		}
	}

}
