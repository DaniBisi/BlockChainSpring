package magenta.blockchainspring.application.service.parser;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import magenta.blockchainspring.application.model.Items;
import magenta.blockchainspring.application.model.Visit;

public class ParseVisit implements ParseAnsware {
	public static final Logger logger = LogManager.getLogger(ParseVisit.class);
	public ParseVisit() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
	}
	
	@Override
	public List<Items> execute(String arguments) {
		JSONParser parser = new JSONParser();
		LinkedList<Items> record = new LinkedList<>();
		try {
			JSONObject item = (JSONObject) parser.parse(arguments);		
				logger.info("keyset = + "+ item.keySet().toString());
				record.add(new Visit((String)item.get("Key"), (String)item.get("IDhash"), (String)item.get("agency"), (String)item.get("name"), (String)item.get("date"), (String)item.get("time")));

		} catch (ParseException e) {
			logger.info("error parsing jsonobject");
		}
		
		return record;
	}

}
