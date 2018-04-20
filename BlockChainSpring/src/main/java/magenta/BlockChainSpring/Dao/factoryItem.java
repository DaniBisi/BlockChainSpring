package magenta.BlockChainSpring.Dao;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import org.json.simple.JSONObject;

public class factoryItem {

	private Set keyset;

	public factoryItem(JSONObject item) {
		keyset = item.keySet();
		
	}

	public Items getItem() {
		Items i1;
		if(keyset.contains("lastInsertedId")){
			return null;
		}
		return null;
	}
	
	
}
