package magenta.BlockChainSpring;

import java.util.LinkedList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ParseAll implements ParseAnsware {
	private static final Logger logger = LogManager.getLogger(ParseAll.class);
	
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
				Message m1 = new Message();
				for (Object key : item.keySet()) {
					m1.addMessage(key.toString(), item.get(key).toString());
				}
				record.add(m1);
			}
		} catch (ParseException e) {
			logger.info("error parsing jsonobject");
		}
		
		return record;
	}

}
