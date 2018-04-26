package magenta.blockChainSpring.application.service.parser;

import java.util.LinkedList;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import magenta.blockChainSpring.application.model.Items;
import magenta.blockChainSpring.application.model.Visit;

public class ParseAllVisits implements ParseAnsware {
	private static final Logger logger = LogManager.getLogger(ParseAllVisits.class);
	public ParseAllVisits() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
	}
	
	@Override
	public LinkedList<Items> execute(String arguments) {
		JSONParser parser = new JSONParser();
		LinkedList<Items> record = new LinkedList<Items>();
		try {
			JSONArray jSonAnsware = (JSONArray) parser.parse(arguments);
			for (Object jSonO : jSonAnsware) {
				JSONObject item = (JSONObject) jSonO;			
				JSONObject itemProv = (JSONObject) item.get("Record"); 
				logger.info("keyset = + "+ item.keySet().toString());
				record.add(new Visit((String)item.get("Key"), (String)itemProv.get("IDhash"), (String)itemProv.get("agency"), (String)itemProv.get("name"), (String)itemProv.get("date")));
			}
		} catch (ParseException e) {
			logger.info("error parsing jsonobject");
		}
		
		return record;
	}

}
