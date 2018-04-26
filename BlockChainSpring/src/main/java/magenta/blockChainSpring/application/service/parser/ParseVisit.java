package magenta.blockChainSpring.application.service.parser;

import java.util.LinkedList;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import magenta.blockChainSpring.model.Visit;
import magenta.blockChainSpring.model.Items;

public class ParseVisit implements ParseAnsware {
	public static final Logger logger = LogManager.getLogger(ParseVisit.class);
	public ParseVisit() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
	}
	
	@Override
	public LinkedList<Items> execute(String arguments) {
		JSONParser parser = new JSONParser();
		LinkedList<Items> record = new LinkedList<Items>();
		try {
			JSONObject item = (JSONObject) parser.parse(arguments);		
				logger.info("keyset = + "+ item.keySet().toString());
				record.add(new Visit((String)item.get("Key"), (String)item.get("IDhash"), (String)item.get("agency"), (String)item.get("name"), (String)item.get("date")));

		} catch (ParseException e) {
			logger.info("error parsing jsonobject");
		}
		
		return record;
	}

}