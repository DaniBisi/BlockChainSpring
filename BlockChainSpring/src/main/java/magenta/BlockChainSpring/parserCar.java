package magenta.BlockChainSpring;

import java.util.LinkedList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class parserCar implements command {

	private static final Logger logger = LogManager.getLogger(parserCar.class);
	@Override
	public LinkedList<Items> execute(String arguments) {
		JSONParser parser = new JSONParser();
		LinkedList<Items> record = new LinkedList<Items>();
		try {
			JSONObject jSonAnsware = (JSONObject) parser.parse(arguments);
			record.add(new Car((String)jSonAnsware.get("Key"), (String)jSonAnsware.get("colour"), (String)jSonAnsware.get("make"), (String)jSonAnsware.get("model"), (String)jSonAnsware.get("owner")));
			
		} catch (Exception e) {
			logger.info("error parsing jsonObject");
		}
		
			
		return record;
	}

}
