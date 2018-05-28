package magenta.blockchainspring.application.service.parser;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import magenta.blockchainspring.application.model.Items;
import magenta.blockchainspring.application.model.Visit;

public class ParseAllVisits implements ParseAnsware {
	private static final Logger logger = LogManager.getLogger(ParseAllVisits.class);
	public ParseAllVisits() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
	}
	
	@Override
	public List<Items> execute(String arguments) {
		JSONParser parser = new JSONParser();
		LinkedList<Items> record = new LinkedList<Items>();
		try {
			JSONArray jSonAnsware = (JSONArray) parser.parse(arguments);
			for (Object jSonO : jSonAnsware) {
				JSONObject item = (JSONObject) jSonO;			
				JSONObject itemProv = (JSONObject) item.get("Record"); 
				logger.info("keyset = + "+ item.keySet().toString());
				record.add(new Visit((String)item.get("Key"), (String)itemProv.get("IDhash"), (String)itemProv.get("agency"), (String)itemProv.get("name"), (String)itemProv.get("date"), (String)itemProv.get("time")));
			}
		} catch (ParseException e) {
			logger.info("error parsing jsonobject");
		}
		
		return record;
	}

}
