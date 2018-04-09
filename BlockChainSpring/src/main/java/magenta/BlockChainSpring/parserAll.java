package magenta.BlockChainSpring;

import java.util.LinkedList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class parserAll implements command {
	private static final Logger logger = LogManager.getLogger(parserAll.class);
	
	@Override
	public LinkedList<Car> execute(String arguments){
		JSONParser parser = new JSONParser();
		LinkedList<Car> record = new LinkedList<Car>();
		try {
			JSONArray jSonAnsware = (JSONArray) parser.parse(arguments);
			for (Object jSonO : jSonAnsware) {
				JSONObject car = (JSONObject) jSonO;			
				JSONObject carProv = (JSONObject) car.get("Record"); 
				logger.info("keyset = + "+ car.keySet().toString());
				record.add(new Car((String)car.get("Key"), (String)carProv.get("colour"), (String)carProv.get("make"), (String)carProv.get("model"), (String)carProv.get("owner")));

			}
		} catch (ParseException e) {
			logger.info("error parsing jsonobject");
		}
		
		return record;
	}

}
