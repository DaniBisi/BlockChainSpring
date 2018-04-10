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
	public LinkedList<Items> execute(String arguments){
		JSONParser parser = new JSONParser();
		LinkedList<Items> record = new LinkedList<Items>();
		try {
			JSONArray jSonAnsware = (JSONArray) parser.parse(arguments);
			for (Object jSonO : jSonAnsware) {
				JSONObject item = (JSONObject) jSonO;			
				JSONObject itemProv = (JSONObject) item.get("Record"); 
				logger.info("keyset = + "+ item.keySet().toString());
				record.add(new Car((String)item.get("Key"), (String)itemProv.get("colour"), (String)itemProv.get("make"), (String)itemProv.get("model"), (String)itemProv.get("owner")));

			}
		} catch (ParseException e) {
			logger.info("error parsing jsonobject");
		}
		
		return record;
	}

}
